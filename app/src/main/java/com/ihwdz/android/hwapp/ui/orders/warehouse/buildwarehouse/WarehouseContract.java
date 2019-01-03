package com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;

/**
 *
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :  添加新仓库 - 0 & 添加收货地址 - 1 & 修改收货地址 - 2
 * version: 1.0
 * </pre>
 */
public interface WarehouseContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void initWarehouseView();
        void initAddressView(boolean isUpdate);

        void updateReceiverInfo(PublishData.AddressEntity data); // 修改收货信息
        void updateAddress(String address);     // 选择完 省市区

        void showDialog(String warehouseName);                      // 提示 新建仓库 已存在

        void showPromptMessage(String message); //  提示信息
    }

    int MODE_WAREHOUSE = 0;        // 添加仓库
    int MODE_ADDRESS = 1;          // 添加 收货地址
    int MODE_ADDRESS_UPDATE = 2;   // 修改 收货地址

    interface Presenter extends BasePresenter {

        void setCurrentMode(int mode);

        void setAddressId(String addressId);   // 收货地址 ID(仅用于修改收货地址)
        void getLocalAddressData();            // 省市区
        void getAddressData();                 // 获取收货地址 - 修改收货地址时 使用

        void setWarehouseName(String warehouseName);// 仓库名称 - 添加仓库

        void setLinkman(String linkman);            // 联系人
        void setTel(String tel);                    // 联系电话

        void selectAddress();                       // 选择 省市区

        void setDetailAddress(String address);      // 填写详细地址

        void setProvince(String province);      // 省
        void setProvinceCode(String code);      // 省
        void setCity(String city);              // 市
        void setCityCode(String code);          // 市
        void setDistrict(String district);      // 区
        void setDistrictCode(String code);      // 区

        void setAsDefault(boolean asDefault);       // 设为默认 - 添加收货地址

        void checkWarehouse(String warehouseName);  // 添加仓库 - 校验仓库 是否存在
        void storeWarehouse();        // 记住新建的仓库 json string -> 历史仓库数据

        boolean checkIsComplete();    // 检测 信息是否填写完整

        boolean getIsSubmitClicked(); // 提交按钮是否点击
        void doSubmit();              // 返回报价页面 更新页面仓库信息 ; 返回收货地址

        void gotoQuoteActivity();     // 新建仓库 - 返回报价
        void gotoAddress();           // 添加收货地址 - 返回收货地址列表

        String changeToWarehouseForPost(WarehouseData.Warehouse warehouse);  // 仓库-》上传数据格式

    }

}
