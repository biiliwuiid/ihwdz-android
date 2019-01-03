package com.ihwdz.android.hwslimcore.API.priceData;

import android.content.Context;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.FactoryPriceData;
import com.ihwdz.android.hwapp.model.bean.MarketPriceData;
import com.ihwdz.android.hwapp.model.bean.PriceCollectionData;
import com.ihwdz.android.hwapp.model.bean.PriceData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;

import rx.Observable;


/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class PriceDataModel {

    private static PriceDataModel model;
    private PriceDataApi mApiService;

    public PriceDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(PriceDataApi.class);
    }

    public static PriceDataModel getInstance(Context context){
        if(model == null) {
            model = new PriceDataModel(context);
        }
        return model;
    }



    /**
     * 市场价
     */
    public Observable<PriceData> getMarketPriceData1(int pageNum,
                                                     int pageSize,
                                                     String breed,
                                                     String spec,
                                                     String city,
                                                     String brand,
                                                     String dateTimeStr) {
        return mApiService.getMarketPriceData(Constant.token, pageNum, pageSize, breed, spec, city, brand, dateTimeStr);
    }
    /**
     * 出厂价
     */
    public Observable<PriceData> getFactoryPriceData1(int pageNum,
                                                      int pageSize,
                                                      String breed,
                                                      String spec,
                                                      String region,
                                                      String brand,
                                                      String dateTimeStr) {
        return mApiService.getFactoryPriceData(Constant.token, pageNum, pageSize, breed, spec, region, brand, dateTimeStr);
    }


    /**
     * 市场价
     */
    public Observable<MarketPriceData> getMarketPriceData(int pageNum,
                                                    int pageSize,
                                                    String breed,
                                                    String spec,
                                                    String city,
                                                    String brand,
                                                    String dateTimeStr) {
        return mApiService.getMarketPrice(Constant.token, pageNum, pageSize, breed, spec, city, brand, dateTimeStr);
    }

    /**
     * 出厂价
     */
    public Observable<FactoryPriceData> getFactoryPriceData(int pageNum,
                                                            int pageSize,
                                                            String breed,
                                                            String spec,
                                                            String region,
                                                            String brand,
                                                            String dateTimeStr) {
        return mApiService.getFactoryPrice(Constant.token, pageNum, pageSize, breed, spec, region, brand, dateTimeStr);
    }


    /**
     * 查价格 - 我的收藏 查询
     */
    public Observable<PriceCollectionData> getPriceCollectedData(String dateTimeStr, int pageNum, int pageSize) {
        return mApiService.getPriceCollectedData(Constant.token, dateTimeStr, pageNum, pageSize );
    }

    /**
     * 查价格 - 收藏 动作
     */
    public Observable<VerifyData> getPriceCollect(int collectionType,       // 0 市场价 1 出厂价
                                                  String breed,
                                                  String spec,
                                                  String brand,
                                                  String area) {
        return mApiService.getPriceCollect(Constant.token, collectionType, breed, spec, brand, area );
    }

    /**
     * 查价格 - 取消收藏 动作
     */
    public Observable<VerifyData> getPriceCollectedCancel(int collectionType, // 0 市场价 1 出厂价
                                                  String breed,
                                                  String spec,
                                                  String brand,
                                                  String area) {
        return mApiService.getPriceCollectedCancel(Constant.token, collectionType, breed, spec, brand, area );
    }






}
