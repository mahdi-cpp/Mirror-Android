package com.mahdi.car.share.component.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mahdi.car.R;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.messenger.FileLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EditTextBoldCursor extends EditText
{

    public  interface Delegate
    {
        void keyboardBack();
    }

    private Delegate delegate;

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    private Object editor;
    private static Field mEditor;
    private static Field mShowCursorField;
    private static Field mCursorDrawableField;
    private static Field mScrollYField;
    private static Method getVerticalOffsetMethod;
    private static Field mCursorDrawableResField;
    private Drawable[] mCursorDrawable;
    private GradientDrawable gradientDrawable;
    private int cursorSize;
    private int ignoreTopCount;
    private int ignoreBottomCount;
    private int scrollY;
    private float lineSpacingExtra;
    private Rect rect = new Rect();
    private StaticLayout hintLayout;
    private int hintColor;
    private boolean hintVisible = true;
    private float hintAlpha = 1.0f;
    private long lastUpdateTime;
    private boolean allowDrawCursor = true;
    private float cursorWidth = 2.0f;

    private boolean showLine = true;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void line(boolean showLine)
    {
        this.showLine = showLine;
        invalidate();
    }

    public EditTextBoldCursor(Context context)
    {
        super(context);

        if (mCursorDrawableField == null) {
            try {
                mScrollYField = View.class.getDeclaredField("mScrollY");
                mScrollYField.setAccessible(true);
                mCursorDrawableResField = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableResField.setAccessible(true);
                mEditor = TextView.class.getDeclaredField("mEditor");
                mEditor.setAccessible(true);
                Class editorClass = Class.forName("android.widget.Editor");
                mShowCursorField = editorClass.getDeclaredField("mShowCursor");
                mShowCursorField.setAccessible(true);
                mCursorDrawableField = editorClass.getDeclaredField("mCursorDrawable");
                mCursorDrawableField.setAccessible(true);
                getVerticalOffsetMethod = TextView.class.getDeclaredMethod("getVerticalOffset", boolean.class);
                getVerticalOffsetMethod.setAccessible(true);
            } catch (Throwable e) {
                //
            }
        }
        try {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xffffffff, 0xffffffff});
            editor = mEditor.get(this);
            mCursorDrawable = (Drawable[]) mCursorDrawableField.get(editor);
            mCursorDrawableResField.set(this, R.drawable.field_carret_empty);
        } catch (Exception e) {
            FileLog.e(e);
        }
        cursorSize = AndroidUtilities.dp(24);

        paint.setColor(0xff000000);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setAllowDrawCursor(boolean value)
    {
        allowDrawCursor = value;
    }

    public void setCursorWidth(float width)
    {
        cursorWidth = width;
    }

    public void setCursorColor(int color)
    {
        gradientDrawable.setColor(0xffffffff);
        invalidate();
    }

    public void setCursorSize(int value)
    {
        cursorSize = value;
    }

    public void setHintVisible(boolean value)
    {
        if (hintVisible == value) {
            return;
        }
        lastUpdateTime = System.currentTimeMillis();
        hintVisible = value;
        invalidate();
    }

    public void setHintColor(int value)
    {
        hintColor = value;
        invalidate();
    }

    public void setHintText(String value)
    {
        hintLayout = new StaticLayout(value, getPaint(), AndroidUtilities.dp(1000), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    @Override public void setLineSpacing(float add, float mult)
    {
        super.setLineSpacing(add, mult);
        lineSpacingExtra = add;
    }

    @Override public int getExtendedPaddingTop()
    {
        if (ignoreTopCount != 0) {
            ignoreTopCount--;
            return 0;
        }
        return super.getExtendedPaddingTop();
    }

    @Override public int getExtendedPaddingBottom()
    {
        if (ignoreBottomCount != 0) {
            ignoreBottomCount--;
            return scrollY != Integer.MAX_VALUE ? -scrollY : 0;
        }
        return super.getExtendedPaddingBottom();
    }

    @Override protected void onDraw(Canvas canvas)
    {

        int topPadding = getExtendedPaddingTop();
        scrollY = Integer.MAX_VALUE;
        try {
            scrollY = mScrollYField.getInt(this);
            mScrollYField.set(this, 0);
        } catch (Exception e) {
            //
        }
        ignoreTopCount = 1;
        ignoreBottomCount = 1;
        canvas.save();
        canvas.translate(0, topPadding);
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            //
        }
        if (scrollY != Integer.MAX_VALUE) {
            try {
                mScrollYField.set(this, scrollY);
            } catch (Exception e) {
                //
            }
        }
        canvas.restore();

        if (length() == 0 && hintLayout != null && (hintVisible || hintAlpha != 0)) {

            if (hintVisible && hintAlpha != 1.0f || !hintVisible && hintAlpha != 0.0f) {

                long newTime = System.currentTimeMillis();
                long dt = newTime - lastUpdateTime;
                if (dt < 0 || dt > 17) {
                    dt = 17;
                }
                lastUpdateTime = newTime;
                if (hintVisible) {
                    hintAlpha += dt / 150.0f;
                    if (hintAlpha > 1.0f) {
                        hintAlpha = 1.0f;
                    }
                } else {
                    hintAlpha -= dt / 150.0f;
                    if (hintAlpha < 0.0f) {
                        hintAlpha = 0.0f;
                    }
                }
                invalidate();
            }
            int oldColor = getPaint().getColor();
            getPaint().setColor(hintColor);
            getPaint().setAlpha((int) (255 * hintAlpha));
            canvas.save();
            int left = 0;
            float lineLeft = hintLayout.getLineLeft(0);
            if (lineLeft != 0) {
                left -= lineLeft;
            }
            canvas.translate(left, (getMeasuredHeight() - hintLayout.getHeight()) / 2.0f);
            hintLayout.draw(canvas);
            getPaint().setColor(oldColor);
            canvas.restore();
        }
        try {
            if (allowDrawCursor && mShowCursorField != null && mCursorDrawable != null && mCursorDrawable[0] != null) {

                long mShowCursor = mShowCursorField.getLong(editor);
                boolean showCursor = (SystemClock.uptimeMillis() - mShowCursor) % (2 * 500) < 500 && isFocused();

                if (showCursor) {
                    canvas.save();
                    int voffsetCursor = 0;
                    if ((getGravity() & Gravity.VERTICAL_GRAVITY_MASK) != Gravity.TOP) {
                        voffsetCursor = (int) getVerticalOffsetMethod.invoke(this, true);
                    }
                    canvas.translate(getPaddingLeft(), getExtendedPaddingTop() + voffsetCursor);
                    Layout layout = getLayout();
                    int line = layout.getLineForOffset(getSelectionStart());
                    int lineCount = layout.getLineCount();
                    Rect bounds = mCursorDrawable[0].getBounds();
                    rect.left = bounds.left;
                    rect.right = bounds.left + AndroidUtilities.dp(cursorWidth);
                    rect.bottom = bounds.bottom;
                    rect.top = bounds.top;
                    if (lineSpacingExtra != 0 && line < lineCount - 1) {
                        rect.bottom -= lineSpacingExtra;
                    }
                    rect.top = rect.centerY() - cursorSize / 2;
                    rect.bottom = rect.top + cursorSize;
                    gradientDrawable.setBounds(rect);
                    //gradientDrawable.draw(canvas);
                    canvas.restore();
                }
            }
        } catch (Throwable e) {
            //ignore
        }

        if (showLine) {
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        }
    }

    @Override public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (delegate != null) {
                delegate.keyboardBack();
            }
            return false;  // So it is not propagated.
        }
        return super.dispatchKeyEvent(event);
    }
}
