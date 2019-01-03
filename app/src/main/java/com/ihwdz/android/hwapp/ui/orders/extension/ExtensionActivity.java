package com.ihwdz.android.hwapp.ui.orders.extension;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ExtensionData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请展期  订单 -点击 申请展期-> come here
 */
public class ExtensionActivity extends BaseActivity implements ExtensionContract.View {


    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.tv_order_id) TextView tvOrderId;                // 订单号
    @BindView(R.id.tv_credit_line) TextView tvCreditLine;          // 授信额度
    @BindView(R.id.tv_extension_rate) TextView tvExtensionRate;    // 展期利率
    @BindView(R.id.tv_payment_day) TextView tvPaymentDay;          // 账期
    @BindView(R.id.tv_extension_amount) TextView tvExtensionAmount;// 展期金额
    @BindView(R.id.tv_remarks) TextView tvRemarks;                 // 备注 展示备注信息 ( 展示字数 < 10)

    String TAG = "ExtensionActivity";
    @Inject ExtensionContract.Presenter mPresenter;

    private String currency;    // 货币符号
    static final String ORDER_ID = "order_id";

    public static void startExtensionActivity(Context context, String id) {
        Intent intent = new Intent(context, ExtensionActivity.class);
        intent.putExtra(ORDER_ID, id);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_extension;
    }

    @Override
    public void initView() {
        initToolbar();
        currency = getResources().getString(R.string.currency_sign);    // 货币符号
        if (getIntent() != null){
            String orderId = getIntent().getStringExtra(ORDER_ID);
            mPresenter.setCurrentId(orderId);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getExtensionData();

    }

    // 收集更改完备注后的 数据
    @Override
    protected void onStart() {
        super.onStart();
        String remarks = Constant.extensionRemarks;
        if (remarks != null){
            LogUtils.printCloseableInfo(TAG, "extensionRemarks: " + remarks);
            tvRemarks.setText(remarks);  // 限制10个文字
            mPresenter.setCurrentRemarks(remarks);
        }
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.extension_apply_title));
    }


    @Override
    public void updateView(ExtensionData.ExtensionEntity data) {

        // 授信额度
        double creditAmount = 0.00;
        if (data.creditAmount != null && data.creditAmount.length() > 0){
            creditAmount = Double.valueOf(data.creditAmount);
            creditAmount = (double) Math.round(creditAmount * 100) / 100;
        }

        // 展期利率
        double extensionRate = 0.00;
        if (data.extendsDaysRate != null && data.extendsDaysRate.length() > 0){
            extensionRate = Double.valueOf(data.extendsDaysRate);
        }

        // 账期
        int paymentDayMax = mPresenter.getMaxPaymentDay();


        // 初始化 展期金额
        double extensionAmount = mPresenter.getExtensionAmount();

        tvOrderId.setText(data.contractCode);
        tvCreditLine.setText(currency + ""+ creditAmount);
        tvExtensionRate.setText(""+ extensionRate +"%");
        tvPaymentDay.setText(""+paymentDayMax);
        tvExtensionAmount.setText(""+ extensionAmount);
        tvRemarks.setText(data.note);
    }

    // 更新账期 和 展期金额
    @Override
    public void updatePaymentDay(int paymentDay) {
        tvPaymentDay.setText(""+ paymentDay);
        double extensionAmount = mPresenter.getExtensionAmount();
        tvExtensionAmount.setText(""+ extensionAmount);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.linear_payment_day, R.id.linear_remark, R.id.bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_payment_day:
                // 点击选择账期
                mPresenter.doSelectPaymentDay();
                break;
            case R.id.linear_remark:
                // 点击编辑备注
                mPresenter.doRemarks();
                break;
            case R.id.bt:
                // 点击提交申请
                if (!mPresenter.getIsSubmitClicked()){
                    mPresenter.doSubmit();
                }

                break;
        }
    }
}
