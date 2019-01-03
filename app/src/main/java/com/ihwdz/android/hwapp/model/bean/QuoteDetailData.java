package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :   报价详情 - 商家
 * version: 1.0
 * </pre>
 */
public class QuoteDetailData {

    public String code;
    public String msg;
    public String timeStamp;
    public QuoteEntity data;

    public static class QuoteEntity{
        public int adminDepId;
        public int adminId;
        public String adminName;
        public String breed;        //  breed + spec + factory
        public String breedAlias;
        public String breedCode;
        public String creator;
        public int creatorId;
        public String creatorMobile;
        public long creatTime;
        public String dateTimeStr;
        public String deliveryDate;  //  交货日期
        public String factory;       //  breed + spec + factory
        public int id;
        public int isDelete;
        public int isOverdue;
        public int isSupplierDistribution;  // 是否供配 0-否 1-是
        public long lastAccess;
        public double logisticsAmt;       // 物流单价?
        public long memberPurchaseId;
        public double price;             //  单价?
        public double qty;               //  采购量
        public String reviewNote;        //  备注
        public int reviewPrice;
        public int sellMemberId;
        public String sellMemberName;
        public String source;
        public String spec;           //  breed + spec + factory
        public int status;            //  判断订单状态
        public int userId;
        public int version;
        public WareHouse warehouse;   // 仓库
    }

    public static class WareHouse{
        public String city;         //
        public String cityCode;
        public long creatTime;
        public String district;     //
        public String districtCode;
        public int id;
        public String lastAccess;
        public String province;      //
        public String provinceCode;
        public int sellMemberQuoteId;
        public String town;
        public String townCode;
        public int version;
        public String warehouse;          // 仓库
        public String warehouseAddress;   // 仓库地址
        public String warehouseAlias;     // 仓库别名
        public String warehouseCode;
        public String warehouseContact;
        public long warehouseId;
        public String warehousePhone;
    }

}
