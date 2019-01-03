package com.ihwdz.android.hwslimcore.Base;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;
import com.ihwdz.android.hwslimcore.Settings.AppSettingsModule;
import com.ihwdz.android.hwslimcore.SlimConnector.SlimConnectorModule;
import com.squareup.otto.Bus;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
@Module(
//        complete = false,
//        library = true,
        includes = {
                //SlimConnectorModule.class,
                AppSettingsModule.class
        }
)
public class SlimCoreModule {
//    @Provides
//    @Singleton
//    ISlimMainController provideController(SlimMainController slimMainController){
//        return slimMainController;
//    }

    @Provides
    @Singleton
    ISlimMainController provideController(Context context){
        return new SlimMainController(context);
    }

    @Provides @Singleton
    Bus provideBus(){
        return SlimApplicationScheduler.getBus();
    }

    @Provides @Singleton
    ExecutorService provideMainExecutorService(){
        return SlimApplicationScheduler.getSlimMainExecutor();
    }

//    @Provides @Singleton
//    RequestQueue provideRequestQueue(@ForApplication Context context){
//        return Volley.newRequestQueue(context);
//    }
}
