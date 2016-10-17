package com.kymjs.oslab.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kymjs.common.ThreadSwitch;
import com.kymjs.oslab.databinding.ActivityMainBinding;
import com.kymjs.oslab.module.blog.BlogFragment;
import com.kymjs.themvp.databind.DataBindActivity;

public class TestActivity extends DataBindActivity<MainViewDelegate, ActivityMainBinding> {

    private static final String TAG = "MainActivity";
    Fragment fragment = new BlogFragment();

    @Override
    protected Class<MainViewDelegate> getDelegateClass() {
        return MainViewDelegate.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThreadSwitch.get()
                .io(new ThreadSwitch.IO() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: ========io");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "run: ========io2");
                    }
                })
                .ui(new ThreadSwitch.Function() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: ========hello");
                    }
                })
                .breakTask()
                .ui(new ThreadSwitch.Function() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: ========hello2");
                    }
                });

//        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.main_body);
    }
}
