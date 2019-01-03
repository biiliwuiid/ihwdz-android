package com.ihwdz.android.hwapp.ui.login.login.pwdforgot;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.widget.CountDownTextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class PwdForgotActivity extends BaseActivity  implements PwdForgotContract.View{

    static String TAG = "PwdForgotActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.edit_telephone) EditText editTel;
    @BindView(R.id.edit_code) EditText editCode;
    @BindView(R.id.edit_pwd) EditText editPwd;
    @BindView(R.id.edit_pwd2) EditText editPwd2;
    @BindView(R.id.tv_send_verify_code) CountDownTextView btCode;

    @Inject PwdForgotContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_pwd_forgot;
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

    }

    public static void startPwdForgotActivity(Context context){
        Log.i(TAG, "=================================== startPwdForgotActivity ===================");
        Intent intent = new Intent(context, PwdForgotActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.pwd_forget));
    }
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    @OnFocusChange(R.id.edit_telephone)
    public void onTelFocusChanged(){
        if (!editTel.isFocused()){
            Log.d(TAG, "============= setAccount ==========");
            //mPresenter.setAccount(editTel.getText().toString().trim());
        }
    }

    @OnClick(R.id.tv_send_verify_code)
    public void onVerifyCodeClicked(){
        Log.d(TAG, "===================== send verify code");
        mPresenter.setAccount(editTel.getText().toString().trim());
    }


    @OnClick(R.id.bt_complete)
    public void onCompleteClicked(){
        if (editPwd.getText().toString() != null && editPwd.getText().toString().equals(editPwd2.getText().toString())){
            mPresenter.setPwd(editPwd.getText().toString().trim());
            mPresenter.setCheckCode(editCode.getText().toString().trim());
            mPresenter.updatePassword();
        }else {
            Toast.makeText(this, getResources().getString(R.string.pwd_different), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void showLoginView() {
        onBackPressed();
    }

    @Override
    public void showKeyboard() {

    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    public void setSendCodeClickable(boolean startCount) {
        Log.d(TAG, "===================== set Start Count: " + startCount);
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


