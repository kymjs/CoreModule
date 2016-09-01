package com.kymjs.oslab.activity;

import android.os.Bundle;
import android.view.View;

import com.kymjs.base.backactivity.BaseBackActivity;
import com.kymjs.oslab.R;

/**
 * Created by ZhangTao on 7/25/16.
 */
public class DemoBackActivity extends BaseBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_baseback);

        findViewById(R.id.demo_baseback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected Class getDelegateClass() {
        return null;
    }
}
