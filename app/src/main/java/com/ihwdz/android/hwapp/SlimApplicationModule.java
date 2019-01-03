package com.ihwdz.android.hwapp;

import android.content.Context;

import com.ihwdz.android.hwslimcore.Base.SlimCoreModule;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;

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
//        injects = SlimApplication.class,
        includes = SlimCoreModule.class
//        library = true
)
public class SlimApplicationModule {

    Context applicationContext;

    public SlimApplicationModule(Context applicationContext){
        this.applicationContext = applicationContext;
    }

    @Provides
    @ForApplication
    Context provideApplicationContext(){
        return applicationContext;
    }
}
