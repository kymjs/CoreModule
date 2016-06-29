package com.kymjs.browser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.kymjs.share_sdk.ShareSDK;

/**
 * Created by ZhangTao on 6/28/16.
 */
public class BrowserActivity extends AppCompatActivity {

    public static final String BROWSER_KEY = "browser_url";
    public static final String BROWSER_TITLE_KEY = "browser_title_url";
    public static final String DEFAULT = "http://www.kymjs.com/";
    public static final String DEFAULT_TITLE = "浏览器";

    private BrowserFragment browserFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_activity_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.browser_icon_delete);
        }
        initData();
    }

    public void initData() {
        Intent intent = getIntent();
        String mCurrentUrl = DEFAULT;
        if (intent != null) {
            mCurrentUrl = intent.getStringExtra(BROWSER_KEY);
            String strTitle = intent.getStringExtra(BROWSER_TITLE_KEY);
            if (TextUtils.isEmpty(mCurrentUrl)) {
                mCurrentUrl = DEFAULT;
            }
            if (!TextUtils.isEmpty(strTitle)) {
                setTitle(strTitle);
            } else {
                setTitle(DEFAULT_TITLE);
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.browser_fragment);
        if (fragment != null && fragment instanceof BrowserFragment) {
            browserFragment = (BrowserFragment) fragment;
            browserFragment.mWebView.loadUrl(mCurrentUrl);
        }
    }

    public static void toBrowser(Context context, String url) {
        toBrowser(context, url, null);
    }

    public static void toBrowser(Context context, String url, String title) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(BROWSER_KEY, url);
        intent.putExtra(BROWSER_TITLE_KEY, title);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (browserFragment == null || !browserFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser_menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.browser_menu_share
                && browserFragment != null
                && browserFragment.mWebView != null
                && !TextUtils.isEmpty(browserFragment.mWebView.getUrl())) {
            ShareSDK.shareUrl(this, browserFragment.mWebView.getUrl());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
