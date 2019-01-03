package com.ihwdz.android.hwapp.ui.orders.confirm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrderConfirmData;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.me.usernotes.UserNotesActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.HwAppDialog;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易会员一键下单- 确认订单（供配 & 非供配）: 供配没有“运费单价”
 */
public class OrderConfirmActivity extends BaseActivity implements OrderConfirmContract.View {

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.iv_right) ImageView rightBt;

    @BindView(R.id.tv_order_receiver) TextView tvOrderReceiver;  // 收货人
    @BindView(R.id.tv_address) TextView tvAddress;               // 收货地址

    @BindView(R.id.tv_company_name) TextView tvCompanyName;      // 商家

    @BindView(R.id.tv_breed) TextView tvBreed;
    @BindView(R.id.tv_brand) TextView tvBrand;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.tv_warehouse) TextView tvWarehouse;
    @BindView(R.id.tv_qty) TextView tvQty;

    // 配送方式
    @BindView(R.id.tv_distribution_way) TextView tvDistributionWay;
    @BindView(R.id.iv_enter) ImageView ivEnter;
    @BindView(R.id.linear_distribution) LinearLayout linearDistribution;

    // 运费单价
    @BindView(R.id.tv_freight) TextView tvFreight;
    @BindView(R.id.linear_freight) LinearLayout linearFreight;
    @BindView(R.id.grey_line) View greyLine;

    // 交货日期
    @BindView(R.id.tv_date_delivery) TextView tvDateDelivery;
    @BindView(R.id.linear_date_delivery) LinearLayout linearDateDelivery;

    @BindView(R.id.linear_payment_title) LinearLayout linearPaymentTitle;

    // 结算方式
    @BindView(R.id.tv_payment_method) TextView tvPaymentMethod;
    @BindView(R.id.iv_enter_payment) ImageView ivPaymentEnter;
    @BindView(R.id.linear_payment) LinearLayout linearPayment;

    // 授信额度 -（结算方式 ：部分授信）
    @BindView(R.id.tv_credit_line) TextView tvCreditLine;
    @BindView(R.id.linear_credit_line) LinearLayout linearCreditLine;
    @BindView(R.id.grey_line_credit_line) View creditGreyLine;

    // 账期
    @BindView(R.id.tv_payment_day) TextView tvPaymentDay;
    @BindView(R.id.linear_payment_day) LinearLayout linearPaymentDay;

    // 资金服务费利率 - 全部授信/部分授信时展示
    @BindView(R.id.tv_service_rate) TextView tvRate;
    @BindView(R.id.linear_service_rate) LinearLayout linearRate;

    // 资金服务费
    @BindView(R.id.tv_fund_fee) TextView tvFundFee;
    @BindView(R.id.linear_fund_fee) LinearLayout linearFundFee;
    @BindView(R.id.grey_line_fund) View fundGreyLine;

    // 销售单价
    @BindView(R.id.tv_sell_price) TextView tvSellPrice;
    @BindView(R.id.linear_sell_price) LinearLayout linearSellPrice;
    @BindView(R.id.grey_sell_price) View sellPriceGreyLine;

    // 使用授信
    @BindView(R.id.tv_use_credit) TextView tvUseCredit;
    @BindView(R.id.iv_enter2) ImageView ivEnter2;
    @BindView(R.id.linear_use_credit) LinearLayout linearUseCredit;
    @BindView(R.id.grey_line_use_credit) View useCreditLine;

    // 手续费
    @BindView(R.id.tv_charge) TextView tvCharge;
    @BindView(R.id.iv_enter3) ImageView ivEnter3;
    @BindView(R.id.linear_charge) LinearLayout linearCharge;

    // @BindView(R.id.tv_bottom_text) TextView tvBottomText;
    @BindView(R.id.tv_bottom) TextView tvBottomValue;          // 合计金额
    @BindView(R.id.tv_bottom_bt) TextView tvBottomBt;          // 按钮
    @BindView(R.id.linear_bottom) LinearLayout linearBottom;

    private Drawable disableDrawable;  // 按钮不可用
    private Drawable enableDrawable;   // 按钮可用

    String TAG = "OrderConfirmActivity";

    private String receiverStr, addressStr;
    private String businessName;     // 商家

    private String currency;         // 货币符号
    private String specBrand;        // 牌号，厂家
    private String warehouseName;    // 仓库

    // 选择配送方式 : 平台配送/自提
    private String distributionPlatformStr;
    private String distributionSelfStr;

    private String creditAll;            // 全部授信
    private String creditPart;           // 部分授信
    private String payFirstStr;          // 款到发货
    private String payOnDeliveryDayStr;  // 货到当天付款

    @Inject OrderConfirmContract.Presenter mPresenter;

    static final String QUOTE_ID = "quote_id";




    /**
     *
     * @param context
     * @param id     报价id
     */
    public static void startOrderConfirmActivity(Context context, String id) {
        Intent intent = new Intent(context, OrderConfirmActivity.class);
        intent.putExtra(QUOTE_ID, id);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public void initView() {
        initToolbar();
        initStr();
        if (getIntent() != null){
            String quoteId = getIntent().getStringExtra(QUOTE_ID);
            mPresenter.setCurrentId(quoteId);
        }
    }

    private void initStr() {
        disableDrawable = getResources().getDrawable(R.drawable.bt_disable_bg);
        enableDrawable = getResources().getDrawable(R.drawable.gradient_orange_background);

        receiverStr = getResources().getString(R.string.order_receiver);
        addressStr = getResources().getString(R.string.order_address);
        currency = getResources().getString(R.string.currency_sign);   // 货币符号
        specBrand = getResources().getString(R.string.spec_brand);     // 牌号，厂家

        businessName = getResources().getString(R.string.order_business);   // 商家
        warehouseName = getResources().getString(R.string.order_warehouse); // 仓库


        distributionPlatformStr = getResources().getString(R.string.distribution_platform);
        distributionSelfStr = getResources().getString(R.string.distribution_self);

        creditAll = getResources().getString(R.string.credit_all);                     // 全部授信
        creditPart = getResources().getString(R.string.credit_part);                   // 部分授信
        payFirstStr = getResources().getString(R.string.pay_first);                    // 款到发货
        payOnDeliveryDayStr = getResources().getString(R.string.pay_on_delivery_day);  // 货到当天付款
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getOrderDetailData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 结算方式 0-全额授信 1-部分授信 2-款到发货(default) 3-货到当天付款
        if (mPresenter.getCurrentPayMethod() == 1){
            double creditLine = Constant.creditLine;
            mPresenter.setCurrentCreditLine(creditLine);  // creditAmount
            String str = new DecimalFormat("0.00").format(creditLine);
            tvCreditLine.setText(currency + ""+str);
        }
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        rightBt.setImageDrawable(this.getResources().getDrawable(R.drawable.user_notes));
        rightBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.order_confirm_title));
    }

    // 订单须知
    @OnClick(R.id.fl_title_menu_right)
    public void onRightClicked(){
        UserNotesActivity.startUserNotesActivity(this,1 );
    }

    // 供配 -隐藏选择按钮
    @Override
    public void initDistributionView() {
        LogUtils.printCloseableInfo(TAG, "initDistributionView  ============= 供配");
        linearFreight.setVisibility(View.GONE);      // 运费单价
        greyLine.setVisibility(View.GONE);

        tvDistributionWay.setText(getResources().getString(R.string.distribution_supplier));       // 供方配送
        ivEnter.setVisibility(View.GONE);

        linearCreditLine.setVisibility(View.GONE);   // HIDE 授信额度
        creditGreyLine.setVisibility(View.GONE);

        tvPaymentMethod.setText(payFirstStr);


        linearPaymentDay.setVisibility(View.GONE);    // 账期

        linearFundFee.setVisibility(View.GONE);       // 资金服务费
        fundGreyLine.setVisibility(View.GONE);


        linearUseCredit.setVisibility(View.GONE);     // 使用授信
        useCreditLine.setVisibility(View.GONE);       // 使用授信
    }

    // 非供配
    @Override
    public void initNoDistributionView() {
        linearFreight.setVisibility(View.VISIBLE);
        greyLine.setVisibility(View.VISIBLE);

        tvDistributionWay.setText(getResources().getString(R.string.distribution_platform));      // 平台配送 >
        ivEnter.setVisibility(View.VISIBLE);

        linearCreditLine.setVisibility(View.GONE);   // HIDE 授信额度
        creditGreyLine.setVisibility(View.GONE);

        tvPaymentMethod.setText(payFirstStr);

        linearPaymentDay.setVisibility(View.GONE);    // 账期

        linearFundFee.setVisibility(View.GONE);       // 资金服务费
        fundGreyLine.setVisibility(View.GONE);

        linearUseCredit.setVisibility(View.GONE);     // 使用授信
        useCreditLine.setVisibility(View.GONE);     // 使用授信
    }

    // 自提: 配送方式 - 非供配 （自提/平台配送）
    // 自提 结算方式只能：款到发货(hide enter image)； 订单小计 ：只有手续费; 结算方式 隐藏 授信额度 和 账期
    @Override
    public void initTakeTheirView() {
        LogUtils.printCloseableInfo(TAG, " ===== initTakeTheirView ======== 配送方式 - 非供配 - 自提");
        linearFreight.setVisibility(View.GONE);          // 运费单价
        greyLine.setVisibility(View.GONE);

        tvDistributionWay.setText(distributionSelfStr);  // 自提

        linearCreditLine.setVisibility(View.GONE);   // HIDE 授信额度
        creditGreyLine.setVisibility(View.GONE);

        linearPaymentDay.setVisibility(View.GONE);   // HIDE 账期

        // 结算方式: 款到发货 (不可选择)
        tvPaymentMethod.setText(payFirstStr);
        ivPaymentEnter.setVisibility(View.GONE);
        linearPayment.setClickable(false);

        linearFundFee.setVisibility(View.GONE);     // 资金服务费
        fundGreyLine.setVisibility(View.GONE);

        linearUseCredit.setVisibility(View.GONE);    // 使用授信
        useCreditLine.setVisibility(View.GONE);      // 使用授信
    }

    // 平台配送(只有平台配送显示 运费单价)
    @Override
    public void initPlatformView() {
        LogUtils.printCloseableInfo(TAG, " ===== initPlatformView ======== 配送方式 - 非供配 - 平台配送");
        linearFreight.setVisibility(View.VISIBLE);     // 运费单价
        greyLine.setVisibility(View.VISIBLE);

        // 结算方式: 款到发货 (可选择)
        tvPaymentMethod.setText(payFirstStr);
        ivPaymentEnter.setVisibility(View.VISIBLE);
        linearPayment.setClickable(true);

        tvDistributionWay.setText(distributionPlatformStr);
    }

    // 结算方式 - 全部授信
    @Override
    public void initCreditAllView() {
        LogUtils.printCloseableInfo(TAG, "initCreditAllView: 结算方式 - 全部授信 ");
        linearCreditLine.setVisibility(View.GONE);    // HIDE 授信额度
        creditGreyLine.setVisibility(View.GONE);

        tvPaymentMethod.setText(creditAll);           // 结算方式 - 全部授信

        linearPaymentDay.setVisibility(View.VISIBLE); // 账期

        linearRate.setVisibility(View.VISIBLE);       // 展示 资金服务费利率

//        if (mPresenter.getIsSupplierDistribution()){   // 供配 - 平台配送
//            linearFreight.setVisibility(View.GONE);    // 运费单价
//        }else {
//            linearFreight.setVisibility(View.VISIBLE);    // 运费单价
//        }

        linearFundFee.setVisibility(View.VISIBLE);    // 资金服务费
        fundGreyLine.setVisibility(View.VISIBLE);

        linearUseCredit.setVisibility(View.VISIBLE);  // 使用授信
        useCreditLine.setVisibility(View.VISIBLE);    // 使用授信
    }

    // 结算方式 - 部分授信 - 授信额度
    @Override
    public void initCreditView() {
        LogUtils.printCloseableInfo(TAG, "initCreditAllView: 结算方式 - 部分授信 - 授信额度 ");
        linearCreditLine.setVisibility(View.VISIBLE); // 显示 授信额度
        creditGreyLine.setVisibility(View.VISIBLE);

        tvPaymentMethod.setText(creditPart);

        linearPaymentDay.setVisibility(View.VISIBLE); // 账期

        linearRate.setVisibility(View.VISIBLE);       // 展示 资金服务费利率

        // linearFreight.setVisibility(View.VISIBLE);   // 运费单价

        linearFundFee.setVisibility(View.VISIBLE);      // 资金服务费
        fundGreyLine.setVisibility(View.VISIBLE);

        linearUseCredit.setVisibility(View.VISIBLE);    // 使用授信
        useCreditLine.setVisibility(View.VISIBLE);      // 使用授信
    }

    // 结算方式 - 款到发货- 隐藏 账期、资金服务费、使用授信
    @Override
    public void initPayFirstView() {
        LogUtils.printCloseableInfo(TAG, "initPayFirstView ================= 结算方式 款到发货- 隐藏 账期、资金服务费、使用授信");
        linearCreditLine.setVisibility(View.GONE);   // HIDE 授信额度
        creditGreyLine.setVisibility(View.GONE);

        tvPaymentMethod.setText(payFirstStr);

        // linearFreight.setVisibility(View.VISIBLE);    // 运费单价

        linearPaymentDay.setVisibility(View.GONE);    // 账期
        linearRate.setVisibility(View.GONE);          // 资金服务费利率

        linearFundFee.setVisibility(View.GONE);       // 资金服务费
        fundGreyLine.setVisibility(View.GONE);

        linearUseCredit.setVisibility(View.GONE);     // 使用授信
        useCreditLine.setVisibility(View.GONE);     // 使用授信

    }

    // 结算方式 - 货到当天付款 账期一天
    @Override
    public void initPayOnDeliveryDayView() {
        LogUtils.printCloseableInfo(TAG, "initPayOnDeliveryDayView: 结算方式 - 货到当天付款 账期一天 ");
        linearCreditLine.setVisibility(View.GONE);   // HIDE 授信额度
        creditGreyLine.setVisibility(View.GONE);

        tvPaymentMethod.setText(payOnDeliveryDayStr);

        // linearFreight.setVisibility(View.VISIBLE); // 运费单价
        linearRate.setVisibility(View.GONE);          // 资金服务费利率
        linearFundFee.setVisibility(View.GONE);    // 资金服务费
        fundGreyLine.setVisibility(View.GONE);

        linearUseCredit.setVisibility(View.GONE);  // 使用授信
        useCreditLine.setVisibility(View.GONE);    // 使用授信
    }

    // 交货日期
    @Override
    public void updateDeliveryDate(String date) {
        tvDateDelivery.setText(date);
    }

    // 账期
    @Override
    public void updatePaymentDay(int number) {
        tvPaymentDay.setText(""+number);
    }

    // 资金服务费利率
    @Override
    public void updateServiceRate(double number) {
        tvRate.setText(""+ number + "%");
    }

    // 资金服务费
    @Override
    public void updateServiceFee(double number) {
        if (number <= 0){
            tvFundFee.setText(currency + "0.00");
        }else {
            // number = (double) Math.round(number * 100) / 100;
            String str = new DecimalFormat("0.00").format(number);
            tvFundFee.setText(currency + str);
        }
    }

    @Override
    public void updateSellPrice(double number) {
        if (number <= 0){
            tvSellPrice.setText(currency + "0.00");
        }else {
            String str = new DecimalFormat("0.00").format(number);
            //number = (double) Math.round(number * 100) / 100;
            tvSellPrice.setText(currency + str);
        }
    }

    // 使用授信
    @Override
    public void updateCreditUsed(double number) {
        LogUtils.printCloseableInfo(TAG, "=========== 使用授信: " +number);
        if (number <= 0){
            tvCreditLine.setText(currency + "0.00");
            tvUseCredit.setText(currency + "0.00");

        }else {
            // number = (double) Math.round(number * 100) / 100;
            String str = new DecimalFormat("0.00").format(number);
            tvCreditLine.setText(currency + str);   // 授信额度
            tvUseCredit.setText(currency + str);    // 使用授信
        }
    }

    // 手续费
    @Override
    public void updateCharge(double number) {
        if (number <= 0){
            tvCharge.setText(currency + "0.00");
        }else {
            String str = new DecimalFormat("0.00").format(number);
            tvCharge.setText(currency + str);
        }

    }

    // 合计金额
    @Override
    public void updateTotalSum(double number) {
        if (number <= 0){
            tvBottomValue.setText(currency + "0.00");
        }else {
            String str = new DecimalFormat("0.00").format(number);
            tvBottomValue.setText(currency + str);
        }
    }

    @Override
    public void updateView(OrderConfirmData.OrderConfirmEntity data) {
        // 收货人/地址
        String receiverInfo = data.contact + "    "+ data.mobile;
        tvOrderReceiver.setText(String.format(receiverStr, receiverInfo));

        String address = data.province + " "+ data.city + " "+ data.district +" "+data.address;
        tvAddress.setText(String.format(addressStr, address));

        // 商家名称
        tvCompanyName.setText(String.format(businessName, data.sellMemberName));

        // 商品信息
        tvBreed.setText(data.breed);
        tvBrand.setText(String.format(specBrand,data.spec, data.factory));
        tvWarehouse.setText(String.format(warehouseName, data.sellMemberQuoteWarehouse));
        tvPrice.setText(currency + data.price);
        tvQty.setText("x"+data.qty);

        // 配送方式 0-供方配送 1-自提 2-平台配送 set in presenter
        // mPresenter.setIsDistribution(data.distributionWay);

        // 运费单价
        tvFreight.setText(currency + data.logisticsAmt);

        // 交货日期
        tvDateDelivery.setText(data.deliveryDate);

        // 结算方式 0-全额授信 1-部分授信 2-款到发货（default）3-货到当天付款
        tvPaymentMethod.setText(payFirstStr);

        // 账期
        tvPaymentDay.setText(data.accountPeriod);

        // 资金服务费
        tvFundFee.setText(currency+"0.00");

        // 使用授信
        tvUseCredit.setText(data.usedAmount);

        // 手续费
        tvCharge.setText(currency+"0.00");

        // 总计
        tvBottomValue.setText(currency+"0.00");


    }

    @Override
    public void showRemindDialog(String message) {
        // 弹框
        HwAppDialog dialog = new HwAppDialog
                .Builder(this)
                //.setTitle("用户状态异常")
                .setInsideContentView(mPresenter.getDialogContentView(message))
                .setPositiveButton(getResources().getString(R.string.contract_service), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击 联系客服
                        mPresenter.doContract();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击 取消
                    }
                })
                .create();
        dialog.show();
    }


    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeButtonDisable(boolean disable) {
        if (disable){
            tvBottomBt.setBackground(disableDrawable); // 不可用
        }else {
            tvBottomBt.setBackground(enableDrawable);  // 可用
        }
        tvBottomBt.setClickable(!disable);
    }


    // 配送方式 - 如果是非供配 则可选（自提/ 平台配送）
    // 供配 则不可点击选择
    @OnClick(R.id.linear_distribution)
    public void onDistributionBtClicked() {
        if (!mPresenter.getIsSupplierDistribution()){
            LogUtils.printCloseableInfo(TAG, "非供配");
            mPresenter.selectDistribution();
        }else {
            LogUtils.printCloseableInfo(TAG, "供配");
        }

    }

    // 交货日期
    @OnClick(R.id.linear_date_delivery)
    public void onDateBtClicked() {
        // 确认订单 不允许修改 交货日期
        // mPresenter.selectDeliveryDate();
    }

    // 结算方式
    @OnClick(R.id.linear_payment)
    public void onPaymentBtClicked() {
        mPresenter.selectPayMethod();
    }

    // 授信额度
    @OnClick(R.id.linear_credit_line)
    public void onCreditLineClicked() {
        mPresenter.updateCredit();
    }

    // 账期
    @OnClick(R.id.linear_payment_day)
    public void onPaymentDayClicked() {
        mPresenter.selectPaymentDay();
    }

    // 提交订单
    @OnClick(R.id.tv_bottom_bt)
    public void onBtClicked() {

        if (mPresenter.getCurrentPayMethod() == 1){
            // 部分授信校验 “授信额度必须大于0”
            // 结算方式 0-全额授信 1-部分授信 2-款到发货(default) 3-货到当天付款
            if (mPresenter.getCurrentCreditLine() == 0){
                showPromptMessage(getResources().getString(R.string.credit_line_0));
            }else {
                if ( !mPresenter.getIsSubmitClicked()){
                    mPresenter.doSubmit();
                }

            }
        }else {
            if ( !mPresenter.getIsSubmitClicked()){
                mPresenter.doSubmit();
            }
        }

    }


}
