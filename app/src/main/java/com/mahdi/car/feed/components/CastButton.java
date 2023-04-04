package com.mahdi.car.feed.components;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;

public class CastButton extends CellView {

    private Delegate delegate;

    private StaticLayout nameLayoutPage1;
    private StaticLayout titleLayoutPage1;

    private StaticLayout nameLayoutPage2;
    private StaticLayout titleLayoutPage2;

    private boolean isConnected = false;

    private Bitmap icon;

    private ValueAnimator xAnimator;
    private int xCounter = 0;
    int iconSize;

    public CastButton(Context context) {
        super(context);
        invalidate();

        xAnimator = new ValueAnimator();
        xAnimator.setDuration(300);
        xAnimator.setRepeatMode(ValueAnimator.RESTART);
        xAnimator.setInterpolator(interpolator);
        xAnimator.setRepeatCount(0);
        xAnimator.addUpdateListener(animation -> {
            xCounter = (int) animation.getAnimatedValue("counter2");
            invalidate();
        });
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect(String connectionType) {

        if (connectionType.contains("USB")) {
            icon = Themp.toolbar.usb_50;
        } else {
            icon = Themp.toolbar.wifi_50;
        }

        isConnected = true;
        titleLayoutPage2 = new StaticLayout("Screen is mirroring by " + connectionType, Themp.TEXT_PAINT_FILL_AND_STROKE_3_GREEN[4], cellWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        xAnimator.setValues(PropertyValuesHolder.ofInt("counter2", 0, -cellWidth));
        xAnimator.start();
    }

    public void disconnect() {
        if (xCounter == 0) {
            return;
        }
        isConnected = false;
        xAnimator.setValues(PropertyValuesHolder.ofInt("counter2", -cellWidth, 0));
        xAnimator.start();
    }

    public void setTitle(String title) {

        nameLayoutPage1 = new StaticLayout("Screen Mirror", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[8], cellWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        titleLayoutPage1 = new StaticLayout("Screen Mirror by Wi-Fi", Themp.TEXT_PAINT_FILL_AND_STROKE_2_GREY[5], cellWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        nameLayoutPage2 = new StaticLayout("Screen is mirroring", Themp.TEXT_PAINT_FILL_AND_STROKE_3_GREEN[9], cellWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        titleLayoutPage2 = new StaticLayout("Screen is mirroring by Wi-Fi", Themp.TEXT_PAINT_FILL_AND_STROKE_3_GREEN[4], cellWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);

        cellWidth = getWidth();
        cellHeight = getHeight();
        centerX = cellWidth / 2;
        centerY = cellHeight / 2;

        if (isPressed == 1) {
            drawRoundRect(dp(2), dp(2), new RectF(0, 0, cellWidth - dp(4), cellHeight - dp(4)), dp(10), Themp.PAINT_FFEEEEEE);
        }

        drawRoundRect(dp(2), dp(2), new RectF(0, 0, cellWidth - dp(4), cellHeight - dp(4)), dp(10), Themp.STROKE_PAINT_1_5DP_FFEEEEEE);

        canvas.save(); // page 1
        {
            canvas.translate(xCounter, 0);
            iconSize = Themp.toolbar.cast.getWidth() / 2;
            canvas.drawBitmap(Themp.toolbar.cast, dp(20), centerY - iconSize, Themp.ICON_PAINT_MULTIPLY_BLACK);

            iconSize = Themp.toolbar.arrow_forward.getHeight() / 2;
            canvas.drawBitmap(Themp.toolbar.arrow_forward, cellWidth - dp(30), centerY - iconSize, Themp.ICON_PAINT_MULTIPLY_BLACK);

            drawTextLayout(nameLayoutPage1, dp(70), centerY / 2 - dp(0));
            drawTextLayout(titleLayoutPage1, dp(70), centerY / 2 + dp(28));
            canvas.restore();
        }

        canvas.save(); // page 2
        {
            canvas.translate(xCounter + cellWidth, 0);

            Themp.PAINT_GREEN.setAlpha(10);
            drawRoundRect(dp(2), dp(2), new RectF(0, 0, cellWidth - dp(4), cellHeight - dp(4)), dp(10), Themp.PAINT_GREEN);
            Themp.PAINT_GREEN.setAlpha(255);

            iconSize = Themp.toolbar.cast_large.getWidth() / 2;
            canvas.drawBitmap(Themp.toolbar.cast_large, dp(20), centerY - iconSize, Themp.ICON_PAINT_MULTIPLY_BLACK);
            if (icon != null) {
                iconSize = icon.getWidth() / 2;
                canvas.drawBitmap(icon, cellWidth - dp(30), centerY - iconSize, Themp.ICON_PAINT_MULTIPLY_BLACK);
            }
            drawTextLayout(nameLayoutPage2, dp(110), centerY / 2 - dp(0));
            drawTextLayout(titleLayoutPage2, dp(110), centerY / 2 + dp(28));
            canvas.restore();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
                    isPressed = 1;
                } else {
                    isPressed = -1;
                }
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight() && isPressed == 1) {
                    if (delegate != null)
                        delegate.onClick();
                }
                isPressed = -1;


                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                isPressed = -1;
                invalidate();
        }
        return true;
    }

    public interface Delegate {
        void onClick();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
