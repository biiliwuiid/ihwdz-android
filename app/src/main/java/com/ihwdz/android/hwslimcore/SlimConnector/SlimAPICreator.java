package com.ihwdz.android.hwslimcore.SlimConnector;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.ihwdz.android.hwslimcore.API.BaseSlimAPI;
import com.ihwdz.android.hwslimcore.Util.WifiService;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public interface SlimAPICreator {

    BaseSlimAPI create(Context context, String serverAddress, WifiService wifiService, RequestQueue queue);
}
