package com.ihwdz.android.hwapp.ui.home.more;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.HW24H_MODE;
import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.REC_ABC_MODE;
import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.REC_MODE;

public class MoreActivity extends BaseActivity implements MoreContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener{

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.refresher) SwipeRefreshLayout refresher;

    @BindView(R.id.recyclerView2) RecyclerView recyclerView;

    @BindView(R.id.title_bottom_news) RelativeLayout contentBar; // 内容分类标题
    @BindView(R.id.view)View boundaryView;          // 分割条
    @BindView(R.id.tv1)TextView tradeABCTv;         // 行业ＡＢＣ
    @BindView(R.id.tv2)TextView tradeResearchTv;    // 行业研究

    @Inject MoreContract.Presenter mPresenter;

    private Handler handler = new Handler();
    private boolean isScrollDown;   //是否是向下滑动

    static final String MODE = "mode";
    int mode = 0;

    static String TAG = "MoreActivity";

    @Override
    public int getContentView() {
        return R.layout.activity_more;
    }

    @Override
    public void initView() {
        Log.i(TAG, "=================================== initView ===================");
        if (getIntent()!= null){
            mode = getIntent().getIntExtra(MODE,0);
            mPresenter.setCurrentMode(mode);
            Log.i(TAG, "===== getIntent ====  type: " + mode);
        }
        initToolBar();
    }

    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
        tradeABCTv.setOnTouchListener(this);
        tradeResearchTv.setOnTouchListener(this);
    }

    @Override
    public void initData() {
        switch (mode){
            case HW24H_MODE:
                mPresenter.getHw24hData();
                break;
            case REC_ABC_MODE:
                mPresenter.getRecommendABCData();
                break;
            case REC_MODE:
                mPresenter.getRecommendData();
                break;
            default:
                break;
        }
    }

    public static void startMoreActivity(Context context, int type) {
        Log.i(TAG, "=================================== startMoreActivity =================== TYPE: "+type);
        Intent intent = new Intent(context, MoreActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MODE, type);
        context.startActivity(intent);
    }

    @Override
    public void showWaitingRing() {
        refresher.post(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(true);
            }
        });
        refresher.setEnabled(false);
    }

    @Override
    public void hideWaitingRing() {
        refresher.post(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(false);
            }
        });
        refresher.setEnabled(true);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            switch (v.getId()){
                case R.id.tv1: // 行业ＡＢＣ
                    mPresenter.setCurrentMode(REC_ABC_MODE);
                    break;
                case R.id.tv2: // 行业研究
                    mPresenter.setCurrentMode(REC_MODE);
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, " =================  onRefresh: " );
        mPresenter.refreshData();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }


    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        if (mode == HW24H_MODE){  // HW24H
            title.setText(getResources().getString(R.string.title_hw24h));
            contentBar.setVisibility(View.GONE);
            boundaryView.setVisibility(View.GONE);
        }else {                   // RECOMMEND
            title.setText(getResources().getString(R.string.title_re));
            contentBar.setVisibility(View.VISIBLE);
            boundaryView.setVisibility(View.VISIBLE);
        }
        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (mPresenter.getCurrentMode() == HW24H_MODE){  // 鸿网２４小时
            recyclerView.setAdapter(mPresenter.getHw24Adapter());
        }else {                                          //　推荐
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(mPresenter.getNewsAdapter());
        }
        recyclerView.addOnScrollListener(mOnScrollListener);
    }


    int distance = 0;
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
//            distance += dy;
//            if (mPresenter.getCurrentMode() != HW24H_MODE){
//                if (distance >= 10){
//                    boundaryView.setVisibility(View.GONE);
//                }else {
//                    boundaryView.setVisibility(View.VISIBLE);
//                }
//            }
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, " ================================================================  onScrollStateChanged ===============: newState: " + newState );
            // SCROLL_STATE_IDLE     : 0
            // SCROLL_STATE_DRAGGING : 1
            // SCROLL_STATE_SETTLING : 2
            if (newState == SCROLL_STATE_IDLE){

                int lastVisibleItem;
                // 获取RecyclerView的LayoutManager
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                // 获取到最后一个可见的item

                if (layoutManager instanceof LinearLayoutManager) {
                    // 如果是 LinearLayoutManager
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    // 如果是 StaggeredGridLayoutManager
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItem = findMax(into);
                } else {
                    // 否则抛出异常
                    throw new RuntimeException("Unsupported LayoutManager used");
                }
                // 获取item的总数
                int totalItemCount = layoutManager.getItemCount();
                /**
                 最后一个可见的item为最后一个item
                 是向下滑动
                 */
                if (lastVisibleItem >= totalItemCount - 1 && isScrollDown) {

                    /**
                     * HW24H more
                     */
                    if (mode == HW24H_MODE){
                        // STATUS_PREPARE
                        if (mPresenter.getHw24Adapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_PREPARE){
                            // 此处调用 加载更多回调接口 的回调方法
                            // set footer : loading
                            mPresenter.getHw24Adapter().setIsLoadMore(true);  // add footer
                            mPresenter.getHw24Adapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING); // set footer : loading

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //load more
                                    mPresenter.getHw24hData();
                                }
                            }, 2000);
                        }

                        // STATUS_EMPTY
                        if (mPresenter.getHw24Adapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_EMPTY){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPresenter.getHw24Adapter().setIsLoadMore(false);
                                }
                            }, 2000);
                        }
                    }
                    else {
                        /**
                         * Recommend more
                         */
                        // STATUS_PREPARE
                        if (mPresenter.getNewsAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_PREPARE){
                            // 此处调用 加载更多回调接口 的回调方法
                            // set footer : loading
                            mPresenter.getNewsAdapter().setIsLoadMore(true);  // add footer
                            mPresenter.getNewsAdapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING); // set footer : loading

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //load more
                                    switch (mPresenter.getCurrentMode()){
                                        case REC_ABC_MODE:
                                            mPresenter.getRecommendABCData();
                                            break;
                                        case REC_MODE:
                                            mPresenter.getRecommendData();
                                            break;
                                        default:
                                            break;
                                    }

                                }
                            }, 2000);
                        }

                        // STATUS_EMPTY
                        if (mPresenter.getNewsAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_EMPTY){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPresenter.getNewsAdapter().setIsLoadMore(false);
                                }
                            }, 2000);
                        }
                    }


                }
            }
        }


    };

    /**
     * 获取数组中的最大值
     *
     * @param lastPositions 需要找到最大值的数组
     * @return 数组中的最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
