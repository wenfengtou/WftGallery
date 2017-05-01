package com.wenfengtou.wftgallery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenfengtou.wftgallery.R;
import com.wenfengtou.wftgallery.adapter.HomeAdapter;

/**
 * Created by wenfengtou on 17/5/1.
 */

public class ComicFragment extends Fragment{
    protected View mContainView;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    HomeAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContainView == null) {
            mContainView = inflater.inflate(R.layout.fragment_comic, container, false);
        }
        return mContainView;    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
