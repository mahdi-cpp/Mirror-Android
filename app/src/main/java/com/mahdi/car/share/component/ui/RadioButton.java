package com.mahdi.car.share.component.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import androidx.annotation.Keep;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.messenger.FileLog;

public class RadioButton extends CellView
{
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private static Paint paint;
    private static Paint eraser;
    private static Paint checkedPaint;

    private int checkedColor;
    private int color;

    private float progress;
    private ObjectAnimator checkAnimator;

    private boolean attachedToWindow;
    private boolean isChecked;
    private int size = dp(16);

    public RadioButton(Context context)
    {
        super(context);
        if (paint == null) {

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(dp(1));
            paint.setStyle(Paint.Style.STROKE);
            checkedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
            eraser.setColor(0);
            eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        try {
            bitmap = Bitmap.createBitmap(dp(size), dp(size), Bitmap.Config.ARGB_4444);
            bitmapCanvas = new Canvas(bitmap);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    @Keep
    public void setProgress(float value)
    {
        if (progress == value) {
            return;
        }
        progress = value;
        invalidate();
    }

    public float getProgress()
    {
        return progress;
    }

    public void setSize(int value)
    {
        if (size == value) {
            return;
        }
        size = value;
    }

    public void setColor(int color1, int color2)
    {
        color = color1;
        checkedColor = color2;
        invalidate();
    }

    public void setBackgroundColor(int color1)
    {
        color = color1;
        invalidate();
    }

    public void setCheckedColor(int color2)
    {
        checkedColor = color2;
        invalidate();
    }

    private void cancelCheckAnimator()
    {
        if (checkAnimator != null) {
            checkAnimator.cancel();
        }
    }

    private void animateToCheckedState(boolean newCheckedState)
    {
        checkAnimator = ObjectAnimator.ofFloat(this, "progress", newCheckedState ? 1 : 0);
        checkAnimator.setDuration(100);
        checkAnimator.start();
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        attachedToWindow = false;
    }

    public void setChecked(boolean checked, boolean animated)
    {
        if (checked == isChecked) {
            return;
        }
        isChecked = checked;

        if (attachedToWindow && animated) {
            animateToCheckedState(checked);
        } else {
            cancelCheckAnimator();
            setProgress(checked ? 1.0f : 0.0f);
        }
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (bitmap == null || bitmap.getWidth() != getMeasuredWidth()) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            try {
                bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                bitmapCanvas = new Canvas(bitmap);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        float circleProgress;
        float innerRad;
        if (progress <= 0.5f) {
            paint.setColor(color);
            checkedPaint.setColor(color);
            circleProgress = progress / 0.5f;
        } else {
            circleProgress = 2.0f - progress / 0.5f;
            int r1 = Color.red(color);
            int rD = (int) ((Color.red(checkedColor) - r1) * (1.0f - circleProgress));
            int g1 = Color.green(color);
            int gD = (int) ((Color.green(checkedColor) - g1) * (1.0f - circleProgress));
            int b1 = Color.blue(color);
            int bD = (int) ((Color.blue(checkedColor) - b1) * (1.0f - circleProgress));
            int c = Color.rgb(r1 + rD, g1 + gD, b1 + bD);
            paint.setColor(c);
            checkedPaint.setColor(c);
        }
        if (bitmap != null) {
            bitmap.eraseColor(0);
            float rad = size / 2 - (1 + circleProgress) * AndroidUtilities.mDensity;
            //bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad, paint);

            if (progress <= 0.5f) {
                bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, dp(8), checkedPaint);
                //bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, AndroidUtilities.dp(4), eraser);
            } else {

                bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, dp(5), checkedPaint);
                bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, dp(11), paint);
            }

            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
