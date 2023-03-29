package com.mahdi.car.story.camera.cell;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;

import android.view.animation.DecelerateInterpolator;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.share.Themp;

public class SlideView extends CellView
{
    private StaticLayout[] layouts;


    private ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
    private PropertyValuesHolder property = PropertyValuesHolder.ofInt("counter", 0, 300);
    private int value;

    private int index = 1;

    public SlideView(Context context)
    {
        super(context);

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 70));
        setBackgroundColor(0x00000000);

        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                value = (int) animation.getAnimatedValue("counter");
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {

            }
        });

        layouts = new StaticLayout[3];

        layouts[0] = new StaticLayout("CREATE", index == 0 ? Themp.TEXT_PAINT_FILL_WHITE[2] : Themp.TEXT_PAINT_FILL_GREY[2], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        layouts[1] = new StaticLayout("NORMAL", index == 1 ? Themp.TEXT_PAINT_FILL_WHITE[2] : Themp.TEXT_PAINT_FILL_GREY[2], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        layouts[2] = new StaticLayout("HANDS-FREE", index == 2 ? Themp.TEXT_PAINT_FILL_WHITE[2] : Themp.TEXT_PAINT_FILL_GREY[2], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);


    }

    public void left()
    {
        if (index == 0) {
            return;
        } else if (index == 1) {
            index = 0;
            property = PropertyValuesHolder.ofInt("counter", 0, dp(68));
            valueAnimator.setValues(property);
            valueAnimator.start();
        } else if (index == 2) {
            index = 1;
            property = PropertyValuesHolder.ofInt("counter", -dp(86), 0);
            valueAnimator.setValues(property);
            valueAnimator.start();
        }


    }

    public void right()
    {
        if (index == 2) {
            return;
        } else if (index == 0) {
            index = 1;
            property = PropertyValuesHolder.ofInt("counter", -dp(68), 0);
            valueAnimator.setValues(property);
            valueAnimator.start();
        } else if (index == 1) {
            index = 2;
            property = PropertyValuesHolder.ofInt("counter", 0, dp(86));
            valueAnimator.setValues(property);
            valueAnimator.start();
        }
    }

    public void select(int index)
    {
        //        if (this.index == 0 && index == 1) {
        //            property = PropertyValuesHolder.ofInt("counter", -dp(85), 0);
        //        } else if (this.index == 1 && index == 0) {
        //            property = PropertyValuesHolder.ofInt("counter", 0, -dp(85));
        //        } else if (this.index == 1 && index == 2) {
        //            property = PropertyValuesHolder.ofInt("counter", 0, -dp(85));
        //        } else if (this.index == 2 && index == 0) {
        //            property = PropertyValuesHolder.ofInt("counter", dp(85), 0);
        //        }
        //
        //        valueAnimator.setValues(property);
        //        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        canvas.save();
        canvas.translate(value, 0);

        drawTextLayout(layouts[0], -dp(70), dp(27));
        drawTextLayout(layouts[0], 0, dp(27));
        drawTextLayout(layouts[0], dp(85), dp(27));

        canvas.restore();
    }
}
