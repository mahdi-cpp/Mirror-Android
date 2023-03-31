package com.mahdi.car.core;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.mahdi.car.core.component.FloatViewParent;
import com.mahdi.car.feed.FeedFragment;
import com.mahdi.car.remote.RemoteControlFragment;
import com.mahdi.car.movie.MovieFragment;
import com.mahdi.car.music.MusicFragment;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.component.ui.LayoutHelper;


public class RootView {

    public static final int PAGE_HOME = 0;
    public static final int PAGE_EXPLORE = 1;
    public static final int PAGE_MOVIES = 2;
    public static final int PAGE_PROFILE = 3;

    private Context context;

    private FrameLayout contentView;
    private CoreFragment[] coreFragments = new CoreFragment[4];

    private boolean isFullScreen = false;
    private FrameLayout fullContentView;
    private CoreFragment fullCoreFragment;
    public  FloatViewParent floatViewParent = null;

    public int currentPage = PAGE_HOME;
    private int color = 0xffeeeeee;

    private boolean isKeyboardShowing = false;

    public User owner;

    @SuppressLint("StaticFieldLeak")
    private static volatile RootView Instance = null;

    public static RootView instance() {
        RootView localInstance = Instance;
        if (localInstance == null) {
            synchronized (RootView.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new RootView();
                }
            }
        }
        return localInstance;
    }

    public boolean getFullScreen() {
        return isFullScreen;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public FrameLayout getContentView() {
        return contentView;
    }

    public FrameLayout getFullContentView() {
        return fullContentView;
    }

    public void init(Context context) {

        //clear();

        this.context = context;
        isFullScreen = false;
        currentPage = PAGE_HOME;

        contentView = new FrameLayout(context);
        contentView.setBackgroundColor(color);
        contentView.setFitsSystemWindows(true);
        contentView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        fullContentView = new FrameLayout(context);
        fullContentView.setBackgroundColor(0x00000000);
        fullContentView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        fullCoreFragment = new CoreFragment(context);
        fullCoreFragment.setBackgroundColor(0x00ffffff);
        fullContentView.addView(fullCoreFragment, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        floatViewParent = new FloatViewParent(context);


        //        ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            if (contentView == null)
                return;

            Rect r = new Rect();
            contentView.getWindowVisibleDisplayFrame(r);
            int screenHeight = contentView.getRootView().getHeight();

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            int keypadHeight = screenHeight - r.bottom;

            //Log.d("QMainView", "keypadHeight = " + keypadHeight);

            if (keypadHeight > screenHeight * 0.16) { // ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true;
                    //Log.e("QMain KeyBoard", "keyboard: " + true);
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false;
                    //Log.e("QMain KeyBoard", "keyboard: " + false);
                }
            }
        });

        showPage(PAGE_HOME, true);
    }

    public boolean isKeyboardShowing() {
        return isKeyboardShowing;
    }

    public void showPage(int index, boolean rebuild) {

        Log.d("FatherView:ShoPage:", "index:" + index);

        if (coreFragments[index] == null || rebuild) {

            coreFragments[index] = new CoreFragment(context);
            //if (contentView != null) {
            contentView.addView(coreFragments[index], LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
            //}

            switch (index) {
                case PAGE_HOME:
                    coreFragments[index].presentFragment(new FeedFragment());
                    break;
                case PAGE_EXPLORE:
                    coreFragments[index].presentFragment(new MusicFragment());
                    break;
                case PAGE_MOVIES:
                    coreFragments[index].presentFragment(new MovieFragment());
                    break;
                case PAGE_PROFILE:
                    coreFragments[index].presentFragment(new RemoteControlFragment());
                    break;
            }
        }

        if (floatViewParent.getParent() == null) {
            contentView.addView(floatViewParent, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 0, 0, 48));
            floatViewParent.setZ(100);
        }


        //onResume
        BaseFragment lastFragment = coreFragments[index].fragmentsStack.get(coreFragments[index].fragmentsStack.size() - 1);
        if (lastFragment != null) {
            lastFragment.onResume();
        }

        for (int i = 0; i < 4; i++) {
            if (i != index && coreFragments[i] != null && coreFragments[i].getVisibility() == View.VISIBLE) {
                coreFragments[i].setVisibility(View.INVISIBLE);
            }
        }
        coreFragments[index].setVisibility(View.VISIBLE);
    }

    public void presentFragment(BaseFragment fragment) {
        if (isFullScreen) {
            fullCoreFragment.presentFragment(fragment);
        } else {
            coreFragments[currentPage].presentFragment(fragment);
        }
    }

    public void page(int position) {
        if (currentPage == position) {
            return;
        }
        currentPage = position;
        showPage(position, false);
    }

    private BaseFragment getCurrentFragment() {
        if (isFullScreen && fullCoreFragment.fragmentsStack.size() > 0)
            return fullCoreFragment.fragmentsStack.get(fullCoreFragment.fragmentsStack.size() - 1);

        if (coreFragments[currentPage] != null && coreFragments[currentPage].fragmentsStack.size() >= 1)
            return coreFragments[currentPage].fragmentsStack.get(coreFragments[currentPage].fragmentsStack.size() - 1);

        return null;
    }

    private BaseFragment getUnderFragment() {
        if (isFullScreen && fullCoreFragment.fragmentsStack.size() > 1)
            return fullCoreFragment.fragmentsStack.get(fullCoreFragment.fragmentsStack.size() - 2);

        if (coreFragments[currentPage] != null && coreFragments[currentPage].fragmentsStack.size() > 1)
            return coreFragments[currentPage].fragmentsStack.get(coreFragments[currentPage].fragmentsStack.size() - 2);

        return null;
    }

    private BaseFragment getTopFragmentFullScreen() {
        if (fullCoreFragment.fragmentsStack.size() >= 1)
            return fullCoreFragment.fragmentsStack.get(fullCoreFragment.fragmentsStack.size() - 1);

        return null;
    }

    public void onWebSocketReceive(String jsonString) {

        if (fullCoreFragment.fragmentsStack.size() > 0) {
            BaseFragment fragment = getTopFragmentFullScreen();
            if (fragment == null)
                return;
            fragment.onWebSocketReceive(jsonString);
        }

        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;

        fragment.onWebSocketReceive(jsonString);
    }

    public void onPause() {
        if (fullCoreFragment.fragmentsStack.size() > 0) {
            BaseFragment fragment = getTopFragmentFullScreen();
            if (fragment == null)
                return;
            fragment.onPause();
            return;
        }

        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;
        fragment.onResume();
    }

    public void onResume() {
        if (fullCoreFragment.fragmentsStack.size() > 0) {
            BaseFragment fragment = getTopFragmentFullScreen();
            if (fragment == null)
                return;
            fragment.onResume();
        }

        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;
        fragment.onResume();
    }

    public void onDestroy() {
        if (fullCoreFragment.fragmentsStack.size() > 0) {
            BaseFragment fragment = getTopFragmentFullScreen();
            if (fragment == null)
                return;
            fragment.onFragmentDestroy();
            return;
        }

        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;
        fragment.onFragmentDestroy();
    }

    public boolean onBackPressed() {
//        if (QZoomView.getInstance().isMenuShow()) {
//            QZoomView.getInstance().hideMenu(true);
//            return false;
//        }

        //        if (StoryCameraView.getInstance().isGalleryShow()) {
        //            StoryCameraView.getInstance().hideGallery();
        //            return false;
        //        } else if (StoryCameraView.getInstance().isShow()) {
        //            StoryCameraView.getInstance().hide(0);
        //            return false;
        //        }

        if (fullCoreFragment.fragmentsStack.size() > 0) {
            fullCoreFragment.onBackPressed();
            return false;
        }

        if (coreFragments[currentPage] != null) {
            return coreFragments[currentPage].onBackPressed();
        }

        return true;
    }

    public void clear() {
        if (contentView == null) {
            return;
        }

        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;

        for (int i = 0; i < 4; i++) {
            if (coreFragments[i] != null) {
                for (int j = 0; j < coreFragments[i].fragmentsStack.size(); j++) {
                    coreFragments[i].fragmentsStack.get(j).onFragmentDestroy();
                    coreFragments[i].fragmentsStack.remove(j);
                }
                coreFragments[i].fragmentsStack = null;
            }
            coreFragments[i] = null;
        }

        contentView = null;
    }

    public void setBackgroundColor(int color) {
        this.color = color;
        contentView.setBackgroundColor(color);
    }

    public void setX(int dX) {
        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;

        fragment.setX(dX);
    }

    public void floatViewVisible(int visible) {
        //floatViewParent.setVisibility(visible);
    }

    public void showFloatView(String username, String title) {
        floatViewParent.show(username, title);
    }

    public void hideFloatView() {
        floatViewParent.hide();
    }

    public void zoomClear(FrameLayout zoomParent) {
        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;

//        fragment.zoomClear(zoomParent);
    }

    public void zoom(float dX, float dY, float pivotX, float pivotY, float scale) {
        BaseFragment fragment = getCurrentFragment();
        if (fragment == null)
            return;

//        fragment.zoom(dX, dY, pivotX, pivotY, scale);
    }

}
