package com.ihwdz.android.hwapp.ui.purchase.quotedetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.QuoteDetailData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.infoupdate.InfoUpdateActivity;
import com.ihwdz.android.hwapp.ui.orders.warehouse.choose.ChooseWHActivity;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import java.lang.reflect.Type;
import java.util.LinkedList;

import javax.inject.Inject;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.common.Constant.Remarks.REVIEW_PRICE;
import static com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailContract.MODE_QUOTE_CONFIRM;
import static com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailContract.MODE_QUOTE_DETAIL;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :   MY_QUOTE -> 报价详情 / QUOTE_BUTTON -> 报价
 * version: 1.0
 * </pre>
 */
public class QuoteDetailPresenter implements QuoteDetailContract.Presenter{

    String TAG = "QuoteDetailPresenter";

    @Inject QuoteDetailActivity parentActivity;
    @Inject QuoteDetailContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    IAppSettings settings;
    private WarehouseData.WarehouseForQuotePost currentWarehouse;           // 记录当前选择的仓库
    private LinkedList<WarehouseData.WarehouseForQuotePost> mWarehouseList; // 记录选择的仓库


    private int currentMode = MODE_QUOTE_DETAIL;
    private String id;

    // 报价提交数据
    private Long memberPurchaseId = 0L;			// 会员求购单Id
    private Double price;
    private Integer isSupplierDistribution = 0;	// 是否供配 0-否 1-是
    private String deliveryDate = null;			// 交货日期
    private String warehouseJson = null;          // 仓库
    private String unitPriceTitle = null;          // 单价

    @Inject
    public QuoteDetailPresenter(QuoteDetailActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);
        settings = SlimAppSettings.getInstance(activity);

        unitPriceTitle = parentActivity.getResources().getString(R.string.unit_price_title);
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
    public void getMyQuoteData() {
        LogUtils.printCloseableInfo(TAG, "ID :" + id);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getMyQuoteDetailData(id)
                .compose(RxUtil.<QuoteDetailData>rxSchedulerHelper())
                .subscribe(new Subscriber<QuoteDetailData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(QuoteDetailData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)) {
                            mView.updateView(data);
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setCurrentMode(int mode) {
        LogUtils.printCloseableInfo(TAG, "MODE: " + mode);
        this.currentMode = mode;
        switch (mode){
            case MODE_QUOTE_DETAIL:
                // 报价详情
                mView.initQuoteDetailView();
                break;
            case MODE_QUOTE_CONFIRM:
                // 确认报价
                mView.initQuoteConfirmView();
                break;
        }

    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setCurrentId(String id) {
        if (!TextUtils.isEmpty(id)){
            this.id = id;
            memberPurchaseId = Long.valueOf(id);
        }

    }

    @Override
    public void setCurrentPrice(Double price) {
        this.price = price;
    }

    @Override
    public void setIsSupplierDistribution(boolean is) {
        this.isSupplierDistribution = is ? 1:0;
    }

    @Override
    public void setDeliveryDate(String date) {
        if (!TextUtils.isEmpty(date)){
            this.deliveryDate = date;
        }
    }

    @Override
    public void setWarehouseJson(String warehouseJson) {
        if (!TextUtils.isEmpty(warehouseJson)){
            this.warehouseJson = warehouseJson;
        }
    }

    @Override
    public void updateWarehouse() {
//        mView.showPromptMessage("选择仓库!");

        Intent intent = new Intent(parentActivity, ChooseWHActivity.class);
        parentActivity.startActivity(intent);
    }

    // 修改交货日期
    int currentYear = 0, currentMonth = 0, currentDay = 0;
    int todayYear = 0, todayMonth = 0, todayDay = 0;
    @Override
    public void updateDeliveryDate() {
        // mView.showPromptMessage("交货日期!");
        // 当日日期
        String todayStr = DateUtils.getDateTodayString();
        LogUtils.printCloseableInfo(TAG, todayStr);
        String[] strs = todayStr.split("-");

        todayYear = Integer.valueOf(strs[0]);
        todayMonth = Integer.valueOf(strs[1]);
        todayDay = Integer.valueOf(strs[2]);

        if (currentYear == 0){
            currentYear = Integer.valueOf(strs[0]);
            currentMonth = Integer.valueOf(strs[1]);
            currentDay = Integer.valueOf(strs[2]);
        }else {

        }


        final DatePicker picker = new DatePicker(parentActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(parentActivity, 10));
        picker.setRangeEnd(currentYear + 1, currentMonth, currentDay);
        picker.setRangeStart(todayYear, todayMonth, todayDay);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                currentYear = Integer.valueOf(year);
                currentMonth = Integer.valueOf(month);
                currentDay = Integer.valueOf(day);

                mView.updateDate(year + "-" + month + "-" + day);
                setDeliveryDate(year + "-" + month + "-" + day);
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
        picker.setLabel(" "," ", " ");  // 默认 显示“年月日”
        picker.setTitleText(currentYear + "-" + currentMonth + "-" + currentDay);
        picker.show();
    }

    // 修改单价
    @Override
    public void updatePrice() {
        String content;
        boolean isHint = true;
        if (Constant.quotePrice == null){
            content = parentActivity.getResources().getString(R.string.quote_range);
        }else {
            content = "" + this.price;
            isHint = false;
        }
        InfoUpdateActivity.startInfoUpdateActivity(parentActivity, Constant.InfoUpdate.INFO_QUOTE_PRICE, unitPriceTitle, content, isHint);
    }

    @Override
    public void storeWarehouse() {

        LogUtils.printCloseableInfo(TAG, "================== storeWarehouse ====================");

        // 历史记录仓库 列表
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

            LogUtils.printCloseableInfo(TAG, "getHistoryWarehouse - result.size: " + result.size());
            LogUtils.printCloseableInfo(TAG, "getHistoryWarehouse - mWarehouseList: " + mWarehouseList);

        }

        // 当前选中的仓库
        currentWarehouse = new WarehouseData.WarehouseForQuotePost();

        if (Constant.id != null && Constant.id.length() > 0){
            currentWarehouse.id = Long.valueOf(Constant.id);
        }

        currentWarehouse.warehouse = Constant.warehouse;

        currentWarehouse.province = Constant.provinceName;
        currentWarehouse.provinceCode = Constant.provinceCode;

        currentWarehouse.city = Constant.cityName;
        currentWarehouse.cityCode = Constant.cityCode;

        currentWarehouse.district = Constant.districtName;
        currentWarehouse.districtCode = Constant.districtCode;

        currentWarehouse.warehouseAddress = Constant.warehouseAddress;
        currentWarehouse.warehousePhone =  Constant.warehousePhone;
        currentWarehouse.warehouseContact = Constant.warehouseContact;


        if ( currentWarehouse.warehouse != null && currentWarehouse.warehouse.length() > 0){

            if (mWarehouseList.size() <= 3 && !mWarehouseList.contains(currentWarehouse)){

                int count =  mWarehouseList.size();
                boolean isContain = false;
                for (int i = 0; i < count; i ++){
                    if (TextUtils.equals(mWarehouseList.get(i).warehouse, currentWarehouse.warehouse)){
                        isContain = true;
                        break;
                    }
                }
                if (!isContain){
                    LogUtils.printCloseableInfo(TAG, "================== storeWarehouse: " + currentWarehouse.warehouse);
                    mWarehouseList.add(currentWarehouse);
                }

            }else {
                mWarehouseList.remove(0);
                mWarehouseList.add(currentWarehouse);
            }

            // 存储历史记录
            String warehouseListJson = null;
            try {
                warehouseListJson = gson.toJson(mWarehouseList);
            }catch (Exception e){
                LogUtils.printError(TAG, "gson.toJson(mWarehouseList): "+ e.toString());
            }

            LogUtils.printCloseableInfo(TAG, "================== store warehouseListJson: " + warehouseListJson);
            settings.storeHistoryWarehouse(warehouseListJson);
        }
    }

    @Override
    public boolean checkCurrentDataComplete() {
        boolean isComplete = true;

        if (isSupplierDistribution == 0){
            // 供配 -否  时才会校验仓库
            if (TextUtils.isEmpty(this.warehouseJson)){
                isComplete = false;
            }

        }

        // 交货日期
        if (TextUtils.isEmpty(this.deliveryDate)){
            isComplete = false;
        }

        // 价格
        if (TextUtils.isEmpty(""+this.price) || this.price<0){
            isComplete = false;
        }

        return isComplete ;
    }

    @Override
    public void gotoMyQuote() {
        // 报价成功后 前往求购池 - 我的报价
        MainActivity.startActivity(parentActivity,1);
    }

    //  确认报价
    @Override
    public void doQuote() {

        // mView.showPromptMessage("确认报价");

//        setCurrentId("163");
//        setCurrentPrice("10");
//        setIsSupplierDistribution(false);
//        setDeliveryDate("2018-11-21");
//        String json = "{" +
//                "\"warehouse\":\"宝安公路1877号\"," +
//                "\"provinceCode\":\"310000\",\"province\":\"上海\"," +
//                "\"cityCode\":\"310100\",\"city\":\"上海\"," +
//                "\"districtCode\":\"310113\",\"district\":\"宝山区\"," +
//                "\"warehouseAddress\":\"宝安公路1877号\"," +
//                "\"warehousePhone\":\"021-36110671\"," +
//                "\"warehouseContact\":\"\"" +
//                "}";
//
//        setWarehouseJson(json);

        setWarehouseJson(Constant.warehouseJson);

        // 2018/12/3 存储历史记录
        storeWarehouse();


        LogUtils.printCloseableInfo(TAG, "memberPurchaseId : " + memberPurchaseId);
        LogUtils.printCloseableInfo(TAG, "price : " + price);
        LogUtils.printCloseableInfo(TAG, "isSupplierDistribution : " + isSupplierDistribution);
        LogUtils.printCloseableInfo(TAG, "deliveryDate : " + deliveryDate);
        LogUtils.printCloseableInfo(TAG, "warehouseJson : " + warehouseJson);

        Subscription rxSubscription = model
                .postQuoteData(memberPurchaseId, price, isSupplierDistribution, deliveryDate,warehouseJson)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        if (TextUtils.equals("0", data.code)){
                            if (data.data){
                                // 报价成功 -》 我的报价
                                gotoMyQuote();
                            }

                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);


    }

    @Override
    public void doReview() {
//        String content = parentActivity.getResources().getString(R.string.quote_range);
//        ReviewActivity.startReviewActivity(parentActivity, REVIEW_PRICE, content,null, id);

    }


}
