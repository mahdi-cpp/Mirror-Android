package com.mahdi.car.dialog.component;

import android.content.Context;
import android.graphics.*;

import android.text.*;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.EditTextBoldCursor;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.dialog.parent.BaseDialogHeader;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.messenger.Utilities;

public class ForwardHeaderView extends BaseDialogHeader
{
    private Delegate delegate;

    private EditTextBoldCursor messageEditText;
    private ForwardSearchView searchView;
    private View shadow;

    private int top_margin = dp(20);
    private long lastProgressUpdateTime = 0;

    public String getMessage()
    {
        return messageEditText.getText().toString();
    }

    public ForwardHeaderView(Context context)
    {

        super(context);

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 70 + 70, Gravity.TOP, 0, 0, 0, 0));

        avatarSize = dp(44);

        int maxLength = 1000;
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(maxLength);

        messageEditText = new EditTextBoldCursor(context);
        messageEditText.setFilters(filterArray);
        messageEditText.setHint("نوشتن پیام...");
        messageEditText.setMaxLines(1);
        messageEditText.setSingleLine(true);
        messageEditText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        messageEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        messageEditText.setBackgroundDrawable(null);
        messageEditText.setHintTextColor(0xff888888);
        messageEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        messageEditText.setCursorColor(0xffff9800);
        messageEditText.setCursorSize(dp(20));
        messageEditText.setCursorWidth(1.5f);
        messageEditText.setTextColor(0xff000000);
        messageEditText.setBackgroundColor(0xffffffff);
        messageEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        messageEditText.setDelegate(() -> delegate.cancel());


        searchView = new ForwardSearchView(context, "جستجوی");
        searchView.setDelegate(new ForwardSearchView.Delegate()
        {
            @Override
            public void afterTextChanged(String text)
            {
                process(text);
            }

            @Override
            public void onFocusChange()
            {
                delegate.onFocusChange(true);
            }

            @Override
            public void clear()
            {
                delegate.clear();
            }

            @Override
            public void cancel()
            {
                delegate.cancel();
            }
        });
        searchView.searchTextView.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                delegate.onFocusChange(hasFocus);
            }
        });

        searchView.searchTextView.setDelegate(() -> delegate.cancel());

        addView(messageEditText, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, Gravity.TOP | Gravity.CENTER_VERTICAL, 20, 20, 48 + 20, 0));
        addView(searchView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 54, Gravity.TOP, 0, 4 + 48 + 34, 0, 0));

        shadow = new View(context);
        shadow.setVisibility(View.INVISIBLE);
        shadow.setBackground(Themp.drawableToolbar[0]);
        //addView(shadow, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 3, Gravity.BOTTOM));

        setWillNotDraw(false);

        invalidate();
    }

    public void setPhoto(String photo)
    {
//        .with(this).load(App.files + photo).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(dp(20)))).into(new SimpleTarget<Drawable>()
//        {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
//            {
//                drawable = resource;
//                invalidate();
//            }
//        });
//
        setMedia(photo, avatarSize, avatarSize, dp(20));
    }

    public void setShow()
    {
        shadow.setVisibility(View.VISIBLE);
    }

    public void setHide()
    {
        shadow.setVisibility(View.INVISIBLE);
    }

    public void clear()
    {
        messageEditText.setText("");
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), dp(20), dp(20), Themp.PAINT_WHITE);
        canvas.drawRect(new RectF(0, getHeight() - dp(30), getWidth(), getHeight()), Themp.PAINT_WHITE);
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        drawMedia(getWidth() - dp(64 - 3), top_margin, getWidth() - dp(64 - 3) + avatarSize, avatarSize + top_margin);

        canvas.drawRoundRect(new RectF(getWidth() - avatarSize - dp(17), top_margin, getWidth() - dp(17), top_margin + avatarSize), dp(10), dp(10), Themp.STROKE_PAINT_PX_FFBBBBBB);

        RectF rectF2 = new RectF((getWidth() / 2) - dp(20), dp(10), (getWidth() / 2) + dp(20), dp(14));
        canvas.drawRoundRect(rectF2, dp(4), dp(4), Themp.STROKE_PAINT_PX_FFBBBBBB);

        canvas.drawLine(0, dp(80), width, dp(80), Themp.STROKE_PAINT_PX_FFDDDDDD);
    }

    public void process(String text)
    {

        Utilities.stageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                AndroidUtilities.run(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        long currentTime = System.currentTimeMillis();
                        if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < currentTime - 200) {
                            lastProgressUpdateTime = currentTime;

                            for (int a = 0; a < searchView.searchTextView.getText().length(); a++) {

                                char ch = searchView.searchTextView.getText().charAt(a);


                                if (ch >= 'A' && ch <= 'Z') {
                                    searchView.searchTextView.getText().replace(a, a + 1, "" + Character.toLowerCase(ch));
                                }
                            }

                            String text = searchView.searchTextView.getText().toString();

                            if (delegate != null) {
                                delegate.afterTextChanged(text);
                            }

                            invalidate();
                        }
                    }
                });
            }
        });
    }

    public interface Delegate
    {
        void afterTextChanged(String text);

        void clear();

        void onFocusChange(boolean hasFocus);

        void cancel();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}