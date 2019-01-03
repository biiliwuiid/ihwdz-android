package com.ihwdz.android.hwslimcore.SlimConnector;

import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class APIResultHandlers {

    public interface RequestFailureHandler{
        void onRequestFailure(SlimAppError error);
    }

    public interface RequestFinishedHandler extends RequestFailureHandler{
        void onRequestSuccess();
    }

    public interface LoginFinishedHandler extends RequestFailureHandler{
        void onLoginFinished();
    }

    public interface LogoutFinishedHandler extends RequestFailureHandler{
        void onLogoutFinished();
    }




    public interface AuthorizationFinishedHandler extends RequestFailureHandler{
        void onAuthorizationFinished(String token,String userInfo);
    }

    public interface CheckLicenseFinishedHandler extends RequestFailureHandler{
        void onHasLicense();
    }



    // get HomePageData handler
    public interface GetDataHandler extends RequestFailureHandler{
        void onDataGot(HomePageData data);
    }



}
