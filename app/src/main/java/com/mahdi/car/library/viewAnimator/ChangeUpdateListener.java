
package com.mahdi.car.library.viewAnimator;

import android.view.View;

import java.lang.ref.WeakReference;


abstract class ChangeUpdateListener {

    protected final WeakReference<View> mView;

    public ChangeUpdateListener(View view) {
        this.mView =  new WeakReference<View>(view);
    }

    public float calculateAnimatedValue(float initialValue, float targetValue, float animationFraction) {
        return targetValue - ((targetValue - initialValue) * (1.0f - animationFraction));
    }

    protected boolean hasView() {
        return mView.get() != null;
    }

    protected class IntValues {
        public int mFrom;
        public int mTo;

        public IntValues(int from, int to) {
            this.mFrom = from;
            this.mTo = to;
        }
    }

    protected class FloatValues {
        public float mFrom;
        public float mTo;

        public FloatValues(float from, float to) {
            this.mFrom = from;
            this.mTo = to;
        }
    }
}
