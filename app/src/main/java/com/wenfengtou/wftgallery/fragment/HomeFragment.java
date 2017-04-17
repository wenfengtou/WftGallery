package com.wenfengtou.wftgallery.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenfengtou.wftgallery.R;

/**
 * Created by 80119510 on 2017-04-12.
 */

public class HomeFragment extends Fragment {
    protected View mContainView;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Toolbar mToolBar;
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
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mSwipeRefreshLayout =(SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_ly);
        mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
