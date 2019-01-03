package com.ihwdz.android.hwapp;

import android.app.Application;

import com.ihwdz.android.hwslimcore.LogUtil.Logger;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimApplication extends Application {

    //ObjectGraph applicationGraph;  //dagger has no this now?

    static final String TAG = "SlimApplication";
    @Inject
    IAppSettings settings;

    @Override
    public void onCreate() {
        Date d1 = new Date();
        Logger.i(TAG, "onCreate:" + d1.getTime());
        super.onCreate();

//        CrashHandler.getInstance().init(this.getApplicationContext());
    }

//    public ObjectGraph getApplicationGraph() {
//        return applicationGraph;
//    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(new SlimApplicationModule(this));
    }

}
