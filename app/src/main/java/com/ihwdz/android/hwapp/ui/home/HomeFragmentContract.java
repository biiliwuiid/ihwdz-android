package com.ihwdz.android.hwapp.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.ihwdz.android.hwapp.base.adapter.BaseBannerPagerAdapter;
import com.ihwdz.android.hwapp.base.adapter.BaseDelegateAdapter;
import com.ihwdz.android.hwapp.base.adapter.SubAdapter;
import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.entity.Hw24hModel;
import com.ihwdz.android.hwapp.model.entity.NewsModel;
import com.ihwdz.android.hwapp.ui.home.deal.CardFlowAdapter;
import com.ihwdz.android.hwapp.ui.home.recommend.RecommendAdapter;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.widget.AutoRollRecyclerView;
import com.ihwdz.android.hwapp.widget.MarqueeRecyclerView;
import com.yc.cn.ycbannerlib.BannerView;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :　首页 Fragment
 * version: 1.0
 * </pre>
 */
public interface HomeFragmentContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void showToolbar();
        void hideToolbar();

        void showBackTopIcon();   // 显示返回顶部按钮
        void hideBackTopIcon();

        void refreshBottomNews();

        void initBanner(BannerView mBanner); // banner
        void setGridClick(int position);     // tabs_5
        void initMarqueeView(MarqueeRecyclerView recyclerView);                // RecyclerView - 情绪指数（走马灯）

        void initDealRecyclerView(AutoRollRecyclerView autoRollRecyclerView);  // RecyclerView - 今日成交（现款价）

        void initBreedRecyclerView(RecyclerView recyclerView);

        void showPromptMessage(String message);
        void showAtLeastOne();

//        void setNetworkErrorView();
//        void setErrorView();
//        void setEmptyView();

        void chooseBreeds();            // 今日成交（现款价）-- +关注
    }

    int NO_MORE = 0;
    int HAS_MORE = 1;
    int HAS_COMPLETE = 2;
    int HAS_BOTTOM = 3;

    int LOAD_FIRST = 0;
    int LOAD_MORE = 1;

    int PageNum = 1;
    int PageSize = 15;

    int CURRENT_NEWS = 0;      // 市场风云
    int INDUSTRY_FOCUSED = 1;  // 深度研报
    int MARKET_COMMENT = 2;    // 宏观头条
    int HY_NEWS = 3;           // 行业热点


    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        void getHomePageData();             // 首页数据
        void getDealData();                 // 今日成交
        void getBreedsData();               // 今日成交关注 Breed

        // 底部新闻 数据
        void getHomePageNews(int pageNum, int pageSize);            // 市场风云
        void getHomePageIndustryNews(int pageNum, int pageSize);    // 深度研报
        void getHomePageMarketNews(int pageNum, int pageSize);      // 宏观头条
        void getHomePageHyNews(int pageNum, int pageSize);          // 行业热点

        void getHomeFlushData();            // 看资讯 - 小红点（新消息数）

        void refreshData();

        String getUnreadCount();
        void setUnreadCount(String count);

        String getSelectedBreeds();
        void setSelectedBreeds(String breeds);

        void setIsHasMore(int hasMore);
        int getIsHasMore();

        void setNewsMode(int mode);               // 设置新闻类型： 市场风云 / 深度研报 / 行业热点 / 宏观头条
        int getNewsMode();

//        void setLoadMode(int mode);
//        int getLoadMode();

        BaseBannerPagerAdapter getBannerAdapter();
        IndexAdapter getIndexAdapter();             // 情绪指数-适配器
        CardFlowAdapter getDealAdapter();           // 今日成交（现款价）-适配器
        BreedAdapter getBreedAdapter();             // 今日成交（现款价） 关注 Breeds -适配器
        // RecommendAdapter getRecommendAdapter();  // 推荐-适配器 - - not uses mow
        BaseDelegateAdapter getFooterAdapter();


        // List<CharSequence> getMarqueeTitle();
        List<Object> getBannerData();
        // List<Hw24hModel> getHw24hList();
        List<NewsModel> getBottomNewsList();


        String getTodayDate();
        DelegateAdapter initRecyclerView(RecyclerView recyclerView); // 初始化 VLayout 的根布局


        BaseDelegateAdapter initBannerAdapter();        // 轮播
        BaseDelegateAdapter initGvMenu();               // tabs_5
        BaseDelegateAdapter initMarqueeView();          // 情绪指数
        BaseDelegateAdapter initTitleDeal(String date); // 今日成交 标题
        BaseDelegateAdapter initDealList();             // 今日成交 数据

//        BaseDelegateAdapter initTitleHw24();
//        BaseDelegateAdapter initHw24hList();      // hw24h <- initList2 using now
//        SubAdapter initHw24List();                // hw24h <- initList2
//
//        BaseDelegateAdapter initTitleRecommend();
//        BaseDelegateAdapter initRecommendList();  // recommend

        BaseDelegateAdapter initTitleBottomNews();      // news title
        SubAdapter initBottomCurrentNewsList();         // current news
        BaseDelegateAdapter initFooter();               // Footer

        android.view.View getBreedView();         // +关注 弹窗视图
        void updateBreeds();                      // +关注 弹窗视图 点击确认按钮
    }
}
