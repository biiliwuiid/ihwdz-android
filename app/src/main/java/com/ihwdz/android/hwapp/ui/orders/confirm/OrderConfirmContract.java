package com.ihwdz.android.hwapp.ui.orders.confirm;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.OrderConfirmData;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/22
 * desc :  交易会员一键下单- 确认订单（供配 & 非供配）
 * version: 1.0
 * </pre>
 */
public interface OrderConfirmContract {

    interface View extends BaseView {

        void initDistributionView();        // 供配    ( 0-供方配送 1-自提 2-平台配送)
        void initNoDistributionView();      // 非供配 （自提/平台配送）

        void initTakeTheirView();           // 自提:  非供配
        void initPlatformView();            // 平台配送: 非供配

        void initCreditAllView();           // 结算方式 - 全部授信 0
        void initCreditView();              // 结算方式 - 部分授信 1 - 授信额度
        void initPayFirstView();            // 结算方式 - 款到发货 2（default）
        void initPayOnDeliveryDayView();    // 结算方式 - 货到当天付款 3（账期1天） -交易会员未认证

        void updateDeliveryDate(String date);   // 更新-交货日期
        void updatePaymentDay(int paymentDay);  // 更新-账期

        void updateServiceRate(double number);  // 更新-资金服务费利率

        void updateServiceFee(double number);   // 更新-资金服务费
        void updateSellPrice(double number);    // 更新-销售单价
        void updateCreditUsed(double number);   // 更新-使用授信
        void updateCharge(double number);       // 更新-手续费
        void updateTotalSum(double number);     // 更新-合计金额

        void updateView(OrderConfirmData.OrderConfirmEntity data);   //  更新订单信息

        void showRemindDialog(String message);  // 用户状态异常（锁定、认证） 联系客服
        void showPromptMessage(String message); // 提示信息

        void makeButtonDisable(boolean disable);               // 按钮状态(是否可用)
    }


    interface Presenter extends BasePresenter {

        void setCurrentId(String id);

        void getOrderDetailData();                      // 订单详情-确认订单信息
        void setCurrentOrderData(OrderConfirmData.OrderConfirmEntity data);   // 订单详情 - 确认订单信息

        void setIsPoundage(int is);                   // 是否已缴纳手续费 0= 否 1= 是（1: 手续费 == 0）
        // void setDepositMoney(int is);              // 是否缴纳保证金   0= 否 1= 是（0: 结算方式只能 款到发货2）

        void setIsSupplierDistribution(int is);       // 是否是供配 0-否; 1-是
        boolean getIsSupplierDistribution();          // 是供配

        // 选择配送方式  底部弹框  平台配送/自提 ( 0-供方配送 1-自提 2-平台配送)
        void selectDistribution();
        void setDistribution(int index, String distribution);

        // 选择交货日期  底部弹框  年月日
        void selectDeliveryDate();
        void setDeliveryDate(String deliveryDate);


        void getCheckDepositData(int receivableWay); // 是否缴纳保证金 get data
        // 选择 结算方式  底部弹框: 0-全额授信 1-部分授信 2-款到发货(授信额度 0)  3-货到当天付款（授信额度：采购总金额 + 运费总金额）
        void selectPayMethod();
        void checkDeposit(int receivableWay);         // 保证金
        void setPayMethod(int index);                 // 设置 结算方式
        int getCurrentPayMethod();

        // 选择账期      底部弹框
        void selectPaymentDay();
        void setPaymentDay(int paymentDay);

        // 使用授信
        void setCurrentCreditLine(double creditLine); // 部分授信 - 授信额度（自填）
        double getCurrentCreditLine();
        void updateCredit();                          // 部分授信 - 修改授信额度

        // void doCalculate();      // 计算金额

        boolean getIsSubmitClicked();// 提交按钮 是否点击

        void doSubmit();                              // 提交订单
        void gotoOrderFragment();                     // 提交成功后->订单

        android.view.View getDialogContentView(String message);         // 弹窗
        void doContract();                                              // 联系客服

    }


}
