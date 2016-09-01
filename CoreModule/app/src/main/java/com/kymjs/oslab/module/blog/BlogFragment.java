package com.kymjs.oslab.module.blog;

import android.view.View;

import com.kymjs.api.Api;
import com.kymjs.model.Blog;
import com.kymjs.model.BlogList;
import com.kymjs.model.utils.XmlUtil;
import com.kymjs.oslab.base.BaseListFragment;
import com.kymjs.recycler.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.rxvolley.RxVolley;

import java.util.ArrayList;

/**
 * Created by ZhangTao on 8/31/16.
 */
public class BlogFragment extends BaseListFragment<Blog> {

    @Override
    protected BasePullUpRecyclerAdapter<Blog> getAdapter() {
        return null;
    }

    @Override
    protected ArrayList<Blog> parserInAsync(byte[] t) {
        return XmlUtil.toBean(BlogList.class, t).getChannel().getItemArray();
    }

    @Override
    public void doRequest() {
        new RxVolley.Builder().url(Api.BLOG_LIST)
                .contentType(RxVolley.Method.GET)
                .cacheTime(600)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {

    }
}
