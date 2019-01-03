package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.WaterfallAdapter;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.clientData.ClientDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterContract.ALL_MODE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterContract.MORE_MODE;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class ClientFilterPresenter implements ClientFilterContract.Presenter{

    String TAG = "ClientFilterPresenter";

    @Inject ClientFilterActivity parentActivity;
    @Inject ClientFilterContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    ClientDataModel model;

    @Inject SelectedAdapter mSelectedProvincesAdapter;  // 已选区域 （省份）
    @Inject ProvinceAdapter mProvinceAdapter;  // 省份展示
    @Inject CityAdapter mCityAdapter;          // 城市展示


    @Inject WaterfallAdapter mLinkAdapter;
    @Inject WaterfallAdapter mRegMoneyAdapter;
    @Inject WaterfallAdapter mRegDateAdapter;

    private int currentMode = ALL_MODE;
    private ProvinceData.Bean currentProvince;              // 当前选中的省份
    private ProvinceData.Bean lastProvince;                 // 上次选中的省份
    private List<String> currentSelectedProvinces;          // 所有选中的省份列表
    private List<ProvinceData.City> currentSelectedCities;  // 所有选中的城市列表

    // 保存 需要上传的选择的数据
    private String type = "0";              // 筛选只针对 潜在客户

    private String hasMobile = null;          // "1":选择;
    private String hasEmail = null;           // "1":选择;
    private String startRegMoney = null;      // 注册资本 - start
    private String endRegMoney = null;        // 注册资本 - end
    private String startCompanyCreated = null;// 注册时间 - start
    private String endCompanyCreated = null;  // 注册时间 - end

    private String selectedCityCodes = null;  // 选择的 cityCode,cityCode1



    @Inject
    public ClientFilterPresenter(ClientFilterActivity activity){
        this.parentActivity = activity;
        model = new ClientDataModel(parentActivity);
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
    public SelectedAdapter getSelectedProvincesAdapter() {
        return mSelectedProvincesAdapter;
    }

    @Override
    public CityAdapter getCityAdapter() {
//        if (mCityAdapter == null){
//            mCityAdapter = new CityAdapter(parentActivity);
//        }
        return mCityAdapter;
    }

    @Override
    public ProvinceAdapter getProvinceAdapter() {
        return mProvinceAdapter;
    }

    @Override
    public WaterfallAdapter getLinkWayAdapter() {
        return mLinkAdapter;
    }

    @Override
    public WaterfallAdapter getRegMoneyAdapter() {
        return mRegMoneyAdapter;
    }

    @Override
    public WaterfallAdapter getRegDateAdapter() {
        return mRegDateAdapter;
    }

    @Override
    public void getLinkWayData() {
        final String[] linkWays = parentActivity.getResources().getStringArray(R.array.link_ways);
        List<String> linkList = Arrays.asList(linkWays);
        mLinkAdapter.setDataList(linkList);

    }

    @Override
    public void getRegMoneyData() {
        final String[] regMoney = parentActivity.getResources().getStringArray(R.array.reg_money);
        List<String> moneyList = Arrays.asList(regMoney);
        mRegMoneyAdapter.setDataList(moneyList);
    }

    @Override
    public void getRegDateData() {
        final String[] regDates = parentActivity.getResources().getStringArray(R.array.reg_date);
        List<String> dateList = Arrays.asList(regDates);
        mRegDateAdapter.setDataList(dateList);
    }

    @Override
    public void setHasPhone(String hasPhone) {
        this.hasMobile = hasPhone;
    }

    @Override
    public void setHasEmail(String hasEmail) {

        this.hasEmail = hasEmail;
    }

    @Override
    public void setStartMoney(String money) {

        this.startRegMoney = money;
    }

    @Override
    public void setEndMoney(String money) {

        this.endRegMoney = money;
    }

    @Override
    public void setStartDate(String date) {

        this.startCompanyCreated = date;
    }

    @Override
    public void setEndDate(String date) {

        this.endCompanyCreated = date;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void getAllData() {
        Subscription rxSubscription = model
                .getProvinceData()
                .compose(RxUtil.<ProvinceData>rxSchedulerHelper())
                .subscribe(new Subscriber<ProvinceData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(ProvinceData data) {
                        mView.showWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null && data.data.size() > 0){
                                mProvinceAdapter.clear();
                                mProvinceAdapter.setDataList(data.data);
                                ProvinceData.ProvinceEntity firstProvince = data.data.get(0);
                                if (firstProvince.getCity() != null && firstProvince.getCity().size() > 0){
                                    mCityAdapter.clear();
                                    // change bean to City put in CityAdapter.
                                    mCityAdapter.setDataList(getCityFromBean(firstProvince.province, firstProvince.getCity()));
                                }
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);
    }


    @Override
    public void setCurrentMode(int mode) {
        if (currentMode != mode){
            currentMode = mode;
            switch (currentMode){
                case ALL_MODE:
                    mView.initAllMode();
                    break;
                case MORE_MODE:
                    mView.initMoreMode();
                    break;
            }
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setCurrentProvince(ProvinceData.Bean province) {
        if (province != null && !province.equals(currentProvince)){
            currentProvince = province;
        }
    }

    @Override
    public ProvinceData.Bean getCurrentProvince() {
        return currentProvince;
    }

    // 点击选择某项城市时，遍历当前所选 -省份- 列表 currentSelectedProvinces，
    // 该列表中没有当前省 -province 时 添加该省。
    @Override
    public void addSelectedProvince(String province) {
        if (currentSelectedProvinces == null){
            currentSelectedProvinces = new ArrayList<>();
        }
        // 不包含时 才添加
        if (!currentSelectedProvinces.contains(province)){
            currentSelectedProvinces.add(province);
            mSelectedProvincesAdapter.addData(province);
        }
    }

    // 取消某项城市选择时，遍历当前剩余的所选 -城市- 列表 currentSelectedCities，
    // 该列表中没有属于当前省 -province 的城市时 删除该省。
    @Override
    public void removeSelectedProvince(String province) {
        if (currentSelectedCities != null){
            boolean contain = false;
            // 遍历所有选择的 city - 都不属于该 province 时 才删除
            for ( int i = 0; i < currentSelectedCities.size(); i++){
                ProvinceData.City city = currentSelectedCities.get(i);
                if (TextUtils.equals(province, city.mProvince.name)){
                    contain = true;
                    LogUtils.printInfo(TAG, "removeSelectedProvince: city.mProvince.name: " + city.mProvince.name);
                    LogUtils.printInfo(TAG, "removeSelectedProvince: " + province);
                    break;
                }
            }
            // 当前选择的城市 不属于该省，并且 currentSelectedProvinces 中有该省
            if (!contain && currentSelectedProvinces.contains(province)){
                currentSelectedProvinces.remove(province);
                mSelectedProvincesAdapter.removeData(province);
            }
        }
    }

    @Override
    public void clearSelectedProvinces() {
        if (currentSelectedProvinces != null){
            currentSelectedProvinces.clear();
        }
    }

    @Override
    public void clickToDeleteProvince(String province) {
        currentSelectedProvinces.remove(province);
        if (currentSelectedCities != null){
//            LogUtils.printInfo(TAG, "currentSelectedCities: NEED DELETE CITY BELONG TO " + province);
//            for (int i =0; i< currentSelectedCities.size(); i++){
//                LogUtils.printInfo(TAG, "currentSelectedCities: " + i +" : " +currentSelectedCities.get(i).mCity.name);
//            }


            // 1. delete all cities belong to this province
            // List<ProvinceData.City> tempList = currentSelectedCities; // size will change with currentSelectedCities;
            List<ProvinceData.City> tempList = new ArrayList<>();
            tempList.addAll(currentSelectedCities);
            for ( int i = 0; i < tempList.size(); i++){
                //LogUtils.printInfo(TAG, "----------tempList: " + tempList.size());
                //LogUtils.printInfo(TAG, "----------currentSelectedCities: " + currentSelectedCities.size());
                ProvinceData.City city = tempList.get(i);
                //LogUtils.printInfo(TAG, "i---------- province: " +i+": "+ city.mProvince.name +" ||  city.mCity.name:  "+ city.mCity.name);
                if (TextUtils.equals(province, city.mProvince.name)){
                    //LogUtils.printInfo(TAG, "----------belong to province: " + city.mCity.name);
                    currentSelectedCities.remove(city);
                }
            }
//            LogUtils.printInfo(TAG, "currentSelectedCities: HAVE DELETED CITY BELONG TO " + province);
//            for (int i =0; i< currentSelectedCities.size(); i++){
//                LogUtils.printInfo(TAG, "currentSelectedCities: " + i +" : " +currentSelectedCities.get(i).mCity.name);
//            }

            // 2. delete this province - and delete it in adapter
            // 更新 CityAdapter
            mCityAdapter.setCitiesSelected(currentSelectedCities);
        }
    }

    @Override
    public List<ProvinceData.City> getCityFromBean(ProvinceData.Bean province, List<ProvinceData.Bean> beanList) {
        List<ProvinceData.City> results = new ArrayList();
        ProvinceData.City city;
        for (int i = 0; i < beanList.size(); i++){
            city = new ProvinceData.City();
            ProvinceData.Bean bean = beanList.get(i);
            city.mProvince = province;
            city.mCity = bean;
            results.add(city);
        }
        return results;
    }

    @Override
    public void addSelectedCity(ProvinceData.City city) {
        if (currentSelectedCities == null){
            currentSelectedCities = new ArrayList<>();
        }
        currentSelectedCities.add(city);
    }

    @Override
    public void removeSelectedCity(ProvinceData.City city) {
        if (currentSelectedCities != null){
            currentSelectedCities.remove(city);
        }
    }

    @Override
    public void clearSelectedCities() {
        if (currentSelectedCities != null){
            currentSelectedCities.clear();
        }
    }


    @Override
    public List<ProvinceData.City> getSelectedCity() {
        if (currentSelectedCities == null){
            currentSelectedCities = new ArrayList<>();
        }
        return currentSelectedCities;
    }

    @Override
    public String getSelectedCityCodes() {
        String result = "";
        // 遍历 currentSelectedCities，获取所有 已选城市code
        for (int i = 0; i < currentSelectedCities.size(); i++){
            //LogUtils.printInfo(TAG, "----------currentSelectedCities: " + currentSelectedCities.size());
            if (i == currentSelectedCities.size() -1){  // 列表中的最后一个
                result += currentSelectedCities.get(i).mCity.getCode();
                //LogUtils.printInfo(TAG, "-result: " + result);
            }else {
                result += currentSelectedCities.get(i).mCity.getCode() + ",";
            }
        }
        LogUtils.printInfo(TAG, "result: " + result);
        return result.trim();
    }


    @Override
    public void confirmation() {

        mView.getLinkWay();
        mView.getRegDate();
        mView.getRegMoney();

        selectedCityCodes = getSelectedCityCodes();

        Constant.hasMobile = hasMobile;          // "1":选择;  "0": 未选择
        Constant.hasEmail = hasEmail;            // "1":选择;  "0": 未选择
        Constant.startRegMoney = startRegMoney;      // 注册资本 - start
        Constant.endRegMoney = endRegMoney;          // 注册资本 - end
        Constant.startCompanyCreated = startCompanyCreated;// 注册时间 - start
        Constant.endCompanyCreated = endCompanyCreated;    // 注册时间 - end

        Constant.selectedCityCodes = selectedCityCodes;    // 选择的 cityCode,cityCode1

        LogUtils.printInfo(TAG, "hasMobile: " + hasMobile);
        LogUtils.printInfo(TAG, "hasEmail: " + hasEmail);
        LogUtils.printInfo(TAG, "startRegMoney: " + startRegMoney);
        LogUtils.printInfo(TAG, "endRegMoney: " + endRegMoney);
        LogUtils.printInfo(TAG, "startCompanyCreated: " + startCompanyCreated);
        LogUtils.printInfo(TAG, "endCompanyCreated: " + endCompanyCreated);
        LogUtils.printInfo(TAG, "selectedCityCodes: " + selectedCityCodes);

        LogUtils.printInfo(TAG, "Constant =====================: ");
        LogUtils.printInfo(TAG, "hasMobile: " + Constant.hasMobile);
        LogUtils.printInfo(TAG, "hasEmail: " + Constant.hasEmail);
        LogUtils.printInfo(TAG, "startRegMoney: " + Constant.startRegMoney);
        LogUtils.printInfo(TAG, "endRegMoney: " +  Constant.endRegMoney);
        LogUtils.printInfo(TAG, "startCompanyCreated: " + Constant.startCompanyCreated);
        LogUtils.printInfo(TAG, "endCompanyCreated: " + Constant.endCompanyCreated);
        LogUtils.printInfo(TAG, "selectedCityCodes: " + Constant.selectedCityCodes);

        // add filter conditions then go back to Client Activity
        Intent intent = new Intent(parentActivity, ClientActivity.class);
        intent.putExtra("FILTER",true);
        parentActivity.startActivity(intent);
    }

    @Override
    public void clearConditions() {

        Constant.hasMobile = "";          // "1":选择;  "0": 未选择
        Constant.hasEmail = "";           // "1":选择;  "0": 未选择
        Constant.startRegMoney = "";      // 注册资本 - start
        Constant.endRegMoney = "";        // 注册资本 - end
        Constant.startCompanyCreated = "";// 注册时间 - start
        Constant.endCompanyCreated = "";  // 注册时间 - end
        Constant.selectedCityCodes = "";  // 选择的 cityCode,cityCode1

        hasMobile = "";          // "1":选择;  "0": 未选择
        hasEmail = "";           // "1":选择;  "0": 未选择
        startRegMoney = "";      // 注册资本 - start
        endRegMoney = "";        // 注册资本 - end
        startCompanyCreated = "";// 注册时间 - start
        endCompanyCreated = "";  // 注册时间 - end
        selectedCityCodes = "";  // 选择的 cityCode,cityCode1

        mView.cleanSelected();   // 清除选择项
    }
}
