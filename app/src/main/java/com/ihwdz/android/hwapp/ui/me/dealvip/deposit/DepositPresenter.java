package com.ihwdz.android.hwapp.ui.me.dealvip.deposit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositContract.MODE_DEPOSIT;
import static com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositContract.MODE_DEPOSIT_REFUND;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/17
 * desc :   保证金
 * version: 1.0
 * </pre>
 */
public class DepositPresenter implements DepositContract.Presenter {

    String TAG = "DepositPresenter";
    @Inject DepositActivity parentActivity;
    @Inject DepositContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    private int currentMode = -1;
    private String id = null;

    private boolean isSubmitClicked = false;  // 是否在提交

    @Inject
    public DepositPresenter(DepositActivity activity){
        this.parentActivity = activity;
        model = LoginDataModel.getInstance(activity);
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
        if (mode != currentMode){
            this.currentMode = mode;
            switch (currentMode){
                case MODE_DEPOSIT:
                    mView.initDepositView();
                    break;
                case MODE_DEPOSIT_REFUND:
                    mView.initRefundSuccessView();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setCurrentId(String id) {
        this.id = id;
    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    @Override
    public void doSubmitClick() {
        switch (currentMode){
            case MODE_DEPOSIT:
                if (!isSubmitClicked){
                    mView.showDialog();
                }
                break;
            case MODE_DEPOSIT_REFUND:
                doComplete();   // "完成"
                break;
            default:
                break;
        }
    }

    // "我要退保证金"
    @Override
    public void doRefund() {
        isSubmitClicked = true;
        Subscription rxSubscription = model
                .getDepositRefundData(id)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //isSubmitClicked = false;
                    }

                    @Override
                    public void onNext(VerifyData data) {

                        if (TextUtils.equals("0", data.code)){
                            if (data.data){
                                setCurrentMode(MODE_DEPOSIT_REFUND);
                            }else {
                                isSubmitClicked = false;
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            isSubmitClicked = false;
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 弹框 确认
    @Override
    public void doConfirm() {
        doRefund();   // "我要退保证金"
    }

    // 弹框 取消
    @Override
    public void doCancel() {

    }

    // "完成"
    @Override
    public void doComplete() {
        // 返回 更新 个人中心
        MainActivity.startActivity(parentActivity, 4);
        parentActivity.finish();
        // parentActivity.onBackClicked();
    }

    @Override
    public View getDialogContentView() {
        View view = View.inflate(parentActivity, R.layout.deposit_dialog, null);
        return view;
    }

}
