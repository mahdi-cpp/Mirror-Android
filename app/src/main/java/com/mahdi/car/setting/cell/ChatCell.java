package com.mahdi.car.setting.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;


import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.GestureDetector;
import android.view.MotionEvent;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mahdi.car.App;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.Media;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.share.Themp;
import com.mahdi.car.core.cell.CellFrameLayout;

public class ChatCell extends CellFrameLayout
{
    private Delegate delegate;

    private static int itemCount = 0;
    private static final int ITEM_CLICK = itemCount++;

    private static int messageType = 0;
    private static final int MESSAGE_IGTV = messageType++;
    private static final int MESSAGE_POST = messageType++;
    private static final int MESSAGE_TEXT = messageType++;
    private int message;

    private StaticLayout usernameLayout;
    private StaticLayout captionLayout;


    private Paint paintIGTV = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    private Paint circlePaint = new Paint(TextPaint.ANTI_ALIAS_FLAG);


    private Drawable postAvatarDrawable;

    private Post post;
    private Media media;

    private String username;
    private int isPressed = -1;

    private int toolbarHeight = dp(60);
    private int captionHeight = dp(100);

    private int width = AndroidUtilities.width;
    private int height = dp(480);
    private int centerX = width / 2;


    private int mediaCount = 0;

    private boolean isVideo = false;
    private boolean isBuffering = false;


    public int position = 0;

    private int leftCorner = dp(60);
    private int rightCorner = dp(46);

    private int mediaWidth = width - (leftCorner + rightCorner);
    private int mediaHeight = dp(360);


    private String title;

    private GestureDetector gestureDetector;


    public ChatCell(Context context)
    {
        super(context);

        setBackgroundColor(0xffffffff);


        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(0xff888888);
        circlePaint.setStrokeWidth(1);
        //
        //        dotPaint.setColor(0xffbbbbbb);
        //        dotPaint.setStyle(Paint.Style.FILL);
        //
        //        bluePaint.setColor(0xff2196F3);
        //        bluePaint.setStyle(Paint.Style.FILL);
        //
        //        //captionTextPaint.setTextSize(dp(14));


        paintIGTV.setColor(0xffffffff);
        paintIGTV.setStyle(Paint.Style.STROKE);
        paintIGTV.setStrokeWidth(dp(10));

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e)
            {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e)
            {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                float x = e.getX();
                float y = e.getY();
                if (x > leftCorner && x < width - rightCorner && y > 0 && y < toolbarHeight + mediaHeight) {
                    delegate.click(post);
                }

                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                return false;
            }
        });

    }

    public void setPost(String profile_pic_url, Post post, int position)
    {
        if (post == null) {
            return;
        }

        this.post = post;

        mediaCount = post.Medias.size();

        if (post.Medias.size() <= 0) {
            invalidate();
            return;
        }

        this.media = post.Medias.get(0);

        isVideo = media.Video != null;

        username = post.User.Username;
        if (username == null) {
            username = "---";
        }

        getMediaHeight();
        if (post.Igtv) {
            message = MESSAGE_IGTV;
            title = post.title;
            title = title.length() > 40 ? title.substring(0, 40) : title;
        } else {
            message = MESSAGE_POST;
        }

        setAvatar(profile_pic_url);
        setMedia(post.Medias.get(0).Photo, mediaWidth, mediaHeight, 1);

        String str = post.Caption;
        //caption = str;

        if (message == MESSAGE_IGTV) {
            captionLayout = new StaticLayout(title, Themp.TEXT_PAINT_FILL_WHITE[4], dp(140), Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.3f, false);
        } else {
            int t = leftCorner + rightCorner + dp(16);
            String u = "<b>" + username + "   " + "</b>";
            captionLayout = new StaticLayout(AndroidUtilities.fromHtml(u + str, Html.FROM_HTML_MODE_LEGACY), Themp.TEXT_PAINT_FILL_BLACK[4], width - t, Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.3f, false);
            if (captionLayout.getLineCount() > 1) {
                int index = captionLayout.getLineEnd(1);
                if (index != -1 && index <= str.length()) {
                    str = str.substring(0, index);
                    captionLayout = new StaticLayout(AndroidUtilities.fromHtml(u + str + " ...", Html.FROM_HTML_MODE_LEGACY), Themp.TEXT_PAINT_FILL_BLACK[4], width - t, Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.3f, false);
                }
            } else {
                captionLayout = new StaticLayout(AndroidUtilities.fromHtml(u + str, Html.FROM_HTML_MODE_LEGACY), Themp.TEXT_PAINT_FILL_BLACK[4], width - t, Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.3f, false);
            }
        }

        if (message == MESSAGE_IGTV) {
            height = dp(260);
        } else {
            height = toolbarHeight + mediaHeight + captionHeight;
        }

        if (message == MESSAGE_IGTV) {
            username = username.substring(0, 15);
            usernameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_WHITE[3], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } else {
            usernameLayout = new StaticLayout(username, Themp.TEXT_PAINT_FILL_BLACK[4], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }


        setPostAvatar("maede_mohamadi72_47583526_351517509009245_6797802406957595277_n.jpg");
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        setLayoutParams(params);

        invalidate();
    }

    //-------------------------------------------------

    @Override
    protected void dispatchDraw(Canvas canvas)
    {


        if (message == MESSAGE_IGTV) {

        } else {

            if (isPressed == ITEM_CLICK) {
                canvas.save();
                canvas.translate(leftCorner, dp(10));
                canvas.drawRoundRect(new RectF(0, 0, width - leftCorner - rightCorner, height - dp(20)), dp(20), dp(20), Themp.PAINT_PRESS_BLACK);
                canvas.restore();
            }

            canvas.save();
            canvas.translate(leftCorner, dp(10));
            canvas.drawRoundRect(new RectF(0, 0, width - leftCorner - rightCorner, height - dp(20)), dp(20), dp(20), Themp.STROKE_PAINT_1DP_FFDDDDDD);
            canvas.restore();
        }

        if (photoDrawable != null) {
            if (message == MESSAGE_IGTV) {
                drawIGTV(canvas);

            } else {
                photoDrawable.setBounds(leftCorner, toolbarHeight, width - rightCorner, height - captionHeight);
                photoDrawable.draw(canvas);
            }
        }

        super.dispatchDraw(canvas);

        drawToolbar(canvas);

        int shiftY;
        if (message == MESSAGE_IGTV) {
            shiftY = height - dp(110);
        } else {
            shiftY = height - dp(90);
        }
        drawTextLayout(captionLayout, leftCorner + dp(8), shiftY);

    }

    public void drawAvatar(int x, int y)
    {

        canvas.save();
        canvas.translate(x, y);

        if (postAvatarDrawable == null) {
            canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), Themp.PAINT_WHITE);
        } else {
            postAvatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
            postAvatarDrawable.draw(canvas);
        }
        canvas.drawCircle(avatarSize / 2, avatarSize / 2, (avatarSize / 2), Themp.STROKE_PAINT_PX_GREY);

        canvas.restore();
    }

    private void drawIGTV(Canvas canvas)
    {
        int igtv_w = dp(150);
        int igtv_h = dp(230);
        int stroke = dp(5);

        int corner_size = dp(50);

        RectF oval = new RectF(0, 0, corner_size, corner_size);

        canvas.save();
        canvas.translate(leftCorner, (height - igtv_h) / 2);

        photoDrawable.setBounds(0, 0, igtv_w, igtv_h);
        photoDrawable.draw(canvas);

        canvas.save();
        canvas.translate(dp(-5), dp(-5));
        canvas.drawArc(oval, 180, 90, false, paintIGTV);
        canvas.restore();

        canvas.save();
        canvas.translate(igtv_w - corner_size + stroke, -dp(5));
        canvas.drawArc(oval, 270, 90, false, paintIGTV);
        canvas.restore();

        canvas.save();
        canvas.translate(igtv_w - corner_size + stroke, igtv_h - corner_size + stroke);
        canvas.drawArc(oval, 0, 90, false, paintIGTV);
        canvas.restore();

        canvas.restore();
    }

    private void drawToolbar(Canvas canvas)
    {
        int shiftY;

        if (message == MESSAGE_IGTV) {

            avatarSize = dp(26);
            shiftY = height - dp(55);

            canvas.save();
            canvas.translate(leftCorner + dp(8), shiftY);
            canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), Themp.PAINT_FFEEEEEE);
            if (postAvatarDrawable != null) {
                postAvatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
                postAvatarDrawable.draw(canvas);
                canvas.drawCircle(avatarSize / 2, avatarSize / 2, (avatarSize / 2), circlePaint);
            }
            drawTextLayout(usernameLayout, dp(30), dp(5));
            canvas.restore();

        } else {
            avatarSize = dp(30);
            shiftY = dp(20);

            canvas.save();
            canvas.translate(leftCorner + dp(10), shiftY);
            canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), Themp.PAINT_FFEEEEEE);
            if (postAvatarDrawable != null) {
                postAvatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
                postAvatarDrawable.draw(canvas);
                canvas.drawCircle(avatarSize / 2, avatarSize / 2, (avatarSize / 2), circlePaint);
            }
            drawTextLayout(usernameLayout, dp(40), dp(8));
            canvas.restore();
        }


        if (isVideo && isBuffering) {
            canvas.drawBitmap(Themp.postCell[13], width - 132, toolbarHeight, Themp.PAINT_FFEEEEEE);
            canvas.drawBitmap(Themp.postCell[14], width - 132, toolbarHeight, Themp.PAINT_FFEEEEEE);
        }

        drawAvatar(canvas);
    }

    private void drawAvatar(Canvas canvas)
    {
        int shiftY;

        if (message == MESSAGE_IGTV) {
            avatarSize = dp(25);
            shiftY = height - avatarSize - dp(20);
            avatarSize = dp(30);
        } else {
            shiftY = height - avatarSize - dp(10);
        }

        canvas.save();
        canvas.translate(dp(15), shiftY);
        canvas.drawBitmap(Themp.bitmapEmptyProfile, null, new Rect(0, 0, avatarSize, avatarSize), null);
        if (avatarDrawable != null) {
            avatarDrawable.setBounds(0, 0, avatarSize, avatarSize);
            avatarDrawable.draw(canvas);
            canvas.drawCircle(avatarSize / 2, avatarSize / 2, (avatarSize / 2), circlePaint);
        }
        canvas.restore();
    }


    //-------------------------------------------------
    public void setPostAvatar(String url)
    {
        postAvatarDrawable = null;
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
                    postAvatarDrawable = resource;
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

    public int getMediaHeight()
    {
        if (media != null) {

            mediaHeight = (int) (((float) media.Height / media.Width) * dp(360));

            mediaHeight = (int) ((float) mediaHeight / 1.4f);
            return mediaHeight;
        }

        return dp(360);
    }

    public Post getPost()
    {
        return post;
    }

    public String getVideo()
    {
        return App.videos + post.Medias.get(0).Video;
    }

    public boolean isVideo()
    {
        return isVideo;
    }

    private boolean events(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x > leftCorner && x < width - rightCorner && y > 0 && y < toolbarHeight + mediaHeight) {
                    isPressed = ITEM_CLICK;
                } else {
                    isPressed = -1;
                }
                invalidate();

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                isPressed = -1;
                invalidate();
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        events(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public interface Delegate
    {
        void click(Post post);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
