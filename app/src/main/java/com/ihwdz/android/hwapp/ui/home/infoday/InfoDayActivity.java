package com.ihwdz.android.hwapp.ui.home.infoday;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.ALL_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.DYNAMIC_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.FY_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.HY_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.PRICE_ADJUST_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.SD_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.TT_DATA;

public class InfoDayActivity extends BaseActivity implements InfoDayContract.View, SwipeRefreshLayout.OnRefreshListener,View.OnTouchListener{

    String TAG = "InfoDayActivity";
    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    @BindView(R.id.iv_arrow_left) ImageView leftArrowIv;
    @BindView(R.id.iv_arrow_right) ImageView rightArrowIv;

    @BindView(R.id.title_recycler_view)
    RecyclerView titleRecyclerView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv1)TextView allTv;
    @BindView(R.id.tv2)TextView priceAdjustTv;
    @BindView(R.id.tv3)TextView dynamicTv;

    @Inject InfoDayContract.Presenter mPresenter;

    private Handler handler = new Handler();

    private boolean isScrollDown;  // 是否是向下滑动
    private boolean isScrollLeft;  // 是否是向左滑动

    //String priceTitle = getResources().getString(R.string.price_info);
    private String PRICE_ADJUST = "";
    private String DYNAMIC = "";
    private String FY = "";
    private String SD = "";
    private String HY = "";
    private String TT = "";


    @Override
    public int getContentView() {
        return R.layout.activity_info_day;
    }

    @Override
    public void initView() {
        initStrings();
        initToolBar();
        initTitleRecyclerView();
        // 2018/10/31 判断标题 选择内容 默认加载 每日讯 而不是 news
        updateRecyclerView(false);
    }

    private void initStrings() {
        PRICE_ADJUST = getResources().getString(R.string.price_info);
        DYNAMIC = getResources().getString(R.string.dynamic_info);
        FY = getResources().getString(R.string.title_fy_news);
        SD = getResources().getString(R.string.title_sd_news);
        HY = getResources().getString(R.string.title_hy_news);
        TT = getResources().getString(R.string.title_tt_news);
    }


    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
        allTv.setOnTouchListener(this);
        priceAdjustTv.setOnTouchListener(this);
        dynamicTv.setOnTouchListener(this);

    }

    @Override
    public void initData() {
        mPresenter.refreshData();
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
    public void updateRecyclerView(boolean isNews) {
        if (isNews){
            // news
            initNewsRecyclerView();
        }else {
            // 每日讯
            initRecyclerView();
        }

    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            switch (v.getId()){
                case R.id.tv1:
                    // Log.d(TAG, " ============================= onTouch tv1: " );
                    // Toast.makeText(this, "onTouch tv1",Toast.LENGTH_SHORT).show();
                    mPresenter.setCurrentMode(ALL_DATA);
                    //initAllMode();

                    break;
                case R.id.tv2:
                    // Log.d(TAG, " ============================= onTouch tv2: " );
                    // Toast.makeText(this, "onTouch tv2",Toast.LENGTH_SHORT).show();
                    mPresenter.setCurrentMode(PRICE_ADJUST_DATA);
                    //initPriceAdjustMode();
                    break;
                case R.id.tv3:
                    // Log.d(TAG, " ============================= onTouch tv3: " );
                    // Toast.makeText(this, "onTouch tv3",Toast.LENGTH_SHORT).show();
                    mPresenter.setCurrentMode(DYNAMIC_DATA);
                    //initDynamicMode();
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_info));
    }

    // 标题
    private void initTitleRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        titleRecyclerView.setLayoutManager(layoutManager);
        String[] titles = getResources().getStringArray(R.array.info_title);
        mPresenter.getTitleAdapter().setDataList(Arrays.asList(titles));
        titleRecyclerView.setAdapter(mPresenter.getTitleAdapter());
        titleRecyclerView.addOnScrollListener(mTitleScrollListener);
        mPresenter.getTitleAdapter().addItemTitleCheckListener(new OnItemTitleClickListener() {
            @Override
            public void onItemTitleClick(String title) {
                // 更换数据
                if (TextUtils.equals(PRICE_ADJUST, title)){
                    mPresenter.setCurrentMode(PRICE_ADJUST_DATA);
                }
                else if (TextUtils.equals(DYNAMIC, title)){
                    mPresenter.setCurrentMode(DYNAMIC_DATA);
                }
                else if (TextUtils.equals(FY, title)){
                    mPresenter.setCurrentMode(FY_DATA);
                }
                else if (TextUtils.equals(SD, title)){
                    mPresenter.setCurrentMode(SD_DATA);
                }
                else if (TextUtils.equals(HY, title)){
                    mPresenter.setCurrentMode(HY_DATA);
                }
                else if (TextUtils.equals(TT, title)){
                    mPresenter.setCurrentMode(TT_DATA);
                }


            }
        });

        // init content
        LinearLayoutManager contentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(contentLayoutManager);
    }

    // 每日讯
    private void initRecyclerView() {
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    // news - homepage bottom
    private void initNewsRecyclerView() {
        recyclerView.setAdapter(mPresenter.getNewsAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    RecyclerView.OnScrollListener mTitleScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollLeft = dx > 0;  // true 左滑
            //LogUtils.printInfo(TAG, "isScrollRight: " + isScrollLeft);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int firstVisibleItem, firstCompletely;
            int lastVisibleItem, lastCompletely;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            // (已知 LinearLayoutManager)
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();          // 第一个可以看到的item position
            firstCompletely = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition(); // 第一个可以 -完整- 看到的item position
            LogUtils.printInfo(TAG, "firstVisibleItem: " + firstVisibleItem);
            LogUtils.printInfo(TAG, "firstCompletely: " + firstCompletely);

            lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            lastCompletely = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            LogUtils.printInfo(TAG, "lastVisibleItem: " + lastVisibleItem);
            LogUtils.printInfo(TAG, "lastCompletely: " + lastCompletely);

            // 获取item的总数
            int totalItemCount = layoutManager.getItemCount();
            LogUtils.printInfo(TAG, "totalItemCount: " + totalItemCount);

            /**
             最后一个可见的item为最后一个item
             */
            if (lastCompletely == totalItemCount - 1) {
                LogUtils.printInfo(TAG, "最后一个完整可见的item为最后一个item ");
                rightArrowIv.setVisibility(View.INVISIBLE);
            }else {
                rightArrowIv.setVisibility(View.VISIBLE);
            }
            if (firstCompletely == 0){
                LogUtils.printInfo(TAG, "最开头一个完整可见的item为最开始一个item ");
                leftArrowIv.setVisibility(View.INVISIBLE);
            }else {
                leftArrowIv.setVisibility(View.VISIBLE);
            }

        }
    };


    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, " ================================================================  onScrollStateChanged ===============: newState: " + newState );
            // SCROLL_STATE_IDLE     : 0
            // SCROLL_STATE_DRAGGING : 1
            // SCROLL_STATE_SETTLING : 2
            if (newState == SCROLL_STATE_IDLE){

                /**
                 *  method1
                 *
                 */

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

                    // STATUS_PREPARE
                    if (mPresenter.getIsNewsContent()){
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
                                        case FY_DATA:
                                            mPresenter.getFyNewsData(mPresenter.getCurrentPageNum());
                                            break;
                                        case SD_DATA:
                                            mPresenter.getSdNewsData(mPresenter.getCurrentPageNum());
                                            break;
                                        case HY_DATA:
                                            mPresenter.getHyNewsData(mPresenter.getCurrentPageNum());
                                            break;
                                        case TT_DATA:
                                            mPresenter.getTtNewsData(mPresenter.getCurrentPageNum());
                                            break;
                                        default:
                                            break;
                                    }

                                    //Toast.makeText(getBaseContext(), "mOnScrollListener ", Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);
                        }
                    }else {
                        if (mPresenter.getAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_PREPARE){
                            // 此处调用 加载更多回调接口 的回调方法
                            // set footer : loading
                            mPresenter.getAdapter().setIsLoadMore(true);  // add footer
                            mPresenter.getAdapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING); // set footer : loading

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //load more
                                    switch (mPresenter.getCurrentMode()){
                                        case ALL_DATA:
                                            mPresenter.getAllData();
                                            break;
                                        case PRICE_ADJUST_DATA:
                                            mPresenter.getPriceAdjustInfoData();
                                            break;
                                        case DYNAMIC_DATA:
                                            mPresenter.getDynamicInfoData();
                                            break;
                                        default:
                                            break;
                                    }

                                    //Toast.makeText(getBaseContext(), "mOnScrollListener ", Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);
                        }
                    }


                    // STATUS_EMPTY
                    if (mPresenter.getAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_EMPTY){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getAdapter().setIsLoadMore(false);
                            }
                        }, 2000);
                    }

                }


                /**
                 *  method2
                 */
//                if (recyclerView.computeVerticalScrollOffset() > 0){
//                    boolean isBottom = false;
//                    // method 1
//                    isBottom = recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
//                            == recyclerView.computeVerticalScrollRange() ;
//                    // method 2
//                    // isBottom = !recyclerView.canScrollVertically(1) ;
//                    if (isBottom && isScrollDown
//                            && mPresenter.getAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_PREPARE ){
//                        // add FooterAdapter
////                        mPresenter.getAdapter().addFooter(footerLayout);
//                        mPresenter.getAdapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING);
//                        mPresenter.getAdapter().setIsLoadMore(true);
////                        showLoading();
//                        //mPresenter.getFooterAdapter().notifyDataSetChanged();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //load more
//                                switch (mPresenter.getCurrentMode()){
//                                    case ALL_DATA:
//                                        mPresenter.getAllData();
//                                        break;
//                                    case PRICE_ADJUST_DATA:
//                                        mPresenter.getPriceAdjustInfoData();
//                                        break;
//                                    case DYNAMIC_DATA:
//                                        mPresenter.getDynamicInfoData();
//                                        break;
//                                    default:
//                                        break;
//                                }
//
//                                //Toast.makeText(getBaseContext(), "mOnScrollListener ", Toast.LENGTH_SHORT).show();
//                            }
//                        }, 2000);
//                    }
//                }
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
