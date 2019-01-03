package com.ihwdz.android.hwapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isUnderWifiNetwork(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        boolean status = false ;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = true;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = false;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = false;
        }
        return status;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean validUrlChecker(String url){
        return false;
    }
}
