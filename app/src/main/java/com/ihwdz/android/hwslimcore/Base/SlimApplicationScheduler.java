package com.ihwdz.android.hwslimcore.Base;

import com.squareup.otto.Bus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimApplicationScheduler {

    private static Bus eventBus;
    public static Bus getBus(){
        if(eventBus == null){
            eventBus = new SlimEventBus();
        }
        return eventBus;
    }

    private static ExecutorService backgroundExecutorService;
    public static ExecutorService getSlimMainExecutor(){
        if(backgroundExecutorService == null){
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("slim-main-work-thread-%d").build();
            backgroundExecutorService = Executors.newFixedThreadPool(4, namedThreadFactory);
        }
        return backgroundExecutorService;
    }
}
