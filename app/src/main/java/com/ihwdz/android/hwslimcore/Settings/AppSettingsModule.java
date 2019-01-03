package com.ihwdz.android.hwslimcore.Settings;

import android.content.Context;

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
// TODO: 2018/8/2  dagger2 have no this ? should do what
//@Module(complete = false,
//        library = true)

@Module
public class AppSettingsModule {

//    @Provides
//    IAppSettings provideSlimAppSettings(SlimAppSettings appSettings){
//        return appSettings;
//    }

    @Provides
    IAppSettings provideSlimAppSettings(Context context){
        return new SlimAppSettings(context);
    }
}
