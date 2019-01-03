package com.ihwdz.android.hwapp.base.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class AppModule {

//    private BaseApplication application;
//
//    public AppModule(BaseApplication application) {
//        this.application = application;
//    }
//
//
//     //提供全局的 SharedPreferences 对象
//    @Singleton
//    @Provides
//    SharedPreferences provideSharedPreferences(){
//        return application.getSharedPreferences("spfile", Context.MODE_PRIVATE);
//    }
//
//    @Singleton
//    @Provides
//    public BaseApplication provideApplication() {
//        return application;
//    }

    @Singleton
    @Provides
    public CompositeSubscription provideSubscription() {
        return new CompositeSubscription();
    }


}
