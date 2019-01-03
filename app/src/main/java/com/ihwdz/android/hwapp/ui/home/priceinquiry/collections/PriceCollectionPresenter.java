package com.ihwdz.android.hwapp.ui.home.priceinquiry.collections;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PriceCollectionData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.priceData.PriceDataModel;
import javax.inject.Inject;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/21
 * desc :   查价格 - 我的收藏
 * version: 1.0
 * </pre>
 */
public class PriceCollectionPresenter implements PriceCollectionContract.Presenter {

    @Inject PriceCollectionActivity parentActivity;
    @Inject PriceCollectionContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    PriceDataModel model ;

    @Inject PriceCollectionAdapter mAdapter;

    String TAG = "PriceCollectionPresenter";

    private int currentPageCount = 1;
    private int currentPageNum = 1;

    private String currentDate = null;     // date

    private int currentCollectionType = 0;        // 0 市场价 1 出厂价
    private String currentBreed = null;
    private String currentSpec = null;
    private String currentBrand = null;
    private String currentArea = null;

    @Inject
    public PriceCollectionPresenter(PriceCollectionActivity activity){
        this.parentActivity = activity;
        model = PriceDataModel.getInstance(parentActivity);
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
    public PriceCollectionAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void refreshData() {
        currentPageNum = 1;
        getPriceCollectedData();
    }

    // 我的收藏数据
    @Override
    public void getPriceCollectedData() {
        LogUtils.printCloseableInfo(TAG, "getPriceCollectedData =============== ");
        LogUtils.printCloseableInfo(TAG, "currentDate: " + currentDate);
        LogUtils.printCloseableInfo(TAG, "currentPageNum: " + currentPageNum);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getPriceCollectedData(currentDate, currentPageNum, PageSize)
                .compose(RxUtil.<PriceCollectionData>rxSchedulerHelper())
                .subscribe(new Subscriber<PriceCollectionData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        mView.showPromptMessage(e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(PriceCollectionData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null  ){
                                currentPageCount = data.data.pageCount;
                                if (data.data.recordList != null && data.data.recordList.size() > 0) { //
                                    mView.hideEmptyView();
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
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

    // 收藏动作
    @Override
    public void doCollect(int collectionType,       // 0 市场价 1 出厂价
                          String breed,
                          String spec,
                          String brand,
                          String area) {
        LogUtils.printCloseableInfo(TAG, "收藏动作=============== ");
        LogUtils.printCloseableInfo(TAG, "collectionType: " + collectionType);
        LogUtils.printCloseableInfo(TAG, "breed: " + breed);
        LogUtils.printCloseableInfo(TAG, "spec: " + spec);
        LogUtils.printCloseableInfo(TAG, "brand: " + brand);
        LogUtils.printCloseableInfo(TAG, "area: " + area);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getPriceCollect(collectionType, breed, spec, brand, area)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        mView.hideWaitingRing();
                        mView.showPromptMessage(data.msg);
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 取消收藏动作
    @Override
    public void doCollectCancel(int collectionType,       // 0 市场价 1 出厂价
                                String breed,
                                String spec,
                                String brand,
                                String area) {
        LogUtils.printCloseableInfo(TAG, "取消收藏动作=============== ");
//        LogUtils.printCloseableInfo(TAG, "collectionType: " + collectionType);
//        LogUtils.printCloseableInfo(TAG, "breed: " + breed);
//        LogUtils.printCloseableInfo(TAG, "spec: " + spec);
//        LogUtils.printCloseableInfo(TAG, "brand: " + brand);
//        LogUtils.printCloseableInfo(TAG, "area: " + area);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getPriceCollectedCancel(collectionType, breed, spec, brand, area)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data){
                                refreshData();
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
    public int getCurrentPageCount() {
        return currentPageCount;
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
    public android.view.View  getCancelCollectView(String message) {
        View view = View.inflate(parentActivity, R.layout.contract_dialog, null);
        TextView messageTv = view.findViewById(R.id.tv);
        messageTv.setText(message);  // 弹窗内容
        return view;
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

    @Override
    public void setCurrentDate(String date) {
        if (TextUtils.isEmpty(currentDate) || !TextUtils.equals(currentDate, date) ){
            currentDate = date;
            mView.updateDateTitle(currentDate);
            refreshData();
        }
    }
}
