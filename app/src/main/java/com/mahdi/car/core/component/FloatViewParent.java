package com.mahdi.car.core.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class FloatViewParent extends CellFrameLayout {

    private FloatView floatView = null;

    private GestureDetector gestureDetector;
    private float velocityY;

    private boolean isPressed = false;
    private boolean isSelect = false;
    private boolean isExpand = false;

    private boolean isShow = false;


    private int bottomToolBar = dp(48);
    private int headerHeight = dp(54);

    public FloatViewParent(Context context) {
        super(context);

        setBackgroundColor(0x00ff9800);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP));

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                processScroll(e1, distanceX, distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                FloatViewParent.this.velocityY = velocityY;
                return false;
            }
        });

        invalidate();

        floatView = new FloatView(context);
        floatView.setTranslationY(screenHeight - bottomToolBar);
        addView(floatView);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (!isShow) {
            return false;
        }

        float x = event.getX();
        float y = event.getY();

        gestureDetector.onTouchEvent(event);
        floatView.onTouchEvent(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (y > screenHeight - headerHeight - bottomToolBar && y < screenHeight) {
                    isPressed = true;
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (isPressed == true) {
                    isSelect = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:

                if (y > screenHeight - headerHeight - bottomToolBar && y < screenHeight - bottomToolBar && isPressed == true) {
                    setExpand();
                    isPressed = false;
                    isSelect = true;
                    velocityY = -2000;
                    return true;
                }

                isPressed = false;
                isSelect = false;

                if (floatView.getTranslationY() > screenHeight / 2) {
                    if (velocityY > -1000) {
                        setCollapse();
                    } else {
                        setExpand();
                    }

                } else {
                    if (velocityY > -1000) {
                        setCollapse();
                    } else {
                        setExpand();
                    }
                }

                break;
        }

        if (isExpand) {
            return true;
        }

        return isSelect;
    }

    private void processScroll(MotionEvent e1, float distanceX, float distanceY) {
        dX -= distanceX;
        dY -= distanceY;

        floatView.setScroll();

        if (Math.abs(dY) > Math.abs(dX)) {
            if (isExpand) {
                floatView.setTranslationY(dY);
            } else {
                floatView.setTranslationY(dY + screenHeight - bottomToolBar - headerHeight);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellHeight = getHeight();
    }

    private void setExpand() {
        isExpand = true;
        ViewAnimator.animate(floatView).setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(200).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dX = 0;
                dY = 0;
                floatView.setExpand();
            }
        }).start();
    }

    private void setCollapse() {
        isExpand = false;
        ViewAnimator.animate(floatView).setInterpolator(new DecelerateInterpolator()).translationY(screenHeight - bottomToolBar - headerHeight).setDuration(200).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dX = 0;
                dY = 0;
                floatView.setCollapse();
            }
        }).start();
    }

    public void show(String username, String title) {
        floatView.setParameters(username, title);
        if (isShow)
            return;

        ViewAnimator.animate(floatView).setInterpolator(new DecelerateInterpolator()).translationY(screenHeight - bottomToolBar - headerHeight).setDuration(150).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isShow = true;
            }
        }).start();
    }

    public void hide() {
        if (isShow == false)
            return;
        ViewAnimator.animate(floatView).setInterpolator(new DecelerateInterpolator()).translationY(screenHeight - bottomToolBar).setDuration(150).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isShow = false;
            }
        }).start();
    }

}
