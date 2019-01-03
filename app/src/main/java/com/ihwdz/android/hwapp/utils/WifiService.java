package com.ihwdz.android.hwapp.utils;

import android.content.Context;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
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
            throw new NoNetworkAccessException();
        }
    }
}
