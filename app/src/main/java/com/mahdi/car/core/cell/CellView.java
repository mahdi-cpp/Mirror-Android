package com.mahdi.car.core.cell;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;


import java.io.File;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.mahdi.car.core.base.DrawableEvent;
import com.mahdi.car.core.base.Share;
import com.mahdi.car.share.Themp;
import com.mahdi.car.App;
import com.mahdi.car.messenger.AndroidUtilities;

public class CellView extends View implements Share
{
    protected Delegate delegate;

    protected static int width = AndroidUtilities.width;
    protected static int screenHeight = AndroidUtilities.screenHeight;
    protected static int statusBarHeight = AndroidUtilities.statusBarHeight;

    protected int cellWidth = width;
    protected int cellHeight = screenHeight;

    protected int centerX = width / 2;
    protected int centerY = screenHeight / 2;

    protected int avatarX;
    protected int avatarY;

    protected int round = dp(10);

    protected int isPressed = -1;
    protected int position = 0;

    protected int spannableShiftX;
    protected int spannableShiftY;

    protected int avatarSize = dp(30);

    protected int userid;
    protected String username;

    protected boolean verify = false;

    protected Drawable avatarDrawable;
    protected Drawable photoDrawable;

    protected ValueAnimator storyAnimator;
    protected int storyCounter = 0;
    protected boolean hasStory = true;
    protected boolean isStoryRing = false;
    protected boolean isStoryShow = false;
    protected int storyState = 0;

    protected int delayStop = 0;

    protected float dX;
    protected float dY;
    protected float pivotX = 0;
    protected float pivotY = 0;
    protected float swipeX = 0;
    protected float swipeY = 0;

    protected Canvas canvas;

    protected ValueAnimator loadingAnimator;
    protected float loadingValue;
    protected boolean loadingShow = false;

    protected LinearOutSlowInInterpolator interpolator = new LinearOutSlowInInterpolator();

    public CellView(Context context)
    {
        super(context);

        storyAnimator = new ValueAnimator();
        storyAnimator.setValues(PropertyValuesHolder.ofInt("counter1", 0, 270));
        storyAnimator.setDuration(2500);
        storyAnimator.setRepeatCount(ValueAnimator.INFINITE);
        storyAnimator.setInterpolator(interpolator);
        storyAnimator.addUpdateListener(animation -> {
            storyCounter = (int) animation.getAnimatedValue("counter1");
            invalidate();
        });
        storyAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                storyCounter = 0;
                super.onAnimationEnd(animation);
            }
        });

        setWillNotDraw(false);
    }


    protected void showLoading()
    {
        loadingAnimator = new ValueAnimator();
        loadingAnimator.setValues(PropertyValuesHolder.ofInt("loadingValue", 0, 360));
        loadingAnimator.setDuration(1000);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setInterpolator(interpolator);
        loadingAnimator.addUpdateListener(animation -> {
            loadingValue = (int) animation.getAnimatedValue("loadingValue");
            invalidate();
        });
        loadingAnimator.start();
    }

    protected void hideLoading()
    {
        loadingAnimator.cancel();
    }

    public void endStoryShow()
    {
        isStoryShow = false;
        isStoryRing = false;
        storyAnimator.cancel();
        invalidate();
    }

    protected void startStoryRing()
    {
        isStoryRing = true;
        storyAnimator.start();

//        FatherView.instance().showStory(this, userid, username, avatarDrawable, avatarSize);
    }

    public int getAvatarPivotX()
    {
        return avatarX;
    }

    public int getAvatarPivotY()
    {
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        return location[1] + avatarY;
    }

    public Drawable getAvatarDrawable()
    {
        return avatarDrawable;
    }

    public int getAvatarSize()
    {
        return avatarSize;
    }

    public int getUserid()
    {
        return userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void stopStoryRing()
    {
        isStoryShow = true;
        isStoryRing = false;
        storyAnimator.cancel();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        this.canvas = canvas;
    }

    public void drawAvatar(int x, int y)
    {
        if (hasStory)
            drawStoryProgress(x, y);

        if (isStoryShow)
            return;

        canvas.save();
        canvas.translate(x, y);

        if (avatarDrawable == null) {
            canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), Themp.PAINT_WHITE);
        } else {
            //            if (clipPath == null)
            //                return;
            //            canvas.clipPath(clipPath);
            avatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
            avatarDrawable.draw(canvas);
        }
        canvas.drawCircle(avatarSize / 2, avatarSize / 2, avatarSize / 2, Themp.STROKE_PAINT_PX_GREY);

        canvas.restore();
    }


    private void drawStoryProgress(int x, int y)
    {
        int circleSize = avatarSize / 2 + dp(4.0f) + dp(1);

        canvas.save();
        canvas.translate(x - dp(4 + 1), y - dp(4 + 1));
        canvas.rotate(120, circleSize, circleSize);

        if (storyState == 0)
            canvas.drawCircle(circleSize, circleSize, circleSize, Themp.PAINT_RING);
        if (storyState == 1)
            canvas.drawCircle(circleSize, circleSize, circleSize, Themp.STROKE_PAINT_1DP_FFCCCCCC);

        if (isStoryRing) {
            canvas.drawArc(new RectF(0, 0, circleSize * 2, circleSize * 2), storyCounter, storyCounter + 45, false, Themp.PAINT_STORY_DASH);
        }

        canvas.drawCircle(circleSize, circleSize, circleSize - dp(1.6f), Themp.PAINT_WHITE);
        canvas.restore();
    }

    protected void drawLoading()
    {
        int size = 144;
        int half = size / 2;

        canvas.save();
        canvas.translate((width / 2 - half), (getHeight() / 2) - half);
        canvas.rotate(loadingValue, half, half);
        canvas.drawBitmap(Themp.Loading.medium, 0, 0, null);
        canvas.restore();
    }

    protected void drawTextLayout(Layout layout, int x, int y)
    {
        if (layout != null) {
            canvas.save();
            canvas.translate(x, y);
            layout.draw(canvas);
            canvas.restore();
        }
    }

    protected void drawUsernameVerifyWhite(Layout layout, int x, int y)
    {
        drawUsername(layout, x, y, true);
    }

    protected void drawUsername(Layout layout, int x, int y)
    {
        drawUsername(layout, x, y, false);
    }

    private void drawUsername(Layout layout, int x, int y, boolean isVerifyWhite)
    {
        if (layout != null) {
            canvas.save();
            canvas.translate(x, y);
            layout.draw(canvas);
            if (verify) {
                int w = (int) layout.getLineWidth(0);
                canvas.drawBitmap(Themp.bitmapVerifiedProfile, dp(5) + w, dp(1), isVerifyWhite ? Themp.PAINT_WHITE : Themp.ICON_PAINT_SRC_IN_BLUE);
            }
            canvas.restore();
        }
    }

    protected void drawRect(RectF rectF, int x, int y, Paint paint)
    {
        if (rectF != null) {
            canvas.save();
            canvas.translate(x, y);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }

    protected void drawRoundRect(Layout layout, int x, int y)
    {
        if (layout != null) {
            canvas.save();
            canvas.translate(x, y);
            layout.draw(canvas);
            canvas.restore();
        }
    }

    protected void drawBitmap(Bitmap bitmap, int x, int y, Paint paint)
    {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }

    protected void drawRoundRect(int x, int y, RectF rect, int round, Paint paint)
    {
        if (rect != null) {
            canvas.save();
            canvas.translate(x, y);
            canvas.drawRoundRect(rect, round, round, paint);
            canvas.restore();
        }
    }


    public void drawMedia(int x, int y, int w, int h)
    {
        if (photoDrawable != null) {
            photoDrawable.setBounds(x, y, w, h);
            photoDrawable.draw(canvas);
        }
    }

    public void setAvatar(String url)
    {
        avatarDrawable = null;
        if (url != null) {

            Glide.with(this).load(App.files + url).transform(new CenterCrop(), new RoundedCorners(dp(200))).into(new Target<Drawable>()
            {
                @Override
                public void onStart()
                {

                }

                @Override
                public void onStop()
                {

                }

                @Override
                public void onDestroy()
                {

                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder)
                {

                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable)
                {

                }

                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
                {
                    avatarDrawable = resource;
                    invalidate();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder)
                {

                }

                @Override
                public void getSize(@NonNull SizeReadyCallback cb)
                {
                    cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                }

                @Override
                public void removeCallback(@NonNull SizeReadyCallback cb)
                {

                }

                @Override
                public void setRequest(@Nullable Request request)
                {

                }

                @Nullable
                @Override
                public Request getRequest()
                {
                    return null;
                }
            });
        }

    }

    protected void setGallery(Drawable[] drawable, int index, String url)
    {
        setGallery(drawable, index, url, null);
    }

    protected void setGallery(Drawable[] drawable, int index, String url, DrawableEvent event)
    {
        Glide.with(this).load(Uri.parse("file:///android_asset/covers/" + url)).into(new Target<Drawable>()
        {

            @Override
            public void onStart()
            {

            }

            @Override
            public void onStop()
            {

            }

            @Override
            public void onDestroy()
            {

            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable)
            {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
            {
                drawable[index] = resource;
                invalidate();

                if (event != null)
                    event.onResourceReady();

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb)
            {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb)
            {

            }

            @Override
            public void setRequest(@Nullable Request request)
            {

            }

            @Nullable
            @Override
            public Request getRequest()
            {
                return null;
            }

        });
    }


    protected void setGallery(Drawable[] drawable, int index, int w, int h, int round, String url)
    {
        setGallery(drawable, index, w, h, round, url, null);
    }

    protected void setGallery(Drawable[] drawable, int index, int w, int h, int round, String url, DrawableEvent event)
    {
        drawable[index] = null;
        invalidate();

        Glide.with(this).load(App.files + url).override(w, h).transform(new CenterCrop(), new RoundedCorners(round)).into(new Target<Drawable>()
        {

            @Override
            public void onStart()
            {

            }

            @Override
            public void onStop()
            {

            }

            @Override
            public void onDestroy()
            {

            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable)
            {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
            {
                drawable[index] = resource;
                invalidate();

                if (event != null)
                    event.onResourceReady();

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb)
            {

            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb)
            {

            }

            @Override
            public void setRequest(@Nullable Request request)
            {

            }

            @Nullable
            @Override
            public Request getRequest()
            {
                return null;
            }

        });
    }

    protected void setMedia(String url)
    {
        if (url == null)
            return;

        setMedia(null, url);
    }

    protected void setMedia(File file)
    {
        if (file == null)
            return;

        setMedia(file, null);
    }

    protected void setMedia(File file, String url)
    {

        photoDrawable = null;

        Glide.with(this).load(file != null ? file : App.files + url).into(new Target<Drawable>()
        {

            @Override
            public void onStart()
            {

            }

            @Override
            public void onStop()
            {

            }

            @Override
            public void onDestroy()
            {

            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable)
            {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
            {
                photoDrawable = resource;
                invalidate();

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb)
            {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb)
            {

            }

            @Override
            public void setRequest(@Nullable Request request)
            {

            }

            @Nullable
            @Override
            public Request getRequest()
            {
                return null;
            }

        });

        invalidate();
    }

    protected void setMedia(String url, int w, int h)
    {
        if (url == null)
            return;

        setMedia(null, url, w, h);
    }

    protected void setMedia(File file, int w, int h)
    {
        if (file == null)
            return;

        setMedia(file, null, w, h);
    }

    protected void setMedia(File file, String url, int w, int h)
    {
        photoDrawable = null;

        Glide.with(this).load(file != null ? file : App.files + url).override(w, h).into(new Target<Drawable>()
        {

            @Override
            public void onStart()
            {

            }

            @Override
            public void onStop()
            {

            }

            @Override
            public void onDestroy()
            {

            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable)
            {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
            {
                photoDrawable = resource;
                invalidate();

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb)
            {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb)
            {

            }

            @Override
            public void setRequest(@Nullable Request request)
            {

            }

            @Nullable
            @Override
            public Request getRequest()
            {
                return null;
            }

        });

        invalidate();
    }

    protected void setMedia(String url, int w, int h, int round)
    {
        if (url == null)
            return;

        setMedia(null, url, w, h, round);
    }

    protected void setMedia(File file, int w, int h, int round)
    {
        if (file == null)
            return;
        setMedia(file, null, w, h, round);
    }

    protected void setMedia(File file, String url, int w, int h, int round)
    {
        photoDrawable = null;
        invalidate();
        Glide.with(this).load(file != null ? file : App.files + url).override(w, h).transform(new CenterCrop(), new RoundedCorners(round)).into(new Target<Drawable>()
        {

            @Override
            public void onStart()
            {

            }

            @Override
            public void onStop()
            {

            }

            @Override
            public void onDestroy()
            {

            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable)
            {

            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
            {
                photoDrawable = resource;
                invalidate();

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder)
            {

            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb)
            {
                cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb)
            {

            }

            @Override
            public void setRequest(@Nullable Request request)
            {

            }

            @Nullable
            @Override
            public Request getRequest()
            {
                return null;
            }

        });
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
                    isPressed = 1;

                    new Handler().postDelayed(this::invalidate, 100);
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
        return true;
    }

    public interface Delegate
    {
        void click();

    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    public int getPosition()
    {
        return position;
    }

    //Utility functions----------------------------------------------------------
    protected static int dp(float value)
    {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(AndroidUtilities.mDensity * value);
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        public Holder(View itemView)
        {
            super(itemView);
        }
    }
    //-------------------------------------------------------------------------------


}
