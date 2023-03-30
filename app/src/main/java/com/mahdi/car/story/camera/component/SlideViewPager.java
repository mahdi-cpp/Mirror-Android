package com.mahdi.car.story.camera.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.SnapHelper;
import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.share.Themp;

import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.library.viewAnimator.ViewAnimator;
import com.mahdi.car.story.camera.cell.Slide2Cell;
import com.mahdi.car.story.camera.cell.SlideView;

public class SlideViewPager extends CellFrameLayout
{
    private Delegate delegate;
    private SlideView slideView;

    private RecyclerView recyclerView;
    private Adapter adapter;

    private List<String> names = new ArrayList<>();

    private int index = 1;

    private Path clipPath;

    public SlideViewPager(Context context)
    {
        super(context);

        setMedia("67O1dlUA6TcatKo6HHbNd5zevts66w-UUHebKbTrTpytRRtXwDwQELvPMwUKp.jpg");

        slideView = new SlideView(context);
        avatarSize = dp(33);

        clipPath = new Path();
        clipPath.addRoundRect(new RectF(0, 0, dp(33), dp(33)), dp(8), dp(8), Path.Direction.CW);

        names.add("         ");
        names.add("CREATE");
        names.add("NORMAL");
        names.add("HANDS-FREE");
        names.add("      ");

        LinearLayoutManager horLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(horLayoutManager);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        recyclerView.setClickable(false);
        recyclerView.setAdapter(adapter = new Adapter(context));
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        setClickable(false);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                Slide2Cell cell = (Slide2Cell) helper.findSnapView(horLayoutManager);
                if (cell != null) {
                    if (index != cell.getPosition()) {
                        index = cell.getPosition();

                        Log.e("new index: ", "index: " + cell.getPosition());

                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                            Slide2Cell view = (Slide2Cell) recyclerView.getChildAt(i);
                            if (view != null) {
                                view.setColor(0xff888888);
                            }
                        }

                        delegate.index(index);
                        cell.setColor(0xffffffff);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

            }
        });

        adapter.notifyDataSetChanged();

        addView(recyclerView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.BOTTOM, 70, 0, 70, 0));
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawRect(new RectF(0, 0, width, getHeight()), Themp.PAINT_OPACITY_66000000);

        canvas.save();

        int round = dp(6);

        canvas.translate(dp(15), dp(18));
        canvas.clipPath(clipPath);
        drawMedia(dp(1), dp(1), dp(34), dp(34));
        canvas.restore();

        canvas.save();
        canvas.translate(dp(15), dp(18));
        canvas.drawRoundRect(new RectF(0, 0, avatarSize, avatarSize), round, round, Themp.STROKE_PAINT_2DP_WHITE);
        canvas.restore();

        canvas.drawBitmap(Themp.camera[4], width - Themp.camera[4].getWidth() - dp(15), dp(12), Themp.ICON_PAINT_SRC_IN_WHITE);//face camera

        canvas.save();
        Matrix rotator = new Matrix();
        rotator.postRotate(90);

        // or, rotate around x,y
        // NOTE: coords in bitmap-space!
        //int xRotate = dp(90);
        //int yRotate = dp(90);
        rotator.postRotate(90, 0, 0);

        // to set the position in canvas where the bitmap should be drawn to;
        // NOTE: coords in canvas-space!
        int xTranslate = width - dp(20);
        int yTranslate = dp(45);
        rotator.postTranslate(xTranslate, yTranslate);

        //canvas.drawBitmap(Theme.bitmapNavigationBack, rotator, iconPaint);
        canvas.restore();
        //canvas.drawBitmap(Theme.bitmapFace, width - dp(48), height - dp(74), iconPaint);
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        int corner = dp(100);

        //paint.setColor(0x66000000);
        //canvas.drawRect(new RectF(0, 0, corner, getHeight()), paint);
        //canvas.drawRect(new RectF(width - corner, 0, width, getHeight()), paint);

    }

    public void processScroll(MotionEvent event, float dX, float dY)
    {
        float y = event.getY();
        if (y > screenHeight - dp(70)) {

            //slideView.setTranslationX(dX);
            slideView.select(0);

            //            Animation animation = new TranslateAnimation(0, dp(100), 0, 0);
            //            animation.setDuration(300);
            //            animation.setFillAfter(true);
            //            slideView.startAnimation(animation);
        }
    }

    public void resetX()
    {
        if (slideView.getTranslationX() != 0)
            ViewAnimator.animate(slideView).setInterpolator(new DecelerateInterpolator()).translationX(0).setDuration(250).start();
    }

    public void click(MotionEvent event)
    {
        //
        //        float x = event.getX();
        //        float y = event.getY();
        //
        //        if (x > dp(70) && x < dp(200) && y > height - dp(70)) {
        //            //slideView.left();
        //            if (index > 0)
        //                index--;
        //        } else if (x > width - dp(200) && x < width - dp(70) && y > height - dp(70)) {
        //            //slideView.right();
        //            if ((index < names.size() + 1))
        //                index++;
        //        }
        //
        //        recyclerView.smoothScrollToPosition(index);

    }

    private class Adapter extends RecyclerView.Adapter
    {
        Context context;

        public Adapter(Context context)
        {
            this.context = context;
        }


        @Override
        public int getItemCount()
        {
            return names.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = new Slide2Cell(context);

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            Slide2Cell cell = (Slide2Cell) holder.itemView;

            String name = names.get(position);
            cell.setName(name, position);
            cell.setColor(position == 2 ? 0xffffffff : 0xff888888);
        }
    }

    public void init()
    {
        recyclerView.smoothScrollToPosition(3);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        float x = motionEvent.getX();
        float y = motionEvent.getX();

        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                if (x > width - dp(60))
                    delegate.switchCamera();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                isPressed = -1;
                break;
        }
        return true;
    }

    public interface Delegate
    {
        void showGallery();

        void index(int index);

        void switchCamera();
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }
}
