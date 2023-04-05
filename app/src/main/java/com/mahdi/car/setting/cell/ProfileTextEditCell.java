package com.mahdi.car.setting.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.messenger.Utilities;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.EditTextBoldCursor;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class ProfileTextEditCell extends CellFrameLayout {
    private Delegate delegate;

    public EditTextBoldCursor editText;

    private StaticLayout titleLayout;
    private String hint;
    private boolean isEmpty = true;
    private boolean forceLowerCase = false;
    private long lastProgressUpdateTime = 0;

    private boolean isUsername = false;

    public ProfileTextEditCell(Context context) {
        super(context);

        editText = new EditTextBoldCursor(context);
        editText.setMaxLines(1);
        editText.setSingleLine(true);
        editText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        //editText.setBackgroundColor(0xffffffff);
        editText.setHintTextColor(0xff888888);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setCursorColor(0xffff9800);
        editText.setCursorSize(dp(20));
        editText.setCursorWidth(1.5f);
        editText.setTextColor(0xff000000);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.setAllCaps(true);
        //editText.line(true);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUsername)
                    processUsername();
                else
                    process();
            }
        });
        editText.setOnEditorActionListener((v, actionId, event) -> {//OnEditorActionListener
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (delegate != null) {
                    String text = editText.getText().toString();
                    delegate.search(text);
                }
            }
            return false;
        });
        editText.setOnClickListener(v -> {
            editText.setEnabled(true);
            editText.requestFocus();
            editText.bringToFront();
            keyboardShow(editText);
        });


        addView(editText, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, Gravity.BOTTOM, 15, 0, 15, 0));

        //setBackgroundColor(0xffff9800);

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 40));

        //setWillNotDraw(false);
    }

    public void setForceLowerCase(boolean forceLowerCase) {
        this.forceLowerCase = forceLowerCase;
    }

    public void setHint(String hint) {
        this.hint = hint;
        titleLayout = new StaticLayout(hint, Themp.TEXT_PAINT_FILL_GREY[1], width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        invalidate();
    }

    public void setUsername(boolean isUsername) {
        this.isUsername = isUsername;
    }

    public void setName(String name) {
        if (name == null || name.length() == 0) {
            isEmpty = true;
            editText.setHint(hint);
        } else {
            isEmpty = false;
            editText.setText(name);
        }
    }

    public String getText() {
        if (editText.getText().toString().length() > 0)
            return editText.getText().toString();

        return "";
    }

    public void cancel() {
        editText.setText("");
        editText.setCursorColor(0xffffffff);
        editText.setEnabled(false);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

//        if (!isEmpty)
//            drawTextLayout(titleLayout, dp(20), dp(15));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(dp(4), dp(4), getWidth() - dp(4), getHeight() - dp(4), dp(8), dp(8), Themp.PAINT_FFEEEEEE);
        //canvas.drawRoundRect(dp(15), getHeight() - dp(4), getWidth() - dp(15), getHeight() - dp(4), dp(8), dp(8), Themp.PAINT_GREEN);
    }


    public void process() {
        Utilities.stageQueue.postRunnable(() -> AndroidUtilities.run(() -> {

            long currentTime = System.currentTimeMillis();
            if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < currentTime - 100) {

                lastProgressUpdateTime = currentTime;

                if (editText.getText().length() == 0) {
                    isEmpty = true;
                    editText.setHint(hint);
                } else {
                    isEmpty = false;
                }

                for (int a = 0; a < editText.getText().length(); a++) {

                    char ch = editText.getText().charAt(a);


                    if (ch >= 'A' && ch <= 'Z') {
                        if (forceLowerCase)
                            editText.getText().replace(a, a + 1, "" + Character.toLowerCase(ch));
                        else
                            editText.getText().replace(a, a + 1, "" + ch);
                    }
                }

                String text = editText.getText().toString();

                if (delegate != null) {
                    delegate.afterTextChanged(text);
                }
                invalidate();
            }
        }));
    }

    public void processUsername() {
        Utilities.stageQueue.postRunnable(() -> AndroidUtilities.run(() -> {

            long currentTime = System.currentTimeMillis();
            if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < currentTime - 50) {
                lastProgressUpdateTime = currentTime;

                for (int a = 0; a < editText.getText().length(); a++) {

                    char ch = editText.getText().charAt(a);

                    if (!(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_' || ch == '.')) {
                        editText.getText().replace(a, a + 1, "");
                        Toast.makeText(getContext(), "فقط از حروف ، عدد ، نقطه و زیر خط می توان استفاده کرد", Toast.LENGTH_SHORT).show();
                    }

                    if (a == 0) {
                        if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_') {

                        } else if (editText.getText().length() > 0) {

                            editText.getText().replace(0, 1, "");
                            Toast.makeText(getContext(), "نام کاربری باید با حروف ، عدد و یا زیر خط شروع شود", Toast.LENGTH_SHORT).show();

                        }
                    }

                    if (ch >= 'A' && ch <= 'Z') {
                        editText.getText().replace(a, a + 1, "" + Character.toLowerCase(ch));
                    }
                }

                username = editText.getText().toString();
            }
        }));
    }

    public interface Delegate {

        void afterTextChanged(String text);

        void cancel();

        void start();

        void search(String text);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
