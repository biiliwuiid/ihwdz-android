package com.ihwdz.android.hwapp.ui.me.records;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.DealRecordData;
import com.ihwdz.android.hwapp.ui.me.records.invoice.InvoiceActivity;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.PageSize;
import static com.ihwdz.android.hwapp.ui.me.records.RecordContract.DEAL_MODE;
import static com.ihwdz.android.hwapp.ui.me.records.RecordContract.InvoiceRequestCode;
import static com.ihwdz.android.hwapp.ui.me.records.RecordContract.SHOP_MODE;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/19
 * desc :   交易记录
 * version: 1.0
 * </pre>
 */
public class RecordPresenter implements RecordContract.Presenter {

    String TAG = "RecordPresenter";
    @Inject RecordsActivity parentActivity;
    @Inject RecordContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    @Inject RecordAdapter mAdapter;

    private String totalAmount;
    private String orderId;

    int currentPageNum = 1;
    int currentMode = DEAL_MODE;

    @Inject
    public RecordPresenter(RecordsActivity activity){
        this.parentActivity = activity;
        model = new LoginDataModel(parentActivity);
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
        Log.d(TAG, "========== setCurrentMode ======= : " + mode);
        if (this.currentMode != mode){
            this.currentMode = mode;
            refreshData();
        }

    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setAmount(String amount) {
        totalAmount = amount;
    }

    @Override
    public void setOrderId(String id) {
        this.orderId = id;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void gotoInvoiceInfo() {
        // InvoiceActivity.startInvoiceActivity(parentActivity, orderId, totalAmount);
        Intent intent = new Intent(parentActivity, InvoiceActivity.class);
        intent.putExtra("AMOUNT", totalAmount);
        intent.putExtra("ORDER_ID", orderId);
        //parentActivity.startActivity(intent);
        parentActivity.startActivityForResult(intent,InvoiceRequestCode);
    }

    @Override
    public void refreshData() {
        currentPageNum = 1;
        mView.showWaitingRing();
        Log.d(TAG, "================ refreshData == currentMode: " + currentMode);
        switch (currentMode){
            case DEAL_MODE:
                getRecordData();
                break;
            case SHOP_MODE:
                getShopRecordData();
                break;
            default:
                break;
        }
    }

    @Override
    public RecordAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void getRecordData() {
        Subscription rxSubscription = model
                .getRecordData(Constant.token, currentPageNum, PageSize)
                .compose(RxUtil.<DealRecordData>rxSchedulerHelper())
                .subscribe(new Subscriber<DealRecordData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        if (mAdapter.getLoadMoreStatus() != Constant.loadStatus.STATUS_EMPTY){
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        }else {
                            Log.d(TAG, "========= getRecordData ==== onCompleted ========== setLoadMoreStatus: STATUS_EMPTY");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(DealRecordData data) {
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                if (data.data.recordList!= null && data.data.recordList.size()>0){
                                    Log.d(TAG, "========= getRecordData ==== onNext ========== data: "+data.data.recordList.size());
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }
                                    else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    // 加载完成　加页数
                                    currentPageNum ++;
                                }
                            }else {
                                if (currentPageNum == 1){
                                    mView.showEmptyView();
                                }else {
                                    mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    Log.d(TAG, "========= getRecordData ==== onNext ========== setLoadMoreStatus: STATUS_EMPTY");
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
    public void getShopRecordData() {
        Subscription rxSubscription = model
                .getShopRecordData(Constant.token, currentPageNum, PageSize)
                .compose(RxUtil.<DealRecordData>rxSchedulerHelper())
                .subscribe(new Subscriber<DealRecordData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        if (mAdapter.getLoadMoreStatus() != Constant.loadStatus.STATUS_EMPTY){
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        }else {
                            Log.d(TAG, "========= getShopRecordData ==== onCompleted ========== setLoadMoreStatus: STATUS_EMPTY");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(DealRecordData data) {

                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                if (data.data.recordList!= null && data.data.recordList.size()>0){
                                    Log.d(TAG, "========= getShopRecordData ==== onNext ========== data: "+data.data.recordList.size());
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }
                                    else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    // 加载完成　加页数
                                    currentPageNum ++;
                                }
                            }else {
                                if (currentPageNum == 1){
                                    mView.showEmptyView();
                                }else {
                                    mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    Log.d(TAG, "========= getShopRecordData ==== onNext ========== setLoadMoreStatus: STATUS_EMPTY");
                                }

                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);

    }
}
