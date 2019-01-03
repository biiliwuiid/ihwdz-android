package com.ihwdz.android.hwslimcore.Base;

import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.API.RequestEvent;
import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;
import com.ihwdz.android.hwslimcore.SlimConnector.APIResultHandlers;
import com.squareup.otto.Bus;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimMainControllerHandler implements
        APIResultHandlers.GetDataHandler,
        APIResultHandlers.RequestFinishedHandler,
        APIResultHandlers.LoginFinishedHandler,
        APIResultHandlers.LogoutFinishedHandler{

    final Bus mEventBus;

    public SlimMainControllerHandler(Bus eventBus) {
        this.mEventBus = eventBus;
    }


    @Override
    public void onDataGot(HomePageData data) {
        mEventBus.post(new RequestEvent.DataGotEvent(data));
    }

    @Override
    public void onRequestFailure(SlimAppError error) {
        this.mEventBus.post(new RequestEvent.OperationFailedEvent(error));
    }
    @Override
    public void onRequestSuccess() {
        this.mEventBus.post(new RequestEvent.OperationSuccessEvent());
    }

    @Override
    public void onLoginFinished() {
        this.mEventBus.post(new RequestEvent.LoginFinishedEvent());
    }

    @Override
    public void onLogoutFinished() {
        this.mEventBus.post(new RequestEvent.LogOutFinishedEvent());
    }
}
