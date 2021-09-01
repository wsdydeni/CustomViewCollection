package wsdydeni.widget.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.Nullable;
import wsdydeni.widget.custom.R;

/*
 * Date: 2020/5/5
 * Author: wsdydeni
 * Description: CircleProgressBar
 */
public class CircleProgressBar extends View {

    public static final int BAR_COLOR_DEF = -15102483;

    public static final float BAR_WIDTH_DEF_DIP = 3.0F;

    public static final int CENTER_TEXT_COLOR_DEF = -1;

    public static final int CENTER_TEXT_SIZE_DEF = 14;

    public static final int RIM_COLOR_DEF = 0x0c000000;

    private int mBarColor;

    private Paint mBarPaint;

    private int mBarPosition;

    private float mBarWidth;

    private RectF mCircleBound;

    private boolean mIsShowProgress;

    private int mMax;

    private int mPercentage;

    private int mProgress;

    private int mRimColor;

    private Paint mRimPaint;

    private boolean mShouldUpdateBound;

    private String mText;

    private int mTextColor;

    private Paint mTextPaint;

    private int mTextSize;

    public CircleProgressBar(Context context) {
        super(context,null);
        this.mBarPosition = 0;
        this.mBarPaint = new Paint();
        this.mRimPaint = new Paint();
        this.mTextPaint = new Paint();
        this.mTextSize = 0;
        this.mText = "0%";
        this.mPercentage = 0;
        this.mShouldUpdateBound = false;
        this.mIsShowProgress = false;
    }

    /**
     * 根据当前进度计算位置
     */
    private int getPosByProgress(int i,boolean z) {
        int i2 = z ? 180 : 100;
        int i3 = this.mMax;
        if (i3 <= 0) {
            return 0;
        }
        return i >= i3 ? i2 : (int) (((float) i) / ((float) i3) * ((float) i2));
    }

    private void init() {
        setBound();
        setPaint();
        this.mBarPosition = getPosByProgress(this.mProgress,true);
        this.mPercentage = getPosByProgress(this.mProgress,false);
        this.mText = this.mPercentage + "%";
    }

    private void setBound() {
        if(this.mCircleBound == null) {
            this.mCircleBound = new RectF();
        }
        this.mCircleBound.left = ((float) getPaddingLeft()) + this.mBarWidth;
        this.mCircleBound.top = ((float) getPaddingTop()) + this.mBarWidth;
        this.mCircleBound.right = ((float) (getWidth() - getPaddingRight())) - this.mBarWidth;
        this.mCircleBound.bottom = ((float) (getHeight() - getPaddingBottom())) - this.mBarWidth;
    }

    private void setPaint() {
        if(this.mBarPaint == null) {
            this.mBarPaint= new Paint();
        }
        this.mBarPaint.setColor(this.mBarColor);
        this.mBarPaint.setAntiAlias(true);
        this.mBarPaint.setStyle(Paint.Style.STROKE);
        this.mBarPaint.setStrokeWidth(this.mBarWidth);
        this.mBarPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mBarPaint.setStrokeCap(Paint.Cap.ROUND);
        if(this.mRimPaint == null) {
            this.mRimPaint = new Paint();
        }
        this.mRimPaint.setColor(this.mRimColor);
        this.mRimPaint.setAntiAlias(true);
        this.mRimPaint.setStyle(Paint.Style.STROKE);
        this.mRimPaint.setStrokeWidth(this.mBarWidth);
        if(this.mTextPaint == null) {
            this.mTextPaint = new Paint();
        }
        this.mTextPaint.setTextSize((float) this.mTextSize);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(this.mShouldUpdateBound) {
            setBound();
            this.mShouldUpdateBound = false;
        }
        // 画进度条背景
        canvas.drawArc(this.mCircleBound,360.0f,360.0f,false,this.mRimPaint);
        RectF rectF = this.mCircleBound;
        int i = this.mBarPosition;
        // 画进度条
        canvas.drawArc(rectF,(float) (i + 90),(float) (i * -2),false,this.mBarPaint);
        float descent = ((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f) - this.mTextPaint.descent();
        float measureText = this.mTextPaint.measureText(this.mText) / 2.0f;
        // 画进度文字
        if(this.mIsShowProgress) {
            canvas.drawText(this.mText,((float) (getWidth() / 2)) - measureText,((float) (getHeight() / 2)) + descent,this.mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mShouldUpdateBound = true;
    }

    public int getMax() {
        return Math.max(this.mMax, 0);
    }

    public int getProgress() {
        return Math.max(this.mProgress,0);
    }

    public void setCircleBarColor(int color) {
        if(this.mBarColor != color) {
            this.mBarColor = color;
            this.mBarPaint.setColor(this.mBarColor);
            postInvalidate();
        }
    }

    public void setCircleBarWidth(float f) {
        if (((double) Math.abs(this.mBarWidth - f)) > 1.0E-6d) {
            this.mBarWidth = Math.max(f, 0.0f);
            this.mBarPaint.setStrokeWidth(this.mBarWidth);
            this.mRimPaint.setStrokeWidth(this.mBarWidth);
            this.mShouldUpdateBound = true;
            postInvalidate();
        }
    }

    public void setMax(int i) {
        if(i < 0) {
            i = 0;
        }
        if(i != this.mMax) {
            this.mMax = i;
            if(this.mProgress > i) {
                this.mProgress = i;
            }
            postInvalidate();
        }
    }

    public void setProgress(int i) {
        if(i < 0) {
            i = 0;
        }
        int i2 = this.mMax;
        if(i > i2) {
            i = i2;
        }
        if(i != this.mProgress) {
            this.mProgress = i;
            this.mBarPosition = getPosByProgress(i,true);
            this.mPercentage = getPosByProgress(this.mProgress,false);
            this.mText = this.mPercentage + "%";
            postInvalidate();
        }
    }

    public void setProgressStatus(boolean z) {
        this.mIsShowProgress = z;
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        this.mBarPosition = 0;
        this.mBarPaint = new Paint();
        this.mRimPaint = new Paint();
        this.mTextPaint = new Paint();
        this.mTextSize = 0;
        this.mText = "0%";
        this.mPercentage = 0;
        this.mShouldUpdateBound = false;
        this.mIsShowProgress = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        this.mBarColor = obtainStyledAttributes.getColor(R.styleable.CircleProgressBar_barColor,BAR_COLOR_DEF);
        this.mRimColor = obtainStyledAttributes.getColor(R.styleable.CircleProgressBar_rimColor,RIM_COLOR_DEF);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mBarWidth = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleProgressBar_barWidth,(int) (displayMetrics.density * BAR_WIDTH_DEF_DIP));
        this.mTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleProgressBar_centerTextSize,(int) (displayMetrics.density * CENTER_TEXT_SIZE_DEF));
        this.mTextColor = obtainStyledAttributes.getColor(R.styleable.CircleProgressBar_centerTextColor,CENTER_TEXT_COLOR_DEF);
        this.mIsShowProgress = obtainStyledAttributes.getBoolean(R.styleable.CircleProgressBar_isShowProgress,this.mIsShowProgress);
        setMax(obtainStyledAttributes.getInt(R.styleable.CircleProgressBar_circleMax,0));
        setProgress(obtainStyledAttributes.getInt(R.styleable.CircleProgressBar_circleProgress,0));
        obtainStyledAttributes.recycle();
        init();
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
