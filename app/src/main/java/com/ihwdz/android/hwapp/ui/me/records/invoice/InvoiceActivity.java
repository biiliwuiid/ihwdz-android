package com.ihwdz.android.hwapp.ui.me.records.invoice;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.model.bean.InvoiceData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class InvoiceActivity extends BaseActivity implements InvoiceContract.View {

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @Inject
    InvoiceContract.Presenter mPresenter;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_tax)
    TextView tvTax;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_account)
    TextView tvBankAccount;
    @BindView(R.id.tv_telephone)
    TextView tvTelephone;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.tv_amount) TextView tvAmount;

    private String currency;                    // 货币符号
    private String invoiceAmountIncludeStr;     // 开票金额 (包含贴现金额 ¥ %2$s )
    private String invoiceAmountStr;            // 开票金额

    String TAG = "InvoiceActivity";

    static final String ORDER_ID = "order_id";

    public static void startInvoiceActivity(Context context, String id) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.putExtra(ORDER_ID, id);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_invoice;
    }

    @Override
    public void initView() {
        if (getIntent() != null){
            String orderId = getIntent().getStringExtra(ORDER_ID);
            if (orderId != null){
                mPresenter.setOrderId(orderId);
            }else {
                showPromptMessage("orderId = null");
            }

        }
        currency = getResources().getString(R.string.currency_sign);   // 货币符号
        invoiceAmountIncludeStr = getResources().getString(R.string.invoice_amount_include);   // 开票金额
        invoiceAmountStr = getResources().getString(R.string.invoice_amount);   // 开票金额
        initToolBar();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getInvoiceData();
    }


    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.invoice_title));
    }


    @Override
    public void updateView(InvoiceData.InvoiceEntity data) {

        tvCompany.setText(data.invoceCompany);
        tvTax.setText(data.taxNumber);
        tvBankName.setText(data.bankName);
        tvBankAccount.setText(data.bankAccount);
        tvTelephone.setText(data.phone);
        tvAddress.setText(data.address);
        double amount = 0, discountAmount = 0;
        if (data.amount != null && data.amount.length() > 0 ){
            amount = Double.valueOf(data.amount);
        }
        if (data.discountAmount != null && data.discountAmount.length() > 0 ){
            discountAmount = Double.valueOf(data.discountAmount);
        }
        amount = amount + discountAmount;
        if (discountAmount > 0){
            tvAmount.setText(String.format(invoiceAmountIncludeStr, amount, discountAmount));
        }else {
            tvAmount.setText(String.format(invoiceAmountStr, amount));
        }


    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(getBaseContext(), string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

    }


    // 提交申请
    @OnClick(R.id.bt)
    public void onApplyBtClicked() {
        if (!mPresenter.getIsSubmitClicked()){
            mPresenter.applyInvoiceData();
        }
    }


}
