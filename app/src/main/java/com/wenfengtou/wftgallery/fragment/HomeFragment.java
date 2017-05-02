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

/**
 * Created by 80119510 on 2017-04-12.
 */

public class HomeFragment extends Fragment {
    protected View mContainView;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    HomeAdapter mAdapter;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    //是否载入更多状态
    private boolean isLoadingMore = false;
    //当前页数
    private int currentPager = 1;
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
        loadData(currentPager);
    }

    int[] lastPositions;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //但RecyclerView滑动到倒数第三个之请求加载更多
            if (lastPositions == null) {
                Log.i("wenfengtou","getSpanCount"+mStaggeredGridLayoutManager.getSpanCount());
                lastPositions = new int[mStaggeredGridLayoutManager.getSpanCount()];
            }
            int[] lastVisibleItem = mStaggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            Log.i("wenfengtou","lastVisibleItem[0 1] is "+lastVisibleItem[0]+" "+lastVisibleItem[1]);
            int totalItemCount = mAdapter.getItemCount();
            // dy>0 表示向下滑动
            if (lastVisibleItem[0] >= totalItemCount - 4 && !isLoadingMore && dy > 0 ) {
                requestMoreData();
                Log.i("wenfengtou","requestMoreData ");
            }
        }
    };

    private void loadData(int pager) {
        Log.i("wenfeng","HomeFragment loadData");
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
        Log.i("wenfeng","requestMoreData");
        isLoadingMore = true;
        loadData(++currentPager);
    }

    private Observer<Data> dataObservable = new Observer<Data>(){

        @Override
        public void onCompleted() {
            Log.i("wenfeng","onCompleted");
            isLoadingMore = false;
        }

        @Override
        public void onError(Throwable e) {
            Log.i("wenfeng","onError");
           // mSwipeRefreshLayout.setRefreshing(false);
            isLoadingMore = false;
        }

        @Override
        public void onNext(Data data) {
            Log.i("wenfeng","onNext");
            if(data != null && data.getResults() != null){
                mAdapter.addData(data.getResults());
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
