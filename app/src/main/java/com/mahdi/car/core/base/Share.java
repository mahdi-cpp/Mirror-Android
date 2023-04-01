package com.mahdi.car.core.base;

import android.view.View;

import java.util.Date;

import com.mahdi.car.core.RootView;
import com.mahdi.car.messenger.AndroidUtilities;

public interface Share
{
    default int px(float px)
    {
        return AndroidUtilities.pxToDp(px);
    }

    default String time(Date date)
    {
        return AndroidUtilities.time(date);
    }

    default void keyboardShow(View view)
    {
        AndroidUtilities.keyboardShow(view);
    }

    default void keyboardHide(View view)
    {
        AndroidUtilities.keyboardHide(view);
    }

    default void showStatusBar()
    {
        AndroidUtilities.showStatusBar();
    }

    default void hideStatusBar()
    {
        AndroidUtilities.hideStatusBar();
    }

    default void showNavigation()
    {
        AndroidUtilities.showNavigation();
    }

    default void hideNavigation()
    {
        AndroidUtilities.hideNavigation();
    }

    default void setNavigationTransparent(boolean transparent)
    {
        AndroidUtilities.setNavigationTransparent(transparent);
    }

    default void modeIGTV()
    {
        AndroidUtilities.modeIGTV();
    }
}
