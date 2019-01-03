package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/07
 * desc :    user/author?token               //是否已升级权限
 * version: 1.0
 * </pre>
 */
public class RightsData {

    public String code;
    public String msg;
    public RightsEntity data;

    public static class RightsEntity{
        public String code;
        public String msg;
        public String goodsId;
        public String goodsName;   // 凤凰...
        public String startDateStr;
        public String endDateStr;

    }
}
