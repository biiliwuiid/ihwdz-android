package com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.CityResultBean;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;
import com.ihwdz.android.hwapp.ui.publish.address.AddressActivity;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseContract.MODE_ADDRESS;
import static com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseContract.MODE_ADDRESS_UPDATE;
import static com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseContract.MODE_WAREHOUSE;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :   添加新仓库 - 0 & 添加收货地址 - 1 & 修改收货地址 - 2
 * version: 1.0
 * </pre>
 */
public class WarehousePresenter implements WarehouseContract.Presenter{

    String TAG = "WarehousePresenter";
    @Inject WarehouseActivity parentActivity;
    @Inject WarehouseContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    private String cannotBeNull;
    private String currentWarehouseJson;
    private WarehouseData.WarehouseForQuotePost currentWarehouse;
    private LinkedList<WarehouseData.WarehouseForQuotePost> mWarehouseList;

    ArrayList<Province> provinces = new ArrayList<>();  // picker data.

    private String warehouseName; // 仓库名
    private int isDefaultInt;

    // 新建/修改 收货地址 （新建不需要id）
    private String address, contactName, mobile, isDefault = "0", provinceCode, provinceName, cityCode, cityName, districtCode,  districtName,  id;

    private boolean isDefaultAddress = false; // 设为默认地址
    private int currentMode;
    private boolean isComplete = true;

    private boolean isSubmitClicked = false; // 提交按钮是否已提交

    @Inject
    public WarehousePresenter(WarehouseActivity activity){
        this.parentActivity = activity;
        model = new LogisticsModel(activity);
        cannotBeNull = parentActivity.getResources().getString(R.string.cannot_be_null);
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
    public void setCurrentMode(int mode) {
        this.currentMode = mode;
        LogUtils.printCloseableInfo(TAG, "currentMode:" + currentMode);
        if (currentWarehouse == null){
            currentWarehouse = new WarehouseData.WarehouseForQuotePost();
        }
        switch (currentMode){
            case MODE_WAREHOUSE:      // 添加仓库
                mView.initWarehouseView();
                break;
            case MODE_ADDRESS:        // 添加 收货地址
                mView.initAddressView(false);

                break;
            case MODE_ADDRESS_UPDATE: // 修改 收货地址
                mView.initAddressView(true);
                break;
        }

    }

    @Override
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
        if (TextUtils.isEmpty(warehouseName)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.warehouse_null));
            return;
        }else {
            // TODO: 2018/11/27 校验仓库名是否存在
            checkWarehouse(warehouseName);
        }
    }

    @Override
    public void setLinkman(String linkman) {
        this.contactName = linkman;

    }

    @Override
    public void setTel(String tel) {
        this.mobile = tel;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    @Override
    public void selectAddress() {
        // TODO: 2018/11/27 使用底部dialog  代替 CityFilterActivity
//        boolean isWarehouse = false;
//        boolean isAddress = false;
//        switch (currentMode){
//            case MODE_WAREHOUSE:
//                isWarehouse = true;
//                break;
//            case MODE_ADDRESS:
//                isAddress = true;
//                break;
//        }
//        Intent intent = new Intent(parentActivity, CityFilterActivity.class);
//        intent.putExtra("WAREHOUSE", isWarehouse);
//        intent.putExtra("ADDRESS", isAddress);
//        parentActivity.startActivity(intent);


        /**
         * 使用底部dialog - AddressPicker
         */

        AddressPicker picker = new AddressPicker(parentActivity, provinces);
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                String provinceName = province.getName();
                String cityName = "";
                if (city != null) {
                    cityName = city.getName();
                    //忽略直辖市的二级名称
                    if (cityName.equals("市辖区") || cityName.equals("市") || cityName.equals("县")) {
                        cityName = "";
                    }
                }
                String countyName = "";
                if (county != null) {
                    countyName = county.getName();
                }

                setProvince(province.getAreaName());
                setProvinceCode(province.getAreaId());

                setCity(city.getAreaName());
                setCityCode(city.getAreaId());

                setDistrict(county.getAreaName());
                setDistrictCode(county.getAreaId());

                LogUtils.printCloseableInfo(TAG, "========================"+ province.getAreaName() + " " + province.getAreaId() );
                LogUtils.printCloseableInfo(TAG, "======================"+ city.getAreaName() + " " + city.getAreaId() );
                LogUtils.printCloseableInfo(TAG, "===================="+ county.getAreaName() + " " + county.getAreaId() );

                // mView.showPromptMessage(provinceName + " " + cityName + " " + countyName);
                mView.updateAddress(provinceName + " " + cityName + " " + countyName);
            }
        });
        picker.show();


    }

    @Override
    public void setDetailAddress(String address) {

        this.address = address;

    }

    @Override
    public void setProvince(String province) {
        this.provinceName = province;
    }

    @Override
    public void setProvinceCode(String code) {

        this.provinceCode = code;
    }

    @Override
    public void setCity(String city) {

        this.cityName = city;
    }

    @Override
    public void setCityCode(String code) {

        this.cityCode = code;
    }

    @Override
    public void setDistrict(String district) {

        this.districtName = district;
    }

    @Override
    public void setDistrictCode(String code) {

        this.districtCode = code;
    }

    @Override
    public void setAddressId(String addressId) {
        this.id = addressId;
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

    // 读取本地 省市区 信息 -》 Picker data
    @Override
    public void getLocalAddressData(){
        // LogUtils.printCloseableInfo(TAG, "================================== getAddressData ");

        String jsonStr = getJson("cityList.js", parentActivity);
        // LogUtils.printCloseableInfo(TAG, "================================== getAddressData "  + jsonStr);

        // parseJson();
        Type listType = new TypeToken<LinkedList<CityResultBean>>(){}.getType();
        Gson gson = new Gson();

        LinkedList<CityResultBean> result = gson.fromJson(jsonStr, listType);

        // LogUtils.printCloseableInfo(TAG, "================================== result.size(): " + result.size());

        Province province;
        for (int i = 0; i < result.size(); i++){
            // 省
            province = new Province();
            List<City> cities = new ArrayList<>();

            City city ;
            CityResultBean provinceBean = result.get(i);

            List<CityResultBean> cityList = provinceBean.children;

            if (cityList != null){
                for (int j = 0; j< cityList.size(); j++){
                    // 市
                    city = new City();
                    List<County> counties = new ArrayList<>();

                    County county;
                    CityResultBean cityBean = cityList.get(j);
                    List<CityResultBean> countryList = cityBean.children;
                    for (int c = 0; c < countryList.size(); c++){
                        // 区
                        county = new County();
                        CityResultBean districtBean = countryList.get(c);
                        county.setCityId(districtBean.value);
                        county.setAreaId(districtBean.value);
                        county.setAreaName(districtBean.label);

                        counties.add(county);

                    }

                    city.setCounties(counties);
                    city.setAreaName(cityBean.label);
                    city.setAreaId(cityBean.value);
                    city.setProvinceId(cityBean.value);

                    cities.add(city);
                }
            }

            province.setCities(cities);
            province.setAreaId(provinceBean.value);
            province.setAreaName(provinceBean.label);
            provinces.add(province);

        }

        // LogUtils.printCloseableInfo(TAG, "======================= Provinces size: " + provinces.size());
    }

    @Override
    public void getAddressData() {
        // currentPageNum, PageSize
        Subscription rxSubscription = model
                .getAddressData()
                .compose(RxUtil.<PublishData.AddressData>rxSchedulerHelper())
                .subscribe(new Subscriber<PublishData.AddressData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PublishData.AddressData data) {
                        LogUtils.printCloseableInfo(TAG, "====== getAddressData ==== id： " + id);
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null && data.data.size() > 0){
                                // 修改地址 - id
                                PublishData.AddressEntity addressEntity;
                                List<PublishData.AddressEntity> list = data.data;
                                for (int i = 0; i< list.size(); i++){

                                    addressEntity = list.get(i);
                                    LogUtils.printCloseableInfo(TAG, "====== getAddressData ==== id： " + addressEntity.memberDeliveryAddressId +"  | i: " + i);

                                    if (TextUtils.equals(id, addressEntity.memberDeliveryAddressId)){
                                        LogUtils.printCloseableInfo(TAG, "====== getAddressData equals ==== id： " + addressEntity.memberDeliveryAddressId +"  | i: " + i);
                                        mView.updateReceiverInfo(addressEntity);
                                        setPCD(addressEntity); // 省市区
                                    }
                                }


                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    private void setPCD(PublishData.AddressEntity data) {
        setProvince(data.provinceName);
        setProvinceCode(data.provinceCode);

        setCity(data.cityName);
        setCityCode(data.cityCode);

        setDistrict(data.districtName);
        setDistrictCode(data.districtCode);
    }

    @Override
    public void setAsDefault(boolean asDefault) {
        isDefaultAddress = asDefault;
        if (isDefaultAddress){
            isDefault = "1";
            isDefaultInt = 1;
        }else {
            isDefault = "0";
            isDefaultInt = 0;
        }
        LogUtils.printCloseableInfo(TAG, "isDefault: " + isDefault);
        LogUtils.printCloseableInfo(TAG, "isDefaultInt: " + isDefaultInt);
    }

    // 校验仓库是否存在
    @Override
    public void checkWarehouse(String warehouseName) {
        Subscription rxSubscription = model
                .getWarehouseData(warehouseName)
                .compose(RxUtil.<WarehouseData.WarehouseCheckData>rxSchedulerHelper())
                .subscribe(new Subscriber<WarehouseData.WarehouseCheckData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "校验仓库是否存在: "+ e.toString());
                    }

                    @Override
                    public void onNext(WarehouseData.WarehouseCheckData data) {

                        // TODO: 2018/11/27 已存在 弹框提示
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){

                                changeToWarehouseForPost(data.data);

                                mView.showDialog(Constant.warehouse);
                                LogUtils.printCloseableInfo(TAG, "仓库已存在！！！！");
                            }else {
                                Constant.id = null;
                            }
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 新建 存储仓库信息 暂存
    @Override
    public void storeWarehouse() {

        //mView.showPromptMessage("新建 存储仓库信息 暂存");
        Constant.warehouse = this.warehouseName;

        Constant.provinceName = this.provinceName;
        Constant.provinceCode = this.provinceCode;

        Constant.cityName = this.cityName;
        Constant.cityCode = this.cityCode;

        Constant.districtName = this.districtName;
        Constant.districtCode = this.districtCode;

        Constant.warehouseAddress = this.address;
        Constant.warehousePhone =  this.mobile;
        Constant.warehouseContact = this.contactName;

        // for post
        currentWarehouse.warehouse = this.warehouseName;

        currentWarehouse.province = this.provinceName;
        currentWarehouse.provinceCode = this.provinceCode;

        currentWarehouse.city = this.cityName;
        currentWarehouse.cityCode = this.cityCode;

        currentWarehouse.district = this.districtName;
        currentWarehouse.districtCode = this.districtCode;

        currentWarehouse.warehouseAddress = this.address;
        currentWarehouse.warehousePhone =  this.mobile;
        currentWarehouse.warehouseContact = this.contactName;

        Gson gson = new Gson();
        String json = null;
        try {
            json = gson.toJson(currentWarehouse);
        }catch (Exception e){
            LogUtils.printError(TAG, "新建仓库 - storeWarehouse: " + e.toString());
        }

        Constant.warehouseJson = json;
    }

    @Override
    public void doSubmit() {
        isSubmitClicked = true;
        LogUtils.printCloseableInfo(TAG, "currentMode: " + currentMode);
        switch (currentMode){
            case MODE_WAREHOUSE:  // 新建仓库 - 》返回 报价界面: QuoteDetailActivity
                storeWarehouse();
                if (checkIsComplete()){
                    isSubmitClicked = false;
                    parentActivity.onBackClicked(); // 返回报价页面(ChooseWHActivity 过来时 要finish掉)
                }
                break;
            case MODE_ADDRESS:       // 添加 收货地址
                if (checkIsComplete()){
                    submitNewAddress();
                    // gotoAddress();   // 返回收货地址列表 接口返回结果后再跳转
                }

                break;
            case MODE_ADDRESS_UPDATE: // 修改 收货地址
                if (checkIsComplete()){
                    submitUpdateAddress();
                    // gotoAddress(); // 返回收货地址列表 接口返回结果后再跳转
                }
        }

    }

    @Override
    public boolean checkIsComplete() {

        isComplete = true;
        // linkman
        if (TextUtils.isEmpty(contactName)){
            isComplete = false;
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.linkman)+ cannotBeNull);
            return false;
        }

        // mobile
        boolean isPhoneNum = isMobileNO(mobile);
        if (TextUtils.isEmpty(mobile)){
            isComplete = false;
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.link_tel)+ cannotBeNull);
            return false;

        }else if (!isPhoneNum){
            isComplete = false;
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_not_right));
            return false;
        }

        // 省市区
        if (TextUtils.isEmpty(provinceCode)){
            isComplete = false;
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.choose_pcd));
            return false;
        }
        // detail address
        if (TextUtils.isEmpty(address)){
            isComplete = false;
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.address_detail)+ cannotBeNull);
            return false;
        }

        return isComplete;
    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    // 提交 新增收货地址
    private void submitNewAddress() {
        LogUtils.printCloseableInfo(TAG, "submitNewAddress");
        Subscription rxSubscription = model
                .getSaveAddressData(address,contactName,mobile,isDefault,provinceCode,provinceName,cityCode,cityName,districtCode,districtName)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        isSubmitClicked = false;
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        isSubmitClicked = false;
                        mView.showPromptMessage(data.msg);
                        if (data.data){
                            gotoAddress();
                        }else {

                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 提交 修改收货地址
    private void submitUpdateAddress() {
        LogUtils.printCloseableInfo(TAG, "submitUpdateAddress");
        LogUtils.printCloseableInfo(TAG, "isDefaultInt: " + isDefaultInt);
        Subscription rxSubscription = model
                .getUpdateAddressData(address,contactName,mobile,isDefaultInt,provinceCode,provinceName,cityCode,cityName,districtCode,districtName,id)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        isSubmitClicked = false;
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        isSubmitClicked = false;
                        mView.showPromptMessage(data.msg);
                        if (data.data){
                            gotoAddress();
                        }else {

                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void gotoQuoteActivity(){
        parentActivity.onBackClicked();
    }

    // 返回收货地址列表
    @Override
    public void gotoAddress(){
        Intent intent = new Intent(parentActivity, AddressActivity.class);
        parentActivity.startActivity(intent);
        parentActivity.finish();
    }

    @Override
    public String changeToWarehouseForPost(WarehouseData.Warehouse warehouse) {
        String warehouseJson = "";

        // 已存在的仓库数据:
        Constant.id = ""+ warehouse.id;
        Constant.warehouse = warehouse.companyShortName;

        Constant.provinceName = warehouse.addressProvinceName;
        Constant.provinceCode = warehouse.addressProvinceCode;

        Constant.cityName = warehouse.addressCityName;
        Constant.cityCode = warehouse.addressCityCode;

        Constant.districtName = warehouse.addressDistrictName;
        Constant.districtCode = warehouse.addressDistrictCode;

        Constant.warehouseAddress = warehouse.address;
        Constant.warehousePhone =  warehouse.mobile;
        Constant.warehouseContact = warehouse.name;

        WarehouseData.WarehouseForQuotePost warehouseForQuotePost = new WarehouseData.WarehouseForQuotePost();
        warehouseForQuotePost.id = warehouse.id;

        warehouseForQuotePost.warehouse = warehouse.companyShortName;

        warehouseForQuotePost.provinceCode = warehouse.addressProvinceCode;
        warehouseForQuotePost.province = warehouse.addressProvinceName;
        warehouseForQuotePost.cityCode = warehouse.addressCityCode;
        warehouseForQuotePost.city = warehouse.addressCityName;
        warehouseForQuotePost.districtCode = warehouse.addressDistrictCode;
        warehouseForQuotePost.district = warehouse.addressDistrictName;

        warehouseForQuotePost.warehouseAddress = warehouse.address;
        warehouseForQuotePost.warehousePhone = warehouse.mobile;
        warehouseForQuotePost.warehouseContact = warehouse.name;

        Gson gson = new GsonBuilder().create();
        try{
            currentWarehouseJson = gson.toJson(warehouse);
            Constant.warehouseJson = currentWarehouseJson;
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.printError(TAG, "新建仓库 - 已存在 ：gson.toJson(warehouse): "+ e.toString());
        }

        LogUtils.printCloseableInfo(TAG, "新建仓库 - 已存在: "+ currentWarehouseJson);

        return currentWarehouseJson;
    }


}
