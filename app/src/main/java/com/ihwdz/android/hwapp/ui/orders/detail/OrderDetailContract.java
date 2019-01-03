package com.ihwdz.android.hwapp.ui.orders.detail;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.OrderDetailData;
import com.ihwdz.android.hwapp.ui.orders.OrderItemAdapter;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/22
 * desc :   订单详情 （全部订单 待付款 待发货 .. 交易 & 商家）
 *          商家 隐藏底部按钮 （只展示订单信息）
 * version: 1.0
 * </pre>
 */
public interface OrderDetailContract {

    interface View extends BaseView {

        void initBusinessOrderDetailView();     // 商家 订单详情
        void initDealOrderDetailView();         // 交易 订单详情

        void initPayFirstView();                // 款到发货 - hide "账期"&"使用授信"

        void updateView(OrderDetailData.OrderDetailEntity data);   //  更新订单详情

        void showBottomLinear(String buttonName);
        void hideBottomLinear();

        void showPromptMessage(String message); //  提示信息
    }


    //int MODE_CANCEL = -1;     // 取消订单 -cancelled
    int MODE_HIDE = -1;         // 隐藏按钮
    int MODE_PAY = 0;           // 支付手续费
    int MODE_INVOICE = 1;       // 申请开票
    int MODE_EXTENSION = 2;     // 申请展期



    interface Presenter extends BasePresenter {

        OrderItemAdapter getAdapter();
        void setCurrentMode(int mode);  // 按钮状态
        int getCurrentMode();

        void setVipMode(int type);
        void setCurrentId(String id);

        void getOrderDetailData();      // 订单详情

        String getStatusTag(String status);

        void buttonClicked();
        void gotoExtension();           // 申请展期Extension
        void gotoInvoice();             // 申请开票
        void gotoPay();                 // 支付手续费

    }



}
