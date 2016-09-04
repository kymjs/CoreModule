package com.kymjs.browser;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.kymjs.browser.interf.ILinkDispatcher;
import com.kymjs.themvp.view.AppDelegate;


/**
 * 浏览器视图
 *
 * @author kymjs (http://www.kymjs.com/) on 12/5/15.
 */
public class BrowserDelegate extends AppDelegate {

    public LinearLayout mLayoutBottom;
    public WebView webView;
    private ILinkDispatcher linkDispatcher;

    private String currentUrl;

    @Override
    public int getRootLayoutId() {
        return R.layout.delegate_layout_browser;
    }

    public void setContentUrl(String url) {
        webView.loadUrl(url);
        currentUrl = url;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setLinkDispatcher(ILinkDispatcher linkDispatcher) {
        this.linkDispatcher = linkDispatcher;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        webView = get(R.id.webview);
        mLayoutBottom = (LinearLayout) View.inflate(getActivity(),
                R.layout.item_browser_bottombar, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .WRAP_CONTENT);
        params.leftMargin = 60;
        params.rightMargin = 60;
        params.bottomMargin = 30;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout) get(R.id.browser_root)).addView(mLayoutBottom, 1, params);
        mLayoutBottom.setVisibility(View.GONE);

        new BrowserDelegateOption(this, linkDispatcher).initWebView();
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.browser_menu_share;
    }
}
