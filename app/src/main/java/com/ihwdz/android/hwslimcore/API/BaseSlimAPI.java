package com.ihwdz.android.hwslimcore.API;

import com.ihwdz.android.hwapp.model.entity.HomePageData;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public abstract class BaseSlimAPI {

    // http://192.168.1.68:8082/app/index/index_data

    // http://192.168.1.68:8082/app/index/bottom_news

    // http://192.168.1.68:8082/app/price/factory_price

    // http://192.168.1.68:8082/app/price/market_price


    protected final String serverAddress;

    protected BaseSlimAPI(String serverAddress){
        this.serverAddress = serverAddress;
    }

    public String getUrl(){
        return serverAddress;
    }

    public static class SlimAPIError
    {
        public SlimAPIError()
        {
            HttpError = 200;
            LocalError = 0;
        }
        public static BaseSlimAPI.SlimAPIError noError()
        {
            BaseSlimAPI.SlimAPIError err = new BaseSlimAPI.SlimAPIError();
            return err;
        }
        public static BaseSlimAPI.SlimAPIError HttpError(int error)
        {
            BaseSlimAPI.SlimAPIError err = new BaseSlimAPI.SlimAPIError();
            err.HttpError = error;
            return err;
        }
        public static BaseSlimAPI.SlimAPIError UnknownError()
        {
            BaseSlimAPI.SlimAPIError err = new BaseSlimAPI.SlimAPIError();
            err.HttpError = 999; //What should be here???
            return err;
        }
        public static BaseSlimAPI.SlimAPIError LocalError(int error)
        {
            BaseSlimAPI.SlimAPIError err = new BaseSlimAPI.SlimAPIError();
            err.LocalError = error;
            return err;
        }
        public static BaseSlimAPI.SlimAPIError ConnectionError(int error)
        {
            BaseSlimAPI.SlimAPIError err = new BaseSlimAPI.SlimAPIError();
            err.ConnectionError = error;
            return err;
        }
        public boolean hasError(){
            if(HttpError != 200  || LocalError != 0 || ConnectionError!=0)
                return true;

            return  false;
        }
        public boolean hasConnectionError()
        {
            if(ConnectionError!=0)
            {
                return true;
            }
            return false;
        }

        public  int getHttpError()
        {
            return this.HttpError;
        }

        public  int getLocalError()
        {
            return this.LocalError;
        }

        public  int getConnectionError()
        {
            return this.ConnectionError;
        }

        int HttpError;

        // 0:OK,1 - Storage failed, 2 - Invalid repsonse
        int LocalError;

        // 0:OK,1 - No internet found
        int ConnectionError;
    }

    public static class SlimApiResult
    {
        public BaseSlimAPI.SlimAPIError Error;
        public boolean isCancelled;
        public SlimApiResult()
        {
            Error = BaseSlimAPI.SlimAPIError.noError();
        }
    }


    Boolean isLoggedIn = false;
    final public void login(String strSlimIdentityToken){
        if(doLogin(strSlimIdentityToken)) {
            isLoggedIn = true;
        }
    }

    final public void logout(){
        if(doLogout()) {
            isLoggedIn = false;
        }
    }

    public Boolean getIsLoggedIn(){
        return isLoggedIn;
    }

    abstract public boolean doLogin(String token);
    abstract public boolean doLogout();

    class NotLoggedInException extends Exception{

    }

    public static class CheckLicenseResult extends SlimApiResult{
        public CheckLicenseResult(){
            super();
        }
    }
    abstract public CheckLicenseResult getLicense();



    public static class GetDataAPIResult extends BaseSlimAPI.SlimApiResult {
        public HomePageData data;
        public GetDataAPIResult(){
            super();
        }
    }

    public BaseSlimAPI.GetDataAPIResult getData(){
        return getData(true);
    }

    abstract public BaseSlimAPI.GetDataAPIResult getData(Boolean containsAllProperties);
}
