package com.kymjs.base.backactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * 侧滑finish的Activity基类
 *
 * @author kymjs (http://www.kymjs.com/) on 11/6/15.
 */
public abstract class BaseBackActivity extends AppCompatActivity {
    private BackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new BackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            v = mHelper.findViewById(id);
        }
        return v;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setSwipeBackEnable(enable);
    }

    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
