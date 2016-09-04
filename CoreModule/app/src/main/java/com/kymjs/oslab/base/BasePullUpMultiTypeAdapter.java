package com.kymjs.oslab.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.kymjs.recycler.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.recycler.adapter.RecyclerHolder;

import java.util.Collection;

/**
 * Created by ZhangTao on 9/4/16.
 */
public abstract class BasePullUpMultiTypeAdapter<T> extends BasePullUpRecyclerAdapter<T> {
    public BasePullUpMultiTypeAdapter(RecyclerView v, Collection<T> datas) {
        super(v, datas, 0);
    }

    public abstract int getItemTypeForPosition(int position);

    public abstract RecyclerHolder onCreateViewHolderForType(ViewGroup parent, int viewType);


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return getItemTypeForPosition(position);
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return super.onCreateViewHolder(parent, viewType);
        } else {
            return onCreateViewHolderForType(parent, viewType);
        }
    }
}
