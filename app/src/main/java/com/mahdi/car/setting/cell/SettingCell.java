package com.mahdi.car.setting.cell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class SettingCell extends CellView
{
    private StaticLayout nameLayout;
    private StaticLayout blueLayout;
    private StaticLayout versionLayout;
    private Bitmap icon;

    public SettingCell(Context context)
    {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 55));
        setWillNotDraw(false);
    }

    public void setIcon(Bitmap icon, String name)
    {
        this.icon = icon;
        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setName(String name)
    {
        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setEmpty(int height)
    {
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, height));
    }

    public void setBlue(String name)
    {
        blueLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLUE[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setVersion(String version)
    {
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 300));
        setBackgroundColor(Themp.color_profile_background);
        versionLayout = new StaticLayout(version, Themp.TEXT_PAINT_FILL_GREY[4], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int h = getHeight();

        if (isPressed == 1) {
            drawRect(new RectF(0, 0, getWidth(), h), 0, 0, Themp.PAINT_PRESS_BLACK);
        }
        drawBitmap(icon, dp(16), (h - 72) / 2, Themp.ICON_PAINT_SRC_IN_BLACK);
        drawTextLayout(nameLayout, dp(50), h / 2 - dp(10));
        drawTextLayout(blueLayout, dp(16), h / 2 - dp(10));

        drawTextLayout(versionLayout, dp(16), dp(30));

        if (versionLayout != null)
            canvas.drawLine(0, 0, width, 0, Themp.PAINT_FFBBBBBB);
    }
}