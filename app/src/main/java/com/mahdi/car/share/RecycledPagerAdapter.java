package com.mahdi.car.share;


import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.LinkedList;
import java.util.Queue;

public abstract class RecycledPagerAdapter<VH extends RecycledPagerAdapter.ViewHolder> extends PagerAdapter
{

    public static class ViewHolder
    {
        final View itemView;

        public ViewHolder(View itemView)
        {
            this.itemView = itemView;
        }
    }

    Queue<VH> destroyedItems = new LinkedList<>();

    @Override
    public final Object instantiateItem(ViewGroup container, int position)
    {
        VH viewHolder = destroyedItems.poll();

        viewHolder = (VH) onCreateViewHolder(container, getItemViewType(position));
        onBindViewHolder(viewHolder, position);
        container.addView(viewHolder.itemView);

        return viewHolder;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(((VH) object).itemView);
        destroyedItems.add((VH) object);
    }

    @Override
    public final boolean isViewFromObject(View view, Object object)
    {
        return ((VH) object).itemView == view;
    }

    /**
     * Create a new view holder
     *
     * @param parent
     * @return view holder
     */
    public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * Bind data at position into viewHolder
     *
     * @param viewHolder
     * @param position
     */
    public abstract void onBindViewHolder(VH viewHolder, int position);


    /**
     * get Item Type
     *
     * @param position
     */
    public abstract int getItemViewType(int position);


}