package com.ihwdz.android.hwapp.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;


/**
 * <pre>
 * author : Duan
 * time : 2018/07/31
 * desc :
 * version: 1.0
 * </pre>
 */
public class MarqueeRecyclerView extends RecyclerView{

    String TAG = "MarqueeRecyclerView";
    MarqueeRecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private Handler mHandler = new Handler();

    private int mItemWith = 340;
    private int scrolled = 0;       // 已经滑动的距离
    boolean startScroll = true;
    private int mDelayMillis_restart = 2000;

    private boolean isRunning = false;

    private int mPerScrollWith = 2;
    private int mDelayMillis = 1;


    public MarqueeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRecyclerView = this;
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        Log.d(TAG, "onScrollStateChanged : state:" + state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        // dx > 0 : 表示正在向左滑动，
        // dx = 0 : 表示停止，
        // dx < 0 : 表示正在向右滑动，

//        scrolled += dx;
//        if (scrolled >= mItemWith){   // LinearLayoutManager.getChildAt(0).getMeasuredWidth();
//            Log.d(TAG, "onScrolled : start again: " + scrolled );
//            startScroll = false;
//            scrolled = 0;
//            mHandler.postDelayed(scrollStartRunnable, mDelayMillis_restart);
//        }
    }



    /**
     * 设置自动滚动
     */
    Runnable scrollRunnable = new Runnable() {

        @Override
        public void run() {
            mRecyclerView.scrollBy(mPerScrollWith, 0);
            mHandler.postDelayed(scrollRunnable, mDelayMillis);
        }
    };


    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (!isRunning){
            mHandler.postDelayed(scrollRunnable,mDelayMillis);
            isRunning = true;
        }

    }

    public void stop(){
        mHandler.removeCallbacks(scrollRunnable);
        isRunning = false;
    }

}
