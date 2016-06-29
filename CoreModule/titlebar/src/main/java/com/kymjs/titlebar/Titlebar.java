package com.kymjs.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ZhangTao on 6/28/16.
 */
public class Titlebar extends RelativeLayout {

    private TextView mTitle;
    private ImageView mIcon;
    private ImageView mMenuIcon;

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
        mTitle = (TextView) view.findViewById(R.id.titlebar_tv_title);
        mIcon = (ImageView) view.findViewById(R.id.titlebar_iv_back);
        mMenuIcon = (ImageView) view.findViewById(R.id.titlebar_iv_menu);
    }

    public ImageView getIconView() {
        return mIcon;
    }

    public TextView getTitleView() {
        return mTitle;
    }

    public ImageView getMenuIconView() {
        return mMenuIcon;
    }
}
