package com.kymjs.oslab.module.blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.kymjs.api.Api;
import com.kymjs.common.ThreadSwitch;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;
import com.kymjs.model.utils.XmlUtil;
import com.kymjs.oslab.R;
import com.kymjs.oslab.base.BaseListFragment;
import com.kymjs.recycler.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.recycler.adapter.RecyclerHolder;
import com.kymjs.rxvolley.RxVolley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ZhangTao on 8/31/16.
 */
public class BlogFragment extends BaseListFragment<Blog> {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ThreadSwitch.get()
                .io(new ThreadSwitch.IO() {
                    @Override
                    public void run() {
                        byte[] cache = RxVolley.getCache(Api.BLOG_LIST);
                        if (cache != null && cache.length != 0) {
                            datas = parserInAsync(cache);
                        }
                    }
                })
                .ui(new ThreadSwitch.Function() {
                    @Override
                    public void run() {
                        adapter.refresh(datas);
                        viewDelegate.mEmptyLayout.dismiss();
                    }
                });
    }

    @Override
    protected BasePullUpRecyclerAdapter<Blog> getAdapter() {
        return new BasePullUpRecyclerAdapter<Blog>(viewDelegate.mRecyclerView, datas,
                R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, final Blog item, int position) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getDescription());
                holder.setText(R.id.item_blog_tv_author, item.getAuthor());
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());
                if (TextUtils.isEmpty(item.getRecommend())) {
                    holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.VISIBLE);
                }

                ImageView imageView = holder.getView(R.id.item_blog_img);
                String imageUrl = item.getImage().trim();
                if (TextUtils.isEmpty(imageUrl)) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(imageUrl).into(imageView);
                }
            }
        };
    }

    @Override
    protected ArrayList<Blog> parserInAsync(byte[] t) {
        return XmlUtil.toBean(BlogList.class, t).getChannel().getItemArray();
    }

    @Override
    protected void onItemClick(View view, Blog data, int position) {
        
    }

    @Override
    public void doRequest() {
        new RxVolley.Builder().url(Api.BLOG_LIST)
                .contentType(RxVolley.Method.GET)
                .cacheTime(600)
                .callback(callBack)
                .doTask();
    }
}
