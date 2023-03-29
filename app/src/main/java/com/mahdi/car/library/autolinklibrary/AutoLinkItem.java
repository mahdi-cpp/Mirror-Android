package com.mahdi.car.library.autolinklibrary;

/**
 * Created by Chatikyan on 25.09.2016-19:43.
 */

public class AutoLinkItem
{

    private AutoLinkMode autoLinkMode;

    private String matchedText;

    private int startPoint, endPoint;

    public AutoLinkItem(int startPoint, int endPoint, String matchedText, AutoLinkMode autoLinkMode)
    {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.matchedText = matchedText;
        this.autoLinkMode = autoLinkMode;
    }

    public AutoLinkMode getAutoLinkMode()
    {
        return autoLinkMode;
    }

    public String getMatchedText()
    {
        return matchedText;
    }

    public int getStartPoint()
    {
        return startPoint;
    }

    public int getEndPoint()
    {
        return endPoint;
    }
}
