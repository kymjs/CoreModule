package com.kymjs.oslab.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kymjs.base.fragment.BaseFragment;
import com.kymjs.base.viewdelegate.PullListDelegate;
import com.kymjs.common.Logger;
import com.kymjs.oslab.inter.IRequestVo;
import com.kymjs.recycler.RecyclerItemClickListener;
import com.kymjs.recycler.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.view.EmptyLayout;

import java.util.ArrayList;

/**
 * Created by ZhangTao on 7/2/16.
 */
public abstract class BaseListFragment<T, D extends PullListDelegate> extends BaseFragment<D> implements
        SwipeRefreshLayout.OnRefreshListener, IRequestVo {

    protected BasePullUpRecyclerAdapter<T> adapter;
    protected ArrayList<T> datas = new ArrayList<>();

    protected abstract BasePullUpRecyclerAdapter<T> getAdapter();

    protected abstract ArrayList<T> parserInAsync(byte[] t);

    protected HttpCallback callBack = new HttpCallback() {
        private ArrayList<T> tempDatas;

        @Override
        public void onSuccessInAsync(byte[] t) {
            super.onSuccessInAsync(t);
            try {
                tempDatas = parserInAsync(t);
            } catch (Exception e) {
                tempDatas = null;
            }
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            Logger.log("===列表网络请求:" + t);
            if (viewDelegate != null && viewDelegate.mEmptyLayout != null) {
                if (tempDatas == null || tempDatas.isEmpty() || adapter == null || adapter
                        .getItemCount() < 1) {
                    viewDelegate.mEmptyLayout.setErrorType(EmptyLayout.NODATA);
                } else {
                    viewDelegate.mEmptyLayout.dismiss();
                    adapter.refresh(tempDatas);
                    datas = tempDatas;
                }
            }
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
            Logger.log("====网络请求异常" + strMsg);
            //有可能界面已经关闭网络请求仍然返回
            if (viewDelegate != null && viewDelegate.mEmptyLayout != null && adapter != null) {
                if (adapter.getItemCount() > 1) {
                    viewDelegate.mEmptyLayout.dismiss();
                } else {
                    viewDelegate.mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (viewDelegate != null) {
                viewDelegate.setSwipeRefreshLoadedState();
            }
        }
    };

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        adapter = getAdapter();
        bindEven();
        viewDelegate.setOnRefreshListener(this);
        doRequest();
    }

    private void bindEven() {
        viewDelegate.mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                viewDelegate.mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            }
        });
        viewDelegate.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //滚动到底部的监听
                    LinearLayoutManager layoutManager = viewDelegate.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastItems = layoutManager.findFirstVisibleItemPosition();
                    if ((pastItems + visibleItemCount) >= totalItemCount) {
                        onBottom();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        viewDelegate.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext()) {
            @Override
            protected void onItemClick(View view, int position) {
                BaseListFragment.this.onItemClick(view, datas.get(position), position);
            }
        });
        viewDelegate.mRecyclerView.setAdapter(adapter);
    }

    public void onBottom() {
        adapter.setState(BasePullUpRecyclerAdapter.STATE_NO_MORE);
    }

    @Override
    public void onRefresh() {
        viewDelegate.setSwipeRefreshLoadingState();
        doRequest();
    }

    protected abstract void onItemClick(View view, T data, int position);
}
