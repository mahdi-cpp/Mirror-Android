package com.mahdi.car.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;


public class OvalButton extends CellView {

    private Delegate delegate;

    private StaticLayout nameLayout;

    private Bitmap icon1;
    private Bitmap icon2;

    private int edgeSpace = dp(2);


    public OvalButton(Context context) {
        super(context);
        nameLayout = new StaticLayout("CH", Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    public void setIcons(Bitmap icon1, Bitmap icon2) {
        this.icon1 = icon1;
        this.icon2 = icon2;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        centerX = getWidth() / 2;

        if (icon1 != null)
            canvas.drawBitmap(icon1, centerX - (icon1.getWidth() / 2), centerX - (icon1.getWidth() / 2), Themp.ICON_PAINT_MULTIPLY_BLUE);

        if (icon2 != null)
            canvas.drawBitmap(icon2, centerX - (icon2.getWidth() / 2), getHeight() - centerX - dp(17), Themp.ICON_PAINT_MULTIPLY_BLUE);

        //canvas.drawCircle(centerX, centerX, getWidth() / 2 - edgeSpace, Themp.STROKE_PAINT_PX_GREY);
        //canvas.drawCircle(centerX, getHeight() - centerX, getWidth() / 2 - edgeSpace, Themp.STROKE_PAINT_PX_GREY);

        drawRoundRect(0, 0, new RectF(edgeSpace, edgeSpace, getWidth() - edgeSpace, getHeight() - edgeSpace), centerX, Themp.STROKE_PAINT_PX_GREY);

        drawTextLayout(nameLayout, centerX - dp(12), getHeight() / 2 - dp(9));

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
