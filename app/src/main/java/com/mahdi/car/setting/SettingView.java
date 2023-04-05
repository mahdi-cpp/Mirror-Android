package com.mahdi.car.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.RootView;
import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.core.component.ToolBar;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.setting.cell.BitrateView;
import com.mahdi.car.setting.cell.ProfileCell;
import com.mahdi.car.setting.cell.ResolutionView;
import com.mahdi.car.setting.cell.SettingCell;
import com.mahdi.car.share.CustomGridLayoutManager;
import com.mahdi.car.share.RefreshRecyclerView;
import com.mahdi.car.share.Themp;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class SettingView extends CellFrameLayout {

    private static int rowCount = 0;
    private static final int rowProfile = rowCount++;
    private static final int rowMirrorSeparate = rowCount++;
    private static final int rowBitrate = rowCount++;

    private static final int rowResolution = rowCount++;
    private static final int rowAboutSeparate = rowCount++;

    private static final int rowGithub = rowCount++;
    private static final int rowEmptyEnd = rowCount++;

    private RefreshRecyclerView swipe;
    private CustomGridLayoutManager layoutManager;
    private Adapter adapter;

    private boolean isShow = false;

    protected ToolBar toolbar;

    private SharedPreferences mirrorPreferences;
    private SharedPreferences.Editor mirrorPreferencesEdit;

    public interface Delegate {
        void leftPressed();
    }

    private Delegate delegate;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public SettingView(Context context) {
        super(context);

        setTranslationX(width);

        layoutManager = new CustomGridLayoutManager(context, 1);

        swipe = new RefreshRecyclerView(context);
        swipe.setScrollEnabled(true);
        swipe.recyclerView.setLayoutManager(layoutManager);
        swipe.recyclerView.setHorizontalScrollBarEnabled(false);
        swipe.recyclerView.setVerticalScrollBarEnabled(false);
        swipe.recyclerView.setAdapter(adapter = new Adapter(context));
        swipe.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = firstVisibleItem == RecyclerView.NO_POSITION ? 0 : Math.abs(layoutManager.findLastVisibleItemPosition() - firstVisibleItem) + 1;
                int totalItemCount = recyclerView.getAdapter().getItemCount();
            }
        });

        swipe.setDelegate(new RefreshRecyclerView.Delegate() {
            @Override
            public void startRefresh() {
                //refresh();
            }

            @Override
            public void update() {

            }

            @Override
            public void reset() {

            }
        });


        toolbar = new ToolBar(this);
        toolbar.settDirect("Settings");
        toolbar.setDelegate(new ToolBar.Delegate() {
            @Override
            public void leftPressed() {
                setShow(false);
            }

            @Override
            public void centerPressed() {

            }

            @Override
            public void rightPressed() {

            }
        });


        addView(swipe, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 47, 0, 0));

        mirrorPreferences = context.getSharedPreferences("mirror", 0); // 0 - for private mode
        mirrorPreferencesEdit = mirrorPreferences.edit();

        invalidate();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        width = getWidth();
        screenHeight = getHeight();

        //        if (loadingShow) {
        //            canvas.drawRect(0, loadingTopMargin, width, getHeight(), Themp.PAINT_WHITE);
        //            centerY = (getHeight() + loadingTopMargin) / 2;
        //            canvas.drawCircle(getWidth() / 2, centerY, size, Themp.STROKE_PAINT_1_5DP_FFEFEFEF);
        //            canvas.drawArc(new RectF(centerX - size, centerY - size, centerX + size, centerY + size), loadingCounter - 45, loadingCounter, false, Themp.STROKE_PAINT_1_5DP_FFDDDDDD);
        //        }

        float dx = 0;
        if (swipe != null) {
            dx = swipe.getX();
        }

        //canvas.drawCircle(width / 2, height / 2, dp(100), Themp.PAINT_PRESS_BLACK);

        toolbar.init(canvas);
        toolbar.dispatchDraw(0, 0);
    }

//        private void processScroll(MotionEvent e1, float distanceX, float distanceY) {
//            dX -= distanceX;
//            dY -= distanceY;
//
//            if (Math.abs(dX) > Math.abs(dY) && !swipe.isSwipe()) {
//
//                layoutManager.setScrollEnabled(false);
//                swipe.setScrollEnabled(false);
//
//                containerView.setTranslationX(dX - 1);
//                feedContainer.setTranslationX(-width + dX);
//            }
//        }

    public void setDirectTranslationX(float dX) {
        setTranslationX(width + dX - 1);
    }

    public void reset() {
        isShow = false;
        ViewAnimator.animate(this).setInterpolator(new LinearOutSlowInInterpolator()).translationX(width).setDuration(300).start();
    }

    public void show() {
        isShow = true;
        ViewAnimator.animate(this).setInterpolator(new LinearOutSlowInInterpolator()).translationX(0).setDuration(300).start();
    }

    //    private void resetX()
    //    {
    //        if (getTranslationX() == 0) {
    //            return;
    //        }
    //
    //        layoutManager.setScrollEnabled(true);
    //        swipe.setScrollEnabled(true);
    //
    //        if (dX > centerX || velocity > 1000) {
    //            isShow = false;
    //            ViewPropertyObjectAnimator.animate(containerView).setInterpolator(new LinearOutSlowInInterpolator()).translationX(width).setDuration(250).start();
    //            ViewPropertyObjectAnimator.animate(feedContainer).setInterpolator(new LinearOutSlowInInterpolator()).translationX(0).setDuration(250).addListener(new AnimatorListenerAdapter()
    //            {
    //                @Override
    //                public void onAnimationEnd(Animator animation)
    //                {
    //                    super.onAnimationEnd(animation);
    //
    //                }
    //            }).start();
    //        } else {
    //            ViewPropertyObjectAnimator.animate(containerView).setInterpolator(new LinearOutSlowInInterpolator()).translationX(0).setDuration(250).start();
    //            ViewPropertyObjectAnimator.animate(feedContainer).setInterpolator(new LinearOutSlowInInterpolator()).translationX(-width).setDuration(250).start();
    //        }
    //    }

    public void endStoryShow() {
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < swipe.recyclerView.getChildCount(); i++) {

                View view = swipe.recyclerView.getChildAt(i);
                if (view instanceof CellView) {
                    CellView cell = (CellView) view;
                    cell.endStoryShow();
                }
            }
        }
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    private class Adapter extends RecyclerView.Adapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == rowProfile) {
                return new Holder(new ProfileCell(context));
            } else if (viewType == rowBitrate) {
                return new Holder(new BitrateView(context));
            } else if (viewType == rowResolution) {
                return new Holder(new ResolutionView(context));
            } else {
                return new Holder(new SettingCell(context));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (position == rowProfile) {
                ProfileCell cell = (ProfileCell) holder.itemView;
                cell.setUsername(mirrorPreferences.getString("username", "User"));
                cell.setDelegate(new ProfileCell.Delegate() {
                    @Override
                    public void button() {

                    }

                    @Override
                    public void edit() {
                        EditNameFragment fragment = new EditNameFragment(mirrorPreferences.getString("username", "User"));
                        fragment.setDelegate(username -> {
                            mirrorPreferencesEdit.putString("username", username);
                            mirrorPreferencesEdit.apply();
                            cell.setUsername(username);
                            fragment.finishFragment();
                        });
                        RootView.instance().presentFragment(fragment);
                    }
                });
            } else if (position == rowMirrorSeparate) {
                SettingCell cell = (SettingCell) holder.itemView;
                cell.setColor(0);
                cell.setEmpty(30);
            } else if (position == rowBitrate) {
                BitrateView cell = (BitrateView) holder.itemView;
                cell.setBitrate(mirrorPreferences.getInt("bitrate", 1));
                cell.setDelegate(bitrate -> {
                    mirrorPreferencesEdit.putInt("bitrate", bitrate);
                    mirrorPreferencesEdit.apply();
                });
            } else if (position == rowResolution) {
                ResolutionView cell = (ResolutionView) holder.itemView;
                cell.setResolution(mirrorPreferences.getInt("resolution", 1080));
                cell.setDelegate(resolution -> {
                    mirrorPreferencesEdit.putInt("resolution", resolution);
                    mirrorPreferencesEdit.apply();
                });

            } else if (position == rowAboutSeparate) {
                SettingCell cell = (SettingCell) holder.itemView;
                cell.setColor(0);
                cell.setEmpty(30);
            } else if (position == rowEmptyEnd) {
                SettingCell cell = (SettingCell) holder.itemView;
                cell.setEmpty(300);
            } else if (position == rowGithub) {
                SettingCell cell = (SettingCell) holder.itemView;
                cell.setIcon(Themp.setting.github, "www.github.com/mahdi-cpp/Mirror-Android");
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


}
