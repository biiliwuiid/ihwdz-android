package com.ihwdz.android.hwapp.ui.me.collections;

import android.content.Context;
import android.content.Intent;
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


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class CollectionsActivity extends BaseActivity implements CollectionContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CollectionsActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.empty)LinearLayout emptyView;
    @BindView(R.id.grey_line)View boundaryView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refresher) SwipeRefreshLayout refresher;

    CollectionAdapter mAdapter = null;
    private Handler handler = new Handler();
    private boolean isScrollDown;   //是否是向下滑动

    @Inject CollectionContract.Presenter mPresenter;

    public static void startCollectionsActivity(Context context) {
        Intent intent = new Intent(context, CollectionsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_collections;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecyclerView();
    }

    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        mPresenter.getCollectionData();
    }


    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_my_collection));
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);
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
    public void showPromptMessage(String string) {
        Toast.makeText(getBaseContext(), string,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
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
                                mPresenter.getCollectionData();

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
