package com.ihwdz.android.hwapp.ui.publish.address;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.orders.warehouse.choose.HistoryWarehouseAdapter;
import com.ihwdz.android.hwapp.ui.orders.warehouse.choose.WarehouseAdapter;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :    收货地址
 * version: 1.0
 * </pre>
 */
public interface AddressContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void showEmptyView();
        void hideEmptyView();

        void showPromptMessage(String message);  //  提示信息
    }
//    int PageNum = 1;
//    int PageSize = 15;

    interface Presenter extends BasePresenter {

        AddressAdapter getAdapter();

        void refreshData();
        void getAddressData();   // 获取收货地址数据

        void gotoAddAddress();   // 添加新收货地址

        void gotoEditAddress(String id);  // 编辑修改收货地址

        void goBackPurchase();    // 返回 发布求购（携带选中数据）
    }

}
