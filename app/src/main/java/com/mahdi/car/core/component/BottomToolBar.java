package com.mahdi.car.core.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.RootView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.share.Themp;

public class BottomToolBar {
    private Delegate delegate;


    public final int viewHeight = dp(48);

    private final int count = 4;
    private int index = 0;
    private int select = 0;
    private int topMargin = dp(10);
    private int width = AndroidUtilities.width;
    private int screenHeight = AndroidUtilities.screenHeight;
    public static int center = 0;

    private boolean isPopup = false;
    private boolean isNotification = false;

    private ValueAnimator profileAnimator;
    private float profileValue = 1.0f;

    private Drawable bookmarkDrawable;
    private ValueAnimator bookmarkAnimator;
    private float bookmarkScale;
    private int bookmarkY = 0;
    private boolean isStartBookmark = false;
    private boolean isShowBookmark = false;

    public BottomToolBar() {
        center = ((((width / count) * (1 * 2 + 1)) - Themp.mainToolBarIcons[1].getWidth()) / 2) + (Themp.mainToolBarIcons[1].getWidth() / 2);

        profileAnimator = new ValueAnimator();
        profileAnimator.setValues(PropertyValuesHolder.ofFloat("counter", 1.0f, 1.1f));
        profileAnimator.setDuration(100);
        profileAnimator.setRepeatMode(ValueAnimator.REVERSE);
        profileAnimator.setRepeatCount(1);
        profileAnimator.setInterpolator(new DecelerateInterpolator());
        profileAnimator.addUpdateListener(animation -> {
            profileValue = (float) animation.getAnimatedValue("counter");
            delegate.invalidate();
        });

        bookmarkAnimator = new ValueAnimator();
        bookmarkAnimator.setRepeatMode(ValueAnimator.RESTART);
        bookmarkAnimator.setInterpolator(new DecelerateInterpolator());
        bookmarkAnimator.addUpdateListener(animation -> {
            if (isStartBookmark) {
                bookmarkScale = (float) animation.getAnimatedValue("bookmark_show");
            } else {
                bookmarkY = (int) animation.getAnimatedValue("bookmark_y");
            }
            delegate.invalidate();
        });

        bookmarkAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isStartBookmark) {
                    new Handler().postDelayed(() -> {
                        isStartBookmark = false;
                        profileAnimator.start();
                        bookmarkAnimator.setValues(PropertyValuesHolder.ofInt("bookmark_y", 0, dp(100)));
                        bookmarkAnimator.start();
                    }, 500);
                } else {
                    isShowBookmark = false;
                }
                super.onAnimationEnd(animation);
            }
        });

        select = BaseFragment.currentPage;
    }

    private void showBookmark() {
        if (isStartBookmark) {
            return;
        }
        isStartBookmark = true;
        isShowBookmark = true;
        bookmarkAnimator.setValues(PropertyValuesHolder.ofFloat("bookmark_show", 0, 1.0f));
        bookmarkAnimator.setDuration(200);
        bookmarkAnimator.start();
    }

//    private void bookmark(Canvas canvas) {
//        int size = dp(44);
//        canvas.save();
//
//        if (isStartBookmark) {
//            canvas.translate(width - dp(40 + 18), dp(10));
//            canvas.scale(bookmarkScale, bookmarkScale, size / 2, +size / 2);
//
//        } else {
//            canvas.translate(width - dp(40 + 18), dp(10) + bookmarkY);
//        }
//
//        if (bookmarkDrawable != null) {
//            bookmarkDrawable.setBounds(0, 0, dp(44), dp(44));
//            bookmarkDrawable.draw(canvas);
//        }
//
//        canvas.restore();
//    }

    public void selectPage(int index) {
        this.select = index;
        delegate.invalidate();
    }

    public void see() {
        isNotification = false;
        isPopup = false;
        delegate.invalidate();
        //ViewPropertyObjectAnimator.animate(notificationView).translationY(dp(60)).setDuration(150).start();
    }

    public void dispatchDraw(float dx, Canvas canvas) {
        canvas.save();
        canvas.translate(dx, 0);

        select = RootView.instance().currentPage;

        canvas.save();
        canvas.translate(0, screenHeight - viewHeight);

        canvas.drawRect(0, 0, width, viewHeight, Themp.PAINT_WHITE);


        for (int i = 0; i < count; i++) {

            int size = width / count;
            int x = (i * size) + (Themp.mainToolBarSelectedIcons[0].getWidth());
            int y = 15;

            if (select == i) {
                if (i == 3) {
                    canvas.drawBitmap(Themp.mainToolBarSelectedIcons[i], x, topMargin + dp(1), Themp.ICON_PAINT_MULTIPLY_WHITE);
                } else {
                    canvas.drawBitmap(Themp.mainToolBarSelectedIcons[i], x, topMargin, Themp.ICON_PAINT_MULTIPLY_WHITE);
                }

            } else {
                if (i == 3) {
                    canvas.drawBitmap(Themp.mainToolBarIcons[i], x, topMargin + dp(1), Themp.ICON_PAINT_MULTIPLY_WHITE);
                } else {
                    canvas.drawBitmap(Themp.mainToolBarIcons[i], x, topMargin, Themp.ICON_PAINT_MULTIPLY_WHITE);
                }
            }

            Themp.drawableToolbar[0].setBounds(0, 0, width, dp(1));
            Themp.drawableToolbar[0].draw(canvas);

        }

        //canvas.drawLine(0,dp(13),width,dp(13),Themp.PAINT_FFAAAAAA);
        //canvas.drawLine(0,dp(13 + 23),width,dp(13 + 23),Themp.PAINT_FFAAAAAA);

        canvas.restore();
        canvas.restore();

    }

    public void showBookmark(Drawable bookmarkDrawable) {
        this.bookmarkDrawable = bookmarkDrawable;
        showBookmark();
    }

    public interface Delegate {
        void invalidate();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    //Utility functions----------------------------------------------------------
    protected int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(AndroidUtilities.mDensity * value);
    }
    //-------------------------------------------------------------------------------

}
