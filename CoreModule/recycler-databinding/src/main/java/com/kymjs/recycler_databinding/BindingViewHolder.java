package com.kymjs.recycler_databinding;

import android.databinding.ViewDataBinding;

import com.kymjs.recycler.adapter.RecyclerHolder;

/**
 * Created by ZhangTao on 8/15/16.
 */
public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerHolder {

    protected final T mBinding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }
}