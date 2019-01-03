package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;


/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :   查价格 未登录时只允许查看一页数据；点击“查看更多”->登录
 * version: 1.0
 * </pre>
 */

public interface PriceInquiryContract {

    interface View extends BaseView{

        void showWaitingRing();       // "查看更多"、"上拉加载更多"
        void hideWaitingRing();

        void initMarketPriceView();   // 市场价
        void initFactoryPriceView();  // 出厂价

        void showMenuView();          // 展示菜单选项卡
        void hideMenuView();          // 隐藏菜单选项卡

        //void updateBreedTitle(String title);  // 更新 Menu name
        void updateSpecTitle(String title);
        void updateCityTitle(String title);
        void updateBrandTitle(String title);
        void updateDateTitle(String title);

        void showLoadMore();          // 未登录时- 显示 "查看更多"
        void hideLoadMore();          // 登录后  - 隐藏 "查看更多"

        void showEmptyView();
        void hideEmptyView();

        void showPromptMessage(String message);

    }

    int PageNum = 1;
    int PageSize = 15;

    int MENU_HIDE = 0;
    int BREED = 1;          // 品名
    int SPEC = 2;           // 型号
    int BRAND = 3;          // 厂家
    int AREA = 4;           // 销售地
    int MENU_DATE = 5;      // 日期

    int MARKET_PRICE = 0;   // 市场价
    int FACTORY_PRICE = 1;  // 出厂价

    interface Presenter extends BasePresenter{

        BreedAdapter getBreedAdapter();     // adapter for breed recycler view
        PriceMenuAdapter getMenuAdapter();  // adapter for menu recycler view
        PriceAdapter getAdapter();          // adapter for price data recycler view

        void refreshData();                // 手动刷新 / 出厂价/市场价
        void updateData();                 // 切换条件

        void getBreedData();               // 获取 Breed 数据
        void getPriceData();               // get MarketPriceData or FactoryPriceData by mode
        void getMarketPriceData();         // 获取 市场指导价
        void getFactoryPriceData();        // 获取 出厂价


        void doCollect(int collectionType,       // 0 市场价 1 出厂价
                       String breed,
                       String spec,
                       String brand,
                       String area);                  // 收藏动作

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        boolean getIsLoadingData();         // 当前是否在加载数据

        int getCurrentMode();
        void setCurrentMode(int mode);       // 市场指导价/出厂价

        // void setCurrentDate(String date)

        void setCurrentBreed(String breed);  // 获取当前选择的 Breed

        void setCurrentMenu(int menu);       // 当前菜单选择项
        int getCurrentMenu();

        void setCurrentMenuSelected(String menuSelected);  // 选中某项菜单栏下的某个选项卡时 - hide recycler view and update menu button(name & checked state)

        void selectDate();                   // 日期选择器
        void setCurrentDate(String date);    // 选中日期

        void gotoMyCollection();             // 我的收藏
        void gotoLoginPage();                // 登录注册   点击“收藏按钮”“我的收藏”“查看更多”

//        List<String> getSpecList();   // 型号
//        List<String> getAreaList();   // 销售地
//        List<String> getBrandList();  // 厂家

//        void getCurrentMarketPriceData();
//        void getCurrentFactoryPriceData();

//        void getMarketPriceData( int pageNum, int pageSize,
//                                 String breed, String spec, String city, String brand);
//
//        void  getFactoryPriceData(int pageNum, int pageSize,
//                                  String breed, String spec, String region, String brand);



//        String getCurrentBreed();

//
//        String getCurrentSpec();
//        void setCurrentSpec(String breed);
//
//        String getCurrentBrand();
//        void setCurrentBrand(String breed);
//
//        String getCurrentArea();
//        void setCurrentArea(String breed);


//        MarketPriceAdapter getMarketPriceAdapter();
//        FactoryPriceAdapter getFactoryPriceAdapter();

    }


}
