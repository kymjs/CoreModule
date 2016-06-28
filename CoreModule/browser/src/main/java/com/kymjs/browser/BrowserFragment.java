package com.kymjs.browser;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kymjs.view.EmptyLayout;

/**
 * Created by ZhangTao on 6/28/16.
 */
public class BrowserFragment extends Fragment {

    public WebView mWebView;
    public View mRootView;
    public ProgressBar mProgress;
    public EmptyLayout mEmptyLayout;
    private String mCurrentUrl;
    private Activity aty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        aty = getActivity();
        mRootView = View.inflate(aty, R.layout.browser_fragment_layout, null);
        mWebView = (WebView) mRootView.findViewById(R.id.browser_webview);
        mEmptyLayout = (EmptyLayout) mRootView.findViewById(R.id.browser_emptyview);
        mProgress = (ProgressBar) mRootView.findViewById(R.id.browser_progress);
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    /**
     * 载入链接之前会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlLoading(WebView view, String url) {
        mProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 链接载入成功后会被调用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlFinished(WebView view, String url) {
        mCurrentUrl = url;
        mProgress.setVisibility(View.GONE);
    }

    /**
     * 当前WebView显示页面的标题
     *
     * @param view  WebView
     * @param title web页面标题
     */
    protected void onWebTitle(WebView view, String title) {
        if (mWebView != null) { // 必须做判断，由于webview加载属于耗时操作，可能会本Activity已经关闭了才被调用
//            mTvTitle.setText(mWebView.getTitle());
        }
    }

    /**
     * 当前WebView显示页面的图标
     *
     * @param view WebView
     * @param icon web页面图标
     */
    protected void onWebIcon(WebView view, Bitmap icon) {
    }

    /**
     * 初始化浏览器设置信息
     */
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用支持javascript
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        webSettings.setAllowFileAccess(true);// 可以访问文件
        webSettings.setBuiltInZoomControls(true);// 支持缩放
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onWebTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            onWebIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (newProgress > 60) {
                mEmptyLayout.dismiss();
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            onUrlLoading(view, url);
            boolean flag = super.shouldOverrideUrlLoading(view, url);
            mCurrentUrl = url;
            return flag;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onUrlFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
//            ViewInject.toast("没有找到数据");
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        }
    }
}
