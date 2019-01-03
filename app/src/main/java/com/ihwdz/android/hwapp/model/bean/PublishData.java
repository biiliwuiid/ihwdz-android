package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :   发布求购时 获取 品名 牌号等信息
 * version: 1.0
 * </pre>
 */
public class PublishData {

    public String code;
    public String msg;
    public List<ProductEntity> data;

    // 产品 breed spec brand
    public static class ProductEntity{
        public String code;
        public String name;
        public String pySName;   // 别名 - alias
    }



    // 原料品名
    public static class BreedEntity{
        public String code;
        public String name;
        public String pySName;   // 别名 - alias
    }

    // 原料牌号
    public static class SpecEntity{
        // public String categoryCode;
        public String name;
    }

    // 厂家
    public static class BrandEntity{
        public List<ProductEntity> recordList;

    }
    // 厂家
    public class PublishBrandData {
        public String code;
        public String msg;
        public BrandEntity data;

    }

    // 发布的求购信息 - post - items
    public static class PublishPurchaseData {
        public String breed;
        public String breedAlias;
        public String breedCode;
        public String spec;
        public String factory;
        public String qty;
    }
    // 收货信息 - post - address
    public static class ReceiverInfo{
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

    public static class MemberPurchaseDTOs{
        public String token;
        public List<PublishPurchaseData> items;
        public ReceiverInfo address;
    }




    // 收货信息 - getAddressDATA - address
    public static class AddressData{
        public String code;
        public String msg;
        public List<AddressEntity> data;
    }
    // 收货信息 - getAddressDATA - address
    public static class AddressEntity{

        public String provinceCode;
        public String provinceName;
        public String cityCode;
        public String cityName;
        public String districtCode;
        public String districtName;
        public String address;

        public String contactName;
        public String mobile;

        public String memberDeliveryAddressId;
        public int apply;            // 1-审核中
        public int allowApply = 0;	 // Integer 是否允许修改 0 -否 1 -是
        public int isDefault;        // 0 -否 ; 1-是
        public String isDefaultStr;
    }


}
