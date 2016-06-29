package com.kymjs.browser;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.kymjs.browser.interf.OnWebViewImageListener;
import com.kymjs.browser.interf.WebViewCallback;
import com.kymjs.gallery.KJGalleryActivity;
import com.kymjs.view.EmptyLayout;

/**
 * Created by ZhangTao on 6/28/16.
 */
public class BrowserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public WebView mWebView;
    public View mRootView;
    public ProgressBar mProgress;
    public EmptyLayout mEmptyLayout;
    public SwipeRefreshLayout mRefreshLayout;
    private Activity aty;

    private WebViewCallback callback;

    public void setCallback(WebViewCallback callback) {
        this.callback = callback;
    }

    public boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        aty = getActivity();
        mRootView = View.inflate(aty, R.layout.browser_fragment_layout, null);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.browser_swiperefresh);
        mWebView = (WebView) mRootView.findViewById(R.id.browser_webview);
        mEmptyLayout = (EmptyLayout) mRootView.findViewById(R.id.browser_emptyview);
        mProgress = (ProgressBar) mRootView.findViewById(R.id.browser_progress);

        if (callback != null) {
            mRefreshLayout.setColorSchemeResources(callback.getRefreshLayoutColor());
        }
        mRefreshLayout.setOnRefreshListener(this);
        mEmptyLayout.dismiss();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeeClient());
        initWebView(mWebView);
        return mRootView;
    }

    @Override
    public void onRefresh() {
        setSwipeRefreshLoadingState();
        mRefreshLayout.setEnabled(false);
        mWebView.reload();
        mRefreshLayout.setEnabled(true);
    }

    public void setSwipeRefreshLoadingState() {
        mRefreshLayout.setRefreshing(true);
    }

    public void setSwipeRefreshLoadedState() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    private class MyWebChromeeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            mProgress.setProgress(newProgress);
            if (newProgress > 80) {
                setSwipeRefreshLoadedState();
                mProgress.setVisibility(View.GONE);
            }
            if (callback != null) {
                callback.onProgressChanged(view, newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (callback != null) {
                callback.onReceivedTitle(view, title);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
            }
            if (callback != null) {
                callback.onPageFinished(view, url);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mEmptyLayout.setErrorType(EmptyLayout.NODATA);
            if (callback != null) {
                callback.onReceivedError(view, request, error);
            }
        }

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return super.shouldOverrideUrlLoading(view, request);
//        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return callback != null
                    && callback.shouldOverrideUrlLoading(view, url)
                    || super.shouldOverrideUrlLoading(view, url);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        addWebImageShow(webView.getContext(), webView);
    }

    /**
     * 添加网页的点击图片展示支持
     */
    @JavascriptInterface
    @SuppressLint("JavascriptInterface")
    public static void addWebImageShow(final Context cxt, WebView wv) {
        wv.addJavascriptInterface(new OnWebViewImageListener() {
            @Override
            @JavascriptInterface
            public void showImagePreview(String bigImageUrl) {
                if (!TextUtils.isEmpty(bigImageUrl)) {
                    KJGalleryActivity.toGallery(cxt, bigImageUrl);
                }
            }
        }, "mWebViewImageListener");
    }

    /**
     * 设置html中图片支持点击预览
     *
     * @param body html内容
     * @return 修改后的内容
     */
    public static String setImagePreview(String body) {
        // 过滤掉 img标签的width,height属性
        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
        // 添加点击图片放大支持
        body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                "$1$2\" onClick=\"mWebViewImageListener.showImagePreview('$2')\"");
        return body;
    }
}
