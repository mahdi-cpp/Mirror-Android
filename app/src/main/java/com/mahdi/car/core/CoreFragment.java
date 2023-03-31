package com.mahdi.car.core;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import com.mahdi.car.MyActivity;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class CoreFragment extends FrameLayout
{
    private Context context;
    private MyActivity parentActivity;
    protected ArrayList<BaseFragment> fragmentsStack;

    public CoreFragment(@NonNull Context context)
    {
        super(context);

        setBackgroundColor(0xffffffff);

        this.context = context;
        parentActivity = (MyActivity) context;
        fragmentsStack = new ArrayList<>();
    }

    public boolean presentFragment(BaseFragment fragment)
    {
        if (fragmentsStack.size() >= 1) {
            BaseFragment currentFragment = fragmentsStack.get(fragmentsStack.size() - 1);
            currentFragment.onPause();
        }

        fragment.setParentLayout(this);
        fragment.onFragmentCreate();
        fragment.createView(context);
        addView(fragment.parentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        fragmentsStack.add(fragment);

        visibility(View.INVISIBLE);

        return true;
    }

    public void closeLastFragment()
    {
        if(fragmentsStack == null) {
            return;
        }

        if (fragmentsStack.isEmpty()) {
            return;
        }

        visibility(View.VISIBLE);

        BaseFragment currentFragment = fragmentsStack.get(fragmentsStack.size() - 1);
        removeView(currentFragment.parentView);
        currentFragment.onPause();
        currentFragment.onFragmentDestroy();
        fragmentsStack.remove(currentFragment);

        int index = fragmentsStack.size() - 1;
        if (index >= 0) {
            final BaseFragment lastFragment = fragmentsStack.get(index);
            if (lastFragment != null) {
                lastFragment.onResume();
            }
        }
    }

    private void visibility(int visibility)
    {
        if (fragmentsStack.size() > 2) {
            BaseFragment f = fragmentsStack.get(fragmentsStack.size() - 3);
            f.parentView.setVisibility(visibility);
        }
    }

    public boolean onBackPressed()
    {
        if (fragmentsStack.isEmpty() && !RootView.instance().getFullScreen()) {
            return true;
        }

        if (fragmentsStack.size() > 1) {

            BaseFragment lastFragment = fragmentsStack.get(fragmentsStack.size() - 1);

            if (lastFragment.isStoryShow()) {
//                lastFragment.hideStory();
                return false;
            } else if (lastFragment.onBackPressed()) {
                if (!fragmentsStack.isEmpty()) {
                    closeLastFragment();
                }
            }
            return false;

        } else if (RootView.instance().getFullScreen()) {

            BaseFragment lastFragment = fragmentsStack.get(0);

            if (lastFragment.isStoryShow()) {
//                lastFragment.hideStory();
                return false;
            } else if (lastFragment.onBackPressed()) {
                if (!fragmentsStack.isEmpty()) {
                    closeLastFragment();
                }
            }
            return false;

        } else {
            BaseFragment lastFragment = fragmentsStack.get(0);

            if (lastFragment.isStoryShow()) {
//                lastFragment.hideStory();
                return false;
            } else
                return lastFragment.onBackPressed();
        }
    }

    public MyActivity getParentActivity()
    {
        return parentActivity;
    }
}
