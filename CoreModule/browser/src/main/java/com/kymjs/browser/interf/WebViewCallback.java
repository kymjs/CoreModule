package com.kymjs.browser.interf;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.kymjs.browser.R;

/**
 * Created by ZhangTao on 6/29/16.
 */
public class WebViewCallback {

    public int[] getRefreshLayoutColor() {
        return new int[]{
                R.color.browser_swiperefresh_color1,
                R.color.browser_swiperefresh_color2,
                R.color.browser_swiperefresh_color3,
                R.color.browser_swiperefresh_color4
        };
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public void onProgressChanged(WebView view, int newProgress) {
    }

    public void onReceivedTitle(WebView view, String title) {
    }

    public void onPageFinished(WebView view, String url) {
    }

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    }

    public void initWebView(WebView view) {
    }
}
