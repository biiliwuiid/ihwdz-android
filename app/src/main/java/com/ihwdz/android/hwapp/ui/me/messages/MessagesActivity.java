package com.ihwdz.android.hwapp.ui.me.messages;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MessagesActivity extends BaseActivity implements MessagesContract.View,  SwipeRefreshLayout.OnRefreshListener{

    String TAG = "MessagesActivity";

    String titleStr = "";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.empty)LinearLayout emptyView;
    @BindView(R.id.grey_line)View boundaryView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refresher) SwipeRefreshLayout refresher;

    private Handler handler = new Handler();
    private boolean isScrollDown;   //是否是向下滑动

    @Inject MessagesContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_messages;
    }

    @Override
    public void initView() {
        initToolbar();
        initRecyclerView();
    }


    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        mPresenter.getMessageData();
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
    public void showPromptMessage(String message) {
        Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        LogUtils.printError(TAG, "showEmptyView");
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        LogUtils.printError(TAG, "hideEmptyView");
        emptyView.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    public void initToolbar(){
        titleStr = getResources().getString(R.string.title_messages_center);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }


    int distance = 0;
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
            distance += dy;
            if (distance >= 10){
                boundaryView.setVisibility(View.GONE);
            }else {
                boundaryView.setVisibility(View.VISIBLE);
            }
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
                     * Recommend more
                     */
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
                                mPresenter.getMessageData();

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
