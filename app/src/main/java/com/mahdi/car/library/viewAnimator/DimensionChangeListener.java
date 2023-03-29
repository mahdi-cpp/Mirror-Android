
package com.mahdi.car.library.viewAnimator;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bartosz Lipinski
 * 13.10.15
 */
class DimensionChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener {

    private final ViewGroup.LayoutParams mParams;

    private IntValues mWidth;
    private IntValues mHeight;

    DimensionChangeListener(View view) {
        super(view);
        mParams = view.getLayoutParams();
        if (mParams == null) {
            throw new IllegalStateException("View does not have layout params yet.");
        }
    }

    private int currentWidth() {
        return mParams.width > 0 ? mParams.width : hasView() ? mView.get().getWidth() : 0;
    }

    private int currentHeight() {
        return mParams.height > 0 ? mParams.height : hasView() ? mView.get().getHeight() : 0;
    }

    public void width(int width) {
        mWidth = new IntValues(currentWidth(), width);
    }

    public void widthBy(int widthBy) {
        mWidth = new IntValues(currentWidth(), currentWidth() + widthBy);
    }

    public void height(int height) {
        mHeight = new IntValues(currentHeight(), height);
    }

    public void heightBy(int heightBy) {
        mHeight = new IntValues(currentHeight(), currentHeight() + heightBy);
    }

    public void size(int size){
        width(size);
        height(size);
    }

    public void sizeBy(int sizeBy){
        widthBy(sizeBy);
        heightBy(sizeBy);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (hasView()) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            if (mWidth != null) {
                mParams.width = (int) calculateAnimatedValue(mWidth.mFrom, mWidth.mTo, animatedFraction);
            }
            if (mHeight != null) {
                mParams.height = (int) calculateAnimatedValue(mHeight.mFrom, mHeight.mTo, animatedFraction);
            }
            mView.get().requestLayout();
        }
    }

}