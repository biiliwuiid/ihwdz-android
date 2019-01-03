package com.ihwdz.android.hwslimcore.API.materialData;

import com.ihwdz.android.hwapp.model.bean.MaterialData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public interface MaterialDataApi {

    /**
     * 买原料
     /app/item/index?breed&spec&brand&wareCity&pageNum&pageSize  // select form menu
     /app/item/keyword?keyword&pageNum&pageSize                  // search box

     物性表：
     /app/attr/home  首页
     /app/attr/list?keyword=HP181&pageNum&pageSize 列表
     /app/attr/detail?baseId= 详情
     */
    @GET("item/index")
    Observable<MaterialData> getAllData();



    @GET("item/index")
    Observable<MaterialData> getAllData(@Query("breed") String breed,
                                        @Query("spec") String spec,
                                        @Query("wareCity") String city,
                                        @Query("brand") String brand,
                                        @Query("pageNum") int pageNum,
                                        @Query("pageSize") int pageSize
    );

    @GET("item/keyword")
    Observable<MaterialData> getSearchData(@Query("keyword") String keyword,
                                           @Query("pageNum") int pageNum,
                                           @Query("pageSize") int pageSize
    );
}
