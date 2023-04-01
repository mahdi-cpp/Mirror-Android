package com.mahdi.car.core.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Button;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class FloatView extends CellFrameLayout {

    private StaticLayout toolbarTitleLayout;
    private StaticLayout toolbarNameLayout;

    private StaticLayout descriptionLayout;
    private StaticLayout titleLayout;

    private StaticLayout videoBitrateLayout;
    private StaticLayout videoSizeLayout;

    private int space = dp(16);

    private boolean isExpand = false;

    private Button buttonPlay;
    private Button buttonPause;

    public FloatView(Context context) {

        super(context);
        hasStory = false;

        setBackgroundColor(0x00ffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 0));

        buttonPlay = new Button(context);
        buttonPlay.setColor(1);
        buttonPlay.setTitle("Play Music");
        buttonPlay.setDelegate(new Button.Delegate() {
            @Override
            public void onClick() {
                setParameters("Maryam", "Music Player");
            }
        });

        buttonPause = new Button(context);
        buttonPause.setColor(1);
        buttonPause.setTitle("Pause Music");
        buttonPause.setDelegate(new Button.Delegate() {
            @Override
            public void onClick() {
                setParameters("Sara", "Movies Stream");
            }
        });


        addView(buttonPlay, LayoutHelper.createFrame(150, 40, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 100));
        addView(buttonPause, LayoutHelper.createFrame(150, 40, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 50));

        round = dp(18);
        cellWidth = (width - (space * 3)) / 2;
        space = dp(16);

        avatarSize = dp(33);
        avatarX = dp(13);
        avatarY = dp(12);

        setParameters("0", "0");
    }

    public void setExpand() {
        isExpand = true;
        invalidate();
    }

    public void setCollapse() {
        isExpand = false;
        invalidate();
    }

    public void setScroll() {
        isExpand = false;
        invalidate();
    }

    public void setParameters(String username, String title) {

        User user = new User();
        userid = user.ID;
        user.Avatar = "2019-11-29_19-03-34_UTC.jpg";
        setAvatar(user.Avatar);

        toolbarNameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[5], dp(200), Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        toolbarTitleLayout = new StaticLayout(title, Themp.TEXT_PAINT_FILL_GREY[5], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        titleLayout = new StaticLayout("Screen Mirror On Ubuntu Desktop", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[11], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);
        descriptionLayout = new StaticLayout("Screen is live on display", Themp.TEXT_PAINT_FILL_GREY[8], width, Layout.Alignment.ALIGN_CENTER, 1.2f, 0.2f, false);

        videoBitrateLayout = new StaticLayout("Bit Rate Quality         2  Mbit/s", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);
        videoSizeLayout = new StaticLayout("Resolution                  488x1080", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.2f, false);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        avatarSize = dp(33);
        avatarX = dp(14);
        avatarY = dp(14);

        int round = dp(1);

        canvas.drawRoundRect(new RectF(0, dp(0), getWidth(), getHeight()), dp(22 + round), dp(22 + round), Themp.PAINT_FFEEEEEE);
        canvas.drawRoundRect(new RectF(dp(2), dp(2), getWidth() - dp(2), getHeight() - dp(2)), dp(20 + round), dp(20 + round), Themp.PAINT_WHITE);

        drawAvatar(avatarX, avatarY);

        drawTextLayout(toolbarNameLayout, dp(65 + 0), dp(9));
        drawTextLayout(toolbarTitleLayout, dp(65 + 0), dp(29));

        canvas.drawBitmap(Themp.toolbar.cast_large, centerX - 100, dp(100), Themp.ICON_PAINT_MULTIPLY_BLACK);

        drawTextLayout(titleLayout, 0, dp(180));
        drawTextLayout(descriptionLayout, 0, dp(230));

        drawTextLayout(videoBitrateLayout, dp(60), dp(350));
        drawTextLayout(videoSizeLayout, dp(60), dp(350 + 25));
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
