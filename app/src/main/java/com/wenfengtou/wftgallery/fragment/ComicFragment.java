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
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        TieTuApi.getInstance().setCallback(new TieTuApi.Callback() {
            @Override
            public void onCallback(List<TieTuListBean> result, boolean success) {
                Log.i(TAG,"haha Call back "+success);
                if(success){
                    Log.i(TAG,"result is "+result.toString());
                    mAdapter.addData(result);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        loadData(1);
    }

    private void loadData(int pager) {
        TieTuApi.getInstance().syncGetTietuListByType();
    }

}
