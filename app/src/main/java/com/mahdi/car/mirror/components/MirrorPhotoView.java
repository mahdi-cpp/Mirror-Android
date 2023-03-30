package com.mahdi.car.mirror.components;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class MirrorPhotoView extends CellView {
    private static int itemCount = 0;

    private static final int ITEM_BUTTON = itemCount++;


    private Delegate delegate;

    private StaticLayout nameLayout;

    private String external_url;

    private int button_y_shift = dp(250);
    private boolean buttonVisible = true;

    private int cellHeight;
    private int height;

    private boolean hasSavedStory = true;

    public void startWaiting() {
        buttonVisible = false;
        startStoryRing();
    }

    public MirrorPhotoView(Context context, boolean isHomeProfile) {
        super(context);

        setName(9, "Mahid");

        round = dp(4);
        avatarSize = dp(100);
        avatarX = centerX - (avatarSize / 2);
        avatarY = dp(50);
        this.cellHeight = width - dp(200);

        spannableShiftX = dp(15);
        spannableShiftY = dp(156);

        setBackgroundColor(0xffffffff);
    }

    public int setUser(User user) {
        if (user == null) {
            return 0;
        }

        userid = user.ID;
        username = user.Username;
        user.Avatar = "00044.jpg";

        external_url = user.Website;

        if (external_url != null) {
            external_url = external_url.replace("http://", "");
            external_url = external_url.replace("https://", "");

            if (external_url.length() > 42) {
                external_url = external_url.substring(0, 42) + "...";
            }
        }

        setAvatar(user.Avatar);

        int linkHeight = external_url == null ? 0 : dp(20);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, AndroidUtilities.pxToDp(height)));

        return height;
    }

    public int getH() {
        return height;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        drawAvatar(avatarX, avatarY);

        //drawRect(new RectF(0,0,getHeight(), getWidth()),0,0, Themp.PAINT_BLUE);

        int y = avatarY + avatarSize + dp(20);

        //canvas.drawBitmap(Themp.toolbar.storyProfileAdd, avatarX + avatarSize - dp(24), avatarY + avatarSize - dp(16), null);

        drawTextLayout(nameLayout, 0, y);

        //-----------------------------------------------------------------

        if (buttonVisible) {
            drawRoundRect(dp(100), button_y_shift, new RectF(0, 0, width - dp(200), dp(40)), dp(5), isPressed == ITEM_BUTTON ? Themp.PAINT_BLACK : Themp.PAINT_BLUE);
            StaticLayout layout = new StaticLayout("Request", Themp.TEXT_PAINT_FILL_AND_STROKE_3_WHITE[8], getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            drawTextLayout(layout, 0, button_y_shift + dp(+7));
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:

                if (x > dp(100) && x < width - dp(100) && y > button_y_shift && y < button_y_shift + dp(50) && buttonVisible) {
                    isPressed = ITEM_BUTTON;
                } else {
                    isPressed = -1;
                }

                invalidate();

                break;
            case MotionEvent.ACTION_UP:

                if (x > dp(100) && x < width - dp(100) && y > button_y_shift && y < button_y_shift + dp(50) && isPressed == ITEM_BUTTON) {
                    startWaiting();
                    delegate.button();
                } else {
                    isPressed = -1;
                }
                //}

                isPressed = -1;
                invalidate();

                if (isPressed >= 0) {
                    //return true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                isPressed = -1;
                invalidate();
                break;
        }

        return true;
    }

    public void setName(int userid, String name) {
        name = name == null ? " " : name;
        name = name.length() == 0 ? " " : name;
        String start = name.substring(0, 1);

        if (AndroidUtilities.isEnglishWord(start)) {
            nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_BLACK[8], width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        } else {
            nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_BLACK[8], width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        }

        invalidate();
    }

    public interface Delegate {
        void button();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }
}
