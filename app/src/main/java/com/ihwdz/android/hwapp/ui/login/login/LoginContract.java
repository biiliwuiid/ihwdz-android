package com.ihwdz.android.hwapp.ui.login.login;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/05
 * desc :
 * version: 1.0
 * </pre>
 */
public interface LoginContract {

    interface View extends BaseView {

        void showDynamicLogin();         // 动态密码登录
        void showNormalLogin();          // 账号密码登录

        void showPwdForgot();
        void updateView();               // 更新页面
        void showPersonalCenter(int role);

        void showKeyboard();                //　弹出软键盘
        void hideKeyboard();                //　隐藏软键盘

        void setSendCodeClickable(boolean clickable);
        void showPromptMessage(String string); //  提示信息
    }

    int MODEL_PWD = 0;  // 账号密码登录
    int MODEL_CODE = 1; // 验证码登录

    interface Presenter extends BasePresenter {

        void setCurrentLoginModel(int model);
        int getCurrentLoginModel();

        // 从个人中心登录 要强制返回个人中心
        void setIsFromPersonalCenter(boolean is);
        boolean isFromPersonalCenter();

        void login();              // 用户名密码登录
        void getUserType();        // 用户类型、认证、锁定状态 (登录之后获取)

        void getUserStatus();      // 用户信息： 交易会员是否认证

        void sendCheckCode();      // 获取验证码
        void loginByCheckCode();   // 动态密码登录

        void setAccount(String account);
        void setPwd(String pwd);
        void setCheckCode(String code);

    }

}
