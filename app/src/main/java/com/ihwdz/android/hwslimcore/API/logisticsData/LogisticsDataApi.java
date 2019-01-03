package com.ihwdz.android.hwslimcore.API.logisticsData;

import com.ihwdz.android.hwapp.model.bean.ExtensionData;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;
import com.ihwdz.android.hwapp.model.bean.LogisticsResultData;
import com.ihwdz.android.hwapp.model.bean.OrderConfirmData;
import com.ihwdz.android.hwapp.model.bean.OrderData;
import com.ihwdz.android.hwapp.model.bean.OrderDetailData;
import com.ihwdz.android.hwapp.model.bean.OrderPayData;
import com.ihwdz.android.hwapp.model.bean.OrdersData;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.model.bean.PurchaseData;
import com.ihwdz.android.hwapp.model.bean.PurchaseQuoteData;
import com.ihwdz.android.hwapp.model.bean.QuoteDetailData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;


import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :  logistics
 * version: 1.0
 * </pre>
 */
public interface LogisticsDataApi {


    /**
     * 用户类型：-1用户， 0-咨询会员，1-交易会员，2-商家会员
     * 会员认证 状态： 0 -未认证, 1 -已认证
     * 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
     * /member/query_type_new?token    GET  用户类型
     */
    @GET("member/query_type_new")
    Observable<UserTypeData> getUserType(@Query("token") String token);

    /**
     *  发布求购 publish
     *
     *  收货信息  member/get_member_delivery_address?token  取默认地址 / 没有默认取第一条
     *
     *  原料品名  baseData/query_breed?keyword
     *  原料牌号  baseData/query_spec?keyword & code // code 为 breed code
     *  生产厂家  baseData/query_brand?keyword
     *
     *  提交 求购数据  member/publish_buy   - post  reference: class: MemberPurchaseDTOs
     */


    // 收货地址
    @GET("member/get_member_delivery_address")
    Observable<PublishData.AddressData> getAddressData(@Query("token") String token);

    // breed
    @GET("baseData/query_breed")
    Observable<PublishData> getBreedData(@Query("keyword") String keyword);
    // spec
    @GET("baseData/query_spec")
    Observable<PublishData> getSpecData(@Query("keyword") String keyword,
                                        @Query("code") String breedCode);
    // brand
    @GET("baseData/query_brand")
    Observable<PublishData.PublishBrandData> getBrandData(@Query("keyword") String keyword);



    // 提交 求购数据 post  Content-Type为application/json  todo
    @POST("member/publish_buy")
    Observable<VerifyData> postPurchaseData(@Body RequestBody requestBody);


    /**
     *  发布求购 publish - 收货地址 新增/编辑 get
        @"/member/save_member_delivery_address"   // 新增收货信息
        @"/member/update_member_delivery_address" // 编辑收货信息
     */
    // 新增收货信息
    @GET("member/save_member_delivery_address")
    Observable<VerifyData> getSaveAddressData(
            @Query("token") String token,
            @Query("address") String address,
            @Query("contactName") String contactName,
            @Query("mobile") String mobile,
            @Query("isDefault") String isDefault,
            @Query("provinceCode") String provinceCode,
            @Query("provinceName") String provinceName,
            @Query("cityCode") String cityCode,
            @Query("cityName") String cityName,
            @Query("districtCode") String districtCode,
            @Query("districtName") String districtName
    );

    // 修改收货信息  （memberDeliveryAddressId 新增地址没有此参数）
    @GET("member/update_member_delivery_address")
    Observable<VerifyData> getUpdateAddressData(
            @Query("token") String token,
            @Query("address") String address,
            @Query("contactName") String contactName,
            @Query("mobile") String mobile,
            @Query("isDefault") int isDefault,
            @Query("provinceCode") String provinceCode,
            @Query("provinceName") String provinceName,
            @Query("cityCode") String cityCode,
            @Query("cityName") String cityName,
            @Query("districtCode") String districtCode,
            @Query("districtName") String districtName,
            @Query("memberDeliveryAddressId") String memberDeliveryAddressId
    );

















    /**
     * purchase
     * member/purchase_pool?token    求购池数据
     * member/my_purchase_list?token 我的求购
     * shop/my_quote_list?token      我的报价
     */
    @GET("member/purchase_pool")
    Observable<PurchaseData> getPurchasePoolData(@Query("token") String token,
                                                 @Query("pageNum") int pageNum,
                                                 @Query("pageSize") int pageSize);


    @GET("member/my_purchase_list")
    Observable<PurchaseData> getMyPurchaseData(@Query("token") String token,
                                                 @Query("pageNum") int pageNum,
                                                 @Query("pageSize") int pageSize);

    @GET("shop/my_quote_list")
    Observable<PurchaseData> getMyQuoteData(@Query("token") String token,
                                                 @Query("pageNum") int pageNum,
                                                 @Query("pageSize") int pageSize);

    /**
     * member/purchase_quote_list?token&memberPurchaseId  求购报价详情
     * shop/my_quote_detail?quoteId&token   报价详情
     */

    @GET("member/purchase_quote_list")
    Observable<PurchaseQuoteData> getMyPurchaseDetailData(@Query("token") String token,
                                                          @Query("memberPurchaseId") String memberPurchaseId);

    @GET("shop/my_quote_detail")
    Observable<QuoteDetailData> getMyQuoteDetailData(@Query("token") String token,
                                                     @Query("quoteId") String quoteId);


    /**
     * member/member_review_quote ？token&sellMemberQuoteId& reviewNote  买家复议
     * shop/sell_review_quote ? token& quoteId& price    卖家复议
     */

    @GET("member/member_review_quote")
    Observable<VerifyData> getPurchaseReviewData(@Query("token") String token,
                                                   @Query("sellMemberQuoteId") String sellMemberQuoteId,
                                                   @Query("reviewNote") String reviewNote);

    @GET("shop/sell_review_quote")
    Observable<VerifyData> getQuoteReviewData(@Query("token") String token,
                                              @Query("quoteId") String quoteId,
                                              @Query("price") String price);


    /** 商家报价
     * baseData/query_like_warehouse？companyShortName 模糊搜索仓库
     * baseData/query_warehouse?companyShortName    精准搜索仓库 1.创建新仓库校验 2.选择历史
     * shop/sell_price 报价 - 提交报价数据 post
     */

    @GET("baseData/query_like_warehouse")
    Observable<WarehouseData> getFuzzyWarehouseData(@Query("companyShortName") String companyShortName);

    @GET("baseData/query_warehouse")
    Observable<WarehouseData.WarehouseCheckData> getWarehouseData(@Query("companyShortName") String companyShortName);


    // 提交报价数据 post todo
    @FormUrlEncoded
    @POST("shop/sell_price")
    Observable<VerifyData> postQuoteData(
            @Field("token") String token,
            @Field("memberPurchaseId") long memberPurchaseId,
            @Field("price") double price,
            @Field("isSupplierDistribution") int isSupplierDistribution,  // 0 - no ; 1 - yes;
            @Field("deliveryDate") String deliveryDate,
            @Field("warehouse") String warehouse                          // warehouse json
    );






    /**
     * logistics
     * /app/logistics/from_address  出发地
     * /app/logistics/to_address?provFrom&cityFrom&distinctFrom    目的地
     * /app/logistics/query_price?provFrom&cityFrom&distinctFrom&provTo&cityTo&distinctTo&amount   价格
     * @return
     */

    // 出发地
    @GET("logistics/from_address")
    Observable<LogisticsCityData> getFromData();

    // 目的地
    @GET("logistics/to_address")
    Observable<LogisticsCityData> getDestinationData(@Query("provFrom") String provFrom,
                                                     @Query("cityFrom") String cityFrom,
                                                     @Query("distinctFrom") String distinctFrom
                                                     );

    // 价格
    @GET("logistics/query_price")
    Observable<LogisticsResultData> getPriceData(@Query("provFrom") String provFrom,
                                                 @Query("cityFrom") String cityFrom,
                                                 @Query("distinctFrom") String distinctFrom,
                                                 @Query("provTo") String provTo,
                                                 @Query("cityTo") String cityTo,
                                                 @Query("distinctTo") String distinctTo,
                                                 @Query("amount") int amount
                                              );


    /**
     * 一键下单页面计算：
     采购单价：求购报价的价格；
     运费单价：求购报价的运费单价；（非供配情况下，选择自提，重置为0）
     资金服务费利率：未认证会员为0；
     账期：使用授信（部分授信，或全部授信）默认为最大账期天数；款到发货，账期为0；

     选择账期后计算-----------------------------------------------------

     1.资金服务费 =（采购单价+运费单价）*数量*资金服务费利率*账期

     * 全部授信- 资金服务费 =（采购单价+运费单价）*数量*资金服务费利率*账期
     * 部分授信- 资金服务费 = 授信额度（自填）*资金服务费利率*账期


     2.销售总货款 =（采购单价+运费单价）*数量 + 资金服务费

     3.采购总货款 = 采购单价*数量

     4.物流总货款 = 运费单价*数量

     5.销售单价 = 销售总货款/数量


     6.使用授信（授信金额）
     全部授信-使用授信 =（采购单价+运费单价）* 数量 + 资金服务费
     部分授信-使用授信 = 授信额度（自填）；
     款到发货-使用授信 = 0；

     7.手续费 = 销售总货款*0.3%
     *
     * /member/purchase_check_create_order?token 				     检验能否一键下单
     * /member/query_purchase_create_order_details?token&quoteId     确认订单
     */

    // 检验能否一键下单
    @GET("member/purchase_check_create_order")
    Observable<VerifyData> getOrderCheckData(@Query("token") String token);

    // 确认订单
    @GET("member/query_purchase_create_order_details")
    Observable<OrderConfirmData> getOrderConfirmData(@Query("token") String token,
                                                     @Query("quoteId") String quoteId);

    /**
     * 保证金
     * order/check_deposit?token & receivableWay
     */
    @GET("order/check_deposit")
    Observable<VerifyData> getCheckDepositData(@Query("token") String token,
                                                     @Query("receivableWay") String receivableWay);

    /**
     * private Long memberPurchaseId = 0L;						// 会员求购单Id
     private Long sellMemberQuoteId = 0L;					    // 报价单Id

     private Double saleSumQty = 0D;							// 销售总重量
     private Double saleSumAmt = 0D;							// 销售总金额
     private Double purchaseSumQty = 0D;						// 采购总重量
     private Double purchaseSumAmt = 0D;						// 采购总金额
     private Double logisticsSumCost = 0D;					    // 物流总货款
     private Double salePrice = 0D;							    // 销售单价

     private Integer distributionWay = 0;					// 配送方式 0-供方配送 1-自提 2-平台配送
     private String deliveryDate = "";						// 交货日期
     private Integer receivableWay = 0;						// 结算方式 0-全额授信 1-部分授信 2-款到发货
     private Double creditAmount = 0D;						// 授信金额
     private Integer receivableDay = 0;						// 货到收款天数- 账期

     private String packagSpec = "";						// 包装规格

     private Double memberMoneyRate = 0D;					// 服务费利率
     private	Double serviceAmt = 0D;						// 服务费
     */
    /**
     * /order/purchase_save_sale_order                 // 一键下单 todo
     */
    @FormUrlEncoded
    @POST("order/purchase_save_sale_order")
    Observable<VerifyData> postOrderData(@Field("token") String token,
                                         @Field("memberPurchaseId") Long memberPurchaseId,
                                         @Field("sellMemberQuoteId") Long sellMemberQuoteId,
                                         @Field("saleSumQty") Double saleSumQty,
                                         @Field("saleSumAmt") Double saleSumAmt,
                                         @Field("purchaseSumQty") Double purchaseSumQty,
                                         @Field("purchaseSumAmt") Double purchaseSumAmt,
                                         @Field("logisticsSumCost") Double logisticsSumCost,
                                         @Field("salePrice") Double salePrice,
                                         @Field("distributionWay") int distributionWay,
                                         @Field("deliveryDate") String deliveryDate,
                                         @Field("receivableWay") int receivableWay,
                                         @Field("creditAmount") Double creditAmount,
                                         @Field("receivableDay") int receivableDay,
                                         @Field("packagSpec") String packagSpec,
                                         @Field("memberMoneyRate") Double memberMoneyRate,
                                         @Field("serviceAmt") Double serviceAmt);





    /**
     * OrderFragment 订单列表
     * @param token
     * @param subStatus
     *  10-待审核 12-待支付手续费 11-待上传采购合同
     *  20-待收款 30-待付款 40-待发货 50-待确认收货
     *  90-交易失败 100-待开票？ 101-待开票审核？ 102-交易成功
     */
    @GET("order/query_order_list")
    Observable<OrdersData> getOrderData(@Query("token") String token,
                                        @Query("subStatus") String subStatus,
                                        @Query("pageNum") int pageNum,
                                        @Query("pageSize") int pageSize);

    /**
     * 订单详情
     * order/query_order_detail?token&orderId
     */
    @GET("order/query_order_detail")
    Observable<OrderDetailData> getOrderDetailData(@Query("token") String token,
                                                   @Query("orderId") String orderId);

    /**
     *  支付手续费
     * order/member_purchase_alipay_create?token&orderSn&goodsAmount 支付宝 todo
     * order/member_purchase_wx_create?token&orderSn&goodsAmount 微信 todo
     * private String operateStatus;	// 操作状态 null没有; A0-支付手续费; A1-开票申请; A2-申请展期
     */

    // 支付宝支付
    @FormUrlEncoded
    @POST("order/member_purchase_alipay_create")
    Observable<OrderData> postAliPayData(@Field("token") String token,
                                         @Query("orderId") String orderId,
                                         @Field("orderSn") String orderSn,
                                         @Field("goodsAmount") String goodsAmount);



    // 微信支付
    @FormUrlEncoded
    @POST("order/member_purchase_wx_create")
    Observable<OrderPayData> postWeChatPayData(@Field("token") String token,
                                               @Query("orderId") String orderId,
                                               @Field("orderSn") String orderSn,
                                               @Field("goodsAmount") String goodsAmount);

    /**
     * rder/query_extends_days_detail?orderId&token  展期详情
     * order/save_extends_days    申请展期
     */
    @GET("order/query_extends_days_detail")
    Observable<ExtensionData> getExtensionData(@Query("token") String token,
                                               @Query("orderId") String orderId);

    /**
     * order/save_extends_days    申请展期
     * private String token ;
       private Long orderId = 0L;        // 订单Id
       private String contractCode = ""; // 关联合同号
       private Double amount = 0D;       // 金额
       private Integer days = 0;         // 天数
       private String note = "";         // 备注
     */

    @FormUrlEncoded
    @POST("order/save_extends_days")
    Observable<VerifyData> postExtensionData(@Field("token") String token,
                                               @Query("orderId") long orderId,
                                               @Field("contractCode") String contractCode,
                                               @Field("amount") double amount,
                                               @Field("days") int days,
                                               @Field("note") String note);

}
