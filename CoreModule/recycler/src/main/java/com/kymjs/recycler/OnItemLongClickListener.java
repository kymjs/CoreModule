package com.kymjs.recycler;

/**
 * Created by ZhangTao on 9/29/16.
 */

import android.view.View;

/**
 * Interface definition for a callback to be invoked when an item in this
 * view has been clicked and held.
 */
public interface OnItemLongClickListener {
    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @return true if the callback consumed the long click, false otherwise
     */
    boolean onItemLongClick(View view, int position);
}