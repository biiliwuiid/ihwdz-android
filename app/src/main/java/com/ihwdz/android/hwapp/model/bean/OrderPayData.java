package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/19
 * desc :  微信支付接口返回数据
 * version: 1.0
 * </pre>
 */
public class OrderPayData {
    public String code;
    public String msg;
    public OrderInfo data;    // 订单信息

    public static class OrderInfo{
        public String timestamp; // 时间戳
        public String nonceStr;  // 随机字符串，不长于32位。推荐随机数生成算法
        public String mchId;     // partnerId  微信支付分配的商户号
        public String code;
        public String msg;
        public String tradeType;
        public String returnMsg;
        public String prepayId;     //	微信返回的支付交易会话ID
        public String returnCode;
        public String sign;
        public String appId;        // 微信开放平台审核通过的应用APPID
        //public String package;    // 官方描述: 暂填写固定值Sign=WXPay
        //public String partnerId;  // no 微信支付分配的商户号

    }
}
