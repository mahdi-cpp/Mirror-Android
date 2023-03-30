package com.mahdi.car.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;


public class CircleButton extends CellView
{

    private Delegate delegate;

    private int iconColor = 0xff000000;
    private Bitmap icon;
    private static final float size = 3.1f;


    public CircleButton(Context context)
    {
        super(context);
    }

    public void setIcon(Bitmap icon)
    {
        this.icon = icon;
        invalidate();
    }

    public void setColor(int color)
    {
        this.iconColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.save();
        canvas.restore();
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / size, Themp.PAINT_WHITE);

        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / size, Themp.STROKE_PAINT_PX_WHITE);

        if (icon != null)
            canvas.drawBitmap(icon, (getWidth() / 2) - (icon.getWidth() / 2), (getWidth() / 2) - (icon.getWidth() / 2), Themp.ICON_PAINT_MULTIPLY_BLUE);


        //paint.setColor(0x88444444);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / size, Themp.STROKE_PAINT_PX_GREY);

        if (isPressed == 1)
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / size, Themp.PAINT_PRESS_BLACK);

    }


    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                isPressed = 1;
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isPressed = -1;

                if (delegate != null)
                    delegate.onClick();
                invalidate();
                break;
        }
        return true;
    }

    public interface Delegate
    {
        void onClick();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
