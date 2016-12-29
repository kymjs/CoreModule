package com.kymjs.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * Created by ZhangTao on 12/27/16.
 */

public abstract class BasePullMultiTypeRecyclerAdapter<T> extends BasePullUpRecyclerAdapter<T> {

    public BasePullMultiTypeRecyclerAdapter(RecyclerView v, Collection<T> datas) {
        super(v, datas, 0);
    }

    public abstract int getItemType(int position);

    public abstract int getItemIdByType(int viewType);

    /**
     * Recycler适配器填充方法
     *
     * @param holder   viewholder
     * @param item     javabean
     * @param viewType type
     */
    public abstract void convert(RecyclerHolder holder, T item, int position, int viewType);

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (type == TYPE_ITEM) {
            type = getItemType(position);
        }
        return type;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return super.onCreateViewHolder(parent, viewType);
        } else {
            LayoutInflater inflater = LayoutInflater.from(cxt);
            View root = inflater.inflate(getItemIdByType(viewType), parent, false);
            return new RecyclerHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (position == getItemCount() - 1) {
            //因为已经在footerview写死了，所以这里就不用再去设置
            //没有数据的时候也不显示footer
            if (position == 0) {
                getFooterView().setVisibility(View.GONE);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void convert(RecyclerHolder holder, T item, int position) {
        convert(holder, item, position, getItemViewType(position));
    }
}
