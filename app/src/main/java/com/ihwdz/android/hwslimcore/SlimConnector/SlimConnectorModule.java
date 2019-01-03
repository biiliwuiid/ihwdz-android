package com.ihwdz.android.hwslimcore.SlimConnector;

import android.content.Context;

import com.ihwdz.android.hwslimcore.API.BaseAPIProxy;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;
import com.ihwdz.android.hwslimcore.Util.WifiService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */

@Module(
//        complete = false,
//        includes = {
//        },
//        library = true
)
public class SlimConnectorModule {

    @Provides
    @Singleton
    @Named("online")
    BaseAPIProxy provideOnlineProxy(SlimAPIProxy proxy){
        return proxy;
    }

    @Provides
    @Singleton
    WifiService provideWifiService(@ForApplication Context context){
        return new WifiService(context);
    }
}
