package com.mahdi.car.direct.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;

import com.mahdi.car.share.component.ui.LayoutHelper;

public class SearchCell extends CellView {
    private Delegate delegate;

    private static int itemCount = 0;
    private static final int ITEM_CLICK = itemCount++;
    private static final int ITEM_FOLLOWING = itemCount++;

    private StaticLayout searchLayout;
    private StaticLayout messageLayout;

    public SearchCell(Context context) {
        super(context);
        setBackgroundColor(0xffffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 80));

        screenH = dp(70);

        searchLayout = new StaticLayout("Search", Themp.TEXT_PAINT_FILL_GREY[7], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        messageLayout = new StaticLayout("Messages", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (isPressed == ITEM_CLICK) {
            canvas.drawRect(new Rect(0, 0, width, screenH), Themp.PAINT_PRESS_BLACK);
        }

        canvas.drawRoundRect(new RectF(dp(16), dp(19), width - dp(16), dp(20 + 37)), dp(8), dp(8), Themp.PAINT_FFEEEEEE);

        drawTextLayout(searchLayout, dp(60), dp(29));

        canvas.drawBitmap(Themp.drawableMap[1], dp(24), dp(28), Themp.ICON_PAINT_MULTIPLY_GREY);
        //canvas.drawBitmap(Themp.direct[0], width - dp(46), dp(26), Themp.ICON_PAINT_MULTIPLY_GREY);

        //drawTextLayout(messageLayout, dp(20), dp(30 + 45));

        canvas.drawLine(0, 0, width, 0, Themp.STROKE_PAINT_1DP_FFAAAAAA);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x > dp(16) && x < dp(154) && y > dp(16) && y < screenH - dp(16)) {
                    isPressed = ITEM_FOLLOWING;
                    invalidate();
                } else if (y > 0 && y < getHeight()) {

                    isPressed = ITEM_CLICK;

                    Handler handler = new Handler();
                    handler.postDelayed(() -> invalidate(), 100);
                } else {
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                if (delegate != null) {

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

    public interface Delegate {
        void click();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
