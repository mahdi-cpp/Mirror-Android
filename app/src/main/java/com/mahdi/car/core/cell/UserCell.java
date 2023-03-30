package com.mahdi.car.core.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.MotionEvent;

import com.mahdi.car.App;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class UserCell extends CellView
{
    private Delegate delegate;

    private static int itemCount = 0;
    private static final int ITEM_STORY = itemCount++;
    private static final int ITEM_CLICK = itemCount++;
    private static final int ITEM_FOLLOWING = itemCount++;

    private StaticLayout buttonLayout;
    private StaticLayout usernameLayout;
    private StaticLayout nameLayout;

    private RectF rectF;

    private User user;

    private int button_width = dp(110);
    private boolean isFollowing = true;
    private boolean isMe = false;
    public boolean isChat = false;

    private boolean hasButton = true;

    public UserCell(Context context)
    {
        super(context);

        screenH = 74;

        setBackgroundColor(0xffffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, screenH));

        round = dp(4);
        rectF = new RectF(0, 0, button_width, dp(30));

        avatarX = dp(20);
        avatarY = dp(7);
    }

    public void setFollowing(boolean isFollowing)
    {
        this.isFollowing = isFollowing;
        invalidate();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user, int position)
    {
        if (user == null) {
            return;
        }

        isMe = false;

        this.user = user;
        this.position = position;

        if (user.ID == App.userid) {
            isMe = true;
        }

        if (isChat)
            avatarSize = dp(50);
        else
            avatarSize = dp(55);

        isFollowing = true;
        userid = user.ID;
        verify = user.is_verified;
        username = user.Username;
        String name = user.FullName != null ? user.FullName : "";

        if (isChat) {
            if (username.length() > 32) {
                username = username.substring(0, 32);
                username += "...";
            }

            if (name.length() > 35) {
                name = name.substring(0, 35);
                name += "...";
            }

        } else {
            if (username.length() > (hasButton ? 16 : 30)) {
                username = username.substring(0, (hasButton ? 16 : 30));
                username += "...";
            }

            if (name.length() > (hasButton ? 25 : 33)) {
                name = name.substring(0, (hasButton ? 25 : 33));
                name += "...";
            }
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

        buttonLayout = new StaticLayout(isFollowing ? "Following" : "Follow", isFollowing ? Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[4] : Themp.TEXT_PAINT_FILL_AND_STROKE_2_WHITE[3], button_width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        setAvatar(user.Avatar);

        invalidate();
    }

    public void setButton(boolean hasButton)
    {
        this.hasButton = hasButton;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (isPressed == ITEM_CLICK) {
            canvas.drawRect(new Rect(0, 0, width, getHeight()), Themp.PAINT_PRESS_BLACK);
        }

        avatarY = (getHeight() - avatarSize) / 2;
        if (isChat)
            avatarY -= dp(3);
        drawAvatar(avatarX, avatarY);

        drawUsername(usernameLayout, avatarX + avatarSize + dp(14), dp(20));
        drawTextLayout(nameLayout, avatarX + avatarSize + dp(14), dp(42));

        if (isChat)
            canvas.drawBitmap(Themp.toolbar.camera, width - dp(44), (getHeight() - Themp.toolbar.camera.getHeight()) / 2 - dp(4), Themp.ICON_PAINT_SRC_IN_GREY);

        if (!isMe && hasButton) {
            canvas.save();
            canvas.translate(width - dp(125), dp(20));

            if (!isFollowing) {
                if (isPressed == ITEM_FOLLOWING) {
                    canvas.drawRoundRect(rectF, round, round, Themp.PAINT_BLUE);
                } else {
                    canvas.drawRoundRect(rectF, round, round, Themp.PAINT_BLACK);
                }
            } else {
                canvas.drawRoundRect(rectF, round, round, isPressed == ITEM_FOLLOWING ? Themp.STROKE_PAINT_PX_GREY : Themp.STROKE_PAINT_PX_GREY);
            }

            drawTextLayout(buttonLayout, 0, dp(7));
            canvas.restore();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x < dp(100) && y > dp(10) && y < getHeight() - dp(10)) {
                    isPressed = ITEM_STORY;
                    invalidate();
                } else if (x > width - button_width + dp(10) && y > dp(16) && y < getHeight() - dp(16)) {
                    isPressed = ITEM_FOLLOWING;
                    invalidate();
                } else if (y > 0 && y < getHeight()) {

                    isPressed = ITEM_CLICK;

                    new Handler().postDelayed(() -> invalidate(), 60);

                } else {
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                if (delegate != null) {

                    if (x < dp(100) && y > dp(10) && y < getHeight() - dp(10) && isPressed == ITEM_STORY) {
                        startStoryRing();
                        invalidate();
                    } else if (x > width - button_width + dp(10) && y > dp(16) && y < getHeight() - dp(16) && isPressed == ITEM_FOLLOWING) {
                        if (!isMe) {
                            delegate.following(isFollowing, position);
                            invalidate();
                        }
                    } else if (y > 0 && y < screenH && isPressed == ITEM_CLICK) {
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

    //    @Override
    //    protected void story(int avatarSize, int pivotX, int pivotY)
    //    {
    //        delegate.story(avatarSize, pivotX, pivotY, avatarDrawable);
    //    }


    public interface Delegate
    {
        //void story(int avatarSize, float pivotX, float pivotY, Drawable avatarDrawable);

        void following(boolean isFollowing, int position);

        void click(User user);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
