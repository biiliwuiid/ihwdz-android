package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LicenseData;
import com.ihwdz.android.hwapp.utils.LQRPhotoSelectUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.DialogUtils;
import com.pedaily.yc.ycdialoglib.selectDialog.CustomSelectDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.DATE_ESTABLISH;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.DATE_VALID;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_ADMIN;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_ENTERPRISE;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_PRODUCT;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_RESULT_APPLYING;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_RESULT_FAIL;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_RESULT_IMPROVE;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_RESULT_NO;

/**
 *  申请开通交易会员(申请进度 及 结果)
 */
public class ApplyProgressActivity extends BaseActivity implements ApplyProgressContract.View {

    String titleStr = "";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;


    // 申请过程界面
    @BindView(R.id.apply_linear) LinearLayout applyLinear;
    // step tags
    @BindView(R.id.tv_step1) TextView tvStep1;
    @BindView(R.id.iv_line1) ImageView ivLine1;
    @BindView(R.id.tv_step2) TextView tvStep2;
    @BindView(R.id.iv_line2) ImageView ivLine2;
    @BindView(R.id.tv_step3) TextView tvStep3;
    @BindView(R.id.iv_line3) ImageView ivLine3;
    @BindView(R.id.tv_step4) TextView tvStep4;

    //@BindView(R.id.step1_layout) LinearLayout step1Linear;         // 法人信息
    @BindView(R.id.step2_layout) LinearLayout step2Linear;         // 企业信息
    @BindView(R.id.step3_layout) LinearLayout step3Linear;         // 生产信息
    @BindView(R.id.step4_layout) LinearLayout step4Linear;         // 管理员信息

    // 个人信息
//    @BindView(R.id.edit_user_name) EditText edUserName;             // 姓名
//    @BindView(R.id.edit_id_card_no) EditText edIdCardNo;            // 身份证号
//    @BindView(R.id.edit_telephone_no) EditText edTelephoneNo;       // 手机号码
//    @BindView(R.id.edit_authentication) EditText edVerifyCode;      // 验证码

    // 企业信息
    @BindView(R.id.iv) ImageView ivBusinessLicense;                  // 营业执照
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.linear_license_info) LinearLayout licenseLinear;  // 识别成功后显示
    @BindView(R.id.edit_credit_code) EditText edCreditCode;          // 社会信用代码
    @BindView(R.id.edit_company_name) EditText edCompanyName;        // 单位名称
    @BindView(R.id.edit_legal_person) EditText edLegalPerson;        // 法人
    @BindView(R.id.edit_date_establish) TextView edEstablishDate;    // 成立日期
    @BindView(R.id.edit_validity_date) TextView edValidDate;         // 有效期
    @BindView(R.id.linear_checking) LinearLayout infoCheckingLinear;         // 校验中请稍后...

    // 产品信息
    @BindView(R.id.title_w) TextView materialTitle;                  // 采购原料
    @BindView(R.id.recyclerView) RecyclerView materialRecyclerView;
    @BindView(R.id.title2_w) TextView goodsTitle;                    // 主营商品类型
    @BindView(R.id.recyclerView2) RecyclerView goodsRecyclerView;

    // 管理员信息
    // 2018/11/22 自己选择 和 平台指派 只能选其一
    @BindView(R.id.tv_choose) TextView chooseAdminTv;  // 请选择
    @BindView(R.id.bt_accept) CheckBox checkBox;       // 平台指派营销顾问



    @BindView(R.id.bt1) TextView bt1;  // 上一步
    @BindView(R.id.bt2) TextView bt2;  // 下一步（完成）

    // 申请结果界面
    @BindView(R.id.result_linear) LinearLayout resultLinear;

    @BindView(R.id.iv_result) ImageView ivResult;
    @BindView(R.id.tv_reviewing) TextView tvReviewing;
    @BindView(R.id.bt_open) Button openAgainBt;
    @BindView(R.id.bt_back) TextView btApplyResultBack;  // 申请结果 - 返回按钮(返回上一级)

    Drawable vividBg;
    Drawable normalBg;
    Drawable vividLine;
    Drawable normalLine;

    int state = 0; // 额度申请状态 -> 资讯会员状态 开通交易会员（ 0: 法人信息 ; 1: 企业信息; 2: 生产信息; 3：管理员信息）
    List materialCheckedList;
    List goodsCheckedList;

    final int MY_PERMISSIONS_REQUEST_CAMERA = 100;             // 相机 RequestCode
    final int PERMISSIONS_CODE_SELECT_IMAGE = 200;             // 相册 RequestCode

    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    String TAG = "ApplyProgressActivity";
    @Inject ApplyProgressContract.Presenter mPresenter;

    static final String APPLY_FROM = "apply_from";
    public static void startApplyProgressActivity(Context context, int applyFrom) {
        Intent intent = new Intent(context, ApplyProgressActivity.class);
        intent.putExtra(APPLY_FROM, applyFrom);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_apply_progress;
    }

    @Override
    public void initView() {
        initToolbar();
        initIntentData();

        // 产品信息 初始化
        materialCheckedList = new ArrayList();
        goodsCheckedList = new ArrayList();
        initMaterialRecyclerView();
        initGoodsRecyclerView();

        // 选择管理人
        chooseAdminTv.setText(getResources().getString(R.string.choose_please));

        //initApplyView();
        init();
    }

    private void initIntentData() {
        int applyFrom = getIntent().getIntExtra(APPLY_FROM, 0);

        if (applyFrom == Constant.ApplyFrom.DEAL_USER){
            // 返回个人中心
            btApplyResultBack.setText(getResources().getString(R.string.back_personal_center));
        }else {
            // 返回
            btApplyResultBack.setText(getResources().getString(R.string.back));
        }
//        if (applyFrom == Constant.ApplyFrom.PURCHASE_POOL){
//            btApplyResultBack.setText(getResources().getString(R.string.back));// 返回
//        }

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolbar() {
        titleStr = getResources().getString(R.string.title_open_deal_vip);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);

        vividBg = getResources().getDrawable(R.drawable.complete);
        normalBg = getResources().getDrawable(R.drawable.uncompleted);
        vividLine = getResources().getDrawable(R.drawable.complete_line);
        normalLine = getResources().getDrawable(R.drawable.uncompleted_line);
    }

    private void initApplyView() {
        // 开通交易会员结果 : 0未申请 1申请中 2申请失败 3完善资料
        // 额度申请状态（ 0: 法人信息; 0: 企业信息; 1: 生产信息; 2：管理员信息）
//        LogUtils.printInfo(TAG, "开通交易会员结果: " + Constant.APPLY_STATUS);
//
//        state = Constant.APPLY_STATUS;
//        //LogUtils.printInfo(TAG, "1申请中 - 开通申请已提交...: " + Constant.APPLY_STATUS);
//        switch (state){
//            case 0:
//                mPresenter.setApplyState(STATE_ENTERPRISE);      // 未申请: 开始申请  - step1 企业信息
//                break;
//            case 1:
//                mPresenter.setApplyState(STATE_RESULT_APPLYING); // 1申请中 - 开通申请已提交...
//                break;
//            case 2:
//                mPresenter.setApplyState(STATE_RESULT_FAIL);      // 2申请失败
//                break;
//            case 3:
//                mPresenter.setApplyState(STATE_RESULT_IMPROVE);   // 3完善资料 - 1-3个工作人内...
//                break;
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    // 选择管理人后 更新
    @Override
    protected void onStart() {
        super.onStart();
        updateView();

        // 显示管理员
        if (Constant.adminName != null && Constant.adminName.length() > 0){
            chooseAdminTv.setText(Constant.adminName);
            checkBox.setChecked(false);
        }else {

        }

    }

    @Override
    public void showWaitingRing() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWaitingRing() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showChecking() {
        // infoCheckingLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideChecking() {
        // infoCheckingLinear.setVisibility(View.GONE);
    }

    @Override
    public void showDialogAnim() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        DialogUtils.showDialog(this,new CustomSelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:                 //拍照

                        // 3、调用拍照方法
                        PermissionGen.with(ApplyProgressActivity.this)
                                .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA
                                ).request();
                        
                        //showCamera();
                        //startGetCamPhoto();      // jph.takephoto
                        break;
                    case 1:                 //相册

                        // 3、调用从图库选取图片方法
                        PermissionGen.needPermission(ApplyProgressActivity.this,
                                LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        );
                        
                        //showGallery();
                        //startGetPhotoFromFile(); // jph.takephoto
                        break;
                }
            }
        }, names);
    }

    @Override
    public void setEditable(boolean editable) {
        edCreditCode.setFocusable(editable);
        edCreditCode.setFocusableInTouchMode(editable);

        edCompanyName.setFocusable(editable);
        edCompanyName.setFocusableInTouchMode(editable);

        edLegalPerson.setFocusable(editable);
        edLegalPerson.setFocusableInTouchMode(editable);

//        edEstablishDate.setFocusable(editable);
//        edEstablishDate.setFocusableInTouchMode(editable);
//
//        edValidDate.setFocusable(editable);
//        edValidDate.setFocusableInTouchMode(editable);

    }

    @Override
    public void updateView(){
        LogUtils.printCloseableInfo(TAG, "updateView ======================getApplyState STATE: "+ mPresenter.getApplyState());
        LogUtils.printCloseableInfo(TAG, "updateView ======================getApplyResult STATE: "+ mPresenter.getApplyResult());
        state = Constant.APPLY_STATUS;
        mPresenter.setApplyResult(state);
        switch (state){
            case STATE_RESULT_NO:
                // 0未申请
                switch (mPresenter.getApplyState()){
//                    case STATE_LEGAL_PERSON:
//                        // 法人信息
//                        initLegalPersonView();
//                        break;
                    case STATE_ENTERPRISE:
                        // 企业信息
                        initEnterpriseView();
                        break;
                    case STATE_PRODUCT:
                        // 生产信息
                        initProductView();
                        break;
                    case STATE_ADMIN:
                        // 管理员信息
                        initAdminView();
                        break;
                }
//
                break;
            case STATE_RESULT_APPLYING:
                // 1申请中 - 开通申请已提交...
                initResultApplyingView();
                break;
            case STATE_RESULT_IMPROVE:
                // 3完善资料 - 1-3个工作人内...
                initResultImproveView();
                break;
            case STATE_RESULT_FAIL:
                // 2申请失败
                initResultFailView();
                break;
        }
    }

    @Override
    public void updateEstablishDate(String date) {
        edEstablishDate.setText(date);
    }

    @Override
    public void updateValidDate(String date) {
        edValidDate.setText(date);
    }

    // APPLY - 法人信息
    private void initLegalPersonView() {
        resultLinear.setVisibility(View.GONE);
        applyLinear.setVisibility(View.VISIBLE);

        tvStep1.setBackground(vividBg);
        ivLine1.setImageDrawable(vividLine);
        tvStep2.setBackground(normalBg);
        ivLine2.setImageDrawable(normalLine);
        tvStep3.setBackground(normalBg);
        ivLine3.setImageDrawable(normalLine);
        tvStep4.setBackground(normalBg);

        //step1Linear.setVisibility(View.VISIBLE);
        step2Linear.setVisibility(View.GONE);
        step3Linear.setVisibility(View.GONE);
        step4Linear.setVisibility(View.GONE);

        bt2.setText(getResources().getString(R.string.next));
    }

    // APPLY - 企业信息
    private void initEnterpriseView() {
        resultLinear.setVisibility(View.GONE);
        applyLinear.setVisibility(View.VISIBLE);

        tvStep1.setBackground(vividBg);
        ivLine1.setImageDrawable(vividLine);
        tvStep2.setBackground(vividBg);
        ivLine2.setImageDrawable(normalLine);
        tvStep3.setBackground(normalBg);
        ivLine3.setImageDrawable(normalLine);
        tvStep4.setBackground(normalBg);

        //step1Linear.setVisibility(View.GONE);
        step2Linear.setVisibility(View.VISIBLE);
        step3Linear.setVisibility(View.GONE);
        step4Linear.setVisibility(View.GONE);

        bt2.setText(getResources().getString(R.string.next));
    }

    // APPLY - 生产信息
    private void initProductView() {
        resultLinear.setVisibility(View.GONE);
        applyLinear.setVisibility(View.VISIBLE);

        tvStep1.setBackground(vividBg);
        ivLine1.setImageDrawable(vividLine);
        tvStep2.setBackground(vividBg);
        ivLine2.setImageDrawable(vividLine);
        tvStep3.setBackground(vividBg);
        ivLine3.setImageDrawable(normalLine);
        tvStep4.setBackground(normalBg);

        //step1Linear.setVisibility(View.GONE);
        step2Linear.setVisibility(View.GONE);
        step3Linear.setVisibility(View.VISIBLE);
        step4Linear.setVisibility(View.GONE);

        bt2.setText(getResources().getString(R.string.next));

       // materialCheckedList = new ArrayList();
       // goodsCheckedList = new ArrayList();

       // initMaterialRecyclerView();
       // initGoodsRecyclerView();
    }

    // APPLY - 管理员信息
    private void initAdminView() {
        resultLinear.setVisibility(View.GONE);
        applyLinear.setVisibility(View.VISIBLE);

        tvStep1.setBackground(vividBg);
        ivLine1.setImageDrawable(vividLine);
        tvStep2.setBackground(vividBg);
        ivLine2.setImageDrawable(vividLine);
        tvStep3.setBackground(vividBg);
        ivLine3.setImageDrawable(vividLine);
        tvStep4.setBackground(vividBg);

        //step1Linear.setVisibility(View.GONE);
        step2Linear.setVisibility(View.GONE);
        step3Linear.setVisibility(View.GONE);
        step4Linear.setVisibility(View.VISIBLE);

        bt2.setText(getResources().getString(R.string.complete));
    }


    // RESULT - 1申请中 - 开通申请已提交...
    private void initResultApplyingView() {
        LogUtils.printCloseableInfo(TAG, " RESULT - 1申请中 - 开通申请已提交.. ===========  initResultApplyingView ==========");
        applyLinear.setVisibility(View.GONE);
        resultLinear.setVisibility(View.VISIBLE);

        ivResult.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        tvReviewing.setText(getResources().getString(R.string.reviewing_text));

        openAgainBt.setVisibility(View.GONE);
        btApplyResultBack.setVisibility(View.VISIBLE);
    }
    //  RESULT - 3完善资料 - 1-3个工作人内...
    private void initResultImproveView() {
        applyLinear.setVisibility(View.GONE);
        resultLinear.setVisibility(View.VISIBLE);

        ivResult.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        tvReviewing.setText(getResources().getString(R.string.reviewing_improve_text));

        openAgainBt.setVisibility(View.GONE);
        btApplyResultBack.setVisibility(View.VISIBLE);

    }
    //  RESULT - 2申请失败
    private void initResultFailView() {
        applyLinear.setVisibility(View.GONE);
        resultLinear.setVisibility(View.VISIBLE);

        ivResult.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        tvReviewing.setText(getResources().getString(R.string.reviewing_failure_text));

        openAgainBt.setVisibility(View.VISIBLE);
        btApplyResultBack.setVisibility(View.GONE);
    }


    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLicenseInfo(LicenseData.LicenseEntity data) {
        licenseLinear.setVisibility(View.VISIBLE);
        String nothing = getResources().getString(R.string.nothing);   // 无
        if (data.creditCode == null || data.creditCode.equals(nothing)){
            edCreditCode.setHint(nothing);
        }else if (data.creditCode.length() > 0){
            edCreditCode.setText(data.creditCode);
        }

        if (data.companyName == null || data.companyName.equals(nothing)){
            edCompanyName.setHint(nothing);
        }else if (data.companyName.length() > 0){
            edCompanyName.setText(data.companyName);
        }

        if (data.legalPerson == null || data.legalPerson.equals(nothing)){
            edLegalPerson.setHint(nothing);
        }else if (data.legalPerson.length() > 0){
            edLegalPerson.setText(data.legalPerson);
        }

        if (data.startTimeStr == null || data.startTimeStr.equals(nothing)){
            edEstablishDate.setHint(nothing);
        }else if (data.startTimeStr.length() > 0){
            edEstablishDate.setText(data.startTimeStr);
            mPresenter.setStartTime(data.startTimeStr);
        }

        if (data.endTimeStr == null || data.endTimeStr.equals(nothing)){
            edValidDate.setHint(nothing);
        }else if (data.endTimeStr.length() > 0){
            edValidDate.setText(data.endTimeStr);
            mPresenter.setEndTime(data.endTimeStr);
        }

        Toast.makeText(this,"请仔细核对信息！",Toast.LENGTH_SHORT).show();
    }

    // 点击 成立日期
    @OnClick(R.id.edit_date_establish)
    public void onEstablishClicked(){
        mPresenter.selectDate(DATE_ESTABLISH);
    }

    // 点击 有效期
    @OnClick(R.id.edit_validity_date)
    public void onValidityClicked(){
        mPresenter.selectDate(DATE_VALID);
    }


    // 生产信息 -
    private void initMaterialRecyclerView() {
        materialRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        materialRecyclerView.setLayoutManager(layoutManager);
        materialRecyclerView.setAdapter(mPresenter.getMaterialAdapter());
        mPresenter.getMaterialAdapter().addItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onItemCheckListener(String checkedStr) {
                Log.d(TAG, "Checked string: " + checkedStr);
                materialCheckedList.add(checkedStr);
            }

            @Override
            public void onItemCancelListener(String cancelStr) {
                materialCheckedList.remove(cancelStr);
            }
        });
    }
    // 生产信息 -
    private void initGoodsRecyclerView() {
        goodsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        goodsRecyclerView.setLayoutManager(layoutManager);
        goodsRecyclerView.setAdapter(mPresenter.getGoodsAdapter());
        mPresenter.getGoodsAdapter().addItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onItemCheckListener(String checkedStr) {
                goodsCheckedList.add(checkedStr);
            }

            @Override
            public void onItemCancelListener(String cancelStr) {
                goodsCheckedList.remove(cancelStr);
            }
        });
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        mPresenter.getApplyStateData();
        mPresenter.getProductData();
    }


    @OnClick(R.id.iv)  // 点击 上传营业执照
    public void onUploadBusinessLicenseClicked(){
        showDialogAnim();
    }

    @OnClick(R.id.linear_admin) // 点击 选择营销顾问
    public void onAdminChooseClicked(){
        mPresenter.goSelectAdmin();
    }

    // 勾选平台指派
    @OnCheckedChanged(R.id.bt_accept)
    public void onCheckedChanged(){
        if (checkBox.isChecked()){
            Constant.adminId = null;
            Constant.adminName = null;
            chooseAdminTv.setText(getResources().getString(R.string.assigned)); // 平台指派
        }
    }


    // 点击“上一步/下一步”
    @OnClick({R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:   // 点击 上一步
                switch (mPresenter.getApplyState()){
                    case STATE_ENTERPRISE:
                        onBackPressed();
                        //mPresenter.setApplyState(STATE_LEGAL_PERSON);// 返回 法人信息
                        break;
                    case STATE_PRODUCT:
                        mPresenter.setApplyState(STATE_ENTERPRISE);    // 返回 企业信息
                        break;
                    case STATE_ADMIN:
                        mPresenter.setApplyState(STATE_PRODUCT);       // 返回 生产信息
                        break;

                }
                break;

            case R.id.bt2:  // 点击 下一步
                switch (mPresenter.getApplyState()){
//                    case 0:
                        // 上传法人信息
//                        mPresenter.setLegalName(edUserName.getText().toString().trim());
//                        mPresenter.setLegalTel(edTelephoneNo.getText().toString().trim());
//                        mPresenter.setLegalIdCard(edIdCardNo.getText().toString().trim());
//                        if (mPresenter.isLegalInfoComplete()){
//                            mPresenter.postLegalPersonInfo();
//                            mPresenter.setApplyState(STATE_ENTERPRISE); // 进入1：企业信息编辑
//                        }else {
//                            //Toast.makeText(this, "请检查法人信息！", Toast.LENGTH_SHORT).show();
//                        }
//                        break;
                    case STATE_ENTERPRISE:
                        /**校验
                         *  社会信用代码-必填-18位；
                            单位名称-必填；
                            法人-必填；

                            企业信息填写完整
                         */
                        // 上传企业信息 （上传营业执照）

                        mPresenter.setCreditCode(edCreditCode.getText().toString().trim());   // 社会信用代码
                        mPresenter.setCompanyName(edCompanyName.getText().toString().trim()); // 单位名称-必填
                        mPresenter.setLegalName(edLegalPerson.getText().toString().trim());   // 法人-必填
                        mPresenter.setStartTime(edEstablishDate.getText().toString().trim());   // 成立日期
                        mPresenter.setEndTime(edValidDate.getText().toString().trim());       // 有效期

                        if (mPresenter.isLicenseUpload() && mPresenter.isEnterpriseInfoComplete()){
                            mPresenter.checkCreditCode();
                            // mPresenter.setApplyState(STATE_PRODUCT);                          // 进入2：生产信息编辑 校验成功后调
                        }else {
                            //String remind = getResources().getString(R.string.license_null);
                            //Toast.makeText(this, remind, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case STATE_PRODUCT:
                        // 提交生产信息
                        if (materialCheckedList != null && materialCheckedList.size() > 0){
                            for (int i = 0; i < materialCheckedList.size(); i++) {
                                mPresenter.setMaterialProductInfo(""+ materialCheckedList.get(i));
                            }
                        }
                        if (goodsCheckedList!=null && goodsCheckedList.size() > 0){
                            for (int i = 0; i < goodsCheckedList.size(); i++) {
                                mPresenter.setGoodsProductInfo(""+ goodsCheckedList.get(i));
                            }
                        }
                        mPresenter.setApplyState(STATE_ADMIN);      // 进入3：管理员信息编辑
                        break;
                    case STATE_ADMIN:
                        // 点击完成 提交管理员信息
                        mPresenter.setAdmin(Constant.adminName);
                        mPresenter.setAdminId(Constant.adminId);
                        LogUtils.printCloseableInfo(TAG, "================================================= ");
                        LogUtils.printCloseableInfo(TAG, "checkBox.isChecked(): " +checkBox.isChecked());
                        mPresenter.setAcceptAssign(checkBox.isChecked());

                        if (mPresenter.isAdminComplete()){
                            mPresenter.setAdminData();
                            if (mPresenter.getIsSubmitClicked()){
                                // 已经提交数据
                                // showPromptMessage("已经提交数据");
                            }else {
                                mPresenter.postApplyData();
                            }

                        }else {
                            //showPromptMessage("请选择营销顾问！"); // mPresenter.isAdminComplete()检验中有提示信息
                        }
                        break;
                }

                break;
        }
     }

    // 点击“重新开通”
    @OnClick(R.id.bt_open)
    public void onOpenAgainClicked(){
        mPresenter.setApplyState(STATE_ENTERPRISE);      // 未申请: 开始申请  - step1 企业信息
    }

    // 点击 “返回个人中心”/ "返回"（返回求购池）
    @OnClick(R.id.bt_back)
    public void onBackToPerCenterClicked(){
        onBackClicked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    // 拍照 / 相册
    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                String filePath = outputFile.getAbsolutePath();
                Log.d(TAG, "当拍照或从图库选取图片成功后回调: getAbsolutePath: "+ outputFile.getAbsolutePath());
                Log.d(TAG, "当拍照或从图库选取图片成功后回调: outputUri.toString(): "+ outputUri.toString());
                Glide.with(ApplyProgressActivity.this).load(outputUri).into(ivBusinessLicense);
                mPresenter.setLicensePath(filePath);
                showWaitingRing();
                mPresenter.postLicense(Constant.token, filePath);
            }
        }, false);//true裁剪，false不裁剪

        //        mLqrPhotoSelectUtils.setAuthorities("com.lqr.lqrnativepicselect.fileprovider");
        //        mLqrPhotoSelectUtils.setImgPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    public void showDialog() {
        //创建对话框创建器
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + ApplyProgressActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }

}
