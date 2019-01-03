package com.ihwdz.android.hwapp.widget;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.ihwdz.android.hwapp.ui.home.deal.CardFlowAdapter;

import java.lang.ref.WeakReference;

/**
 * <pre>
 * author : Duan
 * time :   2018/07/26
 * desc :    use AutoRollRecyclerView instead it now.
 * version: 1.0
 * </pre>
 */
public class AutoPollRecyclerView extends RecyclerView{

    static String TAG = "AutoPollRecyclerView";
//    private static final long TIME_AUTO_POLL = 10;  //5000
//    AutoPollTask autoPollTask;

    private static final long TIME_AUTO_SCROLL = 1;
    AutoScrollTask autoScrollTask;

    private Handler mHandler = new Handler();

    private int mItemWith = 360;    // item width
    static int mPerScrollWith = 2;
    private int scrolled = 0;       // 已经滑动的距离
    static boolean startScroll = true;

    private int mDelayMillis_restart = 5000;

    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int window_heigth = dm.heightPixels;
        int window_width = dm.widthPixels;
        mItemWith = window_width/2;

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
//       scrolled += dx;
        if (scrolled >= mItemWith){   // LinearLayoutManager.getChildAt(0).getMeasuredWidth();
            Log.d(TAG, "onScrolled : start again: " + scrolled );
            startScroll = false;
            scrolled = 0;
            mHandler.postDelayed(scrollStartRunnable, mDelayMillis_restart);
        }
    }

    /**
     * 设置自动滚动
     */

    Runnable scrollStartRunnable = new Runnable() {
        @Override
        public void run() {
            startScroll = true;
        }
    };

//    /**
//     * 设置自动滚动
//     */
//    static class AutoPollTask implements Runnable {
//        private final WeakReference<AutoPollRecyclerView> mReference;
//        //使用弱引用持有外部类引用->防止内存泄漏
//        public AutoPollTask(AutoPollRecyclerView reference) {
//            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
//        }
//        @Override
//        public void run() {
//            AutoPollRecyclerView recyclerView = mReference.get();
//
//
//            if (recyclerView != null) {
//                LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
//                int with = manager.getChildAt(0).getMeasuredWidth();
////                Log.d("run", "getChildAt(0).getMeasuredWidth() : " + with);
//
////                recyclerView.scrollBy(with, 0);
//
//                recyclerView.scrollBy(1, 0);
//
////                CardFlowAdapter adapter = (CardFlowAdapter) recyclerView.getAdapter();
////                if (adapter == null){
////                    Log.e("CardFlowAdapter", "CardFlowAdapter == null");
////                    return;}
////                int index = ((CardFlowAdapter)recyclerView.getAdapter()).getCurrentPosition();
////                manager.scrollToPositionWithOffset(index,0);
//////                recyclerView.smoothScrollToPosition(index);
//////                recyclerView.scrollToPosition(index);
////
//                recyclerView.postDelayed(recyclerView.autoPollTask, recyclerView.TIME_AUTO_POLL);
//
//            }
//        }
//    }


    /**
     * 设置自动滚动
     */
    static class AutoScrollTask implements Runnable {
        private final WeakReference<AutoPollRecyclerView> mReference;
        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoScrollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }
        @Override
        public void run() {
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView != null) {
                if (startScroll){
                Log.d(TAG, "run: scrolling:" );
                    recyclerView.scrollBy(mPerScrollWith ,0);
                }else {
                Log.e(TAG, "run: stop:" );
                    recyclerView.scrollBy(0,0);
                }
                recyclerView.postDelayed(recyclerView.autoScrollTask, recyclerView.TIME_AUTO_SCROLL);
            }
        }
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
//        mHandler.postDelayed(scrollRunnable,mDelayMillis);

//        autoPollTask = new AutoPollTask(this);
//        postDelayed(autoPollTask,TIME_AUTO_POLL);

        autoScrollTask = new AutoScrollTask(this);
        postDelayed(autoScrollTask,TIME_AUTO_SCROLL);
    }

    public void stop(){
//        mHandler.removeCallbacks(scrollRunnable);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        switch (e.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                if (running)
//                    stop();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_OUTSIDE:
//                if (canRun)
//                    start();
//                break;
//        }
//        return super.onTouchEvent(e);
//    }
}
