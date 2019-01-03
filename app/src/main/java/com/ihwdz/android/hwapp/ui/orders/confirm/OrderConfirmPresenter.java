package com.ihwdz.android.hwapp.ui.orders.confirm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrderConfirmData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.ihwdz.android.hwapp.common.Constant.Remarks.CREDIT_LINE;


/**
 * <pre>
 * author : Duan
 * time : 2018/11/22
 * desc : 交易会员一键下单- 确认订单（供配 & 非供配）
 * version: 1.0
 * </pre>
 */
public class OrderConfirmPresenter implements OrderConfirmContract.Presenter{

    String TAG = "OrderConfirmPresenter";

    @Inject OrderConfirmActivity parentActivity;
    @Inject OrderConfirmContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    private boolean isSubmitClicked = false;  // 是否在提交

    boolean isAuthenticated = Constant.isAuthenticated;  // 交易会员已认证

    private String currentQuoteId;          // 报价 id -> getOrderData(订单详情)

    private int currentIsPoundage = 0;      // 是否已缴纳 年手续费 0=否 1=是（手续费 == 0）

    private boolean isDepositPaid = false;      // 是否缴纳保证金
    // private int currentDepositMoney = 0;    // 是否缴纳保证金   0=否 1=是（否: 结算方式只能 款到发货2）

    private String currentCreditLeft;       // 剩余额度（部分授信 -> 剩余额度）- availableAmount
    private String maxAvailableAmount;      // 最大可用额度，可用额度+总额度的20%  (部分授信 - 授信额度 - 范围限制条件之一)

//    public String totalAmount = "";					            // 总额度
//    public String usedAmount = "";						        // 已用额度
//    public String availableAmount = "";			        	    // 可用额度

    private double currentChange = 0.00d;       // 当前 手续费

    private boolean isDistribution = false;     // 是否供配
    private Integer isSupplierDistribution = 0;	// 配送方式 供配: 0-否 1-是

    private String currentDistribution;     // 当前选择的 配送方式 - distributionWay（Integer）

    private Double logisticsAmt = 0D;	     // 运费单价 （自提和供配 时 =0； 只有平台配送有值）
    private Double currentLogisticsAmt = 0D; // 运费单价 （自提和供配 时 =0； 只有平台配送有值）

    private Double price = 0D;	            // 采购单价

    private int paymentDayMax = 30;         // 账期选择器 最大范围


    /**
     *  一键下单 提交参数：
     */
    private Long memberPurchaseId = 0L;						// 会员求购单Id
    private Long sellMemberQuoteId = 0L;					// 报价单Id

    private Double saleSumQty = 0D;							// 销售总重量
    private Double saleSumAmt = 0D;							// 销售总金额
    private Double purchaseSumQty = 0D;						// 采购总重量
    private Double purchaseSumAmt = 0D;						// 采购总金额

    private Double logisticsSumCost = 0D;					// 物流总货款
    private Double salePrice = 0D;							// 销售单价

    private Integer distributionWay = 0;					// 配送方式 0-供方配送 1-自提 2-平台配送

    private String deliveryDate = "";						// 交货日期
    // private String  = "";						        // 交货日期
    private Integer receivableWay = 2;						// 结算方式 0-全额授信 1-部分授信 2-款到发货(default) 3-货到当天付款
    private Double creditAmount = 0D;						// 授信金额
    private Integer receivableDay = 0;						// 账期

    private String packagSpec = "";							// 包装规格

    private Double memberMoneyRate = 0D;					// 资金服务费利率 （value%, 计算时要/100）
    private	Double serviceAmt = 0D;							// 资金服务费
    // 一键下单 提交参数 end

    // 选择配送方式 : 平台配送/自提
    String distributionPlatformStr;
    String distributionSelfStr;

    //结算方式: 0-全额授信 1-部分授信 2-款到发货 3-货到当天付款
    String creditAllStr;
    String creditPartStr;
    String payFirstStr;
    String payOnDeliveryDayStr;

    String depositMoneyRemindStr;  // 提示未缴保证金
    String onlyPayFirstRemindStr;  // 提示该会员只能款到发货


    @Inject
    public OrderConfirmPresenter(OrderConfirmActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);

        distributionPlatformStr = parentActivity.getResources().getString(R.string.distribution_platform);
        distributionSelfStr = parentActivity.getResources().getString(R.string.distribution_self);

        creditAllStr = parentActivity.getResources().getString(R.string.credits_all_payment);
        creditPartStr = parentActivity.getResources().getString(R.string.credit_part_payment);
        payFirstStr = parentActivity.getResources().getString(R.string.pay_first_payment);
        payOnDeliveryDayStr = parentActivity.getResources().getString(R.string.pay_on_day_payment);

        depositMoneyRemindStr = parentActivity.getResources().getString(R.string.deposit_null_remind);

        onlyPayFirstRemindStr = parentActivity.getResources().getString(R.string.pay_first_only_remind);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(parentActivity != null){
            parentActivity = null;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void store(Bundle outState) {

    }

    @Override
    public void restore(Bundle inState) {

    }

    @Override
    public void setCurrentId(String id) {
        this.currentQuoteId = id;
    }

    // 是否缴纳保证金 code :"0" 已缴纳保证金
    @Override
    public void getCheckDepositData(final int receivableWay) {
        Subscription rxSubscription = model
                .getCheckDepositData("" + receivableWay)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getCheckDepositData onError: " + e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {

                        if (TextUtils.equals("0", data.code)){
                            // 已交保证金
                            isDepositPaid = true;
                            setPayMethod(receivableWay); // 结算方式
                        }else {
                            // 未交保证金 -> 结算方式 只允许“款到发货”
                            isDepositPaid = false;
                            setPayMethod(receivableWay); // 结算方式
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getOrderDetailData() {
        // 求购报价 - 一键下单-> 确认订单- 获取订单信息
        // currentQuoteId = "431";
        LogUtils.printCloseableInfo(TAG, "getOrderDetailData currentQuoteId: " + currentQuoteId);
        Subscription rxSubscription = model
                .getOrderConfirmData(currentQuoteId)
                .compose(RxUtil.<OrderConfirmData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrderConfirmData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getOrderDetailData onError: " + e.toString());
                    }

                    @Override
                    public void onNext(OrderConfirmData data) {
                        LogUtils.printCloseableInfo(TAG, "getOrderDetailData onNext: " +data.data.sellMemberQuoteId );
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                setCurrentOrderData(data.data);
                                mView.updateView(data.data);
                            }
                        }else {
                            LogUtils.printCloseableInfo(TAG, "getOrderDetailData onNext: " + data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 是否已缴纳手续费 0=否 1=是（手续费 == 0）
    @Override
    public void setIsPoundage(int is) {
        this.currentIsPoundage = is;
    }

    // 是否缴纳保证金 0=否（结算方式只能 款到发货） 1=是 -> 接口判断: getCheckDepositData
//    @Override
//    public void setDepositMoney(int is) {
//        this.currentDepositMoney = is;
//    }

    // 是否是供配  isSupplierDistribution : 0-否; 1-是
    @Override
    public void setIsSupplierDistribution(int is) {
        this.isSupplierDistribution = is;
        // 是否是供配 0-否; 1-是
        isDistribution = isSupplierDistribution == 1;
        // LogUtils.printCloseableInfo(TAG, "isSupplierDistribution: " +isSupplierDistribution + "  isDistribution: "+isDistribution);
        if (isDistribution){
            mView.initDistributionView();   // 供配
        }else {
            mView.initNoDistributionView(); // 非供配
        }
    }

    @Override
    public boolean getIsSupplierDistribution() {
        return (isSupplierDistribution == 1);
    }


    // 选择 配送方式 ( 0-供方配送 1-自提 2-平台配送)
    @Override
    public void selectDistribution() {
        // mView.showPromptMessage("底部弹框 配送方式");

        String distributionSelfStr = parentActivity.getResources().getString(R.string.distribution_self);
        String distributionPlatformStr = parentActivity.getResources().getString(R.string.distribution_platform);

        List<String> data = new ArrayList<>();

        data.add(distributionSelfStr);
        data.add(distributionPlatformStr);

        SinglePicker<String> picker = new SinglePicker<>(parentActivity, data);
        picker.setCanceledOnTouchOutside(false);
        picker.setSelectedIndex(getCurrentDistributionIndex());
        picker.setCycleDisable(true);
        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                setDistribution(index, item);
            }
        });
        picker.show();
    }
    // 配送方式 index
    private int getCurrentDistributionIndex(){
        if (distributionWay > 1){
            return (distributionWay -1);
        }else {
            return 1;
        }
    }


    // 设置配送方式: 0-供方配送 1-自提 2-平台配送
    @Override
    public void setDistribution(int index, String distribution) {
        this.distributionWay = index + 1  ;
        this.currentDistribution = distribution;
        // LogUtils.printCloseableInfo(TAG, "setDistribution 配送方式: " + distributionWay + " | " + currentDistribution);
        switch (distributionWay){
            case 1:
                mView.initTakeTheirView(); // 1-自提（运费单价 0）
                logisticsAmt = 0D;
                setPayMethod(2);
                break;
            case 2:
                logisticsAmt = currentLogisticsAmt;
                mView.initPlatformView(); // 2-平台配送
                break;
        }
        getCharge();        // 配送方式
        getServiceAmt();    // 配送方式
        getTotalSumAmt();   // 配送方式


    }

    // 选择交货日期
    @Override
    public void selectDeliveryDate() {
        // 当日日期
        int currentYear, currentMonth, currentDay;
        String todayStr = DateUtils.getDateTodayString();
        //LogUtils.printCloseableInfo(TAG, todayStr);
        String[] strs = todayStr.split("-");
        //LogUtils.printCloseableInfo(TAG, ""+strs.length);
        for (int i = 0; i < strs.length; i++){
            LogUtils.printCloseableInfo(TAG, "i: " +i+" - "+ strs[i]);
        }
        currentYear = Integer.valueOf(strs[0]);
        currentMonth = Integer.valueOf(strs[1]);
        currentDay = Integer.valueOf(strs[2]);

        final DatePicker picker = new DatePicker(parentActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(parentActivity, 10));
        picker.setRangeEnd(currentYear + 1, currentMonth, currentDay);
        picker.setRangeStart(currentYear, currentMonth, currentDay);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                setDeliveryDate(year + "-" + month + "-" + day);

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.setLabel(" "," ", " ");
        picker.setTitleText(currentYear + "-" + currentMonth + "-" + currentDay);
        picker.show();
    }

    // 设置交货日期
    @Override
    public void setDeliveryDate( String deliveryDate) {
        this.deliveryDate = deliveryDate;
        mView.updateDeliveryDate(deliveryDate);
    }

    // 选择结算方式: 交易会员 -已认证(0 全部授信、1 部分授信、2 款到发货)
    //                       -未认证(2 款到发货、3 货到当天付款)
    @Override
    public void selectPayMethod() {
        List<String> data = new ArrayList<>();
        if (isAuthenticated){
            // 已认证交易会员 (0 全部授信、1 部分授信、2 款到发货)
            // LogUtils.printCloseableInfo(TAG, "selectPayMethod 选择结算方式: 交易会员已认证");
            data.add(String.format(creditAllStr, currentCreditLeft));
            data.add(creditPartStr);
            data.add(payFirstStr);
        }else {
            // 未认证交易会员 (2 款到发货、3 货到当天付款)
            // LogUtils.printCloseableInfo(TAG, "selectPayMethod 选择结算方式: 未认证交易会员");
            data.add(payFirstStr);
            data.add(payOnDeliveryDayStr);
        }
        int currentSelectedIndex = getCurrentPayMethodIndex(isAuthenticated);
        // LogUtils.printCloseableInfo(TAG, "currentSelectedIndex: " + currentSelectedIndex);

        SinglePicker<String> picker = new SinglePicker<>(parentActivity, data);
        picker.setCanceledOnTouchOutside(false);
        picker.setSelectedIndex(currentSelectedIndex);
        picker.setCycleDisable(true);

        picker.setOnItemPickListener(new SinglePicker.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {

                // LogUtils.printCloseableInfo(TAG, "onItemPicked index: " + index);
                if (isAuthenticated){
                    checkDeposit(index);
                    //setPayMethod(index, item);
                }else {
                    checkDeposit(index + 2);
                    //setPayMethod(index + 2, item);
                }
            }
        });
        picker.show();
    }

    private int getCurrentPayMethodIndex(boolean isAuthenticated){
        if (isAuthenticated){
            return receivableWay;
        }else {
            return (receivableWay - 2);
        }
    }

    // 校验保证金
    @Override
    public void checkDeposit(int receivableWay) {
        getCheckDepositData(receivableWay);
    }


    // 设置结算方式: 0-全额授信 1-部分授信 2-款到发货(授信额度 0)  3-货到当天付款（授信额度：采购总金额+运费总金额; 账期1天）
    // 未认证会员 不需要判断 服务费利率和 保证金
    @Override
    public void setPayMethod(int index) {
        receivableWay = index;
        // LogUtils.printCloseableInfo(TAG, " 是否缴纳保证金:  0=否;1=是 : " + currentDepositMoney);
        switch (receivableWay){
            case 0: // 结算方式: 0-全额授信
                if (isAuthenticated){
                    if (!isDepositPaid){
                        // 是否缴纳保证（结算方式只能 款到发货）-》提示未缴保证金
                        mView.showRemindDialog(depositMoneyRemindStr);
                        setPayMethod(2);
                    }else {
                        if(this.memberMoneyRate == 0){
                            // 资金服务费利率 == 0 -》款到发货 提示该会员只能款到发货
                            mView.showRemindDialog(onlyPayFirstRemindStr);
                            setPayMethod(2);
                        }else {
                            setPaymentDay(paymentDayMax);
                            mView.initCreditAllView();
                        }
                    }
                }else {
                    setPaymentDay(paymentDayMax);
                    mView.initCreditAllView();
                }


                break;
            case 1: // 结算方式:  1-部分授信
                if (isAuthenticated){
                    if (!isDepositPaid){
                        // 是否缴纳保证（结算方式只能 款到发货）-》提示未缴保证金
                        mView.showRemindDialog(depositMoneyRemindStr);
                        setPayMethod(2);
                    }else {
                        if(this.memberMoneyRate == 0){
                            // 资金服务费利率 == 0 -》款到发货
                            mView.showRemindDialog(onlyPayFirstRemindStr);
                            setPayMethod(2);
                        }else {
                            setCurrentCreditLine(0);
                            setPaymentDay(paymentDayMax);
                            mView.initCreditView();
                        }
                    }

                }else {
                    setCurrentCreditLine(0);
                    setPaymentDay(paymentDayMax);
                    mView.initCreditView();
                }

                break;
            case 2: // 结算方式:  2-款到发货(授信额度 0)
                setPaymentDay(0);
                setCurrentCreditLine(0);
                mView.initPayFirstView();
                break;
            case 3: // 结算方式:  3-货到当天付款（授信额度：采购总金额+运费总金额; 账期1天）
                if (this.memberMoneyRate == 0){
                    mView.showRemindDialog(onlyPayFirstRemindStr);
                    setPayMethod(2);
                }else {
                    setPaymentDay(1);
                    mView.initPayOnDeliveryDayView();
                }
                break;
        }

        getSalePrice();    // 销售单价 - 设置结算方式
        getServiceAmt();   // 资金服务费 -设置结算方式
        getCharge();       // 手续费 -设置结算方式
        getCreditAmount(); // 使用授信 -设置结算方式
        getTotalSumAmt();  // 合计金额 -设置结算方式
    }

    @Override
    public int getCurrentPayMethod() {
        return receivableWay;
    }

    // 选择账期
    @Override
    public void selectPaymentDay() {

        // mView.showPromptMessage("底部弹框 账期");

        NumberPicker picker = new NumberPicker(parentActivity);
        picker.setCycleDisable(true);
        picker.setDividerVisible(false);
        picker.setOffset(2);                               //偏移量
        picker.setRange(1, paymentDayMax, 1);//数字范围
        picker.setSelectedItem(receivableDay);
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                //currentPaymentDay  = item.intValue();
                setPaymentDay(item.intValue());
            }
        });
        picker.show();
    }


    // 设置账期
    @Override
    public void setPaymentDay(int paymentDay) {
        if (paymentDay >= 1){
            mView.updatePaymentDay(paymentDay);
        }

        this.receivableDay = paymentDay;

        // LogUtils.printCloseableInfo(TAG, "setPaymentDay 设置账期: ================ ");

        // TODO: 2018/12/4 计算金额
        getCreditAmount();     // 使用授信 -设置账期
        getServiceAmt();       // 资金服务费 -设置账期
        getSalePrice();        // 销售单价 -设置账期
        getLogisticsSumCost(); // 物流总货款 = 运费单价*数量 -设置账期
        getPurchaseSumAmt();   // 采购总金额 -设置账期
        // getSaleSumAmt();       // 销售总金额  合计金额中会调用 -设置账期
        getCharge();           // 手续费 -设置账期
        getTotalSumAmt();      // 合计金额 = 销售总货款 + 手续费 -设置账期
    }

    // 授信额度-使用授信（自填）（结算方式:部分授信）
    @Override
    public void setCurrentCreditLine(double creditLine) {
        // LogUtils.printCloseableInfo(TAG, "setCurrentCreditLine 授信额度:使用授信 ================ ");

        String creditLineStr = String.format("%.2f", creditLine);
        creditLine = Double.valueOf(creditLineStr);

        this.creditAmount = creditLine;
        mView.updateCreditUsed(creditAmount);
        getServiceAmt();   // 授信额度-
        getCharge();       // 授信额度-
        getTotalSumAmt();  // 授信额度-


    }

    @Override
    public double getCurrentCreditLine() {
        return this.creditAmount;
    }

    // 修改授信额度（结算方式:部分授信  授信额度范围 大于0 小于（单价 + 物流单价）*数量 且 小于 最大剩余授信额度（不是剩余授信额度） ）
    @Override
    public void updateCredit() {
        String currentContent;
        boolean isHint = true;
        if (creditAmount > 0){
            currentContent = ""+creditAmount;
            isHint = false;
        }else {
            currentContent = parentActivity.getResources().getString(R.string.credit_hint);
        }

        // LogUtils.printCloseableInfo(TAG, "修改授信额度: 最大可用额度 : " + this.maxAvailableAmount);
        // LogUtils.printCloseableInfo(TAG, "修改授信额度: 采购总金额 : " + this.maxAvailableAmount);
        double max1 = 0.00d;
        double max2 = 0.00d;
        if (this.maxAvailableAmount != null && this.maxAvailableAmount.length() > 0){
            max1 = Double.valueOf(this.maxAvailableAmount);
        }
        max2 = (this.price + this.logisticsAmt)* saleSumQty;

        // 传 剩余额度 ,采购总金额， 最大可用额度
        ReviewActivity.startCreditReviewActivity(parentActivity, CREDIT_LINE, currentContent,isHint, currentCreditLeft, max1, max2);
    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    // 提交订单
    @Override
     public void doSubmit() {
        isSubmitClicked = true;
        mView.makeButtonDisable(isSubmitClicked);

        LogUtils.printCloseableInfo(TAG + "Submit", "=========== doSubmit 提交订单:");
        LogUtils.printError(TAG + "Submit", "=========== doSubmit 提交订单:");
        LogUtils.printCloseableInfo(TAG, "------------------------------------------------------------------------");
//        LogUtils.printCloseableInfo(TAG, "memberPurchaseId: " + memberPurchaseId);
//        LogUtils.printCloseableInfo(TAG, "sellMemberQuoteId: " + sellMemberQuoteId);
//        LogUtils.printCloseableInfo(TAG, "saleSumQty: " + saleSumQty);
//        LogUtils.printCloseableInfo(TAG, "saleSumAmt: " + saleSumAmt);
//        LogUtils.printCloseableInfo(TAG, "purchaseSumQty: " + purchaseSumQty);
//        LogUtils.printCloseableInfo(TAG, "purchaseSumAmt: " + getPurchaseSumAmt());
//        LogUtils.printCloseableInfo(TAG, "logisticsSumCost: " + getLogisticsSumCost());
//        LogUtils.printCloseableInfo(TAG, "salePrice: " + getSalePrice());
//        LogUtils.printCloseableInfo(TAG, "distributionWay: " + distributionWay);
//        LogUtils.printCloseableInfo(TAG, "deliveryDate: " + deliveryDate);
//        LogUtils.printCloseableInfo(TAG, "receivableWay: " + receivableWay);
//        LogUtils.printCloseableInfo(TAG, "creditAmount: " + creditAmount);
//        LogUtils.printCloseableInfo(TAG, "receivableDay: " + receivableDay);
//        LogUtils.printCloseableInfo(TAG, "packagSpec: " + packagSpec);
//
//        String memberMoneyRateStr = new DecimalFormat("0.0000").format(memberMoneyRate);
//        LogUtils.printCloseableInfo(TAG, "memberMoneyRate: " + memberMoneyRate);
//
//        LogUtils.printCloseableInfo(TAG, "memberMoneyRateStr: " + memberMoneyRateStr);
//        LogUtils.printCloseableInfo(TAG, "memberMoneyRate/100: " + memberMoneyRate * 0.01);
//
//        String memberMoneyRateStr100  = new DecimalFormat("0.0000").format(memberMoneyRate * 0.01);
//        LogUtils.printCloseableInfo(TAG, "memberMoneyRateStr100: " + memberMoneyRateStr100);
//
//        LogUtils.printCloseableInfo(TAG, "serviceAmt: " + serviceAmt);
//        LogUtils.printCloseableInfo(TAG, "-------------------------------------------------------------------------");

        Subscription rxSubscription = model
                .postOrderData( memberPurchaseId, sellMemberQuoteId, saleSumQty, saleSumAmt,
                        purchaseSumQty,getPurchaseSumAmt(),getLogisticsSumCost(), getSalePrice(),
                        distributionWay, deliveryDate, receivableWay, creditAmount,
                        receivableDay, packagSpec, memberMoneyRate * 0.01, serviceAmt)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.printCloseableInfo(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.printError(TAG, "doSubmit order: onError: " + e.toString());
                        // isSubmitClicked = false;
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        LogUtils.printCloseableInfo(TAG, "onCompleted");
                        if (TextUtils.equals("0", data.code)){
                            LogUtils.printCloseableInfo(TAG, "doSubmit order: onNext: " + data.msg);
                            if (data.data){
                                gotoOrderFragment();
                            }else {
                                mView.showPromptMessage(data.msg);
                            }

                        }else {
                            // isSubmitClicked = false;
                            mView.showPromptMessage(data.msg);
                            LogUtils.printError(TAG, "doSubmit order: onNext: " + data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 提交成功后 -> 订单
    @Override
    public void gotoOrderFragment() {
        MainActivity.startActivity(parentActivity,3);
    }

    // 联系客服弹窗
    @Override
    public View getDialogContentView(String message) {
        //LogUtils.printError(TAG, "getDialogContentView message: " + message);
        if (message.contains(":")){
            String[] strings =  message.split(":");
            phoneNumber = strings[1];
        }

        View view = View.inflate(parentActivity, R.layout.contract_dialog, null);
        TextView messageTv = view.findViewById(R.id.tv);
        messageTv.setText(message);  // 弹窗内容
        return view;
    }

    private String phoneNumber;
    @Override
    public void doContract() {
        //mView.showPromptMessage("请直接点击客服电话");
        Intent Intent =  new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
        startActivity(Intent);
    }


    // 获取订单详情 -> 初始化 需提交的订单数据
    @Override
    public void setCurrentOrderData(OrderConfirmData.OrderConfirmEntity data) {
        this.memberPurchaseId = data.memberPurchaseId;
        this.sellMemberQuoteId = data.sellMemberQuoteId;

        setIsPoundage(data.isPoundage);                        // 是否已缴纳 年手续费 0=否 1=是（手续费 == 0）

        // -> getCheckDepositData 接口判断
        // setDepositMoney(data.depositMoney);                 // 是否缴纳保证金 0=否（结算方式只能 款到发货） 1=是

        // setIsDistribution(data.distributionWay);            // 是否是供配 0-否; 1-是
        setIsSupplierDistribution(data.isSupplierDistribution);// 是否是供配 0-否; 1-是

        setPayMethod(2);                                       // 默认计算方式 2 - 款到发货

        this.deliveryDate = data.deliveryDate;

        this.saleSumQty = Double.valueOf(data.qty);           // 销售总重量
        this.purchaseSumQty = Double.valueOf(data.qty);       // 采购总重量

        this.price =  Double.valueOf(data.price);              // 采购单价
        this.salePrice = getSalePrice();                       // 销售单价  （设置账期后计算）

        this.logisticsAmt = Double.valueOf(data.logisticsAmt); // 运费单价
        this.currentLogisticsAmt = Double.valueOf(data.logisticsAmt); // 运费单价


        this.distributionWay = data.distributionWay;

//        this.saleSumAmt = getSaleSumAmt();                 // 销售总金额 （设置账期后计算）
//        this.purchaseSumAmt = getPurchaseSumAmt();         // 采购总金额 （设置账期后计算）
//        this.logisticsSumCost = getLogisticsSumCost();     // 物流总货款 （设置账期后计算）

        this.packagSpec = data.packagSpec;                     // 包装规格

        this.paymentDayMax = data.accountPeriod;               // 账期最大值

        this.creditAmount = 0d; //Double.valueOf(data.totalAmount); // 授信金额-使用授信
        this.currentCreditLeft = data.availableAmount;              // 授信金额-剩余额度
        this.maxAvailableAmount = data.maxAvailableAmount;          // 最大可用额度，可用额度+总额度的20%  (部分授信 - 授信额度 - 范围限制条件之一)

        getCreditAmount();    // 使用授信  mView.updateCreditUsed(getCreditAmount());
        getCharge();          // 手续费  mView.updateCharge(getCharge());

        this.memberMoneyRate = Double.valueOf(data.memberMoneyRate); // 资金服务费利率
        mView.updateServiceRate(memberMoneyRate);                    // 更新资金服务费利率
        //LogUtils.printCloseableInfo(TAG, "setCurrentOrderData 资金服务费利率: " + memberMoneyRate);

        this.serviceAmt = getServiceAmt();                           // 资金服务费
        mView.updateServiceFee(this.serviceAmt);

        getTotalSumAmt();//mView.updateTotalSum(getTotalSumAmt());   // 合计金额
    }

    /**
     * 授信额度 （结算方式 ：0-全额授信 1-部分授信 2-款到发货 3-）
     * 使用授信（授信金额）
      0 全部授信-使用授信 =（采购单价+运费单价）* 数量 + 资金服务费  == 销售总货款 （ 销售总货款 =（采购单价+运费单价）* 数量 + 资金服务费 ）
      1 部分授信-使用授信 = 授信额度（自填）；
      2 款到发货-使用授信 = 0；
      3 货到当天付款: 使用授信=（采购单价+运费单价）*数量 （账期1天 不展示，资金服务费也不展示）
     */
    public Double getCreditAmount() {

        switch (receivableWay){
            case 0:
                // creditAmount = (this.price + this.logisticsAmt)* saleSumQty + getServiceAmt();
                creditAmount = getSaleSumAmt();
                break;
            case 1:
                //return creditAmount;
                break;
            case 2:
                creditAmount = 0.00d;
                break;
            case 3:
                creditAmount = (this.price + this.logisticsAmt)* saleSumQty;
                break;
            default:
                creditAmount = 0.00d;
                break;
        }
        mView.updateCreditUsed(creditAmount); // 更新使用授信
        // LogUtils.printCloseableInfo(TAG, "getCreditAmount 使用授信: creditAmount: " + creditAmount);
        return creditAmount;
    }


    /**
     * 销售单价
     0 全部授信——销售单价 =（采购单价+物流单价）*（服务费利率*账期+1） (same as 货到当天付款)
     1 部分授信——销售单价 = 授信额度*服务费利率*账期/重量 +采购单价+运费单价
     2 款到发货——销售单价 =  采购单价+物流单价
     3 货到当天付款——销售单价=（采购单价+物流单价）*（服务费利率*账期+1）
     */
    public Double getSalePrice() {

        switch (receivableWay){
            case 0: // 结算方式: 0-全部授信
                salePrice = (this.price + this.logisticsAmt)*(this.memberMoneyRate/100 * this.receivableDay + 1);

                break;
            case 1: // 结算方式:  1-部分授信
                salePrice = getCreditAmount() * this.memberMoneyRate/100 * this.receivableDay/saleSumQty +  this.price + this.logisticsAmt;
                break;
            case 2: // 结算方式:  2-款到发货(授信额度 0)
                salePrice =  (this.price + this.logisticsAmt);
                break;
            case 3: // 结算方式:  3-货到当天付款（授信额度：采购总金额+运费总金额; 账期1天）
                salePrice = (this.price + this.logisticsAmt)*(this.memberMoneyRate/100 * this.receivableDay + 1);
                break;
        }
        //salePrice = (double) Math.round(salePrice * 100) / 100;
//        String salePriceStr = new DecimalFormat("0.00").format(salePrice);
//        salePrice = Double.valueOf(salePriceStr);

//        LogUtils.printCloseableInfo(TAG, "=================================== 销售单价 before: " + salePrice);
        String salePriceStr = String.format("%.2f", salePrice);
        salePrice = Double.valueOf(salePriceStr);


//        LogUtils.printCloseableInfo(TAG, "===================================销售单价: " + salePrice);
//        LogUtils.printCloseableInfo(TAG, "采购单价: " + price);
//        LogUtils.printCloseableInfo(TAG, "物流单价: " + logisticsAmt);
//        LogUtils.printCloseableInfo(TAG, "服务费利率: " + memberMoneyRate);
//        LogUtils.printCloseableInfo(TAG, "账期: " + receivableDay);

//        LogUtils.printCloseableInfo(TAG, "=================================== 销售单价 after: " + salePrice);

        mView.updateSellPrice(salePrice);
        return salePrice;
    }

    /**
     *  资金服务费
     *  结算方式-全部授信: 资金服务费 =（采购单价+运费单价）* 数量* 资金服务费利率* 账期
     *  结算方式-部分授信: 资金服务费 = 授信额度（自填）*资金服务费利率*账期
     *  default:    0d   //资金服务费 =（采购单价+运费单价）* 数量* 资金服务费利率* 账期
     */
    public Double getServiceAmt() {

        switch (receivableWay){
            case 0: // 全部授信
                serviceAmt =  (this.price + this.logisticsAmt)* saleSumQty * (memberMoneyRate /100 ) * receivableDay;
                //LogUtils.printCloseableInfo(TAG, "serviceAmt 资金服务费: " + serviceAmt);
                break;
            case 1: // 部分授信
                serviceAmt =  (getCreditAmount()) * (memberMoneyRate /100 ) * receivableDay;
                break;
            case 2:  // 款到发货
                serviceAmt = 0d;
                break;
            case 3:  // 货到当天付款
                serviceAmt = 0d;
                break;
            default:
                serviceAmt = 0d;
                break;
        }
//        LogUtils.printCloseableInfo(TAG, "资金服务费 =======================================" );
//        LogUtils.printCloseableInfo(TAG, "receivableWay 结算方式 : " + receivableWay);
//        LogUtils.printCloseableInfo(TAG, "price 采购单价: " + price);
//        LogUtils.printCloseableInfo(TAG, "logisticsAmt 运费单价: " + logisticsAmt);
//        LogUtils.printCloseableInfo(TAG, "receivableDay 数量: " + saleSumQty);
//        LogUtils.printCloseableInfo(TAG, "memberMoneyRate 资金服务费利率: " + memberMoneyRate);
//        LogUtils.printCloseableInfo(TAG, "receivableDay 账期: " + receivableDay);
//        LogUtils.printCloseableInfo(TAG, "creditAmount 授信额度: " + creditAmount);
//        LogUtils.printCloseableInfo(TAG, "serviceAmt 资金服务费: " + serviceAmt);
        if (serviceAmt > 0){
            serviceAmt = (double) Math.round(serviceAmt * 100) / 100;
            mView.updateServiceFee(serviceAmt);  // 更新资金服务费
        }else {
            mView.updateServiceFee(serviceAmt);  // 更新资金服务费
        }
        String serviceAmtStr = new DecimalFormat("0.00").format(serviceAmt);
        serviceAmt = Double.valueOf(serviceAmtStr);
        return serviceAmt;
    }


    /**
     * 物流总货款 = 运费单价 * 数量
     * @return
     */
    public Double getLogisticsSumCost() {
        logisticsSumCost = this.logisticsAmt * saleSumQty;
        return logisticsSumCost;
    }

    // 采购总金额
    public Double getPurchaseSumAmt() {
        purchaseSumAmt = this.price * saleSumQty;
        return purchaseSumAmt;
    }

    /**
     * 销售总货款 =（采购单价+运费单价）* 数量 + 资金服务费
     *  change
     *  销售总货款 = 销售单价 * 数量 （保留两位小数）
     */
    public Double getSaleSumAmt() {
        // saleSumAmt = (this.price + this.logisticsAmt)* saleSumQty + getServiceAmt();
        saleSumAmt = getSalePrice() * saleSumQty;

//        String saleSumAmtStr = String.format("%.2f", saleSumAmt);
//        saleSumAmt = Double.valueOf(saleSumAmtStr);

        BigDecimal b = new BigDecimal(saleSumAmt);
        saleSumAmt = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // LogUtils.printCloseableInfo(TAG, "销售总货款  getSaleSumAmt : " + saleSumAmt);
        return saleSumAmt;
    }

    /**
     * currentIsPoundage// 是否已缴纳 年手续费 0=否 1=是（手续费 == 0）
     * 交了年手续费：   手续费 = 0;
     * 否：             手续费 = 销售总货款 * 0.003
     *
     */
    public Double getCharge() {
        //  是否已缴纳 年手续费0=否 1=是（手续费 == 0）
        if (currentIsPoundage == 1){
            currentChange =  0.00d;
            // LogUtils.printCloseableInfo(TAG, "手续费 : " + currentChange);
        }else {
            currentChange = getSaleSumAmt() * 0.003;
        }
        currentChange = (double) Math.round(currentChange * 100) / 100;
        mView.updateCharge(currentChange);   // 更新手续费
        // LogUtils.printCloseableInfo(TAG, "手续费 : " + currentChange);
        return currentChange;
    }

    /**
     * 合计金额 =（销售总货款 + 手续费）
     */
    public Double getTotalSumAmt() {
        double totalAmount = getSaleSumAmt() + getCharge();

//        String totalAmountStr = String.format("%.2f", totalAmount);
//        totalAmount = Double.valueOf(totalAmountStr);

        BigDecimal b = new BigDecimal(totalAmount);
        totalAmount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        mView.updateTotalSum(totalAmount); // 更新合计金额
        return (totalAmount);
    }

    // TODO: 2018/12/27 optimize the codes

}
