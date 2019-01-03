package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/24
 * desc :   开票信息
 * version: 1.0
 * </pre>
 */
public class InvoiceData {

    public String code;
    public String msg;
    public InvoiceEntity data;

    public static class InvoiceEntity{
        public String address;        // 邮寄地址
        public String amount;         // 开票金额
        public String bankAccount;    // 账号
        public String bankName;       // 开户行
        public String invoceCompany;  // 开票单位
        public String phone;          // 联系电话
        public String taxNumber;      // 税号
        public String discountAmount; // 汇票贴现金额
    }
}
