package com.example.wizart.mytrips.RecyclerAdapter;

import android.view.View;

/**
 * Created by egutierr on 2015-03-06.
 */
public interface ClickListener {
    /**
     * Called when the view is clicked.
     *
     * @param v view that is clicked
     * @param position of the clicked item
     * @param isLongClick true if long click, false otherwise
     */
    public void onClick(View v, int position, boolean isLongClick);
}
