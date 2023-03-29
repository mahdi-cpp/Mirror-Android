package com.mahdi.car.library.autolinklibrary;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.share.AutoLinkText;
import com.mahdi.car.share.AutoLinkTextDelegate;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AutoLinkTextView extends AppCompatTextView
{

    public interface Delegate {

        void mention(String mention);

        void hashtag(String hashtag);
    }

    private Delegate delegate;

    private static final int MIN_PHONE_NUMBER_LENGTH = 8;

    private AutoLinkMode[] autoLinkModes;
    private List<AutoLinkMode> mBoldAutoLinkModes;
    private String customRegex;
    private boolean isUnderLineEnabled = false;
    private boolean isEnable = true;

    public AutoLinkTextView(Context context) {
        super(context);
        setBackgroundColor(0xffffffff);

        setTextColor(0xff000000);
        setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(10), AndroidUtilities.dp(16), AndroidUtilities.dp(4));
        setHighlightColor(Color.TRANSPARENT);

        addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_PHONE, AutoLinkMode.MODE_URL, AutoLinkMode.MODE_EMAIL, AutoLinkMode.MODE_MENTION);
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        if (TextUtils.isEmpty(text)) {
            super.setText(text, type);
            return;
        }

        SpannableString spannableString = makeSpannableString(text);
        setMovementMethod(new LinkTouchMovementMethod());

        super.setText(spannableString, type);


    }

    private SpannableString makeSpannableString(CharSequence text) {

        final SpannableString spannableString = new SpannableString(text);

        List<AutoLinkItem> autoLinkItems = matchedRanges(text);

        for (final AutoLinkItem autoLinkItem : autoLinkItems) {
            int currentColor = 0xff2F41a5;

            TouchableSpan clickableSpan = new TouchableSpan(currentColor, 0xff2F41a5, isUnderLineEnabled) {
                @Override
                public void onClick(View widget) {

                    if (isEnable) {

                        AutoLinkText.getInstance().click(autoLinkItem.getAutoLinkMode(), autoLinkItem.getMatchedText(), new AutoLinkTextDelegate() {
                            @Override
                            public void mention(String mention) {
                                if (delegate != null)
                                    delegate.mention(mention);
                            }

                            @Override
                            public void hashtag(String hashtag) {
                                if (delegate != null)
                                    delegate.hashtag(hashtag);
                            }
                        });
                    }
                }
            };

            spannableString.setSpan(clickableSpan, autoLinkItem.getStartPoint(), autoLinkItem.getEndPoint(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // check if we should make this auto link item bold
            if (mBoldAutoLinkModes != null && mBoldAutoLinkModes.contains(autoLinkItem.getAutoLinkMode())) {

                // make the auto link item bold
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), autoLinkItem.getStartPoint(), autoLinkItem.getEndPoint(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        return spannableString;
    }

    private List<AutoLinkItem> matchedRanges(CharSequence text) {

        List<AutoLinkItem> autoLinkItems = new LinkedList<>();

        if (autoLinkModes == null) {
            throw new NullPointerException("Please add at least one mode");
        }

        for (AutoLinkMode anAutoLinkMode : autoLinkModes) {
            String regex = AutoLinkUtils.getRegexByAutoLinkMode(anAutoLinkMode, customRegex);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            if (anAutoLinkMode == AutoLinkMode.MODE_PHONE) {
                while (matcher.find()) {
                    if (matcher.group().length() > MIN_PHONE_NUMBER_LENGTH)
                        autoLinkItems.add(new AutoLinkItem(matcher.start(), matcher.end(), matcher.group(), anAutoLinkMode));
                }
            } else {
                while (matcher.find()) {
                    autoLinkItems.add(new AutoLinkItem(matcher.start(), matcher.end(), matcher.group(), anAutoLinkMode));
                }
            }
        }

        return autoLinkItems;
    }

    public void addAutoLinkMode(AutoLinkMode... autoLinkModes) {
        this.autoLinkModes = autoLinkModes;
    }

    class LinkTouchMovementMethod extends LinkMovementMethod {

        private TouchableSpan pressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, final Spannable spannable, MotionEvent event) {

            android.util.Log.e("addVideoView", "" + event.getX() + "  " + event.getY());

            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                pressedSpan = getPressedSpan(textView, spannable, event);
                if (pressedSpan != null) {
                    pressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(pressedSpan), spannable.getSpanEnd(pressedSpan));
                }
            } else if (action == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (pressedSpan != null && touchedSpan != pressedSpan) {
                    pressedSpan.setPressed(false);
                    pressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (pressedSpan != null) {
                    pressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                pressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int verticalLine = layout.getLineForVertical(y);
            int horizontalOffset = layout.getOffsetForHorizontal(verticalLine, x);

            TouchableSpan[] link = spannable.getSpans(horizontalOffset, horizontalOffset, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }


}