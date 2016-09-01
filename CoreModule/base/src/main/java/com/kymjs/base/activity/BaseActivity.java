package com.kymjs.base.activity;

import com.kymjs.themvp.presenter.ActivityPresenter;
import com.kymjs.themvp.view.IDelegate;

/**
 * Created by ZhangTao on 7/1/16.
 */
public abstract class BaseActivity<T extends IDelegate> extends ActivityPresenter<T> {
}
