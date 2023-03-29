package com.mahdi.car.share.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;


public class TextCell extends FrameLayout {

    public interface Delegate {
        void click();
    }

    private Delegate delegate;

    private static int itemCount = 0;
    private static int ITEM_CLICK = itemCount++;

    public TextView header;
    public TextView messageTextView;
    private int isPressed = -1;


    public TextCell(Context context) {
        super(context);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 80));


        header = new TextView(context);
        //header.setTypeface(App.typeface);
        header.setTextColor(0xffbbbbbb);
        header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        header.setGravity(Gravity.RIGHT);
        header.setText("---");
        addView(header, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.RIGHT, 4, 4 + 8, 12, 0));

        messageTextView = new TextView(context);
        //messageTextView.setTypeface(App.typeface);
        messageTextView.setTextColor(0xff000000);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        messageTextView.setGravity(Gravity.NO_GRAVITY);
        messageTextView.setLineSpacing(1.2f, 1.2f);
        messageTextView.setMaxLines(4);
        messageTextView.setText("....");
        addView(messageTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.RIGHT, 12, 38, 20, 0));

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, 0, getWidth(), 0, Themp.PAINT_GRAY);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(),  Themp.PAINT_GRAY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (y > 0 && y < getHeight()) {
                    isPressed = ITEM_CLICK;

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> invalidate(), 100);
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                if (delegate != null) {

                    if (y > 0 && y < getHeight() && isPressed == ITEM_CLICK) {
                        delegate.click();
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

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
