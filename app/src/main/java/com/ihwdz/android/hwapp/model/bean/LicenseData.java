package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/17
 * desc :
 * version: 1.0
 * </pre>
 */
public class LicenseData {

    public String code;
    public String msg;
    public LicenseEntity data;

    public static class LicenseEntity{
        public String code = "0";
        public String msg = "OK";
        public String fileUrl;          // 营业执照路径
        public String creditCode;       // 信用代码
        public String evidenceNumber;
        public String companyName;      // 公司名称
        public String companyType;
        public String companyAddress;
        public String legalPerson;       // 公司法人
        public String companyRegisterMony;
        public String startTimeStr;      // 成立时间
        public String endTimeStr;        // 有效期
        public String companyNature;

        public String needChange;	// 需要修改
    }
}
