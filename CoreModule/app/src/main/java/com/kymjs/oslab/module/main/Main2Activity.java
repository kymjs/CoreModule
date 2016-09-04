package com.kymjs.oslab.module.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kymjs.base.util.ActivityUtils;
import com.kymjs.oslab.R;
import com.kymjs.oslab.module.blog.BlogFragment;
import com.kymjs.oslab.module.dress.DressFragment;
import com.kymjs.oslab.utils.KJAnimations;

/**
 * 不要为了MVP而MVP，简单逻辑直接一个类搞定就可以了
 * Created by ZhangTao on 9/3/16.
 */
public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    private Fragment fragment1 = new BlogFragment();
    private Fragment fragment2 = new DressFragment();
    private Fragment currentFragment; //当前内容所显示的Fragment

    private TextView tvExitTip;

    private boolean isOnKeyBacking;
    private Handler mMainLoopHandler = new Handler(Looper.getMainLooper());
    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
            cancleExit();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        tvExitTip = (TextView) findViewById(R.id.titlebar_text_exittip);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment1, R.id.main_body);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.bottombar_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.bottombar_content1:
                        changeFragment(fragment1);
                        break;
                    case R.id.bottombar_content2:
                        changeFragment(fragment2);
                        break;
                }
            }
        });
    }

    /**
     * 用Fragment替换内容区
     *
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(Fragment targetFragment) {
        if (targetFragment.equals(currentFragment)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_body, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
        }
        if (currentFragment != null && currentFragment.isVisible()) {
            transaction.hide(currentFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOnKeyBacking) {
                mMainLoopHandler.removeCallbacks(onBackTimeRunnable);
                isOnKeyBacking = false;
                finish();
            } else {
                isOnKeyBacking = true;
                showExitTip();
                mMainLoopHandler.postDelayed(onBackTimeRunnable, 2000);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 显示Toolbar的退出tip
     */
    public void showExitTip() {
        tvExitTip.setVisibility(View.VISIBLE);
        Animation a = KJAnimations.getTranslateAnimation(0f, 0f,
                -getResources().getDimension(R.dimen.titlebar_height), 0f, 300);
        tvExitTip.startAnimation(a);
    }


    /**
     * 取消退出
     */
    public void cancleExit() {
        Animation a = KJAnimations.getTranslateAnimation(0f, 0f, 0f,
                -getResources().getDimension(R.dimen.titlebar_height), 300);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvExitTip.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        tvExitTip.startAnimation(a);
    }
}
