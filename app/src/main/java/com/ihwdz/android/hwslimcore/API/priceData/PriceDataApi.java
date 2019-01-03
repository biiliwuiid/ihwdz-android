package com.ihwdz.android.hwslimcore.API.priceData;


import com.ihwdz.android.hwapp.model.bean.FactoryPriceData;
import com.ihwdz.android.hwapp.model.bean.MarketPriceData;
import com.ihwdz.android.hwapp.model.bean.PriceCollectionData;
import com.ihwdz.android.hwapp.model.bean.PriceData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public interface PriceDataApi {

    /**
     *  /app/price/market_price?pageNum&pageSize&breed&spec&city&brand&dateTimeStr  // only breeds
     * /app/price/factory_price?pageNum&pageSize&breed&spec&region&brand&dateTimeStr
     * @param pageNum
     * @param pageSize
     * @return
     */

    // 查价格 > 市场价
    @GET("price/market_price")
    Observable<MarketPriceData> getMarketPrice(@Query("token") String token,
                                               @Query("pageNum") int pageNum,
                                               @Query("pageSize") int pageSize,
                                               @Query("breed") String breed,
                                               @Query("spec") String spec,
                                               @Query("city") String city,
                                               @Query("brand") String brand,
                                               @Query("dateTimeStr") String dateTimeStr
                                             );
    // 查价格 > 出厂价
    @GET("price/factory_price")
    Observable<FactoryPriceData> getFactoryPrice(@Query("token") String token,
                                                 @Query("pageNum") int pageNum,
                                                 @Query("pageSize") int pageSize,
                                                 @Query("breed") String breed,
                                                 @Query("spec") String spec,
                                                 @Query("region") String region,
                                                 @Query("brand") String brand,
                                                 @Query("dateTimeStr") String dateTimeStr
                                             );


    // 查价格 > 市场价
    @GET("price/market_price")
    Observable<PriceData> getMarketPriceData(@Query("token") String token,
                                               @Query("pageNum") int pageNum,
                                               @Query("pageSize") int pageSize,
                                               @Query("breed") String breed,
                                               @Query("spec") String spec,
                                               @Query("city") String city,
                                               @Query("brand") String brand,
                                               @Query("dateTimeStr") String dateTimeStr
    );
    //查价格 > 出厂价
    @GET("price/factory_price")
    Observable<PriceData> getFactoryPriceData(@Query("token") String token,
                                       @Query("pageNum") int pageNum,
                                       @Query("pageSize") int pageSize,
                                       @Query("breed") String breed,
                                       @Query("spec") String spec,
                                       @Query("region") String region,
                                       @Query("brand") String brand,
                                       @Query("dateTimeStr") String dateTimeStr
                                       );

    /**
     * price/query_price_collection  查询收藏
     * price/price_collection 收藏 (动作)
     * price/cancle_price_collection 取消收藏 （动作）cancel
     */

    //查价格 > 我的收藏 查询收藏
    @GET("price/query_price_collection")
    Observable<PriceCollectionData> getPriceCollectedData(@Query("token") String token,
                                                          @Query("dateTimeStr") String dateTimeStr,
                                                          @Query("pageNum") int pageNum,
                                                          @Query("pageSize") int pageSize
                                                );
    //查价格 > 收藏 (动作)
    @GET("price/price_collection")
    Observable<VerifyData> getPriceCollect(@Query("token") String token,
                                           @Query("collectionType") int collectionType,// 0 市场价 1 出厂价
                                           @Query("breed") String breed,
                                           @Query("spec") String spec,
                                           @Query("brand") String brand,
                                           @Query("area") String area
                                           );
    //查价格 > 取消收藏 （动作）cancel
    @GET("price/cancel_price_collection")
    Observable<VerifyData> getPriceCollectedCancel(@Query("token") String token,
                                         @Query("collectionType") int collectionType,// 0 市场价 1 出厂价
                                         @Query("breed") String breed,
                                         @Query("spec") String spec,
                                         @Query("brand") String brand,
                                         @Query("area") String area
                                         );



}
