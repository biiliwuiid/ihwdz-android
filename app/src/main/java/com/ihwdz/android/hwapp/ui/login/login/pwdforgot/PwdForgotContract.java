package com.ihwdz.android.hwapp.ui.login.login.pwdforgot;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/15
 * desc :
 * version: 1.0
 * </pre>
 */
public interface PwdForgotContract {

    interface View extends BaseView {

        void showLoginView();
        void showKeyboard();                //　弹出软键盘
        void hideKeyboard();                //　隐藏软键盘
        void setSendCodeClickable(boolean clickable);
        void showPromptMessage(String string); //  提示信息

    }

    interface Presenter extends BasePresenter {

        void sendCheckCode();       // 发送验证码
        void setAccount(String account);
        void setPwd(String pwd);
        void setCheckCode(String code);
        void updatePassword();     // 点击完成 修改密码
    }
}
