package com.mahdi.car.dialog.component;

import android.content.Context;
import android.graphics.*;
import android.text.*;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.mahdi.car.share.Themp;
import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.share.component.ui.EditTextBoldCursor;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class ForwardSearchView extends CellFrameLayout
{
    private Delegate delegate;
    public EditTextBoldCursor searchTextView;
    private ColorFilter filter;
    private View shadow;

    private int space = dp(8);
    private boolean isPressed = false;
    private boolean isText = false;

    public ForwardSearchView(Context context, String name)
    {

        super(context);

        setBackgroundColor(0xffffffff);
        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 64));

        searchTextView = new EditTextBoldCursor(context);
        searchTextView.setHint(name);
        searchTextView.setMaxLines(1);
        searchTextView.setSingleLine(true);
        searchTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        searchTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        searchTextView.setBackgroundDrawable(null);
        searchTextView.setHintTextColor(0xff888888);
        searchTextView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        searchTextView.setCursorColor(0xffff9800);
        searchTextView.setCursorSize(dp(20));
        searchTextView.setCursorWidth(1.5f);
        searchTextView.setTextColor(0xff000000);
        searchTextView.setClickable(false);
        searchTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if (delegate != null) {
                    delegate.onFocusChange();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                String text = searchTextView.getText().toString();

                if (delegate != null) {
                    delegate.afterTextChanged(text);
                }

                if (text.length() > 0) {
                    isText = true;
                } else {
                    isText = false;
                }
                invalidate();
            }
        });
        searchTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (delegate != null) {
                delegate.onFocusChange();
            }
        });
        searchTextView.setDelegate(() -> delegate.cancel());

        addView(searchTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 100, 0, 48, 0));

        shadow = new View(context);
        shadow.setVisibility(View.INVISIBLE);
        shadow.setBackground(Themp.drawableToolbar[0]);
        addView(shadow, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 3, Gravity.BOTTOM));
    }

    public void clear()
    {
        searchTextView.setText("");
        isText = false;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {

        super.dispatchDraw(canvas);

        if (isText) {
            canvas.drawBitmap(Themp.bitmapCloseCircle, dp(24), dp(15), Themp.ICON_PAINT_SRC_IN_BLACK);
        }

        canvas.drawBitmap(Themp.toolbar.search, getWidth() - dp(48), dp(16), Themp.ICON_PAINT_SRC_IN_GREY);

        RectF rectF = new RectF(space + dp(8), space - dp(0), getWidth() - (space + dp(8)), getHeight() - space + dp(0));
        canvas.drawRoundRect(rectF, dp(8), dp(8), Themp.STROKE_PAINT_1DP_FFCCCCCC);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x < dp(60) && y > 0 && y < getHeight()) {
                    isPressed = true;
                    invalidate();
                } else {
                    isPressed = false;
                    invalidate();
                    return false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                if (x < dp(60) && y > 0 && y < getHeight() && isPressed) {
                    clear();
                    delegate.clear();
                } else {
                    isPressed = false;
                    invalidate();
                    return false;
                }
                invalidate();
                break;

            default:
                isPressed = false;
                invalidate();
                break;
        }
        return true;
    }

    public interface Delegate
    {

        void afterTextChanged(String text);

        void onFocusChange();

        void clear();

        void cancel();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}