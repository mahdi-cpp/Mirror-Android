package com.mahdi.car.direct.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class DirectCell extends CellView {
    private Delegate delegate;

    private static int itemCount = 0;
    private static final int ITEM_CLICK = itemCount++;

    private StaticLayout usernameLayout;
    private StaticLayout nameLayout;

    private User user;

    public DirectCell(Context context) {
        super(context);

        screenH = 74;

        setBackgroundColor(0xffffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, screenH));

        round = dp(4);
        avatarX = dp(20);
        avatarY = dp(7);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user, int position) {
        if (user == null) {
            return;
        }

        this.user = user;
        this.position = position;

        avatarSize = dp(50);

        userid = user.ID;
        verify = user.is_verified;
        username = user.Username;
        String name = user.FullName != null ? user.FullName : "";

        if (username.length() > 32) {
            username = username.substring(0, 32);
            username += "...";
        }

        if (name.length() > 45) {
            name = name.substring(0, 35);
            name += "...";
        }

        name = name == null ? " " : name;
        name = name.length() == 0 ? " " : name;
        String start = name.substring(0, 1);

        usernameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_BLACK[6], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        if (AndroidUtilities.isEnglishWord(start)) {
            nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_GREY[6], width - dp(74), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            int index = nameLayout.getLineEnd(0);
            nameLayout = new StaticLayout(name.substring(0, index), Themp.TEXT_PAINT_FILL_GREY[6], width - dp(74), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } else {
            nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_GREY[6], width - dp(74), Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, false);
            int index = nameLayout.getLineEnd(0);
            nameLayout = new StaticLayout(name.substring(0, index), Themp.TEXT_PAINT_FILL_GREY[6], width - dp(74), Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, false);
        }

        setAvatar(user.Avatar);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isPressed == ITEM_CLICK) {
            canvas.drawRect(new Rect(0, 0, width, getHeight()), Themp.PAINT_PRESS_BLACK);
        }

        avatarY = (getHeight() - avatarSize) / 2;
        avatarY -= dp(3);
        drawAvatar(avatarX, avatarY);

        drawUsername(usernameLayout, avatarX + avatarSize + dp(14), dp(20));
        drawTextLayout(nameLayout, avatarX + avatarSize + dp(14), dp(42));

        canvas.drawBitmap(Themp.toolbar.camera, width - dp(44), (getHeight() - Themp.toolbar.camera.getHeight()) / 2 - dp(4), Themp.ICON_PAINT_SRC_IN_GREY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (y > 0 && y < getHeight()) {
                    isPressed = ITEM_CLICK;
                    new Handler().postDelayed(() -> invalidate(), 60);
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                if (delegate != null) {

                    if (y > 0 && y < getHeight() && isPressed == ITEM_CLICK) {
                        delegate.click(user);
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

    public interface Delegate {
        void click(User user);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
