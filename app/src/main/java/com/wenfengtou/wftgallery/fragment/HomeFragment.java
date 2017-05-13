package com.wenfengtou.wftgallery.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenfengtou.wftgallery.R;
import com.wenfengtou.wftgallery.adapter.HomeAdapter;
import com.wenfengtou.wftgallery.bean.Data;
import com.wenfengtou.wftgallery.net.WftgalleryApi;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.wenfengtou.lrucache.DiskLruCacheManager;

import java.io.IOException;


/**
 * Created by 80119510 on 2017-04-12.
 */

public class HomeFragment extends Fragment  implements  SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "HomeFragment";
    protected View mContainView;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    HomeAdapter mAdapter;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private DiskLruCacheManager mDiskLruCacheManager;

    //是否载入更多状态
    private boolean isLoadingMore = false;
    //是否刷新状态
    private boolean isLoadingNewData = false;
    //当前页数
    private int currentPager = 1;
    //是否已经载入去全部
    private boolean isALlLoad = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContainView == null) {
            mContainView = inflater.inflate(R.layout.fragment_meizi, container, false);
        }
        return mContainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new HomeAdapter(getActivity());
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mSwipeRefreshLayout =(SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_ly);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        try {
            Log.i(TAG, "DiskLruCacheManager 创建");
            mDiskLruCacheManager = new DiskLruCacheManager(getActivity());
            Data data = mDiskLruCacheManager.getAsSerializable(TAG);
            Log.i(TAG, "DiskLruCacheManager 读取");
            if (data != null){
                Log.i(TAG, "获取到缓存数据");
                mAdapter.setData(data.getResults());
                mAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //疑问，为什么获取到缓存之后还有再从网络中刷新呢？
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = false;
        isLoadingNewData = true;
        loadData(1);
    }
    
    int[] lastPositions;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //但RecyclerView滑动到倒数第三个之请求加载更多
            if (lastPositions == null) {
                Log.i(TAG,"getSpanCount"+mStaggeredGridLayoutManager.getSpanCount());
                lastPositions = new int[mStaggeredGridLayoutManager.getSpanCount()];
            }
            int[] lastVisibleItem = mStaggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            Log.i(TAG,"lastVisibleItem[0 1] is "+lastVisibleItem[0]+" "+lastVisibleItem[1]);
            int totalItemCount = mAdapter.getItemCount();
            // dy>0 表示向下滑动
            if (lastVisibleItem[0] >= totalItemCount - 4 && !isLoadingMore && dy > 0 ) {
                requestMoreData();
                Log.i(TAG,"requestMoreData ");
            }
        }
    };

    private void loadData(int pager) {
        Log.i(TAG,"HomeFragment loadData");
        WftgalleryApi.getInstance()
                .getWebService()
                .getBenefitsGoods(WftgalleryApi.LOAD_LIMIT,pager)
             // .compose(this.<Data>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataObservable);
    }

    private void requestMoreData(){
        Log.i(TAG,"requestMoreData");
        isLoadingMore = true;
        isLoadingNewData = false;
        loadData(++currentPager);
    }

    private Observer<Data> dataObservable = new Observer<Data>(){

        @Override
        public void onCompleted() {
            Log.i(TAG,"onCompleted");
            mSwipeRefreshLayout.setRefreshing(false);
            isLoadingMore = false;
            isLoadingNewData = false;
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG,"onError");
            mSwipeRefreshLayout.setRefreshing(false);
            isLoadingMore = false;
        }

        @Override
        public void onNext(Data data) {
            Log.i(TAG,"onNext");

            if (data.getResults().size() < WftgalleryApi.LOAD_LIMIT) {
                isALlLoad = true;
                //showSnackbar(R.string.no_more);
            }
            
            if(data != null && data.getResults() != null){
                if(isLoadingMore){
                    mAdapter.addData(data.getResults());
                } else if (isLoadingNewData) {
                    isALlLoad = false;
                    mAdapter.setData(data.getResults());
                    Log.i(TAG, "DiskLruCacheManager 写入");
                    mDiskLruCacheManager.put(TAG,data);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    
}
