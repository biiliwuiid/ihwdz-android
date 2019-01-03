package com.ihwdz.android.hwapp.ui.orders.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrderDetailData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 订单详情 （全部订单 待付款 待发货 .. 交易 & 商家）
 */
public class OrderDetailActivity extends BaseActivity implements OrderDetailContract.View {


    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.tv_company_name) TextView tvCompanyName;
    @BindView(R.id.tv_order_status) TextView tvOrderStatus;

    // 订单明细 -》 recycler view
    @BindView(R.id.tv_breed) TextView tvBreed;
    @BindView(R.id.tv_brand) TextView tvBrand;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.tv_warehouse) TextView tvWarehouse;
    @BindView(R.id.tv_qty) TextView tvQty;
    // 订单明细 -》 recycler view
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView; // 订单明细


    @BindView(R.id.tv_distribution_way) TextView tvDistributionWay; // 配送方式
    @BindView(R.id.linear_distribution) LinearLayout linearDistribution;

    @BindView(R.id.tv_date_delivery) TextView tvDateDelivery;  // 交货日期
    @BindView(R.id.linear_date_delivery) LinearLayout linearDateDelivery;

    @BindView(R.id.tv_payment_method) TextView tvPaymentMethod; // 结算方式
    @BindView(R.id.linear_payment) LinearLayout linearPayment;

    @BindView(R.id.tv_advance_payment) TextView tvAdvancePayment; // 预付款  ???????????????
    @BindView(R.id.linear_advance_payment) LinearLayout linearAdvancePayment;

    @BindView(R.id.tv_payment_day) TextView tvPaymentDay;       // 账期
    @BindView(R.id.linear_payment_day) LinearLayout linearPaymentDay;

    @BindView(R.id.tv_order_id) TextView tvOrderId;             // 交易订单号
    @BindView(R.id.linear_order_id) LinearLayout linearOrderId;

    @BindView(R.id.tv_total_price) TextView tvTotalPrice;       // 商品总价
    @BindView(R.id.linear_total_price) LinearLayout linearTotalPrice;

    @BindView(R.id.tv_use_credit) TextView tvUseCredit;          // 使用授信
    @BindView(R.id.linear_use_credit) LinearLayout linearUseCredit;

    @BindView(R.id.tv_charge) TextView tvCharge;                 // 手续费
    @BindView(R.id.linear_charge) LinearLayout linearCharge;

    @BindView(R.id.tv_amount_total) TextView tvAmountTotal;      // 合计金额
    @BindView(R.id.linear_amount_total) LinearLayout linearAmountTotal;

    @BindView(R.id.tv_order_receiver) TextView tvOrderReceiver;  // 收货人
    @BindView(R.id.tv_address) TextView tvAddress;               // 收货地址

    // bottom linear
    @BindView(R.id.linear_bottom) LinearLayout linearBottom;
    @BindView(R.id.tv_bottom_bt) TextView tvBottomButton;


    private String receiverStr, addressStr;
    private String businessName;    // 商家

    private String currency;     // 货币符号+占位
    private String specBrand;    // 牌号，厂家
    private String warehouseName;    // 仓库

    //配送方式： 0-供方配送 1-自提 2-平台配送(default)
    private String distributionSuppliedStr;
    private String distributionSelfStr;
    private String distributionPlatformStr;

    private String creditAll;            // 全部授信
    private String creditPart;           // 部分授信
    private String payFirstStr;          // 款到发货 (default)
    private String payOnDeliveryDayStr;  // 货到当天付款

    String TAG = "OrderDetailActivity";
    @Inject OrderDetailContract.Presenter mPresenter;

    static final String ORDER_ID = "order_id";
    static final String ORDER_STATUS = "order_status";
    static final String ORDER_OPTION = "order_option";

    public static void startOrderDetailActivity( Context context, String id, String orderStatus, int orderOption) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(ORDER_ID, id);
        intent.putExtra(ORDER_STATUS, orderStatus);
        intent.putExtra(ORDER_OPTION, orderOption);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        Constant.orderOptionDone = false; // 按钮未操作
        initStr();
        initIntentData();
        initToolbar();
        initDetailView(); // show business or deal vip view.
        initRecyclerView();
    }

    private void initIntentData() {
        if (getIntent() != null){
            String orderId = getIntent().getStringExtra(ORDER_ID);
            mPresenter.setCurrentId(orderId);

            //订单状态
//            String orderStatus = getIntent().getStringExtra(ORDER_STATUS);
//            tvOrderStatus.setText(orderStatus);

            // button 状态
            int orderOption = getIntent().getIntExtra(ORDER_OPTION, -1);
            mPresenter.setCurrentMode(orderOption);
        }
    }

    private void initStr() {
        receiverStr = getResources().getString(R.string.order_receiver);
        addressStr = getResources().getString(R.string.order_address);
        currency = getResources().getString(R.string.currency_sign);   // 货币符号
        specBrand = getResources().getString(R.string.spec_brand);     // 牌号，厂家

        businessName = getResources().getString(R.string.order_business);   // 商家
        warehouseName = getResources().getString(R.string.order_warehouse); // 仓库



        distributionSuppliedStr = getResources().getString(R.string.distribution_supplier);
        distributionPlatformStr = getResources().getString(R.string.distribution_platform);
        distributionSelfStr = getResources().getString(R.string.distribution_self);


        creditAll = getResources().getString(R.string.credit_all);     // 全部授信
        creditPart = getResources().getString(R.string.credit_part);    // 部分授信
        payFirstStr = getResources().getString(R.string.pay_first);    // 款到发货
        payOnDeliveryDayStr = getResources().getString(R.string.pay_on_delivery_day);    // 货到当天付款


        int vipType = Constant.VIP_TYPE; // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
        switch (vipType){
            case -1:
                break;
            case 0:
                break;
            case 1: // 交易会员
                LogUtils.printCloseableInfo(TAG, "交易会员");
                mPresenter.setVipMode(0);
                break;
            case 2: // 商家会员
                LogUtils.printCloseableInfo(TAG, "商家会员");
                mPresenter.setVipMode(1);
                break;
            default:
                LogUtils.printCloseableInfo(TAG, "未登录");
                break;
        }


    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getOrderDetailData();
    }

    // 更新按钮状态

    @Override
    protected void onResume() {
        super.onResume();
        // 支付手续费，申请展期，申请开票等操作完成后返回  加载数据 & 隐藏按钮
        if (Constant.orderOptionDone){
            LogUtils.printCloseableInfo(TAG, "==== getIntent().getStringExtra(ORDER_ID) == null");
            mPresenter.getOrderDetailData();
            hideBottomLinear();
        }else {
            LogUtils.printCloseableInfo(TAG, "==== getIntent().getStringExtra(ORDER_ID) != null");
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPresenter.getAdapter());
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.order_detail_title));
    }


    private void initDetailView() {
        int vipType = Constant.VIP_TYPE;
        switch (vipType){
//            case -1:
//                break;
//            case 0:
//                break;
            case 1:
                // 交易会员
                initDealOrderDetailView();
                break;
            case 2:
                // 商家会员
                initBusinessOrderDetailView();
                break;
            default:
                LogUtils.printCloseableInfo(TAG, "不是商家或交易会员");
                break;
        }
    }

    //商家会员 隐藏底部按钮
    @Override
    public void initBusinessOrderDetailView() {
        LogUtils.printCloseableInfo(TAG, "商家会员");
        linearPayment.setVisibility(View.GONE);    //
        linearPaymentDay.setVisibility(View.GONE);

        linearUseCredit.setVisibility(View.GONE);
        linearCharge.setVisibility(View.GONE);
        linearAmountTotal.setVisibility(View.GONE);

        linearBottom.setVisibility(View.GONE);
    }

    //交易会员
    @Override
    public void initDealOrderDetailView() {
        LogUtils.printCloseableInfo(TAG, "交易会员");
    }

    // 款到发货 - hide "账期"&"使用授信"
    @Override
    public void initPayFirstView() {
        linearPaymentDay.setVisibility(View.GONE);
        linearUseCredit.setVisibility(View.GONE);
    }

    @Override
    public void updateView(OrderDetailData.OrderDetailEntity data) {
        // 公司名称
        tvCompanyName.setText(String.format(businessName, data.memberName));

        // 订单状态
        tvOrderStatus.setText(mPresenter.getStatusTag(data.subStatus));

        // 订单明细
        mPresenter.getAdapter().setDataList(data.orderItems, true);

        // 配送方式
        tvDistributionWay.setText(getDistributionWayStr(data.distributionWay));

        // 交货日期
        tvDateDelivery.setText(data.deliveryDate);

        // 结算方式  (款到发货 - hide "账期"&"使用授信")
        tvPaymentMethod.setText(getPaymentWayStr(data.receivableWay));

        // 账期
        tvPaymentDay.setText(data.receivableDay);

        // 交易订单号
        tvOrderId.setText(data.orderSn);

        // 商品总价 - 销售总货款 （商家会员 - purchaseSumAmt   交易会员 - saleSumAmt ）
        double saleSumAmtD, purchaseSumAmtD;
        if (data.saleSumAmt == null){
            saleSumAmtD = 0d;
        }else {
            saleSumAmtD = Double.valueOf(data.saleSumAmt);
        }
        if (data.purchaseSumAmt == null){
            purchaseSumAmtD = 0d;
        }else {
            purchaseSumAmtD = Double.valueOf(data.purchaseSumAmt);
        }
        int vipType =  Constant.VIP_TYPE;  // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
        if (vipType == 1){
            tvTotalPrice.setText(currency + saleSumAmtD);
        }else if (vipType == 2){
            tvTotalPrice.setText(currency + purchaseSumAmtD);
        }else {
            // 非商家/交易 会员
        }


        // 使用授信
        tvUseCredit.setText(currency + data.creditAmount);

        // 手续费
        double chargeD;
        if (data.serviceCharge == null){
            chargeD = 0d;
        }else {
            chargeD = Double.valueOf(data.serviceCharge);
        }
        tvCharge.setText(currency + chargeD);


        // 合计金额 = 商品总价+手续费
        double totalD = saleSumAmtD + chargeD;
        tvAmountTotal.setText(currency + totalD);

        // 收货人/地址
        String receiverInfo = data.address.contact + "    "+ data.address.mobile;
        tvOrderReceiver.setText(String.format(receiverStr, receiverInfo));

        String address = data.address.province + " "+ data.address.city + " "+ data.address.district +" "+data.address.address;
        tvAddress.setText(String.format(addressStr, address));

    }

    //配送方式： 0-供方配送 1-自提 2-平台配送
    private String getDistributionWayStr(Integer distributionWay) {
        switch (distributionWay){
            case 0:
                return distributionSuppliedStr;
            case 1:
                return distributionSelfStr;
            case 2:
                return distributionPlatformStr;
            default:
                return distributionPlatformStr;
        }
    }

    // 结算方式： 0-全额授信 1-部分授信 2-款到发货（default）3-货到当天付款
    private String getPaymentWayStr(Integer distributionWay) {
        switch (distributionWay){
            case 0:
                return creditAll;
            case 1:
                return creditPart;
            case 2:
                initPayFirstView(); // 款到发货 - hide "账期"&"使用授信"
                return payFirstStr;
            case 3:
                return payOnDeliveryDayStr;
            default:
                initPayFirstView(); // 款到发货 - hide "账期"&"使用授信"
                return payFirstStr;
        }
    }

    @Override
    public void showBottomLinear(String buttonName) {
        linearBottom.setVisibility(View.VISIBLE);
        tvBottomButton.setText(buttonName);
    }

    @Override
    public void hideBottomLinear() {
        linearBottom.setVisibility(View.GONE);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.tv_bottom_bt)
    public void onBtClicked() {
        mPresenter.buttonClicked();
    }


}
