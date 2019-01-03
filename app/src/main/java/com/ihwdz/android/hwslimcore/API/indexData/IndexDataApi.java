package com.ihwdz.android.hwslimcore.API.indexData;

import com.ihwdz.android.hwapp.model.bean.IndexData;

import retrofit2.http.GET;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public interface IndexDataApi {

    /**
     * 看指数
     /app/mark/index 看指数

     看指数H5页面　no header
     http://mb.ihwdz.com/ios/price_trend.html?technology=电石法'电石法';//利润//利润走势H5
     H5
     http://mb.ihwdz.com/ios/price_difference.html; //期 //期 //期现价差H5

     看指数H5页面 with header
     http://mb.ihwdz.com/android/price_trend.html?technology=''&breed=9xx'"电石法";//利润//利润走势H5
    http://mb.ihwdz.com/android/price_difference.html?breed=9xx'; //期 //期现价差H5
     */


    @GET("mark/index")
    Observable<IndexData> getAllData();
}
