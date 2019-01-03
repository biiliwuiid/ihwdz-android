package com.ihwdz.android.hwapp.ui.orders.filter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.CityResultBean;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;
import com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseActivity;
import com.ihwdz.android.hwapp.ui.publish.address.AddressActivity;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;

import javax.inject.Inject;

import retrofit2.http.PATCH;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */
public class CityFilterPresenter implements CityFilterContract.Presenter {

    String TAG = "CityFilterPresenter";
    @Inject CityFilterActivity parentActivity;
    @Inject CityFilterContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LogisticsModel model;
    @Inject CityFilterAdapter mProvinceAdapter;
    @Inject CityFilterAdapter mProvinceAdapter2;
    @Inject CityFilterAdapter mCityAdapter;
    @Inject CityFilterAdapter mCityAdapter2;
    @Inject CityFilterAdapter mAreaAdapter;
    @Inject CityFilterAdapter mAreaAdapter2;

    private LogisticsCityData.CityEntity currentProvince; // 当前选择的 省份
    private LogisticsCityData.CityEntity currentCity;     // 当前选择的 城市
    private LogisticsCityData.CityEntity currentArea;     // 当前选择的 地区
    private CityResultBean currentProvince2;// 当前选择的 省份 - 本地Json
    private CityResultBean currentCity2;    // 当前选择的 城市 - 本地Json
    private CityResultBean currentArea2;    // 当前选择的 地区 - 本地Json

    boolean isFromAddress = false;
    boolean isVipDistrict = false;
    boolean isWarehouse = false;
    boolean isAddress = false;

    String name,email,mobile,
            provinceCode,province,
            cityCode,city,
            districtCode,district, address;

    @Inject
    public CityFilterPresenter(CityFilterActivity activity){
        this.parentActivity = activity;
        model = new LogisticsModel(parentActivity);
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
    public void setIsFromAddress(boolean bIsFromAddress) {
        this.isFromAddress = bIsFromAddress;
    }

    @Override
    public void setIsVipDistrict(boolean bIsVipDistrict) {
        this.isVipDistrict = bIsVipDistrict;
    }

    @Override
    public void setIsWarehouse(boolean bIsWarehouse) {
        this.isWarehouse = bIsWarehouse;
    }

    @Override
    public void setIsAddress(boolean bIsAddress) {
        this.isAddress = bIsAddress;
    }

    @Override
    public CityFilterAdapter getProvinceAdapter() {
        return mProvinceAdapter;
    }

    @Override
    public CityFilterAdapter getProvinceAdapter2() {
        return mProvinceAdapter2;
    }

    @Override
    public CityFilterAdapter getCityAdapter() {
        return mCityAdapter;
    }

    @Override
    public CityFilterAdapter getCityAdapter2() {
        return mCityAdapter2;
    }

    @Override
    public CityFilterAdapter getAreaAdapter() {
        return mAreaAdapter;
    }

    @Override
    public CityFilterAdapter getAreaAdapter2() {
        return mAreaAdapter2;
    }

    @Override
    public void refreshData() {

    }



    public void parseJson(){

        /**
         * JsonReader是Android 3.0引入的新解析类
         */
        try{
            InputStream is = null;
            JsonReader reader = null;
            try{
                is =  parentActivity.getAssets().open("cityList.js");
                reader = new JsonReader(new InputStreamReader(is));
                reader.beginArray();
                while (reader.hasNext()){
                    reader.beginObject();
                    String doing = "";
                    while (reader.hasNext()){
                        String name = reader.nextName();
                        if (name.equals("value")) {
                            doing += reader.nextString();
                        }
                        else if (name.equals("label") || reader.peek() != JsonToken.NULL) { // 当前获取的字段是否为：null
                            doing += reader.nextString();
                        }
                        else if (name.equals("children")) {
                            reader.beginArray();
                            while(reader.hasNext()) {
                                String name1 = reader.nextName();
                                if (name1.equals("value")) {
                                    doing += reader.nextString();
                                }
                                else if (name1.equals("label") || reader.peek() != JsonToken.NULL) { // 当前获取的字段是否为：null
                                    doing += reader.nextString();
                                }
                                else if (name1.equals("children")) {
                                    reader.beginArray();
                                    while(reader.hasNext()) {
                                        doing += reader.nextString();
                                    }
                                    reader.endArray();
                                }
                                //doing += reader.nextString();
                            }
                            reader.endArray();
                        }
                    }
                    Log.d(TAG, "doing: " + doing.toString());
                    reader.endObject();
                }
                reader.endArray();

            }finally {
                reader.close();
                is.close();

            }

        } catch (Exception e){
            Log.e(TAG, e.toString());
            //throw new RuntimeException(e);
        }
    }
    public static String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf =
                    new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    @Override
    public void getCityData() {
        LogUtils.printCloseableInfo(TAG, "================================== getCityData ");
        //
        String jsonStr = getJson("cityList.js", parentActivity);
        LogUtils.printCloseableInfo(TAG, "================================== getCityData "  + jsonStr);
        // parseJson();
        Type listType = new TypeToken<LinkedList<CityResultBean>>(){}.getType();
        Gson gson = new Gson();

        LinkedList<CityResultBean> result = gson.fromJson(jsonStr, listType);

        LogUtils.printCloseableInfo(TAG, "==================================result.size(): " + result.size());
//        for (int i = 0; i < result.size(); i++){
//            CityResultBean bean = result.get(i);
//            LogUtils.printCloseableInfo(TAG, "=======================bean: " + bean.label);
//            LogUtils.printCloseableInfo(TAG, "=======================bean.isSelected: " + bean.isSelected);
//        }
        mProvinceAdapter2.setData2List(result);
    }

    @Override
    public void getFromData() {
        Subscription rxSubscription = model
                .getFromData()
                .compose(RxUtil.<LogisticsCityData>rxSchedulerHelper())
                .subscribe(new Subscriber<LogisticsCityData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, e.toString());
                    }
                    @Override
                    public void onNext(LogisticsCityData data) {
                        if (data != null && data.data.addressList.size() > 0){
                            mProvinceAdapter.setDataList(data.data.addressList);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void getDestinationData() {
        LogUtils.printInfo(TAG, "========== getDestinationData  始发地 信息：============");
        LogUtils.printInfo(TAG, "Constant.provFrom: "+Constant.provFrom);
        LogUtils.printInfo(TAG, "Constant.cityFrom: "+Constant.cityFrom);
        LogUtils.printInfo(TAG, "Constant.distinctFrom: "+Constant.distinctFrom);

        Subscription rxSubscription = model
                .getDestinationData(Constant.provFrom, Constant.cityFrom, Constant.distinctFrom)
                .compose(RxUtil.<LogisticsCityData>rxSchedulerHelper())
                .subscribe(new Subscriber<LogisticsCityData>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, e.toString());
                    }
                    @Override
                    public void onNext(LogisticsCityData data) {
                        if (data != null && data.data.addressList.size() > 0){

                            mProvinceAdapter.setDataList(data.data.addressList);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

//    @Override
//    public void getPriceData() {
//
//    }



    @Override
    public void setCurrentProvince(LogisticsCityData.CityEntity province) {
        if (province != null && !province.equals(currentProvince)){
            currentProvince = province;
            LogUtils.printInfo(TAG, "currentProvince: " + currentProvince);
        }
    }

    @Override
    public LogisticsCityData.CityEntity getCurrentProvince() {
        return currentProvince;
    }

    @Override
    public void setCurrentProvince2(CityResultBean province) {
        currentProvince2 = province;
    }

    @Override
    public CityResultBean getCurrentProvince2() {
        return currentProvince2;
    }

    @Override
    public void setCurrentCity(LogisticsCityData.CityEntity province) {
        if (province != null && !province.equals(currentCity)){
            currentCity = province;
            LogUtils.printInfo(TAG, "currentCity: " + currentCity);
        }
    }

    @Override
    public LogisticsCityData.CityEntity getCurrentCity() {
        return currentCity;
    }

    @Override
    public void setCurrentCity2(CityResultBean province) {
        if (province != null && !province.equals(currentCity2)){
            currentCity2 = province;
        }
    }

    @Override
    public CityResultBean getCurrentCity2() {
        return currentCity2;
    }

    @Override
    public void setCurrentArea(LogisticsCityData.CityEntity province) {
        if (province != null && !province.equals(currentArea)){
            currentArea = province;
            LogUtils.printInfo(TAG, "currentArea: " + currentArea);
        }
    }

    @Override
    public LogisticsCityData.CityEntity getCurrentArea() {
        return currentArea;
    }

    @Override
    public void setCurrentArea2(CityResultBean province) {
        if (province != null && !province.equals(currentArea2)){
            currentArea2 = province;
        }
    }

    @Override
    public CityResultBean getCurrentArea2() {
        return currentArea2;
    }

    /**
     * 1.物流 需要 xxFrom & xxTo 两组地址
     */
    @Override
    public void saveAddress() {
        if (isFromAddress){
            Constant.provFrom = getCurrentProvince().text;
            Constant.cityFrom = getCurrentCity().text;
            Constant.distinctFrom = getCurrentArea().text;
        }else {
            Constant.provTo = getCurrentProvince().text;
            Constant.cityTo = getCurrentCity().text;
            Constant.distinctTo = getCurrentArea().text;
        }
    }

    /**
     * 2.会员信息 & 添加仓库 & 添加收货地址 只需要一组地址
     */
    @Override
    public void saveSelectedDistrict() {
        Constant.provinceName = currentProvince2.label;
        Constant.provinceCode = currentProvince2.value;
        Constant.cityName = currentCity2.label;
        Constant.cityCode = currentCity2.value;
        Constant.districtName = currentArea2.label;
        Constant.districtCode = currentArea2.value;

        if (isVipDistrict){
            // 会员信息
            LogUtils.printCloseableInfo(TAG, "isVipDistrict: " + isVipDistrict);
        }

        if (isWarehouse){
            // 添加仓库 -> 返回 报价
            Intent intent = new Intent(parentActivity, QuoteDetailActivity.class);
            intent.putExtra("WAREHOUSE", true);
            parentActivity.startActivity(intent);
        }

        if (isAddress){
            // 添加收货地址 -> 返回 收货地址
            Intent intent = new Intent(parentActivity, AddressActivity.class);
            intent.putExtra("ADDRESS", true);
            parentActivity.startActivity(intent);
        }
    }

//    @Override
//    public void postUserUpdate() {
//        Log.d(TAG, "postUserUpdate =========================== token:" + Constant.token);
//        updateVipInfo();
//        LoginDataModel model = new LoginDataModel(parentActivity);
//        Subscription rxSubscription = model
//                .postUserData(Constant.token,
//                        name,email,mobile,
//                        provinceCode,province,
//                        cityCode,city,
//                        districtCode,district, address
//                        //companyNature, companyType, companyFullName
//                )
//                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
//                .subscribe(new Subscriber<VerifyData>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "------- onCompleted ---------");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "------- onError ---------" + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(VerifyData data) {
//                        Log.d(TAG, "------- onNext ---------");
//                        Log.d(TAG, "------- data: " + data.msg);
//                        if ("0".equals(data.code)) {
//                            if (data.data){
//                                mView.showPromptMessage(data.msg);
//
//                            }
//                        }else {
//                            mView.showPromptMessage(data.msg);
//                        }
//                    }
//
//                });
//        mSubscriptions.add(rxSubscription);
//    }
//
//
//    public void updateVipInfo(){
//        name = Constant.name;
//        mobile = Constant.tel;
//        email = Constant.email;
//        province = Constant.provinceName;
//        provinceCode = Constant.provinceCode;
//        city = Constant.cityName;
//        cityCode = Constant.cityCode;
//        district = Constant.districtName;
//        districtCode = Constant.districtCode;
//        address = Constant.address;
//    }

}
