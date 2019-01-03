package com.ihwdz.android.hwapp.base.app;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :
 * version: 1.0
 * </pre>
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        AllActivitiesModule.class

})
public interface AppComponent {

    void inject(BaseApplication application);

}
