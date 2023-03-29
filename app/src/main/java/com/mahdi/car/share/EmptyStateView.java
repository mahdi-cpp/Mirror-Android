package com.mahdi.car.share;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahdi.car.core.cell.CellFrameLayout;
import com.mahdi.car.share.component.ui.LayoutHelper;


public class EmptyStateView extends CellFrameLayout
{

    private Context context;
    private ImageView imageView;

    private TextView titleTextView;
    private TextView messageTextView;
    private TextView linkTextView;
    private TextView linkTextView2;
    private TextView errorTextView;

    private Delegate delegate;
    private EmptyStateViewDelegate2 delegate2;

    private EmptyCell emptyCell;


    private boolean isPressed = false;

    public EmptyStateView(Context context)
    {
        super(context);

        setBackgroundColor(0x00ffffff);
        setClickable(false);

        this.context = context;

        setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 0));

        imageView = new ImageView(context);
        imageView.setImageDrawable(Themp.drawableEmptyState[0]);
        imageView.setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
        addView(imageView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 40, 0, 0));


        titleTextView = new TextView(context);
        titleTextView.setTextColor(0xff000000);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        //titleTextView.setTypeface(App.typeface, Typeface.BOLD);
        titleTextView.setSingleLine();
        titleTextView.setMaxLines(1);
        titleTextView.setGravity(Gravity.CENTER);
        addView(titleTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 150, 0, 0));

        messageTextView = new TextView(context);
        messageTextView.setTextColor(0xff000000);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        messageTextView.setLineSpacing(1.0f, 1.2f);
        //messageTextView.setTypeface(App.typeface);
        messageTextView.setGravity(Gravity.CENTER);
        addView(messageTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 30, 190, 30, 0));

        OnTouchListener gestureListener = new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        linkTextView.setTextColor(0xff90CAF9);
                        invalidate();
                        isPressed = true;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (isPressed && y > 0 && y < dp(50)) {
                            linkTextView.setTextColor(0xff90CAF9);
                        } else {
                            linkTextView.setTextColor(0xff2196F3);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:

                        linkTextView.setTextColor(0xff2196F3);

                        if (isPressed && y > 0 && y < dp(50)) {
                            if (delegate != null)
                                delegate.link();
                        }

                        invalidate();
                        break;
                    default:
                        linkTextView.setTextColor(0xff2196F3);
                        break;
                }
                return true;
            }
        };

        linkTextView = new TextView(context);
        linkTextView.setTextColor(0xff2196F3);
        linkTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        //linkTextView.setTypeface(App.typeface, Typeface.BOLD);
        linkTextView.setGravity(Gravity.CENTER);
        linkTextView.setOnTouchListener(gestureListener);
        linkTextView.setClickable(true);
        linkTextView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        addView(linkTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 40, Gravity.CENTER_HORIZONTAL, 0, 260, 0, 0));


        OnTouchListener gestureListener2 = new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        linkTextView2.setTextColor(0xff90CAF9);
                        invalidate();
                        isPressed = true;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (isPressed && y > 0 && y < dp(50)) {
                            linkTextView2.setTextColor(0xff90CAF9);
                        } else {
                            linkTextView2.setTextColor(0xff2196F3);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:

                        linkTextView2.setTextColor(0xff2196F3);

                        if (isPressed && y > 0 && y < dp(50)) {
                            if (delegate2 != null)
                                delegate2.delete();
                        }

                        invalidate();
                        break;
                    default:
                        linkTextView2.setTextColor(0xff2196F3);
                        break;
                }
                return true;
            }
        };

        linkTextView2 = new TextView(context);
        linkTextView2.setTextColor(0xff2196F3);
        linkTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        //linkTextView2.setTypeface(App.typeface);
        linkTextView2.setGravity(Gravity.CENTER);
        linkTextView2.setOnTouchListener(gestureListener2);
        linkTextView2.setVisibility(INVISIBLE);
        addView(linkTextView2, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 40, Gravity.CENTER_HORIZONTAL, 0, 260, 0, 0));

        errorTextView = new TextView(context);
        errorTextView.setVisibility(INVISIBLE);
        errorTextView.setTextColor(0xfff44336);
        errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        //errorTextView.setTypeface(App.typeface);
        errorTextView.setGravity(Gravity.CENTER);
        errorTextView.setText("پست شما باید حداقل یک تصویر داشته باشد.");
        addView(errorTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 40, Gravity.CENTER_HORIZONTAL, 0, 330, 0, 0));
    }

    public void newPost()
    {

        int top = 40;
        imageView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 40 + top, 0, 0));
        titleTextView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 150 + top, 0, 0));
        messageTextView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 30, 190 + top, 30, 0));


        setLinkMargin(300);
        imageView.setImageDrawable(Themp.drawableEmptyState[1]);
        titleTextView.setText("پست جدید");
        messageTextView.setText("پست جدید شما می تواند یک عکس ، یک ویدیو و یا یک آلبوم عکس (10 تایی) باشد.");
        linkTextView.setText("اولین عکس یا ویدیو را وارد کنید");
    }

    public void setHome()
    {
        setLinkMargin(250);
        imageView.setImageDrawable(Themp.drawableEmptyState[2]);
        titleTextView.setText(" به Homegram خوش آمدید");
        messageTextView.setText("وقتی دیگران را دنبال می کنید، پست هایی که به اشتراک می گذارند را در اینجا می بینید.");
        linkTextView.setText("اولین فرد را دنبال کنید");
    }

    public void setPost()
    {
        setLinkMargin(250);
        imageView.setImageDrawable(Themp.drawableEmptyState[3]);
        titleTextView.setText("نمایه");
        messageTextView.setText("وقتی عکس و ویدیوها را به اشتراک می گذارید، در نمایه تان نشان داده خواهد شد.");
        linkTextView.setText("اولین پست خود را به اشتراک بگذارید");

    }

    public void setPolygon(boolean show_link)
    {
        setLinkMargin(250);
        imageView.setImageDrawable(Themp.drawableEmptyState[4]);
        titleTextView.setText("ناحیه های ذخیره شده");
        messageTextView.setText("وقتی یک ناحیه را از روی نقشه ذخیره می کنید، در این اینجا نمایش داده می شود.");

        if (show_link) {
            linkTextView.setVisibility(VISIBLE);
            linkTextView.setText("اولین ناحیه خود را ذخیره کنید");
        } else {
            linkTextView.setVisibility(INVISIBLE);
        }
    }

    public void setBookmark()
    {
        imageView.setImageDrawable(Themp.drawableEmptyState[5]);
        titleTextView.setText(" ذخیره");
        messageTextView.setText("پست هایی را که می خواهید دوباره ببینید ذخیره کنید. به هیچ کسی اطلاع داده نمی شود و فقط شما می توانید آنچه را ذخیره کردید ببینید.");
        linkTextView.setText("");
    }

    public void setArchive()
    {
        imageView.setImageDrawable(Themp.drawableEmptyState[6]);
        titleTextView.setText("پست های بایگانی شده");
        messageTextView.setText("وقتی پست هایتان را بایگانی می کنید، در اینجا نشان داده می شوند. فقط شما می توانید آن ها را ببینید. با ضربه زدن روی دکمه سه نقطه پست هایتان را بایگانی کنید.");
        linkTextView.setText("");
    }

    public void setFollowers()
    {
        imageView.setImageDrawable(Themp.drawableEmptyState[7]);
        titleTextView.setText("افرادی که شما را دنبال می کنند");
        messageTextView.setText("وقتی دیگران شما را دنبال کنند، آن ها را در اینجا خواهید دید.");
        linkTextView.setText("");
    }

    public void setFollowings()
    {
        imageView.setImageDrawable(Themp.drawableEmptyState[8]);
        titleTextView.setText("افرادی که دنبال می کنید");
        messageTextView.setText("وقتی دیگران را دنبال می کنید، آن ها را در اینجا خواهید دید.");
        linkTextView.setText("");
    }

    public void setExplore()
    {
        //imageView.setImageResource(R.drawable.empty_state_iran);
        titleTextView.setText("کاوش کردن در استان " );
        messageTextView.setText("وقتی دیگران در این استان پست به اشتراگ بگذارند، آن پست ها را در اینجا می بینید. متاسفانه هنوز پستی در این استان به اشتراک گذاشته نشده است.");
        linkTextView.setText("کاوش کردن در دیگر استان ها");
    }

    public void setCart()
    {
        imageView.setImageDrawable(Themp.drawableEmptyState[9]);
        titleTextView.setText("سبد خرید");
        messageTextView.setText("وقتی خرید می کنید، لیست خرید خود را در اینجا مشاهده خواهید کرد، در حال حاظر شما هیچ خریدی انجام نداده اید.");
        linkTextView.setText("");
    }

    public void setNotificationMe()
    {
        setLinkMargin(280);
        imageView.setImageDrawable(Themp.drawableEmptyState[10]);
        titleTextView.setText("اعلانات شما");
        messageTextView.setText("وقتی یک ناحیه را از روی نقشه ذخیره می کنید، وقتی دیگران شما را دنبال کنند و یا پست های شما را پسند کنند اعلانات آن را در اینجا مشاهده خواهید کرد. در حال حاظر موردی یافت نشد.");
        linkTextView.setText("");
    }

    public void setNotificationFollowings()
    {
        setLinkMargin(260);

        imageView.setImageDrawable(Themp.drawableEmptyState[11]);
        titleTextView.setText("فعالیت افرادی که دنبال می کنید");
        messageTextView.setText("وقتی افرادی که دنبال می کنید دیگران را دنبال کنند و یا پست آن ها را پسند کنند در اینجا مشاهده خواهید کرد. در حال حاظر موردی یافت نشد.");
        linkTextView.setText("");
    }

    private void setLinkMargin(int margin)
    {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dp(margin), 0, 0);
        linkTextView.setLayoutParams(params);
    }


    public void location()
    {

        imageView.setVisibility(VISIBLE);
        titleTextView.setVisibility(VISIBLE);
        messageTextView.setVisibility(VISIBLE);
        linkTextView.setVisibility(VISIBLE);

        emptyCell = new EmptyCell(context);
        addView(emptyCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 8, Gravity.TOP));

        setLinkMargin(250);
        imageView.setImageDrawable(Themp.drawableEmptyState[12]);
        titleTextView.setText("ثبت دقیق موقعیت ملک");
        messageTextView.setText("بسیاری از افراد برای پیدا کردن ملک مورد نظر خود از نقشه استفاده می کنند، با ثبت دقیق موقعیت مکانی این آگهی بازدید آن را افزایش دهید.");

        linkTextView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 40, Gravity.CENTER_HORIZONTAL, 0, 260, 0, 0));
        linkTextView.setText("ثبت موقعیت");

        linkTextView2.setVisibility(INVISIBLE);
    }

    public void chat()
    {
        setLinkMargin(240);
        imageView.setImageDrawable(Themp.drawableEmptyState[10]);
        titleTextView.setText(" چت کردن");
        messageTextView.setText("امکان چت کردن بزودی فعال خواهد شد");
        linkTextView.setText("شروع چت");
    }

    public void show()
    {
        setVisibility(VISIBLE);
    }

    public void hide()
    {

        imageView.setVisibility(INVISIBLE);
        titleTextView.setVisibility(INVISIBLE);
        messageTextView.setVisibility(INVISIBLE);

        linkTextView.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 40, Gravity.RIGHT, 0, 390, 80, 0));
        linkTextView.setText("ثبت مجدد");

        linkTextView2.setLayoutParams(LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 40, Gravity.LEFT, 80, 390, 0, 0));
        linkTextView2.setText("حذف موقعیت");
        linkTextView2.setVisibility(VISIBLE);

    }

    public void showError(boolean visible)
    {
        errorTextView.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }

    public void setDelegateDelete(EmptyStateViewDelegate2 delegate)
    {
        this.delegate2 = delegate;
    }

    public class EmptyCell extends FrameLayout
    {

        int cellHeight;

        public EmptyCell(Context context)
        {
            this(context, dp(8));
        }

        public EmptyCell(Context context, int height)
        {
            super(context);
            setBackgroundColor(0xfff0f0f0);
            cellHeight = height;
        }

        public void setHeight(int height)
        {
            cellHeight = height;
            requestLayout();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.EXACTLY));
        }
    }

        public interface Delegate
    {
        void link();
    }

    public interface EmptyStateViewDelegate2
    {
        void delete();
    }

}
