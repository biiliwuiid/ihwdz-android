package com.ihwdz.android.hwapp.ui.login.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.widget.CountDownTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.ihwdz.android.hwapp.ui.login.register.RegisterContract.TYPE_Business;
import static com.ihwdz.android.hwapp.ui.login.register.RegisterContract.TYPE_Purchaser;

/**
 * 注册成功后返回个人中心
 */
public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    static String TAG = "RegisterActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    // edit text
    @BindView(R.id.edit_name) EditText editName;
    @BindView(R.id.edit_telephone) EditText editTelephone;
    @BindView(R.id.edit_verify_code) EditText editVerifyCode;
    @BindView(R.id.tv_send_verify_code) CountDownTextView btCode;   // 发送验证码
    @BindView(R.id.edit_pwd) EditText editPwd;

    // radio button
//    @BindView(R.id.bt_purchaser) CheckBox btPurchaser; // “我是买家”
//    @BindView(R.id.bt_business) CheckBox btBusiness;   // “我是商家”
//    @BindView(R.id.bt_purchaser) RadioButton btPurchaser; // “我是买家”
//    @BindView(R.id.bt_business) RadioButton btBusiness;   // “我是商家”
//    @BindView(R.id.linear_type) RelativeLayout linearType; // department layout, hide it if didn't select user type
//    @BindView(R.id.type_vip) RadioGroup typeVip;
//    @BindView(R.id.bt_type) RadioButton btType;    // department type
//    @BindView(R.id.bt_chose) RadioButton btChose;  // admin list

    // eula
    @BindView(R.id.bt_accept) CheckBox btAccept;
    @BindView(R.id.tv_terms_service) TextView tvTermsService;
    @BindView(R.id.tv_privacy_policy) TextView tvPrivacyPolicy;

    //register
    @BindView(R.id.bt_register) Button btRegister;

    @Inject RegisterContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    @Override
    public void initListener() {
//        editTelephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    btCode.setVisibility(View.VISIBLE);
//                    btCode.setStartCount(false);
//                }
//            }
//        });

    }

    @Override
    public void initData() {

    }


    public static void startRegisterActivity(Context context) {
        Log.i(TAG, "=================================== startRegisterActivity ===================");
        Intent intent = new Intent(context, RegisterActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void showErrorView(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    // 显示账号已存在
    @Override
    public void showAccountExisted() {
        //btCode.setStopCount();   // 停止计时
        Toast.makeText(this, getResources().getString(R.string.account_existed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegisterSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        MainActivity.startActivity(this, 4);
        finish();
    }

    @Override
    public void showKeyboard() {
        Log.d(TAG, "showKeyboard");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null){
//            editText.requestFocus();
//            imm.showSoftInput(editText, 0);
        }
    }

    @Override
    public void hideKeyboard() {
        Log.d(TAG, "hideKeyboard");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 发送验证码 可用
    @Override
    public void setSendCodeClickable(boolean startCount) {
        btCode.setStartCount(startCount);
        if (startCount){
            btCode.start();
        }
    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    // toolbar
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.register));
    }


    @OnClick({R.id.tv_send_verify_code,
            R.id.tv_terms_service, R.id.tv_privacy_policy, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_verify_code: // 点击 发送验证码
                mPresenter.setTelephone(editTelephone.getText().toString().trim());
                break;
            case R.id.tv_terms_service:     // 查看服务条款
                mPresenter.checkTermsOfService();
                break;
            case R.id.tv_privacy_policy:    // 查看隐私协议
                mPresenter.checkPrivacyPolicy();
                break;
            case R.id.bt_register:          // 点击注册按钮

                mPresenter.setUserName(editName.getText().toString().trim());
                //mPresenter.setTelephone(editTelephone.getText().toString().trim());
                mPresenter.setCheckCode(editVerifyCode.getText().toString().trim());
                mPresenter.setPassword(editPwd.getText().toString().trim());
                mPresenter.setIsEulaAccepted(btAccept.isChecked());

                mPresenter.postRegisterData();
                break;
        }
    }


}
