package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/19
 * desc :
 * version: 1.0
 * </pre>
 */
public class UserInformation {

    public String code;
    public String msg;
    public UserInfo data;

    public static class UserInfo{
        public String name;
        public String email;
        public String mobile;       // user

        public String companyNature; // 企业性质 20==国有企业,40==中外合资企业,70==私企(非上市公司),80==私企(上市公司),100==个体户,90==其他
        public String taxNumber = "";                   // vip 税号;vip
        public String companyType = "";                 // 企业类型 10 == 贸易商,20==生产企业;
        public String companyFullName = "";	            // 公司全名;

        public String provinceName = "";				// 地址所在省名称;vip
        public String provinceCode = "";			// user 地址所在省编码;
        public String province = "";				// user 地址所在省名称;

        public String  cityName = "";					// 地址所在市名称;vip
        public String  cityCode = "";				// user 地址所在市编码;
        public String  city = "";					// user 地址所在市名称;

        public String  districtName = "";				// 地址所在区名称;vip
        public String  districtCode = "";			// user 地址所在区编码;
        public String  district = "";				// user 地址所在区名称;

        public String  address = "";					// 详细地址;vip

        public String  regTime = "";					// 企业注册日期;vip
        public String  regCapital = "";					// 注册资金;vip
        public String  certificateImgUrl = "";			// 营业执照;vip
        public String ticketImgUrl = "";				// 开票资料;vip
    }
}
