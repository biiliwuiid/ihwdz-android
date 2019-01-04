package com.ihwdz.android.hwapp.ui.purchase.quotedetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.QuoteDetailData;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 报价详情 & 报价操作
 */
public class QuoteDetailActivity extends BaseActivity implements QuoteDetailContract.View {

    String TAG = "QuoteDetailActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.tv_order_status) TextView tvOrderStatus; // 订单状态 for quote detail
    @BindView(R.id.tv_breed) TextView tvBreed;        // breed spec factory
    @BindView(R.id.tv_amount) TextView tvAmount;      // 采购量
    @BindView(R.id.tv_address) TextView tvAddress;    // address
    @BindView(R.id.tv_time) TextView tvTime;          // no data

    @BindView(R.id.linear_distribution) LinearLayout linearDistribution;       // 供方配送 linear
    @BindView(R.id.tv_supplier_distribution) TextView tvSupplierDistribution;  // for quote detail
    @BindView(R.id.checkbox) CheckBox checkbox;   // for quote

    @BindView(R.id.linear_warehouse_choose) LinearLayout linearWarehouseChoose; // 选择仓库 linear
    @BindView(R.id.tv_warehouse_choose) TextView tvWarehouseChoose;
    @BindView(R.id.iv_enter) ImageView ivEnter;

    @BindView(R.id.linear_warehouse_address) LinearLayout linearWarehouseAddress;// 仓库地址 linear
    @BindView(R.id.tv_warehouse) TextView tvWarehouseAddress;
    @BindView(R.id.grey_line_address) View greyLineWarehouse;
    //@BindView(R.id.iv_enter2) ImageView ivEnter2;

    @BindView(R.id.linear_date_delivery) LinearLayout linearDateDelivery;// 交货日期 linear
    @BindView(R.id.tv_date_delivery) TextView tvDateDelivery;            // 交货日期
    @BindView(R.id.iv_enter3) ImageView ivEnter3;                        // 交货日期 enter

    @BindView(R.id.linear_price) LinearLayout linearPrice;  // 单价 linear
    @BindView(R.id.tv_price) TextView tvPrice;              // 单价
    @BindView(R.id.iv_enter4) ImageView ivEnter4;           // 单价 enter
    @BindView(R.id.grey_line_quote) View boundary;          //
    @BindView(R.id.grey_line_review)View greyLine;          //

    @BindView(R.id.linear_review) LinearLayout linearReview;  // 报价复议 title
    @BindView(R.id.linear_remark) LinearLayout linearRemark;  // 报价备注 linear
    @BindView(R.id.tv_remarks_note) TextView tvRemarksNote;   // 报价备注 note

    @BindView(R.id.linear_bottom) LinearLayout linearBottom;
    @BindView(R.id.tv_bottom_text) TextView tvBottomText;    // 报价 - 金额合计
    @BindView(R.id.tv_bottom) TextView tvBottomValue;        // 报价 - 金额合计 value
    @BindView(R.id.tv_bottom_bt) TextView tvBottomBt;        // 按钮

    private boolean isDetail = false;     // 是否是 “报价详情”（否则为 确认报价）

    private String currency;     // 货币符号
    private String breed, amount, address;
    private String idStr, statusStr, breedStr, qtyStr, addressStr, timeStr;
    private String reviewStatus;
    private String totalAmount;
    double price = 0, qty = 0;   // 单价 & 采购量

    private Drawable disableDrawable;  // 按钮不可用
    private Drawable enableDrawable;   // 按钮可用

    @Inject QuoteDetailContract.Presenter mPresenter;
    static final String IS_DETAIL = "is_detail";   // true:报价详情  false:报价动作
    static final String ID = "id";
    static final String STATUS = "status";
    static final String BREED = "breed";
    static final String QTY = "qty";
    static final String ADDRESS = "address";
    static final String TIME = "time";

    // from purchase pool
    public static void startQuoteDetailActivity(Context context, boolean isDetail, String id, String status, String breed, String qty, String address, String time) {
        Intent intent = new Intent(context, QuoteDetailActivity.class);
        intent.putExtra(IS_DETAIL, isDetail);
        intent.putExtra(ID, id);
        intent.putExtra(STATUS, status);
        intent.putExtra(BREED, breed);
        intent.putExtra(QTY, qty);
        intent.putExtra(ADDRESS, address);
        intent.putExtra(TIME, time);

        if (!isDetail){  // 报价时 仓库数据，报价 置空
            Constant.provinceName = null;
            Constant.cityName = null;
            Constant.districtName = null;
            Constant.warehouseAddress = null;
            Constant.warehouse = null;

            Constant.quotePrice = null;
        }

        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_quote_detail;
    }

    @Override
    public void initView() {
        initToolbar();

        disableDrawable = getResources().getDrawable(R.drawable.bt_disable_bg);
        enableDrawable = getResources().getDrawable(R.drawable.gradient_orange_background);

        currency = getResources().getString(R.string.currency_sign);   // 货币符号
        breed = getResources().getString(R.string.purchase_breed);
        amount = getResources().getString(R.string.purchase_amount);
        address = getResources().getString(R.string.purchase_address);
        totalAmount = getResources().getString(R.string.total_sum);
        reviewStatus = getResources().getString(R.string.reconsideration); // 待复议
        setPurchaseInfo();
    }


    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        // 报价详情 加载报价数据  但 确认报价 不加载数据
        if (isDetail){
            mPresenter.getMyQuoteData();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.printCloseableInfo(TAG, "=================== onStart ======================= ");
        updateWarehouse();    // 更新 仓库数据
        updateTotalAmount();  // 更新 总价
    }

    // 更新 仓库数据
    private void updateWarehouse(){
        // 选择仓库数据
        if (Constant.warehouse != null ){
            String address = Constant.warehouse;
            LogUtils.printCloseableInfo(TAG, "address: " + address);
            tvWarehouseChoose.setText(address);
        }else {
            if (checkbox.isChecked()){
                tvWarehouseChoose.setText(getResources().getString(R.string.supplier_distribution_text));
                linearWarehouseChoose.setClickable(false);
                ivEnter.setVisibility(View.GONE);
                linearWarehouseAddress.setVisibility(View.GONE);
                greyLineWarehouse.setVisibility(View.GONE);
            }
        }
        if (Constant.provinceName != null && Constant.cityName != null && Constant.districtName != null && Constant.warehouseAddress != null){
            String address = Constant.provinceName + ","+ Constant.cityName +","+ Constant.districtName + " "+Constant.warehouseAddress;
            LogUtils.printCloseableInfo(TAG, "address: " + address);
            tvWarehouseAddress.setText(address);
        }else {
            tvWarehouseAddress.setText("");
        }
    }

    // 更新 总价
    private void updateTotalAmount(){
        // 计算总价
        if (Constant.quotePrice != null){
            // 金额保留2位小数
            if (Constant.quotePrice.length() > 0){
                price = Double.valueOf(Constant.quotePrice);
                LogUtils.printCloseableInfo(TAG, "price: " + price);
                price = (double) Math.round(price * 100) / 100;
                LogUtils.printCloseableInfo(TAG, "Constant.quotePrice: " + Constant.quotePrice);
                LogUtils.printCloseableInfo(TAG, "price: " + price);
            }
            tvPrice.setText(currency + price);
            mPresenter.setCurrentPrice(price);

            if (!TextUtils.isEmpty(qtyStr) && qtyStr.length() > 0){
                qty = Double.valueOf(qtyStr);
                LogUtils.printCloseableInfo(TAG, "qty: " + qty);
            }else {

            }

            double total = price * qty;
            total = (double) Math.round(total * 100) / 100;
            LogUtils.printCloseableInfo(TAG, "total: " + total);
            if (total == 0){
                tvBottomValue.setText(String.format(totalAmount, "0"));
            }else {
                tvBottomValue.setText(String.format(totalAmount, total));
            }
        }else {
            tvBottomValue.setText(currency +"0");
        }
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    public void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
    }

    //报价 - 是否供配 checkbox 0-否 1-是
    @OnCheckedChanged(R.id.checkbox)
    public void onIsSupplierDistributionCheckedChanged(){
        if (checkbox.isChecked()){
            mPresenter.setIsSupplierDistribution(checkbox.isChecked());
            // 商家供配 不需要仓库数据
            Constant.provinceName = null;
            Constant.cityName = null;
            Constant.districtName = null;
            Constant.warehouseAddress = null;
            updateWarehouse();

            tvWarehouseChoose.setText(getResources().getString(R.string.supplier_distribution_text));
            linearWarehouseChoose.setClickable(false);
            ivEnter.setVisibility(View.GONE);
            linearWarehouseAddress.setVisibility(View.GONE);
            greyLineWarehouse.setVisibility(View.GONE);
        }else {
            mPresenter.setIsSupplierDistribution(checkbox.isChecked());
            tvWarehouseChoose.setText("");
            linearWarehouseChoose.setClickable(true);
            ivEnter.setVisibility(View.VISIBLE);
            linearWarehouseAddress.setVisibility(View.VISIBLE);
            greyLineWarehouse.setVisibility(View.VISIBLE);
        }
    }

    // 求购数据 - onCreate (from 我的报价-item / 我的求购-报价按钮)
    void setPurchaseInfo(){
        if (getIntent() != null){
            isDetail = getIntent().getBooleanExtra(IS_DETAIL, false);
            mPresenter.setCurrentMode(isDetail ? 0 : 1); // 0 报价详情；1 报价动作

            if (getIntent().getStringExtra(ID) != null){
                idStr = getIntent().getStringExtra(ID);
                mPresenter.setCurrentId(idStr);
                LogUtils.printCloseableInfo(TAG, "id: " + idStr);
            }

            if (getIntent().getStringExtra(STATUS) != null){
                statusStr = getIntent().getStringExtra(STATUS);
            }

            if ( getIntent().getStringExtra(BREED) != null){
                breedStr = getIntent().getStringExtra(BREED);
            }

            if (getIntent().getStringExtra(QTY)!= null){
                qtyStr = getIntent().getStringExtra(QTY);
            }

            if (getIntent().getStringExtra(ADDRESS) != null){
                addressStr = getIntent().getStringExtra(ADDRESS);
            }
            if (getIntent().getStringExtra(TIME) != null){
                timeStr = getIntent().getStringExtra(TIME);
            }

            if (statusStr != null && statusStr.length() > 0){
                LogUtils.printCloseableInfo(TAG, "statusStr: " + statusStr);
                tvOrderStatus.setText(statusStr);
            }
            if (breedStr != null && breedStr.length() > 0){
                // LogUtils.printCloseableInfo(TAG, "breedStr: " + breedStr);
                tvBreed.setText(breedStr);
            }
            if (qtyStr != null && qtyStr.length() > 0){
                // 采购量
                tvAmount.setText(String.format(amount, qtyStr));
            }
            if (addressStr != null && addressStr.length() > 0){
                tvAddress.setText(addressStr);
            }
            if (timeStr != null && timeStr.length() > 0){
                tvTime.setText(timeStr);
            }
        }

    }


    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    // 报价详情
    @Override
    public void initQuoteDetailView() {
        LogUtils.printCloseableInfo(TAG, "initQuoteDetailView  报价详情 - onCreate");
        title.setText(getResources().getString(R.string.quote_detail_title));
        tvOrderStatus.setVisibility(View.GONE);

        tvSupplierDistribution.setVisibility(View.VISIBLE);
        checkbox.setVisibility(View.GONE);
        ivEnter.setVisibility(View.GONE);
        ivEnter3.setVisibility(View.GONE);
        ivEnter4.setVisibility(View.GONE);

        linearWarehouseChoose.setClickable(false);
        //linearDateDelivery.setClickable(false);
        linearPrice.setClickable(false);

        boundary.setVisibility(View.VISIBLE);
        linearReview.setVisibility(View.VISIBLE);
        greyLine.setVisibility(View.VISIBLE);
        linearRemark.setVisibility(View.VISIBLE);

        // 报价详情 - 待复议 状态 显示底部按钮

        if ( !TextUtils.isEmpty(statusStr) && TextUtils.equals(reviewStatus,statusStr)){
            linearBottom.setVisibility(View.VISIBLE);
            tvBottomBt.setText(R.string.review_quote);
        }else {
            linearBottom.setVisibility(View.GONE);
        }

    }

    // 报价动作 - onCreate
    @Override
    public void initQuoteConfirmView() {
        tvDateDelivery.setText("");   // 交货日期


        LogUtils.printCloseableInfo(TAG, "initQuoteConfirmView  报价动作 - onCreate");
        title.setText(getResources().getString(R.string.quote));
        tvOrderStatus.setVisibility(View.GONE);


        tvBottomValue.setText(String.format(totalAmount,"0"));

        tvSupplierDistribution.setVisibility(View.GONE);
        checkbox.setVisibility(View.VISIBLE);
        ivEnter.setVisibility(View.VISIBLE);
        ivEnter3.setVisibility(View.VISIBLE);
        ivEnter4.setVisibility(View.VISIBLE);

        linearWarehouseChoose.setClickable(true);
        linearDateDelivery.setClickable(true);
        linearPrice.setClickable(true);

        boundary.setVisibility(View.GONE);
        linearReview.setVisibility(View.GONE);
        greyLine.setVisibility(View.GONE);
        linearRemark.setVisibility(View.GONE);

        // 报价按钮
        linearBottom.setVisibility(View.VISIBLE);
        tvBottomText.setVisibility(View.VISIBLE);
        tvBottomValue.setVisibility(View.VISIBLE);
        tvBottomBt.setText(R.string.quote_confirm);
    }

    // 更新报价详情
    @Override
    public void updateView(QuoteDetailData data) {

        tvTime.setText(data.data.dateTimeStr);  // 报价详情 时间从 详情接口获取 而不是 从Intent 获取
        // 报价信息
        // 供方配送 0-否 1-是
        boolean isSupplierDistribution = data.data.isSupplierDistribution  == 1;
        tvSupplierDistribution.setText(getIsSupplierDistribution(data.data.isSupplierDistribution));

        if (isSupplierDistribution){
            // 供配 1.选择仓库 显示“商家包配送” 2.隐藏 仓库地址
            // 选择仓库
            tvWarehouseChoose.setText(getResources().getString(R.string.supplier_distribution_text));
            // 仓库地址
            linearWarehouseAddress.setVisibility(View.GONE);
            greyLineWarehouse.setVisibility(View.GONE);
        }else {
            // 选择仓库
            tvWarehouseChoose.setText(data.data.warehouse.warehouse);
            // 仓库地址
            tvWarehouseAddress.setText(String.format(address, data.data.warehouse.province, data.data.warehouse.city,
                    data.data.warehouse.district, data.data.warehouse.warehouseAddress));
        }

        // 交货日期
        tvDateDelivery.setText(data.data.deliveryDate);
        // 单价
        tvPrice.setText(currency + data.data.price);
        // 报价备注
        tvRemarksNote.setText(data.data.reviewNote);
    }

    @Override
    public void updateDate(String date) {
        tvDateDelivery.setText(date);
    }


    //  报价详情 供方配送 -是/否
    private String getIsSupplierDistribution(int isSupplierDistribution) {
        String isSupplierDistributionStr = "";
        switch (isSupplierDistribution){
            case 0:
                isSupplierDistributionStr = getResources().getString(R.string.no);
                break;
            case 1:
                isSupplierDistributionStr = getResources().getString(R.string.yes);
                break;
            default:
                isSupplierDistributionStr = getResources().getString(R.string.no);
                break;

        }
        return isSupplierDistributionStr;
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

    @OnClick(R.id.linear_warehouse_choose)
    public void onWarehouseClicked(){
        mPresenter.updateWarehouse();
    }

    @OnClick(R.id.linear_date_delivery)
    public void onDateClicked(){
        if (!isDetail){
            // 只有“报价” 可以选择交货日期
            mPresenter.updateDeliveryDate();
        }

    }

    @OnClick(R.id.linear_price)
    public void onPriceClicked(){
        mPresenter.updatePrice();
    }

    // quote/review
    @OnClick(R.id.tv_bottom_bt)
    public void onBottomBtClicked() {
        if (isDetail){
            // 我的报价 详情 “复议报价” 我的报价列表里操作
//            mPresenter.doReview();
//            Intent intent = new Intent(this, ReviewActivity.class);
//            startActivity(intent);

        }else {
            // 报价 “确认报价”
            // set it in presenter
            mPresenter.setWarehouseJson(Constant.warehouseJson);
            // mPresenter.setCurrentPrice(Constant.quotePrice); set-onStart()

            if (mPresenter.checkCurrentDataComplete() && !mPresenter.getIsSubmitClicked()){
                mPresenter.doQuote();
            }else {
                showPromptMessage("请完整填写报价信息！");
            }
        }
    }


}
