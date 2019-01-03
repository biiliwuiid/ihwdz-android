package com.ihwdz.android.hwapp.ui.login.register;

import android.widget.EditText;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc : 注册成功后返回个人中心
 * version: 1.0
 * </pre>
 */
public interface RegisterContract {

    interface View extends BaseView {

        //void showDepartmentType();         // 显示对应部门
        //void hideDepartmentType();         // 隐藏对应部门
        //void showAdministrator();          // 显示管理人列表
        void showErrorView(String error);    // 显示错误提示
        void showAccountExisted();             // 显示账号已存在
        void showRegisterSuccess(String msg);  // 注册成功 返回个人中心 并登录

        void showKeyboard(); //　弹出软键盘
        void hideKeyboard();

        void setSendCodeClickable(boolean clickable); // 发送验证码 可用
        void showPromptMessage(String string);       //  提示信息
    }

    int TYPE_Purchaser = 1;
    int TYPE_Business = 2;
    interface Presenter extends BasePresenter {

        void checkIsRegistered();       // 该手机号是否已经注册
        void sendVerificationCode();    // 发送验证码 －　接口返回判断账号是否存在
        void postRegisterData();        // 注册


        void checkTermsOfService();   // 查看服务条款
        void checkPrivacyPolicy();    // 查看隐私协议

        // 检查　各项输入格式，是否为空；
        void setUserName(String name);
        void setTelephone(String tel);
        void setCheckCode(String code);
        void setPassword(String password);
        void setIsEulaAccepted(boolean value);
    }

}
