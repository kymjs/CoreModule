package com.kymjs.oslab.module.blog;

import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kymjs.api.Api;
import com.kymjs.browser.BrowserDelegate;
import com.kymjs.oslab.R;
import com.kymjs.oslab.base.BaseDetailDelegate;
import com.kymjs.oslab.utils.LinkDispatcher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * 开源实验室博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class BlogDetailDelegate extends BaseDetailDelegate {

    private BrowserDelegate browserDelegate = new BrowserDelegate();

    @Override
    public int getContentLayoutId() {
        return browserDelegate.getRootLayoutId();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initActionbarImage();
        browserDelegate.setRootView(rootView);
        browserDelegate.setLinkDispatcher(new LinkDispatcher());
        browserDelegate.initWidget();

        ((RelativeLayout) get(R.id.browser_root)).removeView(browserDelegate.mLayoutBottom);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout
                .LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 60;
        params.rightMargin = 60;
        params.bottomMargin = 30;
        params.gravity = Gravity.BOTTOM;
        ((CoordinatorLayout) getRootView()).addView(browserDelegate.mLayoutBottom, params);
    }

    /**
     * 初始化状态栏的显示
     * 首先访问网络请求最新的图片地址,加载图片,根据图片主题设置actionbar颜色
     */
    public void initActionbarImage() {
        RxVolley.get(Api.ZONE_IMAGE, new HttpCallback() {
                    @Override
                    public void onSuccess(final String t) {
                        super.onSuccess(t);
                        Picasso.with(getActivity())
                                .load(t)
                                .error(R.mipmap.def_zone_image)
                                .transform(getTransformation(t))
                                .into((ImageView) get(R.id.actionbar_image));
                    }
                }
        );
    }

    private Transformation getTransformation(final String t) {
        return new Transformation() {
            @Override
            public Bitmap transform(Bitmap b) {
                Palette.from(b).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(final Palette palette) {
                        int defaultColor = rootView.getResources().getColor
                                (android.R.color.white);
                        int titleColor = palette.getLightVibrantColor
                                (defaultColor);
                        CollapsingToolbarLayout collapsingToolbar = get(R.id
                                .collapsing_toolbar);
                        collapsingToolbar.setExpandedTitleColor(titleColor);
                    }
                });
                return b;
            }

            @Override
            public String key() {
                return t;
            }
        };
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.menu_share;
    }

    public void setContent(String text) {
        browserDelegate.webView.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }

    public void setContentUrl(String url) {
        browserDelegate.setContentUrl(url);
    }

    public void setCurrentUrl(String currentUrl) {
        browserDelegate.setCurrentUrl(currentUrl);
    }

    public WebView getWebView() {
        return browserDelegate.webView;
    }

    @Override
    public <T extends View> T get(int id) {
        T t = super.get(id);
        if (t == null) {
            t = browserDelegate.get(id);
        }
        return t;
    }
}
