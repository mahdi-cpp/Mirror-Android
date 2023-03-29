package com.mahdi.car.story.camera.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.Themp;
import com.mahdi.car.messenger.AndroidUtilities;

public class RecordButton extends CellView
{
    private Delegate delegate;
    private GestureDetector gestureDetector;
    private Paint gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ValueAnimator indoorAnimator = ValueAnimator.ofInt(0, dp(20));
    private ValueAnimator outAnimator = ValueAnimator.ofInt(0, dp(20));
    private ValueAnimator recordAnimator = ValueAnimator.ofInt(0, dp(360));

    private Bitmap[] bitmaps = new Bitmap[5];
    private Type type;
    private int indoorValue;
    private int outdoorValue;
    private int recordValue;
    private boolean recording = false;
    private int index = 0;

    public enum Type
    {
        CREATE, NORMAL, HANDS_FREE
    }

    public RecordButton(Context context)
    {
        super(context);

        indoorAnimator.setValues(PropertyValuesHolder.ofInt("counter", 0, -dp(6)));
        indoorAnimator.setDuration(250);
        indoorAnimator.setInterpolator(new LinearInterpolator());
        indoorAnimator.addUpdateListener(animation -> {
            indoorValue = (int) animation.getAnimatedValue("counter");
            invalidate();
        });

        outAnimator.setValues(PropertyValuesHolder.ofInt("counter", 0, dp(20)));
        outAnimator.setDuration(250);
        outAnimator.setInterpolator(new LinearInterpolator());
        outAnimator.addUpdateListener(animation -> {
            outdoorValue = (int) animation.getAnimatedValue("counter");
            invalidate();
        });

        recordAnimator.setValues(PropertyValuesHolder.ofInt("counter", 0, 360));
        recordAnimator.setDuration(1000 * 16);
        recordAnimator.setInterpolator(new LinearInterpolator());
        recordAnimator.addUpdateListener(animation -> {
            recordValue = (int) animation.getAnimatedValue("counter");
            invalidate();
        });
        recordAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                //cancelRecord();
            }
        });
        gradientPaint.setStyle(Paint.Style.STROKE);
        gradientPaint.setStrokeWidth(dp(5.2f));
        gradientPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        gradientPaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        //gradientPaint.setShader(new LinearGradient(dp(0), dp(70), getWidth(), getHeight(), 0xffff1744, 0xffC51162, Shader.TileMode.MIRROR));
        gradientPaint.setShader(new LinearGradient(dp(20), dp(30), dp(140), dp(140), new int[]{0xfff44336, 0xffC2185B, 0xffFF5722, 0xffFF9800, 0xffC2185B, 0xffFF5722}, null, Shader.TileMode.CLAMP));

        bitmaps[0] = AndroidUtilities.getCroppedBitmap(Themp.story[0]);
        bitmaps[1] = AndroidUtilities.getCroppedBitmap(Themp.story[1]);
        type = Type.NORMAL;
        gestureDetector = new GestureDetector(context, new Gesture());

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

        canvas.drawCircle(cx, cy, dp(36) + outdoorValue, Themp.STROKE_PAINT_4DP_WHITE);
        canvas.drawCircle(cx, cy, dp(31) + indoorValue, Themp.PAINT_WHITE);

        int size = dp(36) + dp(20);
        if (recording && recordValue > 5) {
            canvas.drawArc(new RectF(cx - size, cy - size, cx + size, cy + size), -90, recordValue, false, gradientPaint);
        }

        //Bitmap drawableBitmap = Theme.story[0].copy(Bitmap.Config.ARGB_8888, true);
        //drawableBitmap.eraseColor(0xffffffff);

        //if (state != State.RECORDING && show) {

        int i = -1;
        if (index == 1) {
            i = 0;
            type = Type.CREATE;
        } else if (index == 3) {
            i = 1;
            type = Type.HANDS_FREE;
        }

        if (i >= 0 && bitmaps[i] != null) {
            canvas.drawBitmap(bitmaps[i], dp(49), dp(49), null);
        }

        if (type == Type.HANDS_FREE) {
            canvas.drawBitmap(bitmaps[1], dp(49), dp(49), null);
        }
    }

    public void setType(int index)
    {
        this.index = index;
        invalidate();
    }

    private void takePhoto()
    {
        if (!recording)
            delegate.tackPicture();
    }

    public void startRecord()
    {
        if (recording)
            return;
        delegate.startRecord();
    }

    public void recordOk()
    {
        recording = true;
        //state = State.RECORDING;
        indoorAnimator.start();
        outAnimator.start();
        recordAnimator.start();
    }

    public void cancelRecord()
    {
        recording = false;
        indoorAnimator.reverse();
        outAnimator.reverse();
    }

    private class Gesture implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
    {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e)
        {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e)
        {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            if (recording)
                delegate.cancelRecord();
            else
                takePhoto();
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
            startRecord();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getX();

        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (recording && type == Type.NORMAL) {
                    delegate.cancelRecord();
                }

                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(dp(160), dp(160));
    }

    public interface Delegate
    {
        void tackPicture();

        void startRecord();

        void cancelRecord();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
