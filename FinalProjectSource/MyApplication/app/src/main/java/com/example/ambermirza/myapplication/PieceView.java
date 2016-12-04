package com.example.ambermirza.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Daneil on 12/2/2016.
 */

public class PieceView extends View {

    private final Paint mPainter = new Paint();
    FrameLayout frameRef;
    float[] start;
    float[] pos;
    float[] size;
    boolean fixed = false;
    Bitmap img;

    PieceView(Context context, float[] start_, float[] size_, Bitmap img_, FrameLayout r) {
        super(context);
        start = start_;
        img = img_;
        size = size_;
        setLayoutParams(new ViewGroup.LayoutParams((int) size[0], (int) size[1]));
        frameRef = r;
        movePieceTo(start);

    }

    public void movePieceTo(float[] p) {
        this.animate()
                .x(p[0])
                .y(p[1])
                .setDuration(0)
                .start();
        pos = p.clone();
    }

    protected synchronized void onDraw(Canvas canvas) {

        // save the canvas
        canvas.save();
        canvas.drawBitmap(img, 0, 0, mPainter);

        if (!fixed) {
            Paint strokePaint = new Paint();
            strokePaint.setARGB(255, 255, 0, 0);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(2);
            Rect r = canvas.getClipBounds();
            Rect outline = new Rect(1, 1, r.right - 1, r.bottom - 1);
            canvas.drawRect(outline, strokePaint);
        }
        // Restore the canvas
        canvas.restore();
    }


    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }
}