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

public class SettingCell extends CellView {
    private StaticLayout nameLayout;
    private StaticLayout blueLayout;
    private StaticLayout versionLayout;
    private Bitmap icon;
    private int color = 0xffffffff;

    public SettingCell(Context context) {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 55));
        setWillNotDraw(false);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setIcon(Bitmap icon, String name) {
        this.icon = icon;
        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setName(String name) {
        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[9], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setEmpty(int height) {
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cellHeight = getHeight();
        centerY = cellHeight / 2;

        if (color != 0xffffffff) {
            drawRect(new RectF(0, 0, width, cellHeight), 0, 0, Themp.PAINT_FFEEEEEE);
        }

        if (isPressed == 1) {
            drawRect(new RectF(0, 0, width, cellHeight), 0, 0, Themp.PAINT_PRESS_BLACK);
        }
        canvas.drawLine(0, 0, width, 0, Themp.PAINT_FFAAAAAA);
        drawBitmap(icon, dp(16), (cellHeight - 72) / 2, Themp.ICON_PAINT_SRC_IN_BLACK);

        drawTextLayout(nameLayout, dp(55), centerY - dp(11));
        drawTextLayout(blueLayout, dp(16), centerY - dp(10));

        drawTextLayout(versionLayout, dp(16), dp(30));

        if (versionLayout != null)
            canvas.drawLine(0, 0, width, 0, Themp.PAINT_FFBBBBBB);
    }
}