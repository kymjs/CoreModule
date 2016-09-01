package com.kymjs.base.fragment;

import android.databinding.ViewDataBinding;

import com.kymjs.themvp.databind.DataBindFragment;
import com.kymjs.themvp.view.IDelegate;

/**
 * Created by ZhangTao on 8/31/16.
 */
public abstract class BaseDataBindingFragment<T extends IDelegate, D extends ViewDataBinding>
        extends DataBindFragment<T, D> {
}