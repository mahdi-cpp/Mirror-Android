package com.mahdi.car.share;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomGridLayoutManager extends GridLayoutManager
{

    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGridLayoutManager(Context context, int spanCount)
    {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout)
    {
        super(context, spanCount, orientation, reverseLayout);
    }


    public void setScrollEnabled(boolean flag)
    {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically()
    {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("CustomGridLayoutManager", "IndexOutOfBoundsException in RecyclerView");
        }
    }

}