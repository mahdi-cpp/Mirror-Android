package com.mahdi.car.remote.components;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;

public class TouchView extends CellView {


    private Delegate delegate;

    public TouchView(Context context, String android_id) {
        super(context);
        round = dp(60);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        centerX = getWidth() / 2;

        canvas.save();
        canvas.translate(0, dp(50));
        //drawRoundRect(dp(20), 0, new RectF(0, 0, getWidth() - dp(40), dp(345)), dp(20), Themp.PAINT_WHITE);
//        Themp.gradientDrawable.setBounds(0, dp(15), getWidth(), getHeight() - dp(20));
//        Themp.gradientDrawable.draw(canvas);

        Themp.drawableToolbar[0].setBounds(0, dp(15), width, getHeight() - dp(55));
        Themp.drawableToolbar[0].draw(canvas);

        Themp.drawableToolbar[1].setBounds(0, dp(15), width, getHeight() - dp(55));
        Themp.drawableToolbar[1].draw(canvas);

        canvas.drawRoundRect(new RectF(dp(0), dp(0), getWidth() - dp(0), getHeight() - dp(40)), round, round, Themp.STROKE_PAINT_30DP_WHITE);
        canvas.restore();

        canvas.save();
        canvas.translate(0, dp(150));


        canvas.restore();
    }

    @Override
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

}