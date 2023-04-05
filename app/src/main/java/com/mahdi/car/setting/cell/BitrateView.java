package com.mahdi.car.setting.cell;


import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class BitrateView extends CellView {

    private Delegate delegate;

    private StaticLayout titleLayout;

    private StaticLayout quality720Layout;
    private StaticLayout quality1080Layout;
    private StaticLayout quality1280Layout;
    private StaticLayout quality1920Layout;

    private final int count = 4;
    private int index = 0;
    private int select = 0;
    private int topMargin = dp(90);
    private int width = AndroidUtilities.width;
    public static int center = 0;

    public static int SHIFT_Y = dp(55);
    public static int CIRCLE_SIZE = dp(30);

    public BitrateView(Context context) {

        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 130));
        setWillNotDraw(false);
        center = ((((width / count) * (1 * 2 + 1)) - Themp.mainToolBarIcons[1].getWidth()) / 2) + (Themp.mainToolBarIcons[1].getWidth() / 2);

        titleLayout = new StaticLayout("Bit Rate", Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        quality720Layout = new StaticLayout("1 Mbit/s", Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        quality1080Layout = new StaticLayout("2 Mbit/s", Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        quality1280Layout = new StaticLayout("4 Mbit/s", Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        quality1920Layout = new StaticLayout("8 Mbit/s", Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        cellHeight = getHeight();

        canvas.drawRect(0, 0, width, cellHeight, Themp.PAINT_WHITE);
        canvas.drawLine(0, 0, width, 0, Themp.PAINT_FFAAAAAA);

        drawTextLayout(titleLayout, dp(15), dp(11));


        for (int i = 0; i < count; i++) {

            int size = width / count;
            int x = (i * size) + (Themp.mainToolBarSelectedIcons[0].getWidth()) + dp(15);
            int y = 15;

            switch (i) {
                case 0:
                    drawTextLayout(quality720Layout, x - dp(15), dp(50));
                    break;
                case 1:
                    drawTextLayout(quality1080Layout, x - dp(15), dp(50));
                    break;
                case 2:
                    drawTextLayout(quality1280Layout, x - dp(15), dp(50));
                    break;
                case 3:
                    drawTextLayout(quality1920Layout, x - dp(15), dp(50));
                    break;
            }

            if (i < count - 1) {
                canvas.drawCircle((int) (x + size / 4), topMargin, dp(2), Themp.PAINT_FFAAAAAA);
                canvas.drawCircle((int) (x + size / 2.4), topMargin, dp(2), Themp.PAINT_FFAAAAAA);
                canvas.drawCircle((int) (x + size / 1.7), topMargin, dp(2), Themp.PAINT_FFAAAAAA);
                canvas.drawCircle((int) (x + size / 1.35), topMargin, dp(2), Themp.PAINT_FFAAAAAA);
            }

            if (select == i) {
                canvas.drawCircle(x, topMargin, dp(15), Themp.STROKE_PAINT_PX_GREY);
                canvas.drawCircle(x, topMargin, dp(6), Themp.PAINT_FF444444);

            } else {
                canvas.drawCircle(x, topMargin, dp(15), Themp.STROKE_PAINT_PX_GREY);
                //canvas.drawCircle(x, topMargin, dp(8), Themp.PAINT_FF444444);
            }
        }
    }

    public void setBitrate(int bitrateValue) {
        switch (bitrateValue) {
            case 1:
                select = 0;
                break;
            case 2:
                select = 1;
                break;
            case 4:
                select = 2;
                break;
            case 8:
                select = 3;
                break;
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int count = 4;
        int cellHeight = getHeight() - dp(15);
        int cell = width / count;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x < cell && y > SHIFT_Y) {
                    isPressed = 0;
                } else if (x > cell && x < (cell * 2) && y > SHIFT_Y && y < cellHeight) {
                    isPressed = 1;
                } else if (x > (cell * 2) && x < (cell * 3) && y > SHIFT_Y && y < cellHeight) {
                    isPressed = 2;
                } else if (x > (cell * 3) && x < (cell * 4) && y > SHIFT_Y && y < cellHeight) {
                    isPressed = 3;
                } else if (x > (cell * 4) && x < (cell * 5) && y > SHIFT_Y && y < cellHeight) {
                    isPressed = 4;
                } else {
                    isPressed = -1;
                }

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                if (x < cell && y > SHIFT_Y && y < cellHeight && isPressed == 0) {
                    select = 0;
                    delegate.click(1);
                } else if (x > cell && x < (cell * 2) && y > SHIFT_Y && y < cellHeight && isPressed == 1) {
                    select = 1;
                    delegate.click(2);
                } else if (x > (cell * 2) && x < (cell * 3) && y > SHIFT_Y && y < cellHeight && isPressed == 2) {
                    select = 2;
                    delegate.click(4);
                } else if (x > (cell * 3) && x < (cell * 4) && y > SHIFT_Y && y < cellHeight && isPressed == 3) {
                    select = 3;
                    delegate.click(8);
                } else if (x > (cell * 4) && x < (cell * 5) && y > SHIFT_Y && y < cellHeight && isPressed == 4) {
                    select = 4;
                    delegate.click(4);
                }

                isPressed = -1;
                invalidate();
                break;
        }

        return true;
    }


    public interface Delegate {
        void click(int bitrate);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
