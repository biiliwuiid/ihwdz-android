package com.ihwdz.android.hwslimcore.API.loginData;

import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.ihwdz.android.hwapp.model.bean.ApplyDealVipData;
import com.ihwdz.android.hwapp.model.bean.DealRecordData;
import com.ihwdz.android.hwapp.model.bean.EnterpriseInformation;
import com.ihwdz.android.hwapp.model.bean.InvoiceData;
import com.ihwdz.android.hwapp.model.bean.LicenseData;
import com.ihwdz.android.hwapp.model.bean.LoginData;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.model.bean.ProductData;
import com.ihwdz.android.hwapp.model.bean.QuotaData;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.model.bean.UserInformation;
import com.ihwdz.android.hwapp.model.bean.UserStateData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;


import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc :  login and user api
 * version: 1.0
 * </pre>
 */
public interface LoginDataApi {

    /**
     * 注册
     /app/user/account_empty?mobile   用户是否存在// true 不存在  false 存在
     /app/user/register        // 注册- POST
     /app/user/sms?mobile&type // 发送验证码 0: 注册; 1: 登陆; 2: 修改密码;
     /app/user/department_employee?departmentType  //	1: 营销管理事业部; 2: 供应商管理事业部;

     var agreementUrl = '= 'http://mb.ihwdz.com/android/agreement.html'; //隐私//隐私协议H5
     var clauseUrl = '= 'http://mb.ihwdz.com/android/clause.html';       //服务//服务条款H5
     */

    // 验证手机是否已注册
    @GET("user/account_empty")
    Observable<VerifyData> getIsRegistered(@Query("mobile") String mobile);

    // 发送验证码   0-注册; 1-登陆; 2-修改密码
    @GET("user/sms")
    Observable<UserData> getVerificationCode(@Query("mobile") String mobile,
                                             @Query("type") String type);



    //　注册
    //@FormUrlEncoded
    @POST("user/register")
    Observable<UserData> postRegisterData(@Body UserData.UserEntity user);

    //　注册 postRegisterData post todo
    @FormUrlEncoded
    @POST("user/register")
    Observable<LoginData> postRegisterData(
            @Field("accountNo") String accountNo,
            @Field("password") String password,
            @Field("name") String name,
            @Field("source") int source,        // 30
            @Field("checkNum") String checkNum
    );

    /**
     *   登陆
     /app/user/uname_login?accountNo&password   - POST 用户名登陆
     /app/user/mobile_login?accountNo&checkCode - POST 手机号登陆
     /app/user/mobile_change_password?accountNo&password&checkCode 修改密码
     */

    //　登陆 - 用户名登陆 postLoginData post todo
    @FormUrlEncoded
    @POST("user/uname_login")
    Observable<LoginData> postLoginData(@Field("accountNo") String accountNo,
                                        @Field("password") String password);

    //　登陆 - 手机号 动态密码登陆  0-注册; 1-登陆; 2-修改密码
    // postLoginByCodeData  动态密码登陆 post todo
    @FormUrlEncoded
    @POST("user/mobile_login")
    Observable<LoginData> postLoginByCodeData(@Field("accountNo") String accountNo,
                                              @Field("checkCode") String checkCode);


    // 修改密码 updatePwd post todo
    @FormUrlEncoded
    @POST("user/mobile_change_password")
    Observable<UserData> updatePwd(@Field("accountNo") String accountNo,
                                       @Field("password") String password,
                                       @Field("checkCode") String checkCode);

    /**
     * 用户类型：-1用户， 0-咨询会员，1-交易会员，2-商家会员
     * 会员认证 状态： 0 -未认证, 1 -已认证
     * 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
     * /member/query_type_new?token    GET  用户类型
     */
    @GET("member/query_type_new")
    Observable<UserTypeData> getUserType(@Query("token") String token);



    /**
     *
     * /app/member/index?token   GET  用户状态信息  -getUserState
     * 1. 用户状态信息: goodsName;endDateStr;(凤凰麻雀有效期)
     * 2. 交易用户是否认证的判断- String totalAmount // length > 0 已认证
     */
    @GET("member/index")
    Observable<UserStateData> getUserStatus(@Query("token") String token);

    /**
     * /app/user/logout?token    GET 退出登陆
     * @param token
     * @return
     */
    @GET("user/logout")
    Observable<UserData> doLogout(@Query("token") String token);

    /**
     * /app/member/feed_back?token&feedType&feedContent      feedType: 1 价格 2原料 3体验 4 其它
     * 反馈 postFeedback post todo
     */
    @FormUrlEncoded
    @POST("member/feed_back")
    Observable<VerifyData> postFeedback(@Field("token") String token,
                                        @Field("feedType") String feedType,
                                        @Field("feedContent") String feedContent);

    /**
     * /app/member/perfect_list  获取企业类型 企业性质 GET
     /app/member/get_infor?token         获取用户信息 GET
     /app/member/update_infor?token&name&email&companyNature&companyType&companyFullName  更新信息 POST

     /app/member/query_member_info?token 获取会员信息 getVipInfo  点击“我的交易/商家会员”时
     */
    @GET("member/perfect_list")
    Observable<EnterpriseInformation> getEnterpriseInfo();

    // 完善信息 - 用户信息展示
    @GET("member/get_infor")
    Observable<UserInformation> getUserInfo(@Query("token") String token);

    //更新信息 - 用户信息 todo
    @FormUrlEncoded
    @POST("member/update_infor")
    Observable<VerifyData> postUserData(@Field("token") String token,
                                        @Field("name") String name,
                                        @Field("email") String email,
                                        @Field("mobile") String mobile,
                                        @Field("provinceCode") String provinceCode,
                                        @Field("province") String province,
                                        @Field("cityCode") String cityCode,
                                        @Field("city") String city,
                                        @Field("districtCode") String districtCode,
                                        @Field("district") String district,
                                        @Field("address") String  address
                                        );
    // 我的交易会员 - 会员信息展示
    @GET("member/query_member_info")
    Observable<UserInformation> getVipInfo(@Query("token") String token);

    /**
     /app/member/get_collection?token&pageSize&pageNum    我的收藏
     /app/article/index_notice?pageNum&pageSize           消息中心
     /app/member/query_order_list?token&pageNum&pageSize  交易会员交易记录
     /app/shop/query_order_list?token&pageNum&pageSize	  商家会员交易记录

     开票信息 ：  /member/invoice_detail?token&orderId
     提交申请 ：  /member/apply_invoice?token&orderId
     */

    // 消息中心
    @GET("article/index_notice")
    Observable<NewsData> getMessageData(@Query("pageNum") int pageNum,
                                        @Query("pageSize") int pageSize);
    // 我的收藏
    @GET("member/get_collection")
    Observable<NewsData> getCollectionData(@Query("token") String token,
                                           @Query("pageNum") int pageNum,
                                           @Query("pageSize") int pageSize);

    // 交易会员 交易记录
    @GET("member/query_order_list")
    Observable<DealRecordData> getRecordData(@Query("token") String token,
                                             @Query("pageNum") int pageNum,
                                             @Query("pageSize") int pageSize);

    // 商家会员 交易记录
    @GET("shop/query_order_list")
    Observable<DealRecordData> getShopRecordData(@Query("token") String token,
                                                 @Query("pageNum") int pageNum,
                                                 @Query("pageSize") int pageSize);

    // 开票信息
    @GET("member/invoice_detail")
    Observable<InvoiceData> getInvoiceData(@Query("token") String token,
                                           @Query("orderId") String orderId);

    // 提交申请
    @GET("member/apply_invoice")
    Observable<InvoiceData> applyInvoiceData(@Query("token") String token,
                                             @Query("orderId") String orderId,
                                             @Query("amount") String amount);


    /**
     *  交易会员 quota 额度申请：
     /app/member/check_personal?userName&idCard&phone&token   法人三要素校验 POST
     /app/member/upload_certificate?token&files               POST 上传营业执照

     /app/member/check_member_name?memberName&token 校验会员名称 - 单位名称
     /app/member/check_tax_number?taxNumber&token   校验税号 -  社会信用代码

     /app/member/product_info         获取生产信息

     /app/member/cofirm_examine       提交数据

     /app/member/apply_amount?token   获取额度
     */

    // 法人三要素校验 post todo
    @FormUrlEncoded
    @POST("member/check_personal")
    Observable<VerifyData> postLegalPersonInfo(@Field("token") String token,
                                               @Field("userName") String userName,
                                               @Field("phone") String phone,
                                               @Field("idCard") String idCard);

    // 上传营业执照 post todo
    @Multipart
    @POST("member/upload_certificate")
    Observable<LicenseData> postLicense(@Query("token") String token,
                                        @Part MultipartBody.Part file);


    // 校验 社会信用代码
    @GET("member/check_tax_number")
    Observable<VerifyData> getCheckTaxNumberData(@Query("token") String token,
                                                  @Query("taxNumber") String taxNumber);

    // 校验 单位名称
    @GET("member/check_member_name")
    Observable<VerifyData> getCheckCompanyNameData(@Query("token") String token,
                                            @Query("memberName") String memberName);



    // 获取生产信息
    @GET("member/product_info")
    Observable<ProductData> getProductData();


    // 获取管理人员列表　departmentType=1: 营销管理事业部(营销顾问);  departmentType=2: 供应商管理事业部（商家管理）
    @GET("user/department_employee")
    Observable<AdminData> getAdministrator(@Query("departmentType") int departmentType);

    // 获取额度
    @GET("member/apply_amount")
    Observable<QuotaData> getQuotaData(@Query("token") String token);



    /**
     */
    // TODO: 2018/11/22 申请成为交易会员 提交数据（ // 交易管理员ID  不选管理员的时候不传）
    // private Long buyerAdminId;			   +      // 交易管理员ID  不选管理员的时候不传
    // 提交申请验证数据 参数看 file:ConfirmInforParam  in E:\temp\TEMP
    //@FormUrlEncoded
    @POST("member/cofirm_examine")
    Observable<VerifyData> postApplyData(@Query("token") String token,
                                         @Body ApplyDealVipData data);


    // 申请成为交易会员 提交数据
    @FormUrlEncoded
    @POST("member/cofirm_examine")
    Observable<VerifyData> postApplyData(@Field("token") String token,
                                         @Field("imageUrl") String imageUrl,
                                         @Field("creditCode") String creditCode,
                                         @Field("companyName") String companyName,
                                         @Field("legalPerson") String legalPerson,
                                         @Field("startTimeStr") String startTimeStr,
                                         @Field("endTimeStr") String endTimeStr,
                                         @Field("materials") String materials,
                                         @Field("goodsTypes") String goodsTypes,
                                         @Field("buyerAdminId") String buyerAdminId);

    /**
     *  member/apply_refund_deposit?memberServeId  申请退还保证金
     */
    @GET("member/apply_refund_deposit")
    Observable<VerifyData> getDepositRefundData(@Query("token") String token,
                                                @Query("memberServeId") String memberServeId);
}
