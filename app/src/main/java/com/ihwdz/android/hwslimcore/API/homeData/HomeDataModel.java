package com.ihwdz.android.hwslimcore.API.homeData;

import android.content.Context;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.BreedData;
import com.ihwdz.android.hwapp.model.bean.CommentData;
import com.ihwdz.android.hwapp.model.bean.DealData;
import com.ihwdz.android.hwapp.model.bean.DetailData;
//import com.ihwdz.android.hwapp.model.bean.NewData;
import com.ihwdz.android.hwapp.model.bean.FlushData;
import com.ihwdz.android.hwapp.model.bean.HomePageData;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.model.bean.RecommendData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.model.entity.HomePageNews;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;

import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class HomeDataModel {

    private static HomeDataModel model;
    private HomeDataApi mApiService;

    public HomeDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(HomeDataApi.class);
    }

    public static HomeDataModel getInstance(Context context){
        if(model == null) {
            model = new HomeDataModel(context);
        }
        return model;
    }

    /**
     * 首页
     */
    public Observable<HomePageData> getHomePageData() {
        return mApiService.getHomePageData();
    }
    /**
     * 获取今日成交数据
     */
    public Observable<DealData> getDealData(String breedList) {
        return mApiService.getDealData(breedList);
    }
    /**
     * 获取今日成交关注数据
     */
    public Observable<BreedData> getBreedData() {
        return mApiService.getBreedData();
    }


    /**
     /app/index/index_list_fengyun 风云
     /app/index/index_list_shendu 深度
     /app/index/index_list_taotiao 头条
     /app/index/index_list_hangye 行业
     */

    /**
     * 时事新闻 - 市场风云
     */
    public Observable<HomePageNews> getHomePageCurrentNews(int pageNum, int pageSize) {
        return mApiService.getHomeBottomFyNew(pageNum, pageSize);
    }
    /**
     * 行业聚焦 - 深度调研
     */
    public Observable<HomePageNews> getHomePageIndustryNews(int pageNum, int pageSize) {
        return mApiService.getHomeBottomSdNew(pageNum, pageSize);
    }
    /**
     * 市场评述 - 行业热点
     */
    public Observable<HomePageNews> getHomePageMarketNews(int pageNum, int pageSize) {
        return mApiService.getHomeBottomTtNew(pageNum, pageSize);
    }

    /**
     * 市场评述 - 宏观头条
     */
    public Observable<HomePageNews> getHomePageHyNews(int pageNum, int pageSize) {
        return mApiService.getHomeBottomHyNew(pageNum, pageSize);
    }


    /**
     * Hw24h
     */
    public Observable<NewsData> getHw24hData(int pageNum, int pageSize){
        return mApiService.getHw24hNews(pageNum, pageSize);
    }
    /**
     * RecommendNewsABC
     */
    public Observable<NewsData> getRecommendNewsABC(int pageNum, int pageSize){
        return mApiService.getRecommendNewsABC(pageNum, pageSize);
    }
    /**
     * RecommendNews
     */
    public Observable<NewsData> getRecommendNews(int pageNum, int pageSize){
        return mApiService.getRecommendNews(pageNum, pageSize);
    }

    /**
     * 新闻详情
     */
    public Observable<DetailData> getNewsDetailData(String id){
        return mApiService.getNewsDetailData(id);
    }


    /**
     * 精彩评论
     */
    public Observable<CommentData> getCommentData(String article, int pageNum, int pageSize ){
        return mApiService.getCommentData(article, pageNum, pageSize);
    }
    /**
     * 精彩评论 -  一级评论
     */
    public Observable<CommentData> postCommentLevel1Data(String token, String articleId, String content ){
        return mApiService.postCommentLevel1Data(token, articleId, content);
    }
    /**
     * 精彩评论 -　二级评论
     */
    public Observable<CommentData> postCommentLevel2Data(String taken, String articleId, String content, String commentId ){
        return mApiService.postCommentLevel2Data(taken, articleId, content, commentId);
    }
    /**
     * 精彩评论 -　点赞
     */
    public Observable<CommentData> getLikeCommentData(String commentId ){
        return mApiService.getLikeCommentData(commentId);
    }

    /**
     * 热门推荐
     */
    public Observable<RecommendData> getRecommendData(String id){
        return mApiService.getRecommendData(id);
    }

    /**
     * 收藏
     */
    public Observable<VerifyData> getCollectionData(String contentId){
        return mApiService.getCollectionData(Constant.token,"0", contentId);
    }

    /**
     * 首页－每日讯－小红点
     */
    public Observable<FlushData> getFlushData(String time){
        return mApiService.getFlushData(time);
    }
}
