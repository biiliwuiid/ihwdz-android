package com.ihwdz.android.hwslimcore.API;

import android.app.Activity;

import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;
import com.ihwdz.android.hwslimcore.SlimConnector.APIResultHandlers;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public abstract class BaseAPIProxy {

    public class BaseAPIProxyError extends Exception{
        public SlimAppError error;
        public BaseAPIProxyError(SlimAppError error){
            this.error = error;
        }
    }

    public void logout(final APIResultHandlers.LogoutFinishedHandler handler){
        if(handler != null) {
            handler.onLogoutFinished();
        }
    }

    abstract public boolean isLoggedIn();


    // no use now start
    abstract public void authWithCredential(final String strSlimIdentityToken) throws BaseAPIProxyError;

    abstract public void authAndCheckAsync(final String strSlimIdentityToken, final APIResultHandlers.LoginFinishedHandler handler);

    public void auth(Activity hostActivity, String userInfo, final APIResultHandlers.AuthorizationFinishedHandler handler){
        if(handler != null) {
            handler.onRequestFailure(SlimAppError.create(SlimAppError.OPERATION_NOT_SUPPORTED));
        }
    }
    public void checkLicenseAsync(APIResultHandlers.CheckLicenseFinishedHandler handler){
        handler.onHasLicense();
    }
    // no use now end


    abstract public void getAllDataAsync(final APIResultHandlers.GetDataHandler handler);
}
