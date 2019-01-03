package com.ihwdz.android.hwapp.ui.login.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.ui.home.index.IndexActivity;
import com.ihwdz.android.hwapp.ui.login.login.pwdforgot.PwdForgotActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.settings.SettingsActivity;
import com.ihwdz.android.hwapp.widget.CountDownTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ihwdz.android.hwapp.ui.login.login.LoginContract.MODEL_CODE;
import static com.ihwdz.android.hwapp.ui.login.login.LoginContract.MODEL_PWD;

public class LoginActivity extends BaseActivity implements LoginContract.View{

    static String TAG = "LoginActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.edit_telephone) EditText editTel;
    @BindView(R.id.edit_pwd) EditText editPwd;
    @BindView(R.id.tv_send_verify_code) CountDownTextView btCode;

    @BindView(R.id.dynamic_pwd_login) LinearLayout dynamicLoginLayout;
    @BindView(R.id.tv_login_type) TextView tvLoginType;

    @Inject LoginContract.Presenter mPresenter;

    static final String IS_FROM_PERSONAL_CENTER = "from_personal_center";             // 来自个人中心

    public static void startLoginActivity(Context context, boolean isFromPersonalCenter) {
        Log.i(TAG, "=================================== startLoginActivity ===================");
        Intent intent = new Intent(context, LoginActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IS_FROM_PERSONAL_CENTER, isFromPersonalCenter);
        context.startActivity(intent);

    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        initToolBar();
        updateView();
        if (getIntent() != null){
            boolean isFromPersonalCenter = getIntent().getBooleanExtra(IS_FROM_PERSONAL_CENTER, false);
            mPresenter.setIsFromPersonalCenter(isFromPersonalCenter);
        }
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    // toolbar
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.login));
    }

    @OnClick({R.id.dynamic_pwd_login, R.id.pwd_forget, R.id.tv_send_verify_code, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dynamic_pwd_login:
                mPresenter.setCurrentLoginModel(MODEL_CODE);
                break;
            case R.id.pwd_forget:
                if (mPresenter.getCurrentLoginModel() == MODEL_PWD){
                    //账号密码登录 - 点击 “忘记密码”
                    showPwdForgot();

                }else if (mPresenter.getCurrentLoginModel() == MODEL_CODE){
                    //动态密码登录 - 点击“账号密码登录”
                    mPresenter.setCurrentLoginModel(MODEL_PWD);

                }else {
                    Toast.makeText(this, "unknown login mode", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_send_verify_code:
                // 发送验证码
                mPresenter.setAccount(editTel.getText().toString().trim());
                mPresenter.sendCheckCode();
                break;
            case R.id.bt_login:
                if (mPresenter.getCurrentLoginModel() == MODEL_PWD){
                    mPresenter.setAccount(editTel.getText().toString().trim());
                    mPresenter.setPwd(editPwd.getText().toString().trim());
                    mPresenter.login();
                }else if (mPresenter.getCurrentLoginModel() == MODEL_CODE){

                    mPresenter.setCheckCode(editPwd.getText().toString().trim());
                    mPresenter.loginByCheckCode();
                }else {
                    Toast.makeText(this, "unknown login mode", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getInputLoginInfo() {
        switch (mPresenter.getCurrentLoginModel()){
            case MODEL_PWD:
                String telephone = editTel.getText().toString().trim();
                String pwd = editPwd.getText().toString().trim();
                mPresenter.setAccount(telephone);
                mPresenter.setPwd(pwd);
                Log.d(TAG, "======== telephone: "+ telephone);
                Log.d(TAG, "======== pwd: "+ pwd);
                break;
            case MODEL_CODE:
                String checkCode = editPwd.getText().toString().trim();
                mPresenter.setCheckCode(checkCode);
                Log.d(TAG, "======== checkCode: "+ checkCode);
                break;
        }
    }


    @Override
    public void showDynamicLogin() {
        dynamicLoginLayout.setVisibility(View.GONE);
        btCode.setVisibility(View.VISIBLE);
        tvLoginType.setText(getResources().getString(R.string.account_pwd));
    }

    @Override
    public void showNormalLogin() {
        dynamicLoginLayout.setVisibility(View.VISIBLE);
        btCode.setVisibility(View.GONE);
        tvLoginType.setText(getResources().getString(R.string.pwd_forget));
    }

    @Override
    public void showPwdForgot() {
        Intent intent = new Intent(LoginActivity.this, PwdForgotActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateView() {
        Log.d(TAG, "======================== updateView ====================: " + mPresenter.getCurrentLoginModel());
        switch (mPresenter.getCurrentLoginModel()){
            case MODEL_PWD:
                showNormalLogin();
                break;
            case MODEL_CODE:
                showDynamicLogin();
                break;
        }
    }

    @Override
    public void showPersonalCenter(int role) {
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
//        editText.setHint(getResources().getString(R.string.search_de));
    }

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
}
