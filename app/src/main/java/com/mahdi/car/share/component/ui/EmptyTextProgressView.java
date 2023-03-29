package com.mahdi.car.share.component.ui;

import android.content.Context;
import android.widget.FrameLayout;


public class EmptyTextProgressView extends FrameLayout
{

//    private TextView textView;
//    private RadialProgressView progressBar;
//    private boolean inLayout;
//    private boolean showAtCenter;
//    private ImageView icon;

    public EmptyTextProgressView(Context context)
    {
        super(context);

//        progressBar = new RadialProgressView(context);
//        progressBar.setVisibility(INVISIBLE);
//        addView(progressBar, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
//
//        icon = new ImageView(context);
//        icon.setImageDrawable(Theme.searchFilters[4]);
//        icon.setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
//        addView(icon, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
//
//        textView = new TextView(context);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//        textView.setTextColor(0xff000000);
//        textView.setGravity(Gravity.CENTER);
//        textView.setVisibility(INVISIBLE);
//        textView.setTypeface(App.typeface);
//        textView.setPadding(dp(40), dp(80), dp(40), 0);
//        addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));
//
//        setOnTouchListener(new OnTouchListener()
//        {
//            @Override public boolean onTouch(View v, MotionEvent event)
//            {
//                return true;
//            }
//        });
    }
//
//    public void showProgress()
//    {
//        textView.setVisibility(INVISIBLE);
//        progressBar.setVisibility(VISIBLE);
//    }
//
//    public void showTextView()
//    {
//        textView.setVisibility(VISIBLE);
//        progressBar.setVisibility(INVISIBLE);
//    }
//
//    public void setText(String text)
//    {
//        textView.setText(text);
//    }
//
//    public void setTextColor(int color)
//    {
//        textView.setTextColor(color);
//    }
//
//    public void setProgressBarColor(int color)
//    {
//        progressBar.setProgressColor(color);
//    }
//
//    public void setTextSize(int size)
//    {
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
//    }
//
//    public void setShowAtCenter(boolean value)
//    {
//        showAtCenter = value;
//    }
//
//    @Override protected void onLayout(boolean changed, int l, int t, int r, int b)
//    {
//        inLayout = true;
//        int width = r - l;
//        int height = b - t;
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//
//            if (child.getVisibility() == GONE) {
//                continue;
//            }
//
//            int x = (width - child.getMeasuredWidth()) / 2;
//            int y;
//            if (showAtCenter) {
//                y = (height / 2 - child.getMeasuredHeight()) / 2;
//            } else {
//                y = (height - child.getMeasuredHeight()) / 2;
//            }
//            child.layout(x, y, x + child.getMeasuredWidth(), y + child.getMeasuredHeight());
//        }
//        inLayout = false;
//    }
//
//    @Override public void requestLayout()
//    {
//        if (!inLayout) {
//            super.requestLayout();
//        }
//    }

    @Override public boolean hasOverlappingRendering()
    {
        return false;
    }
}
