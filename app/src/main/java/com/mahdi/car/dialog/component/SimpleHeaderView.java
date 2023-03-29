package com.mahdi.car.dialog.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import android.view.Gravity;

import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.dialog.parent.BaseDialogHeader;

public class SimpleHeaderView extends BaseDialogHeader
{
    public SimpleHeaderView(Context context)
    {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 50, Gravity.TOP, 0, 0, 0, 0));

        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), dp(20), dp(20), Themp.PAINT_WHITE);

        RectF rectF2 = new RectF((getWidth() / 2) - dp(20), dp(10), (getWidth() / 2) + dp(20), dp(14));
        canvas.drawRoundRect(rectF2, dp(4), dp(4), Themp.PAINT_FFBBBBBB);

        canvas.drawLine(0, dp(80), width, dp(80), Themp.STROKE_PAINT_PX_FFCCCCCC);
    }

}