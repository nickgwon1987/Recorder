package com.example.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

/**
 * DrawView
 */
public class DrawView extends View {                    // draw the picture of waves
    private byte[] bytes_fft;
    private byte[] bytes_wave;
    private float[] points, pointw;
    private Paint paint = new Paint();
    private Paint paint1 = new Paint();
    private Rect rect_wave = new Rect();
    private Rect rect_fft = new Rect();
    private int range = 48;
    private boolean isfft = false;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bytes_fft = bytes_wave = null;
        paint.setStrokeWidth(3f);                       // set the attributes of paint brush
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);                  // set the brush color
        paint.setStyle(Paint.Style.FILL);

        paint1.setStrokeWidth(10f);                       // set the attributes of paint brush
        paint1.setAntiAlias(true);
        paint1.setColor(Color.RED);                  // set the brush color
        paint1.setStyle(Paint.Style.FILL);
    }
    public DrawView(Context context) {
        super(context);
        bytes_fft = bytes_wave = null;
        paint.setStrokeWidth(3f);                       // set the attributes of paint brush
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);                  // set the brush color
        paint.setStyle(Paint.Style.FILL);

        paint1.setStrokeWidth(10f);                       // set the attributes of paint brush
        paint1.setAntiAlias(true);
        paint1.setColor(Color.RED);                  // set the brush color
        paint1.setStyle(Paint.Style.FILL);
    }

    public void updateVisualizer(byte[] data, boolean flag) {
        isfft = flag;
        if (!isfft) {
            bytes_wave = data;
        }
        else {
            bytes_fft = new byte[data.length / 2 + 1];
            bytes_fft[0] = (byte) Math.abs(data[0]);
            for (int i = 2, j = 1; j < range; ) {
                bytes_fft[j] = (byte) Math.hypot(data[i], data[i + 1]);
                i += 2;
                j++;
            }
        }
        invalidate();                                   // repaint the widget
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bytes_fft == null || bytes_wave == null) return;
        rect_wave.set(0 , 0, getWidth(), getHeight() / 2-60);                  // mark the range of wave graph
        rect_fft.set(0, getHeight() / 2 + 30, getWidth(), getHeight());          // mark the range of fft graph
        if (points == null || points.length < bytes_fft.length * 4) {
            points = new float[bytes_fft.length * 4];
        }
        if (pointw == null || pointw.length < bytes_wave.length * 4) {
            pointw = new float[bytes_wave.length * 4];
        }
            for (int i = 0; i < bytes_wave.length - 1; i++) {                  // draw the wave graph
                float left = rect_wave.width() * i / (bytes_wave.length);
                float top = rect_wave.height()/2 - (byte) (bytes_wave[i + 1] + 128) * rect_wave.height() / 256;
                float right = rect_wave.width() * i / (bytes_wave.length);
                float bottom = rect_wave.height()/2 + (byte) (bytes_wave[i + 1] + 128) * rect_wave.height() / 256;
                //canvas.drawRect(left, top, right, bottom, paint);
                pointw[i * 4] = left;
                pointw[i * 4 + 1] = top;
                pointw[i * 4 + 2] = right;
                pointw[i * 4 + 3] = bottom;

            }
            canvas.drawLines(pointw, paint);
            for (int i = 0; i < range; i ++) {                  // draw the fft graph
                if (bytes_fft[i] < 0) bytes_fft[i] = 127;
                points[i * 4] = points[i * 4 + 2] = rect_fft.width() * i / range;
                points[i * 4 + 1] = rect_fft.bottom;
                points[i * 4 + 3] = rect_fft.bottom - bytes_fft[i] * rect_fft.height() / 128;
            }
            canvas.drawLines(points, paint1);
    }
}