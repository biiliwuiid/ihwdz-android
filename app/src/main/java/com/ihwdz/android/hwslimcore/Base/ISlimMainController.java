package com.ihwdz.android.hwslimcore.Base;

import android.app.Activity;

import com.ihwdz.android.hwapp.model.entity.HomePageData;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public interface ISlimMainController {

    int API_MODE_ONLINE = 0;
    int API_MODE_OFFLINE = 1;

    int SLIM_STATUS_IDLE = 0;
    int SLIM_STATUS_LOGIN = 1;
    int SLIM_STATUS_UPLOADING = 2;

    int DATA_BASE_REALM = 0;   // Realm
    int DATA_BASE_DBFLOW = 1;  // DBFlow


    int getStatus();

    boolean isInitialized();
    void initialize();
    void initialize(int dataBaseType);

    int getCurrentMode();
//    void switchToOnlineMode();
//    void switchToOfflineMode();

    void tryOnlineLogin();

    boolean IsAcceptEULA();
    void AcceptEULA();

    //region event bus registration
    void registerForEvents(Object object);
    void unregisterForEvents(Object object);
    //endregion


    //region slim communicator
//    void tryLoginWithLastCredential();
    void login(String url, Activity hostActivity);
    void logout();
    void getAllData();
    void refreshAllData();

    String getLastBreeds();
    void storeLastBreeds(String breeds);

    //endregion


    void enableStartupActivityLoaded();
    boolean isStartupActivityLoaded();
}
