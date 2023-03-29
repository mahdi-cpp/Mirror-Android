package com.mahdi.car.share.cell;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.share.component.ui.RadialProgressView;

public class LoadingCell extends FrameLayout
{

    private RadialProgressView progressBar;

    public LoadingCell(Context context)
    {
        super(context);

        progressBar = new RadialProgressView(context);
        progressBar.setVisibility(View.VISIBLE);
        addView(progressBar, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
    }

}
