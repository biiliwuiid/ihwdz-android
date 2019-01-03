package com.ihwdz.android.hwapp.ui.home.materialpurchase;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.MaterialData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.materialData.MaterialDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.ALL_DATA;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.BRAND;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.BREED;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.CITY;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.KEYWORDS_DATA;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.MENU_HIDE;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.PageSize;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.SPEC;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :   买原料
 * version: 1.0
 * </pre>
 */
public class MaterialPresenter implements MaterialContract.Presenter {

    String TAG = "MaterialPresenter";
    @Inject MaterialActivity parentActivity;
    @Inject MaterialContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    MaterialDataModel model;

    @Inject MaterialAdapter mAdapter;
    @Inject CheckableAdapter mMenuAdapter;

//    @Inject CheckableAdapter mBreedMenuAdapter;
//    @Inject CheckableAdapter mSpecMenuAdapter;
//    @Inject CheckableAdapter mCityMenuAdapter;
//    @Inject CheckableAdapter mBrandMenuAdapter;

    int currentMenu = MENU_HIDE;   // 默认 隐藏
    int currentMode = ALL_DATA;    // 默认为 加载全部数据

    int currentPageNum = 1;

    String currentKeywords = null;
    String currentBreed = "PP";   //"PP"
    String currentSpec = null;
    String currentCity = null;
    String currentBrand = null;

    String defaultSpec = null;
    String defaultCity = null;
    String defaultBrand = null;

    List<String> mBreedList;
    List<String> mSpecList;
    List<String> mCityList;
    List<String> mBrandList;
    List<MaterialData.CheckableItem> mMenuList;

    private String currentMenuSelected = null;

    @Inject
    public MaterialPresenter(MaterialActivity activity){
        this.parentActivity = activity;
        model = new MaterialDataModel(parentActivity);
        defaultSpec = parentActivity.getResources().getString(R.string.spec_ma);
        defaultCity = parentActivity.getResources().getString(R.string.city_ma);
        defaultBrand = parentActivity.getResources().getString(R.string.brand_ma);
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
    public MaterialAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public CheckableAdapter getMenuAdapter() {
        return mMenuAdapter;
    }


    @Override
    public void refreshData() {
        mAdapter.clear();
        setCurrentPageNum(1);
        switch (currentMode){
            case ALL_DATA:
                getAllData();
                break;
            case KEYWORDS_DATA:
                getSearchData(currentPageNum, PageSize);
                break;
        }

    }

    @Override
    public void getAllData() {
        LogUtils.printInfo(TAG, "================ getAllData === currentBreed: "+currentBreed);
        LogUtils.printInfo(TAG, "================ getAllData === currentSpec: "+currentSpec);
        LogUtils.printInfo(TAG, "================ getAllData === currentCity: "+currentCity);
        LogUtils.printInfo(TAG, "================ getAllData === currentBrand: "+currentBrand);
        LogUtils.printInfo(TAG, "================ getAllData === currentPageNum: "+currentPageNum);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getAllData(currentBreed, currentSpec, currentCity, currentBrand, currentPageNum, PageSize)
                .compose(RxUtil.<MaterialData>rxSchedulerHelper())
                .subscribe(new Subscriber<MaterialData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MaterialData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){

                                ///////////////////////////////////////////////////////////////////// start update Menu data
                                /**
                                 * breed list为空时加载数据 否则 只有选择项不属于该菜单时 加载数据
                                 */
                                if (data.data.breeds != null && data.data.breeds.size() > 0){
                                    if (mBreedList == null || mBreedList.size() == 0){
                                        mBreedList = data.data.breeds;
                                    }else if (currentMenu != BREED){
                                        mBreedList.clear();
                                        mBreedList = data.data.breeds;
                                    }
                                    LogUtils.printInfo(TAG, "mBreedList: " + mBreedList.size());
                                }
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
                                 * city
                                 */
                                if (data.data.cities != null && data.data.cities.size() > 0){
                                    if (mCityList == null || mCityList.size() == 0){
                                        mCityList = data.data.cities;
                                    }else if (currentMenu != CITY){
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
                                if (data.data.pagenation.recordList!= null && data.data.pagenation.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mAdapter.setDataList(data.data.pagenation.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.pagenation.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    // 第一次 加载就没有数据 - 提示没有数据
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }

                            }else {
                                mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    @Override
    public void getSearchData(int pageNum, int pageSize) {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getSearchData(currentKeywords, pageNum, PageSize)
                .compose(RxUtil.<MaterialData>rxSchedulerHelper())
                .subscribe(new Subscriber<MaterialData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");

                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString()); //  提示信息(e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(MaterialData data) {
                        mView.hideWaitingRing();
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                mView.hideEmptyView();
                                mView.hideKeyboard();
                                if (data.data.pagenation.recordList != null && data.data.pagenation.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        LogUtils.printInfo(TAG, "===========  getSearchData  =========== currentPageNum == 1");
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.pagenation.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.pagenation.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    // 第一次 加载就没有数据 - 提示没有数据
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }
                            }else {
                                mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                            mView.showEmptyView();
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
    public void setCurrentMode(int mode) {
        if (currentMode != mode){
            currentMode = mode;
            currentPageNum = 1;
            updateData();
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setCurrentMenu(int menu) {
//        LogUtils.printInfo(TAG, "lastMenu: " + currentMenu);
         if (currentMenu != menu){
            currentMenu = menu;
//             LogUtils.printInfo(TAG, "currentMenu: " + currentMenu);
            if (currentMenu == MENU_HIDE){
                mView.hideMenuView();
            }else {

                // 更新 菜单栏选项卡数据
                switch (currentMenu){
                    case BREED:
                        mMenuList = getCheckableData(mBreedList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mBreedMenuAdapter: " + mMenuList.size());

                        mMenuAdapter.setDataList(currentBreed, mMenuList);
                        break;
                    case SPEC:
                        mMenuList = getCheckableData(mSpecList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mSpecMenuAdapter: " + mMenuList.size());

                        mMenuAdapter.setDataList(currentSpec, mMenuList);
                        break;
                    case CITY:
                        mMenuList = getCheckableData(mCityList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mCityMenuAdapter: " + mMenuList.size());
//                        for (int i = 0; i < mMenuList.size(); i++){
//                            LogUtils.printInfo(TAG, " mCityMenuAdapter: " + mMenuList.get(i).name +"  i: " + i);
//                        }
                        mMenuAdapter.setDataList(currentCity, mMenuList);
                        break;
                    case BRAND:
                        mMenuList = getCheckableData(mBrandList);
                        LogUtils.printInfo(TAG, "Adapter.setDataList  mBrandMenuAdapter: " + mMenuList.size());

                        mMenuAdapter.setDataList(currentBrand, mMenuList);
                        break;
                }

                mView.showMenuView();
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
            case BREED:
                currentBreed = menuSelected;
                mView.updateBreedTitle(menuSelected);
                break;
            case SPEC:
                if (TextUtils.equals(menuSelected, defaultSpec)){
                    currentSpec = null;
                }else {
                    currentSpec = menuSelected;
                }
                mView.updateSpecTitle(menuSelected);
                break;
            case CITY:
                if (TextUtils.equals(menuSelected, defaultCity)){
                    currentCity = null;
                }else {
                    currentCity = menuSelected;
                }

                mView.updateCityTitle(menuSelected);
                break;
            case BRAND:
                if (TextUtils.equals(menuSelected, defaultBrand)){
                    currentBrand = null;
                }else {
                    currentBrand = menuSelected;
                }
                mView.updateBrandTitle(menuSelected);
                break;
        }
        updateData();

    }

    // 为菜单列表 添加check
    List<MaterialData.CheckableItem> getCheckableData(List<String> list){
        List<MaterialData.CheckableItem> results = new ArrayList();
        MaterialData.CheckableItem item;

        for (int i = 0; i < list.size(); i++){
            item = new MaterialData.CheckableItem();
            String str = list.get(i);
            item.name = str;
            item.isChecked = false;
            results.add(item);
        }

        if (currentMenu != BREED){
            item = new MaterialData.CheckableItem();
            switch (currentMenu){
                case SPEC:
                    item.name = defaultSpec;
                    item.isChecked = false;
                    results.add(0, item);
                    break;
                case CITY:
                    item.name = defaultCity;
                    item.isChecked = false;
                    results.add(0, item);
                    break;
                case BRAND:
                    item.name = defaultBrand;
                    item.isChecked = false;
                    results.add(0, item);
                    break;
            }
        }

        return results;
    }

    public void updateData(){
        setCurrentPageNum(1);
        getAllData();
    }

    @Override
    public void setSearchKeywords(String keywords) {
        currentKeywords = keywords;
        currentMode = KEYWORDS_DATA;
    }

}
