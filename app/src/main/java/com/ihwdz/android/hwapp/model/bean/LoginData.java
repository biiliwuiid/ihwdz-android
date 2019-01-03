package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/17
 * desc :
 * version: 1.0
 * </pre>
 */
public class LoginData {

    public String code;
    public String msg;
    public LoginInfoEntity data;

    public static class LoginInfoEntity{
        public long accountId;
        public String accountNo;
        public String clientIp;
        public long expireTime;       //  token 过期日期
        public long lastLoginTimeStr;
        public long loginFrom;
        public long memberId;
        public String memberName;
        public int memberRole;
        public String token;
        public String email;
        public String companyFullName;
        public String companyNature;
        public String companyType;
    }

    public static class LoginEntity{
        public String code;
        public String msg;
        public String token;
        public String memberRole;
        public long accountId;
        public String accountNo;
        public long memberId;
        public String memberName;
        public int loginFrom;
        public String clientIp;
        public String lastLoginTimeStr;
        public long expireTime;
    }

}
