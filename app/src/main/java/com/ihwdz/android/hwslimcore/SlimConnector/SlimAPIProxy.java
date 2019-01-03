package com.ihwdz.android.hwslimcore.SlimConnector;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.API.BaseAPIProxy;
import com.ihwdz.android.hwslimcore.API.BaseSlimAPI;
import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;
import com.ihwdz.android.hwslimcore.Util.WifiService;

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
public class SlimAPIProxy extends BaseAPIProxy {

    ExecutorService mExecutorService;
    SlimAPICreator apiCreator;
    Context context;
    WifiService wifiService;
    RequestQueue queue;
    ISlimAuthenticator slimAuthenticator;
    BaseSlimAPI mAPI;
    final static String TAG = "SlimAPIProxy";


    @Inject
    public SlimAPIProxy(@ForApplication Context context,
                        ExecutorService executorService,
                        WifiService wifiService,
                        RequestQueue queue,
                        SlimAPICreator apiCreator,
                        ISlimAuthenticator slimAuthenticator
    ){
        this.context = context;
        this.wifiService = wifiService;
        this.mExecutorService = executorService;
        this.apiCreator = apiCreator;
        this.queue = queue;
        this.slimAuthenticator = slimAuthenticator;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void authWithCredential(String strSlimIdentityToken) throws BaseAPIProxyError {
        if(mAPI == null){
            throw new BaseAPIProxyError(SlimAppError.create(SlimAppError.INVALID_URL));
        }else if(mAPI.getUrl() == null || strSlimIdentityToken == null || mAPI.getUrl().isEmpty() || strSlimIdentityToken.isEmpty()) {
            throw new BaseAPIProxyError(SlimAppError.create(SlimAppError.INVALID_CREDENTIAL));
        }else {
            mAPI.login(strSlimIdentityToken);
        }
    }

    @Override
    public void authAndCheckAsync(final String strSlimIdentityToken, final APIResultHandlers.LoginFinishedHandler handler) {

        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    authWithCredential(strSlimIdentityToken);

                    //BUG: Sometimes getLicense returns correct response while getData throws a 401 error. So use getJob to test if the login is success.
                    BaseSlimAPI.CheckLicenseResult result = mAPI.getLicense();
                    if(result.Error.hasError()){
                        if(result.Error.getHttpError() == 300){
                            handler.onRequestFailure(SlimAppError.create(SlimAppError.NOT_SLIM_URL));
                        }else {
                            handler.onRequestFailure(SlimAppError.createFromWebAPIError(result.Error));
                        }
                    }else {
                        //TODO: The License check api is not stable now. Let's ignore the result and mark it as success if there's valid result.
                        BaseSlimAPI.GetDataAPIResult getDataResult = mAPI.getData(false);
                        if(getDataResult.Error.hasError()){
                            if(handler!=null) {
                                handler.onRequestFailure(SlimAppError.create(SlimAppError.INVALID_CREDENTIAL));
                            }
                        }else{
                            if(handler!=null) {
                                handler.onLoginFinished();
                            }
                        }

                    }

                }catch (BaseAPIProxyError e) {
                    if(handler!=null) {
                        handler.onRequestFailure(e.error);
                    }
                }
            }
        });
    }

    @Override
    public void getAllDataAsync(final APIResultHandlers.GetDataHandler handler) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    HomePageData data = getAllData();
                    if (handler != null) {
                        handler.onDataGot(data);
                    }
                }catch (BaseAPIProxyError e){
                    if (handler != null) {
                        handler.onRequestFailure(e.error);
                    }
                }
            }
        });
    }

    private HomePageData getAllData() throws BaseAPIProxyError{
        if(mAPI == null){
            throw new BaseAPIProxyError(SlimAppError.create(SlimAppError.INVALID_URL));
        }else {
            BaseSlimAPI.GetDataAPIResult result = mAPI.getData();

            if (result.Error.hasError()) {

                if(result.Error.getHttpError() == 405){
                    throw new BaseAPIProxyError(SlimAppError.create(SlimAppError.NO_LICENSE));
                }

                throw new BaseAPIProxyError(SlimAppError.createFromWebAPIError(result.Error));
            } else if (result.isCancelled) {
                //Do nothing?
                throw new BaseAPIProxyError(SlimAppError.create(SlimAppError.OPERATION_CANCELLED));
            } else {
                return result.data;
            }
        }
    }
}
