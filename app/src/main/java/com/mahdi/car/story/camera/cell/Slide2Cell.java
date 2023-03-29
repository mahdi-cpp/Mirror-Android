package com.mahdi.car.story.camera.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Gravity;

import com.mahdi.car.core.cell.CellView;
import com.mahdi.car.share.component.ui.LayoutHelper;
import com.mahdi.car.server.model.Category;

public class Slide2Cell extends CellView
{

    public interface Delegate
    {
        void click(Category category);
    }

    private Delegate delegate;


    private StaticLayout replyNameLayout;
    private TextPaint nameTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ColorFilter filter;

    private String name = "---";
    private boolean isPressed = false;
    private int round = dp(4);

    private int position = 0;

    private int w = 104;
    private int color = 0xffffffff;

    public int getPosition()
    {
        return position;
    }

    public Slide2Cell(Context context)
    {

        super(context);

        setLayoutParams(LayoutHelper.createFrame(w, 60, Gravity.TOP, 0, 2, 0, 4));

        nameTextPaint.setTextSize(dp(13));
        nameTextPaint.setColor(0xffffffff);
        nameTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        nameTextPaint.setStrokeWidth(dp(0.3f));

        filter = new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.SRC_IN);
        iconPaint.setColorFilter(filter);

        //paint.setShader(new LinearGradient(0, 0, 0, AndroidUtilities.dp(88), COLORS, LOCATIONS, Shader.TileMode.REPEAT));
    }

    public void setName(String name, int position)
    {

        this.position = position;

        if (name == null) {
            return;
        }

        this.name = name;
        isPressed = false;

        w = name.length() * dp(position == 3 ? 3.2f : 3.5f);
        setLayoutParams(LayoutHelper.createFrame(w, 60, Gravity.TOP, 0, 2, 0, 4));


        invalidate();
    }

    public void setColor(int color)
    {
        nameTextPaint.setColor(color);
        invalidate();
    }

    @Override protected void onDraw(Canvas canvas)
    {


        RectF rectF = new RectF(dp(4), dp(8), dp(w - 4), dp(60 - 8 - 12));

        if (isPressed) {
            paint.setColor(0x0b000000);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rectF, round, round, paint);
        }


        canvas.save();
        canvas.translate(0, dp(25));
        replyNameLayout = new StaticLayout(name, nameTextPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        replyNameLayout.draw(canvas);
        canvas.restore();

    }


    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

}
