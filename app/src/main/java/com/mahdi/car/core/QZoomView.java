package com.mahdi.car.core;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.mahdi.car.App;

import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.setting.SettingFragment;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class QZoomView
{
//    private MenuView menuView;
    private View menuCover;

    private Activity parentActivity = null;
    private FrameLayout contentView;
    private View view;
    private Drawable postDrawable;
    private int width = AndroidUtilities.width;
    private int height;
    private int topMargin;

    private float dX;
    private float dY;
    private float pivotX;
    private float pivotY;
    private float scale;

    private boolean isShow = false;
    private boolean isVideo = false;

    private ValueAnimator resetZoomAnimator;

    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;

    private int downX;
    private int downY;

    private int _xDelta;
    private int _yDelta;
    private boolean isSmall = false;

    private boolean isMove = false;
    private boolean isAnimationBusy = false;

    private boolean touchEnabled = true;

    private int centerX = width / 2;

    private boolean isMenuShow = false;

//    private PostCell cell;

    //protected BlurringView blurringView;

    private static volatile QZoomView Instance = null;

    public static QZoomView getInstance()
    {
        QZoomView localInstance = Instance;
        if (localInstance == null) {
            synchronized (QZoomView.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new QZoomView();
                }
            }
        }
        return localInstance;
    }

    public void setParentActivity(final Activity activity)
    {
        if (activity == null || parentActivity == activity) {
            return;
        }

        parentActivity = activity;

        contentView = new FrameLayout(parentActivity);
        contentView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        paint.setColor(0xfffffff);

        view = new View(parentActivity)
        {
            @Override
            public void onDraw(Canvas canvas)
            {
                super.onDraw(canvas);

                if (postDrawable != null) {
                    canvas.save();
                    canvas.translate(dX, dY);
                    canvas.scale(scale, scale, pivotX, AndroidUtilities.dp(350));
                    postDrawable.setBounds(0, topMargin, width, topMargin + height);
                    postDrawable.draw(canvas);
                    canvas.restore();
                }
            }
        };

        view.setWillNotDraw(false);
        view.setBackgroundColor(0x00000000);
        view.setVisibility(INVISIBLE);

        contentView.addView(view, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        resetZoomAnimator = new ValueAnimator();
        resetZoomAnimator.setDuration(230);
        resetZoomAnimator.setInterpolator(new DecelerateInterpolator());
        resetZoomAnimator.addUpdateListener(animation -> {
            scale = (float) animation.getAnimatedValue("zoomProperty");
            dX = (float) animation.getAnimatedValue("dxProperty");
            dY = (float) animation.getAnimatedValue("dyProperty");
            //cell.zoomReset(dX, dY, scale);
            if (isVideo) {
                FatherView.instance().zoom(dX, dY, pivotX, pivotY, scale);
            }
            view.invalidate();
        });

        resetZoomAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                view.setVisibility(INVISIBLE);
                isShow = false;
                if (isVideo) {
                    FatherView.instance().zoomClear(contentView);
                }
                FatherView.instance().setX(0);
            }
        });

        //blurringView = new BlurringView(parentActivity);
        //contentView.addView(blurringView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        //addQuiz();
    }

    public FrameLayout getView()
    {
        return contentView;
    }

    public void setPivot(float pivotY)
    {
        if (pivotY > AndroidUtilities.height - AndroidUtilities.dp(200)) {
            pivotY = AndroidUtilities.height;
        }

        this.pivotY = pivotY;
    }

//    public void show(PostCell cell, Drawable postDrawable, int height, int topMargin, boolean isVideo)
//    {
//        if (postDrawable == null || isShow) {
//            return;
//        }
//
//        this.cell = cell;
//        this.isShow = true;
//        this.isVideo = isVideo;
//
//        if (isVideo) {
//            FatherView.instance().zoomParent(contentView);
//        } else {
//            this.height = height;
//            this.topMargin = topMargin;
//            this.postDrawable = postDrawable;
//            view.setVisibility(VISIBLE);
//        }
//
//        //AndroidUtilities.hideStatusBar();
//        //AndroidUtilities.hideNavigation();
//        FatherView.instance().setBackgroundColor(0x00000000);
//    }

    public void update(float dX, float dY, float pivotX, float pivotY, float scale)
    {
        this.dX = dX;
        this.dY = dY;
        this.pivotX = pivotX;
        this.scale = scale;

        if (isVideo) {
            FatherView.instance().zoom(dX, dY, pivotX, AndroidUtilities.dp(110), scale);
        } else {
            view.invalidate();
        }
    }

    public void hide()
    {
        if (!isShow) {
            return;
        }

        //Log.e("QZoomView", "hide");

        PropertyValuesHolder zoomProperty = PropertyValuesHolder.ofFloat("zoomProperty", scale, 1.0f);
        PropertyValuesHolder dxProperty = PropertyValuesHolder.ofFloat("dxProperty", dX, 0);
        PropertyValuesHolder dyProperty = PropertyValuesHolder.ofFloat("dyProperty", dY, 0);
        resetZoomAnimator.setValues(zoomProperty, dxProperty, dyProperty);
        resetZoomAnimator.start();


        AndroidUtilities.showNavigation();
        AndroidUtilities.showStatusBar();
    }

    public boolean isShow()
    {
        return isShow;
    }

    public void showBlur(View view)
    {
        //Bitmap bitmap = getBitmapFromView(view);


        //        RenderScript renderScript = RenderScript.create(parentActivity);
        //        bitmap = new RSBlurProcessor(renderScript).blur(getBitmapFromView(view), 15, 1);
        //
        //        this.view.setVisibility(VISIBLE);
        //        this.view.invalidate();


        //        blurringView.setVisibility(VISIBLE);
        //        blurringView.setBlurredView(view);
        //        blurringView.invalidate();
        //        containerView.setVisibility(VISIBLE);
    }

    public static Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }

    private final class ChoiceTouchListener implements View.OnTouchListener
    {
        public boolean onTouch(View view, MotionEvent event)
        {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();

            if (event.getPointerCount() == 2) {
                //imageView.setVisibility(INVISIBLE);
                touchEnabled = false;
                view.setClickable(false);
                view.setOnTouchListener(null);
            }

            if (!touchEnabled) {
                return false;
            }

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:

                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = x - lParams.leftMargin;
                    _yDelta = y - lParams.topMargin;

                    downX = x;
                    downY = y;


                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:

                    Handler handler = new Handler();
                    handler.postDelayed(() -> isMove = false, 50);

                    if (view.getScaleX() != 1) {
                        //viewCancelSelect(view);
                    }

                    break;

                case MotionEvent.ACTION_MOVE:

                    int dx = Math.abs(x) - Math.abs(downX);
                    int dy = Math.abs(y) - Math.abs(downY);

                    if (Math.abs(dx) > AndroidUtilities.dp(4) || Math.abs(dy) > AndroidUtilities.dp(4)) {
                        isMove = true;
                    } else {
                        isMove = false;
                        return false;
                    }

                    if (view.getScaleX() != 1) {
                        //viewCancelSelect(view);
                    }

                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = x - _xDelta;
                    layoutParams.topMargin = y - _yDelta;
                    //layoutParams.rightMargin = -250;
                    //layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);

                    //view.setTranslationX(_xDelta);
                    //view.setTranslationY(_yDelta);

                    if (x > centerX - AndroidUtilities.dp(25) && x < centerX + AndroidUtilities.dp(25) && y > height - AndroidUtilities.dp(50)) {

                        if (!isSmall) {
                            isSmall = true;

                            ViewAnimator.animate(view).setInterpolator(new DecelerateInterpolator())
                                    //.topMargin(0)
                                    //.leftMargin(0)
                                    //.translationX(0)
                                    //.translationY(0)
                                    .scales(0.3f)

                                    .setDuration(200).addListener(new AnimatorListenerAdapter()
                            {
                                @Override
                                public void onAnimationEnd(Animator animation)
                                {
                                    //removeView(view);
                                    super.onAnimationEnd(animation);
                                }
                            }).start();


                            Vibrator vb = (Vibrator) App.getApplication().getSystemService(Context.VIBRATOR_SERVICE);
                            vb.vibrate(10);
                            return false;
                        }

                    } else if (isSmall) {

                        ViewAnimator.animate(view).setInterpolator(new DecelerateInterpolator()).scales(1.0f).setDuration(200).addListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                isSmall = false;
                                //removeView(view);
                                super.onAnimationEnd(animation);
                            }
                        }).start();
                    }

                    break;
            }

            return false;
        }
    }

    public void hideMenu(boolean animation)
    {
//        if (!menuView.isShow) {
//            return;
//        }

//        menuView.hide(animation);

        isMenuShow = false;
//        menuView.isShow = false;
        menuCover.setVisibility(INVISIBLE);
//        menuView.setVisibility(INVISIBLE);
    }

    public boolean isMenuShow()
    {
        return isMenuShow;
    }

    public void showMenu(FrameLayout parent)
    {
        isMenuShow = true;

//        menuView = new MenuView(parentActivity, parent);
//        menuView.setDelegate(new MenuView.Delegate()
//        {
//            @Override
//            public void archive()
//            {
//                hideMenu(false);
//                FatherView.instance().presentFragment(new ArchiveFragment());
//            }
//
//            @Override
//            public void bookmark()
//            {
//                hideMenu(false);
//                FatherView.instance().presentFragment(new BookmarkCollectionFragment());
//            }
//
//            @Override
//            public void post()
//            {
//                hideMenu(false);
//                FatherView.instance().presentFragment(new GalleryFragment());
//            }
//
//            @Override
//            public void star()
//            {
//
//            }
//
//            @Override
//            public void setting()
//            {
//                hideMenu(false);
//                FatherView.instance().presentFragment(new SettingFragment());
//            }
//        });

//        menuView.setTranslationX(width);

        menuCover = new View(parentActivity);
        menuCover.setClickable(true);
        //menuCover.setBackgroundColor(0x44ff9800);
        menuCover.setOnClickListener(v -> {
            menuCover.setVisibility(View.GONE);
//            menuView.hide(true);
        });

//        contentView.addView(menuView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, AndroidUtilities.pxToDp(AndroidUtilities.statusBarHeight), 0, 0));
        contentView.addView(menuCover, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 220, 0));

        menuCover.setVisibility(VISIBLE);
//        menuView.setVisibility(VISIBLE);
//        menuView.show();
    }

    public void addCamera(FrameLayout frameLayout)
    {
        contentView.addView(frameLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

}
