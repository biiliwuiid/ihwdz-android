package com.ihwdz.android.hwslimcore.API.infoData;

import android.content.Context;

import com.ihwdz.android.hwapp.model.bean.BottomNewsData;
import com.ihwdz.android.hwapp.model.bean.InfoData;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;

import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :
 * version: 1.0
 * </pre>
 */
public class InfoDataModel {

    private static InfoDataModel model;
    private InfoDataApi mApiService;

    public InfoDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(InfoDataApi.class);
    }

    public static InfoDataModel getInstance(Context context){
        if(model == null) {
            model = new InfoDataModel(context);
        }
        return model;
    }

    /**
     *  全部
     */
    public Observable<InfoData> getAllData() {
        return mApiService.getAllData();
    }
    /**
     *  全部 next page
     */
    public Observable<InfoData> getAllData(String yesterday) {
        return mApiService.getAllData(yesterday);
    }

    /**
     *  调价
     */
    public Observable<InfoData> getPriceAdjustInfoData(String yesterday) {
        return mApiService.getPriceAdjustInfoData(yesterday);
    }
    /**
     *  动态
     */
    public Observable<InfoData> getDynamicInfoData(String yesterday) {
        return mApiService.getDynamicInfoData(yesterday);
    }


    /**
     *  市场风云
     */
    public Observable<BottomNewsData> getHomeBottomFyNew(int pageNum) {
        return mApiService.getHomeBottomFyNew(pageNum, 15);
    }

    /**
     *  深度研报
     */
    public Observable<BottomNewsData> getHomeBottomSdNew(int pageNum) {
        return mApiService.getHomeBottomSdNew(pageNum, 15);
    }
    /**
     *  行业热点
     */
    public Observable<BottomNewsData> getHomeBottomHyNew(int pageNum) {
        return mApiService.getHomeBottomHyNew(pageNum, 15);
    }
    /**
     *  宏观头条
     */
    public Observable<BottomNewsData> getHomeBottomTtNew(int pageNum) {
        return mApiService.getHomeBottomTtNew(pageNum, 15);
    }

}
