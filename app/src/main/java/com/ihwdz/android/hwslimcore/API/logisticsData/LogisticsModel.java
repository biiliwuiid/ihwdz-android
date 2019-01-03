package com.ihwdz.android.hwslimcore.API.logisticsData;

import android.content.Context;

import com.ihwdz.android.hwapp.common.Constant;
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
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */
public class LogisticsModel {

    private static LogisticsModel model;
    private LogisticsDataApi mApiService;

    public LogisticsModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(LogisticsDataApi.class);
    }

    public static LogisticsModel getInstance(Context context){
        if(model == null) {
            model = new LogisticsModel(context);
        }
        return model;
    }

    /**
     * 用户类型: 0 - 咨询会员; 1 - 交易会员; 2 - 商家会员; -1 - 用户
     * 会员认证 状态: 0 -未认证, 1 -已认证
     * 会员锁定 状态: 0 -正常, 1 -锁定, 2 -注销
     */
    public Observable<UserTypeData> getUserType(){
        return mApiService.getUserType(Constant.token);
    }




    /**
     发布求购 publish 产品信息-breed
     */

    public Observable<PublishData> getBreedData(String keyword){
        return mApiService.getBreedData(keyword);
    }
    /**
     发布求购 publish 产品信息-spec
     */

    public Observable<PublishData> getSpecData(String keyword, String breedCode){
        return mApiService.getSpecData(keyword, breedCode);
    }
    /**
     发布求购 publish 产品信息-brand
     */
    public Observable<PublishData.PublishBrandData> getBrandData(String keyword){
        return mApiService.getBrandData(keyword);
    }
    /**
     发布求购 publish - 提交求购数据 post
     */
    public Observable<VerifyData> postPurchaseData( String json) {

        MediaType JSON = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(JSON,json);
        return mApiService.postPurchaseData(body);
    }

    /**
     发布求购 publish 收货地址
     */
    public Observable<PublishData.AddressData> getAddressData(){ //int pageNum, int pageSize
        return mApiService.getAddressData(Constant.token );
    }

    /**
     发布求购 publish 增加收货地址
     */
    public Observable<VerifyData> getSaveAddressData(String address, String contactName,
                                                     String mobile, String isDefault,
                                                     String provinceCode, String provinceName,
                                                     String cityCode, String cityName,
                                                     String districtCode, String districtName){
        return mApiService.getSaveAddressData(Constant.token, address, contactName, mobile, isDefault,
                 provinceCode,  provinceName, cityCode,  cityName, districtCode,  districtName );
    }


    /**
     发布求购 publish 修改收货地址
     */
    public Observable<VerifyData> getUpdateAddressData(String address, String contactName,
                                                     String mobile, int isDefault,
                                                     String provinceCode, String provinceName,
                                                     String cityCode, String cityName,
                                                     String districtCode, String districtName, String id){
        return mApiService.getUpdateAddressData(Constant.token, address, contactName, mobile, isDefault,
                provinceCode, provinceName, cityCode,  cityName, districtCode, districtName, id );
    }








    /**
     求购池数据
     */
    public Observable<PurchaseData> getPurchasePoolData(int pageNum, int pageSize) {
        return mApiService.getPurchasePoolData(Constant.token, pageNum, pageSize);
    }
    /**
     我的求购
     */
    public Observable<PurchaseData> getMyPurchaseData(int pageNum, int pageSize) {
        return mApiService.getMyPurchaseData(Constant.token, pageNum, pageSize);
    }
    /**
     我的报价
     */
    public Observable<PurchaseData> getMyQuoteData(int pageNum, int pageSize) {
        return mApiService.getMyQuoteData(Constant.token, pageNum, pageSize);
    }

    /**
     detail - 我的求购报价
     */
    public Observable<PurchaseQuoteData> getMyPurchaseDetailData(String id) {
        return mApiService.getMyPurchaseDetailData(Constant.token, id);
    }

    /**
     detail - 我的求购报价 - 一键下单 校验 能否一键下单
     */
    public Observable<VerifyData> getOrderCheckData() {
        return mApiService.getOrderCheckData(Constant.token);
    }

    /**
     detail - 我的求购报价 -- 一键下单-- 确认订单-详情
     */
    public Observable<OrderConfirmData> getOrderConfirmData(String quoteId) {
        return mApiService.getOrderConfirmData(Constant.token, quoteId);
    }

    /**
     保证金
     */
    public Observable<VerifyData> getCheckDepositData(String receivableWay) {
        return mApiService.getCheckDepositData(Constant.token, receivableWay);
    }

    /**
     detail - 我的求购报价 -- 一键下单-- 确认订单-提交订单
     */
    /**
     - 报价 - 提交报价数据 post
     */
    public Observable<VerifyData> postOrderData( Long memberPurchaseId,
                                                 Long sellMemberQuoteId,
                                                 Double saleSumQty,
                                                 Double saleSumAmt,
                                                 Double purchaseSumQty,
                                                 Double purchaseSumAmt,
                                                 Double logisticsSumCost,
                                                 Double salePrice,
                                                 int distributionWay,
                                                 String deliveryDate,
                                                 int receivableWay,
                                                 Double creditAmount,
                                                 int receivableDay,
                                                 String packagSpec,
                                                 Double memberMoneyRate,
                                                 Double serviceAmt) {
        return mApiService.postOrderData(Constant.token, memberPurchaseId, sellMemberQuoteId, saleSumQty, saleSumAmt, purchaseSumQty,purchaseSumAmt,logisticsSumCost,
                salePrice,distributionWay, deliveryDate, receivableWay, creditAmount, receivableDay, packagSpec, memberMoneyRate, serviceAmt);
    }


    /**
     detail - 我的报价
     */
    public Observable<QuoteDetailData> getMyQuoteDetailData(String id) {
        return mApiService.getMyQuoteDetailData(Constant.token, id);
    }

    /**
     - 买家复议
     */
    public Observable<VerifyData> getPurchaseReviewData(String id, String review) {
        return mApiService.getPurchaseReviewData(Constant.token, id, review);
    }

    /**
      - 卖家 复议报价
     */
    public Observable<VerifyData> getQuoteReviewData(String id, String review) {
        return mApiService.getQuoteReviewData(Constant.token, id, review);
    }

    /**
     - 仓库 - 模糊搜索
     */
    public Observable<WarehouseData> getFuzzyWarehouseData(String companyShortName ) {
        return mApiService.getFuzzyWarehouseData(companyShortName);
    }
    /**
     - 仓库 - 搜索
     */
    public Observable<WarehouseData.WarehouseCheckData> getWarehouseData(String companyShortName ) {
        return mApiService.getWarehouseData(companyShortName);
    }



    /**
      - 报价 - 提交报价数据 post
     */
    public Observable<VerifyData> postQuoteData( long memberPurchaseId, double price,
                                                int isSupplierDistribution,String deliveryDate,String warehouse) {
        return mApiService.postQuoteData(Constant.token, memberPurchaseId, price, isSupplierDistribution, deliveryDate, warehouse);
    }




    /**
     * 物流
     */
    public Observable<LogisticsCityData> getFromData() {
        return mApiService.getFromData();
    }

    public Observable<LogisticsCityData> getDestinationData(String provFrom, String cityFrom, String distinctFrom) {
        return mApiService.getDestinationData(provFrom, cityFrom, distinctFrom);
    }

    public Observable<LogisticsResultData> getPriceData(String provFrom,String cityFrom,String distinctFrom,
                                                        String provTo,String cityTo,String distinctTo,int amount) {
        return mApiService.getPriceData(provFrom, cityFrom, distinctFrom, provTo, cityTo, distinctTo, amount);
    }

    /**
     * Order
     */
    public Observable<OrdersData> getOrderData(String subStatus, int pageNum, int pageSize ) {
        return mApiService.getOrderData(Constant.token, subStatus, pageNum, pageSize);
    }

    /**
     * Order detail
     */
    public Observable<OrderDetailData> getOrderDetailData(String orderId) {
        return mApiService.getOrderDetailData(Constant.token,orderId);
    }


    /**
     * 手续费 支付宝支付 -POST
     */
    public Observable<OrderData> postAliPayData(String orderId, String orderSn, String goodsAmount){
        return mApiService.postAliPayData(Constant.token, orderId, orderSn, goodsAmount);
    }

    /**
     * 手续费 微信支付 -POST
     */
    public Observable<OrderPayData> postWeChatPayData(String orderId, String orderSn, String goodsAmount){
        return mApiService.postWeChatPayData(Constant.token, orderId, orderSn, goodsAmount);
    }


    /**
     * 展期详情
     */
    public Observable<ExtensionData> getExtensionData(String orderId){
        return mApiService.getExtensionData(Constant.token, orderId);
    }
    /**
     * 展期申请
     */
    public Observable<VerifyData> postExtensionData(long orderId, String contractCode, double amount, int days, String note){
        return mApiService.postExtensionData(Constant.token, orderId, contractCode, amount, days, note);
    }

}
