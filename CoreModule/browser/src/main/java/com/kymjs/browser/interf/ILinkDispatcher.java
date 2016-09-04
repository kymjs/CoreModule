package com.kymjs.browser.interf;

import android.webkit.WebView;

/**
 * Created by ZhangTao on 9/4/16.
 */
public interface ILinkDispatcher {
    void dispatch(WebView view, String url);

    String getActionTitle(String url, String defTitle);

    String getHost(String url);
}
