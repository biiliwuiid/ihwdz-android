package com.ihwdz.android.hwslimcore.API;

import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.ErrorHandler.SlimAppError;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class RequestEvent {

    public static class OperationSuccessEvent {
        public OperationSuccessEvent(){
        }
    }

    public static class OperationFailedEvent {
        public SlimAppError error;
        public OperationFailedEvent(SlimAppError error){
            this.error = error;
        }
    }

    public static class LoginFinishedEvent extends OperationSuccessEvent{

    }

    public static class LogOutFinishedEvent extends OperationSuccessEvent{

    }


    // HomePageData 首页 上部分数据（except bottom news）
    public static class DataGotEvent extends OperationSuccessEvent {
        public HomePageData data;
        public DataGotEvent(HomePageData homePageData){
            this.data = homePageData;
        }
    }


    public static class GetDataFailedEvent extends OperationFailedEvent {
        public GetDataFailedEvent(SlimAppError error) {
            super(error);
        }
    }



}
