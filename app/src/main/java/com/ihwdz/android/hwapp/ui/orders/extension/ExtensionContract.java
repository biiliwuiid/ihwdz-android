package com.ihwdz.android.hwapp.ui.orders.extension;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.ExtensionData;
import com.ihwdz.android.hwapp.model.bean.OrderDetailData;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/28
 * desc :   申请展期
 * version: 1.0
 * </pre>
 */
public interface ExtensionContract {

    interface View extends BaseView {

        void updateView(ExtensionData.ExtensionEntity data);   //  更新 展期数据
        void updatePaymentDay(int paymentDay);                 //  更新 账期

        void showPromptMessage(String message);                //  提示信息
    }

    interface Presenter extends BasePresenter {

        void setCurrentId(String id);      // 订单id

        void getExtensionData();           // 展期数据

        int getMaxPaymentDay();            // 最大账期
        double getExtensionAmount();       // 展期金额 = 授信额度*展期利率*账期（随账期变化）

        void  setCurrentRemarks(String remarks);

        void doSelectPaymentDay();         // 选择账期 - 设置账期
        void doRemarks();                  // 编辑备注

        boolean getIsSubmitClicked();

        void doSubmit();                   // 提交申请
        void goBack();                     // 返回订单详情页 更新按钮状态


    }

}
