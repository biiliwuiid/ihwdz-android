package com.ihwdz.android.hwapp.ui.me.dealvip;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/15
 * desc : 交易会员
 * version: 1.0
 * </pre>
 */
public interface DealVipContract {

    interface View extends BaseView {

        void updateView(String quota, String deposit);
        void showPromptMessage(String message);       //  提示信息
    }

    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        void getApplyStateData();   // 获取保证金 获取开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料

        void gotoCollections();    // 显示　我的收藏
        void gotoMsgCenter();      // 显示　消息中心

        void gotoUpdateUserInfo(); // 显示　完善信息

        void gotoMyVipInfo();      // 显示  我的交易会员
        void gotoCheckQuota();     // 显示　查看额度
        void gotoDeposit();        // 显示　保证金

        void gotoLogistics();      // 显示  物流小助手
        void gotoSettings();       // 显示　设置

//        void getQuotaData();      //  获取 额度数据


//        void getCollections();    //  我的收藏
//        void getDealRecords();    //　交易记录
//        void getPurchaseList();   //  求购单



    }

}
