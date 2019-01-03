package com.ihwdz.android.hwapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.ui.login.login.LoginActivity;
import com.ihwdz.android.hwapp.ui.login.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录/注册　提示页
 */
public class LoginPageActivity extends BaseActivity {

    static String TAG = "LoginPageActivity";
    @BindView(R.id.fl_title_menu)
    FrameLayout btBack;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.qq)
    LinearLayout qqLinear;
    @BindView(R.id.we_chat)
    LinearLayout weChatLinear;
    @BindView(R.id.blog)
    LinearLayout blogLinear;

    private boolean mIsFromPersonalCenter = false;
    static final String IS_FROM_PERSONAL_CENTER = "from_personal_center";             // 来自个人中心

    public static void startLoginPageActivity(Context context) {
        Log.i(TAG, "=================================== startLoginPageActivity ===================");
        Intent intent = new Intent(context, LoginPageActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startLoginPageActivity(Context context, boolean isFromPersonalCenter) {
        Log.i(TAG, "=================================== startLoginPageActivity ===================");
        Intent intent = new Intent(context, LoginPageActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IS_FROM_PERSONAL_CENTER, isFromPersonalCenter);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login_page;
    }

    @Override
    public void initView() {

        if (getIntent() != null){
            mIsFromPersonalCenter = getIntent().getBooleanExtra(IS_FROM_PERSONAL_CENTER, false);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }



    @OnClick({R.id.fl_title_menu, R.id.bt_login, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_title_menu:
                onBackPressed();
                break;
            case R.id.bt_login:
                LoginActivity.startLoginActivity(this ,mIsFromPersonalCenter );
                finish();
                break;
            case R.id.bt_register:
                RegisterActivity.startRegisterActivity(this);
                finish();
                break;
        }
    }
}
