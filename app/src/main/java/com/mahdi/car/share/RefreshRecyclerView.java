package com.mahdi.car.share;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class RefreshRecyclerView extends CellFrameLayout
{
    private Delegate delegate;

    public RecyclerView recyclerView;
    private GestureDetector gestureDetector;

    private ValueAnimator animator;

    private float progress;
    private float size = dp(17);
    private float dX;
    private float dY;
    private float counter;
    private int loadingTopMargin = 0;

    private boolean isAnimation = false;
    private boolean isBusy = false;
    private boolean isScrollEnabled = true;

    private boolean enabled = true;

    public RefreshRecyclerView(Context context)
    {
        super(context);

        setBackgroundColor(0xffffffff);

        recyclerView = new RecyclerView(context);
        recyclerView.setBackgroundColor(0xffffffff);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        addView(recyclerView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                //dX = 0;
                //dY = 0;
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
//                if (QZoomView.getInstance().isShow()) {
//                    return false;
//                }
                processScroll(e1, distanceX, distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                return false;
            }
        });

        animator = new ValueAnimator();
        animator.setValues(PropertyValuesHolder.ofInt("counter1", 0, 270));
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            counter = (int) animation.getAnimatedValue("counter1");
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                counter = 0;
                super.onAnimationEnd(animation);
            }
        });

        setWillNotDraw(false);
    }

    public void setScrollEnabled(boolean flag)
    {
        this.isScrollEnabled = flag;
    }

    public boolean isSwipe()
    {
        if (recyclerView.getTranslationY() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (!isSwipe()) {
            return;
        }

        canvas.save();
        canvas.translate(0, loadingTopMargin);

        int centerX = getWidth() / 2;
        int centerY = dp(40);

        canvas.drawCircle(getWidth() / 2, centerY, size, Themp.STROKE_PAINT_1_5DP_FFEEEEEE);

        if (isAnimation) {
            canvas.drawArc(new RectF(centerX - size, centerY - size, centerX + size, centerY + size), counter, counter + 45, false, Themp.STROKE_PAINT_1_5DP_FFBBBBBB);
        } else {
            canvas.drawArc(new RectF(centerX - size, centerY - size, centerX + size, centerY + size), -90, progress, false, Themp.STROKE_PAINT_1_5DP_FFCCCCCC);
        }

        canvas.restore();
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (!enabled)//disable temporary
            return true;

        setGesture(ev);
        return super.onInterceptTouchEvent(ev);
    }


    private void processScroll(MotionEvent e1, float distanceX, float distanceY)
    {
        dX -= distanceX;
        dY -= distanceY;

        if (Math.abs(dY) > Math.abs(dX)) {

            if (dY > 0 && recyclerView.computeVerticalScrollOffset() == 0) {

                float progress = dY;
                setProgress(progress);

                if (delegate != null) {
                    delegate.update();
                }

            } else {
                if (recyclerView.getTranslationY() != 0) {
                    recyclerView.setTranslationY(0);
                }
            }
        }
    }

    public void setGesture(MotionEvent event)
    {
        if (!isScrollEnabled) {
            return;
        }

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                dX = 0;
                dY = 0;
//                QZoomView.getInstance().setPivot(event.getY());
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (recyclerView.getTranslationY() > 0) {
                    Reset();
                }
                break;

        }
    }

    private void Reset()
    {
        if (isBusy) {
            return;
        }

        isBusy = true;

        if (delegate != null) {
            delegate.reset();
        }

        if (progress > 360) {
            if (delegate != null) {
                delegate.startRefresh();
            }
            animator.start();
            isAnimation = true;
        }
        int height = (progress > 360) ? dp(80) : 0;

        ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationY(height).setDuration(250).addUpdateListener(animation -> {
            if (delegate != null) {
                delegate.update();
            }
        }).addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                isBusy = false;
                dX = 0;
                dY = 0;

                if (delegate != null) {
                    delegate.reset();
                }

                super.onAnimationEnd(animation);
            }
        }).start();
    }

    public void done()
    {

        ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(300).addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                if (delegate != null) {
                    delegate.update();
                }
            }
        }).addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                isBusy = false;
                dX = 0;
                dY = 0;
                if (delegate != null) {
                    delegate.reset();
                }
                isAnimation = false;
                animator.cancel();
                invalidate();
                super.onAnimationEnd(animation);
            }
        }).start();
    }

    public void setLoadingTopMargin(int loadingTopMargin)
    {
        this.loadingTopMargin = loadingTopMargin;
    }

    public void setProgress(float progress)
    {
        this.progress = progress;

        recyclerView.setTranslationY(progress);

        invalidate();
    }

    public interface Delegate
    {
        void startRefresh();

        void update();

        void reset();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
