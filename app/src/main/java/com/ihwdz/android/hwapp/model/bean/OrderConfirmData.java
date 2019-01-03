package com.ihwdz.android.hwapp.model.bean;


/**
 * <pre>
 * author : Duan
 * time : 2018/12/06
 * desc :
 * version: 1.0
 * </pre>
 */
public class OrderConfirmData {
    public String code;
    public String msg;
    public OrderConfirmEntity data;

    public static class OrderConfirmEntity{
        public String code;
        public Long memberPurchaseId = 0L;							// 会员求购单Id
        public Long sellMemberQuoteId = 0L;						    // 报价单Id
        public Long memberId = 0L;									// 会员id
        public String memberName = "";								// 会员名称
        public Integer authStatus = 0;				                // 买家认证状态 1==已认证,0==未认证
        public Long sellMemberId = 0L;								// 商家id
        public String sellMemberName = "";							// 商家名称
        public Integer isSupplierDistribution = 0;					// 是否供配 0-否 1-是
        public String isSupplierDistributionStr = "";				// 是否供配描述
        public Integer distributionWay = 0;                         // 配送方式
        public String logisticsAmt = "";							// 物流单价
        public String deliveryDate = "";							// 交货日期

        public String provinceCode = "";							// 省code
        public String province = "";								// 省
        public String cityCode = "";								// 城市code
        public String city = "";									// 城市
        public String districtCode = "";							// 区code
        public String district = "";								// 区
        public String townCode = "";								// 街道code
        public String town = "";									// 街道
        public String address = "";								    // 地址
        public String contact = "";								    // 联系人
        public String mobile = "";									// 电话

        public String totalAmount = "";					            // 总额度
        public String usedAmount = "";						        // 已用额度
        public String availableAmount = "";			        	    // 可用额度

        // 修改授信额度（结算方式:部分授信  授信额度范围 大于0 小于（单价 + 物流单价）*数量       且小于 最大剩余授信额度（不是剩余授信额度） ）
        public String maxAvailableAmount = "";				        // 最大可用额度，可用额度+总额度的20%  (部分授信 - 授信额度 - 范围限制条件之一)

        public Integer periodType = 0;						        // 账期类型 10==天,20==月,30==年
        public Integer accountPeriod = 0;					        // 账期

        public String breed = "";									// 品种
        public String breedAlias = "";								// 品种别名
        public String breedCode = "";								// 品种code

        public String spec = "";									// 规格
        public String factory = "";								    // 厂家

        public String qty = "";									    // 数量
        public String price = "";									// 采购价格
        public String purchaseSumAmt = "";						    // 采购总金额



        public String sellMemberQuoteProvinceCode = "";						// 省code
        public String sellMemberQuoteProvince = "";							// 省
        public String sellMemberQuoteCityCode = "";							// 城市code
        public String sellMemberQuoteCity = "";								// 城市
        public String sellMemberQuoteDistrictCode = "";						// 区code
        public String sellMemberQuoteDistrict = "";							// 区
        public String sellMemberQuoteTownCode = "";							// 街道code
        public String sellMemberQuoteTown = "";								// 街道
        public String sellMemberQuoteWarehouseAddress = "";					// 仓库地址
        public String sellMemberQuoteWarehouse = "";				        // 仓库名称
        public String sellMemberQuoteWarehousePhone = "";					// 仓库联系电话
        public String sellMemberQuoteWarehouseContact = "";					// 仓库联系人

        public String memberMoneyRate = "0.000";                               // 服务费利率

        public String packagSpec = "";							               // 包装规格

        public Integer isPoundage = 0;                                         // 是否已缴纳手续费 0=否 1=是（手续费 == 0）

        public Integer depositMoney = 0;                                       // 是否缴纳保证金 0=否（结算方式只能 款到发货） 1=是

    }

}
