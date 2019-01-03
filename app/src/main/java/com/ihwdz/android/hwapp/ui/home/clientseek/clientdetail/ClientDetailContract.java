package com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/19
 * desc :  找客户- 详情
 * version: 1.0
 * </pre>
 */
public class ClientDetailContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();
        void setFollowStarClickable(boolean clickable);


        void initDeviceRecyclerView();
        void initProductRecyclerView();
        void showEmptyDeviceLayout();
        void hideProductLayout();
        void showProductLayout();

        void updateFollowIcon(boolean followed);
        void updateView(ClientDetailData data);       // 更新
        void showPromptMessage(String string); //  提示信息

    }

    interface Presenter extends BasePresenter {
        void refreshData();

        void setClientId(String id);
        void setFollow(String follow);

        DeviceAdapter getDeviceAdapter();
        ProductAdapter getProductAdapter();

        void getClientDetailData();       // 获取客户详细信息
        void updateClientFollowData();    // 修改是否关注
    }
}
