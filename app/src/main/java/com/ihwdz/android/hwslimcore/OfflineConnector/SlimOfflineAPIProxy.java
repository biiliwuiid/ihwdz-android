package com.ihwdz.android.hwslimcore.OfflineConnector;

import android.content.Context;

import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.API.BaseAPIProxy;
import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.SlimConnector.APIResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimOfflineAPIProxy extends BaseAPIProxy {

    Context context;
    ExecutorService mainBackgroundExecutorService;
    IAppSettings settings;
    ExecutorService databaseExecutorService;

    String addressUrl;
    String strSlimIdentityToken;

    @Inject
    SlimOfflineAPIProxy(@ForApplication Context context,
                        ExecutorService backgroundExecutorService,
                        IAppSettings settings,
                        @ForOfflineUploading ExecutorService databaseExecutorService) {
        this.context = context;
        this.mainBackgroundExecutorService = backgroundExecutorService;
        this.settings = settings;
        this.databaseExecutorService = databaseExecutorService;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public void authWithCredential(String strSlimIdentityToken) throws BaseAPIProxyError {
        if(strSlimIdentityToken != null && !strSlimIdentityToken.isEmpty()){
            this.strSlimIdentityToken = strSlimIdentityToken;
        }else {
            throw new BaseAPIProxyError(SlimAppError.create(SlimAppError.OPERATION_NOT_SUPPORTED));
        }
    }

    @Override
    public void authAndCheckAsync(String strSlimIdentityToken, APIResultHandlers.LoginFinishedHandler handler) {

    }

    @Override
    public void getAllDataAsync(final APIResultHandlers.GetDataHandler handler) {
        mainBackgroundExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                HomePageData data = settings.getLastData();
                if(data == null) {
                    handler.onDataGot(new HomePageData());
                }else{
                    handler.onDataGot(data);
                }
            }
        });
    }
}
