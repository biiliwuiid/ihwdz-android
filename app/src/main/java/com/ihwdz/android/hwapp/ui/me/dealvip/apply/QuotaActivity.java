package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  交易会员 - 查看额度
 */
public class QuotaActivity extends BaseActivity {

    String TAG = "QuotaActivity";

    String titleStr = "";
    String currency;
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;


    // apply complete
    @BindView(R.id.linear1) LinearLayout completeApplyLayout;        // 审核完成 - 展示： 信用额度 已用额度 可用额度
    @BindView(R.id.tv_credit_limit) TextView tvCreditLimit;          // 信用额度
    @BindView(R.id.tv_limit_used) TextView tvLimitUsed;              // 已用额度
    @BindView(R.id.tv_limit_available) TextView tvLimitAvailable;    // 可用额度


    @Override
    public int getContentView() {
        return R.layout.activity_quota;
    }

    @Override
    public void initView() {
        currency = getResources().getString(R.string.currency_sign);
       initToolbar();
       setQuota();
    }

    private void setQuota() {
        tvCreditLimit.setText(currency + Constant.totalAmount);
        tvLimitUsed.setText(currency +Constant.usedAmount);
        tvLimitAvailable.setText(currency +Constant.availableAmount);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    void initToolbar(){
        titleStr = getResources().getString(R.string.title_quota);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }




}
