package com.ihwdz.android.hwslimcore.API.indexData;

import android.content.Context;

import com.ihwdz.android.hwapp.model.bean.IndexData;
import com.ihwdz.android.hwapp.model.bean.InfoData;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;
import com.ihwdz.android.hwslimcore.API.clientData.ClientDataApi;
import com.ihwdz.android.hwslimcore.API.clientData.ClientDataModel;

import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class IndexDataModel {

    private static IndexDataModel model;
    private IndexDataApi mApiService;

    public IndexDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(IndexDataApi.class);
    }

    public static IndexDataModel getInstance(Context context){
        if(model == null) {
            model = new IndexDataModel(context);
        }
        return model;
    }

    public Observable<IndexData> getAllData() {
        return mApiService.getAllData();
    }
}
