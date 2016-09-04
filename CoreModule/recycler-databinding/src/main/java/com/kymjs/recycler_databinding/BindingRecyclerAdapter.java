package com.kymjs.recycler_databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kymjs.recycler.adapter.RecyclerHolder;

/**
 * Created by ZhangTao on 9/1/16.
 */
public abstract class BindingRecyclerAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder> {

    protected final int mItemLayoutId;
    protected Context cxt;
    public T binding;

    public BindingRecyclerAdapter(Context context, int itemLayoutId) {
        mItemLayoutId = itemLayoutId;
        cxt = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cxt);
        binding = DataBindingUtil.inflate(inflater, mItemLayoutId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        
    }
}
