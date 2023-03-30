package com.mahdi.car.story.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;

import android.text.Layout;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;

import androidx.core.app.ActivityCompat;

import com.mahdi.car.camera.CameraController;
import com.mahdi.car.camera.CameraView;
import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.story.camera.component.RecordButton;
import com.mahdi.car.story.camera.component.SlideViewPager;
import com.mahdi.car.messenger.NotificationCenter;
import com.mahdi.car.share.Logger;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.messenger.AndroidUtilities;

public class StoryCameraControllerView extends CellFrameLayout implements NotificationCenter.NotificationCenterDelegate
{
    private Delegate delegate;
    private Context context;
    private Activity activity;

    private CameraView cameraView;
    private RecordButton recordButton;
    private SlideViewPager slideViewPager;

    private Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private File cameraFile;
    private Runnable videoRecordRunnable;

    private int videoRecordTime;
    private boolean requestingPermissions;
    private boolean deviceHasGoodCamera = true;
    private boolean cameraOpened = false;
    private boolean takingPhoto;
    //private boolean mediaCaptured;
    private boolean recording = false;

    private static final String TAG = StoryCameraControllerView.class.getSimpleName();
    private static final Logger LOG = new Logger(TAG);

    public StoryCameraControllerView(Context context, Activity activity)
    {
        super(context);
        this.context = context;
        this.activity = activity;
        setClickable(true);
        setBackgroundColor(0xff000000);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP));

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.cameraInitied);

        alphaPaint.setColor(0xff9E9E9E);
        alphaPaint.setAlpha(0);

        CameraController.getInstance().initCamera(null);

        recordButton = new RecordButton(context);
        recordButton.setDelegate(new RecordButton.Delegate()
        {
            @Override
            public void tackPicture()
            {
                takePhoto();
            }

            @Override
            public void startRecord()
            {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
                } else {
                    StoryCameraControllerView.this.startRecord();
                }
            }

            @Override
            public void cancelRecord()
            {
                StoryCameraControllerView.this.cancelRecord();
            }
        });

        slideViewPager = new SlideViewPager(context);
        slideViewPager.setDelegate(new SlideViewPager.Delegate()
        {
            @Override
            public void showGallery()
            {

            }

            @Override
            public void index(int index)
            {
                recordButton.setType(index);

                if (index == 0) {
                    showCamera(false);
                } else {
                    showCamera(true);
                }
            }

            @Override
            public void switchCamera()
            {
                //                String current = cameraView.getCameraSession().getCurrentFlashMode();
                //                String next = cameraView.getCameraSession().getNextFlashMode();
                //                cameraView.getCameraSession().setCurrentFlashMode(next);
                //cameraView.switchCamera();
                //                String next = cameraView.getCameraSession().getNextFlashMode();
                ////                cameraView.getCameraSession().setCurrentFlashMode(next);
            }
        });

        if (CameraController.getInstance().isCameraInitied())
            showCamera(true);
    }

    private void takePhoto()
    {
        if (cameraView == null || cameraView.getCameraSession() == null) {
            return;
        }

        //mediaCaptured = true;

        //        if (recordButton.getRecording()) {
        //            resetRecordState();
        //            CameraController.getInstance().stopVideoRecording(cameraView.getCameraSession(), false);
        //            return;
        //        }

        cameraFile = AndroidUtilities.generatePicturePath();
        final boolean sameTakePictureOrientation = cameraView.getCameraSession().isSameTakePictureOrientation();

        takingPhoto = CameraController.getInstance().takePicture(cameraFile, cameraView.getCameraSession(), new Runnable()
        {
            @Override
            public void run()
            {
                takingPhoto = false;
                if (cameraFile == null) {
                    return;
                }

                int orientation = 0;
                //Log.e("cameraFile", cameraFile.getAbsolutePath());
                //delegate.photo(new MediaController.PhotoEntry(1, 1, 0, cameraFile.getAbsolutePath(), 90, false));
            }
        });
    }

    private void startRecord()
    {
        recording = true;
        slideViewPager.setVisibility(INVISIBLE);
        videoRecordTime = 0;
        recordButton.recordOk();

        cameraFile = AndroidUtilities.generateVideoPath();
        videoRecordRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if (videoRecordRunnable == null) {
                    return;
                }

                if (videoRecordTime >= 15) {
                    cancelRecord();
                    return;
                }

                Log.e("videoRecordRunnable", "ttttt" + videoRecordTime);

                videoRecordTime++;
                AndroidUtilities.run(videoRecordRunnable, 1000);
            }
        };

        CameraController.getInstance().recordVideo(cameraView.getCameraSession(), cameraFile, new CameraController.VideoTakeCallback()
        {
            @Override
            public void onFinishVideoRecording(String thumbPath, long duration)
            {
                Log.e("videoRecording", "onFinishVideoRecording" + videoRecordTime);
                if (cameraFile == null) {
                    return;
                }
                //delegate.video(new MediaController.PhotoEntry(0, 0, 0, cameraFile.getAbsolutePath(), 90, true));
            }
        }, new Runnable()
        {
            @Override
            public void run()
            {
                AndroidUtilities.run(videoRecordRunnable, 1000);
            }
        }, false);
    }

    private void cancelRecord()
    {
        recording = false;
        slideViewPager.setVisibility(VISIBLE);
        recordButton.cancelRecord();

        AndroidUtilities.cancelRunOnUIThread(videoRecordRunnable);
        videoRecordRunnable = null;

        if (cameraView != null) {
            CameraController.getInstance().stopVideoRecording(cameraView.getCameraSession(), true);
        }
    }

    public void showCamera(boolean request)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (request) {
                    activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 17);
                }
                deviceHasGoodCamera = false;
            } else {
                CameraController.getInstance().initCamera(null);
                deviceHasGoodCamera = CameraController.getInstance().isCameraInitied();
            }
        } else {
            CameraController.getInstance().initCamera(null);
            deviceHasGoodCamera = CameraController.getInstance().isCameraInitied();
        }
        //
        //        if (deviceHasGoodCamera && !cameraOpened) {

        if (cameraView == null) {
            cameraView = new CameraView(context, false);
            cameraView.setDelegate(new CameraView.CameraViewDelegate()
            {
                @Override
                public void onCameraCreated(Camera camera)
                {

                }

                @Override
                public void onCameraInit()
                {

                }
            });
            addView(cameraView);
        }
        //        }

        removeView(recordButton);
//        addView(recordButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0, 0, 50));

        removeView(slideViewPager);
        //addView(slideViewPager, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 64, Gravity.BOTTOM));

        cameraOpened = true;
    }

    public void showGallery()
    {
        alphaPaint.setAlpha(255);
        invalidate();
    }

    public void hideGallery()
    {
        alphaPaint.setAlpha(0);
        invalidate();
    }

    public void translationY(float dY)
    {
        this.setTranslationY(dY);

        float divide = ((float) screenHeight / 255);
        float alpha = (screenHeight + dY) / (divide);

        alpha = 255 - alpha;
        alpha += 20;
        if (alpha > 255)
            alpha = 255;

        if (alpha < 0)
            alpha = 0;

        //Log.e("1234566", "alpha: " + alpha);

        alphaPaint.setAlpha((int) alpha);
        invalidate();
    }

    public void translationX(float dX)
    {
        this.setTranslationX(dX);
        invalidate();
    }

    public void processScroll(MotionEvent event, float dX, float dY)
    {
        slideViewPager.processScroll(event, dX, dY);
    }

    public void resetX()
    {
        slideViewPager.resetX();
    }

    public void click(MotionEvent event)
    {
        slideViewPager.click(event);
    }

    public void init()
    {
        slideViewPager.init();
    }

    public void closeCamera()
    {
        if (takingPhoto || cameraView == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 21 && cameraView != null) {
            cameraView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        canvas.drawRect(new Rect(0, 0, width, getHeight()), alphaPaint);

        if (alphaPaint.getAlpha() > 240) {
            canvas.save();
            canvas.translate(dp(15), getHeight() - dp(45));
            String currentGallery = "Gallery";
            StaticLayout staticLayout = new StaticLayout(currentGallery, Themp.TEXT_PAINT_FILL_WHITE[10], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            staticLayout.draw(canvas);
            canvas.restore();
            canvas.drawBitmap(Themp.bitmapGallery, dp(37) + dp(currentGallery.length() * 4) + dp(16), getHeight() - dp(40), Themp.ICON_PAINT_SRC_IN_WHITE);
        }

        if (!recording) {
            canvas.drawBitmap(Themp.camera[0], dp(15), dp(15), Themp.ICON_PAINT_SRC_IN_WHITE);
            //canvas.drawBitmap(Themp.camera[1], (width / 2) - (Themp.camera[1].getWidth() / 2), dp(15), Themp.ICON_PAINT_SRC_IN_WHITE);
            canvas.drawBitmap(Themp.camera[3], width - Themp.camera[3].getWidth() - dp(15), dp(15), Themp.ICON_PAINT_SRC_IN_WHITE);
        }

        canvas.save();
        Matrix rotator = new Matrix();
        rotator.postRotate(90);
        rotator.postRotate(90, 0, 0);

        int xTranslate = width - dp(20);
        int yTranslate = dp(45);
        rotator.postTranslate(xTranslate, yTranslate);
    }

    public void onResume()
    {
        showCamera(false);

    }

    //    public void onPause()
    //    {
    //        if (recordButton == null) {
    //            return;
    //        }
    //        if (!requestingPermissions) {
    //            if (cameraView != null && recordButton.getState() == RecordButton.State.RECORDING) {
    //                resetRecordState();
    //                CameraController.getInstance().stopVideoRecording(cameraView.getCameraSession(), false);
    //                recordButton.setState(RecordButton.State.DEFAULT, true);
    //            }
    //            if (cameraOpened) {
    //                closeCamera();
    //            }
    //            destroy(true);
    //        } else {
    //            if (cameraView != null && recordButton.getState() == RecordButton.State.RECORDING) {
    //                recordButton.setState(RecordButton.State.DEFAULT, true);
    //            }
    //            requestingPermissions = false;
    //        }
    //        //paused = true;
    //    }

    public void destroy(boolean async)
    {
        if (!deviceHasGoodCamera || cameraView == null) {
            return;
        }
        cameraView.destroy(async, null);
        removeView(cameraView);
        cameraView = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return true;
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == NotificationCenter.cameraInitied) {
            showCamera(true);
        }
    }

    public interface Delegate
    {
        //        void photo(MediaController.PhotoEntry photoEntry);
        //
        //        void video(MediaController.PhotoEntry photoEntry);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
