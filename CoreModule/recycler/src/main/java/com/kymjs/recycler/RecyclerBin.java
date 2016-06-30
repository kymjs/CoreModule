package com.kymjs.recycler;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * 用于视图复用
 * Created by ZhangTao on 5/9/16.
 */
public class RecyclerBin {
    private ArrayList<View> viewArrayList;
    private int layoutId;

    public RecyclerBin(int layoutId) {
        this.layoutId = layoutId;
        viewArrayList = new ArrayList<>();
    }

    public View getView(Context context) {
        if (viewArrayList.size() == 0) {
            return create(context);
        } else {
            View view = viewArrayList.remove(0);
            if (view == null) {
                view = create(context);
            }
            return view;
        }
    }

    public View create(Context context) {
        return View.inflate(context, layoutId, null);
    }

    public void put(View view) {
        viewArrayList.add(view);
    }
}
