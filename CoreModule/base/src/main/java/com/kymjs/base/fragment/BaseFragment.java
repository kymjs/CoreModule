package com.kymjs.base.fragment;

import com.kymjs.themvp.presenter.FragmentPresenter;
import com.kymjs.themvp.view.IDelegate;

/**
 * Created by ZhangTao on 7/2/16.
 */
public abstract class BaseFragment<T extends IDelegate> extends FragmentPresenter<T> {
}
