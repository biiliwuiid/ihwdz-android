package com.ihwdz.android.hwapp.ui.publish.purchase;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.PublishData;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/05
 * desc :  发布求购
 * version: 1.0
 * </pre>
 */
public interface PurchaseContract {

    interface View extends BaseView {

        void updateAddress(PublishData.ReceiverInfo data);

        //void setPublishButtonClickable(boolean isClickable); // 点击后不再允许点击 提示已经发布

        void showRemindDialog(String message);               // 用户状态异常（锁定、认证） 联系客服

        void showPromptMessage(String message);              // 提示信息

        void makeButtonDisable(boolean disable);               // 按钮状态(是否可用)
    }

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        AddViewAdapter getAdapter();          // 求购单明细

        BreedInfoAdapter getBreedAdapter();   // 求购单-Breed
        BreedInfoAdapter getSpecAdapter();    // 求购单-Spec
        BreedInfoAdapter getFactoryAdapter(); // 求购单-Factory

        void getAddressData();                 // 收货地址 数据
        void setPostAddress(PublishData.AddressEntity data);  // 获取默认收货地址
        void setPostAddress();                                // 选择地址后 更新收货地址

        void setKeyword(String s);   // 查询关键字

        void getBreedData();  // 获取 求购单-Breed 数据
        void getSpecData();   // 获取 求购单-Spec 数据
        void getFactoryData();// 获取 求购单-Factory 数据

//        PublishData.ProductEntity getCurrentBreed();  // 当前选择的-Breed 数据
//        PublishData.ProductEntity getCurrentSpec();   // 当前选择的-Spec 数据
//        PublishData.ProductEntity getCurrentFactory();// 当前选择的-Factory 数据
//        String getCurrentQty();
//
//        void setCurrentBreed(PublishData.ProductEntity breed);
//        void setCurrentSpec(PublishData.ProductEntity spec);
//        void setCurrentFactory(PublishData.ProductEntity factory);
//        void setCurrentQty(String qty);

        boolean checkCurrentDataComplete(); // 检查 当前求购单明细 是否填写完整

        void addItem();                                       // 增加 求购单明细
//        void deleteItem(PublishData.PublishPurchaseData data);// 删除 求购单明细

        void gotoSelectAddress();    // 选择 收货地址

        boolean getIsSubmitClicked();// 提交按钮 是否点击

        void doSubmit();             // 提交 求购数据
        void gotoMyPurchase();       // 提交成功后 -> 我的求购

        android.view.View getDialogContentView(String message);         // 弹窗
        void doContract();                                              // 联系客服
    }


}
