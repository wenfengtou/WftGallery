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
    Toolbar mToolBar;
    HomeAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("wenfeng","HomeFragment onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("wenfeng","HomeFragment onCreateView");
        if(mContainView == null) {
            mContainView = inflater.inflate(R.layout.fragment_meizi, container, false);
        }
        return mContainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("wenfeng","HomeFragment onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new HomeAdapter(getActivity());
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mSwipeRefreshLayout =(SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_ly);
        //mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        loadData(1);
    }

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

    private Observer<Data> dataObservable = new Observer<Data>(){

        @Override
        public void onCompleted() {
            Log.i("wenfeng","数据onCompleted 停止刷新");
        }

        @Override
        public void onError(Throwable e) {
            Log.i("wenfeng","onError 停止刷新");
           // mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(Data data) {
            Log.i("wenfeng","onNext " + data.toString());
            if(data != null && data.getResults() != null){
                mAdapter.addData(data.getResults());
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
