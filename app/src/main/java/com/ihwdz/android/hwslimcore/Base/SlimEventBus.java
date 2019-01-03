package com.ihwdz.android.hwslimcore.Base;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class SlimEventBus extends Bus {

    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void post(final Object event) {
        if(Looper.myLooper() == Looper.getMainLooper()){
            super.post(event);
        }else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    SlimEventBus.super.post(event);
                }
            });
        }
    }
}
