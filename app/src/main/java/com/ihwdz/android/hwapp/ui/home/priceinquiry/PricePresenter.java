package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import android.os.Bundle;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PriceData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.collections.PriceCollectionActivity;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.priceData.PriceDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.AREA;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.BRAND;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.BREED;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.FACTORY_PRICE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.MARKET_PRICE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.MENU_DATE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.MENU_HIDE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.PageNum;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.PageSize;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.SPEC;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :  查价格
 * version: 1.0
 * </pre>
 */
public class PricePresenter implements PriceInquiryContract.Presenter {

    @Inject PriceInquiryActivity parentActivity;
    @Inject PriceInquiryContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    PriceDataModel model ;

    @Inject BreedAdapter mBreedAdapter;           // breed 菜单
    @Inject PriceMenuAdapter mMenuAdapter;        // 菜单选项卡（型号/厂家/销售地）
    @Inject PriceAdapter mPriceAdapter;           // 价格数据

    String TAG = "PriceInquiryPresenter";

    int currentPageNum = 1;
    int currentMode = MARKET_PRICE;// 价格默认 市场价
    int currentMenu = MENU_HIDE;   // 菜单默认 隐藏

    boolean mIsLoading = false;    // 正在加载数据

    String currentBreed = "PP";    //"PP"
    String currentSpec = null;     // 型号
    String currentBrand = null;    // 厂家
    String currentArea = null;     // 销售地 (mCityList)
    String currentDate = null;     // date
    String mSelectedDate = null;   // 默认选择日期 后台提供


    String defaultSpec = null;  // 型号
    String defaultBrand = null; // 厂家
    String defaultArea = null;  // 销售地

    List<String> mBreedList;
    List<String> mSpecList;   // 型号
    List<String> mBrandList;  // 厂家
    List<String> mCityList;   // 销售地

    List<PriceData.CheckableItem> mMenuList;

    // 收藏
    private int currentCollectionType = 0;        // 0 市场价 1 出厂价
    private String currentCollectBreed = null;
    private String currentsCollectSpec = null;
    private String currentCollectBrand = null;
    private String currentCollectArea = null;


    @Inject
    public PricePresenter(PriceInquiryActivity activity) {
        this.parentActivity = activity;
        model = PriceDataModel.getInstance(parentActivity);
        defaultSpec = parentActivity.getResources().getString(R.string.spec_p);
        defaultBrand = parentActivity.getResources().getString(R.string.brand_p);
        defaultArea = parentActivity.getResources().getString(R.string.area_p);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(parentActivity != null){
            parentActivity = null;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void store(Bundle outState) {

    }

    @Override
    public void restore(Bundle inState) {

    }

    @Override
    public BreedAdapter getBreedAdapter() {
        return mBreedAdapter;
    }

    @Override
    public PriceMenuAdapter getMenuAdapter() {
        return mMenuAdapter;
    }

    @Override
    public PriceAdapter getAdapter() {
        return mPriceAdapter;
    }


    @Override
    public void refreshData() {
        mPriceAdapter.clear();
        setCurrentPageNum(1);
        if (!mIsLoading){
            getBreedAdapter();
            getPriceData();
        }

    }

    @Override
    public void updateData() {
        mPriceAdapter.clear();
        setCurrentPageNum(1);
        if (!mIsLoading){
            getPriceData();
        }
    }

    // breed data
    @Override
    public void getBreedData() {
        Subscription rxSubscription = model
                // .getMarketPriceData(PageNum, PageSize, null, null, null, null, mStartDate)
                .getMarketPriceData1(PageNum, PageSize, null, null, null, null, currentDate)
                .compose(RxUtil.<PriceData>rxSchedulerHelper())
                .subscribe(new Subscriber<PriceData>(){
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mBreedAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PriceData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if(data.data.dateTimeStr != null && data.data.dateTimeStr.length() > 0){
                                setCurrentDate(data.data.dateTimeStr);
                                mSelectedDate = data.data.dateTimeStr;
                            }

                            LogUtils.printCloseableInfo(TAG, "getBreedData  onNext: mSelectedDate: "+ mSelectedDate);
                            if (data.data.brands != null){
                                mBreedAdapter.clear();
                                mBreedAdapter.setDataList(data.data.breeds);
                            }else {
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                            LogUtils.printError(TAG, "getBreedData  onNext: "+ data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    @Override
    public void getPriceData() {
        LogUtils.printError(TAG, "================ getPriceData === currentMode: "+ currentMode);
        if (currentMode == MARKET_PRICE){
            getMarketPriceData();
        } else {
            getFactoryPriceData();
        }
    }


    // 市场价  MarketPriceData -> PriceData
    @Override
    public void getMarketPriceData() {
        LogUtils.printInfo(TAG, "================ getMarketPriceData === currentPageNum: "+currentPageNum);
        LogUtils.printInfo(TAG, "================ getMarketPriceData === currentBreed: "+currentBreed);
        LogUtils.printInfo(TAG, "================ getMarketPriceData === currentSpec: "+currentSpec);
        LogUtils.printInfo(TAG, "================ getMarketPriceData === currentArea: "+currentArea);
        LogUtils.printInfo(TAG, "================ getMarketPriceData === currentBrand: "+currentBrand);
        LogUtils.printInfo(TAG, "================ getMarketPriceData === currentDate: "+ currentDate);

        mIsLoading = true;
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getMarketPriceData1(currentPageNum, PageSize, currentBreed, currentSpec, currentArea, currentBrand, currentDate)
                .compose(RxUtil.<PriceData>rxSchedulerHelper())
                .subscribe(new Subscriber<PriceData>(){
                    @Override
                    public void onCompleted() {
                        mIsLoading = false;
                        mView.hideWaitingRing();
                        mPriceAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        mBreedAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIsLoading = false;
                        mView.hideWaitingRing();
                        e.printStackTrace();
                        mPriceAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(PriceData data) {

                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                mView.hideEmptyView();
                                ///////////////////////////////////////////////////////////////////// start update Menu data
                                /**
                                 * breed list为空时加载数据 否则 只有选择项不属于该菜单时 加载数据
                                 */
//                                if (data.data.breeds != null && data.data.breeds.size() > 0){
//                                    if (mBreedList == null || mBreedList.size() == 0){
//                                        mBreedList = data.data.breeds;
//                                        if (mBreedAdapter.getItemCount() < 1){
//                                            LogUtils.printInfo(TAG, "mBreedAdapter.setDataList: " + mBreedList.size());
//                                            mBreedAdapter.setDataList(data.getData().getBreeds());
//                                        }
//
//                                    }else if (currentMenu != BREED){
//                                        mBreedList.clear();
//                                        mBreedList = data.data.breeds;
//                                    }
//                                    LogUtils.printInfo(TAG, "mBreedList: " + mBreedList.size());
//                                }

                                /**
                                 * specs
                                 */
                                if (data.data.specs != null && data.data.specs.size() > 0){
                                    if (mSpecList == null || mSpecList.size() == 0){
                                        mSpecList = data.data.specs;
                                    }else if (currentMenu != SPEC){
                                        mSpecList.clear();
                                        mSpecList = data.data.specs;
                                    }
                                    LogUtils.printInfo(TAG, "mSpecList: " + mSpecList.size());
                                }
                                /**
                                 * city -data.data.cities
                                 */
                                if (data.data.cities != null && data.data.cities.size() > 0){
                                    if (mCityList == null || mCityList.size() == 0){
                                        mCityList = data.data.cities;
                                    }else if (currentMenu != AREA){
                                        mCityList.clear();
                                        mCityList = data.data.cities;
                                    }

                                    LogUtils.printInfo(TAG, "mCityList: " + mCityList.size());
                                }
                                /**
                                 * brand
                                 */
                                if (data.data.brands != null && data.data.brands.size() > 0){
                                    if (mBrandList == null || mBrandList.size() == 0){
                                        mBrandList = data.data.brands;
                                    }else if (currentMenu != BRAND){
                                        mBrandList.clear();
                                        mBrandList = data.data.brands;
                                    }

                                    LogUtils.printInfo(TAG, "mBrandList: " + mBrandList.size());
                                }
                                ///////////////////////////////////////////////////////////////////// end update Menu data

                                if (data.data.productPrices!= null && data.data.productPrices.size() > 0){
                                    mPriceAdapter.setCurrentPriceType(Constant.priceType.typeMarket);
                                    if (currentPageNum == 1){
                                        mPriceAdapter.clear();
                                        mPriceAdapter.setDataList(data.data.productPrices);

                                    } else {
                                        mPriceAdapter.addDataList(data.data.productPrices);
                                    }
                                    currentPageNum ++;
                                }else {
                                    // 第一次 加载就没有数据 - 提示没有数据
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mPriceAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }
                            }

                        }
                        mIsLoading = false;
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 出厂价 FactoryPriceData -> PriceData
    @Override
    public void getFactoryPriceData() {
        LogUtils.printInfo(TAG, "================ getFactoryPriceData === currentPageNum: "+currentPageNum);
        LogUtils.printInfo(TAG, "================ getFactoryPriceData === currentBreed: "+currentBreed);
        LogUtils.printInfo(TAG, "================ getFactoryPriceData === currentSpec: "+currentSpec);
        LogUtils.printInfo(TAG, "================ getFactoryPriceData === currentArea: "+currentArea);
        LogUtils.printInfo(TAG, "================ getFactoryPriceData === currentBrand: "+currentBrand);
        LogUtils.printInfo(TAG, "================ getFactoryPriceData === currentDate: "+currentDate);

        mIsLoading = true;
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getFactoryPriceData1(currentPageNum, PageSize, currentBreed, currentSpec, currentArea, currentBrand, currentDate)
                .compose(RxUtil.<PriceData>rxSchedulerHelper())
                .subscribe(new Subscriber<PriceData>(){
                    @Override
                    public void onCompleted() {
                        mIsLoading = false;
                        mView.hideWaitingRing();
                        mPriceAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIsLoading = false;
                        mView.hideWaitingRing();
                        e.printStackTrace();
                        mPriceAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(PriceData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                ///////////////////////////////////////////////////////////////////// start update Menu data
                                /**
                                 * breed list为空时加载数据 否则 只有选择项不属于该菜单时 加载数据
                                 */
//                                if (data.data.breeds != null && data.data.breeds.size() > 0){
//                                    if (mBreedList == null || mBreedList.size() == 0){
//                                        mBreedList = data.data.breeds;
//                                        if (mBreedAdapter.getItemCount() < 1){
//                                            LogUtils.printInfo(TAG, "mBreedAdapter.setDataList: " + mBreedList.size());
//                                            mBreedAdapter.setDataList(data.getData().getBreeds());
//                                        }
//                                    }else if (currentMenu != BREED){
//                                        mBreedList.clear();
//                                        mBreedList = data.data.breeds;
//                                    }
//                                    LogUtils.printInfo(TAG, "mBreedList: " + mBreedList.size());
//                                }

                                /**
                                 * specs
                                 */
                                if (data.data.specs != null && data.data.specs.size() > 0){
                                    if (mSpecList == null || mSpecList.size() == 0){
                                        mSpecList = data.data.specs;
                                    }else if (currentMenu != SPEC){
                                        mSpecList.clear();
                                        mSpecList = data.data.specs;
                                    }
//                                    LogUtils.printInfo(TAG, "mSpecList: " + mSpecList.size());
                                }
                                /**
                                 * city -data.data.regions
                                 */
                                if (data.data.regions != null && data.data.regions.size() > 0){
                                    if (mCityList == null || mCityList.size() == 0){
                                        mCityList = data.data.regions;
                                    }else if (currentMenu != AREA){
                                        mCityList.clear();
                                        mCityList = data.data.regions;
                                    }

//                                    LogUtils.printInfo(TAG, "mCityList: " + mCityList.size());
                                }
                                /**
                                 * brand
                                 */
                                if (data.data.brands != null && data.data.brands.size() > 0){
                                    if (mBrandList == null || mBrandList.size() == 0){
                                        mBrandList = data.data.brands;
                                    }else if (currentMenu != BRAND){
                                        mBrandList.clear();
                                        mBrandList = data.data.brands;
                                    }

//                                    LogUtils.printInfo(TAG, "mBrandList: " + mBrandList.size());
                                }

                                ///////////////////////////////////////////////////////////////////// end update Menu data

                                if (data.data.productPrices!= null && data.data.productPrices.size() > 0){
                                    mPriceAdapter.setCurrentPriceType(Constant.priceType.typeFactory);
                                    if (currentPageNum == 1){
                                        mPriceAdapter.clear();
                                        mPriceAdapter.setDataList(data.data.productPrices);
                                        // mPriceAdapter.clearFactoryData();
                                        // mPriceAdapter.setFactoryDataList(data.data.productPrices);

                                    } else {
                                        mPriceAdapter.addDataList(data.data.productPrices);
                                        // mPriceAdapter.setFactoryDataList(data.data.productPrices);
                                    }
                                    currentPageNum ++;
                                }else {
                                    // 第一次 加载就没有数据 - 提示没有数据
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mPriceAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }

                            }
                        }
                        mIsLoading = false;
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 收藏成功后更新数据
    @Override
    public void doCollect(int collectionType, String breed, String spec, String brand, String area) {
//        LogUtils.printCloseableInfo(TAG, "collectionType: " + collectionType);
//        LogUtils.printCloseableInfo(TAG, "breed: " + breed);
//        LogUtils.printCloseableInfo(TAG, "spec: " + spec);
//        LogUtils.printCloseableInfo(TAG, "brand: " + brand);
//        LogUtils.printCloseableInfo(TAG, "area: " + area);
//        LogUtils.printCloseableInfo(TAG, "====================== doCollect: ");
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getPriceCollect(collectionType, breed, spec, brand, area)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        mView.hideWaitingRing();
//                        LogUtils.printCloseableInfo(TAG, "doCollect: onNext: " + data.msg);
                        if (TextUtils.equals("0", data.code)){
                            if (data.data){
                                updateData();
                            }else {
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    @Override
    public void setCurrentPageNum(int pageNum) {
        this.currentPageNum = pageNum;
    }

    @Override
    public boolean getIsLoadingData() {
        return mIsLoading;
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    // 0 市场价 1 出厂价
    @Override
    public void setCurrentMode(int mode) {

        if (currentMode != mode){
            currentMode = mode;
            switch (currentMode){
                case MARKET_PRICE:
                    currentCollectionType = 0;
                    mView.initMarketPriceView();
                    break;
                case FACTORY_PRICE:
                    currentCollectionType = 1;
                    mView.initFactoryPriceView();
                    break;
            }
            refreshData();
        }

    }

    // 当前选择的 Breed
    @Override
    public void setCurrentBreed(String str) {
        if (str != null && !str.equals(currentBreed)){
            currentBreed = str;
            updateData();
        }
    }

    @Override
    public void setCurrentMenu(int menu) {
        // LogUtils.printInfo(TAG, "lastMenu: " + currentMenu);
        if (currentMenu != menu){
            currentMenu = menu;
            // LogUtils.printInfo(TAG, "currentMenu: " + currentMenu);

            if (currentMenu == MENU_HIDE){
                mView.hideMenuView();
            }else if (currentMenu == MENU_DATE){
                //  2018/12/21   时间选项卡
                mView.hideMenuView();
                selectDate();
            }else {
                // 更新 菜单栏选项卡数据 （型号/厂家/销售地）
                mView.showMenuView();
                switch (currentMenu){
//                    case BREED:
//                        mMenuList = getCheckableData(mBreedList);
//                        LogUtils.printInfo(TAG, "Adapter.setDataList  mBreedMenuAdapter: " + mMenuList.size());
                        //mMenuAdapter.setDataList(currentBreed, mMenuList);
//                        break;
                    case SPEC:
                        mMenuList = getCheckableData(mSpecList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mSpecMenuAdapter: " + mMenuList.size());
                        mMenuAdapter.setDataList(currentSpec, mMenuList);
                        break;

                    case BRAND:
                        mMenuList = getCheckableData(mBrandList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mBrandMenuAdapter: " + mMenuList.size());
                        mMenuAdapter.setDataList(currentBrand, mMenuList);
                        break;

                    case AREA:
                        mMenuList = getCheckableData(mCityList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mAreaMenuAdapter: " + mMenuList.size());
//                        for (int i = 0; i < mMenuList.size(); i++){
//                            LogUtils.printInfo(TAG, " mAreaMenuAdapter: " + mMenuList.get(i).name +"  i: " + i);
//                        }
                        mMenuAdapter.setDataList(currentArea, mMenuList);
                        break;
                }

//                mView.showMenuView();
            }
        }
    }

    @Override
    public int getCurrentMenu() {
        return currentMenu;
    }

    @Override
    public void setCurrentMenuSelected(String menuSelected) {
        mView.hideMenuView();
        switch (currentMenu){
//            case BREED:
//                currentBreed = menuSelected;
//                 mView.updateBreedTitle(menuSelected);
//                break;
            case SPEC:
                if (TextUtils.equals(menuSelected, defaultSpec)){
                    currentSpec = null;
                }else {
                    currentSpec = menuSelected;
                }
                mView.updateSpecTitle(menuSelected);
                break;
            case BRAND:
                if (TextUtils.equals(menuSelected, defaultBrand)){
                    currentBrand = null;
                }else {
                    currentBrand = menuSelected;
                }
                mView.updateBrandTitle(menuSelected);
                break;
            case AREA:
                if (TextUtils.equals(menuSelected, defaultArea)){
                    currentArea = null;
                }else {
                    currentArea = menuSelected;
                }
                mView.updateCityTitle(menuSelected);
                break;

        }
        updateData();

    }

    // 选择日期 当前日期 后台返回；开始日期 3年前；结束日期 当天；
    @Override
    public void selectDate() {
        // 当日日期
        int currentYear, currentMonth, currentDay;
        int todayYear, todayMonth, todayDay;
        String todayStr = DateUtils.getDateTodayString();
        //LogUtils.printCloseableInfo(TAG, todayStr);

        String[] todayDate = todayStr.split("-");
        todayYear = Integer.valueOf(todayDate[0]);
        todayMonth = Integer.valueOf(todayDate[1]);
        todayDay = Integer.valueOf(todayDate[2]);

        currentYear = Integer.valueOf(todayDate[0]);
        currentMonth = Integer.valueOf(todayDate[1]);
        currentDay = Integer.valueOf(todayDate[2]);

        String[] currentDate = null;
        if (mSelectedDate != null && mSelectedDate.contains("-")){
            currentDate = mSelectedDate.split("-");
            currentYear = Integer.valueOf(currentDate[0]);
            currentMonth = Integer.valueOf(currentDate[1]);
            currentDay = Integer.valueOf(currentDate[2]);
        }


        final DatePicker picker = new DatePicker(parentActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(parentActivity, 10));

        picker.setRangeEnd(todayYear , todayMonth, todayDay);
        picker.setRangeStart(todayYear-3, todayMonth, todayDay);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);

        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                setCurrentDate(year + "-" + month + "-" + day);

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.setLabel(" "," ", " ");
        picker.setTitleText(currentYear + "-" + currentMonth + "-" + currentDay);
        picker.show();
    }

    // 选择日期后 更新日期 ；重新加载数据；（getBreedData() 后首次设置）
    @Override
    public void setCurrentDate(String date) {
        if (TextUtils.isEmpty(currentDate) || !TextUtils.equals(currentDate, date) ){
            currentDate = date;
            mView.updateDateTitle(currentDate);
            updateData();
        }

    }

    // 我的收藏
    @Override
    public void gotoMyCollection() {
        PriceCollectionActivity.startPriceCollectionActivity(parentActivity);
    }

    // 登录注册
    @Override
    public void gotoLoginPage() {
        LoginPageActivity.startLoginPageActivity(parentActivity);
    }


    // 为菜单列表 添加check
    List<PriceData.CheckableItem> getCheckableData(List<String> list){
        List<PriceData.CheckableItem> results = new ArrayList();
        PriceData.CheckableItem item;

        for (int i = 0; i < list.size(); i++){
            item = new PriceData.CheckableItem();
            String str = list.get(i);
            item.name = str;
            item.isChecked = false;
            results.add(item);
        }

        if (currentMenu != BREED){
            item = new PriceData.CheckableItem();
            switch (currentMenu){
                case SPEC:
                    item.name = defaultSpec;
                    item.isChecked = false;
                    results.add(0, item);
                    break;
                case BRAND:
                    item.name = defaultBrand;
                    item.isChecked = false;
                    results.add(0, item);
                    break;
                case AREA:
                    item.name = defaultArea;
                    item.isChecked = false;
                    results.add(0, item);
                    break;
            }
        }

        return results;
    }


//    @Override
//    public List<String> getSpecList() {
//        if (mSpecList == null){
//            mSpecList = new ArrayList<>();
//        }
//        return mSpecList;
//    }
//
//    @Override
//    public List<String> getAreaList() {
//        if (mCityList == null){
//            mCityList = new ArrayList<>();
//        }
//        return mCityList;
//    }
//
//    @Override
//    public List<String> getBrandList() {
//        if (mBrandList == null){
//            mBrandList = new ArrayList<>();
//        }
//        return mBrandList;
//    }

}
