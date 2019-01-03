package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :   发布求购 提交 数据
 * version: 1.0
 * </pre>
 */
public class MemberPurchaseDTOs {

    public String token;
    public List<MemberPurchase> items;
    public MemberPurchaseDistributionAddress address;

    // 产品明细
    public static class MemberPurchase{
        public String breed;
        public String breedAlias;
        public String breedCode;

        public String spec;
        public String factory;
        public String qty;

    }

    // 收货地址 联系人
    public static class MemberPurchaseDistributionAddress{
        public String provinceCode;
        public String province;
        public String cityCode;
        public String city;
        public String districtCode;
        public String district;

        public String address;
        public String contact;
        public String mobile;
    }


}
