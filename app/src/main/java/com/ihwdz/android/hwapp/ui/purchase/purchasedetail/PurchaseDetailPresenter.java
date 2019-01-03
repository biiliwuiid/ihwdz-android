package com.ihwdz.android.hwapp.ui.purchase.purchasedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PurchaseQuoteData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.orders.confirm.OrderConfirmActivity;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.ihwdz.android.hwapp.common.Constant.Remarks.PRICE_REVIEW;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :   求购报价： 我的求购 -item-> 求购报价（交易会员）
 * version: 1.0
 * </pre>
 */
public class PurchaseDetailPresenter implements PurchaseDetailContract.Presenter {
    String TAG = "PurchaseDetailPresenter";

    @Inject PurchaseDetailActivity parentActivity;
    @Inject PurchaseDetailContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LogisticsModel model;

    @Inject PurchaseQuoteAdapter mAdapter;

    private int currentPageNum = 1;

    private String id;      // 我的 当前该条求购的 id
    private String quoteId; // 我的 当前该条求购的 某条报价的id

    @Inject
    public PurchaseDetailPresenter(PurchaseDetailActivity activity){
        this.parentActivity = activity;
        model = new LogisticsModel(activity);
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
    public PurchaseQuoteAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void getMyPurchaseListData() {
        LogUtils.printCloseableInfo(TAG, "getMyPurchaseListData ID :" + id);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getMyPurchaseDetailData(id)
                .compose(RxUtil.<PurchaseQuoteData>rxSchedulerHelper())
                .subscribe(new Subscriber<PurchaseQuoteData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PurchaseQuoteData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)) {
                            if (data.data != null){
                                // purchase info
                                if (data.data.memberPurchaseNewVO != null){
                                    mView.updatePurchaseView(data.data.memberPurchaseNewVO);
                                }
                                // quote info
                                if (data.data.sellMemberQuoteList != null && data.data.sellMemberQuoteList.size() > 0){
                                    mView.updateQuoteView(data.data.sellMemberQuoteList.size());   // 更新报价数
                                    mAdapter.clear();
                                    mAdapter.setDataList(data.data.sellMemberQuoteList);
                                }else {
                                    mView.updateQuoteView(0);                          // 更新报价数
                                }
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }

    // 判断用户类型、锁定、认证状态
    @Override
    public void getUserType() {
        Subscription rxSubscription = model
                .getUserType()
                .compose(RxUtil.<UserTypeData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserTypeData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getUserType onError: " + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserTypeData data) {
                        if ("0".equals(data.code)){
                            if (data != null && data.data != null){

                                Constant.VIP_TYPE = data.data.type;               //会员类型：-1 用户; 0 资讯; 1 交易; 2 商家;
                                Constant.VIP_LOCK_STATUS = data.data.status;  // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                                Constant.VIP_AUTHENTIC = data.data.authStatus == 1;   // 认证状态 0==未认证, 1==已认证

                            }
                        }else {
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
//                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
//                                if (Constant.token != null && Constant.token.length()>0){
//                                    mView.showPromptMessage(data.msg);
//                                }
//                            }
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getOrderCheckData() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getOrderCheckData()
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.printError(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {

                        if (TextUtils.equals("0", data.code)){
                            if (data.data){
                                // 确认订单
                                gotoOrderConfirm();
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
    public void setCurrentId(String id) {
        this.id = id;
    }

    @Override
    public void setCurrentQuoteId(String id) {
        this.quoteId = id;
    }


    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    @Override
    public void setCurrentPageNum(int pageNum) {
        this.currentPageNum = pageNum;
    }

    // 点击：一键下单 --检验能否一键下单--> 确认订单
    @Override
    public void doOrder() {
        // mView.showPromptMessage("一键下单");
        // 2018/11/19  一键下单 check 能否一键下单 能 -> "确认订单"； 否 -> show message
        getOrderCheckData();
    }

    @Override
    public void doReview(String id) {
        //mView.showPromptMessage("价格复议");
        String currentContent = parentActivity.getResources().getString(R.string.review_hint); // 仅一次
        boolean isHint = true;
        ReviewActivity.startReviewActivity(parentActivity, PRICE_REVIEW, currentContent, isHint, null,id);
    }

    // -> 确认订单 页面 finish this activity
    @Override
    public void gotoOrderConfirm() {
        // quoteId = 431
        OrderConfirmActivity.startOrderConfirmActivity(parentActivity, quoteId);
        //parentActivity.finish();
    }

    // 联系客服弹窗
    @Override
    public View getDialogContentView(String message) {
        LogUtils.printError(TAG, "getDialogContentView message: " + message);
        if (message.contains(":")){
            String[] strings =  message.split(":");
            phoneNumber = strings[1];
        }
        View view = View.inflate(parentActivity, R.layout.contract_dialog, null);
        TextView messageTv = view.findViewById(R.id.tv);
        messageTv.setText(message);  // 弹窗内容
        return view;
    }

    private String phoneNumber;
    @Override
    public void doContract() {
        //mView.showPromptMessage("请直接点击客服电话");
        Intent Intent =  new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
        startActivity(Intent);
    }



}
