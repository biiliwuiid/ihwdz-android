package com.ihwdz.android.hwapp.ui.orders.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  支付页面  订单 -点击 支付手续费-> come here
 */
public class PaymentActivity extends BaseActivity implements PaymentContract.View{

    String TAG = "PaymentActivity";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;


    @BindView(R.id.bt1) RadioButton radioAli;     // 支付宝
    @BindView(R.id.bt2) RadioButton radioWechat;  // 微信
    @BindView(R.id.bt) Button btPay;              // 立即支付

    @Inject PaymentContract.Presenter mPresenter;

    static final String ORDER_ID = "order_id";
    static final String ORDER_SN = "order_sn";
    static final String GOODS_AMOUNT = "goods_amount";

    String payStr;
    //private PaymentReceiver receiver;
    private IntentFilter intentFilter;

    public static void startPaymentActivity(Context context, String orderId, String orderSn, String goodsAmount) {
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(ORDER_SN, orderSn);
        intent.putExtra(GOODS_AMOUNT, goodsAmount);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        payStr = getResources().getString(R.string.pay_now);
        initToolbar();

        if (getIntent() != null){
            String orderId = getIntent().getStringExtra(ORDER_ID);
            mPresenter.setOrderId(orderId);

            String orderSn = getIntent().getStringExtra(ORDER_SN);
            mPresenter.setOrderSn(orderSn);

            String charge = getIntent().getStringExtra(GOODS_AMOUNT);
            // LogUtils.printCloseableInfo(TAG, "String charge: " + charge);
            double charged = Double.valueOf(charge);
            // LogUtils.printCloseableInfo(TAG, "double charged: " + charged);
            charge = String.format("%.2f", charged);
            // LogUtils.printCloseableInfo(TAG, "charge: " + charge);

            btPay.setText(String.format(payStr, charge));
            mPresenter.setGoodsAmount(charge);
        }
        radioAli.setChecked(true);     // 默认选择支付宝

        // 注册广播
        // receiver = new PaymentReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.PAYMENT_RESULT");
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(receiver,intentFilter);
    }

    String ACTION_NAME = "android.PAYMENT_RESULT";
    String PAY_MODE = "PAY_MODE";
    String PAY_RESULT = "PAY_RESULT";
    private BroadcastReceiver receiver = new PaymentReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_NAME)){
                LogUtils.printError(TAG, "onReceive  处理action名字相对应的广播: " + action );
                int payMode = intent.getIntExtra(PAY_MODE, 0);
                int errorCode = intent.getIntExtra(PAY_RESULT, 0);
                LogUtils.printError(TAG, "onReceive  payMode: " + payMode );
                LogUtils.printError(TAG, "onReceive   errorCode: " + errorCode );
                // 升级权益
                if (payMode == Constant.PayMode.PAY_CHARGE){
                    if (errorCode == 0){  // 支付成功返回上级
                        onBackClicked();
                        // showPromptMessage("onReceive  支付手续费 支付成功");
                        // mPresenter.backToClientActivity();
                    }else {
                        mPresenter.setIsWeChatPaySubmitClicked(false);
                    }
                    LogUtils.printError(TAG, "onReceive  支付手续费: " );
                }
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        showPromptMessage("onReceive  返回上一级");
        onBackPressed();
    }
    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.payment_title));
    }


    // 点击 立即支付
    @OnClick(R.id.bt)
    public void onPayBtClicked(){
        if (radioAli.isChecked()){
            // 支付宝支付
            if (!mPresenter.getIsAliPaySubmitClicked()){
                mPresenter.postAliPayData();
            }
        }else if (radioWechat.isChecked()){
            // 微信支付
            if (!mPresenter.getIsWeChatPaySubmitClicked() || Constant.weChatPayFailed){
                mPresenter.postWeChatPayData();
            }

        }else {
            showPromptMessage(getResources().getString(R.string.no_pay_up));
        }
    }


    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
