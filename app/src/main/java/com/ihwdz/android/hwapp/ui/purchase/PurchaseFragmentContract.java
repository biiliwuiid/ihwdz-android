package com.ihwdz.android.hwapp.ui.purchase;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/25
 * desc :   求购池 Fragment // status: -3 已报价 -2未登录 -1用户 0 资讯 1交易 2商家 3交易未认证 4商家未认证 5交易失效 6商家失效;
 * version: 1.0
 * </pre>
 */
public interface PurchaseFragmentContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void updateView();                       //  更新求购池

        void showRemindDialog(String message);   // 用户状态异常（锁定、认证） 联系客服
        void showPromptMessage(String message);  //  提示信息

        void showRemindView(String remind, String buttonTitle);
        void hideRemindView();

        void showEmptyView();
        void hideEmptyView();
    }

    int PageNum = 1;
    int PageSize = 15;

    int ALL_DATA_MODE = 0;
    int MY_DATA_MODE = 1;

    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        PurchaseAdapter getAdapter();
        void refreshData();

        void getUserType();           // 判断用户类型、锁定、认证状态

        void getPurchasePoolData();   // 获取求购池数据 - 所有求购/所有报价
        void getMyPurchaseListData(); // 获取求购池数据 - 我的求购 - 交家会员
        void getMyQuoteListData();    // 获取求购池数据 - 我的报价 - 商家会员已认证

        boolean isLoadingData();      // 当前是否正在加载数据

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        void setCurrentMode(int mode);
        int getCurrentMode();

        void gotoLoginPage();                 // 登陆/注册  - 未登录 - 我的求购
        void gotoOpenDealVip();               // 提醒开通交易会员 - 普通用户/资讯会员 - 我的求购
        void gotoBusinessAuthentic();         // 提醒商家认证     - 商家会员未认证 - 我的报价

        void gotoPurchaseDetail( String id, String status, String breed, String qty, String address );          // "求购报价" 交易会员 - 我的求购 - item

        void gotoQuoteDetail( String id, String status, String breed, String qty, String address, String dateStr);              // "报价详情" 商家会员已认证 - 我的报价 - item

        void doQuote(boolean isQuoteEnable, String id, String status, String breed, String qty, String address, String dateStr); // 商家报价

        void doReview(String id, String price );                                                // 商家会员已认证  复议报价

        android.view.View getDialogContentView(String message);         // 弹窗
        void doContract();                                              // 联系客服


    }

}
