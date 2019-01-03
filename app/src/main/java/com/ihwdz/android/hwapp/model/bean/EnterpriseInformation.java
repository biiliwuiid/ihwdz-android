package com.ihwdz.android.hwapp.model.bean;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/19
 * desc :
 * version: 1.0
 * </pre>
 */
public class EnterpriseInformation {

    public String code;
    public String msg;
    public EnterpriseInfo data;

    public static class EnterpriseInfo{

//        public List<Map<String, String>> companyTypeList;
//        public List<Map<String, String>> companyNatureList;

        public List<EnterpriseBean> comapnyTypeList;    // name from json data, the nonstandard name crap
        public List<EnterpriseBean> companyNatureList;
    }
    public static class EnterpriseBean{
        public String content;
        public String value;
    }
}
