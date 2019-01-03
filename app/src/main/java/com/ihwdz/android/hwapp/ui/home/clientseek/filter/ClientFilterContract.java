package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.WaterfallAdapter;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/24
 * desc :   高级筛选只针对 潜在客户 type = "0";
 * version: 1.0
 * </pre>
 */
public interface ClientFilterContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void initAllMode();
        void initMoreMode();

        void getLinkWay();
        void getRegMoney();
        void getRegDate();

        void cleanSelected();
        void showPromptMessage(String string); //  提示信息

    }

    int ALL_MODE = 0;
    int MORE_MODE = 1;

    interface Presenter extends BasePresenter {

        SelectedAdapter getSelectedProvincesAdapter();  // 已选区域
        CityAdapter getCityAdapter();          // 城市区域
        ProvinceAdapter getProvinceAdapter();  // 省份区域

        WaterfallAdapter getLinkWayAdapter();   // 联系方式 邮箱、手机
        WaterfallAdapter getRegMoneyAdapter();  // 注册资金
        WaterfallAdapter getRegDateAdapter();   // 注册时间

        void getLinkWayData();
        void getRegMoneyData();
        void getRegDateData();

        void setHasPhone(String hasPhone);
        void setHasEmail(String hasEmail);
        void setStartMoney(String money);
        void setEndMoney(String money);
        void setStartDate(String date);
        void setEndDate(String date);

        void refreshData();
        void getAllData();  // 获取所有 省份-城市 信息

        void setCurrentMode(int mode);
        int getCurrentMode();

        void setCurrentProvince(ProvinceData.Bean province);
        ProvinceData.Bean getCurrentProvince();

        void addSelectedProvince(String province);    // 选择城市后 调用
        void removeSelectedProvince(String province); // 取消选择城市后 调用
        void clearSelectedProvinces();

        // 1. delete all cities belong to this province
        // 2. delete this province
        void clickToDeleteProvince(String province);  // 已选区域 - 主动点击删除该省

        List<ProvinceData.City> getCityFromBean(ProvinceData.Bean province, List<ProvinceData.Bean> beanList);  // 为城市添加省份信息

        // 选中的城市
        void addSelectedCity(ProvinceData.City city);
        void removeSelectedCity(ProvinceData.City city);
        void clearSelectedCities();
        List<ProvinceData.City> getSelectedCity();
        String getSelectedCityCodes();


        void confirmation();       // 确认
        void clearConditions();    // 清空条件

    }
}
