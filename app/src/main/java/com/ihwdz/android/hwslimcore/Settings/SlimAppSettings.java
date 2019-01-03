package com.ihwdz.android.hwslimcore.Settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimAppSettings implements IAppSettings {

    SharedPreferences sharedPreferences;
    static final String APP_SETTING_NAME = "app_setting";

//    static final String SLIM_URL = "slim_url";
//    static final String SLIM_CREDENTIAL = "slim_credential";
//    static final String SLIM_DOMAIN_USER = "slim_domain_user";
    static final String SLIM_LAST_DATA_LIST = "slim_last_data_list";

    static final String ACCEPT_EULA = "ACCEPT_EULA";

    static final String SLIM_IS_FIRST_LOAD = "firstLoad";
    static final String SLIM_IS_FIRST_LOGIN = "firstLogin";
    static final String SLIM_IS_LOGOUT = "isLogout";
    static final String SLIM_TOKEN = "token";
    static final String SLIM_USER_NAME = "username";
    static final String SLIM_USER_PWD = "userPwd";
    static final String SLIM_END_DATE = "endDate";
    static final String SLIM_USER_TYPE = "userType";
    static final String SLIM_IS_AUTHENTICATED = "isAuthenticated";
    static final String SLIM_APPLY_STATUS = "userApplyStatus";
    static final String SLIM_PASSWORD = "password";

    static final String SLIM_BREEDS = "breeds";    // 用户选择的品类
    static final String SLIM_WAREHOUSE = "warehouse";    // 仓库历史记录



    private static SlimAppSettings settings;
    public static SlimAppSettings getInstance(Context context){
        if(settings == null) {
            settings = new SlimAppSettings(context);
        }
        return settings;
    }

    @Inject
    public SlimAppSettings(@ForApplication Context context){
        sharedPreferences = context.getSharedPreferences(APP_SETTING_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void storeIsFirstLogin(boolean isFirst) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SLIM_IS_FIRST_LOGIN, isFirst);   //TODO: encryption
        editor.apply();
    }

    @Override
    public boolean getIsFirstLogin() {
        return sharedPreferences.getBoolean(SLIM_IS_FIRST_LOGIN,true);
    }

    @Override
    public void storeIsLogout(boolean isLogout) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SLIM_IS_LOGOUT, isLogout);   //TODO: encryption
        editor.apply();
    }

    @Override
    public boolean getIsLogout() {
        return sharedPreferences.getBoolean(SLIM_IS_LOGOUT,true);
    }

//    @Override
//    public void storeIsFirstLoad(boolean isFirst) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(SLIM_IS_FIRST_LOAD, isFirst);   //TODO: encryption
//        editor.apply();
//    }
//
//    @Override
//    public boolean getIsFirstLoad() {
//        return sharedPreferences.getBoolean(SLIM_IS_FIRST_LOAD,true);
//    }

    @Override
    public void storeToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_TOKEN, token);   //TODO: encryption
        editor.apply();
    }

    @Override
    public String getLastToken() {
        String url = sharedPreferences.getString(SLIM_TOKEN, "");
        return url;
    }

    @Override
    public void storeUserNameString(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_USER_NAME, userName);   //TODO: encryption
        editor.apply();
    }

    @Override
    public String getLastUserName() {
        String url = sharedPreferences.getString(SLIM_USER_NAME, "");
        return url;
    }

    @Override
    public void storeUserPwd(String userPwd) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_USER_PWD, userPwd);   //TODO: encryption
        editor.apply();
    }

    @Override
    public String getLastUserPwd() {
        String url = sharedPreferences.getString(SLIM_USER_PWD, "");
        return url;
    }

    @Override
    public void storeEndDate(String endDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_END_DATE, endDate);   //TODO: encryption
        editor.apply();
    }

    @Override
    public String getEndDate() {
        String url = sharedPreferences.getString(SLIM_END_DATE, "");
        return url;
    }

    @Override
    public void storeUserType(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_USER_TYPE, userType);   //TODO: encryption
        editor.apply();
    }

    @Override
    public String getLastUserType() {
        String url = sharedPreferences.getString(SLIM_USER_TYPE, "");
        return url;
    }

    @Override
    public void storeIsAuthenticated(boolean isAuthenticated) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SLIM_IS_AUTHENTICATED, isAuthenticated);   //TODO: encryption
        editor.apply();
    }

    @Override
    public boolean getIsAuthenticated() {
        return sharedPreferences.getBoolean(SLIM_IS_AUTHENTICATED, false);

    }

    @Override
    public void storeApplyStatus(String status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_APPLY_STATUS, status);   //TODO: encryption
        editor.apply();
    }

    @Override
    public String getApplyStatus() {
        String url = sharedPreferences.getString(SLIM_APPLY_STATUS, "");
        return url;
    }

    @Override
    public void storeLastData(HomePageData data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String dataJsonStr = gson.toJson(data);
        editor.putString(SLIM_LAST_DATA_LIST, dataJsonStr);   //TODO: encryption
        editor.apply();

    }

    @Override
    public HomePageData getLastData() {
        String jsonStr = sharedPreferences.getString(SLIM_LAST_DATA_LIST, "");
        if(jsonStr != null && !jsonStr.isEmpty()){
            Gson gson = new Gson();
//            Type type = new TypeToken<List<HomePageData>>(){}.getType();
//            List<HomePageData> list = gson.fromJson(jsonStr, type);

            Type type = new TypeToken<HomePageData>(){}.getType();
            HomePageData data = gson.fromJson(jsonStr, type);
            return data;
        }
        return null;
    }

    @Override
    public void acceptEULA() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ACCEPT_EULA, true);
        editor.apply();
    }

    @Override
    public boolean IsAcceptEULA() {
        return  sharedPreferences.getBoolean(ACCEPT_EULA, false);
    }

    @Override
    public void storeLastBreeds(String breeds) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_BREEDS, breeds);
        editor.apply();
    }

    @Override
    public String getLastBreeds() {
        String breeds = sharedPreferences.getString(SLIM_BREEDS, "");
        return breeds;
    }

    @Override
    public void storeHistoryWarehouse(String warehouseListJson) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SLIM_WAREHOUSE, warehouseListJson);
        editor.apply();
    }

    @Override
    public String getHistoryWarehouse() {
        String breeds = sharedPreferences.getString(SLIM_WAREHOUSE, "");
        return breeds;
    }
}
