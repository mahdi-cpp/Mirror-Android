package com.mahdi.car.share;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.mahdi.car.share.component.ui.LayoutHelper;

public class ViewPagerTabBar extends BaseToolBar
{
    private Delegate delegate;

    private ValueAnimator animator = ValueAnimator.ofInt(0, 100);
    private PropertyValuesHolder property1 = PropertyValuesHolder.ofFloat("counter1", 0.0f, 1.0f);
    private PropertyValuesHolder property2 = PropertyValuesHolder.ofFloat("counter2", 1.0f, 0.0f);

    private String[] names = new String[]{"ACCOUNTS", "TAGS", "PLACES"};
    //private float counter1;
    //private float counter2;
    private int length;
    //private int old_select = 1;
    private boolean isFirst = true;

    private int scroll = 0;

    private int count;
    private int isPressed = -1;
    private int select = 0;

    private int x = 0;
    private boolean permision = true;


    public ViewPagerTabBar(Context context)
    {
        super(context);

        count = 3;
        length = width / count;

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 49));

        animator.setValues(property1, property2);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            //counter1 = (float) animation.getAnimatedValue("counter1");
            //counter2 = (float) animation.getAnimatedValue("counter2");
            //value += 0.01f;
            //counter2 = (int) animation.getAnimatedValue("counter2");
            invalidate();
        });

        addView(shadow, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 3, Gravity.BOTTOM));
    }


    public void setScroll(int scroll)
    {
        if (scroll > 0 && permision) {
            this.scroll = scroll / count;
            invalidate();
        }
    }

    public void onPageSelected(int select)
    {
        this.select = select;
        invalidate();
    }

    public void onPageScrollStateChanged(int select)
    {
        if (select == 0) {
            x = 0;
        } else if (select == 1) {
            x = length;
        } else if (select == 2) {
            x = length + length;
        }

        scroll = 0;

        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        canvas.drawRect(0, 0, width, getHeight() - dp(3), Themp.PAINT_PROFILE_BACKGROUND);

        for (int i = 0; i < count; i++) {
            canvas.save();
            canvas.translate(i * length, dp(10));
            StaticLayout  layout = new StaticLayout(names[i], select == i ? Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[4] : Themp.TEXT_PAINT_FILL_AND_STROKE_2_GREY[4], length, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
            layout.draw(canvas);
            canvas.restore();
        }

        canvas.save();
        canvas.translate(x + scroll, getHeight() - dp(3));
        canvas.drawLine(0, 0, length, 0, Themp.STROKE_PAINT_1DP_BLACK);
        canvas.restore();
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

                //old_select = select;

                if (delegate != null) {

                    if (x < length && y > 0 && y < getHeight() && isPressed == 0) {
                        isFirst = false;
                        //old_select = select;
                        select = 0;
                        animator.start();

                        delegate.select(select);

                    } else if (x > length && x < length * 2 && y > 0 && y < getHeight() && isPressed == 1) {
                        isFirst = false;
                        //old_select = select;
                        animator.start();
                        select = 1;
                        delegate.select(select);
                    } else if (x > select * 2 && x < select * 3 && y > 0 && y < getHeight()) {
                        isFirst = false;
                        //old_select = select;
                        animator.start();
                        delegate.select(select);
                        select = 2;
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
        void select(int index);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
