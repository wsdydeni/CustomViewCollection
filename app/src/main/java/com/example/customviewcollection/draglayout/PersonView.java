package com.example.customviewcollection.draglayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customviewcollection.until.PixelUtils;

public class PersonView extends View {

    private Paint paint;
    private Paint backgroundPaint;
    private Paint textPaint;

    private boolean isChoosed = false;

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    private RectF rectF = new RectF(0f,0f,200f,100f);

    public PersonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setColor(Color.parseColor("#5AEC29"));
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.parseColor("#E7E7E7"));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(18f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("PersonView", "onLayout " + "left:" + left + ",top:" + top + ",right:" + right + "bottom:" + bottom );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isChoosed) {
            canvas.drawRoundRect(rectF, PixelUtils.Companion.dp2pxF(this.getContext(),5f), PixelUtils.Companion.dp2pxF(this.getContext(),5f),paint);
        }
        canvas.drawRoundRect(rectF, PixelUtils.Companion.dp2pxF(this.getContext(),5f), PixelUtils.Companion.dp2pxF(this.getContext(),5f),backgroundPaint);
        canvas.drawText("你好",0f,0f,textPaint);
    }

    public PersonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PersonView(Context context) {
        this(context,null);
    }

}
