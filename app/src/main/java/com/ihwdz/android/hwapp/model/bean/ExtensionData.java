package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/03
 * desc :  展期信息
 * version: 1.0
 * </pre>
 */
public class ExtensionData {

    public String code;
    public String msg;
    public ExtensionEntity data;       // 展期信息

    public static class ExtensionEntity{
        public String contractCode;        // 订单号
        public String creditAmount;        // 授信额度
        public String extendsDaysRate;     // 展期利率
        public String note;                // 备注
        public String receivableDay;       // 展期
        public String receivableDayAmount; // 展期金额
    }

}
