package com.ihwdz.android.hwapp.ui.me.dealvip.deposit;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/17
 * desc :   保证金
 * version: 1.0
 * </pre>
 */
public interface DepositContract {

    interface View extends BaseView {

        void initDepositView();       // 保证金
        void initRefundSuccessView(); // 退款成功

        void showDialog();            // 是否确认退保证金
        void showPromptMessage(String message);  //  提示信息
    }

    int MODE_DEPOSIT = 0;          // 保证金 正常
    int MODE_DEPOSIT_REFUND = 1;   // 保证金 退款成功

    interface Presenter extends BasePresenter {

        void setCurrentMode(int mode); // 当前保证金状态：正常 ？ 退款中？ 退款成功？
        int getCurrentMode();

        void setCurrentId(String id);

        boolean getIsSubmitClicked();// 提交按钮 是否点击
        void doSubmitClick();

        void doRefund();           // "我要退保证金"

        void doConfirm();          // 弹框 确认
        void doCancel();           // 弹框 取消

        void doComplete();         // "完成"

        android.view.View getDialogContentView();         // 弹窗

    }

}
