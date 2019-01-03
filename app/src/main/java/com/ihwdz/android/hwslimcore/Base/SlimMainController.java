package com.ihwdz.android.hwslimcore.Base;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.base.app.BaseApplication;
import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.API.BaseAPIProxy;
import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;
import com.ihwdz.android.hwslimcore.LogUtil.FileHelper;
import com.ihwdz.android.hwslimcore.LogUtil.Logger;
import com.ihwdz.android.hwslimcore.OfflineConnector.OfflineDataController;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.SlimConnector.APIResultHandlers;
import com.ihwdz.android.hwslimcore.SlimConnector.SlimAPICreator;
import com.ihwdz.android.hwslimcore.Util.WifiService;
import com.squareup.otto.Bus;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import io.realm.Realm;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimMainController implements ISlimMainController {

    Context mContext;

    SlimMainControllerHandler eventHandler = null;
    //final Bus eventBus;
    ExecutorService backgroundExecutorService;
    //final SlimAPICreator apiCreator;
    //public final IAppSettings settings;
    //final WifiService wifiService;

    @Inject IAppSettings settings;

    OfflineDataController offlineDataController;
    HomePageData cacheData = null;

    @Inject @Named("online")
    Lazy<BaseAPIProxy> onlineProxy;
//    @Inject @ForOfflineUploading Lazy<BaseAPIProxy> offlineProxy;
    SoftReference<BaseAPIProxy> currentProxyRef;



    public SlimMainController(@ForApplication Context applicationContext,
                       Bus eventBus,
                       ExecutorService backgroundExecutorService,
                       SlimAPICreator apiCreator,
                       IAppSettings settings,
                       WifiService wifiService,
                       OfflineDataController offlineDataController
    ) {
        eventHandler = new SlimMainControllerHandler(eventBus);
//        this.mContext = applicationContext;
        this.backgroundExecutorService = backgroundExecutorService;
//        this.eventBus = eventBus;
//        this.wifiService = wifiService;
//        this.settings = settings;
//        this.apiCreator = apiCreator;
//        this.offlineDataController = offlineDataController;

    }
    @Inject
    public SlimMainController(Context context){
        this.mContext = context;
    }
    static final String TAG = "SlimMainController";


    int mStatus = SLIM_STATUS_IDLE;

    @Override
    public int getStatus() {
        return mStatus;
    }

    Boolean isInitialized = false;

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }


    @Override
    public void initialize() {
        if (!isInitialized) {
            isInitialized = true;
            backgroundExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    // clear cache
                    FileHelper.clearCache(mContext);

                    // TODO: 2018/8/3   data base cache   DBFlow
//                    FlowManager.init(mContext);

                    // TODO: 2018/8/3   data base cache   Realm
                    //Realm.init(mContext);
                    //BaseApplication.getInstance().initialize(mContext,eventBus);
//                    switchToOnlineProxy();
                }});
        }
    }

    @Override
    public void initialize(int dataBaseType) {

    }

    int currentAPIMode = API_MODE_ONLINE;
    @Override
    public int getCurrentMode() {
        return currentAPIMode;
    }


    @Override
    public void tryOnlineLogin() {

        // TODO: 2018/8/3  login options
    }

    @Override
    public boolean IsAcceptEULA() {
        return settings.IsAcceptEULA();
    }

    @Override
    public void AcceptEULA() {
        settings.acceptEULA();
    }

    @Override
    public void registerForEvents(Object object) {
//        try {
//            eventBus.register(object);
//        }
//        catch (Exception ex)
//        {}
    }

    @Override
    public void unregisterForEvents(Object object) {
//        try {
//            eventBus.unregister(object);
//        }
//        catch (Exception ex)
//        {}
    }

    @Override
    public void login(String url, Activity hostActivity) {
        String userInfo = this.settings.getLastUserName();
        mStatus = SLIM_STATUS_LOGIN;
        currentProxyRef.get().auth(hostActivity, userInfo,new APIResultHandlers.AuthorizationFinishedHandler() {
            @Override
            public void onAuthorizationFinished(String token,String userInfo) {
                if(!userInfo.isEmpty())
                    settings.storeUserNameString(userInfo);

//                settings.storeCookieString(token);
//                settings.storeLastInputUrl(currentProxyRef.get().getUrl());
//                switchToOnlineProxy();
                mStatus = SLIM_STATUS_UPLOADING;
                mStatus = SLIM_STATUS_IDLE;
                cacheData = null;
                eventHandler.onLoginFinished();
            }

            @Override
            public void onRequestFailure(final SlimAppError error) {
                if(error.isConnectionError()) {

                }else {
                    mStatus = SLIM_STATUS_IDLE;
                    eventHandler.onRequestFailure(error);
                }
            }
        });
    }

    @Override
    public void logout() {

        mStatus = SLIM_STATUS_IDLE;
        currentProxyRef.get().logout(new APIResultHandlers.LogoutFinishedHandler() {
            @Override
            public void onLogoutFinished() {
            // TODO: 2018/8/3  onLogoutFinished
                eventHandler.onLogoutFinished();
            }

            @Override
            public void onRequestFailure(SlimAppError error) {
                eventHandler.onRequestFailure(error);
            }
        });
    }

    @Override
    public void getAllData() {
        if(cacheData != null){
            eventHandler.onDataGot(cacheData);
        }else {
            refreshAllData();
        }
    }

    @Override
    public void refreshAllData() {
        Logger.i(TAG, "start refreshAllJobs");
        currentProxyRef.get().getAllDataAsync(new APIResultHandlers.GetDataHandler() {

            @Override
            public void onDataGot(final HomePageData data) {
                // don't need login to get this data
                try {
                    cacheData = data;
                    settings.storeLastData(data);

                    if (currentAPIMode == API_MODE_ONLINE){
                        offlineDataController.updateLocalDataAsync(
                                data,
                                new OfflineDataController.UpdateLocalDataFinishedHandler() {
                                    @Override
                                    public void onUpdateLocalDataFinished() {
                                        eventHandler.onDataGot(data);
                                    }
                                }
                        );
                    }else {
                        eventHandler.onDataGot(data);
                    }

                }catch (Exception e){
                    Logger.e(TAG, "refreshAllData", e);
                }

            }

            @Override
            public void onRequestFailure(SlimAppError error) {
                Logger.e(TAG, "refreshAllData Failed. error: " + error.error);
                eventHandler.onRequestFailure(error);
            }
        });
    }

    @Override
    public String getLastBreeds() {
        return settings.getLastBreeds();
    }

    @Override
    public void storeLastBreeds(String breeds) {
        settings.storeLastBreeds(breeds);
    }


    boolean bStartupActivityLoaded = false;
    @Override
    public void enableStartupActivityLoaded() {
        bStartupActivityLoaded = true;
    }

    @Override
    public boolean isStartupActivityLoaded() {
        return bStartupActivityLoaded;
    }
}
