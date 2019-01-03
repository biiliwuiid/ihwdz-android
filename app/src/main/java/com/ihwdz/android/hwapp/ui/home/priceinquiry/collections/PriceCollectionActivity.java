package com.ihwdz.android.hwapp.ui.home.priceinquiry.collections;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.OnPriceItemClickListener;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.HwAppDialog;
import com.ihwdz.android.hwapp.widget.ToggleRadioButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 *  查价格 - 我的收藏
 */
public class PriceCollectionActivity extends BaseActivity implements PriceCollectionContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "PriceCollectionActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.radio_date) ToggleRadioButton dateRadio;   // 日期

    @BindView(R.id.refresher) SwipeRefreshLayout refresher;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.empty_tv) TextView emptyTv;

    private String collectionRemindStr;
    private Handler handler = new Handler();
    private boolean isScrollDown; //是否是向下滑动

    @Inject PriceCollectionContract.Presenter mPresenter;

    public static void startPriceCollectionActivity(Context context) {
        Intent intent = new Intent(context, PriceCollectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_price_collection;
    }

    @Override
    public void initView() {
        collectionRemindStr = getResources().getString(R.string.collection_dialog);
        initToolBar();
        initRecyclerView();
    }

    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        String todayStr = DateUtils.getDateTodayString();
        mPresenter.setCurrentDate(todayStr);
        //mPresenter.getPriceCollectedData();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.collection_title));
    }

    // price recycler view
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mPresenter.getAdapter().setOnPriceItemClickListener(new OnPriceItemClickListener() {
            @Override
            public void onPriceStarClicked(int collectionType, String breed, String spec, String brand, String area, boolean collect) {
                // 取消收藏
                showDialog(collectionType, breed, spec, brand, area);
//                if (collect){
//                    // 收藏
//                    // mPresenter.doCollect(collectionType, breed, spec, brand, area);
//                }else {
//                    // 取消收藏
//                    showDialog(collectionType, breed, spec, brand, area);
//                }
            }
        });
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);

    }

    // 日期
    @OnCheckedChanged(R.id.radio_date)
    public void onDateCheckedChanged(){
        if (dateRadio.isChecked()){
            dateRadio.getPaint().setFakeBoldText(true);
            mPresenter.selectDate();      // 日期选择器
        }else {
            dateRadio.getPaint().setFakeBoldText(false);
        }
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
    public void showEmptyView() {
        emptyTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        emptyTv.setVisibility(View.GONE);
    }

    @Override
    public void showDialog(final int collectionType, final String breed, final String spec, final String brand, final String area) {
        // 弹框选择Breeds
        HwAppDialog dialog = new HwAppDialog
                .Builder(this)
                .setTitle(getResources().getString(R.string.title_dialog_deal))
                .setInsideContentView(mPresenter.getCancelCollectView(collectionRemindStr))
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消收藏
                        mPresenter.doCollectCancel(collectionType, breed, spec, brand, area);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void updateDateTitle(String title) {
        dateRadio.setText(title);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    /**
     *  RecyclerView.OnScrollListener1
     */

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
                    if (mPresenter.getAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_PREPARE){
                        // 此处调用 加载更多回调接口 的回调方法
                        // set footer : loading
                        mPresenter.getAdapter().setIsLoadMore(true);  // add footer
                        mPresenter.getAdapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING); // set footer : loading


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //load more
                                if (mPresenter.getCurrentPageNum() != mPresenter.getCurrentPageCount()){
                                    mPresenter.getPriceCollectedData();
                                }else {
                                    mPresenter.getAdapter().setIsLoadMore(false);
                                }


                            }
                        }, 2000);
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

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }
}
