package com.ihwdz.android.hwapp.ui.orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrdersData;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoTitleAdapter;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailActivity;
import com.ihwdz.android.hwapp.ui.orders.extension.ExtensionActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;


import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_ALL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_COMPLETE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_BILL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_DELIVER;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_INVOICE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_PAY;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_TAKE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_ALL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_COMPLETE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_WAIT_BILL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_WAIT_DELIVER_BUSI;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_WAIT_DELIVER_DEAL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_WAIT_INVOICE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_WAIT_PAY;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.ORDER_WAIT_TAKE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/25
 * desc :  订单：全部订单/ 待收款/ 待发货/ 待收货/ 交易成功 - 商家 /待开票 - 交易
 * version: 1.0
 * </pre>
 */
public class OrderPresenter implements OrderContract.Presenter {

    String TAG = "OrderPresenter";

    @NonNull
    private CompositeSubscription mSubscriptions;
    LogisticsModel model ;

    private InfoTitleAdapter mTitleAdapter;
    private OrderAdapter mAdapter;

    private OrderContract.View mView;
    private MainActivity activity;

    private String currentMode = ORDER_ALL;  // 默认 - 全部订单
    private int currentModeIndex = INDEX_ORDER_ALL;  // 默认 - 全部订单

    private int currentPageNum = 1;

    public OrderPresenter(OrderContract.View view){
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
        model = LogisticsModel.getInstance(activity);
    }

    @Override
    public void subscribe() {

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
    public void bindActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public InfoTitleAdapter getTitleAdapter() {
        if (mTitleAdapter == null){
            mTitleAdapter = new InfoTitleAdapter(activity);
        }
        return mTitleAdapter;
    }

    @Override
    public OrderAdapter getAdapter() {
        if (mAdapter == null){
            mAdapter = new OrderAdapter(activity);
        }
        return mAdapter;
    }

//    @Override
//    public void setCurrentMode(String mode) {
//        if (!TextUtils.equals(mode, currentMode)){
//            LogUtils.printInfo(TAG, "========== setCurrentMode ======= : " + mode);
//            this.currentMode = mode;
//            refreshData();
//        }
//    }

    @Override
    public void setCurrentMode(int mode) {
        if(this.currentModeIndex != mode){
            this.currentModeIndex = mode;
            switch (currentModeIndex){
                case INDEX_ORDER_ALL:
                    currentMode = ORDER_ALL;
                    break;
                case INDEX_ORDER_WAIT_BILL:     // 待收款 - 商家
                    currentMode = ORDER_WAIT_BILL;
                    break;
                case INDEX_ORDER_WAIT_PAY:     // 待付款 - 交易
                    currentMode = ORDER_WAIT_PAY;
                    break;
                case INDEX_ORDER_WAIT_DELIVER: // 待发货
                    int vipType = Constant.VIP_TYPE; // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
                    if (vipType  == 1){
                        currentMode = ORDER_WAIT_DELIVER_DEAL;
                    }
                    if (vipType == 2){
                        currentMode =  ORDER_WAIT_DELIVER_BUSI;
                    }
                    break;
                case INDEX_ORDER_WAIT_TAKE:   // 待收货
                    currentMode = ORDER_WAIT_TAKE;
                    break;
                case INDEX_ORDER_WAIT_INVOICE:// 待开票 - 交易
                    currentMode = ORDER_WAIT_INVOICE;
                    break;
                case INDEX_ORDER_COMPLETE:    // 交易成功 - 商家
                    currentMode = ORDER_COMPLETE;
                    break;
            }
            refreshData();
        }
    }

    @Override
    public String getCurrentMode() {
        return currentMode;
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
    public void refreshData() {
        LogUtils.printCloseableInfo(TAG, "============ refreshData");
        currentPageNum = 1;
        mAdapter.clear();
        mView.showWaitingRing();
        getAllData();

    }

    @Override
    public void getAllData() {
        if (currentMode == null){
            LogUtils.printCloseableInfo(TAG, "currentMode == null load all data");
        }else {
            LogUtils.printCloseableInfo(TAG, "currentMode: " + currentMode);
        }

        Subscription rxSubscription = model
                .getOrderData(currentMode, currentPageNum, PageSize)
                .compose(RxUtil.<OrdersData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrdersData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        if (mAdapter.getLoadMoreStatus() != Constant.loadStatus.STATUS_EMPTY){
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(OrdersData data) {
                        mView.hideWaitingRing();
                        mView.hideEmptyView();
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                if (data.data.recordList != null && data.data.recordList.size() > 0){

                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum++;
                                }else {
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }
                            }
                        }else {
                            mView.showEmptyView();
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);  // may be null
                            }

                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }


    @Override
    public void gotoDetail(String id, String orderStatus, int orderOption) {
        OrderDetailActivity.startOrderDetailActivity(activity, id, orderStatus, orderOption);
    }

    @Override
    public void gotoPay(String id, String orderSn, String charge) {
        // mView.showPromptMessage("支付手续费");
        //PaymentActivity.startPaymentActivity(activity, id, orderSn, charge);
    }

    @Override
    public void gotoInvoice(String id) {
        // mView.showPromptMessage("申请开票");
        // InvoiceActivity.startInvoiceActivity(activity, id);
    }

    @Override
    public void gotoExtension(String id) {

        // mView.showPromptMessage("申请展期");
         ExtensionActivity.startExtensionActivity(activity, id);
    }


}
