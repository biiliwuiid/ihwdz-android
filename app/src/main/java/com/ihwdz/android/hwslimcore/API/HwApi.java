package com.ihwdz.android.hwslimcore.API;

/**
 * <pre>
 * author : Duan
 * time :   2018/07/25
 * desc :
 * version: 1.0
 * </pre>
 */
public class HwApi {

    // 正式环境
    public static final String HWDZ_URL_ROOT = "http://mb.ihwdz.com/app/";       // 获取数据二者皆可

    // 测试环境
    public static final String HWDZ_URL_TEST = "http://172.16.10.39:8082/app/";   // 提交数据用测试
    public static final String HWDZ_URL_TEST1 = "http://192.168.1.68:8082/app/";  // 提交数据用测试

    public static final String HWDZ_URL = HWDZ_URL_TEST;

    public static final String HWDZ_UPGRADE_URL = HWDZ_URL + "version/index_android";      // app 升级
    // public static final String HWDZ_UPGRADE_URL = "http://192.168.1.68:8082/app/version/index_android";

    public static final String PRIVACY_URL = "http://mb.ihwdz.com/android/agreement.html";   // 隐私协议 Privacy Policy
    public static final String SERVICE_URL = "http://mb.ihwdz.com/android/clause.html";      // 服务条款 Terms of Service agreement

    public static final String MOOD_INDEX_URL = "http://mb.ihwdz.com/ios/breed_index.html";  // 首页情绪指数

//    IndexWebActivity
//    String profitUrl = "http://mb.ihwdz.com/ios/price_trend.html?technology=" + technology + "&baseId=" + baseId; // 利润走势H5
//    String futureUrl = "http://mb.ihwdz.com/ios/price_difference.html?baseId=" + baseId;                          // 期现价差H5

    public static final String USER_NOTES_URL = "http://mb.ihwdz.com/users/user.html";           // 用户须知
    public static final String ORDER_NOTES_URL = "http://mb.ihwdz.com/users/instructions.html";  // 订单须知

}
