package com.kymjs.oslab.module.blog;

import com.kymjs.base.viewdelegate.BaseListDelegate;
import com.kymjs.base.viewdelegate.PullListDelegate;
import com.kymjs.oslab.R;

/**
 * Created by ZhangTao on 9/4/16.
 */
public class BlogListViewDelegate extends PullListDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_blog;
    }
}
