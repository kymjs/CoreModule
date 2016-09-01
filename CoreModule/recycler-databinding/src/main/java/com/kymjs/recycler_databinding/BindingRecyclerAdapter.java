package com.kymjs.recycler_databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by ZhangTao on 9/1/16.
 */
public abstract class BindingRecyclerAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<BindingViewHolder<T>> {

    protected final int mItemLayoutId;
    protected Context cxt;
    public T binding;

    public BindingRecyclerAdapter(Context context, int itemLayoutId) {
        mItemLayoutId = itemLayoutId;
        cxt = context;
    }

    @Override
    public BindingViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cxt);
        binding = DataBindingUtil.inflate(inflater, mItemLayoutId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<T> holder, int position) {

    }
}
