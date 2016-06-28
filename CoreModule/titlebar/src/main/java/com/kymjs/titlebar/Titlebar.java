package com.kymjs.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by ZhangTao on 6/28/16.
 */
public class Titlebar extends RelativeLayout {

    public Titlebar(Context context) {
        super(context);
        init();
    }

    public Titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.titlebar_default, null);
        this.addView(view);
    }
}
