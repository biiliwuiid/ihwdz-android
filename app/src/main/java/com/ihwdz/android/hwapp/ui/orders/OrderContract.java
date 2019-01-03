package com.ihwdz.android.hwapp.ui.orders;


import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoTitleAdapter;
import com.ihwdz.android.hwapp.ui.main.MainActivity;


/**
 * <pre>
 * author : Duan
 * time : 2018/07/25
 * desc :   订单 Fragment 订单：全部订单/ 待收款/ 待发货/ 待收货/ 交易成功 - 商家 /待开票 - 交易
 * version: 1.0
 * </pre>
 */
public interface OrderContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void showPromptMessage(String message); //  提示信息

        void showEmptyView();                   // 没有数据时显示
        void hideEmptyView();
    }

    String ORDER_ALL = null;                   // 全部订单
    String ORDER_WAIT_BILL = "11,12,20,30";    // 待收款 - 商家
    String ORDER_WAIT_PAY = "12,20";           // 待付款 - 交易

    String ORDER_WAIT_DELIVER_DEAL = "11,30,40"; // 待发货- 交易
    String ORDER_WAIT_DELIVER_BUSI = "40";       // 待发货- 商家

    String ORDER_WAIT_TAKE = "50";             // 待收货
    String ORDER_WAIT_INVOICE = "100,101";     // 待开票 - 交易
    String ORDER_COMPLETE = "100,101,102";     // 交易成功 - 商家

    int INDEX_ORDER_ALL = 0;            // 全部订单
    int INDEX_ORDER_WAIT_BILL = 20;     // 待收款 - 商家
    int INDEX_ORDER_WAIT_PAY = 30;      // 待付款 - 交易
    int INDEX_ORDER_WAIT_DELIVER = 40;  // 待发货
    int INDEX_ORDER_WAIT_TAKE = 50;     // 待收货
    int INDEX_ORDER_WAIT_INVOICE = 100; // 待开票 - 交易
    int INDEX_ORDER_COMPLETE = 102;     // 交易成功 - 商家

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        InfoTitleAdapter getTitleAdapter();     // 标题栏（全部/待发货...）
        OrderAdapter getAdapter();

        //void setCurrentMode(String mode);
        void setCurrentMode(int mode);           // 当前订单数据类型（全部/待发货...）
        String getCurrentMode();

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        void refreshData();

        void getAllData();                        // 订单数据

//        void getWaitPayData();        // 待付款
//        void getWaitDeliverData();    // 待发货
//        void getWaitTakeData();       // 待收货
//        void getWaitInvoiceData();    // 待开票 - 交易
//        void getCompleteData();       // 交易成功 - 商家

        void gotoDetail(String id, String orderStatus, int orderOption);    // 订单详情
        void gotoPay(String id, String orderSn, String charge);             // 支付手续费
        void gotoInvoice(String id);   // 申请开票
        void gotoExtension(String id); // 申请展期

    }


}
