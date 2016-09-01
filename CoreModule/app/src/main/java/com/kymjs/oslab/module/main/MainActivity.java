package com.kymjs.oslab.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kymjs.base.util.ActivityUtils;
import com.kymjs.oslab.R;
import com.kymjs.oslab.databinding.ActivityMainBinding;
import com.kymjs.oslab.module.blog.BlogFragment;
import com.kymjs.themvp.databind.DataBindActivity;

public class MainActivity extends DataBindActivity<MainViewDelegate, ActivityMainBinding> {

    private static final String TAG = "MainActivity";

    @Override
    protected Class<MainViewDelegate> getDelegateClass() {
        return MainViewDelegate.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new BlogFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.main_body);
    }
}
