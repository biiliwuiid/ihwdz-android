package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;
import com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import java.lang.reflect.Type;
import java.util.LinkedList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :   报价 -> 选择仓库
 * version: 1.0
 * </pre>
 */
public class ChooseWHPresenter implements ChooseWHContract.Presenter{

    String TAG = "ChooseWHPresenter";
    @Inject ChooseWHActivity parentActivity;
    @Inject ChooseWHContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    IAppSettings settings;

    @Inject HistoryWarehouseAdapter mHistoryAdapter;
    @Inject WarehouseAdapter mWarehouseAdapter;

    private String currentWarehouseJson;
    private String currentWarehouseName;
    private WarehouseData.WarehouseForQuotePost currentWarehouse;
    private LinkedList<WarehouseData.WarehouseForQuotePost> mWarehouseList;
    //private List<WarehouseData.WarehouseForQuotePost> mWarehouseList;

    @Inject
    public ChooseWHPresenter(ChooseWHActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);
        settings = SlimAppSettings.getInstance(activity);
        //settings = new SlimAppSettings(activity);
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

    // 退出 仓库选择页面时 记录历史数据(从历史数据中选择 则不变)
    @Override
    public void stop() {
        LogUtils.printCloseableInfo(TAG, "================== stop store ====================");
        //  报价点击之后再存储历史记录
//        if (currentWarehouse != null && currentWarehouse.warehouse != null && currentWarehouse.warehouse.length() > 0){
//            LogUtils.printCloseableInfo(TAG, "================== stop store currentWarehouse: " + currentWarehouse.warehouse);
//            if (mWarehouseList.size() <= 3){
//                mWarehouseList.add(currentWarehouse);
//            }else {
//                mWarehouseList.remove(0);
//                mWarehouseList.add(currentWarehouse);
//            }
//            // 转Json
//            // TODO: 2018/12/3  转Json
//            Gson gson = new Gson();
//            String warehouseListJson=null;
//            try {
//                warehouseListJson = gson.toJson(mWarehouseList);
//            }catch (Exception e){
//                LogUtils.printError(TAG, "gson.toJson(mWarehouseList): "+ e.toString());
//            }
//            LogUtils.printCloseableInfo(TAG, "================== stop store warehouseListJson: " + warehouseListJson);
//            settings.storeHistoryWarehouse(warehouseListJson);
//        }
    }

    @Override
    public void store(Bundle outState) {


    }

    @Override
    public void restore(Bundle inState) {

    }



    @Override
    public void setCurrentKeyWord(String keyWord) {
        this.currentWarehouseName = keyWord;
    }

    // 获取 精确仓库数据 - 用于校验 历史记录里的仓库是否存在 (选中某项历史记录时调用)
    // 仓库有该条数据，使用仓库数据；并且更新本地历史记录（need it? 每次都要校验 -> needn't）。否则使用本地数据
    // 设置 currentWarehouseJson 后返回；显示error 数据后返回
    @Override
    public void getWarehouseData() {
        Subscription rxSubscription = model
                .getWarehouseData(currentWarehouseName)
                .compose(RxUtil.<WarehouseData.WarehouseCheckData>rxSchedulerHelper())
                .subscribe(new Subscriber<WarehouseData.WarehouseCheckData>() {
                    @Override
                    public void onCompleted() {
                        parentActivity.onBackClicked();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                       // mView.showPromptMessage(e.toString());
                        LogUtils.printError(TAG, "获取 精确仓库数据: onError: "+ e.toString());
                        parentActivity.onBackClicked();

                    }

                    @Override
                    public void onNext(WarehouseData.WarehouseCheckData data) {

                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                // 仓库有该条数据，使用仓库数据；并且更新本地历史记录（add: id）。否则使用本地数据
                                WarehouseData.Warehouse warehouse = data.data;
                                // 改为 WarehouseForQuotePost
                                changeToWarehouseForPost(warehouse);

                            }else {

                                // 仓库没有该条数据，使用本地数据

                            }

                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 获取 模糊仓库数据
    @Override
    public void getFuzzyWarehouseData() {
        Subscription rxSubscription = model
                .getFuzzyWarehouseData(currentWarehouseName)
                .compose(RxUtil.<WarehouseData>rxSchedulerHelper())
                .subscribe(new Subscriber<WarehouseData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(WarehouseData data) {

                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null && data.data.size() > 0){
                                mWarehouseAdapter.clear();
                                mWarehouseAdapter.setDataList(data.data);
                            }

                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 获取 历史仓库数据 进入该页面时
    @Override
    public void getHistoryWarehouse() {
        String historyJson = settings.getHistoryWarehouse();
        LogUtils.printCloseableInfo(TAG, "historyJson = settings.getHistoryWarehouse(): " + historyJson);
        Type listType = new TypeToken<LinkedList<WarehouseData.WarehouseForQuotePost>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<WarehouseData.WarehouseForQuotePost> result = null;

        try{
            result = gson.fromJson(historyJson, listType);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.printError(TAG, "获取 历史仓库数据: gson.fromJson Exception: "+ e.toString());
        }

        if (mWarehouseList == null){
            mWarehouseList = new LinkedList<>();
        }

        mWarehouseList.clear();

        if (result != null && result.size() > 0){
            mWarehouseList.addAll(result);
            mHistoryAdapter.setDataList(result);

            LogUtils.printCloseableInfo(TAG, "getHistoryWarehouse - result.size: " + result.size());
            LogUtils.printCloseableInfo(TAG, "getHistoryWarehouse - mWarehouseList: " + mWarehouseList);

        }else {
            mView.showEmptyHistory();
        }

    }

    @Override
    public HistoryWarehouseAdapter getHistoryAdapter() {
        return mHistoryAdapter;
    }

    @Override
    public WarehouseAdapter getWarehouseAdapter() {
        return mWarehouseAdapter;
    }

    // 添加新仓库
    @Override
    public void addWarehouse() {
        // 添加新仓库 - 0 & 添加收货地址 - 1 & 修改收货地址 - 2
        WarehouseActivity.startWarehouseActivity(parentActivity,0,null);
        parentActivity.finish();
    }

    @Override
    public void setCurrentWarehouse(WarehouseData.WarehouseForQuotePost warehouseForQuotePost) {
        this.currentWarehouse = warehouseForQuotePost;
        // 选中的仓库数据（HistoryWarehouseAdapter & WarehouseAdapter）
//        Constant.warehouse = currentWarehouse.warehouse;
//        Constant.provinceName = currentWarehouse.province;
//        Constant.cityName = currentWarehouse.city;
//        Constant.districtName = currentWarehouse.district;
//        Constant.warehouseAddress = currentWarehouse.warehouseAddress;
        LogUtils.printCloseableInfo(TAG, "currentWarehouse "+ currentWarehouse.warehouse);
    }

    // 选中某仓库（选中后 store and back）
    @Override
    public void warehouseSelected(String warehouseJson, String warehouseName) {
        this.currentWarehouseJson = warehouseJson;
        Constant.warehouseJson = warehouseJson;

        LogUtils.printCloseableInfo(TAG, "currentWarehouseJson: "+ currentWarehouseJson);
        parentActivity.onBackClicked();

    }

    @Override
    public void historyWarehouseSelected(String warehouseJson, String warehouseName) {
        currentWarehouseJson = warehouseJson;
        Constant.warehouseJson = warehouseJson;
        LogUtils.printCloseableInfo(TAG, "历史仓库数据: "+ currentWarehouseJson);
        // 校验：选中某个 历史仓库时（当该仓库为上次新建仓库时 可能此时并未 添加进数据库）数据库中没有则用本地数据；否则获取到的数据（add : id）；
        currentWarehouseName = warehouseName;
        getWarehouseData();
        // parentActivity.onBackClicked();  // 校验后再返回 报价页面
    }

    // 校验：选中某个 历史仓库时（当该仓库为上次新建仓库时 可能此时并未 添加进数据库）数据库中没有则用本地数据；否则获取到的数据 （add : id）；
    @Override
    public String changeToWarehouseForPost(WarehouseData.Warehouse warehouse) {
        String warehouseJson = "";

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
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.printError(TAG, "校验历史记录 ：gson.toJson(warehouse): "+ e.toString());
        }

        LogUtils.printCloseableInfo(TAG, "仓库有该条数据，使用仓库数据: "+ currentWarehouseJson);

        return currentWarehouseJson;
    }


}
