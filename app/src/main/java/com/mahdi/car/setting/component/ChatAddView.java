package com.mahdi.car.setting.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Editable;
import android.text.InputType;
import android.text.StaticLayout;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.EditTextBoldCursor;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class ChatAddView extends CellFrameLayout
{
    private Delegate delegate;

    private EditTextBoldCursor messageTextView;
    private StaticLayout postLayout;
    private boolean isCharacter = false;
    private long lastProgressUpdateTime = 0;
    private int user_size = dp(32);
    private int size = dp(32);
    private int space = dp(10);

    public ChatAddView(Context context)
    {
        super(context);

        setBackgroundColor(0xffffffff);

        messageTextView = new EditTextBoldCursor(context);
        messageTextView.setHint("Message...");
        messageTextView.setMaxLines(1);
        messageTextView.line(false);
        messageTextView.setSingleLine(true);
        messageTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        messageTextView.setBackgroundColor(0x00ffffff);
        messageTextView.setHintTextColor(0xffbbbbbb);
        messageTextView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        messageTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        messageTextView.setCursorColor(0xffff9800);
        messageTextView.setCursorSize(dp(20));
        messageTextView.setCursorWidth(1.5f);
        //commentTextView.setTypeface(Application.typeface);
        messageTextView.setTextColor(0xff000000);
        messageTextView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        messageTextView.setEnabled(true);
        messageTextView.setClickable(true);
        messageTextView.setAllCaps(true);

        messageTextView.addTextChangedListener(new TextWatcher()
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

                if (messageTextView.length() == 0) {
                    isCharacter = false;
                } else {
                    isCharacter = true;
                }

            }
        });
        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (delegate != null) {
                        String text = messageTextView.getText().toString();
                        //delegate.search(text);
                    }
                }
                return false;
            }
        });

        messageTextView.setOnClickListener(v -> {
            messageTextView.requestFocus();
            messageTextView.bringToFront();
            keyboardShow(messageTextView);
        });


        //setAvatar(owner().Avatar);
        round = dp(50);

        addView(messageTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER_VERTICAL, 68, 0, 48, 0));

    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        canvas.save();
        canvas.translate(dp(20), dp(9));

        canvas.drawRoundRect(new RectF(0, 0, width - dp(40), dp(49)), round, round, Themp.STROKE_PAINT_1DP_FFDDDDDD);

        if (isPressed == 1) {
            canvas.drawCircle(dp(32), dp(25), dp(23), Themp.PAINT_PRESS_BLACK);
        }

        canvas.drawBitmap(Themp.bitmapChat[0], dp(7), dp(6), null);

        canvas.drawBitmap(Themp.bitmapChat[7], width - dp(80), dp(12), null);
        canvas.drawBitmap(Themp.bitmapChat[2], width - dp(80 + 44), dp(12), null);
        canvas.drawBitmap(Themp.bitmapChat[8], width - dp(80 + 44 + 44), dp(12), null);

        canvas.restore();

    }

    public void post()
    {
        isCharacter = false;
        invalidate();

        String text = messageTextView.getText().toString();

        text = text.replace("\r\r", "\r");
        text = text.replace("\r\r\r", "\r");
        text = text.replace("\r\r\r", "\r");
        text = text.replace("\r\r\r\r", "\r");
        text = text.replace("\r\r\r\r\r", "\r");
        text = text.replace("\r\r\r\r\r\r", "\r");
        text = text.replace("\r\r\r\r\r\r\r", "\r");
        text = text.replace("\r\r\r\r\r\r\r\r", "\r");

        text = text.replace("\n\n", "\n");
        text = text.replace("\n\n\n", "\n");
        text = text.replace("\n\n\n\n", "\n");
        text = text.replace("\n\n\n\n\n", "\n");
        text = text.replace("\n\n\n\n\n\n", "\n");
        text = text.replace("\n\n\n\n\n\n\n", "\n");
        text = text.replace("\n\n\n\n\n\n\n\n", "\n");

        text = text.replace("  ", "");

        delegate.post(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (x < dp(60) && y > 0 && y < getHeight()) {
                    isPressed = 1;
                } else {
                    isPressed = -1;
                }

                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                if (x < dp(60) && isPressed == 1 && isCharacter && delegate != null && y < getHeight()) {
                    post();
                }

                isPressed = -1;
                invalidate();
                break;
        }
        return true;
    }

    public interface Delegate
    {
        void post(String message);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
