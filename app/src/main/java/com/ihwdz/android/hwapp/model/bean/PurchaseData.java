package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/08
 * desc :
 * version: 1.0
 * </pre>
 */
public class PurchaseData {
    public String code;
    public String msg;
    public PurchaseEntity data;

    public static class PurchaseEntity{
        public int currentPage;
        public int numPerPage;
        public int totalCount;
        public List<PurchaseModel> recordList;
        public int pageCount;
        public int beginPageIndex;
        public int endPageIndex;
        public String countResultMap;
    }

    // 求购 / 我的求购
    public static class PurchaseModel{
        public String brand;
        public String breed;          //
        public String breedAlias;
        public String breedCode;
        public String cityCode;
        public String cityName;
        public String dateTimestr;     //
        public String districtCode;
        public String districtName;
        public String id;              // ------------------------ for purchase detail
        public int isDelete;
        public String memberName;
        public String memberPurchaseAddress;
        public String provinceCode;
        public String provinceName;
        public String qty;                  // 采购量
        public String sellMemberQuoteCount; // 报价商家数
        public String sellMemberQuoteId;    // --------------------for quote detail
        public String spec;

        // default: 显示“报价”（此时商家未报价）
        // -2: 未登录;     - 不显示“报价”
        // -1: 商家待审核;
        // 0: 商家未报价;
        // 1: 商家已报价;   - 显示“已报价”
        // 2: 交易会员;     - 不显示“报价”
        // 3: 资讯;

        // 商家 订单状态  0-待物流确认(不显示)
        // 1-待买家确认 10-待复议 20-待二次确认 88-已确认报价（已下单） 99-失效 (交易会员 求购报价 详情)
        public String status;

        // 我的报价
        public String factory;         //
        public String price;           //
        public String reviewNote;      //

        public String memberPurchaseCity;     //
        public String memberPurchaseDistrict; //
        public String memberPurchaseProvince; //
    }

    // 我的报价
    public static class QuoteModel{
        public String breed;
        public String creator;
        public String creatorId;
        public String creatorMobile;
        public String creatTime;
        public String deliveryDate;
        public String factory;       //
        public String isSupplierDistribution;
        public String logisticsAmt;
        public String memberId;
        public String memberName;
        public String memberPurchaseAddress;
        public String memberPurchaseCity;     //
        public String memberPurchaseDistributionAddressId;
        public String memberPurchaseDistrict; //
        public String memberPurchaseId;
        public String memberPurchaseProvince; //

        public String price;           //
        public String qty;
        public String reviewNote;      //

        public String sellMemberId;
        public String sellMemberName;
        public String sellMemberQuoteCity;
        public String sellMemberQuoteDistrict;
        public String sellMemberQuoteId;  // --------------------for detail
        public String sellMemberQuoteProvince;
        public String sellMemberQuoteWarehouseId;
        public String source;
        public String spec;

        // 商家 订单状态  0-待物流确认 1-待买家确认 10-待卖家复议 20-待二次确认 88-已确认报价 99-失效
        public String status;
        public String warehouse;
        public String warehouseAddress;
        public String warehouseId;
    }

    // 仓库信息
    public static class Warehouse{
        public Long id = 0l;	            // 仓库id

        public String warehouse = "";		// 仓库名称

        public String provinceCode = "";	// 省code
        public String province = "";		// 省

        public String cityCode = "";		// 城市code
        public String city = "";			// 城市

        public String districtCode = "";	// 区code
        public String district = "";		// 区

        public String warehouseAddress = "";	// 仓库地址
        public String warehousePhone = "";		// 仓库联系电话
        public String warehouseContact = "";	// 仓库联系人
    }


}
