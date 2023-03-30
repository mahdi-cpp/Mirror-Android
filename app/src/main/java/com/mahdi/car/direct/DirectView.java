package com.mahdi.car.direct;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdi.car.core.FatherView;
import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.core.component.ToolBar;
import com.mahdi.car.direct.cell.DirectCell;
import com.mahdi.car.direct.cell.SearchCell;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.server.https.Server;
import com.mahdi.car.server.model.User;
import com.mahdi.car.share.CustomGridLayoutManager;
import com.mahdi.car.share.RefreshRecyclerView;
import com.mahdi.car.share.cell.LoadingCell;
import com.mahdi.car.share.component.ui.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectView extends CellFrameLayout {
    private RefreshRecyclerView swipe;
    private CustomGridLayoutManager layoutManager;
    private Adapter adapter;

    private List<User> users = new ArrayList<>();

    private long count;
    private boolean isShow = false;

    private boolean isShow2 = false;

    protected ToolBar toolbar;

    public interface Delegate {
        void leftPressed();
    }

    private Delegate delegate;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public DirectView(Context context) {
        super(context);

        setTranslationX(width);

        layoutManager = new CustomGridLayoutManager(context, 1);

        swipe = new RefreshRecyclerView(context);
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

                if (visibleItemCount != 0 && firstVisibleItem + visibleItemCount > totalItemCount - 2 && users.size() < count) {
                    Log.e("onScrolled", "Estate:" + users.size());
                    refresh();
                }
            }
        });

        swipe.setDelegate(new RefreshRecyclerView.Delegate() {
            @Override
            public void startRefresh() {
                refresh();
            }

            @Override
            public void update() {

            }

            @Override
            public void reset() {

            }
        });


        toolbar = new ToolBar(this);
        toolbar.settDirect("mahdi.abd.1987");
        toolbar.setDelegate(new ToolBar.Delegate() {
            @Override
            public void leftPressed() {
                setShow(false);
//                resetDirect();
//                delegate.leftPressed();
            }

            @Override
            public void centerPressed() {

            }

            @Override
            public void rightPressed() {

            }
        });

        addView(swipe, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 47, 0, 0));
        //setBackgroundColor(0x44ff9800);

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

    //    private void processScroll(MotionEvent e1, float distanceX, float distanceY)
    //    {
    //        dX -= distanceX;
    //        dY -= distanceY;
    //
    //        if (Math.abs(dX) > Math.abs(dY) && !swipe.isSwipe()) {
    //
    //            layoutManager.setScrollEnabled(false);
    //            swipe.setScrollEnabled(false);
    //
    //            containerView.setTranslationX(dX - 1);
    //            feedContainer.setTranslationX(-width + dX);
    //        }
    //    }

    public void setDirectTranslationX(float dX) {
        setTranslationX(width + dX - 1);
    }

    public void resetDirect() {
        isShow = false;
        ViewAnimator.animate(this).setInterpolator(new LinearOutSlowInInterpolator()).translationX(width).setDuration(300).start();
    }

    public void showDirect() {
        isShow = true;
        ViewAnimator.animate(this).setInterpolator(new LinearOutSlowInInterpolator()).translationX(0).setDuration(300).start();

        if (users.size() == 0) {
            refresh();
        }
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

    public void setUsers(List<User> users) {
        this.users = users;
        adapter.notifyDataSetChanged();
    }

    public void done() {
        swipe.done();
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public class Adapter extends RecyclerView.Adapter {
        private Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            if (viewType == 1) {
                view = new SearchCell(context);
            } else if (viewType == 2) {
                view = new DirectCell(parent.getContext());
            } else {
                view = new LoadingCell(context);
            }

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.itemView instanceof SearchCell) {

            } else if (holder.itemView instanceof DirectCell) {

                DirectCell cell = (DirectCell) holder.itemView;
                User user = users.get(position);

                if (user != null) {
                    cell.setUser(user, position);
                    cell.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 78));
                    cell.setDelegate(new DirectCell.Delegate() {
                        @Override
                        public void click(User user) {
//                            if (swipe.isSwipe())
//                                return;
                            FatherView.instance().presentFragment(new ChatFragment(user));
                        }
                    });
                }

            } else {

                LoadingCell loadingCell = (LoadingCell) holder.itemView;
                FrameLayout.LayoutParams params;

                if (count == users.size() || users.size() == 0)
                    params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp(0));
                else
                    params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp(0));

                loadingCell.setLayoutParams(params);
                loadingCell.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return users.size() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 1;
            } else if (position >= 0 && position < users.size()) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public void refresh() {
        if (users.size() > 0)
            return;

        Call<List<User>> call = Server.user.getAllUsers();
        if (call != null) {

            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {

                        List<User> newUsers = response.body();

                        for (int a = 0; a < newUsers.size(); a++) {
                            User f = newUsers.get(a);
                            users.add(f);
                        }

                        if (newUsers.size() > 0) {
                            //count = newUsers.get(0).count;
                        } else {
                            count = users.size();
                        }

                        adapter.notifyDataSetChanged();

                        done();

                    } else {
                        done();
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    done();
                }
            });
        }
    }
}
