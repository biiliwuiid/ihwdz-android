package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :    求购报价
 * version: 1.0
 * </pre>
 */
public class PurchaseQuoteData {

    public String code;
    public String msg;
    public PurchaseQuoteEntity data;

    public class PurchaseQuoteEntity{
        public PurchaseEntity memberPurchaseNewVO;
        public List<QuoteEntity> sellMemberQuoteList;
    }

    // 求购信息
    public class PurchaseEntity{
        public int status;
        public String breed;    // breed + spec + factory
        public String spec;     // breed + spec + factory
        public String factory;  // breed + spec + factory
        public String qty;      // qty
        public String dateTimeStr;
        public Warehouse address;
    }

    // 报价信息
    public class QuoteEntity{
        public String sellMemberQuoteId;  // 商家id
        public String sellMemberName;     // 商家
        public String price;              // 采购报价
        public String logisticsAmt;       // 物流费
        public String reviewNote;         // 复议
        public int status;
    }

    // 仓库信息
    public static class Warehouse{
        public String id;	        // 仓库id

        public String warehouse;    // 仓库名称

        public String provinceCode;	// 省code
        public String province;		// 省

        public String cityCode;		// 城市code
        public String city ;			// 城市

        public String districtCode;	// 区code
        public String district;		// 区

        public String address ;	// 仓库地址
    }


}
