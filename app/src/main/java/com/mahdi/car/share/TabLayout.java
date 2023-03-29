package com.mahdi.car.share;


import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.mahdi.car.share.component.ui.LayoutHelper;

public class TabLayout extends BaseToolBar
{
    private Delegate delegate;
    private int count;
    private int finalSelect = 0;
    private int length;
    private int scroll = 0;
    private boolean permissionScroll = true;
    private String[] names;

    public TabLayout(Context context, int count, String[] names)
    {
        super(context);

        this.count = count;
        length = width / count;
        invalidate();

        this.names = names;

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 49));

        shadow = new View(context);
        shadow.setBackground(Themp.drawableToolbar[1]);
        addView(shadow, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 3, Gravity.BOTTOM));
    }

    public void setCurrentItem(int index)
    {
        finalSelect = index;
        invalidate();
    }
    public int getCurrentItem()
    {
        return finalSelect ;
    }

    public void setScroll(int position, int x)
    {
        if (!permissionScroll) {
            return;
        }

        if (finalSelect == 0) {
            scroll = x / count;

        } else if (finalSelect == 1) {

            if (position == 1) {
                scroll = length + (x / count);
            } else {
                scroll = x / count;
            }

        } else if (finalSelect == 2) {

            if ((x / count) > 0) {
                scroll = length + (x / count);
            } else {
                scroll = length + length;
            }
        }

        invalidate();
    }

    public void setSelect(int finalSelect)
    {
        this.finalSelect = finalSelect;
        permissionScroll = false;
        invalidate();
    }

    public void clear()
    {
        permissionScroll = true;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        if (count == 0) {
            return;
        }

        canvas.drawRect(0, 0, width, getHeight(), Themp.PAINT_PROFILE_BACKGROUND);

        if (permissionScroll) {
            canvas.save();
            canvas.translate(scroll, getHeight() - dp(0.5f));
            canvas.drawLine(0, 0, length, 0, Themp.STROKE_PAINT_1DP_BLACK);
            canvas.restore();
        } else {
            canvas.save();
            canvas.translate(finalSelect * length, getHeight() - dp(0.5f));
            canvas.drawLine(0, 0, length, 0, Themp.STROKE_PAINT_1DP_BLACK);
            canvas.restore();
        }

        for (int i = 0; i < count; i++) {
            StaticLayout layout = new StaticLayout(names[i], finalSelect == i ? Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[5] : Themp.TEXT_PAINT_FILL_AND_STROKE_3_GREY[5], width / count, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            drawTextLayout(layout, length * i, getHeight() / 2 - dp(9));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = -1;

                if (x < (getWidth() / count) && y < getHeight()) {
                    isPressed = 0;
                } else if (x > (getWidth() / count) && x < ((getWidth() / count) * 2) && y < getHeight()) {
                    isPressed = 1;
                } else if (x > ((getWidth() / count) * 2) && x < ((getWidth() / count) * 3) && y < getHeight()) {
                    isPressed = 2;
                }
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (delegate != null) {

                    if (x < (getWidth() / count) && y > 0 && y < getHeight() && isPressed == 0) {
                        permissionScroll = true;
                        delegate.accounts();
                        finalSelect = 0;
                    } else if (x > (getWidth() / count) && x < ((getWidth() / count) * 2) && y > 0 && y < getHeight() && isPressed == 1) {
                        permissionScroll = true;
                        delegate.tags();
                        finalSelect = 1;
                    } else if (x > ((getWidth() / count) * 2) && x < ((getWidth() / count) * 3) && y > 0 && y < getHeight()) {
                        permissionScroll = true;
                        delegate.places();
                        finalSelect = 2;
                    }
                }

                isPressed = -1;
                invalidate();
                break;
            default:
                isPressed = -1;
                invalidate();

        }
        return true;
    }

    public interface Delegate
    {
        void accounts();

        void tags();

        void places();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
