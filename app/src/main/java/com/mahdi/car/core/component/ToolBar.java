package com.mahdi.car.core.component;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mahdi.car.App;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.share.Themp;

public class ToolBar {
    private static int itemCount = 0;
    private static final int ITEM_LEFT = itemCount++;
    private static final int ITEM_CENTER = itemCount++;
    private static final int ITEM_RIGHT = itemCount++;
    private static final int ITEM_GALLERY = itemCount++;

    private Delegate delegate;

    private int isPressed = -1;

    private int width = AndroidUtilities.width;
    private int height = dp(48);

    private int centerX = width / 2;
    private int centerY = height / 2;

    private int avatarSize = dp(30);
    private boolean verify = false;

    private Drawable avatarDrawable;

    private FrameLayout parent;
    private Context context;
    private Canvas canvas;

    private StaticLayout nameLayout;
    private StaticLayout usernameLayout;
    private StaticLayout secondLayout;
    private StaticLayout rightLayout;

    private int iconSize = dp(55);
    private RectF rect = new RectF(0, 0, iconSize, height);

    private Bitmap[] icons = new Bitmap[8];

    private boolean shadow = true;
    private boolean isBackgroundColorWhite = false;
    private boolean isGallery = false;

    private int avatarX = dp(60);
    private int avatarY = dp(10);

    private int nameX = dp(60);
    private int nameY = dp(15);

    private int secondNameX = dp(60);
    private int secondNameY = dp(14);

    private int usernameX = dp(60);
    private int usernameY = dp(14);

    private int rightNameX = width - dp(53);
    private int rightNameY = dp(13);

    private int iconLeftX = dp(16);
    private int iconLeftY = dp(13);

    private int iconCenterX = dp(85);
    private int iconCenterY = dp(14);

    private int iconRightX = dp(40);
    private int iconRightY = dp(14);

    private int iconFloatX = dp(36);
    private int iconFloatY = dp(14);

    private boolean transparent = false;
    private boolean hasBackground = true;
    private boolean hasAvatar = false;

    private Paint colorRightIcon;

    private ValueAnimator loadingAnimator;
    private boolean isLoading = false;
    private float loadingCounter;
    private float progress;

    public ToolBar(FrameLayout parent) {
        this.parent = parent;
        this.context = parent.getContext();

        icons[0] = Themp.toolbar.finish;

        loadingAnimator = new ValueAnimator();
        loadingAnimator.setValues(PropertyValuesHolder.ofInt("counter1", 0, 360));
        loadingAnimator.setDuration(1000);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setInterpolator(new LinearInterpolator());
        loadingAnimator.addUpdateListener(animation -> {
            loadingCounter = (int) animation.getAnimatedValue("counter1");
            parent.invalidate();
        });
        loadingAnimator.start();

        colorRightIcon = Themp.ICON_PAINT_MULTIPLY_BLACK;


    }

    public boolean getLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;

        if (isLoading)
            loadingAnimator.start();
        else
            loadingAnimator.cancel();
    }

    public void setNext() {
        setRightName(width - dp(53), dp(13), "Next", Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLUE[9]);
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
        parent.invalidate();
    }

    public void settGallery(String directory, int page) {
        isGallery = true;
        directory = directory.length() < 18 ? directory : directory.substring(0, 18) + "...";
        setName(nameX, dp(14), directory, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[9]);
        setLeftIcon(Themp.toolbar.close);

        if (page == 0) {
            float ww = nameLayout.getLineWidth(0);
            setFloatIcon(dp(70) + (int) ww, dp(18), Themp.toolbar.dialog);
            setRightName("Next");
        } else {
            icons[5] = null;
            setRightName("");
        }
        parent.invalidate();
    }

    public void settIgtvGallery(String directory) {
        isGallery = true;
        directory = directory.length() < 18 ? directory : directory.substring(0, 18) + "...";
        setName(nameX, dp(14), directory, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[9]);
        setLeftIcon(Themp.toolbar.close);
        float ww = nameLayout.getLineWidth(0);
        setFloatIcon(dp(70) + (int) ww, dp(18), Themp.toolbar.dialog);
        parent.invalidate();
    }

    public void settIGTV() {
        colorRightIcon = transparent ? Themp.ICON_PAINT_SRC_IN_WHITE : Themp.ICON_PAINT_MULTIPLY_BLACK;

        setName(nameX, nameY, "IGTV", transparent ? Themp.TEXT_PAINT_FILL_AND_STROKE_3_WHITE[7] : Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[7]);
        setCenterIcon(Themp.toolbar.search);

        setRightIcon(Themp.toolbar.plus);
        parent.invalidate();
    }

    public void settFeed() {
        hasBackground = false;
//        setLeftIcon(null);
        setRightIcon(Themp.toolbar.setting_78);
        setLeftIcon(Themp.toolbar.camera);
//        setRightIcon(Themp.toolbar.forward);
//        setFeedLogoIcon(Themp.toolbar.forward);
//        parent.invalidate();
    }

    public void settMirror() {
        hasBackground = false;
        setLeftIcon(Themp.toolbar.remote);
    }


    public void setSetting() {
        hasBackground = false;
        setLeftIcon(Themp.toolbar.AlertProfile);
    }

    public void setMovie() {
        hasBackground = false;
        setLeftIcon(Themp.toolbar.movies);
    }

    public void setMusic() {
        hasBackground = false;
        setLeftIcon(Themp.toolbar.music);

    }

    public void settDirect(String username) {
        setUsername(dp(60), dp(13), username, transparent ? Themp.TEXT_PAINT_FILL_AND_STROKE_3_WHITE[9] : Themp.TEXT_PAINT_FILL_AND_STROKE_4_BLACK[10]);
//        setRightIcon(Themp.bitmapChat[9]);
//        setCenterIcon(Themp.bitmapChat[1]);
        parent.invalidate();
    }

    public void settChat(String url, String username, String full_name) {
        hasAvatar = true;
        setAvatar(url);
        setUsername(dp(100), dp(8), username, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[4]);
        setName(dp(100), dp(28), full_name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_GREY[3]);

        setCenterIcon(Themp.bitmapChat[1]);
        setRightIcon(Themp.bitmapChat[6]);
        parent.invalidate();
    }

    public void setDirect(String username) {
        setUsername(username);
        setRightIcon(Themp.toolbar.menuProfile);
        parent.invalidate();
    }

    public void settExplore() {
        shadow = false;
        setLeftIcon(Themp.toolbar.search);
        setName(dp(46), dp(16), "Search", Themp.TEXT_PAINT_FILL_GREY[8]);
        setRightIcon(Themp.toolbar.microphone);
        parent.invalidate();
    }

    public void setProfile(String username, boolean isHomeProfile, boolean verify) {
        isBackgroundColorWhite = false;
        shadow = false;

        this.verify = verify;
        if (isHomeProfile)
            setLeftIcon(null);

        setUsername(isHomeProfile ? dp(15) : dp(60), dp(12), username);

        if (isHomeProfile)
            setRightIcon(Themp.toolbar.menuProfile);
        else
            setRightIcon(dp(26), dp(16), Themp.toolbar.more);

        setCenterIcon(Themp.toolbar.AlertProfile);

        parent.invalidate();
    }

    public void settEditProfile() {
        colorRightIcon = Themp.ICON_PAINT_SRC_IN_BLUE;
        setName("Edit Profile");
        setLeftIcon(Themp.toolbar.close);
        setRightIcon(dp(36), dp(11), Themp.toolbar.ok);
        parent.invalidate();
    }

    public void setEditPost() {
        colorRightIcon = Themp.ICON_PAINT_SRC_IN_BLUE;
        setName("Edit Info");
        setLeftIcon(Themp.toolbar.close);
        setRightIcon(dp(36), dp(11), Themp.toolbar.ok);
        parent.invalidate();
    }

    public void setFollower(String username) {
        isBackgroundColorWhite = false;
        shadow = false;
        setName(nameX, nameY, username);
        parent.invalidate();
    }

    public void setBookmarkCollection(String collection) {
        setName("Saved");

        if (collection.length() > 30) {
            collection = collection.substring(0, 30) + "...";
        }

        secondLayout = new StaticLayout(collection, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[7], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setBookmark(String collection) {
        setName(dp(60), dp(8), "Saved", Themp.TEXT_PAINT_FILL_AND_STROKE_1_BLACK[2]);

        if (collection.length() > 30) {
            collection = collection.substring(0, 30) + "...";
        }

        setSecondName(dp(60), dp(21), collection, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[6]);

        parent.invalidate();
    }

    public void settHashtag(String hashtag) {
        setName(hashtag);
        setRightIcon(dp(27), dp(17), Themp.toolbar.more);
        parent.invalidate();
    }

    public void setLeftIcon(Bitmap icon) {
        icons[0] = icon;
        parent.invalidate();
    }

    private void setLeftIcon(int x, int y, Bitmap icon) {
        iconLeftX = x;
        iconLeftY = y;
        icons[0] = icon;
        parent.invalidate();
    }

    private void setCenterIcon(Bitmap icon) {
        icons[1] = icon;
        parent.invalidate();
    }

    public void setRightIcon(Bitmap icon) {
        icons[2] = icon;
        parent.invalidate();
    }

    public void setRightIcon(int x, int y, Bitmap icon) {
        iconRightX = x;
        iconRightY = y;
        icons[2] = icon;
        parent.invalidate();
    }

    private void setFloatIcon(int x, int y, Bitmap icon) {
        iconFloatX = x;
        iconFloatY = y;
        icons[5] = icon;
        parent.invalidate();
    }

    private void setFeedLogoIcon(Bitmap icon) {
        icons[4] = icon;
        parent.invalidate();
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
        parent.invalidate();
    }

    public void setName(String name) {
        if (name == null)
            return;
        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[10], dp(400), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setName(int x, int y, String name) {
        nameX = x;
        nameY = y;
        nameLayout = new StaticLayout(name, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[8], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setName(int x, int y, String name, TextPaint paint) {
        nameX = x;
        nameY = y;
        nameLayout = new StaticLayout(name, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setSecondName(int x, int y, String name, TextPaint paint) {
        secondNameX = x;
        secondNameY = y;
        secondLayout = new StaticLayout(name, paint, width / 2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setUsername(int x, int y, String username) {
        usernameX = x;
        usernameY = y;
        usernameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_AND_STROKE_4_BLACK[10], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setUsername(int x, int y, String username, TextPaint paint) {
        usernameX = x;
        usernameY = y;
        usernameLayout = new StaticLayout(username, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setUsername(String username) {
        usernameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLACK[7], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setRightName(String rightName) {
        rightLayout = new StaticLayout(rightName, Themp.TEXT_PAINT_FILL_AND_STROKE_3_BLUE[9], width / 2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void setRightName(int x, int y, String rightName, TextPaint paint) {
        rightNameX = x;
        rightNameY = y;
        rightLayout = new StaticLayout(rightName, paint, width / 2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        parent.invalidate();
    }

    public void onDraw(Canvas canvas) {
        this.canvas = canvas;
    }

    public void init(Canvas canvas) {
        this.canvas = canvas;
    }

    private void drawPressed() {
        if (isPressed == ITEM_LEFT && icons[0] != null) {
            drawRect(rect, 0, 0, Themp.PAINT_PRESS_BLACK);
        } else if (isPressed == ITEM_CENTER && icons[1] != null) {
            drawRect(rect, width - iconSize - iconSize, 0, Themp.PAINT_PRESS_BLACK);
        } else if (isPressed == ITEM_RIGHT && icons[2] != null) {
            drawRect(rect, width - iconSize, 0, Themp.PAINT_PRESS_BLACK);
        } else if (isPressed == ITEM_RIGHT && rightLayout != null) {
            drawRect(new RectF(0, 0, iconSize + dp(15), height), width - iconSize - dp(15), 0, Themp.PAINT_PRESS_BLACK);
        }
    }

    public void dispatchDraw(float dx, float swipeY)//--------------
    {
        //drawShadow();


        canvas.save();
        canvas.translate(dx, swipeY);

        drawShadow();

        drawPressed();

        if (icons[0] != null)//Left Icon
            canvas.drawBitmap(icons[0], iconLeftX, iconLeftY, transparent ? Themp.ICON_PAINT_SRC_IN_BLACK : Themp.ICON_PAINT_MULTIPLY_BLACK);

        if (icons[1] != null)//Center Icon
            canvas.drawBitmap(icons[1], width - iconCenterX, iconCenterY, transparent ? Themp.ICON_PAINT_SRC_IN_WHITE : Themp.ICON_PAINT_MULTIPLY_BLACK);

        if (icons[2] != null && !isLoading)//Right Icon
            canvas.drawBitmap(icons[2], width - iconRightX, iconRightY, colorRightIcon);

        if (icons[4] != null)
            canvas.drawBitmap(icons[4], dp(20), dp(5 + 9), null);

        if (icons[5] != null)
            canvas.drawBitmap(icons[5], iconFloatX, iconFloatY, transparent ? Themp.ICON_PAINT_SRC_IN_WHITE : Themp.ICON_PAINT_MULTIPLY_BLACK);

        drawName();
        drawSecond();
        drawUsername();
        drawRightName();
        drawAvatar(avatarX, avatarY);

        loading();

        canvas.restore();
    }

    public void loading() {
        if (!isLoading)
            return;

        int size = 72;
        int half = size / 2;

        canvas.save();
        canvas.translate((width - dp(38)), dp(13));
        canvas.rotate(loadingCounter, half, half);
        canvas.drawBitmap(Themp.Loading.small, 0, 0, Themp.ICON_PAINT_SRC_IN_BLUE);
        canvas.restore();

        //        canvas.save();
        //        canvas.translate(width - dp(34), dp(16));
        //
        //        canvas.drawCircle(half, half, half, Themp.STROKE_PAINT_1_5DP_FFEEEEEE);
        //        canvas.drawArc(new RectF(0, 0, size, size), counter, counter + 45, false, Themp.STROKE_PAINT_2DP_BLUE);
        //        canvas.restore();
    }

    private void drawShadow() {
        if (transparent)
            return;

        if (hasBackground)
            canvas.drawRect(0, 0, width, height, isBackgroundColorWhite ? Themp.PAINT_WHITE : Themp.PAINT_WHITE);

        if (shadow) {
            Themp.drawableToolbar[0].setBounds(0, height, width, height + dp(3));
            Themp.drawableToolbar[0].draw(canvas);
        }
    }

    private void drawName() {
        if (nameLayout != null) {
            canvas.save();
            canvas.translate(nameX, nameY);
            nameLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawSecond() {
        if (secondLayout != null) {
            canvas.save();
            canvas.translate(secondNameX, secondNameY);
            secondLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawUsername() {
        if (usernameLayout != null) {
            canvas.save();
            canvas.translate(usernameX, usernameY);
            usernameLayout.draw(canvas);
            if (verify) {
                int ww = (int) usernameLayout.getLineWidth(0);
                canvas.drawBitmap(Themp.bitmapVerifiedProfile, dp(5) + ww, dp(4), Themp.ICON_PAINT_SRC_IN_BLUE);
            }
            canvas.restore();
        }
    }

    private void drawRightName() {
        if (rightLayout != null) {
            canvas.save();
            canvas.translate(rightNameX, rightNameY);
            rightLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawRect(RectF rectF, int x, int y, Paint paint) {
        if (rectF != null) {
            canvas.save();
            canvas.translate(x, y);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }

    private void drawAvatar(int x, int y) {
        if (!hasAvatar)
            return;

        canvas.save();
        canvas.translate(x, y);

        if (avatarDrawable == null) {
            canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), Themp.PAINT_WHITE);
        } else {
            avatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
            avatarDrawable.draw(canvas);
        }
        canvas.drawCircle(avatarSize / 2, avatarSize / 2, (avatarSize / 2), Themp.STROKE_PAINT_PX_GREY);

        canvas.restore();
    }

    private void setAvatar(String url) {
        avatarDrawable = null;
        if (url != null) {
            Glide.with(context).load(App.files + url).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(dp(200)))).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    avatarDrawable = resource;
                    parent.invalidate();
                }
            });
        }
        parent.invalidate();
    }

    public void events(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x < dp(60) && y < height) {
                    isPressed = ITEM_LEFT;
                } else if (x > width - dp(120) && x < width - dp(60) && y < height) {
                    isPressed = ITEM_CENTER;
                } else if (x > width - dp(60) && y < height) {
                    isPressed = ITEM_RIGHT;
                } else if (x > dp(60) && x < width - dp(100) && y < height && isGallery) {
                    isPressed = ITEM_GALLERY;
                } else {
                    isPressed = -1;
                }

                parent.invalidate();

                break;

            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (x < dp(60) && y < height && isPressed == ITEM_LEFT) {
                    delegate.leftPressed();
                } else if (x > width - dp(120) && x < width - dp(60) && y < height && isPressed == ITEM_CENTER) {
                    delegate.centerPressed();
                } else if (x > width - dp(60) && y < height && isPressed == ITEM_RIGHT) {
                    delegate.rightPressed();
                } else if (x > dp(60) && x < width - dp(100) && y < height && isGallery) {
                    delegate.rightPressed();
                }

                isPressed = -1;
                parent.invalidate();

                break;

            default:
                isPressed = -1;
                parent.invalidate();
                break;
        }
    }

    public interface Delegate {
        void leftPressed();

        void centerPressed();

        void rightPressed();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    //Utility functions----------------------------------------------------------
    protected int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(AndroidUtilities.mDensity * value);
    }
    //-------------------------------------------------------------------------------

}
