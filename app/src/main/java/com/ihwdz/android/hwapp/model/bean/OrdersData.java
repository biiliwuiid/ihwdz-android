package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/28
 * desc :   订单列表 （OrderFragment）
 * version: 1.0
 * </pre>
 */
public class OrdersData {

    public String code;
    public String msg;
    public OrdersEntity data;       // 订单信息

    public static class OrdersEntity{

        public int currentPage;
        public int numPerPage;
        public int totalCount;
        public List<OrdersModel> recordList;
        public int pageCount;
        public int beginPageIndex;
        public int endPageIndex;
        public String countResultMap;
    }

    public static class OrdersModel{
        public String orderId;
        public Double purchaseSumAmt = 0D;	// 采购总金额 - 商家
        public String saleSumAmt;           // 采购总金额 - 买家
        public String saleSumQty;

        public String orderSn;        // ? have it?
        public String contractCode;   // 订单号- 合同号
        public String serviceCharge;  // 手续费

        public String memberName;     // 买家
        public String sellMemberName; // 卖家


        public String subStatus;
        public String operateStatus;   // null没有; A0- 支付手续费; A1- 开票申请; A2- 申请展期

        public List<OrderItem> orderItems;
        //public List<OrderCost> orderCostList;
    }

    public static class OrderItem{
        public String brand;
        public String breed;
        public String spec;
        public String qty;
        public String salePrice;     // 买家
        public Double purchasePrice; // 商家
        public String warehouse;

    }

    public static class OrderCost{
        public String amt;         // double? float?
        public String contractCode;
        public String creatTime;
        public String id;
        public String orderId;
        public String rate;
        public String type;
        public String version;
        public String lastAccess;

    }

}
