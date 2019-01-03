package com.ihwdz.android.hwapp.ui.me.records.invoice;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.InvoiceData;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/24
 * desc :   开票信息
 * version: 1.0
 * </pre>
 */
public interface InvoiceContract {

    interface View extends BaseView {

        void updateView(InvoiceData.InvoiceEntity data);
        void showPromptMessage(String string); //  提示信息
        void showEmptyView();                  // 没有数据时显示
    }

    interface Presenter extends BasePresenter {

        void setOrderId(String id);
        String getOrderId();

        void setAmount(String amount);

        void getInvoiceData();           // 开发票信息

        boolean getIsSubmitClicked();
        void applyInvoiceData();         // 提交申请

        void goBack();                   // 返回订单详情页 更新按钮状态
    }
}
