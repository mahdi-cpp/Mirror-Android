package com.mahdi.car.setting.cell;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.MotionEvent;

import com.mahdi.car.core.RootView;
import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Button;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class ProfileCell extends CellFrameLayout {
    private static int itemCount = 0;

    private static final int ITEM_BUTTON = itemCount++;

    private Delegate delegate;

    private StaticLayout nameLayout;

    private int button_y_shift = dp(250);
    private boolean buttonVisible = true;

    Button btnEdit;


    public void startWaiting() {
        buttonVisible = false;
        startStoryRing();
    }

    public ProfileCell(Context context) {
        super(context);

        setName(9, "Mahdi Abdolmaleki");

        round = dp(4);
        avatarSize = dp(100);
        avatarX = centerX - (avatarSize / 2);
        avatarY = dp(30);
        this.cellHeight = width - dp(200);

        spannableShiftX = dp(15);
        spannableShiftY = dp(156);

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 250));

        btnEdit = new Button(context);
        btnEdit.setTitle("Edit");
        btnEdit.setColor(1);
        btnEdit.setFontSize(6);
        addView(btnEdit, LayoutHelper.createFrame(150, 30, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 20));

        setUser();
    }

    public void setUser() {
        User user = new User();
        userid = user.ID;
        username = user.Username;
        user.Avatar = "0044.jpg";
        setAvatar(user.Avatar);
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
                    RootView.instance().showFloatView("Mahdi", "gg");
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
