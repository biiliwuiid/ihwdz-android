package com.ihwdz.android.hwapp.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.adapter.BaseDelegateAdapter;
import com.ihwdz.android.hwapp.base.adapter.SubAdapter;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.home.deal.CardFlowAdapter;
import com.ihwdz.android.hwapp.ui.home.index.IndexActivity;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoDayActivity;
import com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialActivity;
import com.ihwdz.android.hwapp.ui.home.moodindex.MoodIndexActivity;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.AutoRollRecyclerView;
import com.ihwdz.android.hwapp.widget.HwAppDialog;
import com.ihwdz.android.hwapp.widget.MarqueeRecyclerView;
import com.yc.cn.ycbannerlib.BannerView;
import com.yc.cn.ycbannerlib.banner.util.SizeUtil;


import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.CURRENT_NEWS;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HAS_BOTTOM;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HAS_MORE;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HY_NEWS;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.INDUSTRY_FOCUSED;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.MARKET_COMMENT;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.NO_MORE;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.PageNum;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements HomeFragmentContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    String TAG = "HomeFragment";
    @BindView(R.id.tv_title) TextView title; // 首页
    @BindView(R.id.toolbarLayout)
    LinearLayout toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    @BindView(R.id.back_to_top)
    ImageView backToTop;

    private HomeFragmentContract.Presenter mPresenter = new HomeFragmentPresenter(this);
    private MainActivity activity;
    private BannerView mBanner;
    private List<DelegateAdapter.Adapter> mAdapters;

    private CardFlowAdapter mDealAdapter;
    int distance = 0;
    private int distance_top = 1080;  // 上滑一定距离显示“TOP BUTTON”　否则隐藏
    private int distance_title = 64;  // 上滑一定距离显示“首页”　否则隐藏
    private int distance_bottom_news = 800;
    private int lastVisibleItem = 0;
    private int mPageNum = 1;
    private Handler handler = new Handler();
    private boolean isScrollDown;              //是否是向下滑动

    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
        mPresenter.start();
    }

    // onCreateView
    @Override
    public int getContentView() {
        return R.layout.base_recycler_view;
    }

    // onViewCreated()
    @Override
    public void initView() {
        //mPresenter.start();
        title.setText(getResources().getString(R.string.title_tab0));
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }

    // onViewCreated
    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    // onActivityCreated()
    @Override
    public void initData() {
        if (activity.getCurrentFragment() == 0){
            mPresenter.getHomePageData();
            mPresenter.getDealData();
            mPresenter.getBreedsData();
            mPresenter.getHomeFlushData();
            mPresenter.getHomePageNews(PageNum, PageSize);
        }

    }

    @Override
    public void initLoginSuccess() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBanner != null) {
            mBanner.resume();
        }
    }

    @Override
    public void onPause() {
        distance = 0;
        super.onPause();
        if (mBanner != null) {
           mBanner.pause();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (activity != null) {
            activity = null;
        }
    }


    private void initRecyclerView() {

        DelegateAdapter delegateAdapter = mPresenter.initRecyclerView(recyclerView);

        // 把轮播器添加到集合
        BaseDelegateAdapter bannerAdapter = mPresenter.initBannerAdapter();
        mAdapters.add(bannerAdapter);

        // 初始化九宫格 5个菜单
        BaseDelegateAdapter menuAdapter = mPresenter.initGvMenu();
        mAdapters.add(menuAdapter);

        // 初始化走马灯 情绪指数
        BaseDelegateAdapter marqueeAdapter = mPresenter.initMarqueeView();
        mAdapters.add(marqueeAdapter);

        // 初始化  deal card 标题
        BaseDelegateAdapter titleDealAdapter = mPresenter.initTitleDeal("+关注"); // mPresenter.getTodayDate() date -> "+关注"
        mAdapters.add(titleDealAdapter);

        // 初始化 Deal
        BaseDelegateAdapter dealListAdapter = mPresenter.initDealList();
        mAdapters.add(dealListAdapter);

        // 初始化 Hw24h 标题
        // BaseDelegateAdapter titleHw24Adapter = mPresenter.initTitleHw24();
        // mAdapters.add(titleHw24Adapter);

        // 初始化 hw24
        // BaseDelegateAdapter hw24hListAdapter = mPresenter.initHw24hList();
        // mAdapters.add(hw24hListAdapter);

        // 初始化 Recommend 标题
        // BaseDelegateAdapter titleRecommendAdapter = mPresenter.initTitleRecommend();
        // mAdapters.add(titleRecommendAdapter);

        // 初始化 Recommend
        // BaseDelegateAdapter recommendListAdapter = mPresenter.initRecommendList();
        // mAdapters.add(recommendListAdapter);

        // 初始化 BottomNews Title
        BaseDelegateAdapter bottomNewsTitleAdapter = mPresenter.initTitleBottomNews();
        mAdapters.add(bottomNewsTitleAdapter);

        // 初始化 BottomNews
        SubAdapter newsAdapter = mPresenter.initBottomCurrentNewsList();
        mAdapters.add(newsAdapter);

        // 初始化 Footer
        BaseDelegateAdapter footerAdapter = mPresenter.initFooter();
        mAdapters.add(footerAdapter);

        // 设置适配器
        delegateAdapter.setAdapters(mAdapters);

        recyclerView.requestLayout();
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
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.setAlpha(alpha);
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }


    // 点击按钮回到顶部
    @OnClick(R.id.back_to_top)
    public void onBackToTopClicked(){
        layoutManager.scrollToPosition(0);
        distance = 0;
        // hideBackTopIcon();
    }

    @Override
    public void showBackTopIcon() {
        backToTop.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBackTopIcon() {
        backToTop.setVisibility(View.GONE);
    }

    @Override
    public void refreshBottomNews() {
        mPageNum = 1;
    }


    /**
     * 初始化轮播图
     */
    @Override
    public void initBanner(BannerView mBanner) {
        this.mBanner = mBanner;
        mBanner.setHintGravity(1);         //指示器比重
        mBanner.setAnimationDuration(1000);
        mBanner.setPlayDelay(8000);
        mBanner.setHintPadding(0,0,0, SizeUtil.dip2px(activity,10));
//        mBanner.setAdapter(new BaseBannerPagerAdapter(activity, arrayList));
        mBanner.setAdapter(mPresenter.getBannerAdapter());
    }

    /**
     * 查价格;每日讯;看指数;找客户;采原料
     * @param position
     */
    @Override
    public void setGridClick(int position) {
        switch (position){
            case 0:
                // 查价格
                Intent intent = new Intent(getActivity(), PriceInquiryActivity.class);
                startActivity(intent);
                break;
            case 1:
                // 每日讯 -> 看资讯
                Intent intent1 = new Intent(getActivity(), InfoDayActivity.class);
                startActivity(intent1);
                break;
            case 2:
                // 看指数
                Intent intent2 = new Intent(getActivity(), IndexActivity.class);
                startActivity(intent2);
                break;
            case 3:
                // 找客户
                Intent intent3 = new Intent(getActivity(), ClientActivity.class);
                startActivity(intent3);
                break;

            case 4:
                // 采原料
                Intent intent4 = new Intent(getActivity(), MaterialActivity.class);
                startActivity(intent4);
                break;
            default:
                break;

        }
    }


//    @Override
//    public void setDealCardClick(int position) {
//        ToastUtil.showToast(getContext(), "setDealCardClick");
//
//    }
//
//
//    @Override
//    public void setHw24hTitleMoreClick() {
//        // 更多页面
//        MoreActivity.startMoreActivity(getContext(), 0);
//    }
//
//    @Override
//    public void setRecommendTitleMoreClick() {
//        // 更多页面
//        MoreActivity.startMoreActivity(getContext(), 1);
//    }
    /**
     *  RecyclerView - 情绪指数（走马灯）
     */
    @Override
    public void initMarqueeView(MarqueeRecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mPresenter.getIndexAdapter().setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int tag) {
                // GO TO : PP 情绪指数
                MoodIndexActivity.startMoodIndexActivity(activity);
            }
        });

        recyclerView.setAdapter(mPresenter.getIndexAdapter());
        recyclerView.start();
    }

    @Override
    public void initDealRecyclerView(AutoRollRecyclerView autoRollRecyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        autoRollRecyclerView.setLayoutManager(layoutManager);
        mDealAdapter = mPresenter.getDealAdapter();
        autoRollRecyclerView.setAdapter(mDealAdapter);
        //AutoRollRecyclerView.start(); // 取消自动轮播交易信息
    }

    @Override
    public void initBreedRecyclerView(RecyclerView recyclerView) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getBreedAdapter());
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAtLeastOne() {
        Toast.makeText(activity, getResources().getString(R.string.at_least_one), Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public void initRecommendListView(RecyclerView recyclerView) {
//
//        LinearLayoutManager recommendLayoutManager =
//                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(recommendLayoutManager);
//        recyclerView.setAdapter(mPresenter.getRecommendAdapter());
//    }


//    @Override
//    public void setNetworkErrorView() {
//
//    }
//
//    @Override
//    public void setErrorView() {
//
//    }
//
//
//    @Override
//    public void setEmptyView() {
//
//    }


    // +关注
    @Override
    public void chooseBreeds() {
        // 弹框选择Breeds
        HwAppDialog dialog = new HwAppDialog
                .Builder(activity)
                .setTitle(getResources().getString(R.string.title_dialog_deal))
                .setInsideContentView(mPresenter.getBreedView())
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updateBreeds();
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


    //  SwipeRefreshLayout refresher;
    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }


    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            super.onScrollStateChanged(recyclerView, newState);

            if (newState == SCROLL_STATE_IDLE ) {

                int lastVisibleItem;
                // 获取RecyclerView的LayoutManager
                layoutManager = recyclerView.getLayoutManager();
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

                int totalItemCount = layoutManager.getItemCount();
                /**
                 最后一个可见的item为最后一个item
                 是向下滑动
                 */
                if (lastVisibleItem >= totalItemCount - 1 && isScrollDown) {
                    // 说明滚动到底部,触发加载更多
                    mPresenter.setIsHasMore(HAS_MORE);
                    Log.i(TAG, "run  ******************************* : HAS_MORE");
                    mPresenter.getFooterAdapter().notifyDataSetChanged();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mPresenter.getIsHasMore() != NO_MORE){
                                mPageNum ++;
                                Log.i(TAG, "run  mPageNum ******************************* : =="+ mPageNum);
                                // 加载数据
                                switch (mPresenter.getNewsMode()){
                                    case CURRENT_NEWS:
                                        mPresenter.getHomePageNews(mPageNum, PageSize);
                                        break;
                                    case INDUSTRY_FOCUSED:
                                        mPresenter.getHomePageIndustryNews(mPageNum, PageSize);
                                        break;
                                    case MARKET_COMMENT:    // 宏观头条
                                        mPresenter.getHomePageMarketNews(mPageNum, PageSize);
                                        break;
                                    case HY_NEWS:           // 行业热点
                                        mPresenter.getHomePageHyNews(mPageNum, PageSize);
                                        break;
                                    default:
                                        break;
                                }

                            }else {
                                mPresenter.setIsHasMore(HAS_BOTTOM);
                                Log.i(TAG, "run  ******************************* : HAS_BOTTOM ");
                                mPresenter.getFooterAdapter().notifyDataSetChanged();
                            }

                        }
                    },1000);

                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
            distance += dy;
            // 上滑一定距离显示“首页”　否则隐藏
            if (distance > distance_title){
                showToolbar();
            }else{
                hideToolbar();
            }

//            if (distance < distance_title && distance > 0){
//                showToolbar(distance/64);
//            }else if (distance >= distance_title){
//                showToolbar(1);
//            }



            LogUtils.printInfo(TAG, "distance: " + distance);
            // 上滑一定距离显示“TOP BUTTON”　否则隐藏
            if (distance > distance_top){
                showBackTopIcon();
            }else{
                hideBackTopIcon();
            }

            VirtualLayoutManager lm = (VirtualLayoutManager)recyclerView.getLayoutManager();
            lastVisibleItem = lm.findLastVisibleItemPosition();

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