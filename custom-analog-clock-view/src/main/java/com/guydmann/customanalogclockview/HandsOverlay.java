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
    private float mHourRot;
    private float mMinRot;
    private float mSecRot;
    private boolean mShowSeconds;
    private int mWidthIncrement;
    private int mHeightIncrement;
    private String timeText;

    public HandsOverlay(boolean showSeconds) {
        mShowSeconds = showSeconds;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDraw(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                       boolean sizeChanged) {
        updateHands(calendar);
        canvas.save();
        mWidthIncrement = w/10;
        mHeightIncrement = h/10;
        int hourWidth = w-(mWidthIncrement*5);
        int hourHeight = h-(mHeightIncrement*5);
        int minuteWidth = w-(mWidthIncrement*3);
        int minuteHeight = h-(mHeightIncrement*3);
        int secondWidth = w-(mWidthIncrement);
        int secondHeight = h-(mHeightIncrement);

        drawHours(canvas, cX, cY, hourWidth , hourHeight);
        drawMinutes(canvas, cX, cY, minuteWidth, minuteHeight);
        if (mShowSeconds) {
            drawSeconds(canvas, cX, cY, secondWidth, secondHeight);
        }
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
        paint.setStrokeWidth((float) (mWidthIncrement*1.25));
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
        final int ampm = calendar.get(Calendar.AM_PM);
        final int h = calendar.get(Calendar.HOUR_OF_DAY);
        final int m = calendar.get(Calendar.MINUTE);
        final int s = calendar.get(Calendar.SECOND);
        final int ms = calendar.get(Calendar.MILLISECOND);

        if (mShowSeconds) {
            if (s > 0) {
                mSecRot = (s * 6);
            } else {
                mSecRot = ((1000 - ms) / 1000.0f) * 360;
            }
        }

        if (m == 0 && s == 0) {
            mMinRot = ((1000 - ms)/1000.0f)*360;
        } else {
            mMinRot = (m * 6) + (mShowSeconds ? (s / 60.0f) : 0) ;
        }

        if (h == 0 && m == 0 && s == 0) {
            mHourRot = ((1000 - ms)/1000.0f)*360;
        } else {
            mHourRot = (((h / 12.0f) * 360) % 360) + (m / 2.0f);
        }


        timeText = String.format("%d:%02d:%02d %s", h%12, m, s, (ampm == 1? "PM" : "AM" ) );

    }

}
