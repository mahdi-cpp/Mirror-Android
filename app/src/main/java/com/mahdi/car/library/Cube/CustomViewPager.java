package com.mahdi.car.library.Cube;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.mahdi.car.messenger.AndroidUtilities;

import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager
{
    private Delegate delegate;

    private GestureDetector gestureDetector;
    private float old_dY;
    private float dX;
    private float dY;

    private boolean permission = true;

    public CustomViewPager(Context context)
    {
        super(context);
        postInitViewPager();
        //setMyScroller();
        init();

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                dX = 0;
                dY = 0;
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e)
            {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                processScroll(e1, distanceX, distanceY);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                return true;
            }
        });
    }

    private static final String TAG = CustomViewPager.class.getSimpleName();

    public void init()
    {
        //setPageTransformer(true, new VerticalViewPagerTransform());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public CustomViewPager(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    private MotionEvent swapXY(MotionEvent event)
    {
        float newX = event.getX();
        float newY = event.getY();

        gestureDetector.onTouchEvent(event);
        event.setLocation(newX, newY);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                Reset();
                break;

            default:
                Reset();
                invalidate();
        }

        return event;
    }

    public void setPermission(boolean permission)
    {
        this.permission = permission;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
//        if (!permission) {
//            return false;
//        }

        boolean intercept = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return super.onTouchEvent(swapXY(ev));
    }

    private class VerticalViewPagerTransform implements PageTransformer
    {
        private static final float Min_Scale = 0.65f;

        @Override
        public void transformPage(@NonNull View page, float position)
        {

            if (position < -1) {
                page.setAlpha(0);
            } else if (position <= 0) {
                page.setAlpha(1);
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(page.getHeight() * position);
                page.setScaleX(1);
                page.setScaleY(1);
            } else if (position <= 1) {
                page.setAlpha(1 - position);
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(0);
                float scaleFactor = Min_Scale + (1 - Min_Scale) * (1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else if (position > 1) {
                page.setAlpha(0);
            }
        }
    }

    private void processScroll(MotionEvent e1, float distanceX, float distanceY)
    {
        if (e1 == null) {
            return;
        }

        float x = e1.getX();
        float y = e1.getY();

        old_dY = dY;

        dX -= distanceX;
        dY -= distanceY;

        if (Math.abs(dY) > Math.abs(dX)) {

            if (dY < (AndroidUtilities.screenHeight - AndroidUtilities.dp(100)) && delegate != null) {
                delegate.vertical(dY);
            }
        }
    }

    private void Reset()
    {
        if (delegate != null) {
            if (Math.abs(dY) > Math.abs(dX)) {
                delegate.close(dY, old_dY);
            } else {
                delegate.Reset(dY);
            }
        }

        //        if (dY < -U.dp(100)) {
        //            //cover.setVisibility(View.INVISIBLE);
        //            //igtvListView.show();
        //            old_dY = 0;
        //            dY = 0;
        //        } else {// hide listView
        //
        //            //cover.setVisibility(View.VISIBLE);
        //            //ViewPropertyObjectAnimator.animate(igtvListView).
        //            // setInterpolator(new DecelerateInterpolator()).translationY(U.dp(400)).setDuration(300).start();
        //        }
    }

    private ScrollerCustomDuration mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager()
    {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor)
    {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    public interface Delegate
    {
        void vertical(float dY);

        void close(float dY, float old_dY);

        void Reset(float dY);
    }

}