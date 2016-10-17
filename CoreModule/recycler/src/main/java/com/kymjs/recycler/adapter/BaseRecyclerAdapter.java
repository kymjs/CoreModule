package com.kymjs.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kymjs.recycler.OnItemClickListener;
import com.kymjs.recycler.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerView的万能适配器，适配任何一个RecyclerView
 *
 * @author kymjs (http://www.kymjs.com/) on 8/27/15.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    protected List<T> realDatas;
    protected final int mItemLayoutId;
    protected Context cxt;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, Collection<T> datas, int itemLayoutId) {
        if (datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        mItemLayoutId = itemLayoutId;
        cxt = context;
    }

    /**
     * Recycler适配器填充方法
     *
     * @param holder viewholder
     * @param item   javabean
     */
    public abstract void convert(RecyclerHolder holder, T item, int position);

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cxt);
        View root = inflater.inflate(mItemLayoutId, parent, false);
        return new RecyclerHolder(root);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {
        convert(holder, realDatas.get(position), position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return realDatas.size();
    }

    public BaseRecyclerAdapter<T> refresh(Collection<T> datas) {
        if (realDatas != null && !realDatas.isEmpty()) {
            //去重
            for (T data : datas) {
                if (!realDatas.contains(data)) realDatas.add(data);
            }
        } else {
            if (datas == null) {
                return this;
            } else if (datas instanceof List) {
                realDatas = (List<T>) datas;
            } else {
                realDatas = new ArrayList<>(datas);
            }
        }
        notifyDataSetChanged();
        return this;
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

}
