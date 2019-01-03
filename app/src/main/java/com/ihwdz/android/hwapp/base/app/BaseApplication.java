package com.ihwdz.android.hwapp.base.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.baidu.mobstat.StatService;
import com.blankj.utilcode.util.Utils;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.service.InitializeService;
import com.ihwdz.android.hwapp.utils.log.LogCallback;
import com.squareup.otto.Bus;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import cn.ycbjie.ycthreadpoollib.PoolThread;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dell on 2018/7/24.
 *
 * 1.实现HasActivityInjector接口

 2.实现HasActivityInjector接口的activityInjector()方法

 3.声明一个泛型为Activity的DispatchingAndroidInjector成员变量并在activityInjector()方法中返回
 */

public class BaseApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    private static BaseApplication instance;
    private PoolThread executor;
    private Realm realm;
    private ExecutorService executorService;

//    @Inject
//    ISlimMainController controller;

    Bus mBus;
    Context mCxt;

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication(){}

    public void registerForEvents(Object receiver)
    {
        try
        {
            mBus.register(receiver);
        }catch (Exception e)
        {
            //????
        }
    }
    public void unregisterForEvents(Object receiver) {
        try
        {
            mBus.unregister(receiver);
        }catch (Exception e)
        {
            //????
        }
    }
    boolean initialized = false;
    public void initialize(Context cxt,Bus bus )
    {
        if(!initialized) {
            initialized = true;
            mCxt = cxt;
            mBus = bus;
        }

    }

    /**
     * 最先执行
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 程序启动的时候执行
     */
    @Override
    public void onCreate() {
        Log.d("Application", "onCreate");
        super.onCreate();
        //先写AppComponent接口，然后编译，才能让编译器自动生成DaggerMyAppComponent类。
        DaggerAppComponent.create().inject(this);

        // 百度统计
        StatService.start(this);

        // Jpush
        // 初始化sdk
        // TODO: 2018/12/29  正式版的时候设置 JPushInterface.setDebugMode -- false，关闭调试 （2018-12-29 发现setDebugMode(false)会收不到推送）
        JPushInterface.setDebugMode(true); // 正式版的时候设置false，关闭调试
        JPushInterface.init(this);

        // 添加tag标签，发送消息的之后就可以指定tag标签来发送了
//        Set<String> set = new HashSet<>();
//        set.add("andfixdemo");             // 名字任意，可多添加几个
//        set.add("test_debug");
//        JPushInterface.setTags(this, set, null); //设置标签

        instance = this;
        executorService = Executors.newSingleThreadExecutor();
        Utils.init(this);
        BaseLifecycleCallback.getInstance().init(this);
        initRealm();

        initData();

        initThreadPool();
        AppConfig.INSTANCE.initConfig();
        //在子线程中初始化
        InitializeService.start(this);
    }




    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        Log.d("Application", "onTerminate");
        super.onTerminate();
        if(realm!=null){
            realm.close();
            realm = null;
        }
        if(executor!=null){
            executor.close();
            executor = null;
        }
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        Log.d("Application", "onLowMemory");
        super.onLowMemory();
    }


    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        Log.d("Application", "onTrimMemory");
        super.onTrimMemory(level);
    }


    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Application", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化数据库
     */
    private void initRealm() {
        File file ;
        try {
            file = new File(Constant.ExternalStorageDirectory, Constant.DATABASE_FILE_PATH_FOLDER);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        } catch (Exception e) {
            Log.e("异常",e.getMessage());
        }
        Realm.init(instance);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
    }


    /**
     * 获取Realm数据库对象
     * @return              realm对象
     */
    public Realm getRealmHelper() {
        return realm;
    }

    /**
     * 初始化线程池管理器
     */
    private void initThreadPool() {
        // 创建一个独立的实例进行使用
        executor = PoolThread.ThreadBuilder
                .createFixed(6)
                .setPriority(Thread.MAX_PRIORITY)
                .setCallback(new LogCallback())
                .build();
    }

    /**
     * 获取线程池管理器对象，统一的管理器维护所有的线程池
     * @return                      executor对象
     */
    public PoolThread getExecutor(){
        return executor;
    }


    private void initData(){

    }



    // Dagger2 android
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

//    public static byte[] getAssertsFile(Context context, String fileName) {
//        InputStream inputStream = null;
//        AssetManager assetManager = context.getAssets();
//        try {
//            inputStream = assetManager.open(fileName);
//            if (inputStream == null) {
//                return null;
//            }
//
//            BufferedInputStream bis = null;
//            int length;
//            try {
//                bis = new BufferedInputStream(inputStream);
//                length = bis.available();
//                byte[] data = new byte[length];
//                bis.read(data);
//
//                return data;
//            } catch (IOException e) {
//
//            } finally {
//                if (bis != null) {
//                    try {
//                        bis.close();
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

//    public void parseJsonData(JSONObject data){
//
//        final HomePageEntity result = new HomePageEntity();
//        final HomePageData result_data = new HomePageData();
//        JSONObject object = data;
//        if (object != null) {
//            //
//            final String code = object.optString(HomePageData.CODE);
//            if (!TextUtils.isEmpty(code) && code.equals("0") ){
//            }
//
//        }
//    }

}
