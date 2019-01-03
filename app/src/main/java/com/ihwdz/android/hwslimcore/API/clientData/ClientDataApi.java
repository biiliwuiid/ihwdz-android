package com.ihwdz.android.hwslimcore.API.clientData;

import com.ihwdz.android.hwapp.model.bean.ClientData;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;
import com.ihwdz.android.hwapp.model.bean.IndexData;
import com.ihwdz.android.hwapp.model.bean.OrderData;
import com.ihwdz.android.hwapp.model.bean.OrderPayData;
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import com.ihwdz.android.hwapp.model.bean.RightsData;
import com.ihwdz.android.hwapp.model.bean.UserGoodsData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
public interface ClientDataApi {

    /**
     * 找客户
     /user/author?token               //是否已升级权限  VerifyData - code:"0"已升级 否则未升级
     /app/company/index
     /app/company/detail?id&token    详情
     /app/company/follow?id&follow&token   详情 - 关注   follow = "0" - 取消关注; "1"- 关注
     /app/company/address城市地址


     /app/company/info_update?token公司数据更新更新 ?
     */


    // 是否已升级权限
    @GET("user/author")
    Observable<RightsData> getUpdateRightData(@Query("token") String token);

    @GET("company/index")
    Observable<ClientData> getAllData();

    //找客户          type = 0： 潜在客户;  type = 1： 关注客户； keyword != null 则为搜索（type=0）
    @GET("company/index")
    Observable<ClientData> getAllData(@Query("pageNum") int pageNum,
                                      @Query("pageSize") int pageSize,
                                      @Query("type") String type,
                                      @Query("token") String token,
                                      @Query("keyword") String keyword);

    //筛选 - 找客户          type = 0： 潜在客户;
    @GET("company/index")
    Observable<ClientData> getFilterData(@Query("pageNum") int pageNum,
                                         @Query("pageSize") int pageSize,
                                      @Query("type") String type,
                                      @Query("hasMobile") String hasMobile,
                                      @Query("hasEmail") String hasEmail,
                                      @Query("stratRegMony") String startRegMoney,
                                      @Query("endRegMony") String endRegMoney,
                                      @Query("startCompanyCreated") String startCompanyCreated,
                                      @Query("endCompanyCreated") String endCompanyCreated,
                                      @Query("cityCodes") String cityCodes);


    // 详情
    @GET("company/detail")
    Observable<ClientDetailData> getClientDetailData(@Query("id") String id,
                                                     @Query("token") String token);

    // 详情 - 关注
    @GET("company/follow")
    Observable<VerifyData> updateClientFollowData( @Query("id") String id,
                                                @Query("follow") String follow,
                                                @Query("token") String token);



    @GET("company/address")
    Observable<ProvinceData> getProvinceData();



    /**
     * 会员升级
     * /user/goods   请求价格的接口

     @"/user/create_wx_order"          微信支付
     @"/user/create_ali_order"         阿里支付
     支付参数	{
     token = "44032b3f016e42ebbf0732a0d3dd8a2c_15900000001",
     catId = "3",  // 请求价格的接口 返回的数据
     days = "12",  // 请求价格的接口 返回的数据
     }
     */

    // 请求价格
    @GET("user/goods")
    Observable<UserGoodsData> getUserGoodsData();

    // 微信支付
    @FormUrlEncoded
    @POST("user/create_wx_order")
    Observable<OrderPayData> postWeChatPayData(@Field("token") String token,
                                               @Field("catId") String catId,
                                               @Field("days") int days);

    // 支付宝支付
    @FormUrlEncoded
    @POST("user/create_ali_order")
    Observable<OrderData> postAliPayData(@Field("token") String token,
                                          @Field("catId") String catId,
                                          @Field("days") int days);


}
