package com.mahdi.car.feed.components;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class FeedView extends CellView {

    private StaticLayout descriptionLayout;
    private StaticLayout titleLayout;
    private StaticLayout connectLayout;

    private boolean isConnected = false;
    private int space = dp(16);


    public FeedView(Context context) {

        super(context);

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 0));

        round = dp(18);
        cellWidth = (width - (space * 3)) / 2;
        space = dp(16);

        init();
    }

    public void setConnection(boolean connection) {
        isConnected = connection;
        invalidate();
    }

    public void init() {

        titleLayout = new StaticLayout("Ubuntu Desktop", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[14], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
        descriptionLayout = new StaticLayout("QmlScrcpy supports displaying and controlling Android devices via USB or WiFi . " +
                "It does NOT require root privileges.", Themp.TEXT_PAINT_FILL_GREY[8], width - dp(60), Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
        connectLayout = new StaticLayout("connected", Themp.TEXT_PAINT_FILL_AND_STROKE_2_WHITE[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        int size = dp(120);
        int half = size / 2;
        int round = dp(7);

        canvas.translate(centerX, dp(100));

        canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(10), isConnected ? Themp.PAINT_GREEN : Themp.PAINT_RED);
        canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(7), Themp.PAINT_WHITE);

        if (isConnected) {
            canvas.save();
            canvas.translate(-dp(43), dp(90));
            canvas.drawRoundRect(-dp(3), -dp(3), dp(86) + dp(3), dp(20) + dp(3), round, round, Themp.PAINT_FFEEEEEE);
            canvas.drawRoundRect(-dp(2), -dp(2), dp(86) + dp(2), dp(20) + dp(2), round, round, Themp.PAINT_WHITE);
            canvas.drawRoundRect(0, 0, dp(86), dp(20), round - dp(2), round - dp(2), Themp.PAINT_GREEN);
            drawTextLayout(connectLayout, dp(7), 0);
            canvas.restore();
        }

        canvas.drawBitmap(Themp.toolbar.screen, -Themp.toolbar.screen.getWidth() / 2, 0, Themp.ICON_PAINT_MULTIPLY_BLACK);
        canvas.restore();


        drawTextLayout(titleLayout, 0, dp(190 + 40));
        drawTextLayout(descriptionLayout, dp(30), dp(260 + 40));

    }

}
