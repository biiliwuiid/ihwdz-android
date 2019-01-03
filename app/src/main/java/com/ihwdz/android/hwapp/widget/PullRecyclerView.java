package com.ihwdz.android.hwapp.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ihwdz.android.hwapp.R;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/09
 * desc :
 * version: 1.0
 * </pre>
 */
public class PullRecyclerView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener  {



    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout footerView;
    private OnPullRefreshListener listener;
    //是否正在刷新
    private boolean isRefreshing = false;
    //是否正在加载
    private boolean isLoading = false;
    //是否有更多数据
    private boolean hasMore = false;

    public PullRecyclerView(Context context) {
        this(context, null);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initListener();
        init();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.pull_recycler_layout, this, true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        footerView = (LinearLayout) findViewById(R.id.footerView);

    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new PullableScroll());
        //防止滚动的时候，滑动View
        recyclerView.setOnTouchListener(this);
    }

    private void init() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        //隐藏垂直滚动条
        recyclerView.setVerticalScrollBarEnabled(true);
        //item高度固定时，设置该选项提高性能
        recyclerView.setHasFixedSize(true);
        //设置item动画效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        recyclerView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置监听下拉或上拉的事件
     *
     * @param listener
     */
    public void setOnPullRefreshListener(OnPullRefreshListener listener) {
        this.listener = listener;
    }

    /**
     * 设置是否有更多数据
     *
     * @param hasMore
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        recyclerView.scrollToPosition(0);
    }

    /**
     * 正在刷新
     */
    @Override
    public void onRefresh() {
        isRefreshing = true;
        if (listener != null) {
            listener.onRefresh();
        }

    }

    /**
     * 设置是否允许下拉
     *
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    /**
     * 滚动时判断能否能刷新
     *
     * @return
     */
    private boolean isRefreshEnable() {
        return !isRefreshing && !isLoading;
    }

    /**
     * 正在加载更多
     */
    public void doLoadMore() {
        if (!isLoading && hasMore && !isRefreshing) {
            footerView.setVisibility(View.VISIBLE);
            isLoading = true;
            //禁止下拉
            setRefreshEnable(false);
            if (listener != null) {
                listener.onLoadMore();
            }
        }
    }

    /**
     * 刷新或加载完成
     */
    public void refreshOrLoadComplete() {
        isRefreshing = false;
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        footerView.setVisibility(View.GONE);
        //允许下拉
        this.setEnabled(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return isRefreshing || isLoading;
    }


    public interface OnPullRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh();

        /**
         * 加载操作
         */
        void onLoadMore();
    }

    /**
     * 监听RecycleView滑动底部或顶部
     */
    class PullableScroll extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = 0;
            int firstVisibleItem = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int totalItemCount = layoutManager.getItemCount();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
                // this array into an array of position and then take the maximum value that is the last show the position value
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItem = findMax(lastPositions);
                firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
            }

            pullRefreshEnable(firstVisibleItem, totalItemCount);
            if (isSlideToBottom(recyclerView)) {
                loadMore(dx, dy, lastVisibleItem, totalItemCount);
            }

        }

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

    /**
     * 判断是否滑动到底部
     *
     * @param recyclerView
     * @return
     */
    public boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return false;
        }
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange()) {
            return true;
        }
        return false;
    }

    private void loadMore(int dx, int dy, int lastVisibleItem, int totalItemCount) {
        //滚动到底部时且有更多数据能够上拉加载
        if (lastVisibleItem >= totalItemCount - 1 && (dx > 0 || dy > 0)) {
            doLoadMore();
        }
    }

    private void pullRefreshEnable(int firstVisibleItem, int totalItemCount) {
        //滚动到顶部时能够下拉刷新
        if (firstVisibleItem == 0 || totalItemCount == 0) {
            if (isRefreshEnable()) {
                //允许下拉
                setRefreshEnable(true);
            }
        } else {
            //禁止下拉
            setRefreshEnable(false);
        }
    }
}
