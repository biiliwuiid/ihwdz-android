package com.ihwdz.android.hwapp.ui.orders;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/06
 * desc :
 * version: 1.0
 * </pre>
 */
public interface OnOrderItemClickListener {


    // 订单详情
    interface OnItemClickListener {
        void onItemClicked(String id, String orderStatus, int orderOption);
    }

    // 申请展期
    interface OnExtensionClickListener{
        void onExtensionClicked(String id);
    }

    // 支付手续费
    interface OnPayClickListener {
        void onPayClicked(String id, String orderSn, String charge);
    }

    // 申请开票
    interface OnInvoiceClickListener{
        void onInvoiceClicked(String id);
    }


    // 取消订单 - cancelled now
//    interface OnCancelClickListener {
//        void onCancelClicked(String id);
//    }

}
