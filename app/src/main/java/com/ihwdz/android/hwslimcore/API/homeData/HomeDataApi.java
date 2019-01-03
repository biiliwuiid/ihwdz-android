package com.ihwdz.android.hwslimcore.API.homeData;

import com.ihwdz.android.hwapp.model.bean.BreedData;
import com.ihwdz.android.hwapp.model.bean.CommentData;
import com.ihwdz.android.hwapp.model.bean.DealData;
import com.ihwdz.android.hwapp.model.bean.DetailData;
import com.ihwdz.android.hwapp.model.bean.FlushData;
import com.ihwdz.android.hwapp.model.bean.HomePageData;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.model.bean.RecommendData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.model.entity.HomePageNews;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public interface HomeDataApi {

    //  http://192.168.1.68:8082/app/index/index_data
    // 提交数据用测试环境 172.16.10.39:8082


    /**
     * 获取首页数据
     */
    @GET("index/index_data")
    Observable<HomePageData> getHomePageData();

    /**
     * 获取今日成交数据
     */
    @GET("index/trade_price_list")
    Observable<DealData> getDealData(@Query("breedList") String breedList);

    /**
     * 获取今日成交关注Breed
     */
    @GET("index/trade_breed_province")
    Observable<BreedData> getBreedData();




    /**
     * 分页数据: http://mb.ihwdz.com/app/index/bottom_news/第几页/请求个数
     * 分页数据: http://mb.ihwdz.com/app/index/bottom_news?pageNum&pageSize
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     *       eg: http://mb.ihwdz.com/app/index/bottom_news?pageNum=1&pageSize=15  // 分页默认pageSize为15
     *
     /app/index/index_list_fengyun 风云
     /app/index/index_list_shendu 深度
     /app/index/index_list_taotiao 头条
     /app/index/index_list_hangye 行业
     */

    // 时事新闻 -> 风云
//    @GET("index/bottom_news_shishi")
    @GET("index/index_list_fengyun")
    Observable<HomePageNews> getHomeBottomFyNew(@Query("pageNum") int pageNum,
                                                @Query("pageSize") int pageSize);

    // 行业聚焦 -> 深度
//    @GET("index/bottom_news")
    @GET("index/index_list_shendu")
    Observable<HomePageNews> getHomeBottomSdNew(@Query("pageNum") int pageNum,
                                                @Query("pageSize") int pageSize);
    // 市场评述 -> 头条
//    @GET("index/bottom_news_market")
    @GET("index/index_list_taotiao")
    Observable<HomePageNews> getHomeBottomTtNew(@Query("pageNum") int pageNum,
                                                @Query("pageSize") int pageSize);

    // 行业
    @GET("index/index_list_hangye")
    Observable<HomePageNews> getHomeBottomHyNew(@Query("pageNum") int pageNum,
                                                @Query("pageSize") int pageSize);

    /**
     *  /app/article/index_tewntyfour_list?pageNum&pageSize 　// hw24h >>more
     *  /app/article/index_list_recom?pageNum&pageSize         // 推荐更多　行业ABC
     *  /app/article/index_list_recom_yanjiu?pageNum&pageSize  // 推荐更多　行业研究
     */

    // 鸿网２４小时　更多
    @GET("article/index_tewntyfour_list")
    Observable<NewsData> getHw24hNews(@Query("pageNum") int pageNum,
                                      @Query("pageSize") int pageSize);

    // 推荐更多  行业ABC
    @GET("article/index_list_recom")
    Observable<NewsData> getRecommendNewsABC(@Query("pageNum") int pageNum,
                                             @Query("pageSize") int pageSize);
    // 推荐更多  行业研究
    @GET("article/index_list_recom_yanjiu")
    Observable<NewsData> getRecommendNews(@Query("pageNum") int pageNum,
                                          @Query("pageSize") int pageSize);


    /**
     * 文章详情
     /app/article/detail?id

     /app/article/query_comment?acticleId&pageNum&pageSize　查询评论
     /app/article/create_onerank_comment?token&articleId&content  POST一级评论
     /app/article/create_tworank_comment?token&articleId&content&commentId POST二级评论 (回复)
     /app/article/like_comment？commentId 点赞

     /app/article/detail_recommond?id     推荐


     */
    // 详情 DetailData
    @GET("article/detail")
    Observable<DetailData> getNewsDetailData(@Query("id") String id);

    // 精彩评论 - 查询评论 NewData ->RecommendData
    @GET("article/query_comment")
    Observable<CommentData> getCommentData(@Query("acticleId") String id,
                                           @Query("pageNum") int pageNum,
                                           @Query("pageSize") int pageSize);
    //　一级评论
    @FormUrlEncoded
    @POST("article/create_onerank_comment")
    Observable<CommentData> postCommentLevel1Data(@Field("token") String token,
                                                  @Field("articleId") String articleId,
                                                  @Field("content") String content);

    //　二级评论
    @FormUrlEncoded
    @POST("article/create_tworank_comment")
    Observable<CommentData> postCommentLevel2Data(@Field("token") String token,
                                                  @Field("articleId") String articleId,
                                                  @Field("content") String content,
                                                  @Field("commentId") String commentId);

    //  点赞 CommentData
    @GET("article/like_comment")
    Observable<CommentData> getLikeCommentData(@Query("commentId") String id);



    //　推荐 NewsData
    @GET("article/detail_recommond")
    Observable<RecommendData> getRecommendData(@Query("id") String id);


    /**
     * 收藏
     * member/save_collection?token&collectionType=0&contentId    收藏
     */
    @GET("member/save_collection")
    Observable<VerifyData> getCollectionData(@Query("token") String token,
                                             @Query("collectionType") String collectionType,
                                             @Query("contentId") String contentId);





    /**
     * 首页　每日讯－小红点
     /app/index/time_flush?fastNewsLastTime
     */
    //　首页　每日讯－小红点
    @GET("index/time_flush")
    Observable<FlushData> getFlushData(@Query("fastNewsLastTime") String fastNewsLastTime);
}
