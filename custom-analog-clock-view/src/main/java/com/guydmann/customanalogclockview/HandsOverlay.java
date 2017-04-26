package com.guydmann.customanalogclockview;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;


import java.util.Calendar;


public class HandsOverlay implements DialOverlay {

    private final boolean mUseLargeFace;
    private float mHourRot;
    private float mMinRot;
    private float mSecRot;
    private boolean mShowSeconds;
    private int mIncrement;
    private String timeText;

    static float HALF_PI = (float) (Math.PI/2);
    static float TWO_PI = (float) (Math.PI*2);

    public HandsOverlay(Context context, boolean useLargeFace) {
        final Resources r = context.getResources();

        mUseLargeFace = useLargeFace;

    }

    public HandsOverlay() {
        mUseLargeFace = false;
    }

    /*
    public HandsOverlay(Context context, int hourHandRes, int minuteHandRes) {
        final Resources r = context.getResources();

        mUseLargeFace = false;

        mHour = r.getDrawable(hourHandRes);
        mMinute = r.getDrawable(minuteHandRes);
    }
    */

    public static float getHourHandAngle(int h, int m) {
        return ((12 + h) / 12.0f * 360) % 360 + (m / 60.0f) * 360 / 12.0f;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDraw(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                       boolean sizeChanged) {
        updateHands(calendar);
        canvas.save();
        mIncrement = w/10;
        drawHours(canvas, cX, cY, w-(mIncrement*5), h-(mIncrement*5));
        drawMinutes(canvas, cX, cY, w-(mIncrement*3), h-(mIncrement*3));
        drawSeconds(canvas, cX, cY, w-mIncrement, h-mIncrement);
        //drawDayOfWeek(canvas, cX, cY, w, h, calendar, sizeChanged);
        //drawDayOfMonth(canvas, cX, cY, w, h, calendar, sizeChanged);
        //drawMonth(canvas, cX, cY, w, h, calendar, sizeChanged);
        drawTimeText(canvas, cX, cY, w, h);
        canvas.restore();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawHours(Canvas canvas, int cX, int cY, int w, int h) {
        drawArc(canvas, cX, cY, w, h, mHourRot);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawMinutes(Canvas canvas, int cX, int cY, int w, int h) {
        drawArc(canvas, cX, cY, w, h, mMinRot);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawSeconds(Canvas canvas, int cX, int cY, int w, int h) {
        drawArc(canvas, cX, cY, w, h, mSecRot);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawArc(Canvas canvas, int cX, int cY, int w, int h, float rotation) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth((float) (mIncrement*1.25));
        paint.setColor(Color.HSVToColor(new float[]{rotation, 1, 1}));

        final RectF oval = new RectF();
        int r = Math.min(w, h);
        oval.set(cX - r, cY - r, cX + r, cY + r);
        Path arcPath = new Path();
        arcPath.arcTo(oval, -90, rotation , true);

        canvas.drawPath(arcPath, paint);


    }

    private void drawTimeText(Canvas canvas, int cX, int cY, int w, int h) {

        int r = Math.min(w, h);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(timeText, 0, timeText.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * (r / 2) / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);


        canvas.drawText(timeText, cX-(r / 4), cY, paint);
    }



    public void setShowSeconds(boolean showSeconds) {
        mShowSeconds = showSeconds;
    }


    private void updateHands(Calendar calendar) {

        /*
        final int mn = calendar.get(Calendar.MONTH);
        final int dom = calendar.get(Calendar.DAY_OF_MONTH);
        final int dow = calendar.get(Calendar.DAY_OF_WEEK);
        */
        final int ampm = calendar.get(Calendar.AM_PM);
        final int h = calendar.get(Calendar.HOUR_OF_DAY);
        final int m = calendar.get(Calendar.MINUTE);
        final int s = calendar.get(Calendar.SECOND);

        mSecRot = (s  * 6);

        mMinRot = (m * 6) + (mShowSeconds ? (s / 60.0f) : 0) ;
        mHourRot = (((h / 12.0f) * 360) % 360) + (m / 2.0f);

        timeText = String.format("%d:%02d:%02d %s", h%12, m, s, (ampm == 1? "PM" : "AM" ) );

    }

}
