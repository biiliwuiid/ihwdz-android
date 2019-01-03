package com.ihwdz.android.hwapp.ui.purchase.reviewquote;

import android.os.Bundle;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.common.Constant.Remarks.CREDIT_LINE;
import static com.ihwdz.android.hwapp.common.Constant.Remarks.EXTENSION_APPLY;
import static com.ihwdz.android.hwapp.common.Constant.Remarks.PRICE_REVIEW;
import static com.ihwdz.android.hwapp.common.Constant.Remarks.REVIEW_PRICE;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/19
 * desc :  * 备注: 必须修改后才能提交
 *   交易会员:
 *   价格复议（求购报价-价格复议）“确认复议”
 *   申请展期备注（订单列表-申请展期）“提交申请”
 *
 *
 *   授信额度 （一键下单-结算方式:部分授信）“确认”
 *
 *   商家会员:
 *   复议报价 （我的报价-复议报价）“确认报价”
 * version: 1.0
 * </pre>
 */
public class ReviewPresenter implements ReviewContract.Presenter{

    String TAG = "ReviewPresenter";

    @Inject ReviewActivity parentActivity;
    @Inject ReviewContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    private int currentMode = -1;
    private String currentId;

    private double creditMax1 = 0.00d;
    private double creditMax2 = 0.00d;

    private String currentContent;   // 复议内容

    @Inject
    public ReviewPresenter (ReviewActivity activity){
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
    public void setCurrentMode(int mode) {
        // 不会内部切换 不需要判断 是否改变
        this.currentMode = mode;
        switch (currentMode){
            case PRICE_REVIEW:
                mView.updatePurchaseView(); // 价格复议 - 买家
                break;
            case EXTENSION_APPLY:
                mView.updateExtensionView(); // 申请展期 备注 - 买家
                break;
            case CREDIT_LINE:
                mView.updateCreditView();    // 授信额度 - 买家 (限制 只输入数字)
                break;
            case REVIEW_PRICE:
                mView.updateQuoteView();     // 复议价格 - 商家 (限制 只输入数字)
                break;
        }

    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }


    @Override
    public void setCurrentId(String id) {
        this.currentId = id;
    }

    @Override
    public void setCreditMax(double max1, double max2) {
        this.creditMax1 = max1;
        this.creditMax2 = max2;
    }

    @Override
    public void setReviewContent(String content) {
        this.currentContent = content;
    }

    // 点击按钮
    @Override
    public void doClickButton() {
        switch (currentMode){
            case PRICE_REVIEW:  // 确认复议 - 买家
                // 确认复议
                if (currentContent != null && currentContent.length() >0){
                    doConfirmReview();
                }else {
                    mView.showPromptMessage(parentActivity.getResources().getString(R.string.review_null));  // "请输入备注信息"
                }

                break;
            case EXTENSION_APPLY: // 申请展期 备注 - 买家
                Constant.extensionRemarks = currentContent;
                parentActivity.onBackPressed(); // 返回上级
                break;
            case CREDIT_LINE:  // 授信额度 - 买家
                if (checkCredit()){
                    if (currentContent != null && currentContent.length() > 0){
                        double creditLine = Double.valueOf(currentContent);
                        Constant.creditLine = creditLine;
                        parentActivity.onBackPressed(); // 返回上级
                    }
                }
                break;
            case REVIEW_PRICE: // 复议价格 - 商家
                if (checkQuote()){
                    doConfirmQuote();
                }
                break;
        }

    }

    // 校验 授信额度 0 - max1 max2
    private boolean checkCredit() {
        String creditHighRemind = parentActivity.getResources().getString(R.string.credit_too_high);
        String creditLowRemind = parentActivity.getResources().getString(R.string.credit_too_low);

        if (!TextUtils.isEmpty(currentContent)){
            double credit = Double.valueOf(currentContent);
            if (credit > creditMax1){
                mView.showPromptMessage(String.format(creditHighRemind, ""+creditMax1));
                return false;
            }else {
                if (credit > creditMax2){
                    mView.showPromptMessage(String.format(creditHighRemind, ""+creditMax2));
                    return false;
                }else {
                    return true;
                }
            }
        }else {
            mView.showPromptMessage(creditLowRemind);
            return false;
        }
    }

    // 校验报价5000-40000
    private boolean checkQuote() {
        String quoteLowRemind = parentActivity.getResources().getString(R.string.quote_too_low);
        String quoteHighRemind = parentActivity.getResources().getString(R.string.quote_too_high);
        if (!TextUtils.isEmpty(currentContent)){
            double quote = Double.valueOf(currentContent);
            if (quote < 5000){
                mView.showPromptMessage(quoteLowRemind);
                return false;
            }
            if (quote > 40000){
                mView.showPromptMessage(quoteHighRemind);
                return false;
            }
            return true;
        }else {
            mView.showPromptMessage(quoteLowRemind);
            return false;
        }
    }

    // 交易会员（求购报价-价格复议）“确认复议”
    @Override
    public void doConfirmReview() {
        Subscription rxSubscription = model
                .getPurchaseReviewData(currentId,currentContent)
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
                                // 成功
                                mView.showPromptMessage(data.msg);
                                parentActivity.onBackPressed(); // 返回上级
                            }else {
                                // 失败 what need to do?
                                mView.showPromptMessage(data.msg);
                            }

                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 商家会员（我的报价-复议报价）“确认报价”
    @Override
    public void doConfirmQuote() {
        Subscription rxSubscription = model
                .getQuoteReviewData(currentId,currentContent)
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
                                // 成功
                                parentActivity.onBackPressed(); // 返回上级
                            }else {
                                // 失败 what need to do?
                                mView.showPromptMessage(data.msg);
                            }

                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }
    
}
