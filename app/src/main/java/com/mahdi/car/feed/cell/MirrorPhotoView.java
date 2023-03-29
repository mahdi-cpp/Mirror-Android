package com.mahdi.car.feed.cell;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;

import com.mahdi.car.core.FatherView;
import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class MirrorPhotoView extends CellView {
    private static int itemCount = 0;

    private static final int ITEM_STORY = itemCount++;

    private static final int ITEM_POST = itemCount++;
    private static final int ITEM_FOLLOWERS = itemCount++;
    private static final int ITEM_FOLLOWINGS = itemCount++;

    private static final int ITEM_EDIT_PROFILE = itemCount++;

    private Delegate delegate;

    private StaticLayout nameLayout;
    private StaticLayout categoryLayout;
    private StaticLayout biographyLayout;
    private StaticLayout externalUrlLayout;

    private String[] name = new String[]{"Wi-Fi", "USB", "Signal"};
    private String[] number = new String[]{"12dB", "3.1M", "218"};
    private String[] buttons;
    private String business_category_name;
    private String external_url;
    private String biography;
    private int shift = dp(70);
    private int cell;
    private boolean isHomeProfile;
    private int cellHeight;
    private int height;

    private boolean hasSavedStory = true;

    public MirrorPhotoView(Context context, boolean isHomeProfile) {
        super(context);

        startStoryRing();

        setName(9, "Parsa");

        round = dp(4);
        avatarSize = dp(110);
        avatarX = centerX - (avatarSize / 2);
        avatarY = dp(50);
        this.cellHeight = width - dp(130);
        cell = this.cellHeight / 3;

        spannableShiftX = dp(15);
        spannableShiftY = dp(156);

        this.isHomeProfile = isHomeProfile;

        buttons = new String[]{"Following", "Message"};

        setBackgroundColor(0xffffffff);
    }

    public int setUser(User user) {
        if (user == null) {
            return 0;
        }

        if (user.ID == 61) {
            number[0] = "1,039";
            number[1] = "2.7M";
            number[2] = "178";
        }

        userid = user.ID;
        username = user.Username;

        biography = user.Biography;
        business_category_name = "Waiting For Request ";
        external_url = user.Website;

        if (external_url != null) {
            external_url = external_url.replace("http://", "");
            external_url = external_url.replace("https://", "");

            if (external_url.length() > 42) {
                external_url = external_url.substring(0, 42) + "...";
            }
        }

        setAvatar(user.Avatar);
        setBiographyLayout(null);

        int linkHeight = external_url == null ? 0 : dp(20);
        height = dp(hasSavedStory ? 365 : 260) + linkHeight + biographyLayout.getHeight() + dp(40);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, AndroidUtilities.pxToDp(height)));

        return height;
    }

    public int getH() {
        return height;
    }

    private void setBiographyLayout(MotionEvent event) {
        if (biography == null) {
            biography = "";
        }

        biographyLayout = new StaticLayout(biography, Themp.TEXT_PAINT_FILL_BLACK[5], width - dp(32), Layout.Alignment.ALIGN_NORMAL, 1.15f, 0, false);

        boolean isMoreLine = true;
        if (biography.length() > 30 && biographyLayout.getLineCount() > 1 && !isMoreLine) {
            int index = biographyLayout.getLineEnd(1);
            biography = biography.substring(0, index - 9) + "... more";
            SpannableString spannableString = FatherView.instance().makeSpannableString("", spannableShiftX, spannableShiftY + dp(50), biographyLayout, biography, event);

            spannableString.setSpan(new ForegroundColorSpan(0xff888888), index - 9, biography.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            biographyLayout = new StaticLayout(spannableString, Themp.TEXT_PAINT_FILL_BLACK[5], width - dp(32), Layout.Alignment.ALIGN_NORMAL, 1.15f, 0, false);
        } else {
            SpannableString spannableString = FatherView.instance().makeSpannableString("", spannableShiftX, spannableShiftY + dp(50), biographyLayout, biography, event);
            biographyLayout = new StaticLayout(spannableString, Themp.TEXT_PAINT_FILL_BLACK[5], width - dp(32), Layout.Alignment.ALIGN_NORMAL, 1.15f, 0, false);
        }

        if (business_category_name != null)
            categoryLayout = new StaticLayout(business_category_name, Themp.TEXT_PAINT_FILL_GREY[6], width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        if (external_url != null)
            externalUrlLayout = new StaticLayout(external_url, Themp.TEXT_PAINT_FILL_HASHTAG, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        invalidate();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        drawAvatar(avatarX, avatarY);

        //drawRect(new RectF(0,0,getHeight(), getWidth()),0,0, Themp.PAINT_BLUE);

        int y = avatarY + avatarSize + dp(20);

        if (isHomeProfile) {
            canvas.drawBitmap(Themp.toolbar.storyProfileAdd, avatarX + avatarSize - dp(24), avatarY + avatarSize - dp(16), null);
        } else {
            //canvas.drawCircle(leftMargin + avatarSize / 2, topMargin + avatarSize / 2, (avatarSize / 2) + dp(3), Themp.STROKE_PAINT_1DP_FFDDDDDD);
        }

        drawTextLayout(nameLayout, 0, y);
        drawTextLayout(categoryLayout, 0, y + dp(30));
        drawTextLayout(biographyLayout, spannableShiftX, spannableShiftY);
        int hh = biographyLayout.getHeight();
        drawTextLayout(externalUrlLayout, dp(15), spannableShiftY + hh + dp(8));

        //-----------------------------------------------------------------
        for (int i = 0; i < 3; i++) {
            StaticLayout layout = new StaticLayout(number[i], Themp.TEXT_PAINT_FILL_BOLD_BLACK[7], cell, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            drawTextLayout(layout, shift + cell * i, dp(40 + 220));
        }

        int count = 3;
        for (int i = 0; i < count; i++) {
            StaticLayout layout = new StaticLayout(name[i], Themp.TEXT_PAINT_FILL_BLACK[4], cell, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            drawTextLayout(layout, shift + cell * i, dp(64 + 220));
        }
        //-----------------------------------------------------------------

        canvas.save();
        canvas.translate(0, hasSavedStory ? -dp(105) : 0);

        if (isHomeProfile) {
            RectF rectF = new RectF(dp(100), -dp(5), width - dp(100), dp(30));
            canvas.save();
            canvas.translate(0, getHeight() - dp(90));
            canvas.drawRoundRect(rectF, round, round, Themp.PAINT_WHITE);
            canvas.drawRoundRect(rectF, round, round, Themp.STROKE_PAINT_1DP_FFDDDDDD);
            StaticLayout layout = new StaticLayout("Connect", Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[6], width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            layout.draw(canvas);
            canvas.restore();

        } else {

            RectF rectF = new RectF(0, -dp(6), width / 2 - dp(34), dp(25));

            canvas.save();
            canvas.translate((dp(13)), getHeight() - dp(80 + 20));
            canvas.drawRoundRect(rectF, round, round, Themp.PAINT_WHITE);
            canvas.drawRoundRect(rectF, round, round, Themp.STROKE_PAINT_1DP_FFDDDDDD);
            StaticLayout layout = new StaticLayout("Following", Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[6], width / 2 - dp(40), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            layout.draw(canvas);
            canvas.drawBitmap(Themp.toolbar.aaa, width / 3 - dp(15), dp(4), null);
            canvas.restore();

            canvas.save();
            canvas.translate((width / 2 - dp(13)), getHeight() - dp(80 + 20));
            canvas.drawRoundRect(rectF, round, round, Themp.PAINT_WHITE);
            canvas.drawRoundRect(rectF, round, round, Themp.STROKE_PAINT_1DP_FFDDDDDD);
            layout = new StaticLayout("Message", Themp.TEXT_PAINT_FILL_AND_STROKE_2_BLACK[5], width / 2 - dp(40), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            layout.draw(canvas);
            canvas.restore();

            rectF = new RectF(0, -dp(6), dp(27), dp(25));
            canvas.save();
            canvas.translate(width - dp(40), getHeight() - dp(80 + 20));
            canvas.drawRoundRect(rectF, round, round, Themp.PAINT_WHITE);
            canvas.drawRoundRect(rectF, round, round, Themp.STROKE_PAINT_1DP_FFDDDDDD);
            canvas.drawBitmap(Themp.toolbar.aaa, dp(8), dp(4), null);
            canvas.restore();

        }

        for (int i = 1; i < 4; i++) {

            if (isPressed == i) {
                int cell = this.cell;
                canvas.save();
                canvas.translate(shift + cell * i - (i * dp(1)), dp(15));
                canvas.drawRect(new RectF(0, 0, this.cell, dp(85)), Themp.PAINT_PRESS_BLACK);
                canvas.restore();
            }
        }
        canvas.restore();
    }

    public void event(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:

                //if (isHomeProfile) {

                if (x > dp(10) && x < dp(140) && y > dp(50) && y < dp(150)) {
                    isPressed = ITEM_STORY;
                } else if (x > dp(10) && x < width - dp(10) && y > getHeight() - dp(150) && y < getHeight() - dp(100)) {
                    isPressed = ITEM_EDIT_PROFILE;
                } else if (x > shift && x < shift + cell && y > dp(15) && y < cellHeight) {
                    isPressed = ITEM_POST;
                } else if (x > shift + cell && x < shift + cell + cell && y > dp(15) && y < cellHeight) {
                    isPressed = ITEM_FOLLOWERS;
                } else if (x > shift + cell + cell && x < shift + cell + cell + cell && y > dp(15) && y < cellHeight) {
                    isPressed = ITEM_FOLLOWINGS;
                } else {
                    isPressed = -1;
                }

                //                } else {
                //                     else if (x > shift && x < shift + cell && y > dp(15) && y < cellHeight) {
                //                        isPressed = ITEM_POST;
                //                    } else if (x > shift + cell && x < shift + cell + cell && y > dp(15) && y < cellHeight) {
                //                        isPressed = ITEM_FOLLOWERS;
                //                    } else if (x > shift + cell + cell && x < shift + cell + cell + cell && y > dp(15) && y < cellHeight) {
                //                        isPressed = ITEM_FOLLOWINGS;
                //                    } else {
                //                        isPressed = -1;
                //                    }
                //                }

                if (isPressed >= 0) {
                    new Handler().postDelayed(() -> invalidate(), 60);
                } else {
                    invalidate();
                }

                break;

            case MotionEvent.ACTION_UP:
                startStoryRing();

                setBiographyLayout(event);

                //if (isHomeProfile) {
                if (x > dp(10) && x < dp(140) && y > dp(50) && y < dp(150) && isPressed == ITEM_STORY) {
                    startStoryRing();
                } else if (x > dp(10) && x < width - dp(10) && y > getHeight() - dp(150) && y < getHeight() - dp(100) && isPressed == ITEM_EDIT_PROFILE) {
                    delegate.edit();
                } else if (x > shift && x < shift + cell && y > dp(15) && y < cellHeight && isPressed == ITEM_POST) {
                    delegate.posts();
                } else if (x > shift + cell && x < shift + cell + cell && y > dp(15) && y < cellHeight && isPressed == ITEM_FOLLOWERS) {
                    delegate.follower();
                } else if (x > shift + cell + cell && x < shift + cell + cell + cell && y > dp(15) && y < cellHeight && isPressed == ITEM_FOLLOWINGS) {
                    delegate.following();
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:

                break;

        }
        return false;
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

    public void setValues(long followings, long followers, long posts) {
        //        number[0] = followings + "";
        //        number[1] = followers + "";
        //        number[2] = posts + "";
        invalidate();
    }

    public interface Delegate {
        void posts();

        void follower();

        void following();

        void edit();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }
}
