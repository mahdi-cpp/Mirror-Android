package com.mahdi.car.share;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class CustomButton extends CellView
{
    private Delegate delegate;

    private StaticLayout nameLayout;

    private boolean isSelect = false;
    private int color = 0xff00C853;
    private boolean enabled = true;

    public CustomButton(Context context)
    {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 120));
    }

    public void enabled(boolean enabled)
    {
        this.enabled = enabled;
        invalidate();
    }

    public void setName(String name)
    {
        nameLayout = new StaticLayout(name, isSelect ? Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[3] : Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[3], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setColor(int color)
    {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        //        paint.setColor(0x11FF4081);
        //        paint.setStrokeWidth(dp(1));
        //        paint.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF(dp(100), dp(40), getWidth() - dp(100), getHeight());

        if (!isSelect) {
            if (isPressed == 1) {
                //paint.setColor(0xff2196F3);
                canvas.drawRoundRect(rectF, dp(20), dp(20), Themp.PAINT_OPACITY_66000000);
            } else {
                //paint.setColor(enabled ? color : 0xff64B5F6);
                canvas.drawRoundRect(rectF, dp(20), dp(20), Themp.PAINT_OPACITY_66000000);
            }
        }


        if (isSelect) {
            //paint.setStyle(Paint.Style.STROKE);
            //paint.setColor(isPressed == 1 ? 0xffeeeeee : 0xffbbbbbb);
            canvas.drawRoundRect(rectF, dp(20), dp(20), isPressed == 1 ? Themp.STROKE_PAINT_PX_FFCCCCCC : Themp.STROKE_PAINT_PX_FFBBBBBB);
        }

        drawTextLayout(nameLayout, 0, getHeight() - dp(27));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x > dp(80) && x < getWidth() - dp(80) && y > 0 && y < getHeight() && enabled) {
                    isPressed = 1;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight() && isPressed == 1 && delegate != null) {
                    delegate.OnClick();
                }
                isPressed = -1;
                invalidate();
                break;
            default:
                isPressed = -1;
                invalidate();
                break;

        }
        return true;
    }

    public interface Delegate
    {
        void OnClick();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
