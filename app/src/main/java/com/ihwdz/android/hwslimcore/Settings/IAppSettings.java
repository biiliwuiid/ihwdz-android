package com.ihwdz.android.hwslimcore.Settings;

import com.ihwdz.android.hwapp.model.entity.HomePageData;


/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public interface IAppSettings {

    // 是否是首次登陆
    void storeIsFirstLogin(boolean isFirst);
    boolean getIsFirstLogin();

    // 是否退出登录
    void storeIsLogout(boolean isLogout);
    boolean getIsLogout();

//    void storeIsFirstLoad(boolean isFirst);
//    boolean getIsFirstLoad();

    void storeToken(String token);
    String getLastToken();

    void storeUserNameString(String userName);
    String getLastUserName();

    void storeUserPwd(String userPwd);
    String getLastUserPwd();

    void storeEndDate(String endDate);
    String getEndDate();

    // 用户类型: 普通用户、资讯会员、交易会员、商家会员
    void storeUserType(String userType);
    String getLastUserType();

    // 记录 交易会员 是否已认证
    void storeIsAuthenticated(boolean isAuthenticated);
    boolean getIsAuthenticated();

    // 记录 交易会员 开通进度
    void storeApplyStatus(String status);
    String getApplyStatus();

    void storeLastData(HomePageData data);
    HomePageData getLastData();

    void acceptEULA();
    boolean IsAcceptEULA();

    void storeLastBreeds(String breeds);
    String getLastBreeds();

    void storeHistoryWarehouse(String warehouseListJson);
    String getHistoryWarehouse();
}
