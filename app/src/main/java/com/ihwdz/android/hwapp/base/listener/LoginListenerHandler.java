package com.ihwdz.android.hwapp.base.listener;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/28
 * desc :   监听是否登录成功
 * version: 1.0
 * </pre>
 */
public class LoginListenerHandler {

    public static OnLoginSuccessListener mLoginSuccessListener;

    public LoginListenerHandler(OnLoginSuccessListener listener){
        this.mLoginSuccessListener = listener;
    }

    public static OnLoginSuccessListener getLoginSuccessListener(){
        return mLoginSuccessListener;
    }

    public void cancel(){
        mLoginSuccessListener = null;
    }
}
