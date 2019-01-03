package com.ihwdz.android.hwapp.ui.purchase.purchasedetail;

import android.content.DialogInterface;
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
import com.ihwdz.android.hwapp.model.bean.PurchaseQuoteData;
import com.ihwdz.android.hwapp.widget.HwAppDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *   求购报价： 我的求购 -item-> 求购报价（交易会员）
 */
public class PurchaseDetailActivity extends BaseActivity implements PurchaseDetailContract.View{

    String TAG = "PurchaseDetailActivity";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.tv_order_status) TextView tvOrderStatus; // 订单状态
    @BindView(R.id.tv_breed) TextView tvBreed;              // breed spec factory
    @BindView(R.id.tv_amount) TextView tvAmount;            // 采购量
    @BindView(R.id.tv_address) TextView tvAddress;          // address
    @BindView(R.id.tv_time) TextView tvTime;                //
    @BindView(R.id.tv_address_detail) TextView tvDetailAddress;    // address

    // 报价
    @BindView(R.id.linear_quote) LinearLayout quoteLinear;
    @BindView(R.id.tv_quote_title) TextView tvQuoteTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private String breed, amount, address, quoteTitle;
    private String idStr, statusStr, breedStr, qtyStr, addressStr, timeStr, quoteTitleStr;
    private String reviewStatus;

    @Inject PurchaseDetailContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_purchase_detail;
    }

    @Override
    public void initView() {
        initToolbar();
        initRecyclerView();
    }



    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolbar() {
        if (getIntent() != null) {
            mPresenter.setCurrentId(getIntent().getStringExtra("ID"));
        }
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.purchase_quote_title));

        breed = getResources().getString(R.string.purchase_breed);
        amount = getResources().getString(R.string.purchase_amount);
        address = getResources().getString(R.string.purchase_address);
        quoteTitle = getResources().getString(R.string.quote_count_title);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 交易会员 点击 进行  -> 一键下单
        mPresenter.getAdapter().setOnOrderClickListener(new OnOrderClickListener() {
            @Override
            public void onOrderClicked(String id) {
                mPresenter.setCurrentQuoteId(id);

                // 交易 判断锁定状态 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                if (Constant.VIP_LOCK_STATUS == 1){
                    showRemindDialog(getResources().getString(R.string.deal_error_remind));
                    // showPromptMessage(getResources().getString(R.string.error_deal_vip));
                }else {
                    mPresenter.doOrder();
                }

            }
//            @Override
//            public void onQuoteBtClicked(String id,String status,String breed,String qty, String address) {
//                mPresenter.doOrder(id);
//            }
        });

        // 交易会员 点击 进行 -> 价格复议
        mPresenter.getAdapter().setOnReviewClickListener(new OnReviewClickListener() {
            @Override
            public void onReviewClicked(String id) {
                // 交易 判断锁定状态 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                if (Constant.VIP_LOCK_STATUS == 1){
                    showRemindDialog(getResources().getString(R.string.deal_error_remind));
                    // showPromptMessage(getResources().getString(R.string.error_deal_vip));
                }else {
                    mPresenter.doReview(id);
                }

            }
        });
        recyclerView.setAdapter(mPresenter.getAdapter());
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getUserType();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getMyPurchaseListData();
    }

    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }


    // 求购
    @Override
    public void updatePurchaseView(PurchaseQuoteData.PurchaseEntity data) {
//        reviewStatus = getResources().getString(R.string.reconsideration); // 待复议
//        statusStr = getIntent().getStringExtra("STATUS");
//        breedStr = getIntent().getStringExtra("BREED");
//        qtyStr = getIntent().getStringExtra("QTY");
//        addressStr = getIntent().getStringExtra("ADDRESS");
//        timeStr = getIntent().getStringExtra("TIME");

        breedStr = String.format(breed, data.breed, data.spec, data.factory);
        qtyStr = String.format(amount, data.qty);
        addressStr = String.format(address, data.address.province, data.address.city, data.address.district,"");
        timeStr = data.dateTimeStr;

        //tvOrderStatus.setText(statusStr);
        tvBreed.setText(breedStr);
        tvAmount.setText(qtyStr);
        tvAddress.setText(addressStr);
        tvDetailAddress.setText(data.address.address);
        tvTime.setText(timeStr);
    }

    // 报价栏
    @Override
    public void updateQuoteView(int quoteCount) {
//        if (quoteCount > 0){
//            quoteTitleStr = String.format(quoteTitle, ""+quoteCount);
//            quoteLinear.setVisibility(View.VISIBLE);
//            tvQuoteTitle.setText(quoteTitleStr);  // 报价（ quoteCount ）
//        }else {
//            quoteLinear.setVisibility(View.GONE);
//        }

        quoteTitleStr = String.format(quoteTitle, ""+quoteCount);
        quoteLinear.setVisibility(View.VISIBLE);
        tvQuoteTitle.setText(quoteTitleStr);  // 报价（ quoteCount ）
    }

    @Override
    public void showRemindDialog(String message) {
        // 弹框选择Breeds
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
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }

}
