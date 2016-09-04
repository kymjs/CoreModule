package com.kymjs.oslab.module.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.kymjs.base.backactivity.BaseBackActivity;
import com.kymjs.browser.BrowserDelegateOption;
import com.kymjs.common.ThreadSwitch;
import com.kymjs.oslab.R;
import com.kymjs.oslab.inter.IRequestVo;
import com.kymjs.oslab.utils.AppConfig;
import com.kymjs.oslab.utils.SystemTool;
import com.kymjs.oslab.utils.Tools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.view.EmptyLayout;


/**
 * 博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class BlogDetailActivity extends BaseBackActivity<BlogDetailDelegate> implements IRequestVo {

    public static final String KEY_BLOG_URL = "blog_url_key";
    public static final String KEY_BLOG_TITLE = "blog_title_key";
    protected String url;

    protected EmptyLayout emptyLayout;
    protected WebView webView;
    protected String contentHtml = null;
    protected byte[] httpCache = null;

    @Override
    protected Class<BlogDetailDelegate> getDelegateClass() {
        return BlogDetailDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        webView = viewDelegate.get(R.id.webview);
        emptyLayout = viewDelegate.get(R.id.emptylayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        url = intent.getStringExtra(KEY_BLOG_URL);
        String title = intent.getStringExtra(KEY_BLOG_TITLE);
        CollapsingToolbarLayout collapsingToolbar = viewDelegate.get(R.id.collapsing_toolbar);
        if (title != null) {
            collapsingToolbar.setTitle(title);
        } else {
            collapsingToolbar.setTitle(getString(R.string.kymjs_blog_name));
        }

        readCache();
        doRequest();
    }

    /**
     * 读取缓存内容
     */
    protected void readCache() {
        ThreadSwitch.get()
                .io(new ThreadSwitch.IO() {
                    @Override
                    public void run() {
                        byte[] cache = RxVolley.getCache(url);
                        if (cache != null && cache.length != 0) {
                            httpCache = cache;
                            contentHtml = parserHtml(new String(cache));
                        }
                    }
                })
                .ui(new ThreadSwitch.Function() {
                    @Override
                    public void run() {
                        emptyLayout.dismiss();
                        viewDelegate.setContent(contentHtml);
                        viewDelegate.setCurrentUrl(url);
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 不知道为什么点击完webview图片后,再返回,webview内容就没了,只好再设置一次
        viewDelegate.setContent(contentHtml);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }


    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_share && !TextUtils.isEmpty(url)) {
            Tools.shareUrl(this, url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doRequest() {
        if (TextUtils.isEmpty(url)) return;
        new RxVolley.Builder().url(url).callback(new HttpCallback() {
            @Override
            public void onSuccess(final String t) {
                super.onSuccess(t);
                ThreadSwitch.get()
                        .io(new ThreadSwitch.IO() {
                            @Override
                            public void run() {
                                contentHtml = parserHtml(t);
                            }
                        })
                        .ui(new ThreadSwitch.Function() {
                            @Override
                            public void run() {
                                if (httpCache == null ||
                                        !new String(httpCache).equals(t)
                                                && viewDelegate != null
                                                && contentHtml != null) {
                                    viewDelegate.setContent(contentHtml);
                                }
                                emptyLayout.dismiss();
                            }
                        });
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        }).doTask();
    }

    /**
     * 跳转到博客详情界面
     *
     * @param url   传递要显示的博客的地址
     * @param title 传递要显示的博客的标题
     */
    public static void goinActivity(Context cxt, @NonNull String url, @Nullable String title) {
        if (url.toLowerCase().contains(AppConfig.DONATE_STR)
                && SystemTool.checkApkExist(cxt, AppConfig.ALIPAY_PKGNAME)) {
            SystemTool.copy(cxt, AppConfig.ALIPAY_ID);
            SystemTool.openOtherApp(cxt, AppConfig.ALIPAY_PKGNAME, AppConfig.ALIPAY_MAIN_NAME);
        } else {
            Intent intent = new Intent(cxt, BlogDetailActivity.class);
            intent.putExtra(KEY_BLOG_URL, url);
            if (TextUtils.isEmpty(title))
                title = cxt.getString(R.string.kymjs_blog_name);
            intent.putExtra(KEY_BLOG_TITLE, title);
            cxt.startActivity(intent);
        }
    }

    public static final String regEx_script =
            "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    public static final String regEx_header =
            "<[\\s]*?header[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?header[\\s]*?>";
    public static final String regEx_footer =
            "<[\\s]*?footer[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?footer[\\s]*?>";

    /**
     * 对博客数据加工,适应手机浏览
     */
    public static String parserHtml(String html) {
        html = html.replaceAll(regEx_script, "");
        html = html.replaceAll(regEx_header, "");
        html = html.replaceAll(regEx_footer, "");
        html = html.replaceAll("<img src=\"/", "<img src=\"http://kymjs.com/");
        html = html.replaceAll("<a href=\"/donate\">", "<a href=\"http://kymjs.com/donate\">");
        html = BrowserDelegateOption.setImagePreview(html);
        String commonStyle = "<link rel=\"stylesheet\" type=\"text/css\" " +
                "href=\"file:///android_asset/template/common.css\">";
        html = commonStyle + html;
        return html;
    }
}
