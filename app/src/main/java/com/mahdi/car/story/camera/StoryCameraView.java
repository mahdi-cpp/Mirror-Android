package com.mahdi.car.story.camera;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.messenger.MediaController;
import com.mahdi.car.messenger.NotificationCenter;
import com.mahdi.car.share.component.ui.LayoutHelper;

import java.util.ArrayList;

public class StoryCameraView extends CellFrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private FrameLayout feesContentView;
    private Activity parentActivity;
    private Context context;
    private StoryCameraControllerView cameraView;
//    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;

    //private ArrayList<MediaController.PhotoEntry> photos = new ArrayList<>();
//    private Adapter adapter;

    private boolean busyAnimation = false;
    private boolean isShow = false;
    private boolean isGalleryShow = false;
    private boolean permissionClick = true;
    private int h = screenHeight + statusBarHeight;
    private float velocityX;

    public StoryCameraView(Context context, Activity parentActivity, FrameLayout feedContentView) {
        super(context);

        this.context = context;
        this.parentActivity = parentActivity;
        this.feesContentView = feedContentView;
        setBackgroundColor(0x44ff9800);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.albumsDidLoaded);

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                cameraView.click(e);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                processScroll(e1, distanceX, distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                StoryCameraView.this.velocityX = velocityX;
                return false;
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //define span size for this position
                //some example for your first three items
                if (position == 0) {
                    return 3; //item will take 1/3 space of row
                } else {
                    return 1; //you will have 2/3 space of row
                }
            }
        });

//        recyclerView = new RecyclerView(context);
//        recyclerView.setBackgroundColor(0xff000000);
//        recyclerView.setAdapter(adapter = new Adapter(context));
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                //Log.e("onScrollStateChanged", "newState:" + newState);
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                Log.e("onScrolled***", "newState:" + recyclerView.getTranslationY());
//                permissionClick = true;
//
//            }
//        });

        MediaController.loadGalleryPhotosAlbums(0);

        if (Build.VERSION.SDK_INT >= 23 && parentActivity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            parentActivity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 19);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                dX = 0;
                dY = 0;
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:

                if (cameraView.getTranslationY() != 0) {
                    resetY();
                } else if (cameraView.getTranslationX() != 0) {
                    resetX();
                }

                cameraView.resetX();

                break;
        }

        return super.dispatchTouchEvent(ev);

    }

    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent ev)
    //    {
    //
    //
    //        //return super.onInterceptTouchEvent(ev);
    //    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent ev)
    //    {
    //        gestureDetector.onTouchEvent(ev);
    //
    //        switch (ev.getAction()) {
    //
    //            case MotionEvent.ACTION_DOWN:
    //                dX = 0;
    //                dY = 0;
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //
    //                break;
    //            case MotionEvent.ACTION_UP:
    //            case MotionEvent.ACTION_CANCEL:
    //            case MotionEvent.ACTION_POINTER_UP:
    //
    //                if (cameraView.getTranslationY() != 0) {
    //                    resetY();
    //                } else if (cameraView.getTranslationX() != 0) {
    //                    resetX();
    //                }
    //
    //                cameraView.resetX();
    //
    //                break;
    //        }
    //
    //        return super.onTouchEvent(ev);
    //    }

    private void resetX() {
        if (dX < -centerX || velocityX < -1000) {
            onBackPressed();
        } else {
            ViewAnimator.animate(cameraView).setInterpolator(new DecelerateInterpolator()).translationX(0).setDuration(250).start();
//            ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationX(0).setDuration(250).start();
            ViewAnimator.animate(feesContentView).setInterpolator(new DecelerateInterpolator()).translationX(width).setDuration(250).start();
        }
    }

    private void resetY() {
        if (cameraView.getTranslationY() > -dp(150) || (cameraView.getTranslationY() > -dp(400) && isGalleryShow)) {
            isGalleryShow = false;
            cameraView.hideGallery();
            ViewAnimator.animate(cameraView).setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(250).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            }).start();
//            ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationY(h).setDuration(250).start();
        } else {
            isGalleryShow = true;
            cameraView.showGallery();
            ViewAnimator.animate(cameraView).setInterpolator(new DecelerateInterpolator()).translationY(-h + dp(100)).setDuration(250).addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            }).start();
//            ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationY(dp(100)).setDuration(250).start();
        }
    }

    //    @Override
    //    public boolean onT(MotionEvent event)
    //    {
    //
    //    }

    public void setTranslate(float dX) {
        cameraView.translationX(dX);
//        recyclerView.setTranslationX(dX);
        feesContentView.setTranslationX(width + dX);
    }

    private void processScroll(MotionEvent e1, float distanceX, float distanceY) {
        dX -= distanceX;
        dY -= distanceY;

        if (Math.abs(dY) > Math.abs(dX) && cameraView.getTranslationX() == 0) {

            permissionClick = false;

            if (dY > 0 && !isGalleryShow) {
                return;
            }

//            View view = recyclerView.getChildAt(0);
//            if ((view instanceof DetectEdge)) {
//
//            } else {
//                return;
//            }

            if (isGalleryShow) {
                if (dY > 0) {
                    cameraView.translationY((-h + dp(100)) + dY);
//                    recyclerView.setTranslationY(dp(100) + dY);
                }
            } else {
                cameraView.translationY(dY);
//                recyclerView.setTranslationY(h + dY);
            }


        } else if (dX < 0 && dX > -width && cameraView.getTranslationY() == 0) {

            float y = e1.getY();
            if (y < h - dp(100)) {
                cameraView.translationX(dX);
//                recyclerView.setTranslationX(dX);
                feesContentView.setTranslationX(width + dX);
            }
        }

        cameraView.processScroll(e1, dX, dY);
    }

    public void show() {
        if (busyAnimation) {
            return;
        }

        setVisibility(VISIBLE);
        hideStatusBar();

        if (MediaController.allMediaAlbumEntry == null) {
            MediaController.loadGalleryPhotosAlbums(0);
        }

        isShow = true;
        busyAnimation = true;

        if (cameraView == null) {
            cameraView = new StoryCameraControllerView(parentActivity, parentActivity);
            cameraView.setTranslationX(-width);
            cameraView.setDelegate(new StoryCameraControllerView.Delegate() {
                //                @Override
                //                public void startRecord()
                //                {
                //
                //                }
                //
                //                @Override
                //                public void photo(MediaController.PhotoEntry photoEntry)
                //                {
                //
                //                }
                //
                //                @Override
                //                public void video(MediaController.PhotoEntry photoEntry)
                //                {
                //
                //                }
            });
            addView(cameraView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

//            removeView(recyclerView);
//            addView(recyclerView);
        }


        setBackgroundColor(0x00000000);
        cameraView.init();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(feesContentView, "translationX", width), ObjectAnimator.ofFloat(cameraView, "translationX", 0));
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(300);
        animatorSet.setStartDelay(100);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                busyAnimation = false;
//                recyclerView.setVisibility(VISIBLE);
            }
        });
        animatorSet.start();
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.albumsDidLoaded) {

            if (MediaController.allMediaAlbumEntry != null) {
                for (int a = 0; a < Math.min(100, MediaController.allMediaAlbumEntry.photos.size()); a++) {
                    MediaController.PhotoEntry photoEntry = MediaController.allMediaAlbumEntry.photos.get(a);
                    photoEntry.reset();
                }
            }

            //photos = MediaController.allMediaAlbumEntry.photos;

            ValueAnimator animator = new ValueAnimator();
            animator.setValues(PropertyValuesHolder.ofInt("value", 0, 100));
            animator.setDuration(500);
            animator.addUpdateListener(animation -> {

            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
//                    adapter.notifyDataSetChanged();

//                    new Handler().postDelayed(() -> {
//                        recyclerView.setAlpha(1.0f);
//                        recyclerView.setTranslationY(h);
//                        recyclerView.setTranslationX(0);
//                    }, 200);
                }
            });
            animator.start();
        }
    }

    public void onBackPressed() {
        if (busyAnimation) {
            return;
        }

        if (isGalleryShow) {
            hideGallery();
            return;
        }

//        if (!createStoryView.onBackPressed()) {
//            return;
//        }

        showStatusBar();

        isShow = false;
        busyAnimation = true;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(feesContentView, "translationX", feesContentView.getTranslationX(), 0), ObjectAnimator.ofFloat(cameraView, "translationX", -width));
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(INVISIBLE);
                cameraRelease();
            }
        });
        animatorSet.start();
    }

    public boolean isShow() {
        return isShow;
    }

    private void cameraRelease() {
        busyAnimation = false;
        if (cameraView != null) {
            removeView(cameraView);
            cameraView.destroy(true);
            cameraView = null;
        }

//        if (createStoryView != null) {
//            removeView(createStoryView);
//            createStoryView = null;
//        }
    }

    private void hideGallery() {
//        if (createStoryView != null && createStoryView.isShow()) {
//            createStoryView.onBackPressed();
//        } else {
        feesContentView.setTranslationX(width);
        //isListViewShow = false;
        cameraView.hideGallery();
        ViewAnimator.animate(cameraView).setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(250).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isGalleryShow = false;
            }
        }).start();
//        ViewAnimator.animate(recyclerView).setInterpolator(new DecelerateInterpolator()).translationY(h).setDuration(250).start();
//        }
    }


    private class DetectEdge extends View {

        public DetectEdge(Context context) {
            super(context);
            setBackgroundColor(0xff000000);
        }
    }

    public void onResume() {
        if (isShow) {
            hideStatusBar();
        }
    }

}
