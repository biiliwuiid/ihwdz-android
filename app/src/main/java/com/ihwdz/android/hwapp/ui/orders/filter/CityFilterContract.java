package com.ihwdz.android.hwapp.ui.orders.filter;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.CityResultBean;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */
public interface CityFilterContract {

    interface View extends BaseView {

        void showPromptMessage(String string); //  提示信息
    }

    interface Presenter extends BasePresenter {

        void setIsFromAddress(boolean bIsFromAddress);
        void setIsVipDistrict(boolean bIsVipDistrict);
        void setIsWarehouse(boolean bIsWarehouse);
        void setIsAddress(boolean bIsAddress);


        CityFilterAdapter getProvinceAdapter();  // 始发地 省份
        CityFilterAdapter getProvinceAdapter2(); // 目的地 省份
        CityFilterAdapter getCityAdapter();      // 始发地 城市
        CityFilterAdapter getCityAdapter2();     // 目的地 城市
        CityFilterAdapter getAreaAdapter();      // 始发地 地区
        CityFilterAdapter getAreaAdapter2();     // 目的地 地区

        void refreshData();

        void getCityData();        // 获取城市信息（我的会员信息 - 选择所在地 - 读取本地Json文件）

        void getFromData();        // 获取始发地信息
        void getDestinationData(); // 获取目的地信息
        //void getPriceData();       // 获取价格信息

        void setCurrentProvince(LogisticsCityData.CityEntity province);
        LogisticsCityData.CityEntity getCurrentProvince();

        void setCurrentProvince2(CityResultBean province);
        CityResultBean getCurrentProvince2();

        void setCurrentCity(LogisticsCityData.CityEntity province);
        LogisticsCityData.CityEntity getCurrentCity();

        void setCurrentCity2(CityResultBean province);
        CityResultBean getCurrentCity2();

        void setCurrentArea(LogisticsCityData.CityEntity province);
        LogisticsCityData.CityEntity getCurrentArea();

        void setCurrentArea2(CityResultBean province);
        CityResultBean getCurrentArea2();

        void saveAddress();           // 保存已选地址 - 物流
        void saveSelectedDistrict();  // 保存已选地址 - 会员信息 & 添加仓库 & 添加收货地址

//        void postUserUpdate();   // 会员信息- 所在地区选择后 提交修改

    }
}
