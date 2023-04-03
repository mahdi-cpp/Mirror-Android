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
    private boolean isScreenMirror = false;

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

    public void setScreenMirror(boolean isScreenMirror) {
        isScreenMirror = isScreenMirror;
        invalidate();
    }

    public void init() {

        titleLayout = new StaticLayout("Ubuntu Desktop", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[12], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
        descriptionLayout = new StaticLayout("Phone Screen Mirror on Ubuntu Desktop", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[6], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
        connectLayout = new StaticLayout("connected", Themp.TEXT_PAINT_FILL_AND_STROKE_2_WHITE[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTextLayout(titleLayout, 0, dp(40));

        canvas.save(); // Ubuntu Desktop
        {
            int size = dp(120);
            int half = size / 2;
            int round = dp(7);

            canvas.translate(centerX, dp(120));
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
        }

        if (isScreenMirror) {
            canvas.save();
            {
                canvas.translate(centerX, dp(230));
                for (int i = 0; i < 5; i++) {
                    canvas.drawCircle(0, i * dp(16), dp(4), Themp.STROKE_PAINT_PX_GREY);
                    canvas.drawCircle(0, i * dp(16), dp(2), Themp.PAINT_GREEN);
                }
                canvas.restore();
            }

            canvas.save(); // Phone Cell
            {
                float phoneWidth = dp(100);
                float phoneHeight = phoneWidth * 2;
                int round1 = dp(8);
                int round3 = dp(3);
                int inside3 = dp(6);
                canvas.translate(centerX - (phoneWidth / 2), dp(325));

                canvas.drawRoundRect(0, 0, phoneWidth, phoneHeight, round1, round1, Themp.STROKE_PAINT_1DP_BLACK);
                canvas.drawRoundRect(inside3, inside3 + dp(10), phoneWidth - inside3, phoneHeight - inside3 - dp(11), round3, round3, Themp.STROKE_PAINT_1DP_BLACK);

                canvas.save(); // cell phone Home Speaker and Camera
                canvas.translate(phoneWidth / 2 - dp(20), dp(8));
                canvas.drawRoundRect(0, 0, dp(40), dp(3), dp(10), dp(10), Themp.STROKE_PAINT_1DP_BLACK);
                canvas.drawCircle(-dp(15), dp(1), dp(2), Themp.STROKE_PAINT_1DP_BLACK);
                canvas.restore();

                canvas.save(); // cell phone Home button
                canvas.translate(phoneWidth / 2 - dp(20), phoneHeight - dp(12));
                canvas.drawRoundRect(0, 0, dp(35), dp(7), dp(3), dp(3), Themp.STROKE_PAINT_1DP_BLACK);
                canvas.restore();

                int iconCast = Themp.toolbar.cast.getWidth() / 2;
                canvas.drawBitmap(Themp.toolbar.cast, (phoneWidth / 2) - iconCast, phoneHeight / 2 - iconCast, Themp.ICON_PAINT_MULTIPLY_BLACK);

                canvas.restore();
            }
        }

        //drawTextLayout(descriptionLayout, 0, dp(550));

    }

}
