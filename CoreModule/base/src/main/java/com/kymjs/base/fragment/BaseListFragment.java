package com.kymjs.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kymjs.base.R;
import com.kymjs.view.EmptyLayout;

/**
 * Created by ZhangTao on 7/2/16.
 */
public class BaseListFragment extends BaseFragment {

    public EmptyLayout emptyLayout;
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(getActivity(), R.layout.base_list_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.base_swiperefresh_color1,
                R.color.base_swiperefresh_color2,
                R.color.base_swiperefresh_color3,
                R.color.base_swiperefresh_color4);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        emptyLayout = (EmptyLayout) getView().findViewById(R.id.emptyLayout);
    }
}
