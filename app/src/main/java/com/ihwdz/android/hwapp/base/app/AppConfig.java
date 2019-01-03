package com.ihwdz.android.hwapp.base.app;

import com.blankj.utilcode.util.SPUtils;
import com.ihwdz.android.hwapp.common.Constant;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :
 * version: 1.0
 * </pre>
 */
public enum AppConfig {

    //对象
    INSTANCE;

    private boolean isLogin;

    public void initConfig(){
        //1.是否是登录状态
        isLogin = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_LOGIN, false);
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_LOGIN,login);
        this.isLogin = login;
    }

}
