package com.ihwdz.android.hwapp.ui.home;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.blankj.utilcode.util.SizeUtils;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.BreedData;
import com.ihwdz.android.hwapp.model.bean.DealData;
import com.ihwdz.android.hwapp.model.bean.FlushData;
import com.ihwdz.android.hwapp.model.bean.HomePageData;
import com.ihwdz.android.hwapp.ui.home.detail.NewsDetailActivity;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceMenuAdapter;
import com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositActivity;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.base.adapter.BaseBannerPagerAdapter;
import com.ihwdz.android.hwapp.base.adapter.BaseDelegateAdapter;
import com.ihwdz.android.hwapp.base.adapter.SubAdapter;
import com.ihwdz.android.hwapp.base.app.BaseApplication;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.entity.HomePageNews;
import com.ihwdz.android.hwapp.model.entity.NewsModel;
import com.ihwdz.android.hwapp.ui.home.deal.CardFlowAdapter;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwapp.widget.AutoRollRecyclerView;
import com.ihwdz.android.hwapp.widget.MarqueeRecyclerView;
import com.ihwdz.android.hwslimcore.API.homeData.HomeDataModel;
import com.ihwdz.android.hwslimcore.Base.ISlimMainController;
import com.ihwdz.android.hwslimcore.Base.SlimMainController;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;
import com.yc.cn.ycbannerlib.BannerView;
import com.yc.cn.ycbaseadapterlib.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.CURRENT_NEWS;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HAS_BOTTOM;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HAS_COMPLETE;
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
 * desc :   首页
 * version: 1.0
 * </pre>
 */
public class HomeFragmentPresenter implements HomeFragmentContract.Presenter {

    String TAG = "HomeFragmentPresenter";
    @Inject ISlimMainController controller;
    @Inject IAppSettings settings;

    @Inject BaseBannerPagerAdapter mBannerAdapter;
    @Inject BaseDelegateAdapter mGvMenuAdapter;
    @Inject IndexAdapter mIndexAdapter;

    @Inject CardFlowAdapter mDealAdapter;

    @Inject BreedAdapter mBreedAdapter;  // 关注 breed

    @Inject SubAdapter mCurrentNewsAdapter;
    @Inject BaseDelegateAdapter mFooterAdapter;

    @NonNull private CompositeSubscription mSubscriptions;
    HomeDataModel model ;
    private Realm realm;
    private HomeFragmentContract.View mView;
    private MainActivity activity;

    static final int CURRENT_ACTION_NONE = 0;
    static final int CURRENT_ACTION_REFRESH = 1;
    static final int CURRENT_ACTION_GET_DATA = 2;
    static final int CURRENT_ACTION_LOGOUT = 3;

    Drawable selectedBackground ;
    int selectedColor;
    Drawable normalBackground;
    int normalColor;

    int currentAction = CURRENT_ACTION_NONE;
    private boolean mFirstLoad = true;

    int currentMode = HomeFragmentContract.HAS_MORE;
    int currentLoadMode = HomeFragmentContract.LOAD_FIRST;
    int currentNewsMode = CURRENT_NEWS;

    String currentTime = null;     // 获取　每日讯－小红点　时的参数，每日讯　中的:　systemTime
    private String currentUnreadCount;
    private int currentUnreadCountInt;

    private List<String> allBreedList = null;    // all Breed list.
    private List<String> mBreedList = null;      // 当前选择的 Breed list.
    private String selectedBreeds = null;        // 选择的 Breed list -> String,   本地存储
    private String currentSelectedBreeds = "";   // 当前选择的 Breed list.


    private boolean isLastOneSelected = false;

    CheckBox cb_abs;
    CheckBox cb_pp;
    CheckBox cb_pc;
    CheckBox cb_lldpe;
    CheckBox cb_ldpe;
    CheckBox cb_hdpe;

    List<NewsModel> mBottomNewsList;

    public HomeFragmentPresenter(HomeFragmentContract.View homeView) {
        this.mView = homeView;
        mSubscriptions = new CompositeSubscription();
        model = HomeDataModel.getInstance(activity);
    }


    @Override
    public void subscribe() {
        initRealm();
    }


    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(activity != null){
            activity = null;
        }
    }



    private void clearAllAdapter(){
        //mBannerAdapter.clear(); 下拉刷新时banner不变
        mIndexAdapter.clear();
        mDealAdapter.clear();
    }

    private void setAdapterData(HomePageData data) {
        if(mIndexAdapter == null){
            mIndexAdapter = new IndexAdapter(activity);
        }
        if(mDealAdapter == null){
            mDealAdapter = new CardFlowAdapter(activity);
        }
        if (mBannerAdapter.getCount()<1){
            mBannerAdapter.setDataList(data.data.ads);
        }
        mIndexAdapter.setDataList(data.data.indexes);
        mDealAdapter.setDataList(data.data.prices);
    }

    @Override
    public void start() {
        settings = new SlimAppSettings(activity);
        controller = new SlimMainController(activity);
        if (mBreedList == null){
            mBreedList = new ArrayList<>();
        }

        selectedBreeds = settings.getLastBreeds();   // 本地保存

        if (selectedBreeds != null && selectedBreeds.length() > 0){
            mBreedList.clear();

            if (selectedBreeds.contains(",")){
                String[] breedsArray = selectedBreeds.split(",");
                List<String> resultList = new ArrayList<>(Arrays.asList(breedsArray));

                mBreedList.addAll(resultList);
            }else {
                mBreedList.add(selectedBreeds);
            }
        }else {
            // 本地没有 breed 关注记录
        }
        // LogUtils.printCloseableInfo(TAG, "start");
        LogUtils.printCloseableInfo(TAG, "start   selectedBreeds: "+ selectedBreeds);
    }

    //  public void onStop() 里调用
    @Override
    public void stop() {
//        String breeds = mBreedList.toString();
//        LogUtils.printCloseableInfo(TAG, "stop   breeds: "+ breeds);
//        selectedBreeds = breeds.substring(1, breeds.length()-1);
//        LogUtils.printCloseableInfo(TAG, "stop   selectedBreeds: "+ selectedBreeds);
//        //selectedBreeds = mBreedList.toString();
//
//        settings.storeLastBreeds(selectedBreeds);


    }

    static final String CURRENT_SELECTED_BREEDS = "currentSelectedBreeds";
    @Override
    public void store(Bundle outState) {
//        outState.putString(CURRENT_SELECTED_BREEDS, selectedBreeds); // 记住当前选择的Breeds.
//        LogUtils.printInfo(TAG, "--------- STORE ---------");
    }

    @Override
    public void restore(Bundle inState) {
//        LogUtils.printInfo(TAG, "--------- restore ---------");
//        selectedBreeds = inState.getString(CURRENT_SELECTED_BREEDS);
//        LogUtils.printInfo(TAG, "--------- restore --------- inState.getString: " + selectedBreeds);
//        selectedBreeds = settings.getLastBreeds();
//        LogUtils.printInfo(TAG, "--------- restore --------- controller.getLastBreeds(): " + selectedBreeds);
    }


    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void getHomePageData() {

        Subscription rxSubscription = model
                .getHomePageData()
                .compose(RxUtil.<HomePageData>rxSchedulerHelper())
                .subscribe(new Subscriber<HomePageData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        if (currentAction == CURRENT_ACTION_REFRESH){
                            currentAction = CURRENT_ACTION_GET_DATA;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, e.toString());
                    }

                    @Override
                    public void onNext(HomePageData data) {
                        mView.hideWaitingRing();
                        if (data != null && data.data != null){
                            setAdapterData(data);
                            if (currentAction != CURRENT_ACTION_REFRESH){
                            }
                        }
                        else {
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void getDealData() {
        LogUtils.printInfo(TAG, "getDealData ============= selectedBreeds: " + selectedBreeds);
        Subscription rxSubscription = model
                .getDealData(selectedBreeds)
                .compose(RxUtil.<DealData>rxSchedulerHelper())
                .subscribe(new Subscriber<DealData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DealData data) {
                        if (data.getCode().equals("0")){
                            if (data.getData()!= null && data.getData().size() > 0){
                                mDealAdapter.clear();
                                mDealAdapter.setDataList(data.getData());
                            }
                        }else {
                            // 获取失败！
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getBreedsData() {
        // LogUtils.printInfo(TAG, "getBreedsData ============= ");
        Subscription rxSubscription = model
                .getBreedData()
                .compose(RxUtil.<BreedData>rxSchedulerHelper())
                .subscribe(new Subscriber<BreedData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG,"getBreedsData onError: "+ e.toString());
                    }

                    @Override
                    public void onNext(BreedData data) {
                        if (data.code.equals("0")){
                            if (data.data!= null && data.data.breeds != null){
                                allBreedList = data.data.breeds;
                                LogUtils.printInfo(TAG, "getBreedsData  getBreedAdapter().setDataList============= mBreedList: "+ mBreedList.size());
                                LogUtils.printInfo(TAG, "getBreedsData getBreedAdapter().setDataList ============= allBreedList: "+ allBreedList.size());
                            }
                        }else {
                            // 获取失败！
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 为菜单列表 添加check
    List<BreedData.CheckableItem> getCheckableBreedData(List<String> list){
        List<BreedData.CheckableItem> results = new ArrayList();
        BreedData.CheckableItem item;

        if (list != null && list.size()>0){
            for (int i = 0; i < list.size(); i++){
                item = new BreedData.CheckableItem();
                String str = list.get(i);
                item.name = str;
                item.isChecked = false;
                results.add(item);
            }
        }
        return results;
    }


    @Override
    public void getHomePageNews(final int pageNum, int pageSize) {

        Subscription rxSubscription = model
                .getHomePageCurrentNews(pageNum, pageSize)
                .compose(RxUtil.<HomePageNews>rxSchedulerHelper())
                .subscribe(new Subscriber<HomePageNews>() {
                    @Override
                    public void onCompleted() {
                        mCurrentNewsAdapter.notifyDataSetChanged();

                        if (getIsHasMore() == HAS_MORE){
                            setIsHasMore(HAS_COMPLETE);
                            //Log.d(TAG, "onCompleted home page news ============== HAS_COMPLETE ============= mFooterAdapter.notifyDataSetChanged();");
                        }
                        mFooterAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(HomePageNews data) {
                        if (data != null && data.getData() != null){
                            if (pageNum == 1){
                                if (mBottomNewsList != null){
                                    mBottomNewsList.clear();
                                }
                                getBottomNewsList().addAll(data.getData().getBottomNews().getRecordList());
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }else {

                                if(data.getData().getBottomNews().getRecordList().size() > 0){
                                    mBottomNewsList.addAll(data.getData().getBottomNews().getRecordList());
                                }else {
                                    setIsHasMore(NO_MORE);
                                }
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            //mView.setEmptyView();
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void getHomePageIndustryNews(final int pageNum, int pageSize) {

        Subscription rxSubscription = model
                .getHomePageIndustryNews(pageNum, pageSize)
                .compose(RxUtil.<HomePageNews>rxSchedulerHelper())
                .subscribe(new Subscriber<HomePageNews>() {
                    @Override
                    public void onCompleted() {
                        mCurrentNewsAdapter.notifyDataSetChanged();

                        if (getIsHasMore() == HAS_MORE){
                            setIsHasMore(HAS_COMPLETE);
                        }
                        mFooterAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(HomePageNews data) {
                        if (data != null && data.getData() != null){
                            if (pageNum == 1){
                                if (mBottomNewsList != null){
                                    mBottomNewsList.clear();
                                }
                                getBottomNewsList().addAll(data.getData().getBottomNews().getRecordList());
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }else {

                                if(data.getData().getBottomNews().getRecordList().size() > 0){
                                    mBottomNewsList.addAll(data.getData().getBottomNews().getRecordList());
                                }else {
                                    setIsHasMore(NO_MORE);
                                }
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }

                        }
                        else {
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getHomePageMarketNews(final int pageNum, int pageSize) {

        Subscription rxSubscription = model
                .getHomePageHyNews(pageNum, pageSize)
                .compose(RxUtil.<HomePageNews>rxSchedulerHelper())
                .subscribe(new Subscriber<HomePageNews>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted MarketNews");
                        mCurrentNewsAdapter.notifyDataSetChanged();

                        if (getIsHasMore() == HAS_MORE){
                            setIsHasMore(HAS_COMPLETE);
                        }
                        mFooterAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError ON GET MarketNews");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(HomePageNews data) {
                        if (data != null && data.getData() != null){
                            if (pageNum == 1){
                                if (mBottomNewsList != null){
                                    mBottomNewsList.clear();
                                }
                                getBottomNewsList().addAll(data.getData().getBottomNews().getRecordList());
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }else {

                                if(data.getData().getBottomNews().getRecordList().size() > 0){
                                    mBottomNewsList.addAll(data.getData().getBottomNews().getRecordList());
                                }else {
                                    setIsHasMore(NO_MORE);
                                }
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }

                        }
                        else {
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getHomePageHyNews(final int pageNum, int pageSize) {

        Subscription rxSubscription = model
                .getHomePageMarketNews(pageNum, pageSize)
                .compose(RxUtil.<HomePageNews>rxSchedulerHelper())
                .subscribe(new Subscriber<HomePageNews>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted MarketNews");
                        mCurrentNewsAdapter.notifyDataSetChanged();

                        if (getIsHasMore() == HAS_MORE){
                            setIsHasMore(HAS_COMPLETE);
                            //Log.d(TAG, "onCompleted MarketNews ============== HAS_COMPLETE ============= mFooterAdapter.notifyDataSetChanged();");
                        }
                        mFooterAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError ON GET MarketNews");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(HomePageNews data) {
//                        Log.d("RETROFIT", "onNext MarketNews");
                        if (data != null && data.getData() != null){
                            //Log.d(TAG, "onNext MarketNews ============================== pageNum  === "+pageNum);
                            if (pageNum == 1){
//                                mView.showWaitingRing();
                                if (mBottomNewsList != null){
                                    mBottomNewsList.clear();
                                }
                                getBottomNewsList().addAll(data.getData().getBottomNews().getRecordList());
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }else {
//                                mView.showWaitingRing();
                                //Log.d(TAG, "onNext MarketNews ============================== data.size  === "+ data.getData().getBottomNews().getRecordList().size());

                                if(data.getData().getBottomNews().getRecordList().size() > 0){
                                    mBottomNewsList.addAll(data.getData().getBottomNews().getRecordList());
                                }else {
                                    setIsHasMore(NO_MORE);
                                    //Log.d(TAG, "onNext MarketNews ===============********************************===============NO_MORE ");
                                }
                                //Log.d(TAG, "onNext MarketNews ============================== mBottomNewsList  === "+ mBottomNewsList.size());
                                mCurrentNewsAdapter.notifyDataSetChanged();
                            }

                        }
                        else {
                            if (data.msg != null && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void getHomeFlushData() {

        Subscription rxSubscription = model
                .getFlushData(Constant.systemTime)
                .compose(RxUtil.<FlushData>rxSchedulerHelper())
                .subscribe(new Subscriber<FlushData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "===== getHomeFlushData ===== onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FlushData data) {
                        Log.d(TAG, "===== getHomeFlushData ===== onNext data" + data.getData().getFastNewsTag() + " | systemTime: " + Constant.systemTime);
                        if (data != null && data.getData()!= null)
                        setUnreadCount(data.getData().getFastNewsTag());
                        mGvMenuAdapter.notifyItemChanged(1);  // 更新第二项（每日讯）position == 1
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void refreshData() {
        clearAllAdapter();
        mView.showWaitingRing();
        currentAction = CURRENT_ACTION_REFRESH;
        getHomePageData();
        getDealData();
        getBreedsData();
        getHomeFlushData();
        getHomePageNews(PageNum, PageSize);

    }


    @Override
    public String getUnreadCount() {
        return currentUnreadCount;
    }

    @Override
    public void setUnreadCount(String count) {
        if (count != null && !count.equals(currentUnreadCount)){
            currentUnreadCount = count;
            currentUnreadCountInt = new Integer(count);

        }else {
            currentUnreadCountInt = 0;
        }
        Log.d(TAG, "============ setUnreadCount ===========" + currentUnreadCountInt);
    }

    @Override
    public String getSelectedBreeds() {

        return selectedBreeds;
    }

    @Override
    public void setSelectedBreeds(String breeds) {
        if (breeds != null && !selectedBreeds.equals(breeds)){
            this.selectedBreeds = breeds;
        }
    }


    @Override
    public void setIsHasMore(int hasMore) {
        if(this.currentMode != hasMore) {
            this.currentMode = hasMore;
        }
    }

    @Override
    public int getIsHasMore() {
        return this.currentMode;
    }

    TextView currentNews ;
    TextView industryFocused ;
    TextView marketComment ;
    TextView hyNews;
    @Override
    public void setNewsMode(int mode) {
        if(this.currentNewsMode != mode) { // MODE CHANGE ->DATA CHANGE
            this.currentNewsMode = mode;
            mView.refreshBottomNews();
//            mCurrentNewsAdapter.notifyDataSetChanged();
            switch (mode){
                case CURRENT_NEWS:
                    setIsHasMore(HAS_MORE);

                    currentNews.setTextColor(selectedColor);
                    currentNews.setBackground(selectedBackground);
                    industryFocused.setTextColor(normalColor);
                    industryFocused.setBackground(normalBackground);
                    marketComment.setTextColor(normalColor);
                    marketComment.setBackground(normalBackground);
                    hyNews.setTextColor(normalColor);
                    hyNews.setBackground(normalBackground);
                    getHomePageNews(PageNum, PageSize);
                    break;
                case INDUSTRY_FOCUSED:
                    setIsHasMore(HAS_MORE);

                    currentNews.setTextColor(normalColor);
                    currentNews.setBackground(normalBackground);
                    industryFocused.setTextColor(selectedColor);
                    industryFocused.setBackground(selectedBackground);
                    marketComment.setTextColor(normalColor);
                    marketComment.setBackground(normalBackground);
                    hyNews.setTextColor(normalColor);
                    hyNews.setBackground(normalBackground);

                    getHomePageIndustryNews(PageNum, PageSize);
                    break;
                case MARKET_COMMENT:
                    setIsHasMore(HAS_MORE);

                    currentNews.setTextColor(normalColor);
                    currentNews.setBackground(normalBackground);
                    industryFocused.setTextColor(normalColor);
                    industryFocused.setBackground(normalBackground);
                    marketComment.setTextColor(selectedColor);
                    marketComment.setBackground(selectedBackground);
                    hyNews.setTextColor(normalColor);
                    hyNews.setBackground(normalBackground);

                    getHomePageMarketNews(PageNum, PageSize);
                    break;

                case HY_NEWS:
                    setIsHasMore(HAS_MORE);

                    currentNews.setTextColor(normalColor);
                    currentNews.setBackground(normalBackground);
                    industryFocused.setTextColor(normalColor);
                    industryFocused.setBackground(normalBackground);
                    marketComment.setTextColor(normalColor);
                    marketComment.setBackground(normalBackground);
                    hyNews.setTextColor(selectedColor);
                    hyNews.setBackground(selectedBackground);

                    getHomePageHyNews(PageNum, PageSize);
                    break;

            }
        }
    }

    @Override
    public int getNewsMode() {
        return currentNewsMode;
    }


    @Override
    public BaseBannerPagerAdapter getBannerAdapter() {
        if(mBannerAdapter == null){
            mBannerAdapter = new BaseBannerPagerAdapter(activity);
        }
        return mBannerAdapter;
    }

    @Override
    public IndexAdapter getIndexAdapter() {
        if(mIndexAdapter == null){
            mIndexAdapter = new IndexAdapter(activity);
        }
        return mIndexAdapter;
    }

    @Override
    public CardFlowAdapter getDealAdapter() {
        if(mDealAdapter == null){
            mDealAdapter = new CardFlowAdapter(activity);
        }
        return mDealAdapter;
    }

    @Override
    public BreedAdapter getBreedAdapter() {
        if(mBreedAdapter == null){
            mBreedAdapter = new BreedAdapter(activity);
        }
        return mBreedAdapter;
    }


    @Override
    public BaseDelegateAdapter getFooterAdapter() {
        return mFooterAdapter;
    }


    @Override
    public List<Object> getBannerData() {
        List<Object> lists = new ArrayList<>();
        TypedArray bannerImage = activity.getResources().obtainTypedArray(R.array.banner_image);
        for (int i = 0; i < 3 ; i++) {
            int image = bannerImage.getResourceId(i, R.drawable.img_default); // 如果数据里没有数据 默认加载 R.drawable.img_default
            lists.add(image);
        }
        bannerImage.recycle();
        return lists;
    }


    @Override
    public List<NewsModel> getBottomNewsList() {
        if (mBottomNewsList == null){
            mBottomNewsList = new ArrayList<>();
        }
        return mBottomNewsList;
    }


    @Override
    public void bindActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getTodayDate() {
        return DateUtils.getDateTodayString();
    }

    @Override
    public DelegateAdapter initRecyclerView(RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    @Override
    public BaseDelegateAdapter initBannerAdapter() {

        // 获取轮播图数据
        final List<Object> arrayList = new ArrayList<>();
        List<Object> lists = getBannerData();
        arrayList.addAll(lists);

        //banner
        return new BaseDelegateAdapter(activity, new LinearLayoutHelper(), R.layout.base_match_banner, 1, Constant.viewType.typeBanner) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                // 绑定数据
                BannerView mBanner = holder.getView(R.id.banner);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.height = SizeUtils.dp2px(120);
                mBanner.setLayoutParams(layoutParams);
                mView.initBanner(mBanner);
            }
        };

    }

    @Override
    public BaseDelegateAdapter initGvMenu() {

        final TypedArray proPic = activity.getResources().obtainTypedArray(R.array.find_gv_image);
        final String[] proName = activity.getResources().getStringArray(R.array.find_gv_title);
        final List<Integer> images = new ArrayList<>();
        for(int a = 0 ; a < proName.length ; a++){
            images.add( proPic.getResourceId(a, R.drawable.img_default));
        }
        proPic.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
        gridLayoutHelper.setPadding(0, 10, 0, 10);
        gridLayoutHelper.setVGap(0);
        gridLayoutHelper.setHGap(0);
        gridLayoutHelper.setBgColor(Color.WHITE);
        mGvMenuAdapter = new BaseDelegateAdapter(activity, gridLayoutHelper, R.layout.item_vp_grid_iv, 5, Constant.viewType.typeGv) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.title, proName[position]);
                holder.setImageResource(R.id.iv, images.get(position));

                holder.getView(R.id.linear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position == 1){
                            currentUnreadCountInt = 0;
                            notifyItemChanged(1);
                        }

                        mView.setGridClick(position);
                    }
                });
//                Log.d(TAG, "*************** ****************** ********************");
//                Log.d(TAG, "*************** initGvMenu ******************** position: "+position);


                // 每日讯　消息提醒 (其他无消息提醒)
                if (position == 1 && currentUnreadCountInt > 0){
                    holder.getView(R.id.fl_title_menu).setVisibility(View.VISIBLE);
                    holder.setText(R.id.msg_count, getUnreadCount());

                }else {
                    holder.getView(R.id.fl_title_menu).setVisibility(View.GONE);
                }

            }
        };
        return mGvMenuAdapter;
    }

    @Override
    public BaseDelegateAdapter initMarqueeView() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return new BaseDelegateAdapter(activity, linearLayoutHelper , R.layout.layout_marquee,
//                R.layout.view_vlayout_marquee,
                1, Constant.viewType.typeMarquee) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                TextView title = holder.getView(R.id.title);
                title.getPaint().setFakeBoldText(true);

                MarqueeRecyclerView recyclerView = holder.getView(R.id.recyclerView);
                mView.initMarqueeView(recyclerView);
            }
        };
    }

    @Override
    public BaseDelegateAdapter initTitleDeal(final String date) {

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        linearLayoutHelper.setDividerHeight(5);
//        linearLayoutHelper.setMargin(0, 10, 0, 0);

        return new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.item_home_deal_header, 1, Constant.viewType.typeTitle_deal) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                TextView title = holder.getView(R.id.title);
                title.getPaint().setFakeBoldText(true);

                holder.setText(R.id.item_header_date, date);
                // 点击“+关注”（之前为: date）
                holder.getView(R.id.item_header_date).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.chooseBreeds();
                    }
                });
            }
        };
    }
    @Override
    public BaseDelegateAdapter initDealList() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setAspectRatio(3.5f);  // 4.0f
        linearLayoutHelper.setDividerHeight(0);   // 5->0
        linearLayoutHelper.setMargin(0, 0, 0, 20);
        linearLayoutHelper.setPadding(0, 0, 0, 0); // 10
        return new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.item_recycler_view_deal, 1, Constant.viewType.typeDealCard) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder,
                                         @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
//                AutoPollRecyclerView autoPollRecyclerView = holder.getView(R.id.autoPollRecyclerView);
                AutoRollRecyclerView autoRollRecyclerView = holder.getView(R.id.autoRollRecyclerView);
                mView.initDealRecyclerView(autoRollRecyclerView);

            }
        };
    }



    int count = 0;
    @Override
    public BaseDelegateAdapter initTitleBottomNews() {
        StickyLayoutHelper layoutHelper = new StickyLayoutHelper();
        //layoutHelper.setOffset(100);
//        layoutHelper.setAspectRatio(4);
        BaseDelegateAdapter mBDA_titleNews =
         new BaseDelegateAdapter(activity, layoutHelper, R.layout.title_bottom_news_home, 1, Constant.viewType.getTypeTitle_news) {

            @Override
            public void onBindViewHolder(final BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);


                currentNews = holder.getView(R.id.current_news);
                industryFocused = holder.getView(R.id.industry_focused);
                marketComment = holder.getView(R.id.market_comment);
                hyNews = holder.getView(R.id.hy_news);

                currentNews.getPaint().setFakeBoldText(true);
                industryFocused.getPaint().setFakeBoldText(true);
                marketComment.getPaint().setFakeBoldText(true);
                hyNews.getPaint().setFakeBoldText(true);

                count++;
                if (count == 1){
                    // 获取默认的背景
                    normalBackground = marketComment.getBackground();
                    normalColor = marketComment.getCurrentTextColor();
                }

//                Log.d(TAG, "==========  normalBackground  :  "+ normalBackground);
//                Log.d(TAG, "==========  normalColor  :  "+ normalColor);

                // 默认 CURRENT_NEWS
                if (currentNewsMode == CURRENT_NEWS){
                    currentNews.setTextColor(selectedColor);
                    currentNews.setBackground(selectedBackground);
                    industryFocused.setTextColor(normalColor);
                    industryFocused.setBackground(normalBackground);
                    marketComment.setTextColor(normalColor);
                    marketComment.setBackground(normalBackground);

                }
                selectedBackground = activity.getResources().getDrawable(R.drawable.tab_background);
                selectedColor = activity.getResources().getColor(R.color.orangeTab);
                View.OnTouchListener onTouchListener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP){
                            switch (v.getId()){
                                case R.id.current_news:
                                    setNewsMode(CURRENT_NEWS);
                                    break;
                                case R.id.industry_focused:
                                    setNewsMode(INDUSTRY_FOCUSED);
                                    break;
                                case R.id.market_comment:
                                    setNewsMode(MARKET_COMMENT);
                                    break;
                                case R.id.hy_news:
                                    setNewsMode(HY_NEWS);
                                    break;
                            }

                        }
                        return false;
                    }
                };

                holder.setOnTouchListener(R.id.current_news, onTouchListener);
                holder.setOnTouchListener(R.id.industry_focused, onTouchListener);
                holder.setOnTouchListener(R.id.market_comment, onTouchListener);
                holder.setOnTouchListener(R.id.hy_news, onTouchListener);

            }
        };

        return mBDA_titleNews;
    }
    @Override
    public SubAdapter initBottomCurrentNewsList() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(2);

        mCurrentNewsAdapter = new SubAdapter(activity, linearLayoutHelper, getBottomNewsList().size(),
                R.layout.item_home_news, Constant.viewType.typeNews_CurrentNews ) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (mBottomNewsList != null && mBottomNewsList.size() > 0){
                    final NewsModel model = mBottomNewsList.get(position);
                    Log.d(TAG, "NewsModel: "+ model);

                    LinearLayout linear = holder.getView(R.id.content_news);
                    linear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // DetailActivity.startDetailActivity(activity, model.getId());
                            NewsDetailActivity.startNewsDetailActivity(activity, model.getId());
                        }
                    });
                    ImageView imageView = holder.getView(R.id.articleImg);
                    ImageUtils.loadImgByPicasso(activity, model.getArticleImg(), imageView);
                    TextView title = holder.getView(R.id.title);
                    title.setText(model.getTitle());
                    TextView viewTimes = holder.getView(R.id.viewTimes);
                    viewTimes.setText(model.getViewTimes());
                    TextView author = holder.getView(R.id.author);
                    author.setText(model.getAuthor());
                    TextView date = holder.getView(R.id.date_news);
                    date.setText(model.getShorDate());
                }else {
                    Log.d(TAG, "COUNT getNewsListAdapter().getData(): = 0/ null");
                }
            }

            @Override
            public int getItemCount() {
                return mBottomNewsList.size();
            }
        };

        return mCurrentNewsAdapter;
    }

    @Override
    public BaseDelegateAdapter initFooter(){
        SingleLayoutHelper layoutHelper = new SingleLayoutHelper();
        layoutHelper.setAspectRatio(6.0f);
        mFooterAdapter =  new BaseDelegateAdapter(activity, layoutHelper, R.layout.foot_view, 1, Constant.viewType.typeFooter){
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                TextView textView = holder.itemView.findViewById(R.id.textview);
                ProgressBar progressBar = holder.itemView.findViewById(R.id.progressbar);

                Log.d(TAG, "*********************************************** initFooter HasMore?????: "+ getIsHasMore());

                if(getIsHasMore() == HAS_MORE){
                    textView.setText(R.string.loading);
                    progressBar.setVisibility(View.VISIBLE);
                }else if(getIsHasMore() == NO_MORE){
                    textView.setText(R.string.load_finish);
                    progressBar.setVisibility(View.GONE);
                }else if(getIsHasMore() == HAS_COMPLETE){
                    textView.setText(R.string.load_complete);
                    progressBar.setVisibility(View.GONE);
                }else if(getIsHasMore() == HAS_BOTTOM){
                    textView.setText(R.string.loading_bottom);
                    progressBar.setVisibility(View.GONE);
                }

            }
        };
        return mFooterAdapter;
    }

    @Override
    public View getBreedView() {

        LogUtils.printInfo(TAG, "========= getBreedView ========");

        View view0 = View.inflate(activity, R.layout.breeds_dialog_list, null);
        RecyclerView recyclerView = view0.findViewById(R.id.recycler_view);
        mView.initBreedRecyclerView(recyclerView);



        if (mBreedList != null && mBreedList.size()>0){
            // 本地有关注数据
        }else {
            // 本地没有关注数据 - 全选
            if (mBreedList == null){
                mBreedList = new ArrayList<>();
            }
            mBreedList.addAll(allBreedList);
        }
        LogUtils.printInfo(TAG, "getBreedView  getBreedAdapter().setDataList============= mBreedList: "+ mBreedList.size());
        LogUtils.printInfo(TAG, "getBreedView getBreedAdapter().setDataList ============= allBreedList: "+ allBreedList.size());

        getBreedAdapter().clear();
        getBreedAdapter().setDataList(mBreedList, getCheckableBreedData(allBreedList));
        return view0;

    }

    // 点击弹窗的确认按钮后 更新数据 getDealData();
    @Override
    public void updateBreeds() {

        mBreedList = mBreedAdapter.getSelectedList();  // 当前选中的 breeds

        String breeds = mBreedList.toString().trim();
        selectedBreeds = breeds.substring(1, breeds.length()-1);
        LogUtils.printCloseableInfo(TAG, "updateBreeds   selectedBreeds: " + selectedBreeds );
        getDealData();                            // 更新 今日成交 数据
        settings.storeLastBreeds(selectedBreeds); // 更新 本地 breed关注 数据


    }

    private void checkCurrentBreeds(){
        currentSelectedBreeds = "";
        if (cb_abs.isChecked()){
            currentSelectedBreeds = "ABS";
        }
        if (cb_pp.isChecked()){
            if (currentSelectedBreeds == null){
                currentSelectedBreeds = "PP";
            }else {
                currentSelectedBreeds = currentSelectedBreeds + ",PP";
            }
        }
        if (cb_pc.isChecked()){
            if (currentSelectedBreeds == null){
                currentSelectedBreeds = "PC";
            }else {
                currentSelectedBreeds = currentSelectedBreeds + ",PC";
            }
        }
        if (cb_lldpe.isChecked()){
            if (currentSelectedBreeds == null){
                currentSelectedBreeds = "LLDPE";
            }else {
                currentSelectedBreeds = currentSelectedBreeds + ",LLDPE";
            }
        }
        if (cb_ldpe.isChecked()){
            if (currentSelectedBreeds == null){
                currentSelectedBreeds = "LDPE";
            }else {
                currentSelectedBreeds = currentSelectedBreeds + ",LDPE";
            }
        }
        if (cb_hdpe.isChecked()){
            if (currentSelectedBreeds == null){
                currentSelectedBreeds = "HDPE";
            }else {
                currentSelectedBreeds = currentSelectedBreeds + ",HDPE";
            }
        }
        LogUtils.printInfo(TAG, "checkCurrentBreeds: currentSelectedBreeds: "+ currentSelectedBreeds );
    }

    private void checkRadioButtonState(){
        checkCurrentBreeds();
        if (currentSelectedBreeds != null){
//            if (!currentSelectedBreeds.contains(",") && currentSelectedBreeds.length() > 0){
//                // 最后一个已选选项
//                isLastOneSelected = true;
//                mView.showPromptMessage(activity.getResources().getString(R.string.at_least_one));
//            }
            if (currentSelectedBreeds.length() == 0){
                isLastOneSelected = true;
                // 0个已选选项
                mView.showAtLeastOne();
            }
        }

    }


    //    @Override
//    public BaseDelegateAdapter initTitleHw24() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//
//        linearLayoutHelper.setDividerHeight(2);
//        BaseDelegateAdapter mBDA_titleHw24 = new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.item_home_header_hw24h, 1, Constant.viewType.typeTitle_hw24) {
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//                holder.getView(R.id.item_header_more).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mView.setHw24hTitleMoreClick();
//                    }
//                });
//            }
//        };
//        return mBDA_titleHw24;
//    }
//
//
//    @Override
//    public BaseDelegateAdapter initHw24hList() {
//
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        linearLayoutHelper.setDividerHeight(0); // 5
//        linearLayoutHelper.setMargin(0,0,0,0);
//        mHw24hAdapter = new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.item_home_hw24h, getHw24Count(), Constant.viewType.typeList_hw24) {
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//                if (mHw24hList != null && mHw24hList.size() > 0){
//
//                    Hw24hModel model = mHw24hList.get(position);
//
//                    TextView title = holder.getView(R.id.title);
//                    title.setText(model.getTitle());
//
//                    TextView name1 = holder.getView(R.id.name1);
//                    TextView name2 = holder.getView(R.id.name2);
//                    TextView name3 = holder.getView(R.id.name3);
//
//                    String[] strs;
//                    if (model.getKeywords().contains(",")){
//                        strs = model.getKeywords().split(",");
//                        if (strs.length >= 1 && strs[0] != null){
//                            name1.setVisibility(View.VISIBLE);
//                            //name1.setText(model.getCatName());
//                            name1.setText(strs[0]);
//                        }
//                        if (strs.length >= 2 && strs[1] != null){
//                            name2.setVisibility(View.VISIBLE);
//                            name2.setText(strs[1]);
//                        }
//                        if (strs.length >= 3 && strs[2] != null){
//                            name3.setVisibility(View.VISIBLE);
//                            name3.setText(strs[2]);
//                        }
//                    }else {
//                        if (model.getKeywords().length() > 0){
//                            name1.setVisibility(View.VISIBLE);
//                            name1.setText(model.getCatName());
//                        }else {
//                            // model.getKeywords() = "";
//                        }
//                    }
//
//
//                    TextView viewTimes = holder.getView(R.id.viewTimes);
//                    viewTimes.setText(model.getViewTimes());
//                    TextView author = holder.getView(R.id.author);
//                    author.setText(model.getAuthor());
//                    TextView date = holder.getView(R.id.showTimer);
//                    date.setText(model.getShorTime());
//
//                }else {
//                    Log.d(TAG, "COUNT getHw24hAdapter().getData(): = 0/ null");
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return getHw24Count();
//            }
//        };
//        return mHw24hAdapter;
//    }
//
//    private int getHw24Count(){
//        int count = 1;
////        count = getHw24hAdapter().getCount();
//        count = getHw24hList().size();
//        if (count > 3){
//            count = 3;
//        }
//        return count;
//    }
//    @Override
//    public SubAdapter initHw24List() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        linearLayoutHelper.setDividerHeight(0);
//        linearLayoutHelper.setMargin(0,0,0,0);
//
//
////        Log.d(TAG, "COUNT_Hw24Adapter: "+ getHw24Count() );
//        mHw24Adapter = new SubAdapter(activity, linearLayoutHelper, getHw24Count() ,R.layout.item_home_hw24h, Constant.viewType.typeList_hw24) {
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//
////                List<Hw24hModel> tempList = getHw24hAdapter().getData();
//
////                if (tempList != null && tempList.size() > 0){
//                if (mHw24hList != null && mHw24hList.size() > 0){
//
////                    Hw24hModel model = (Hw24hModel) getHw24hAdapter().getItem(position);
//
//                    Hw24hModel model = mHw24hList.get(position);
////                    Log.d(TAG, "Hw24hModel: "+ model);
//
//                    TextView title = holder.getView(R.id.title);
//                    title.setText(model.getTitle());
//
//                    TextView name1 = holder.getView(R.id.name1);
//                    TextView name2 = holder.getView(R.id.name2);
//                    TextView name3 = holder.getView(R.id.name3);
//
//                    String[] strs;
//                    if (model.getKeywords().contains(",")){
//                        strs = model.getKeywords().split(",");
//                        if (strs.length >= 1 && strs[0] != null){
//                            name1.setText(strs[0]);
//                        }
//                        if (strs.length >= 2 && strs[1] != null){
//                            name2.setVisibility(View.VISIBLE);
//                            name2.setText(strs[1]);
//                        }
//                        if (strs.length >= 3 && strs[2] != null){
//                            name3.setVisibility(View.VISIBLE);
//                            name3.setText(strs[2]);
//                        }
//                    }else {
//                        if (model.getKeywords().length() > 0){
//                            name1.setVisibility(View.VISIBLE);
//                            name1.setText(model.getCatName());
//                        }else {
//                            // model.getKeywords() = "";
//                        }
//                    }
//
//
//
//                    TextView viewTimes = holder.getView(R.id.viewTimes);
//                    viewTimes.setText(model.getViewTimes());
//                    TextView author = holder.getView(R.id.author);
//                    author.setText(model.getAuthor());
//                    TextView date = holder.getView(R.id.showTimer);
//                    date.setText(model.getShorTime());
//
//                }else {
//                    Log.d(TAG, "COUNT getHw24hAdapter().getData(): = 0/ null");
//                }
//
//            }
//
//            @Override
//            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                return new BaseViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_home_hw24h, parent, false));
//            }
//
//            @Override
//            public int getItemCount() {
////                return getHw24hAdapter().getCount();
//                return getHw24Count();
//            }
//        };
//        return mHw24Adapter;
//    }
//
//    @Override
//    public BaseDelegateAdapter initTitleRecommend() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        linearLayoutHelper.setMargin(0, 0, 0, 0);
//        mBDA_titleRecommend =
//                new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.item_home_header_recommend, 1, Constant.viewType.typeTitle_recommend) {
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder, int position) {
//                super.onBindViewHolder(holder, position);
//                holder.getView(R.id.item_header_more).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mView.setRecommendTitleMoreClick();
//                    }
//                });
//            }
//            @Override
//            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                return new BaseViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_home_header_recommend, parent, false));
//            }
//
//            @Override
//            public int getItemCount() {
//                return 1;
//            }
//
//        };
//
//       return mBDA_titleRecommend;
//
//
//    }

//    @Override
//    public BaseDelegateAdapter initRecommendList() {
//        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
//        linearLayoutHelper.setAspectRatio(2.2f);
////        linearLayoutHelper.setDividerHeight(2);
//        linearLayoutHelper.setMargin(0, 0, 0, 20);
////        linearLayoutHelper.setPadding(0, 0, 0, 10);
//        mBDA_RecommendAdapter = new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.base_item_reycler, 1, Constant.viewType.typeList_recommend) {
//            @Override
//            public void onBindViewHolder(BaseViewHolder holder,
//                                         @SuppressLint("RecyclerView") final int position) {
//                super.onBindViewHolder(holder, position);
//
//                RecyclerView recyclerView = holder.getView(R.id.recyclerView);
//                mView.initRecommendListView(recyclerView);
//            }
//        };
//        return mBDA_RecommendAdapter;
//    }


}
