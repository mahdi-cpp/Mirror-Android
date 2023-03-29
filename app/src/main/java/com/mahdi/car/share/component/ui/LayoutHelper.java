package com.mahdi.car.share.component.ui;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mahdi.car.messenger.AndroidUtilities;


public class LayoutHelper
{

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private static int getSize(float size)
    {
        return (int) (size < 0 ? size : AndroidUtilities.dp(size));
    }


    public static FrameLayout.LayoutParams createFrame(int width, float height)
    {
        return new FrameLayout.LayoutParams(getSize(width), getSize(height));
    }

    public static FrameLayout.LayoutParams createFrame(int width, int height, int gravity)
    {
        return new FrameLayout.LayoutParams(getSize(width), getSize(height), gravity);
    }

    public static FrameLayout.LayoutParams createFrame(int width, float height, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin)
    {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getSize(width), getSize(height), gravity);
        layoutParams.setMargins(AndroidUtilities.dp(leftMargin), AndroidUtilities.dp(topMargin), AndroidUtilities.dp(rightMargin), AndroidUtilities.dp(bottomMargin));
        return layoutParams;
    }


    public static LinearLayout.LayoutParams createLinear(int width, int height)
    {
        return new LinearLayout.LayoutParams(getSize(width), getSize(height));
    }

    public static LinearLayout.LayoutParams createLinear(int width, int height, float weight)
    {
        return new LinearLayout.LayoutParams(getSize(width), getSize(height), weight);
    }

    public static LinearLayout.LayoutParams createLinear(int width, int height, float leftMargin, float topMargin, float rightMargin, float bottomMargin)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(width), getSize(height));
        layoutParams.setMargins(AndroidUtilities.dp(leftMargin), AndroidUtilities.dp(topMargin), AndroidUtilities.dp(rightMargin), AndroidUtilities.dp(bottomMargin));
        return layoutParams;
    }

    public static LinearLayout.LayoutParams createLinear(int width, int height, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(width), getSize(height));
        layoutParams.setMargins(AndroidUtilities.dp(leftMargin), AndroidUtilities.dp(topMargin), AndroidUtilities.dp(rightMargin), AndroidUtilities.dp(bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
    }


}

