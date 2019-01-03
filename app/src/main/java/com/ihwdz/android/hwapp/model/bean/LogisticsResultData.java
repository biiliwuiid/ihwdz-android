package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/09
 * desc :
 * version: 1.0
 * </pre>
 */
public class LogisticsResultData {

    public int code = 0;            // 200
    public String mess = "OK";
    public String result = "success";
    public long timeStamp = 0;
    public LogisticsResult data;


    public static class LogisticsResult{
        public String company = "";
        public String contract = "";      // contact
        public String telephone = "";
        public String aokaiPrice = "";    // 鸿网物流
        public String marketPrice = "";   // 市场价
        public String unitPrice = "";
    }
}
