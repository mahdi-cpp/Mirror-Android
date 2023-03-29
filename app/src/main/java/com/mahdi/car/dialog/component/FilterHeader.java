package com.mahdi.car.dialog.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class FilterHeader extends CellFrameLayout
{

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public FilterHeader(Context context)
    {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, Gravity.TOP | Gravity.LEFT, 0, 0, 0, 0));
        setWillNotDraw(false);
    }

    @Override protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dp(1));
        paint.setColor(0xff444444);
        paint.setStyle(Paint.Style.FILL);
        paint.setFilterBitmap(true);

        paint.setColor(0xffeeeeee);
        canvas.drawLine(dp(24), getHeight() - 1, getWidth() - dp(24), getHeight() - 1, paint);

        RectF rectF = new RectF((getWidth() / 2) - dp(30), dp(22), (getWidth() / 2) + dp(30), dp(26));
        paint.setColor(0xffdddddd);
        canvas.drawRoundRect(rectF, dp(4), dp(4), paint);

    }
}
