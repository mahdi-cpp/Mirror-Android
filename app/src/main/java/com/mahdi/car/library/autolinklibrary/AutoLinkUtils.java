package com.mahdi.car.library.autolinklibrary;

import android.util.Log;

/**
 * Created by Chatikyan on 25.09.2016-18:57.
 */

public class AutoLinkUtils
{

    private static boolean isValidRegex(String regex)
    {
        return regex != null && !regex.isEmpty() && regex.length() > 2;
    }

    public static String getRegexByAutoLinkMode(AutoLinkMode anAutoLinkMode, String customRegex)
    {
        switch (anAutoLinkMode) {
            case MODE_HASHTAG:
                return RegexParser.HASHTAG_PATTERN;
            case MODE_MENTION:
                return RegexParser.MENTION_PATTERN;
            case MODE_URL:
                return RegexParser.URL_PATTERN;
            case MODE_PHONE:
                return RegexParser.PHONE_PATTERN;
            case MODE_EMAIL:
                return RegexParser.EMAIL_PATTERN;
            case MODE_CUSTOM:
                if (!AutoLinkUtils.isValidRegex(customRegex)) {
                    Log.e("AutoLinkTextView", "Your custom regex is null, returning URL_PATTERN");
                    return RegexParser.URL_PATTERN;
                } else {
                    return customRegex;
                }
            default:
                return RegexParser.URL_PATTERN;
        }
    }

}
