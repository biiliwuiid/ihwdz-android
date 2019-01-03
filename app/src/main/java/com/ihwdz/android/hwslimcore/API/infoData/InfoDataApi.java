package com.ihwdz.android.hwslimcore.API.infoData;

import com.ihwdz.android.hwapp.model.bean.BottomNewsData;
import com.ihwdz.android.hwapp.model.bean.InfoData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :
 * version: 1.0
 * </pre>
 */
public interface InfoDataApi {

    /**
     * 每日讯
     * /app/article/news_fast?dateTimeStr 全部
       /app/article/news_fast_tiaojia?dateTimeStr 调价
       /app/article/news_fast_dongtai?dateTimeStr 动态
     */
    @GET("article/news_fast?dateTimeStr")
    Observable<InfoData> getAllData();

    @GET("article/news_fast")
    Observable<InfoData> getAllData( @Query("dateTimeStr") String yesterday);

    @GET("article/news_fast_tiaojia")
    Observable<InfoData> getPriceAdjustInfoData(@Query("dateTimeStr") String yesterday);

    @GET("article/news_fast_dongtai")
    Observable<InfoData> getDynamicInfoData(@Query("dateTimeStr") String yesterday);


    /**
     * news
     /app/index/index_list_fengyun 风云
     /app/index/index_list_shendu 深度
     /app/index/index_list_taotiao 头条
     /app/index/index_list_hangye 行业
     */

    // 时事新闻 -> 市场风云
    @GET("index/index_list_fengyun")
    Observable<BottomNewsData> getHomeBottomFyNew(@Query("pageNum") int pageNum,
                                            @Query("pageSize") int pageSize);

    // 行业聚焦 -> 深度研报
    @GET("index/index_list_shendu")
    Observable<BottomNewsData> getHomeBottomSdNew(@Query("pageNum") int pageNum,
                                                @Query("pageSize") int pageSize);

    // 行业热点
    @GET("index/index_list_hangye")
    Observable<BottomNewsData> getHomeBottomHyNew(@Query("pageNum") int pageNum,
                                                @Query("pageSize") int pageSize);
    // 市场评述 -> 宏观头条
    @GET("index/index_list_taotiao")
    Observable<BottomNewsData> getHomeBottomTtNew(@Query("pageNum") int pageNum,
                                                  @Query("pageSize") int pageSize);

}
