package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/22
 * desc :   订单详情 & 确认订单 &
 * version: 1.0
 * </pre>
 */
public class OrderDetailData {

    public String code;
    public String msg;
    public OrderDetailEntity data;

    /**
     *  for order detail
     */
    public static class OrderDetailEntity{

        public String id;             //
        public String orderSn;        //
        public String serviceCharge;  // 手续费
        public List<OrdersData.OrderItem> orderItems;

        public Long memberId = 0L;									// 会员id
        public String memberName = "";								// 会员名称
        public Integer distributionWay = 0;                         // 配送方式
        public String deliveryDate = "";							// 交货日期
        public OrderAddress address;


        public String contractCode;             //
        public String creditAmount;             //
        public String isOverdue;             //
        public String isReceivedComplete;             //
        public String isSendGoodsComplete;             //
        public String logisticsSumCost;             //
        public String memberAdminDepId;             //
        public String memberAdminId;             //
        public String memberAdminName;             //
        public String note;             //
        public String payTime;             //
        public int payWay;             //
        public String purchaseContractCode;             //

        public String purchaseSumAmt;          // 卖家总金额？

        public String purchaseSumQty;             //
        public String receivableDay;             //
        public String receivableTime;             //
        public int receivableWay;             //

        public String saleSumAmt;             // 买家销售总金额？

        public String saleSumQty;             //
        public String sendGoodsSignTime;             //
        public String sendGoodsTime;             //
        public String status;             //
        public String subStatus;             //
        public String userId;             //
        public String userName;             //
        public String userPhone;             //
    }

    public static class OrderAddress{
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
