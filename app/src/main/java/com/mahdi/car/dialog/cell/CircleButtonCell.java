package com.mahdi.car.dialog.cell;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;

import com.mahdi.car.share.Themp;
import com.mahdi.car.core.cell.CellView;

public class CircleButtonCell extends CellView
{
    private Delegate delegate;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int space = 14;
    private boolean isPlus = true;
    private boolean isPressed = false;
    private int value = 0;

    public CircleButtonCell(Context context)
    {
        super(context);

    }

    public void setMinus()
    {
        isPlus = false;
        invalidate();
    }

    public void setValue(int value)
    {
        this.value = value;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        if (isPressed) {
            paint.setColor(0x0b000000);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, dp(21), paint);
        }

        if (value == -1) {

            paint.setColor(0xff00C853);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, dp(18), paint);

            canvas.drawBitmap(Themp.bitmapTick, getWidth() / 2 - (Themp.bitmapTick.getWidth() / 2), getWidth() / 2 - (Themp.bitmapTick.getWidth() / 2), paint);

        } else {
            paint.setColor(0xff000000);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp(1));

            if (isPlus) {
                canvas.drawLine(getWidth() / 2, dp(space), getWidth() / 2, getHeight() - dp(space), paint);
            }

            canvas.drawLine(dp(space), getHeight() / 2, getWidth() - dp(space), getHeight() / 2, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
                    isPressed = true;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (x > 0 && x < getWidth() && y > 0 && y < getHeight() && isPressed && delegate != null)
                    delegate.click();
                isPressed = false;
                invalidate();
                break;

            default:
                isPressed = false;
                invalidate();
        }
        return true;
    }

    public interface Delegate
    {
        void click();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
