package com.mahdi.car.share;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;

public class Button extends CellView {

    private Delegate delegate;
    private int iconColor = 0xff000000;
    private String title = "Button";

    public Button(Context context) {
        super(context);
    }

    public void setColor(int color) {
        this.iconColor = color;
        invalidate();
    }
    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRoundRect(0, 0, new RectF(0, 0, getWidth(), getHeight()), dp(10), isPressed == 1 ? Themp.PAINT_BLACK : Themp.PAINT_BLUE);
        StaticLayout layout = new StaticLayout(title, Themp.TEXT_PAINT_FILL_AND_STROKE_3_WHITE[8], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        drawTextLayout(layout, 0, 0 + dp(9));

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
