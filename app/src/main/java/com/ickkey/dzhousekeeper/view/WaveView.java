package com.ickkey.dzhousekeeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangbin11 on 2017/8/3.
 */

public class WaveView extends View implements Runnable{
    private Handler mHandler;
    // ****************波形线算法中用到
    private long c = 0L;

    // **********动画的状态************是否开始动画
    private boolean mStarted = false;
    private final float f = 0.033F;
    private int mAlpha = 41; // **********控件View透明度
    private final int mColor = Color.parseColor("#FFFFFF");
    private float mAmplitude = 50.0F; // 振幅
    private final Paint mPaint = new Paint();
    private Paint mPaintLater = new Paint();
    private Paint mPaintLater1 = new Paint();

    // 水位的高度百分比
    private float mWateLevel = 0.0F;

    // 水位的高度
    private float mAnimationHeignt;

    // ****************下面三个变量只在changwater()中用到,且都是水位的百分比.**************************
    private float changLevel;// 需要改变水位的量
    private float mStep;// 每次水位变化的量
    protected float percent;// 要达到的目的水位

    public WaveView(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    public WaveView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    /**
     * 开启水波动的动画效果
     */
    public void startWave() {
        if (!mStarted) {
            this.c = 0L;
            mStarted = true;
            this.mHandler.sendEmptyMessage(0);
        }
    }

    public void stopWave() {
        mStarted = false;
    }

    private void init(Context context) {
        mPaint.setStrokeWidth(1.0F);
        mPaint.setColor(mColor);
        mPaint.setAlpha(mAlpha);

        mPaintLater.setStrokeWidth(1.0F);
        mPaintLater.setColor(mColor);
        mPaintLater.setAlpha(41);

        mPaintLater1.setStrokeWidth(1.0f);
        mPaintLater1.setColor(mColor);
        mPaintLater1.setAlpha(80);

        /**
         * 在startWave()中开启动画,调用Handler(),且在Handler中不断调用自己更新画面.实现动画效果.
         */
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    invalidate();
                    if (mStarted) {
                        mHandler.sendEmptyMessageDelayed(0, 60L);
                    }
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int width = getWidth();
        int height = getHeight();
        if ((!mStarted) || (width == 0) || (height == 0)) {

            // 动画没启动的效果
            // canvas.drawRect(0.0F, height / 2, width, height, mPaint);
            canvas.drawRect(0.0F, height, width, height, mPaint);
            return;
        }
        if (this.c >= 8388607L) {
            this.c = 0L;
        }
        this.c = (1L + this.c);
        mAnimationHeignt = height * (1.0F - mWateLevel);
        int top = (int) (mAnimationHeignt + mAmplitude);
        /*
         * mPath.reset(); mPath.addCircle(mScreenWidth / 2, mScreenWidth / 2,
         * mScreenWidth / 2, Path.Direction.CCW); canvas.clipPath(mPath,
         * Region.Op.REPLACE);
         */
        canvas.drawRect(0.0F, top, width, height, mPaint);

        int n = (int) (mAnimationHeignt - this.mAmplitude
                * Math.sin(Math.PI * (2.0F * (0.0F + this.c * width * this.f))
                / width));

        int startX = 0;
        int stopX = 0;
        while (stopX < width) {
            int startY = (int) (mAnimationHeignt - mAmplitude
                    * Math.sin(Math.PI
                    * (2.0F * (stopX + this.c * width * this.f))
                    / width));
            int mStartY = (int) (mAnimationHeignt + mAmplitude
                    * Math.sin(Math.PI
                    * (2.0F * (stopX + this.c * width * this.f))
                    / width + 5));

            int mStartY1 = (int) (mAnimationHeignt + mAmplitude
                    * Math.sin(Math.PI
                    * (2.0F * (stopX + this.c * width * this.f))
                    / width + 20));

            // canvas.drawLine(startX, n, stopX, startY, mPaint);
            canvas.drawLine(stopX, startY, stopX, top, mPaint);

            canvas.drawLine(stopX, mStartY, stopX, height, mPaintLater);

            canvas.drawLine(stopX, mStartY1, stopX, top, mPaintLater1);

            int i4 = stopX + 1;
            startX = stopX;
            stopX = i4;
            n = startY;
        }
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // HardwareAccUtils.setLayerTypeAsSoftware(this);
    }

    public void setAmplitude(float amplitued) {
        mAmplitude = amplitued;
    }

    public void setWaterAlpha(float alpha) {
        this.mAlpha = ((int) (255.0F * alpha));
        mPaint.setAlpha(this.mAlpha);
    }

    public void setWaterLevel(float paramFloat) {
        mWateLevel = paramFloat;
    }

    /**
     * 完成水位变化的动画效果.
     *
     * @param percentAge 要水位高度的百分比 取值0.0~1.0
     * @param duration 完成动画的时间
     */
    public void changWater(float percentAge, int duration) {
        if (mWateLevel != 0) {
            mWateLevel = percentAge;
            postInvalidate();
            return;
        }
        changLevel = percentAge - mWateLevel;
        mStep = changLevel / (float) duration;
        percent = percentAge;

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (changLevel > 0 && mWateLevel < percent || changLevel < 0
                && mWateLevel > percent) {
            mWateLevel += mStep;
            try {
                Thread.sleep(1);
                postInvalidate();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 定位水位在指定位置
     *
     * @param percentAge 要水位高度的百分比 取值0.0~1.0
     */
    public void changWater(float percentAge) {
        mWateLevel = percentAge;
        postInvalidate();
    }

}