package com.ihwdz.android.hwapp.widget;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/01
 * desc :
 * version: 1.0
 * </pre>
 */
public class AutoRollRecyclerView extends RecyclerView {

    String TAG = "AutoRollRecyclerView";

    AutoRollRecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    private Handler mHandler = new Handler();

    private int mItemWith = 360;
    private int mPerScrollWith = 10;
    private int scrolled = 0;       // 已经滑动的距离
    boolean startScroll = false;

    private int mDelayMillis = 1;
    private int mDelayMillis_restart = 5000;

    private boolean isRunning = false;


    Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (startScroll){
//                Log.d(TAG, "run: scrolling:" );
                mRecyclerView.scrollBy(mPerScrollWith,0);
            }else {
//                Log.e(TAG, "run: stop:" );
                mRecyclerView.scrollBy(0,0);
            }
            mHandler.postDelayed(scrollRunnable, mDelayMillis);
        }
    };

    Runnable scrollStartRunnable = new Runnable() {
        @Override
        public void run() {
            startScroll = true;
        }
    };

    public AutoRollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRecyclerView = this;
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int window_heigth = dm.heightPixels;
        int window_width = dm.widthPixels;
        mItemWith = window_width/2;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        /**
         *  SCROLL_STATE_IDLE 0: The RecyclerView is not currently scrolling.
         *
         *  SCROLL_STATE_DRAGGING 1: The RecyclerView is currently being dragged by outside input such as user touch input.
         *
         *  SCROLL_STATE_SETTLING 2: The RecyclerView is currently animating to a final position while not under outside control.
         */
        Log.d(TAG, "onScrollStateChanged : state:" + state);
        if (state == SCROLL_STATE_DRAGGING){
            if (this.computeVerticalScrollOffset() > 0) {
                // 有滚动距离，说明可以加载更多，解决了 items 不能充满 RecyclerView 的问题及滑动方向问题
                boolean isBottom = false ;
                isBottom = this.computeVerticalScrollExtent()
                        + this.computeVerticalScrollOffset()
                        == this.computeVerticalScrollRange() ;
                if (isBottom){

                }
            }

        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        scrolled += dx;
        if (scrolled >= mItemWith){     // LinearLayoutManager.getChildAt(0).getMeasuredWidth();
//            Log.d(TAG, "onScrolled : start again: " + scrolled );
            startScroll = false;
            scrolled = 0;
            mHandler.postDelayed(scrollStartRunnable, mDelayMillis_restart);
        }
    }



    //开启:如果正在运行,先停止->再开启
    public void start() {
        scrolled = 0;
        if (!isRunning){
            isRunning = true;
            startScroll = true;
            mHandler.postDelayed(scrollRunnable,mDelayMillis);
        }else {
            Log.d(TAG, "start : is running");
        }


    }

    public void stop(){
        isRunning = false;
        mHandler.removeCallbacks(scrollRunnable);
    }

}
