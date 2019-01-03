package com.ihwdz.android.hwapp.ui.purchase.reviewquote;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 备注:
 *   交易会员:
 *   价格复议（求购报价-价格复议）“确认复议” (提交成功后 返回 求购报价)
 *   申请展期备注（订单列表-申请展期）“提交申请”
 *   授信额度 （一键下单-结算方式:部分授信）“确认”
 *
 *   商家会员:
 *   复议报价 （我的报价-复议报价）“确认报价”
 */
public class ReviewActivity extends BaseActivity implements ReviewContract.View{

    String TAG = "ReviewActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.edit) EditText editText;              // 输入框
    @BindView(R.id.tv_remarks) TextView tvRemarks;       // 剩余额度提示
    @BindView(R.id.edit_remarks) EditText editRemarks;   // 备注文本输入框

    @BindView(R.id.bt) Button confirmBt;                  // 确认按钮

    static final String MODE_REVIEW = "mode_review";       // 备注类型（报价/授信额度/复议报价...）
    static final String CONTENT_REVIEW = "content_review"; // 备注内容
    static final String CONTENT_HINT = "content_hint";     // 备注内容 提示
    static final String CREDIT_LEFT = "credit_left";       // 授信额度 剩余额度
    static final String CREDIT_MAX1 = "credit_max1";       // 授信额度 最大限制1 - 最大可用额度（不是剩余额度）
    static final String CREDIT_MAX2 = "credit_max2";       // 授信额度 最大限制2 - 采购总金额
    static final String QUOTE_ID = "quote_id";             // 某条报价的复议

    private int currentMode;
    private boolean isHint = true;
    private String currentContent;
    private String creditLeft;
    private String currentId;

    @Inject ReviewContract.Presenter mPresenter;

    /**
     *
     * @param context
     * @param mode
     * @param content
     * @param id
     */
    public static void startReviewActivity(Context context,int mode, String content,boolean isHint, String credit, String id) {
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra(MODE_REVIEW, mode);
        intent.putExtra(CONTENT_REVIEW, content);
        intent.putExtra(CONTENT_HINT, isHint);
        intent.putExtra(CREDIT_LEFT, credit);
        intent.putExtra(QUOTE_ID, id);
        context.startActivity(intent);
    }

    // 授信额度 <- OrderConfirmPresenter
    public static void startCreditReviewActivity(Context context,int mode, String content,boolean isHint, String creditLeft, double creditMax1, double creditMax2) {
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra(MODE_REVIEW, mode);
        intent.putExtra(CONTENT_REVIEW, content);
        intent.putExtra(CONTENT_HINT, isHint);
        intent.putExtra(CREDIT_LEFT, creditLeft);
        intent.putExtra(CREDIT_MAX1, creditMax1);
        intent.putExtra(CREDIT_MAX2, creditMax2);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_review;
    }

    @Override
    public void initView() {
        initToolbar();
        initIntentData();  // 初始化 Intent携带数据
    }

    private void initIntentData() {
        if (getIntent()!= null){
            // 类型
            currentMode = getIntent().getIntExtra(MODE_REVIEW, 0);
            mPresenter.setCurrentMode(currentMode);

            // 内容
            isHint = getIntent().getBooleanExtra(CONTENT_HINT, true);
            currentContent = getIntent().getStringExtra(CONTENT_REVIEW);
            if (currentContent != null){
                if (isHint){
                    editText.setHint(currentContent);
                    editRemarks.setHint(currentContent);
                }else {
                    editText.setText(currentContent);
                    editRemarks.setText(currentContent);
                }
            }

            // 剩余额度
            creditLeft = getIntent().getStringExtra(CREDIT_LEFT);
            String remarks = getResources().getString(R.string.credit_left);
            if (creditLeft != null){
                tvRemarks.setText(String.format(remarks, creditLeft));
            }

            double max1 = getIntent().getDoubleExtra(CREDIT_MAX1, 0.00d);
            double max2 = getIntent().getDoubleExtra(CREDIT_MAX2, 0.00d);
            mPresenter.setCreditMax(max1, max2);

            // 求购报价-价格复议 id
            currentId = getIntent().getStringExtra(QUOTE_ID);
            mPresenter.setCurrentId(currentId);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    public void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
    }

    // 价格复议 - 买家 备注
    @Override
    public void updatePurchaseView() {
        title.setText(getResources().getString(R.string.price_review));
        editText.setVisibility(View.GONE);
        editRemarks.setVisibility(View.VISIBLE);
        confirmBt.setText(getResources().getString(R.string.review_confirm));
    }

    // 申请展期 - 买家 备注
    @Override
    public void updateExtensionView() {
        title.setText(getResources().getString(R.string.extension_remarks_title));
        editText.setVisibility(View.GONE);
        editRemarks.setVisibility(View.VISIBLE);
        confirmBt.setText(getResources().getString(R.string.confirm));
    }

    // 授信额度 - 买家 - 数字
    @Override
    public void updateCreditView() {
        title.setText(getResources().getString(R.string.credit_line));
        controlInput();
        tvRemarks.setVisibility(View.VISIBLE);
        confirmBt.setText(getResources().getString(R.string.confirm));
    }

    // 复议价格 - 商家 - 数字
    @Override
    public void updateQuoteView() {
        title.setText( getResources().getString(R.string.review_price));
        // editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        // editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        controlInput();
        confirmBt.setText(getResources().getString(R.string.quote_confirm));
    }

    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt)
    public void onQuoteBtClicked(){
        String review = editText.getText().toString().trim();
        if (mPresenter.getCurrentMode() == Constant.Remarks.EXTENSION_APPLY || mPresenter.getCurrentMode() == Constant.Remarks.PRICE_REVIEW){
            review = editRemarks.getText().toString().trim();
        }
        mPresenter.setReviewContent(review);
        mPresenter.doClickButton();
    }

    // 控制输入 只能有两位小数
    private void controlInput(){
        //设置Input的类型两种都要
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);

        //设置字符过滤
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(".") && dest.toString().length() == 0){
                    return "0.";
                }
                if(dest.toString().contains(".")){
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if(length == 3){
                        return "";
                    }
                }
                return null;
            }
        }});
    }


}
