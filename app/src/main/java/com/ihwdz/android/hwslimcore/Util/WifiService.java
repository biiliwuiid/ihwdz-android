package com.ihwdz.android.hwslimcore.Util;

import android.content.Context;

import com.ihwdz.android.hwslimcore.LogUtil.Logger;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class WifiService {

    static public class NoNetworkAccessException extends Exception
    {
        public NoNetworkAccessException()
        {
            super();
        }
    }

    Context mContext;
    public WifiService(Context context){
        mContext = context;
    }
    public void makeSureNetworkConnectionIsAvailable() throws NoNetworkAccessException {
        if(!NetworkUtil.isNetworkAvailable(mContext))
        {
            Logger.e("WifiService", "!!!Mobile wifi is off!!!");
            throw new NoNetworkAccessException();
        }
    }
}
