package com.kymjs.oslab.module.blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kymjs.api.Api;
import com.kymjs.common.ThreadSwitch;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;
import com.kymjs.model.utils.XmlUtil;
import com.kymjs.oslab.R;
import com.kymjs.oslab.base.BaseListFragment;
import com.kymjs.oslab.base.BasePullUpMultiTypeAdapter;
import com.kymjs.recycler.adapter.RecyclerHolder;
import com.kymjs.rxvolley.RxVolley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ZhangTao on 8/31/16.
 */
public class BlogFragment extends BaseListFragment<Blog, BlogListViewDelegate> {

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
    protected Class<BlogListViewDelegate> getDelegateClass() {
        return BlogListViewDelegate.class;
    }

    @Override
    protected BasePullUpMultiTypeAdapter<Blog> getAdapter() {
        return new BasePullUpMultiTypeAdapter<Blog>(viewDelegate.mRecyclerView, datas) {
            private static final int BLOG_ITEM_TEXT = 1;
            private static final int BLOG_ITEM_IMAGE = 2;

            @Override
            public int getItemTypeForPosition(int position) {
                if (TextUtils.isEmpty(datas.get(position).getImage().trim())) {
                    return BLOG_ITEM_TEXT;
                } else {
                    return BLOG_ITEM_IMAGE;
                }
            }

            @Override
            public RecyclerHolder onCreateViewHolderForType(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(cxt);
                if (viewType == BLOG_ITEM_TEXT) {
                    View root = inflater.inflate(R.layout.item_blog_text, parent, false);
                    return new RecyclerHolder(root);
                } else if (viewType == BLOG_ITEM_IMAGE) {
                    View root = inflater.inflate(R.layout.item_blog_image, parent, false);
                    return new RecyclerHolder(root);
                }
                return null;
            }

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

                String imageUrl = item.getImage().trim();
                ImageView imageView = holder.getView(R.id.item_blog_img);
                //其实应该使用的是getItemTypeForPosition(position) == BLOG_ITEM_IMAGE
                if (imageView != null) {
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
        BlogDetailActivity.goinActivity(view.getContext(), data.getLink(), data.getTitle());
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
