package com.kymjs.base.activity;

import android.databinding.ViewDataBinding;

import com.kymjs.themvp.databind.DataBindActivity;
import com.kymjs.themvp.view.IDelegate;

/**
 * Created by ZhangTao on 8/31/16.
 */
public abstract class BaseDataBindingActivity<T extends IDelegate, D extends ViewDataBinding>
        extends DataBindActivity<T, D> {
}
