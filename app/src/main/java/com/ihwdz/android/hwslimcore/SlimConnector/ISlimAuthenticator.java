package com.ihwdz.android.hwslimcore.SlimConnector;

import android.app.Activity;

import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */
public interface ISlimAuthenticator {
    interface AuthenticateFinishedHandler {
        void onSuccess(String strSlimIdentityToken,String strUserInfo);
        void onFailure(SlimAppError error);
    }
    void auth(Activity hostActivity, String baseUrl, String strUserInfo, AuthenticateFinishedHandler handler);
}
