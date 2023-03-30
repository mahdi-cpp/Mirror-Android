package com.mahdi.car.core;

import static com.mahdi.car.messenger.AndroidUtilities.dp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.base.Share;

import com.mahdi.car.dialog.ForwardDialog;
import com.mahdi.car.feed.FeedFragment;
import com.mahdi.car.share.Themp;
import com.mahdi.car.core.component.BottomToolBar;
import com.mahdi.car.core.component.ToolBar;

import com.mahdi.car.messenger.AndroidUtilities;
import com.mahdi.car.server.model.Post;
import com.mahdi.car.setting.SettingFragment;
import com.mahdi.car.share.QMenu;
import com.mahdi.car.share.RefreshRecyclerView;
import com.mahdi.car.share.CustomLinearLayoutManager;
import com.mahdi.car.share.component.ui.LayoutHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseFragment implements Share {

    private Context context;
    private CoreFragment coreFragment;

    protected FrameLayout parentView;
    protected FrameLayout contentView;

    protected ToolBar toolbar;
    protected View currentCell;

    protected RefreshRecyclerView swipe;
    protected RecyclerView.Adapter adapter;
    protected LinearLayoutManager linearLayoutManager;

    protected HashMap<Long, Boolean> lines = new HashMap<>();
    protected HashMap<Long, Integer> currentMedias = new HashMap<>();

    protected int page = 0;
    protected int size = 20;
    protected boolean has_next_page = true;

    protected boolean permissionLoading = true;
    protected boolean permissionSwipe = false;

    protected int swipeTopMargin = 48;
    protected int swipeBottomMargin = 48;

    protected int width = AndroidUtilities.width;
    protected int bottomToolbarHeight = dp(48);

    protected int statusBarHeight = AndroidUtilities.statusBarHeight;
    protected int screenHeight = AndroidUtilities.screenHeight;
    protected int navigationBarHeight = AndroidUtilities.navigationBarHeight;

    protected int centerX = width / 2;
    protected int centerY;

    protected boolean loadingShow = false;
    private float loadingValue;
    protected int loadingTopMargin = 0;
    protected boolean hasStatusBar;

    private BottomToolBar bottomToolBar;
    protected boolean hasBottomToolbar = true;
    public static int currentPage;
    private int isPressed = -1;

    protected float swipeX = 0;
    protected float swipeY = 0;
    protected float dX;
    protected float dY;
    protected float velocity;

    protected float swipeToolbar;
    protected float swipeDY;

    protected Config config = new Config();

    private boolean isPause;
    private boolean isStoryShow = false;

    private ValueAnimator showAnimator;
    private ValueAnimator hideAnimator;

    protected String currentUrl = "";
    protected String url = "";
    protected int position = -1;
    protected int videoHeight;
    private float beforeZoomTranslationY = 0;

    protected ForwardDialog forwardDialog;

    protected fragment thisFragment = fragment.none;

    protected enum fragment {
        none, feed, explore, profile, profile_edit, list, chat, comment, gallery
    }

    protected View createView(Context context) {
        this.context = context;
        fragmentConfig();

        bottomToolBar = new BottomToolBar();

        hasBottomToolbar = !FatherView.instance().getFullScreen();

        if (thisFragment == fragment.profile_edit || thisFragment == fragment.chat || thisFragment == fragment.comment) {
            hasBottomToolbar = false;
        }

        if (!hasBottomToolbar)
            swipeBottomMargin = 0;

        parentView = new FrameLayout(context) {
            @Override
            public void dispatchDraw(Canvas canvas) {
                canvas.save();

                if (config.animation == 3) {
                    canvas.translate(swipeX, 0);
                }
                if (thisFragment == fragment.profile)
                    canvas.translate(0, swipeY);
                else
                    canvas.translate(0, swipeY);


                if (thisFragment == fragment.profile)
                    canvas.drawRect(0, 0, width, statusBarHeight, Themp.PAINT_WHITE);
                super.dispatchDraw(canvas);
                canvas.restore();
            }
        };

        contentView = new FrameLayout(getParentActivity()) {
            @Override
            public void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);

                if (loadingShow) {

                    int size = 144;
                    int half = size / 2;

                    canvas.drawRect(0, loadingTopMargin, width, getHeight(), Themp.PAINT_WHITE);

                    canvas.save();
                    canvas.translate((centerX - half), (getHeight() / 2) - half);
                    canvas.rotate(loadingValue, half, half);
                    canvas.drawBitmap(Themp.Loading.medium, 0, 0, null);

                    canvas.restore();
                }

                float dx = 0;
                if (swipe != null) {
                    dx = swipe.getX();
                }

                if (config.hasTopToolbar) {
                    toolbar.init(canvas);
                    toolbar.dispatchDraw(dx, swipeToolbar);
                }

                if (hasBottomToolbar) {
                    bottomToolBar.dispatchDraw(dx, canvas);
                }
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                BaseFragment.this.onTouchEvent(event);
                event(event);
                toolbar.events(event);
                return true;
            }

            @Override
            public boolean onInterceptTouchEvent(MotionEvent event) {
                BaseFragment.this.onInterceptTouchEvent(event);
                event(event);
                toolbar.events(event);
                return false;
            }
        };

        contentView.setVisibility(View.INVISIBLE);

        toolbar = new ToolBar(contentView);
        toolbar.setDelegate(new ToolBar.Delegate() {
            @Override
            public void leftPressed() {
                toolbarLeftPressed();
            }

            @Override
            public void centerPressed() {
                toolbarCenterPressed();
            }

            @Override
            public void rightPressed() {
                toolbarRightPressed();
            }
        });

        if (config.hasServerData) {
            loadingShow = true;
            contentView.invalidate();
        }

        ValueAnimator loadingAnimator = new ValueAnimator();
        loadingAnimator.setValues(PropertyValuesHolder.ofInt("counter1", 0, 360));
        loadingAnimator.setDuration(1000);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setInterpolator(new LinearInterpolator());
        loadingAnimator.addUpdateListener(animation -> {
            loadingValue = (int) animation.getAnimatedValue("counter1");
            contentView.invalidate();
        });
        loadingAnimator.start();

        size = dp(18);

        contentView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        if (linearLayoutManager == null) {
            linearLayoutManager = new CustomLinearLayoutManager(context);
        }

        if (config.hasSwipe) {

            swipe = new RefreshRecyclerView(context);
            swipe.setClipToPadding(false);
            swipe.recyclerView.setLayoutManager(linearLayoutManager);
            swipe.recyclerView.setClickable(false);
            swipe.recyclerView.setAdapter(adapter);
            swipe.setDelegate(new RefreshRecyclerView.Delegate() {
                @Override
                public void startRefresh() {
                    //springSend();
                    new Handler().postDelayed(() -> swipe.done(), 500);
                }

                @Override
                public void update() {
                    swipeScrolled();
                }

                @Override
                public void reset() {
                    swipeScrolled();
                }
            });

            swipe.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    //Log.e("onScrollStateChanged", "newState:" + newState);
                }
            });
            swipe.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    swipeDY -= dy;
                    swipeScrolled();
                    setScroll(recyclerView);

                    if (thisFragment == fragment.list) {

                        swipeToolbar -= dy;

                        if (swipeToolbar >= 0)
                            swipeToolbar = 0;

                        if (swipeToolbar < -dp(300))
                            swipeToolbar = -dp(300);

                    } else if (thisFragment == fragment.gallery) {

                        swipeToolbar -= dy;

                        if (swipeToolbar >= 0)
                            swipeToolbar = 0;

//                        if (swipeToolbar < -dp(300))
//                            swipeToolbar = -dp(300);
                    }

                    contentView.invalidate();
                }
            });
            swipe.recyclerView.setItemAnimator(new DefaultItemAnimator() {
                @Override
                public boolean isRunning() {
                    return false;
                }
            });

            contentView.addView(swipe, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, swipeTopMargin, 0, swipeBottomMargin));
        }

        contentView.invalidate();

        if (config.hasServerData)
            serverSend();


        if (config.animation == 3) {
            contentView.setVisibility(View.VISIBLE);

            showAnimator = new ValueAnimator();
            showAnimator.setDuration(150);
            showAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            showAnimator.addUpdateListener(animation -> {
                swipeX = (float) animation.getAnimatedValue("swipeX");
                parentView.invalidate();
            });
            showAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //showStatusBar();
                    animationUpdate();
                    parentView.invalidate();
                }
            });
            showAnimator.setValues(PropertyValuesHolder.ofFloat("swipeX", width, 0));
            showAnimator.start();

            hideAnimator = new ValueAnimator();
            hideAnimator.setDuration(100);
            hideAnimator.setInterpolator(new LinearInterpolator());
            hideAnimator.addUpdateListener(animation -> {
                swipeX = (float) animation.getAnimatedValue("swipeX");
                parentView.invalidate();
            });
            hideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    coreFragment.closeLastFragment();
                }
            });

            contentView.setVisibility(View.VISIBLE);
        } else if (config.animation > 0) {

            contentView.setVisibility(View.VISIBLE);

            showAnimator = new ValueAnimator();
            showAnimator.setDuration(300);
            showAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            showAnimator.addUpdateListener(animation -> {
                swipeY = (float) animation.getAnimatedValue("swipeY");
                parentView.invalidate();
            });
            showAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //showStatusBar();
                    animationUpdate();
                    parentView.invalidate();
                }
            });
            showAnimator.setValues(PropertyValuesHolder.ofFloat("swipeY", screenHeight, 0));
            showAnimator.start();

            hideAnimator = new ValueAnimator();
            hideAnimator.setDuration(300);
            hideAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            hideAnimator.addUpdateListener(animation -> {
                swipeY = (float) animation.getAnimatedValue("swipeY");
                parentView.invalidate();
            });
            hideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    coreFragment.closeLastFragment();
                }
            });

        } else {
            contentView.setVisibility(View.VISIBLE);
        }


        //        if (FatherView.instance().getFullScreen()) {
        //            if (this instanceof SingleIGTVFragment || this instanceof ExploreIGTVFragment || this instanceof GalleryFragment) {
        //                hasStatusBar = false;
        //                parentView.addView(contentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        //            } else {
        //                hasStatusBar = true;
        //                parentView.addView(contentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, px(statusBarHeight), 0, 0));
        //            }
        //
        //        } else {

        hasStatusBar = true;
        parentView.addView(contentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        //}

        return null;
    }

    protected void setWithStatusBar() {
        parentView.removeView(contentView);
        parentView.addView(contentView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, px(statusBarHeight), 0, 0));
    }

    public class Config {
        public boolean hasServerData = true;
        public boolean hasSwipe = true;
        public boolean hasRecyclerViewVideo = false;
        public boolean hasTopToolbar = true;

        public int animation = 0;
    }

    private void fragmentConfig() {
        if (this instanceof SettingFragment) {
            config.hasServerData = false;
        }

        //        if (this instanceof FeedFragment || this instanceof ExploreFragment) {
        //            hasBottomToolBar = true;
        //        }
        //


        if (FatherView.instance().getFullScreen() && thisFragment == fragment.profile) {
            config.animation = 1;
        }


        if (this instanceof FeedFragment) {
            page = 0;
            size = 200;
        }

        if (this instanceof FeedFragment) {
            config.hasRecyclerViewVideo = true;
            config.hasServerData = true;
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    private void setScroll(RecyclerView recyclerView) {
        int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
        int visibleItemCount = firstVisibleItem == RecyclerView.NO_POSITION ? 0 : Math.abs(linearLayoutManager.findLastVisibleItemPosition() - firstVisibleItem) + 1;
        int totalItemCount = recyclerView.getAdapter().getItemCount();

        if (visibleItemCount != 0 && firstVisibleItem + visibleItemCount > totalItemCount - 2 && permissionLoading && has_next_page) {
            //refresh(false, false);
        }
    }

    public void presentFragment(BaseFragment fragment) {

        if (swipe != null && swipe.isSwipe()) {
            return;
        }
        FatherView.instance().presentFragment(fragment);
    }

    public void toolbarRightPressed() {

    }

    public void toolbarCenterPressed() {

    }

    public void toolbarLeftPressed() {
        finishFragment();
    }

    public void finishFragment() {
        if (FatherView.instance().isKeyboardShowing()) {
            keyboardHide(contentView);
            return;
        }
        coreFragment.closeLastFragment();
    }

    private void closeAnimation() {
        if (swipeY != 0 || swipeX != 0)
            return;

        if (config.animation == 3) {
            hideAnimator.setValues(PropertyValuesHolder.ofFloat("swipeX", 0, width));
        } else {
            hideAnimator.setValues(PropertyValuesHolder.ofFloat("swipeY", 0, screenHeight + dp(100)));
        }
        hideAnimator.start();
    }

    public void addVideoLayout() {

    }

    public void setEmpetyStateView() {
        //        if (emptyStateView == null) {
        //            emptyStateView = new EmptyStateView(context);
        //            emptyStateView.setVisibility(View.INVISIBLE);
        //            emptyStateView.setHome();
        //            emptyStateView.setDelegate(() -> {
        //
        //            });
        //            contentView.addView(emptyStateView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        //        }
    }

    //System Function----------------------------------------------------------

    public View getParent() {
        return contentView;
    }

    public Activity getParentActivity() {
        return coreFragment.getParentActivity();
    }

    protected BaseFragment getUnderFragment() {
        if (coreFragment.fragmentsStack.size() > 1)
            return coreFragment.fragmentsStack.get(coreFragment.fragmentsStack.size() - 2);
        return null;
    }

    protected boolean isTop() {
        if (coreFragment == null)
            return false;

        if (coreFragment.fragmentsStack == null || coreFragment.fragmentsStack.size() == 0)
            return false;

        return coreFragment.fragmentsStack.get(coreFragment.fragmentsStack.size() - 1) == this;
    }

    public void animationUpdate() {

    }

    public void setParentLayout(CoreFragment coreFragment) {
        this.coreFragment = coreFragment;
    }

    public void onFragmentCreate() {

    }

    protected void onFragmentDestroy() {

    }

    public void onResume() {
        isPause = false;

        if (isStoryShow) {
//            storyCell.onResume();
        }

        contentView.invalidate();

        if (swipe == null) {
            return;
        }

        addVideoLayout();
    }

    public void onPause() {
        Log.e("onPause", this.getClass().toString());
        isPause = true;
    }

    public boolean onBackPressed() {
        if (config.animation > 0) {
            closeAnimation();
            return false;
        }

        return true;
    }

    protected void swipeScrolled() {
    }

    //Story----------------------------------------------------------

    protected boolean isStoryShow() {
        return isStoryShow;
    }


    protected void endStoryShow() {
        isStoryShow = false;
    }


    //PostCell----------------------------------------------------------

    public void permissionSwipe(boolean enabled) {
        permissionSwipe = enabled;

        swipe.setScrollEnabled(false);
        CustomLinearLayoutManager a = (CustomLinearLayoutManager) linearLayoutManager;
        a.setScrollEnabled(enabled);
    }

    public void setX(int dx) {
        if (linearLayoutManager instanceof CustomLinearLayoutManager && swipe != null) {
            boolean enabled = dx > dp(5) ? false : true;
            swipe.setScrollEnabled(enabled);

            CustomLinearLayoutManager a = (CustomLinearLayoutManager) linearLayoutManager;
            a.setScrollEnabled(enabled);
        }
    }

    protected void forward() {
        //forwardDialog.refresh(true, post);
        //forwardDialog.show();
    }

    protected void menu(Post post) {
        QMenu.getInstance().post(context, post, true, () -> {
            //refresh(true, true)
        });
    }

    //Server----------------------------------------------------------
    protected void ok() {
        permissionLoading = true;
        if (swipe != null)
            swipe.done();


        if (config.animation == 0 && !(thisFragment == fragment.profile) || config.animation == 3) {
            loadingShow = false;
            contentView.invalidate();
        }
    }

    protected void serverSend() {
        if (permissionLoading == false) {
            return;
        }

        permissionLoading = false;
        loadingShow = true;
        contentView.invalidate();
    }

    protected <T> void serverOnResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            ok();
        }
    }

    protected <T> void serverOnFailure(Call<T> call, Throwable t) {
        Toast.makeText(context, "server error " + t.toString(), Toast.LENGTH_LONG).show();
    }

    protected <T> void server(Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                serverOnResponse(call, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                serverOnFailure(call, t);
            }
        });
    }

    private void event(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int count = 4;
        int top = parentView.getHeight() - dp(48);
        int h = parentView.getHeight();
        int cell = width / count;

        toolbar.events(event);

        if (FatherView.instance().getFullScreen() || !hasBottomToolbar)
            return;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (x < cell && y > top) {
                    isPressed = 0;
                } else if (x > cell && x < (cell * 2) && y > top && y < h) {
                    isPressed = 1;
                } else if (x > (cell * 2) && x < (cell * 3) && y > top && y < h) {
                    isPressed = 2;
                } else if (x > (cell * 3) && x < (cell * 4) && y > top && y < h) {
                    isPressed = 3;
                } else if (x > (cell * 4) && x < (cell * 5) && y > top && y < h) {
                    isPressed = 4;
                } else {
                    isPressed = -1;
                }

                contentView.invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                if (x < cell && y > top && y < h && isPressed == 0) {
                    currentPage = 0;
                    FatherView.instance().page(currentPage);
                } else if (x > cell && x < (cell * 2) && y > top && y < h && isPressed == 1) {
                    currentPage = 1;
                    FatherView.instance().page(currentPage);
                } else if (x > (cell * 2) && x < (cell * 3) && y > top && y < h && isPressed == 2) {
                    currentPage = 2;
                    FatherView.instance().page(currentPage);
                } else if (x > (cell * 3) && x < (cell * 4) && y > top && y < h && isPressed == 3) {
                    currentPage = 3;
                    FatherView.instance().page(currentPage);
                } else if (x > (cell * 4) && x < (cell * 5) && y > top && y < h && isPressed == 4) {
                    currentPage = 4;
                    FatherView.instance().page(currentPage);
                }

                isPressed = -1;
                contentView.invalidate();
                break;
        }
    }

    protected void onActivityResultFragment(int requestCode, int resultCode, Intent data) {

    }

    protected void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {

    }
    protected String videoGetCurrentUrl() {
        return currentUrl;
    }

    protected static class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }

    }
}
