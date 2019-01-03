package com.ihwdz.android.hwslimcore.API.materialData;

import android.content.Context;

import com.ihwdz.android.hwapp.model.bean.IndexData;
import com.ihwdz.android.hwapp.model.bean.MaterialData;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;

import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class MaterialDataModel {

    private static MaterialDataModel model;
    private MaterialDataApi mApiService;

    public MaterialDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(MaterialDataApi.class);
    }

    public static MaterialDataModel getInstance(Context context){
        if(model == null) {
            model = new MaterialDataModel(context);
        }
        return model;
    }

    /**
     * 从菜单栏选择; 默认加载
     * @param breed
     * @param spec
     * @param city
     * @param brand
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Observable<MaterialData> getAllData(String breed, String spec, String city, String brand, int pageNum, int pageSize ) {
        return mApiService.getAllData(breed, spec, city, brand, pageNum, pageSize);
    }

    /**
     * 搜索框 搜索数据
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Observable<MaterialData> getSearchData(String keyword, int pageNum, int pageSize) {
        return mApiService.getSearchData(keyword, pageNum, pageSize);
    }
}
