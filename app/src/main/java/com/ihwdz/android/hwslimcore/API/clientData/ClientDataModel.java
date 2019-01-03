package com.ihwdz.android.hwslimcore.API.clientData;

import android.content.Context;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ClientData;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;
import com.ihwdz.android.hwapp.model.bean.MaterialData;
import com.ihwdz.android.hwapp.model.bean.OrderData;
import com.ihwdz.android.hwapp.model.bean.OrderPayData;
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import com.ihwdz.android.hwapp.model.bean.RightsData;
import com.ihwdz.android.hwapp.model.bean.UserGoodsData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;

import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class ClientDataModel {

    private static ClientDataModel model;
    private ClientDataApi mApiService;

    public ClientDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(ClientDataApi.class);
    }

    public static ClientDataModel getInstance(Context context){
        if(model == null) {
            model = new ClientDataModel(context);
        }
        return model;
    }

    public Observable<ClientData> getAllData() {
        return mApiService.getAllData();
    }

    /**
     *  潜在客户
     * @return
     */
    public Observable<ClientData> getPotentialClientsData( int pageNum,
                                              int pageSize,
                                              //String type,
                                              String token
                                              //String keyword
    ) {
        return mApiService.getAllData(pageNum, pageSize, "0", token, null);
    }
    /**
     *  关注客户
     * @return
     */
    public Observable<ClientData> getFollowedClientsData( int pageNum,
                                                          int pageSize
                                                          ) {
        return mApiService.getAllData(pageNum, pageSize, "1", Constant.token, null);
    }

    /**
     *  搜索客户
     * @return
     */
    public Observable<ClientData> getSearchClientsData( int pageNum,
                                                        int pageSize,
                                                        String keyword) {
        return mApiService.getAllData(pageNum, pageSize, "0", Constant.token, keyword);
    }

    /**
     *  筛选客户
     * @return
     */
    public Observable<ClientData> getFilterClientsData(int pageNum,
                                                       int pageSize,
                                                        String hasMobile,
                                                        String hasEmail,
                                                        String startRegMoney,
                                                        String endRegMoney,
                                                        String startCompanyCreated,
                                                        String endCompanyCreated,
                                                        String cityCodes
    ) {
        return mApiService.getFilterData(pageNum, pageSize,"0", hasMobile, hasEmail, startRegMoney, endRegMoney, startCompanyCreated, endCompanyCreated, cityCodes);
    }




    /**
     * 客户详情
     */
    public Observable<ClientDetailData> getClientDetailData(String id) {
        // Constant.token = "e7cc3bb283494cbe9fb377081ac615d4_13524114059";
        return mApiService.getClientDetailData(id, Constant.token);
    }

    /**
     * 客户详情 - 关注
     */
    public Observable<VerifyData> updateClientFollowData(String id, String follow) {
        // Constant.token = "e7cc3bb283494cbe9fb377081ac615d4_13524114059";
        return mApiService.updateClientFollowData(id, follow, Constant.token);
    }


    // province - cities - filter
    public Observable<ProvinceData> getProvinceData() {
        return mApiService.getProvinceData();
    }

    /**
     * 请求价格
     */
    public Observable<UserGoodsData> getUserGoodsData() {
        return mApiService.getUserGoodsData();
    }

    /**
     * 是否已升级权限
     */
    public Observable<RightsData> getUpdateRightData() {
        return mApiService.getUpdateRightData(Constant.token);
    }

    /**
     * 微信支付 -POST
     */
    public Observable<OrderPayData> postWeChatPayData(String catId, int days){
        return mApiService.postWeChatPayData(Constant.token, catId, days);
    }

    /**
     * 支付宝支付 -POST
     */
    public Observable<OrderData> postAliPayData(String catId, int days){
        return mApiService.postAliPayData(Constant.token, catId, days);
    }
}
