package com.ihwdz.android.hwapp.ui.me.vipinformation;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserInformation;
import com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic.PicUpdateActivity;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的xx会员 展示会员信息 -不修改: 企业信息 注册资金...
 */

public class VipInfoActivity extends BaseActivity implements VipInfoContract.View{

    String TAG = "VipInfoActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_social_credit_code)
    TextView tvSocialCreditCode;
    @BindView(R.id.tv_enterprise_type)
    TextView tvEnterpriseType;
    @BindView(R.id.tv_enterprise_nature)
    TextView tvEnterpriseNature;
    @BindView(R.id.tv_date_establish)
    TextView tvDateEstablish;
    @BindView(R.id.tv_fund_register)
    TextView tvFundRegister;
    @BindView(R.id.tv_address_district)
    TextView tvAddressDistrict;
    @BindView(R.id.tv_address_detail)
    TextView tvAddressDetail;
    @BindView(R.id.iv_business_license)
    ImageView ivBusinessLicense;
    @BindView(R.id.iv_invoice_info)
    ImageView ivInvoiceInfo;

    private String titleStr = "";
    private String unitStr = "";
    private String licensePath;
    private String invoicePath;

    @Inject VipInfoContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_vip_info;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        LogUtils.printCloseableInfo(TAG, "============= initData ===============");
        mPresenter.getVipInfo();
    }



    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        Log.d(TAG, "onBackPressed");
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        if (Constant.VIP_TYPE == 1){
            titleStr = getResources().getString(R.string.title_deal_vip);
        }else if (Constant.VIP_TYPE == 2){
            titleStr = getResources().getString(R.string.title_business_vip);
        }else {
            titleStr = "";
        }
        unitStr = getResources().getString(R.string.ten_thousand);  // 万元
        title.setText(titleStr);
    }


    // 点击所在地区
    @OnClick(R.id.linear_district)
    public void onDistrictClicked(){
        // 只展示信息 不允许修改
        // mPresenter.goSelectDistrict();
    }

    // 只展示信息 不允许修改
    @OnClick({R.id.linear_business_license, R.id.linear_invoice_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_business_license:    // 点击 营业执照 -> 修改营业执照
                //updatePicture(getResources().getString(R.string.title_update_license), licensePath);
                break;
            case R.id.linear_invoice_info:        // 点击 开票资料 -> 修改开票资料
                //updatePicture(getResources().getString(R.string.title_update_invoice), invoicePath);
                break;
        }
    }

    private void updatePicture(String string, String path) {
        Intent intent = new Intent(VipInfoActivity.this, PicUpdateActivity.class);
        intent.putExtra("TITLE", string);
        intent.putExtra("PATH", path);
        startActivity(intent);
    }


    @Override
    public void showVipInfo(UserInformation.UserInfo info) {

        if (info != null){
            tvCompany.setText(info.companyFullName);
            tvSocialCreditCode.setText(info.taxNumber);
            tvEnterpriseType.setText(info.companyType);
            tvEnterpriseNature.setText(info.companyNature);
            tvDateEstablish.setText(info.regTime);
            tvFundRegister.setText(info.regCapital + unitStr);

            String district = info.provinceName + " "+info.cityName + " "+info.districtName;
            tvAddressDistrict.setText(district);

            String detailAddress = info.address;
            tvAddressDetail.setText(detailAddress);

            licensePath = info.certificateImgUrl;
            invoicePath = info.ticketImgUrl;

            ImageUtils.loadImgByPicasso(this, info.certificateImgUrl, ivBusinessLicense);
            ImageUtils.loadImgByPicasso(this, info.ticketImgUrl, ivInvoiceInfo);
        }

    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string,Toast.LENGTH_SHORT).show();
    }
}
