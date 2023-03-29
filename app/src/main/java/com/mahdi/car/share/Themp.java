package com.mahdi.car.share;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;

import com.mahdi.car.App;
import com.mahdi.car.R;
import com.mahdi.car.messenger.AndroidUtilities;

public class Themp
{
    public static class Toolbar
    {
//        public Bitmap logo = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.d0x);
        public Bitmap cast = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cast);
        public Bitmap movies = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.movies);
        public Bitmap music = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.music);
        public Bitmap finish = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dor);
        public Bitmap camera = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ar);
        public Bitmap cameraFill = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b0g);
        public Bitmap igtv = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.igtv_w);
        public Bitmap forward = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cjn);
        public Bitmap search = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.nav_search);
        public Bitmap microphone = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_microphone_24dp);
        public Bitmap more = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bt1);
        public Bitmap menuProfile = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b4l);
        public Bitmap AlertProfile = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cdf);
        public Bitmap plus = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b0x);
        public Bitmap close = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bsf);
        public Bitmap ok = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bed);
        public Bitmap dialog = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bm3);
        public Bitmap rotate = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_rotate);
        public Bitmap rotate2 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cb_);

        public Bitmap storyFeedAdd = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ct8);
        public Bitmap storyProfileAdd = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ct9);

        public Bitmap bigClose = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bwm);
        public Bitmap aaa = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.aaa1);

        public Bitmap camera_1 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cty);
        public Bitmap camera_2 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c_i);

        public Bitmap arrow_back = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.biz2);
    }


    public static class Setting
    {
        public Bitmap notification = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c4t);
        public Bitmap account = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dgh);
        public Bitmap about = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bql);
        public Bitmap help = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.k9);
        public Bitmap cash = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c55);

    }

    public static class Loading
    {
        public static Bitmap big = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bqi);
        public static Bitmap medium = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.loading);
        public static Bitmap small = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ka);

    }

    public static class Gallery
    {
        public static Bitmap multiSelect = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.sidecar_icon);
        public static Bitmap scale = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b9q);
        public static Bitmap smallClose = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.close72);
    }

    private static int w = AndroidUtilities.width;
    private static int TEXT_PAINT_COUNT = 15;
    private static int one = dp(1);


    public static Bitmap bitmapEmptyProfile = null;
    public static Drawable drawableEmptyProfile = null;

    public static Bitmap bitmapCheck = null;
    public static Bitmap bitmapMoreMenuToolBar = null;
    public static Bitmap bitmapDelete = null;
    public static Bitmap bitmapCloseCircle = null;

    public static Bitmap bitmapHand = null;
    public static Bitmap bitmapLocation_56 = null;
    public static Bitmap bitmapTick = null;
    public static Bitmap bitmapClose24 = null;
    public static Bitmap bitmapVerifiedProfile = null;


    public static Bitmap bitmapFilterOff = null;
    public static Bitmap bitmapPlus2 = null;
    public static Bitmap bitmapGallery = null;
    public static Bitmap shop95 = null;
    public static Bitmap shop72 = null;
    public static Bitmap favourit36 = null;
    public static Bitmap shop36 = null;
    public static Bitmap bitmapNext = null;
    public static Bitmap bitmapIGTVClose = null;

    public static Bitmap[] bitmapChat = new Bitmap[10];

    public static Bitmap bitmapCloseFriend = null;


    public static Bitmap bitmapArchive = null;
    public static Bitmap bitmapYourActivity = null;
    public static Bitmap bitmapSetting = null;
    public static Bitmap bitmapFace = null;

    public static Bitmap notificationCursor = null;
    public static Bitmap[] notifications = new Bitmap[5];


    public static Bitmap[] mainToolBarIcons = new Bitmap[5];
    public static Bitmap[] mainToolBarSelectedIcons = new Bitmap[5];


    public static Bitmap[] commentIcons = new Bitmap[5];
    public static Bitmap[] bitmapsHandListGrid = new Bitmap[4];
    public static Bitmap[] postCell = new Bitmap[30];
    public static Bitmap[] bitmapTicketGalleryToolBar = new Bitmap[6];
    public static Bitmap[] searchFilters = new Bitmap[10];
    public static Bitmap[] map_polygon_toolbar = new Bitmap[2];
    public static Bitmap[] drawableMap = new Bitmap[10];
    public static Bitmap[] iconFilters = new Bitmap[20];
    public static Bitmap[] homegramSmallCell = new Bitmap[5];
    public static Bitmap[] videoPlayer = new Bitmap[5];

    public static Bitmap[] story = new Bitmap[10];

    public static Bitmap[] camera = new Bitmap[10];
    public static Bitmap[] direct = new Bitmap[10];

    public static Bitmap[] storyCreate = new Bitmap[20];
    public static Bitmap[] storyPaint = new Bitmap[15];
    public static Bitmap[] storyText = new Bitmap[10];

    public static int color_profile_background = 0xFF888888;
    public static int color_Blue = 0xFF2196F3;

    public static Drawable[] drawableToolbar = new Drawable[10];
    public static Drawable[] drawableEmptyState = new Drawable[15];
    public static Drawable[] drawableCamera = new Drawable[15];
    public static Drawable[] drawableGallery = new Drawable[15];

    /*-------------------------------------------------------------------
    ---------------------------------------------------------------------
    ---------------------------------------------------------------------
    ---------------------------------------------------------------------*/

    public static Paint PAINT_DYNAMIC = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_PROFILE_BACKGROUND = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_WHITE = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_BLACK = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_DARK = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_GRAY = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_LINE = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_PROFILE_CIRCLE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_GREEN = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);


    public static Paint PAINT_PRESS_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_PRESS_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_PRESS_STRONG_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_SELECT_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);


    public static Paint PAINT_FFF8F8F8 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FFEEEEEE = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FFDDDDDD = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FFCCCCCC = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FFBBBBBB = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FFAAAAAA = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FF555555 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FF444444 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FF333333 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_FF222222 = new Paint(TextPaint.ANTI_ALIAS_FLAG);

    public static Paint PAINT_OPACITY_EE222222 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_DD222222 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_CC222222 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_BB222222 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_AA000000 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_66000000 = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_88FFFFFF = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_OPACITY_22000000 = new Paint(TextPaint.ANTI_ALIAS_FLAG);




    private static int[] colors = new int[10];
    public static Paint PAINT_RING = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    public static Paint PAINT_STORY_DASH = new Paint(TextPaint.ANTI_ALIAS_FLAG);
    //----------------------------------------------------------------------

    public static Paint STROKE_PAINT_PX_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_GREY = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_FFAAAAAA = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_FFBBBBBB = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_FFCCCCCC = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_PX_FFDDDDDD = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_1DP_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_2DP_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_8DP_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_18DP_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_1DP_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_2DP_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_1DP_GREY = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_1DP_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_2DP_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_1DP_FFAAAAAA = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1DP_FFBBBBBB = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1DP_FFCCCCCC = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1DP_FFDDDDDD = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_OPACITY_1DP_88FFFFFF = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_OPACITY_2DP_AAFFFFFF = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_OPACITY_3DP_DDFFFFFF = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint STROKE_PAINT_1_5DP_FFAAAAAA = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1_5DP_FFBBBBBB = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1_5DP_FFCCCCCC = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1_5DP_FFDDDDDD = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1_5DP_FFEEEEEE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_1_5DP_FFEFEFEF = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_4DP_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint STROKE_PAINT_4DP_RED = new Paint(Paint.ANTI_ALIAS_FLAG);
    //----------------------------------------------------------------------

    public static Paint LIST_PAINT_SHADER = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint IGTV_PAINT_SHADER = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint IGTV_PAINT_SHADER_BOTTOM = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint igtv_seriesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //----------------------------------------------------------------------

    public static Paint ICON_PAINT_SRC_IN_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint ICON_PAINT_SRC_IN_LIGHT = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint ICON_PAINT_MULTIPLY_WHITE = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint ICON_PAINT_SRC_IN_GREY = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint ICON_PAINT_MULTIPLY_GREY = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint ICON_PAINT_SRC_IN_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint ICON_PAINT_MULTIPLY_BLACK = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Paint ICON_PAINT_SRC_IN_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint ICON_PAINT_MULTIPLY_BLUE = new Paint(Paint.ANTI_ALIAS_FLAG);


    public static Paint ICON_PAINT_SRC_IN_RED = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint ICON_PAINT_MULTIPLY_RED = new Paint(Paint.ANTI_ALIAS_FLAG);
    //----------------------------------------------------------------------


    public static TextPaint[] TEXT_PAINT_FILL_WHITE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_LIGHT = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_BLACK = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_GREY = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_BLUE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_FFBBBBBB = new TextPaint[TEXT_PAINT_COUNT];

    public static TextPaint[] TEXT_PAINT_FILL_BOLD_WHITE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_BOLD_BLACK = new TextPaint[TEXT_PAINT_COUNT];


    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_1_WHITE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_2_WHITE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_3_WHITE = new TextPaint[TEXT_PAINT_COUNT];

    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_1_BLACK = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_2_BLACK = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_3_BLACK = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_4_BLACK = new TextPaint[TEXT_PAINT_COUNT];

    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_1_GREY = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_2_GREY = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_3_GREY = new TextPaint[TEXT_PAINT_COUNT];

    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_1_BLUE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_2_BLUE = new TextPaint[TEXT_PAINT_COUNT];
    public static TextPaint[] TEXT_PAINT_FILL_AND_STROKE_3_BLUE = new TextPaint[TEXT_PAINT_COUNT];


    public static TextPaint TEXT_PAINT_FILL_30_BLACK = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public static TextPaint TEXT_PAINT_FILL_DYNAMIC = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    public static TextPaint TEXT_PAINT_FILL_CATEGORY = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public static TextPaint TEXT_PAINT_FILL_HASHTAG = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public static TextPaint SHADER_TEXT_PAINT_FILL_AND_STROKE_2 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    public static TextPaint SHADER_TEXT_PAINT_FILL_AND_STROKE_STORY_TAG = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    //public static int hashtagColor = 0xffdddddd;

    public static Paint PAINT_SHADER = new Paint(Paint.ANTI_ALIAS_FLAG);
    //----------------------------------------------------------------------

    public static GradientDrawable gradientDrawable;

    //----------------------------------------------------------------------

    public static Toolbar toolbar = new Toolbar();
    public static Setting setting = new Setting();

    public static void createMahdiResources(Context context)
    {

        //------------------------------------------------
        colors[0] = 0xfff77d4c;
        colors[1] = 0xfffb8743;
        colors[2] = 0xfff67c4d;
        colors[3] = 0xffd03a8e;
        colors[4] = 0xffa842aa;
        colors[5] = 0xff9e44b0;
        colors[6] = 0xffae41a6;
        colors[7] = 0xffae41a6;
        colors[8] = 0xffd93988;
        colors[9] = 0xfff77d4c;

        Shader shader = new SweepGradient(dp(18.5f), dp(18.5f), colors, null);
        PAINT_RING.setShader(shader);

        PAINT_STORY_DASH.setColor(0xffffffff);
        PAINT_STORY_DASH.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        PAINT_STORY_DASH.setPathEffect(new DashPathEffect(new float[]{dp(3), dp(1)}, 0));
        PAINT_STORY_DASH.setStrokeWidth(dp(12));
        PAINT_STORY_DASH.setStyle(Paint.Style.STROKE);

        //------------------------------------------------
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xffcccccc, 0xffffffff, Themp.color_profile_background});
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientRadius(dp(450));
        //------------------------------------------------

        PAINT_DYNAMIC.setStyle(Paint.Style.FILL);

        PAINT_PROFILE_BACKGROUND.setColor(0xffffffff);
        PAINT_PROFILE_BACKGROUND.setStyle(Paint.Style.FILL);

        PAINT_WHITE.setColor(0xffffffff);
        PAINT_WHITE.setStyle(Paint.Style.FILL);

        PAINT_BLACK.setColor(0xFF000000);
        PAINT_BLACK.setStyle(Paint.Style.FILL);

        PAINT_DARK.setColor(0xff222222);
        PAINT_DARK.setStyle(Paint.Style.FILL);


        PAINT_GRAY.setColor(0xff888888);
        PAINT_GRAY.setStyle(Paint.Style.FILL);

        PAINT_PRESS_WHITE.setColor(0x11FFFFFF);
        PAINT_PRESS_WHITE.setStyle(Paint.Style.FILL);

        PAINT_PRESS_BLACK.setColor(0x11000000);
        PAINT_PRESS_BLACK.setStyle(Paint.Style.FILL);

        PAINT_PRESS_STRONG_BLACK.setColor(0x44000000);
        PAINT_PRESS_STRONG_BLACK.setStyle(Paint.Style.FILL);

        PAINT_SELECT_BLUE.setColor(0x0f00B0FF);
        PAINT_SELECT_BLUE.setStyle(Paint.Style.FILL);

        PAINT_LINE.setColor(0xFF888888);
        PAINT_LINE.setStyle(Paint.Style.STROKE);
        PAINT_LINE.setStrokeWidth(1);

        PAINT_PROFILE_CIRCLE.setStyle(Paint.Style.STROKE);
        PAINT_PROFILE_CIRCLE.setColor(0xff888888);
        PAINT_PROFILE_CIRCLE.setStrokeWidth(1);

        PAINT_GREEN.setStyle(Paint.Style.FILL);
        PAINT_GREEN.setColor(0xff00C853);

        PAINT_BLUE.setStyle(Paint.Style.FILL);
        PAINT_BLUE.setColor(0xff2196F3);


        PAINT_FFAAAAAA.setColor(0xffAAAAAA);
        PAINT_FFAAAAAA.setStyle(Paint.Style.FILL);

        PAINT_FFBBBBBB.setColor(0xffBBBBBB);
        PAINT_FFBBBBBB.setStyle(Paint.Style.FILL);

        PAINT_FFCCCCCC.setColor(0xffCCCCCC);
        PAINT_FFCCCCCC.setStyle(Paint.Style.FILL);

        PAINT_FFDDDDDD.setColor(0xffdddddd);
        PAINT_FFDDDDDD.setStyle(Paint.Style.FILL);

        PAINT_FFEEEEEE.setColor(0XFFEEEEEE);
        PAINT_FFEEEEEE.setStyle(Paint.Style.FILL);

        PAINT_FFF8F8F8.setColor(0XFFF8f8f8);
        PAINT_FFF8F8F8.setStyle(Paint.Style.FILL);

        PAINT_FF444444.setColor(0xff444444);
        PAINT_FF444444.setStyle(Paint.Style.FILL);

        PAINT_FF333333.setColor(0xff333333);
        PAINT_FF333333.setStyle(Paint.Style.FILL);

        PAINT_FF222222.setColor(0xff222222);
        PAINT_FF222222.setStyle(Paint.Style.FILL);

        PAINT_OPACITY_88FFFFFF.setColor(0x88FFFFFF);
        PAINT_OPACITY_88FFFFFF.setStyle(Paint.Style.FILL);

        PAINT_OPACITY_AA000000.setColor(0xaa000000);
        PAINT_OPACITY_AA000000.setStyle(Paint.Style.FILL);

        PAINT_OPACITY_CC222222.setColor(0xCC222222);
        PAINT_OPACITY_CC222222.setStyle(Paint.Style.FILL);

        PAINT_OPACITY_DD222222.setColor(0xDD222222);
        PAINT_OPACITY_DD222222.setStyle(Paint.Style.FILL);

                PAINT_OPACITY_DD222222.setColor(0xDD222222);
        PAINT_OPACITY_DD222222.setStyle(Paint.Style.FILL);

        PAINT_OPACITY_EE222222.setColor(0xEE222222);
        PAINT_OPACITY_EE222222.setStyle(Paint.Style.FILL);



        PAINT_OPACITY_66000000.setColor(0x66000000);
        PAINT_OPACITY_66000000.setStyle(Paint.Style.FILL);

        PAINT_OPACITY_22000000.setColor(0x22000000);
        PAINT_OPACITY_22000000.setStyle(Paint.Style.FILL);

        //------------------------------------------------

        STROKE_PAINT_PX_WHITE.setColor(0xffffffff);
        STROKE_PAINT_PX_WHITE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_WHITE.setStrokeWidth(1);

        STROKE_PAINT_PX_BLACK.setColor(0xff000000);
        STROKE_PAINT_PX_BLACK.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_BLACK.setStrokeWidth(1);

        STROKE_PAINT_PX_GREY.setColor(0xff888888);
        STROKE_PAINT_PX_GREY.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_GREY.setStrokeWidth(1);

        STROKE_PAINT_PX_BLUE.setColor(0xff00C853);
        STROKE_PAINT_PX_BLUE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_BLUE.setStrokeWidth(1);

        STROKE_PAINT_PX_FFAAAAAA.setColor(0xffAAAAAA);
        STROKE_PAINT_PX_FFAAAAAA.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_FFAAAAAA.setStrokeWidth(1);

        STROKE_PAINT_PX_FFBBBBBB.setColor(0xffBBBBBB);
        STROKE_PAINT_PX_FFBBBBBB.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_FFBBBBBB.setStrokeWidth(1);

        STROKE_PAINT_PX_FFCCCCCC.setColor(0xffCCCCCC);
        STROKE_PAINT_PX_FFCCCCCC.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_FFCCCCCC.setStrokeWidth(1);

        STROKE_PAINT_PX_FFDDDDDD.setColor(0xffDDDDDD);
        STROKE_PAINT_PX_FFDDDDDD.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_PX_FFDDDDDD.setStrokeWidth(1);

        STROKE_PAINT_1DP_WHITE.setColor(0xFFFFFFFF);
        STROKE_PAINT_1DP_WHITE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_WHITE.setStrokeWidth(one);

        STROKE_PAINT_2DP_WHITE.setColor(0xFFFFFFFF);
        STROKE_PAINT_2DP_WHITE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_2DP_WHITE.setStrokeWidth(dp(2));

        STROKE_PAINT_1_5DP_FFAAAAAA.setColor(0xFFAAAAAA);
        STROKE_PAINT_1_5DP_FFAAAAAA.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1_5DP_FFAAAAAA.setStrokeWidth(dp(1.5f));

        STROKE_PAINT_1_5DP_FFBBBBBB.setColor(0xFFBBBBBB);
        STROKE_PAINT_1_5DP_FFBBBBBB.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1_5DP_FFBBBBBB.setStrokeWidth(dp(1.5f));

        STROKE_PAINT_1_5DP_FFCCCCCC.setColor(0xFFCCCCCC);
        STROKE_PAINT_1_5DP_FFCCCCCC.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1_5DP_FFCCCCCC.setStrokeWidth(dp(1.5f));

        STROKE_PAINT_1_5DP_FFDDDDDD.setColor(0xFFDDDDDD);
        STROKE_PAINT_1_5DP_FFDDDDDD.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1_5DP_FFDDDDDD.setStrokeWidth(dp(1.5f));

        STROKE_PAINT_1_5DP_FFEEEEEE.setColor(0xFFEEEEEE);
        STROKE_PAINT_1_5DP_FFEEEEEE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1_5DP_FFEEEEEE.setStrokeWidth(dp(1.5f));

        STROKE_PAINT_1_5DP_FFEFEFEF.setColor(0xFFEFEFEF);
        STROKE_PAINT_1_5DP_FFEFEFEF.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1_5DP_FFEFEFEF.setStrokeWidth(dp(1.5f));

        STROKE_PAINT_4DP_WHITE.setColor(0xFFFFFFFF);
        STROKE_PAINT_4DP_WHITE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_4DP_WHITE.setStrokeWidth(dp(4f));

        STROKE_PAINT_4DP_RED.setColor(0xFFFF0000);
        STROKE_PAINT_4DP_RED.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_4DP_RED.setStrokeWidth(dp(4f));
        STROKE_PAINT_4DP_RED.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        STROKE_PAINT_4DP_RED.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too

        STROKE_PAINT_8DP_WHITE.setColor(0xFFFFFFFF);
        STROKE_PAINT_8DP_WHITE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_8DP_WHITE.setStrokeWidth(dp(8));

        STROKE_PAINT_18DP_WHITE.setColor(0xFFFFFFFF);
        STROKE_PAINT_18DP_WHITE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_18DP_WHITE.setStrokeWidth(dp(15));

        STROKE_PAINT_1DP_BLACK.setColor(0xff000000);
        STROKE_PAINT_1DP_BLACK.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_BLACK.setStrokeWidth(one);

        STROKE_PAINT_2DP_BLACK.setColor(0xff000000);
        STROKE_PAINT_2DP_BLACK.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_2DP_BLACK.setStrokeWidth(dp(2));

        STROKE_PAINT_1DP_GREY.setColor(0xFF888888);
        STROKE_PAINT_1DP_GREY.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_GREY.setStrokeWidth(one);

        STROKE_PAINT_1DP_BLUE.setColor(0xFF2196F3);
        STROKE_PAINT_1DP_BLUE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_BLUE.setStrokeWidth(one);

        STROKE_PAINT_2DP_BLUE.setColor(0xFF2196F3);
        STROKE_PAINT_2DP_BLUE.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_2DP_BLUE.setStrokeWidth(dp(2));

        STROKE_PAINT_1DP_FFAAAAAA.setColor(0xFFAAAAAA);
        STROKE_PAINT_1DP_FFAAAAAA.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_FFAAAAAA.setStrokeWidth(one);

        STROKE_PAINT_1DP_FFBBBBBB.setColor(0xFFBBBBBB);
        STROKE_PAINT_1DP_FFBBBBBB.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_FFBBBBBB.setStrokeWidth(one);

        STROKE_PAINT_1DP_FFCCCCCC.setColor(0xFFCCCCCC);
        STROKE_PAINT_1DP_FFCCCCCC.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_FFCCCCCC.setStrokeWidth(one);

        STROKE_PAINT_1DP_FFDDDDDD.setColor(0xFFDDDDDD);
        STROKE_PAINT_1DP_FFDDDDDD.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_1DP_FFDDDDDD.setStrokeWidth(one);

        STROKE_PAINT_OPACITY_1DP_88FFFFFF.setStyle(Paint.Style.STROKE);
        STROKE_PAINT_OPACITY_1DP_88FFFFFF.setStrokeWidth(one);

        STROKE_PAINT_OPACITY_2DP_AAFFFFFF.setStrokeWidth(dp(2));
        STROKE_PAINT_OPACITY_2DP_AAFFFFFF.setColor(0xAAFFFFFF);
        STROKE_PAINT_OPACITY_2DP_AAFFFFFF.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        STROKE_PAINT_OPACITY_2DP_AAFFFFFF.setStrokeCap(Paint.Cap.ROUND);

        STROKE_PAINT_OPACITY_3DP_DDFFFFFF.setStrokeWidth(dp(3));
        STROKE_PAINT_OPACITY_3DP_DDFFFFFF.setColor(0xDDFFFFFF);

        //------------------------------------------------
        ICON_PAINT_SRC_IN_WHITE.setColorFilter(new PorterDuffColorFilter(0xffffffff, PorterDuff.Mode.SRC_IN));
        ICON_PAINT_MULTIPLY_WHITE.setColorFilter(new PorterDuffColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY));

        ICON_PAINT_SRC_IN_LIGHT.setColorFilter(new PorterDuffColorFilter(0xffdddddd, PorterDuff.Mode.SRC_IN));

        ICON_PAINT_SRC_IN_GREY.setColorFilter(new PorterDuffColorFilter(0xff888888, PorterDuff.Mode.SRC_IN));
        ICON_PAINT_MULTIPLY_GREY.setColorFilter(new PorterDuffColorFilter(0xff888888, PorterDuff.Mode.MULTIPLY));

        ICON_PAINT_SRC_IN_BLACK.setColorFilter(new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.SRC_IN));
        ICON_PAINT_MULTIPLY_BLACK.setColorFilter(new PorterDuffColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY));

        ICON_PAINT_SRC_IN_BLUE.setColorFilter(new PorterDuffColorFilter(0xff2196F3, PorterDuff.Mode.SRC_IN));
        ICON_PAINT_MULTIPLY_BLUE.setColorFilter(new PorterDuffColorFilter(0xff2196F3, PorterDuff.Mode.MULTIPLY));

        ICON_PAINT_SRC_IN_RED.setColorFilter(new PorterDuffColorFilter(0xffed4956, PorterDuff.Mode.SRC_IN));
        ICON_PAINT_MULTIPLY_RED.setColorFilter(new PorterDuffColorFilter(0xffed4956, PorterDuff.Mode.MULTIPLY));
        //------------------------------------------------

        for (int i = 0; i < TEXT_PAINT_COUNT; i++) {
            TEXT_PAINT_FILL_WHITE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_WHITE[i].setColor(0xFFFFFFFF);
            TEXT_PAINT_FILL_WHITE[i].setTextSize(dp(10 + i));

            TEXT_PAINT_FILL_LIGHT[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_LIGHT[i].setColor(0xFFaaaaaa);
            TEXT_PAINT_FILL_LIGHT[i].setTextSize(dp(10 + i));


            TEXT_PAINT_FILL_BLACK[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_BLACK[i].setColor(0xff000000);
            TEXT_PAINT_FILL_BLACK[i].setTextSize(dp(10 + i));

            TEXT_PAINT_FILL_GREY[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_GREY[i].setColor(0xff888888);
            TEXT_PAINT_FILL_GREY[i].setTextSize(dp(10 + i));

            TEXT_PAINT_FILL_BLUE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_BLUE[i].setColor(0xFF2196F3);
            TEXT_PAINT_FILL_BLUE[i].setTextSize(dp(10 + i));

            TEXT_PAINT_FILL_FFBBBBBB[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_FFBBBBBB[i].setColor(0xffBBBBBB);
            TEXT_PAINT_FILL_FFBBBBBB[i].setTextSize(dp(10 + i));

            TEXT_PAINT_FILL_BOLD_WHITE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_BOLD_WHITE[i].setColor(0xFFFFFFFF);
            TEXT_PAINT_FILL_BOLD_WHITE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_BOLD_WHITE[i].setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            TEXT_PAINT_FILL_BOLD_BLACK[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_BOLD_BLACK[i].setColor(0xff000000);
            TEXT_PAINT_FILL_BOLD_BLACK[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_BOLD_BLACK[i].setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        }

        for (int i = 0; i < TEXT_PAINT_COUNT; i++) {

            TEXT_PAINT_FILL_AND_STROKE_1_WHITE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_1_WHITE[i].setColor(0xFFFFFFFF);
            TEXT_PAINT_FILL_AND_STROKE_1_WHITE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_1_WHITE[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_1_WHITE[i].setStrokeWidth(0.1f);

            TEXT_PAINT_FILL_AND_STROKE_2_WHITE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_2_WHITE[i].setColor(0xFFFFFFFF);
            TEXT_PAINT_FILL_AND_STROKE_2_WHITE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_2_WHITE[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_2_WHITE[i].setStrokeWidth(0.2f);

            TEXT_PAINT_FILL_AND_STROKE_3_WHITE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_3_WHITE[i].setColor(0xFFFFFFFF);
            TEXT_PAINT_FILL_AND_STROKE_3_WHITE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_3_WHITE[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_3_WHITE[i].setStrokeWidth(0.3f);

            TEXT_PAINT_FILL_AND_STROKE_1_BLACK[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_1_BLACK[i].setColor(0xff000000);
            TEXT_PAINT_FILL_AND_STROKE_1_BLACK[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_1_BLACK[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_1_BLACK[i].setStrokeWidth(0.1f);

            TEXT_PAINT_FILL_AND_STROKE_2_BLACK[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_2_BLACK[i].setColor(0xff000000);
            TEXT_PAINT_FILL_AND_STROKE_2_BLACK[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_2_BLACK[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_2_BLACK[i].setStrokeWidth(0.2f);

            TEXT_PAINT_FILL_AND_STROKE_3_BLACK[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_3_BLACK[i].setColor(0xff000000);
            TEXT_PAINT_FILL_AND_STROKE_3_BLACK[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_3_BLACK[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_3_BLACK[i].setStrokeWidth(0.3f);

            TEXT_PAINT_FILL_AND_STROKE_4_BLACK[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_4_BLACK[i].setColor(0xff000000);
            TEXT_PAINT_FILL_AND_STROKE_4_BLACK[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_4_BLACK[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_4_BLACK[i].setStrokeWidth(1.9f);

            TEXT_PAINT_FILL_AND_STROKE_1_GREY[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_1_GREY[i].setColor(0xff888888);
            TEXT_PAINT_FILL_AND_STROKE_1_GREY[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_1_GREY[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_1_GREY[i].setStrokeWidth(0.1f);

            TEXT_PAINT_FILL_AND_STROKE_2_GREY[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_2_GREY[i].setColor(0xff888888);
            TEXT_PAINT_FILL_AND_STROKE_2_GREY[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_2_GREY[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_2_GREY[i].setStrokeWidth(0.2f);

            TEXT_PAINT_FILL_AND_STROKE_3_GREY[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_3_GREY[i].setColor(0xff888888);
            TEXT_PAINT_FILL_AND_STROKE_3_GREY[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_3_GREY[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_3_GREY[i].setStrokeWidth(0.3f);

            TEXT_PAINT_FILL_AND_STROKE_1_BLUE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_1_BLUE[i].setColor(0xFF2196F3);
            TEXT_PAINT_FILL_AND_STROKE_1_BLUE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_1_BLUE[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_1_BLUE[i].setStrokeWidth(0.1f);

            TEXT_PAINT_FILL_AND_STROKE_2_BLUE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_2_BLUE[i].setColor(0xFF2196F3);
            TEXT_PAINT_FILL_AND_STROKE_2_BLUE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_2_BLUE[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_2_BLUE[i].setStrokeWidth(0.2f);

            TEXT_PAINT_FILL_AND_STROKE_3_BLUE[i] = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            TEXT_PAINT_FILL_AND_STROKE_3_BLUE[i].setColor(0xFF2196F3);
            TEXT_PAINT_FILL_AND_STROKE_3_BLUE[i].setTextSize(dp(10 + i));
            TEXT_PAINT_FILL_AND_STROKE_3_BLUE[i].setStyle(Paint.Style.FILL_AND_STROKE);
            TEXT_PAINT_FILL_AND_STROKE_3_BLUE[i].setStrokeWidth(0.3f);

        }

        TEXT_PAINT_FILL_30_BLACK.setColor(0xFF000000);
        TEXT_PAINT_FILL_30_BLACK.setTextSize(dp(30));

        TEXT_PAINT_FILL_CATEGORY.setColor(0xFF000000);
        TEXT_PAINT_FILL_CATEGORY.setTextSize(dp(14));
        TEXT_PAINT_FILL_CATEGORY.setStyle(Paint.Style.FILL_AND_STROKE);
        TEXT_PAINT_FILL_CATEGORY.setStrokeWidth(0.6f);

        TEXT_PAINT_FILL_HASHTAG.setColor(0xff2F41a5);
        TEXT_PAINT_FILL_HASHTAG.setTextSize(dp(16));


        SHADER_TEXT_PAINT_FILL_AND_STROKE_2.setTextSize(dp(20));
        SHADER_TEXT_PAINT_FILL_AND_STROKE_2.setStyle(Paint.Style.FILL_AND_STROKE);
        SHADER_TEXT_PAINT_FILL_AND_STROKE_2.setStrokeWidth(dp(0.2f));
        SHADER_TEXT_PAINT_FILL_AND_STROKE_2.setColor(0xffD81B60);


        SHADER_TEXT_PAINT_FILL_AND_STROKE_STORY_TAG.setTextSize(dp(37));
        SHADER_TEXT_PAINT_FILL_AND_STROKE_STORY_TAG.setTypeface(App.avenyTypeface);

        igtv_seriesPaint.setColor(0x66000000);

        LIST_PAINT_SHADER.setShader(new LinearGradient(AndroidUtilities.width / 2, 0, AndroidUtilities.width / 2, dp(50), new int[]{0xdd000000, 0x00000000}, new float[]{0, 1}, Shader.TileMode.CLAMP));

        IGTV_PAINT_SHADER.setShader(new LinearGradient(w / 4, 0, w / 4, dp(80), new int[]{0x88000000, 0x00000000}, new float[]{0, 1}, Shader.TileMode.CLAMP));
        IGTV_PAINT_SHADER_BOTTOM.setShader(new LinearGradient(w / 4, 0, w / 4, dp(80), new int[]{0x00000000, 0x88000000}, new float[]{0, 1}, Shader.TileMode.CLAMP));

        PAINT_SHADER.setShader(new LinearGradient(w / 2, -dp(40), w / 2, dp(80), new int[]{0x88ffffff, 0x88000000}, new float[]{0, 1}, Shader.TileMode.CLAMP));
        //------------------------------------------------


        drawableCamera[0] = context.getResources().getDrawable(R.drawable.overlay_camera_swap);
        drawableCamera[1] = context.getResources().getDrawable(R.drawable.cgo);

        drawableToolbar[0] = context.getResources().getDrawable(R.drawable.header_shadow);
        drawableToolbar[1] = context.getResources().getDrawable(R.drawable.header_shadow_reverse);

        bitmapGallery = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bm3);
        drawableGallery[0] = context.getResources().getDrawable(R.drawable.play_button);


        drawableEmptyState[0] = context.getResources().getDrawable(R.drawable.empty_state_plus);
        drawableEmptyState[1] = context.getResources().getDrawable(R.drawable.empty_state_gallery);
        drawableEmptyState[2] = context.getResources().getDrawable(R.drawable.nux_main_feed_empty_icon);
        drawableEmptyState[3] = context.getResources().getDrawable(R.drawable.empty_state_plus);
        drawableEmptyState[4] = context.getResources().getDrawable(R.drawable.empty_state_polygon);
        drawableEmptyState[5] = context.getResources().getDrawable(R.drawable.empty_state_save);
        drawableEmptyState[6] = context.getResources().getDrawable(R.drawable.empty_state_private);
        drawableEmptyState[7] = context.getResources().getDrawable(R.drawable.empty_state_follow);
        drawableEmptyState[8] = context.getResources().getDrawable(R.drawable.empty_state_follow);

        drawableEmptyState[9] = context.getResources().getDrawable(R.drawable.empty_state_cart);
        drawableEmptyState[10] = context.getResources().getDrawable(R.drawable.empty_state_heart);
        drawableEmptyState[11] = context.getResources().getDrawable(R.drawable.empty_state_location);
        drawableEmptyState[12] = context.getResources().getDrawable(R.drawable.empty_state_follow);

        bitmapEmptyProfile = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.profile_anonymous_user);
        drawableEmptyProfile = context.getResources().getDrawable(R.drawable.profile_anonymous_user);

        bitmapCheck = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.nav_check);
        bitmapMoreMenuToolBar = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ced);

        bitmapDelete = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.trash_72);
        bitmapCloseCircle = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_close_white_24dp);
        bitmapHand = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_hand_default);
        bitmapLocation_56 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.location_56);
        bitmapTick = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.checkbig);
        bitmapClose24 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_close_white_24dp);
        bitmapVerifiedProfile = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bqp);

        bitmapFilterOff = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.filter_16);
        bitmapPlus2 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.plus_36);

        shop95 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dmj);
        shop72 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cb7);
        shop36 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cs3);
        favourit36 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.favourite);
        bitmapNext = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.next);
        bitmapIGTVClose = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bc);
        bitmapClose24 = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_close_white_24dp);

        bitmapChat[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c_y);
        bitmapChat[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.xe);
        bitmapChat[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cd9);
        bitmapChat[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.gw);
        bitmapChat[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dp7);
        bitmapChat[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c74);
        bitmapChat[6] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bil);

        bitmapChat[7] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.pe);
        bitmapChat[8] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cxee);
        bitmapChat[9] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.igtv);

        bitmapCloseFriend = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bm7);

        bitmapArchive = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b7o);
        bitmapYourActivity = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cqo);
        bitmapSetting = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cbu);
        bitmapFace = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ce_);

        notifications[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.notification_like_icon);
        notifications[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.notification_comment_icon);
        notifications[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.notification_people_icon);
        notifications[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.notification_forward_icon);

        notificationCursor = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.notification_coursor);







        mainToolBarIcons[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.home_b);
        mainToolBarIcons[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cast);
        mainToolBarIcons[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.movies);
        mainToolBarIcons[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.music);


        mainToolBarSelectedIcons[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.home_blue);
        mainToolBarSelectedIcons[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cast_blue);
        mainToolBarSelectedIcons[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.movies_blue);
        mainToolBarSelectedIcons[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.music_blue);












        bitmapsHandListGrid[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.grid);
        bitmapsHandListGrid[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b7f);
        bitmapsHandListGrid[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.shop72);
        bitmapsHandListGrid[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.igtv_category);




        postCell[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bdx);
        postCell[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.r1);
        postCell[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ufi_direct_share_bold);
        postCell[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ufi_comment_bold);
        postCell[6] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.browse);
        postCell[7] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bmg);

        postCell[8] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dhx);
        postCell[9] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.pbookmark);

        postCell[11] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bxc);

        postCell[13] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bp6);
        postCell[14] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bul);

        postCell[15] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.brv);//sound
        postCell[16] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ki);//mute
        postCell[17] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b9h);//igtv
        postCell[18] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dhs);//tag

        postCell[19] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dav);//tag_corner_top
        postCell[20] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cte);//tag_corner_bottom

        bitmapTicketGalleryToolBar[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tq);

        searchFilters[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_sort_by_alpha_white_24dp);
        searchFilters[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.slider_72);
        searchFilters[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.location_72);
        searchFilters[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.place_x);
        searchFilters[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_nothing_24dp);

        drawableMap[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.list_60);
        drawableMap[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ios_search);
        drawableMap[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.filter);
        drawableMap[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.map_51);
        drawableMap[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.gps_me);
        drawableMap[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.livepin);
        drawableMap[7] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_hand_default);
        drawableMap[8] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_close_white_24dp);
        drawableMap[9] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_draw_active);


        map_polygon_toolbar[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ic_hand_default);
        map_polygon_toolbar[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.list_60);

        iconFilters[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_adjust_straighten_whiteout);
        iconFilters[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_brightness_whiteout);
        iconFilters[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_contrast_whiteout);
        iconFilters[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_structure_whiteout);
        iconFilters[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_warmth_whiteout);
        iconFilters[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_saturation_whiteout);
        iconFilters[6] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_tint_whiteout);
        iconFilters[7] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_fade_whiteout);
        iconFilters[8] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_highlights_whiteout);
        iconFilters[9] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_shadows_whiteout);
        iconFilters[10] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_vignette_whiteout);
        iconFilters[11] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_sharpen_whiteout);
        iconFilters[12] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_tilt_whiteout);
        iconFilters[13] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.tool_tilt_whiteout);

        homegramSmallCell[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.filled_grid_album_icon);
        homegramSmallCell[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.grid_camera_icon);
        homegramSmallCell[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.grid_camera_icon);
        homegramSmallCell[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.igtv5);
        //homegramSmallCell[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.igtv3);

        videoPlayer[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b4v);
        videoPlayer[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bn1);

        commentIcons[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.like_comment);
        commentIcons[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.like_comment_on);

        camera[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cgo);
        camera[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bx5);
        camera[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ccc);
        camera[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bwm);
        camera[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cgo);
        camera[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.notification_icon);

        direct[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.browser_more_button);

        story[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cxv);
        story[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bom);

        storyCreate[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.biq);
        storyCreate[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.storylink);
        storyCreate[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bg5);
        storyCreate[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bij);
        storyCreate[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.paint);
        storyCreate[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cyk);

        storyCreate[6] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ccw);
        storyCreate[7] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cy8);

        storyPaint[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.o7);
        storyPaint[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable._9);
        storyPaint[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ce3);
        storyPaint[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.cup);
        storyPaint[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c74);
        storyPaint[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.ckm);
        storyPaint[6] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.mb);
        storyPaint[7] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.crv);
        storyPaint[8] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bkt);
        storyPaint[9] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.beo);

        storyPaint[10] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b22);//undo

        storyText[0] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable._y);
        storyText[1] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.b25);
        storyText[2] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.dh_);

        storyText[3] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.c8x);
        storyText[4] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bx7);
        storyText[5] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.bx8);

        storyText[6] = BitmapFactory.decodeResource(App.context.getResources(), R.drawable.adp7);
    }


    //Utility functions----------------------------------------------------------
    public static int dp(float value)
    {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(AndroidUtilities.mDensity * value);
    }
    //-------------------------------------------------------------------------------


}
