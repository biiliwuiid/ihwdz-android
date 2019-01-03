package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/11
 * desc :
 * version: 1.0
 * </pre>
 */
public class UserTypeData {

    public String code;
    public String msg;
    public UserStatus data;

    public static class UserStatus{

        public int type;         // 用户类型：-1用户， 0-咨询会员，1-交易会员，2-商家会员
        public int status;       // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
        public int authStatus;   // 会员认证 状态： 0 -未认证, 1 -已认证
    }

}
