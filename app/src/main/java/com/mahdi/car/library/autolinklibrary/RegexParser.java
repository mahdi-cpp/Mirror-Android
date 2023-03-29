package com.mahdi.car.library.autolinklibrary;

import android.util.Patterns;

/**
 * Created by Chatikyan on 25.09.2016-19:23.
 */

class RegexParser
{

    static final String PHONE_PATTERN = Patterns.PHONE.pattern();

    static final String EMAIL_PATTERN = Patterns.EMAIL_ADDRESS.pattern();

    //static final String HASHTAG_PATTERN = "(?:^|\\s|$) #[\\p{L}0-9_]* (?:^|\\s|$|[.])";
    static final String HASHTAG_PATTERN = "[#]+[A-Za-z0-9-_]+\\b";


    //static final String MENTION_PATTERN = "(?:^|\\s|$|[.]) @[\\p{L}0-9_].* (?:^|\\s|$|[.])";
    static final String MENTION_PATTERN = "[@]+[A-Za-z0-9-_]+[.-A-Za-z0-9-_]\\b";
    //static final String MENTION_PATTERN = "[@]+[A-Za-z0-9-_]+[A-Za-z0-9\\-\\.\\_]+(?:\\s|$)";

    static final String URL_PATTERN = "(^|[\\s.:;?\\-\\]<\\(])" + "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" + "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])";
}
