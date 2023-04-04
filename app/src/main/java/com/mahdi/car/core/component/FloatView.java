package com.mahdi.car.core.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.model.Mirror;
import com.mahdi.car.share.Button;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class FloatView extends CellFrameLayout {

    private Button buttonPlay;

    private StaticLayout toolbarTitleLayout;
    private StaticLayout toolbarNameLayout;

    private StaticLayout titleLayout;
    private StaticLayout mirrorLayout;

    private StaticLayout shareLayout;

    private StaticLayout usernameLayout;
    private StaticLayout connectionTypeLayout;
    private StaticLayout bitrateLayout;
    private StaticLayout resolutionLayout;

    protected ValueAnimator streamAnimator;
    protected ValueAnimator liveAnimator;
    protected ValueAnimator photoAnimator;
    protected int streamCounter = 0;
    protected int liveCounter = 0;
    protected float photoCounter = 0.0F;

    private int space = dp(16);

    private boolean isExpand = false;
    private boolean isScroll = false;

    private String[] str = new String[3];

    public void setParameters(Mirror mirror) {

        if (mirror == null) {
            return;
        }

        str[0] = mirror.connectionType;
        str[1] = mirror.bitrate;
        str[2] = mirror.resolution;

        //User user = new User();
        //userid = user.ID;
        //user.Avatar = "2019-02-02_18-57-04_UTC_profile_pic.jpg";
        //setAvatar(user.Avatar);

        toolbarNameLayout = new StaticLayout(mirror.username, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[6], dp(200), Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        toolbarTitleLayout = new StaticLayout(mirror.title, Themp.TEXT_PAINT_FILL_GREY[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        titleLayout = new StaticLayout("Screen Mirror On Ubuntu Desktop", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[8], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);

        usernameLayout = new StaticLayout("Owner", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        connectionTypeLayout = new StaticLayout("Connection", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        bitrateLayout = new StaticLayout("Bit Rate", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        resolutionLayout = new StaticLayout("Resolution", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        mirrorLayout = new StaticLayout("Mirror", Themp.TEXT_PAINT_FILL_AND_STROKE_2_WHITE[5], dp(60), Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        int round = dp(1);


        canvas.drawRoundRect(new RectF(0, dp(0), getWidth(), getHeight()), dp(22 + round), dp(22 + round), Themp.PAINT_FFEEEEEE);
        canvas.drawRoundRect(new RectF(dp(2), dp(2), getWidth() - dp(2), getHeight() - dp(2)), dp(20 + round), dp(20 + round), Themp.PAINT_WHITE);

        if (isExpand) {
            canvas.save(); //
            canvas.translate(centerX - dp(20), dp(12));
            canvas.drawRoundRect(0, 0, dp(40), dp(5), dp(10), dp(10), Themp.PAINT_GRAY);
            canvas.restore();

        } else {
            canvas.save(); // Small Circle
            {
                canvas.translate(dp(29), -dp(3));
                int size = dp(26);
                int half = size / 2;


                canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(9), Themp.PAINT_RING);
                canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(7), Themp.PAINT_WHITE);
                canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(5), Themp.PAINT_BLACK);

                Themp.STROKE_PAINT_PX_WHITE.setAlpha(250);

                canvas.save();// scale animation
                {
                    int center = Themp.toolbar.cast_large.getWidth() / 2;
                    canvas.scale((float) 0.30 + photoCounter, (float) 0.30 + photoCounter, dp(2), center);
                    canvas.drawBitmap(Themp.toolbar.cast_large, -center - dp(3), dp(3), Themp.ICON_PAINT_SRC_IN_WHITE);
                    canvas.restore();
                }

                canvas.restore();
            }
            drawTextLayout(toolbarNameLayout, dp(65 + 0), dp(9));
            drawTextLayout(toolbarTitleLayout, dp(65 + 0), dp(29));
        }

        //drawTextLayout(descriptionLayout, 0, dp(600));

        if (isScroll | isExpand) {
            canvas.save(); // Ubuntu Desktop
            {
                canvas.translate(centerX, dp(80));

                int size = dp(100);
                int half = size / 2;

                canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(10), Themp.PAINT_RING);
                canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(8), Themp.PAINT_WHITE);
                canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, half + dp(4), Themp.PAINT_BLACK);

                for (int i = 0; i < 20; i++) {
                    Themp.STROKE_PAINT_PX_WHITE.setAlpha(i * 5);
                    canvas.drawCircle(dp(0), Themp.toolbar.cast_large.getWidth() / 2, dp(i) + liveCounter, Themp.STROKE_PAINT_PX_WHITE);
                }
                Themp.STROKE_PAINT_PX_WHITE.setAlpha(250);

                canvas.save();// scale animation
                {
                    int center = Themp.toolbar.cast_large.getWidth() / 2;
                    canvas.scale((float) 0.90 + photoCounter, (float) 0.9 + photoCounter, 0, center);
                    canvas.drawBitmap(Themp.toolbar.cast_large, -center, 0, Themp.ICON_PAINT_SRC_IN_WHITE);
                    canvas.restore();
                }
                canvas.save();
                {
                    round = dp(7);
                    canvas.translate(-dp(30), dp(75));
                    //6canvas.drawRoundRect(-dp(3), -dp(3), dp(60) + dp(3), dp(20) + dp(3), round, round, Themp.PAINT_FFEEEEEE);
                    canvas.drawRoundRect(-dp(2), -dp(2), dp(60) + dp(2), dp(20) + dp(2), round, round, Themp.PAINT_WHITE);
                    canvas.drawRoundRect(0, 0, dp(60), dp(20), round - dp(2), round - dp(2), Themp.PAINT_RING);
                    drawTextLayout(mirrorLayout, 0, dp(1));
                    canvas.restore();
                }
                canvas.restore();
            }

            canvas.save(); // stream circles
            {
                canvas.translate(centerX, dp(225 - 40));
                for (int i = 0; i < 6; i++) {
                    canvas.drawCircle(0, i * dp(11), dp(3), Themp.STROKE_PAINT_PX_GREY);

                    if (streamCounter == i || streamCounter == i + 1 || streamCounter == i + 2 || streamCounter == i + 3) {
                        canvas.drawCircle(0, i * dp(11), dp(2), Themp.PAINT_RING);
                    }
                }
                canvas.restore();
            }
            canvas.save(); // Phone Cell
            {
                float phoneWidth = dp(100);
                float phoneHeight = phoneWidth * 2;
                int round1 = dp(8);
                int round3 = dp(4);
                int inside3 = dp(6);
                canvas.translate(centerX - (phoneWidth / 2), dp(290 - 40));

                canvas.drawRoundRect(0, 0, phoneWidth, phoneHeight, round1, round1, Themp.STROKE_PAINT_1DP_BLACK);

                canvas.drawRoundRect(inside3, inside3 + dp(10), phoneWidth - inside3, phoneHeight - inside3 - dp(11), round3, round3, Themp.PAINT_RING);
                canvas.drawRoundRect(inside3 + dp(2), inside3 + dp(10 + 2), phoneWidth - inside3 - dp(2), phoneHeight - inside3 - dp(11) - dp(2), round3 - dp(2), round3 - dp(1), Themp.PAINT_WHITE);

                canvas.save(); // cell phone Home Speaker and Camera
                canvas.translate(phoneWidth / 2 - dp(20), dp(8));
                canvas.drawRoundRect(0, 0, dp(40), dp(3), dp(10), dp(10), Themp.STROKE_PAINT_1DP_BLACK);
                canvas.drawCircle(-dp(15), dp(1), dp(2), Themp.STROKE_PAINT_1DP_BLACK);
                canvas.restore();

                canvas.save(); // cell phone Home button
                canvas.translate(phoneWidth / 2 - dp(15), phoneHeight - dp(12));
                canvas.drawRoundRect(0, 0, dp(30), dp(7), dp(3), dp(3), Themp.STROKE_PAINT_1DP_BLACK);
                canvas.restore();

                int iconCast = Themp.toolbar.cast.getWidth() / 2;
                canvas.drawBitmap(Themp.toolbar.cast, (phoneWidth / 2) - iconCast, phoneHeight / 2 - iconCast, Themp.ICON_PAINT_MULTIPLY_BLACK);

                canvas.restore();
            }

            drawTextLayout(titleLayout, 0, dp(620));

            canvas.save();
            {
                canvas.translate(dp(0), dp(430));
                int offsetX = dp(10);
                int offsetY = dp(50);

                for (int i = 0; i < 3; i++) {
                    canvas.drawCircle(width / 4 + (i * (width / 4)), offsetY + dp(30), dp(35), Themp.PAINT_FFEEEEEE);
                    shareLayout = new StaticLayout(str[i], Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[7], dp(70), Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
                    drawTextLayout(shareLayout, width / 4 + (i * (width / 4)) - dp(35), offsetY + dp(20));
                }

                drawTextLayout(connectionTypeLayout, width / 7 + dp(5), dp(120));
                drawTextLayout(bitrateLayout, width / 2 - dp(27), dp(120));
                drawTextLayout(resolutionLayout, width / 2 + dp(69), dp(120));


                canvas.restore();
            }
        }

        //canvas.drawLine(centerX, 0, centerX, getHeight(), Themp.PAINT_BLACK);
    }

    public FloatView(Context context) {

        super(context);
        hasStory = false;

        setBackgroundColor(0x00ffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 0));

        buttonPlay = new Button(context);
        buttonPlay.setColor(1);
        buttonPlay.setTitle("Stop Mirroring");
        buttonPlay.setDelegate(new Button.Delegate() {
            @Override
            public void onClick() {

            }
        });

        //addView(buttonPlay, LayoutHelper.createFrame(160, 40, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 50));

        round = dp(18);
        cellWidth = (width - (space * 3)) / 2;
        space = dp(16);


        avatarSize = dp(20);
        avatarX = dp(0);
        avatarY = dp(0);

        streamAnimator = new ValueAnimator();
        streamAnimator.setValues(PropertyValuesHolder.ofInt("counter1", 12, -3));
        streamAnimator.setDuration(1000);
        streamAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //streamAnimator.setInterpolator(interpolator);
        streamAnimator.addUpdateListener(animation -> {
            streamCounter = (int) animation.getAnimatedValue("counter1");
            invalidate();
        });

        liveAnimator = new ValueAnimator();
        liveAnimator.setValues(PropertyValuesHolder.ofInt("counter2", 40, 120));
        liveAnimator.setDuration(700);
        liveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        liveAnimator.addUpdateListener(animation -> {
            liveCounter = (int) animation.getAnimatedValue("counter2");
            invalidate();
        });

        photoAnimator = new ValueAnimator();
        photoAnimator.setValues(PropertyValuesHolder.ofFloat("ali", (float) 0.1, (float) 0.0));
        photoAnimator.setDuration(700);
        photoAnimator.setRepeatCount(10000);
        photoAnimator.setRepeatMode(ValueAnimator.REVERSE);
        photoAnimator.addUpdateListener(animation -> {
            photoCounter = (float) animation.getAnimatedValue("ali");
            invalidate();
        });
        photoAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

//                photoAnimator.setValues(PropertyValuesHolder.ofFloat("ali", (float) 0.55, (float) 0.00));
//                photoAnimator.setRepeatMode();
            }
        });

        liveAnimator.start();
        photoAnimator.start();

        startStreamAnimation();
    }

    public void stopStreamAnimation() {
        streamAnimator.cancel();
        invalidate();
    }

    protected void startStreamAnimation() {
        streamAnimator.start();
    }

    public void setExpand() {
        isExpand = true;
        invalidate();
    }

    public void setCollapse() {
        isExpand = false;
        invalidate();
    }

    public void setScroll(boolean isScroll) {
        this.isScroll = isScroll;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
                    isPressed = 1;

                    new Handler().postDelayed(() -> invalidate(), 100);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (x > 0 && x < getWidth() && isPressed == 1 && delegate != null && y > 0 && y < getHeight() && delegate != null) {
                    delegate.click();
                }

                isPressed = 0;
                invalidate();
                break;

            default:
                isPressed = 0;
                invalidate();
        }
        return false;
    }
}
