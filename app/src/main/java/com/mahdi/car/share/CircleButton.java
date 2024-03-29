package com.mahdi.car.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;


public class CircleButton extends CellView {

    private Delegate delegate;

    private int edgeSpace = dp(2);
    private Bitmap icon;

    public CircleButton(Context context) {
        super(context);
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        centerX = getWidth() / 2;

        canvas.save();
        canvas.restore();
        canvas.drawCircle(centerX, centerX, centerX, Themp.PAINT_WHITE);

        canvas.drawCircle(centerX, centerX, centerX, Themp.STROKE_PAINT_PX_WHITE);

        if (icon != null)
            canvas.drawBitmap(icon, centerX - (icon.getWidth() / 2), centerX - (icon.getWidth() / 2), Themp.ICON_PAINT_MULTIPLY_BLUE);


        canvas.drawCircle(centerX, centerX, centerX - edgeSpace, Themp.STROKE_PAINT_PX_GREY);

        if (isPressed == 1)
            canvas.drawCircle(centerX, centerX, centerX - edgeSpace, Themp.PAINT_PRESS_BLACK);

    }


    public boolean onTouchEvent(MotionEvent event) {
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

    public interface Delegate {
        void onClick();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
