package com.mahdi.car.dialog.parent;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.CustomGridLayoutManager;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.share.component.ui.RadialProgressView;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.messenger.AndroidUtilities;

public abstract class BaseDialog extends CellFrameLayout
{
    private FrameLayout parent;
    private Context context;
    protected RecyclerView recyclerView;

    protected RecyclerView.Adapter adapter;
    protected CustomGridLayoutManager layoutManager;
    protected RecyclerView.SmoothScroller smoothScroller;

    protected int scrollOffsetY;
    private boolean isShow = false;
    private long lastUpdateTime = 0;
    private long lastUpdateDuration = 200;

    protected BaseDialogHeader baseDialogHeader;
    protected View shadowView;
    protected CellView roundView;

    protected View statusBarView;

    protected HeaderEdgeView headerEdgeView;
    protected HeaderSpaceView headerSpaceView;

    public BaseDialog(Context context)
    {
        super(context);

        this.context = context;
        setVisibility(INVISIBLE);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    protected void init(RecyclerView.Adapter adapter, int rowCount, FrameLayout parent)
    {
        this.adapter = adapter;
        this.parent = parent;

        layoutManager = new CustomGridLayoutManager(context, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                //define span size for this position
                //some example for your first three items
                if (position > rowCount + 5) {
                    return 1;
                } else if (position < rowCount) {
                    return 6;
                } else {
                    return 2; //item will take 1/3 space of row
                }
            }
        });

        recyclerView = new RecyclerView(context);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                View view = recyclerView.getChildAt(0);

                if (view instanceof EdgeView) {
                    hide();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                updateCover();
            }
        });
        smoothScroller = new LinearSmoothScroller(context)
        {
            @Override
            protected int getVerticalSnapPreference()
            {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        statusBarView = new View(context);
        statusBarView.setBackgroundColor(0xffffffff);

        roundView = new CellView(context)
        {
            @Override
            protected void onDraw(Canvas canvas)
            {
                round = dp(20);
                canvas.drawRoundRect(new RectF(0, 0, width, getHeight()), round, round, Themp.PAINT_WHITE);
            }
        };

        headerEdgeView = new HeaderEdgeView(context);
        headerSpaceView = new HeaderSpaceView(context);

        shadowView = new CellView(context);
        shadowView.setBackgroundColor(0x22000000);

        addView(shadowView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        addView(roundView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, AndroidUtilities.screenHeight * 2));
        addView(recyclerView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 1000, Gravity.TOP, 0, 0, 0, 0));

        addView(statusBarView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 0, Gravity.TOP));

        parent.addView(this);
    }

    protected void addHeader(BaseDialogHeader baseDialogHeader)
    {
        this.baseDialogHeader = baseDialogHeader;
        addView(this.baseDialogHeader);
    }

    public void setGlassImage(View view)
    {
//        blurringView.setVisibility(VISIBLE);
//        blurringView.setBlurredView(view);
//        blurringView.invalidate();
    }

    private void updateCover()
    {
        update1();

        int[] top = new int[2];

        for (int i = 0; i < recyclerView.getChildCount(); i++) {

            View view = recyclerView.getChildAt(i);

//            if (view instanceof StickerSearchView) {
//
//                view.getLocationInWindow(top);
//                int offsetY = top[1];
//
//                //if (blurringView != null) {
//                blurringView.setTranslationY(offsetY);
//                blurringView.invalidate();
//                //                } else {
//                //                    roundView.setTranslationY(y);
//                //                    roundView.invalidate();
//                //                }
//                return;
//            }
        }
    }

    private void update1()
    {
        int top[] = new int[2];

        if (baseDialogHeader == null) {
            return;
        }

        for (int i = 0; i < recyclerView.getChildCount(); i++) {

            View view = recyclerView.getChildAt(i);

            if (view != null && view instanceof HeaderEdgeView) {
                view.getLocationInWindow(top);
                int y = top[1];
                scrollOffsetY = y;
                baseDialogHeader.setTranslationY(y - dp(1));
                if (baseDialogHeader.getVisibility() == INVISIBLE) {
                    baseDialogHeader.setVisibility(VISIBLE);
                }
                return;
            }
        }

        if (baseDialogHeader.getVisibility() == INVISIBLE) {
            baseDialogHeader.setVisibility(VISIBLE);
        }

        if (baseDialogHeader.getTranslationY() < dp(100)) {
            scrollOffsetY = 0;
            baseDialogHeader.setTranslationY(0);
        }
    }

    public void show()
    {
        parent.removeView(this);
        parent.addView(this);

        setAlpha(1.0f);
        setVisibility(VISIBLE);


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            long currentTime = System.currentTimeMillis();
            if (lastUpdateTime == 0 || lastUpdateTime < currentTime - lastUpdateDuration) {

                lastUpdateTime = currentTime;
                setSmoothScroller(2);
                isShow = true;
            }
        }, 0);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getY() < scrollOffsetY) {
            hide();
            cancel();
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void hide()
    {
        long currentTime = System.currentTimeMillis();
        if (lastUpdateTime == 0 || lastUpdateTime < currentTime - lastUpdateDuration) {

            lastUpdateTime = currentTime;
            isShow = false;
            setSmoothScroller(0);

            ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(0).
                    addListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            setSmoothScroller(0);
                            setVisibility(INVISIBLE);
                            cancel();
                        }
                    }).start();
        }
    }

    protected void setSmoothScroller(int position)
    {
        smoothScroller.setTargetPosition(position);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    public boolean isShow()
    {
        return isShow;
    }

    protected class EdgeView extends View
    {
        public EdgeView(Context context)
        {
            super(context);
            setBackgroundColor(0x00ffffff);
            setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, px(AndroidUtilities.screenHeight) / 3));
        }
    }

    protected class HeaderEdgeView extends View
    {
        public HeaderEdgeView(Context context)
        {
            super(context);
            setBackgroundColor(0x00ffffff);
            setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 1));
        }
    }

    protected class HeaderSpaceView extends View
    {
        public HeaderSpaceView(Context context)
        {
            super(context);
            setBackgroundColor(0x00ff9800);
            setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 150));
        }
    }

    protected class GlassView extends FrameLayout
    {
        public GlassView(Context context)
        {
            super(context);
            setAlpha(0);
            setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, px(AndroidUtilities.screenHeight) / 2.2f));
        }
    }

    protected class EmptyProgressView extends FrameLayout
    {
        private RadialProgressView progressView;

        public EmptyProgressView(Context context)
        {
            super(context);
            setClickable(true);
            //setBackgroundColor(0x88ff9800);

            progressView = new RadialProgressView(context);
            this.addView(progressView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 60, Gravity.TOP, 0, 100, 0, 0));
        }
    }

    abstract protected void cancel();

}
