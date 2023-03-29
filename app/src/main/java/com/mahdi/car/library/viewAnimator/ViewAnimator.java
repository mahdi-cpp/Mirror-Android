package com.mahdi.car.library.viewAnimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ViewAnimator
{

    private final WeakReference<View> mView;
    private long mDuration = -1;
    private long mStartDelay = -1;
    private boolean mWithLayer = false;
    private Interpolator mInterpolator;
    private List<Animator.AnimatorListener> mListeners = new ArrayList<>();
    private List<ValueAnimator.AnimatorUpdateListener> mUpdateListeners = new ArrayList<>();
    private List<Animator.AnimatorPauseListener> mPauseListeners = new ArrayList<>();
    private ArrayMap<Property<View, Float>, PropertyValuesHolder> mPropertyHoldersMap = new ArrayMap<>();
    private MarginChangeListener mMarginListener;
    private DimensionChangeListener mDimensionListener;
    private PaddingChangeListener mPaddingListener;
    private ScrollChangeListener mScrollListener;

    private ViewAnimator(View view)
    {
        mView = new WeakReference<View>(view);
    }

    public static ViewAnimator animate(View view)
    {
        return new ViewAnimator(view);
    }

    private void animateProperty(Property<View, Float> property, float toValue)
    {
        if (hasView()) {
            float fromValue = property.get(mView.get());
            animatePropertyBetween(property, fromValue, toValue);
        }
    }

    private void animatePropertyBy(Property<View, Float> property, float byValue)
    {
        if (hasView()) {
            float fromValue = property.get(mView.get());
            float toValue = fromValue + byValue;
            animatePropertyBetween(property, fromValue, toValue);
        }
    }

    private void animatePropertyBetween(Property<View, Float> property, float fromValue, float toValue)
    {
        mPropertyHoldersMap.remove(property); //if the same property is assigned again, we want to override it
        mPropertyHoldersMap.put(property, PropertyValuesHolder.ofFloat(property, fromValue, toValue));
    }

    public ViewAnimator scaleX(float scaleX)
    {
        animateProperty(View.SCALE_X, scaleX);
        return this;
    }

    public ViewAnimator scaleXBy(float scaleXBy)
    {
        animatePropertyBy(View.SCALE_X, scaleXBy);
        return this;
    }

    public ViewAnimator scaleY(float scaleY)
    {
        animateProperty(View.SCALE_Y, scaleY);
        return this;
    }

    public ViewAnimator scaleYBy(float scaleYBy)
    {
        animatePropertyBy(View.SCALE_Y, scaleYBy);
        return this;
    }

    public ViewAnimator scales(float scales)
    {
        scaleY(scales);
        scaleX(scales);
        return this;
    }

    public ViewAnimator scalesBy(float scalesBy)
    {
        scaleYBy(scalesBy);
        scaleXBy(scalesBy);
        return this;
    }

    public ViewAnimator translationX(float translationX)
    {
        animateProperty(View.TRANSLATION_X, translationX);
        return this;
    }

    public ViewAnimator translationXBy(float translationXBy)
    {
        animatePropertyBy(View.TRANSLATION_X, translationXBy);
        return this;
    }

    public ViewAnimator translationY(float translationY)
    {
        animateProperty(View.TRANSLATION_Y, translationY);
        return this;
    }

    public ViewAnimator translationYBy(float translationYBy)
    {
        animatePropertyBy(View.TRANSLATION_Y, translationYBy);
        return this;
    }

    @SuppressLint("NewApi") public ViewAnimator translationZ(float translationZ)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateProperty(View.TRANSLATION_Z, translationZ);
        }
        return this;
    }

    @SuppressLint("NewApi") public ViewAnimator translationZBy(float translationZBy)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animatePropertyBy(View.TRANSLATION_Z, translationZBy);
        }
        return this;
    }

    public ViewAnimator alpha(float alpha)
    {
        animateProperty(View.ALPHA, alpha);
        return this;
    }

    public ViewAnimator alphaBy(float alphaBy)
    {
        animatePropertyBy(View.ALPHA, alphaBy);
        return this;
    }

    public ViewAnimator rotation(float rotation)
    {
        animateProperty(View.ROTATION, rotation);
        return this;
    }

    public ViewAnimator rotationBy(float rotationBy)
    {
        animatePropertyBy(View.ROTATION, rotationBy);
        return this;
    }

    public ViewAnimator rotationX(float rotationX)
    {
        animateProperty(View.ROTATION_X, rotationX);
        return this;
    }

    public ViewAnimator rotationXBy(float rotationXBy)
    {
        animatePropertyBy(View.ROTATION_X, rotationXBy);
        return this;
    }

    public ViewAnimator rotationY(float rotationY)
    {
        animateProperty(View.ROTATION_Y, rotationY);
        return this;
    }

    public ViewAnimator rotationYBy(float rotationYBy)
    {
        animatePropertyBy(View.ROTATION_Y, rotationYBy);
        return this;
    }

    public ViewAnimator x(float x)
    {
        animateProperty(View.X, x);
        return this;
    }

    public ViewAnimator xBy(float xBy)
    {
        animatePropertyBy(View.X, xBy);
        return this;
    }

    public ViewAnimator y(float y)
    {
        animateProperty(View.Y, y);
        return this;
    }

    public ViewAnimator yBy(float yBy)
    {
        animatePropertyBy(View.Y, yBy);
        return this;
    }

    @SuppressLint("NewApi") public ViewAnimator z(float z)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateProperty(View.Z, z);
        }
        return this;
    }

    @SuppressLint("NewApi") public ViewAnimator zBy(float zBy)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animatePropertyBy(View.Z, zBy);
        }
        return this;
    }

    public ViewAnimator leftMargin(int leftMargin)
    {
        if (initMarginListener()) {
            mMarginListener.leftMargin(leftMargin);
        }
        return this;
    }

    public ViewAnimator leftMarginBy(int leftMarginBy)
    {
        if (initMarginListener()) {
            mMarginListener.leftMarginBy(leftMarginBy);
        }
        return this;
    }

    public ViewAnimator topMargin(int topMargin)
    {
        if (initMarginListener()) {
            mMarginListener.topMargin(topMargin);
        }
        return this;
    }

    public ViewAnimator topMarginBy(int topMarginBy)
    {
        if (initMarginListener()) {
            mMarginListener.topMarginBy(topMarginBy);
        }
        return this;
    }

    public ViewAnimator rightMargin(int rightMargin)
    {
        if (initMarginListener()) {
            mMarginListener.rightMargin(rightMargin);
        }
        return this;
    }

    public ViewAnimator rightMarginBy(int rightMarginBy)
    {
        if (initMarginListener()) {
            mMarginListener.rightMarginBy(rightMarginBy);
        }
        return this;
    }

    public ViewAnimator bottomMargin(int bottomMargin)
    {
        if (initMarginListener()) {
            mMarginListener.bottomMargin(bottomMargin);
        }
        return this;
    }

    public ViewAnimator bottomMarginBy(int bottomMarginBy)
    {
        if (initMarginListener()) {
            mMarginListener.bottomMarginBy(bottomMarginBy);
        }
        return this;
    }

    public ViewAnimator horizontalMargin(int horizontalMargin)
    {
        if (initMarginListener()) {
            mMarginListener.horizontalMargin(horizontalMargin);
        }
        return this;
    }

    public ViewAnimator horizontalMarginBy(int horizontalMarginBy)
    {
        if (initMarginListener()) {
            mMarginListener.horizontalMarginBy(horizontalMarginBy);
        }
        return this;
    }

    public ViewAnimator verticalMargin(int verticalMargin)
    {
        if (initMarginListener()) {
            mMarginListener.verticalMargin(verticalMargin);
        }
        return this;
    }

    public ViewAnimator verticalMarginBy(int verticalMarginBy)
    {
        if (initMarginListener()) {
            mMarginListener.verticalMarginBy(verticalMarginBy);
        }
        return this;
    }

    public ViewAnimator margin(int margin)
    {
        if (initMarginListener()) {
            mMarginListener.margin(margin);
        }
        return this;
    }

    public ViewAnimator marginBy(int marginBy)
    {
        if (initMarginListener()) {
            mMarginListener.marginBy(marginBy);
        }
        return this;
    }

    public ViewAnimator width(int width)
    {
        if (initDimensionListener()) {
            mDimensionListener.width(width);
        }
        return this;
    }

    public ViewAnimator widthBy(int widthBy)
    {
        if (initDimensionListener()) {
            mDimensionListener.widthBy(widthBy);
        }
        return this;
    }

    public ViewAnimator height(int height)
    {
        if (initDimensionListener()) {
            mDimensionListener.height(height);
        }
        return this;
    }

    public ViewAnimator heightBy(int heightBy)
    {
        if (initDimensionListener()) {
            mDimensionListener.heightBy(heightBy);
        }
        return this;
    }

    public ViewAnimator size(int size)
    {
        if (initDimensionListener()) {
            mDimensionListener.size(size);
        }
        return this;
    }

    public ViewAnimator sizeBy(int sizeBy)
    {
        if (initDimensionListener()) {
            mDimensionListener.sizeBy(sizeBy);
        }
        return this;
    }

    public ViewAnimator leftPadding(int leftPadding)
    {
        if (initPaddingListener()) {
            mPaddingListener.leftPadding(leftPadding);
        }
        return this;
    }

    public ViewAnimator leftPaddingBy(int leftPaddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.leftPaddingBy(leftPaddingBy);
        }
        return this;
    }

    public ViewAnimator topPadding(int topPadding)
    {
        if (initPaddingListener()) {
            mPaddingListener.topPadding(topPadding);
        }
        return this;
    }

    public ViewAnimator topPaddingBy(int topPaddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.topPaddingBy(topPaddingBy);
        }
        return this;
    }

    public ViewAnimator rightPadding(int rightPadding)
    {
        if (initPaddingListener()) {
            mPaddingListener.rightPadding(rightPadding);
        }
        return this;
    }

    public ViewAnimator rightPaddingBy(int rightPaddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.rightPaddingBy(rightPaddingBy);
        }
        return this;
    }

    public ViewAnimator bottomPadding(int bottomPadding)
    {
        if (initPaddingListener()) {
            mPaddingListener.bottomPadding(bottomPadding);
        }
        return this;
    }

    public ViewAnimator bottomPaddingBy(int bottomPaddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.bottomPaddingBy(bottomPaddingBy);
        }
        return this;
    }

    public ViewAnimator horizontalPadding(int horizontalPadding)
    {
        if (initPaddingListener()) {
            mPaddingListener.horizontalPadding(horizontalPadding);
        }
        return this;
    }

    public ViewAnimator horizontalPaddingBy(int horizontalPaddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.horizontalPaddingBy(horizontalPaddingBy);
        }
        return this;
    }

    public ViewAnimator verticalPadding(int verticalPadding)
    {
        if (initPaddingListener()) {
            mPaddingListener.verticalPadding(verticalPadding);
        }
        return this;
    }

    public ViewAnimator verticalPaddingBy(int verticalPaddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.verticalPaddingBy(verticalPaddingBy);
        }
        return this;
    }

    public ViewAnimator padding(int padding)
    {
        if (initPaddingListener()) {
            mPaddingListener.padding(padding);
        }
        return this;
    }

    public ViewAnimator paddingBy(int paddingBy)
    {
        if (initPaddingListener()) {
            mPaddingListener.paddingBy(paddingBy);
        }
        return this;
    }

    public ViewAnimator scrollX(int scrollX)
    {
        if (initScrollListener()) {
            mScrollListener.scrollX(scrollX);
        }
        return this;
    }

    public ViewAnimator scrollXBy(int scrollXBy)
    {
        if (initScrollListener()) {
            mScrollListener.scrollXBy(scrollXBy);
        }
        return this;
    }

    public ViewAnimator scrollY(int scrollY)
    {
        if (initScrollListener()) {
            mScrollListener.scrollY(scrollY);
        }
        return this;
    }

    public ViewAnimator scrollYBy(int scrollYBy)
    {
        if (initScrollListener()) {
            mScrollListener.scrollYBy(scrollYBy);
        }
        return this;
    }


    private boolean initMarginListener()
    {
        //we're initializing margin listener only when needed (it can cause an exception when there are no params)
        if (mMarginListener == null) {
            if (!hasView()) {
                return false;
            }
            mMarginListener = new MarginChangeListener(mView.get());
        }
        return true;
    }

    private boolean initDimensionListener()
    {
        //we're initializing dimension listener only when needed (it can cause an exception when there are no params)
        if (mDimensionListener == null) {
            if (!hasView()) {
                return false;
            }
            mDimensionListener = new DimensionChangeListener(mView.get());
        }
        return true;
    }

    private boolean initPaddingListener()
    {
        if (mPaddingListener == null) {
            if (!hasView()) {
                return false;
            }
            mPaddingListener = new PaddingChangeListener(mView.get());
        }
        return true;
    }

    private boolean initScrollListener()
    {
        if (mScrollListener == null) {
            if (!hasView()) {
                return false;
            }
            mScrollListener = new ScrollChangeListener(mView.get());
        }
        return true;
    }


    public ViewAnimator withLayer()
    {
        mWithLayer = true;
        return this;
    }

    public ViewAnimator setStartDelay(long startDelay)
    {
        if (startDelay < 0) {
            throw new IllegalArgumentException("startDelay cannot be < 0");
        }
        mStartDelay = startDelay;
        return this;
    }

    public ViewAnimator setDuration(long duration)
    {
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be < 0");
        }
        mDuration = duration;
        return this;
    }

    public ViewAnimator setInterpolator(Interpolator interpolator)
    {
        mInterpolator = interpolator;
        return this;
    }

    public ViewAnimator addListener(Animator.AnimatorListener listener)
    {
        mListeners.add(listener);
        return this;
    }

    public ViewAnimator removeListener(Animator.AnimatorListener listener)
    {
        mListeners.remove(listener);
        return this;
    }

    public ViewAnimator removeAllListeners()
    {
        mListeners.clear();
        return this;
    }

    public ViewAnimator addUpdateListener(ValueAnimator.AnimatorUpdateListener listener)
    {
        mUpdateListeners.add(listener);
        return this;
    }

    public ViewAnimator removeUpdateListener(ValueAnimator.AnimatorUpdateListener listener)
    {
        mUpdateListeners.remove(listener);
        return this;
    }

    public ViewAnimator removeAllUpdateListeners()
    {
        mUpdateListeners.clear();
        return this;
    }

    public ViewAnimator addPauseListener(ValueAnimator.AnimatorPauseListener listener)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPauseListeners.add(listener);
        }
        return this;
    }

    public ViewAnimator removePauseListener(ValueAnimator.AnimatorPauseListener listener)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPauseListeners.remove(listener);
        }
        return this;
    }

    public ViewAnimator removeAllPauseListeners()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPauseListeners.clear();
        }
        return this;
    }

    public ViewAnimator withStartAction(final Runnable runnable)
    {
        return addListener(new AnimatorListenerAdapter()
        {
            @Override public void onAnimationStart(Animator animation)
            {
                runnable.run();
                removeListener(this);
            }
        });
    }

    public ViewAnimator withEndAction(final Runnable runnable)
    {
        return addListener(new AnimatorListenerAdapter()
        {
            private boolean mIsCanceled;

            @Override public void onAnimationCancel(Animator animation)
            {
                mIsCanceled = true;
            }

            @Override public void onAnimationEnd(Animator animation)
            {
                if (!mIsCanceled) {
                    runnable.run();
                }
                removeListener(this);
            }
        });
    }

    private boolean hasView()
    {
        return mView.get() != null;
    }

    @SuppressLint("NewApi") public ObjectAnimator get()
    {
        if (hasView()) {
            Collection<PropertyValuesHolder> holders = mPropertyHoldersMap.values();
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mView.get(), holders.toArray(new PropertyValuesHolder[holders.size()]));
            if (mWithLayer) {
                animator.addListener(new AnimatorListenerAdapter()
                {
                    int mCurrentLayerType = View.LAYER_TYPE_NONE;

                    @Override public void onAnimationStart(Animator animation)
                    {
                        if (hasView()) {
                            View view = mView.get();
                            mCurrentLayerType = view.getLayerType();
                            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                            if (ViewCompat.isAttachedToWindow(view)) {
                                view.buildLayer();
                            }
                        }
                    }

                    @Override public void onAnimationEnd(Animator animation)
                    {
                        if (hasView()) {
                            mView.get().setLayerType(mCurrentLayerType, null);
                        }
                    }
                });
            }
            if (mStartDelay != -1) {
                animator.setStartDelay(mStartDelay);
            }
            if (mDuration != -1) {
                animator.setDuration(mDuration);
            }
            if (mInterpolator != null) {
                animator.setInterpolator(mInterpolator);
            }
            for (Animator.AnimatorListener listener : mListeners) {
                animator.addListener(listener);
            }
            if (mMarginListener != null) {
                animator.addUpdateListener(mMarginListener);
            }
            if (mDimensionListener != null) {
                animator.addUpdateListener(mDimensionListener);
            }
            if (mPaddingListener != null) {
                animator.addUpdateListener(mPaddingListener);
            }
            if (mScrollListener != null) {
                animator.addUpdateListener(mScrollListener);
            }
            for (ValueAnimator.AnimatorUpdateListener listener : mUpdateListeners) {
                animator.addUpdateListener(listener);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Animator.AnimatorPauseListener listener : mPauseListeners) {
                    animator.addPauseListener(listener);
                }
            }
            return animator;
        }
        return ObjectAnimator.ofFloat(null, View.ALPHA, 1, 1);
    }

    public void start()
    {
        get().start();
    }

}
