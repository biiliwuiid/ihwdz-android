package com.ihwdz.android.hwslimcore.LogUtil;

import android.content.Context;
import android.os.storage.StorageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class VolumeSelector {

    private StorageManager mStorageManager;
    private Method mMethodGetPaths;
    private Method mMethodGetPathsState;
    private String TAG="StorageList";

    static private VolumeSelector instance;
    static VolumeSelector getInstance(Context context){
        if(instance == null) {
            synchronized (VolumeSelector.class){
                if(instance != null){
                    return instance;
                }
                instance = new VolumeSelector();
                instance.init(context);
            }
        }
        return instance;
    }

    private VolumeSelector(){ }

    public void init(Context context)
    {
        mStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        try{
            mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");

            //getDeclaredMethod()---ignore permission
            mMethodGetPathsState=mStorageManager.getClass(). getMethod("getVolumeState", String.class);
        }catch(NoSuchMethodException ex){
            ex.printStackTrace();
        }
    }

    public String[] getVolumePaths(){
        String[] paths=null;
        try{
            paths=(String[])mMethodGetPaths.invoke(mStorageManager);
        }catch(InvocationTargetException ex){
            Logger.e(TAG, "error", ex);
        }catch(IllegalAccessException ex){
            Logger.e(TAG, "error", ex);
        }catch(IllegalArgumentException ex){
            Logger.e(TAG, "error", ex);
        }
        return paths;
    }

    public String getPathState(String path){
        String state=null;
        try{
            state=(String)mMethodGetPathsState.invoke(mStorageManager, path);

        }catch(IllegalArgumentException ex){
            Logger.e(TAG, "error", ex);
        }catch(IllegalAccessException ex){
            Logger.e(TAG, "error", ex);
        }catch(InvocationTargetException ex){
            Logger.e(TAG, "error", ex);
        }
        return state;
    }

    public String getInternalPath(){
        String[] paths = getVolumePaths();
        if(paths.length >= 1){
            return paths[0];
        }
        return null;
    }

    public List<String> getExternalPaths() {
        String[] paths = getVolumePaths();
        List<String> retPath = new ArrayList<>();
        if(paths.length >= 1){
            for(String path : paths){
                if(path == null) continue;
                if(path.equals(paths[0])) continue;

                if(path.toLowerCase().contains("sdcard")) {
                    String state = getPathState(path);
                    if(state != null && state.equalsIgnoreCase("mounted")) {
                        retPath.add(path);
                    }
                }
            }
        }
        return retPath;
    }
}
