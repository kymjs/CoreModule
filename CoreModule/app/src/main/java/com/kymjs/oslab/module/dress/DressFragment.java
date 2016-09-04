package com.kymjs.oslab.module.dress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kymjs.browser.BrowserFragment;

/**
 * Created by ZhangTao on 9/4/16.
 */
public class DressFragment extends BrowserFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.loadUrl("http://www.uullnn.com/articles");
    }
}
