package com.wenfengtou.wftgallery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenfengtou.wftgallery.R;
import com.wenfengtou.wftgallery.adapter.ComicAdapter;
import com.wenfengtou.wftgallery.api.tietu.TieTuApi;
import com.wenfengtou.wftgallery.api.tietu.TieTuListBean;
import com.wenfengtou.wftgallery.api.tietu.TietuUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by wenfengtou on 17/5/1.
 */

public class ComicFragment extends Fragment{
    private static final String TAG = "ComicFragment";
    protected View mContainView;
    private  RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ComicAdapter mAdapter;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    //是否载入更多状态
    private boolean isLoadingMore = false;
    //当前页数
    private int currentPager = 1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG,"oncreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContainView == null) {
            mContainView = inflater.inflate(R.layout.fragment_comic, container, false);
        }
        return mContainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ComicAdapter(getActivity());
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.comic_recycler_view);
        mSwipeRefreshLayout =(SwipeRefreshLayout) getActivity().findViewById(R.id.comic_swipe_ly);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        TieTuApi.getInstance().setCallback(new TieTuApi.Callback() {
            @Override
            public void onCallback(List<TieTuListBean> result, boolean success) {
                Log.i(TAG,"haha Call back "+success);
                isLoadingMore = false;
                if(success){
                    Log.i(TAG,"result is "+result.toString());
                    mAdapter.addData(result);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
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

    private void loadData(int page) {
        TieTuApi.getInstance().syncGetTietuListByType(page);
    }

    private void requestMoreData(){
        Log.i("wenfeng","requestMoreData");
        isLoadingMore = true;
        loadData(++currentPager);
    }

}
