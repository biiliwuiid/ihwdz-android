package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time :   2018/09/06
 * desc :
 * version: 1.0
 * </pre>
 */
public class UserData {
    public String code;
    public String msg;
    private UserEntity data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserEntity getData() {
        return data;
    }

    public void setData(UserEntity data) {
        this.data = data;
    }

    public static class UserEntity{
        private String accountNo;			// 账号
        private String password;			// 密码
        private String name;				// 姓名
        private String company;				// 公司名称
        private int source = 30;            // 30
        // private Integer source;			// 30  : OFFICIAL_WEBSITE("官网", 10), MONEY_TREE("摇钱塑", 20), INFO_MEMBER_APP("资讯APP", 30),OFFLINE("管理后台", 40), H5_REGISTER("H5注册", 50);
        private String checkNum;			// 注册验证码
        private int memberType = 0;         // 0-资讯会员  1-买家会员 2-商家会员
        //private Integer memberType = 0;   // 0-资讯会员  1-买家会员 2-商家会员
        private Long popAdminId;			// 商家管理员ID
        private Long buyerAdminId;			// 交易管理员ID  // 交易管理员ID  不选管理员的时候不传

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public String getCheckNum() {
            return checkNum;
        }

        public void setCheckNum(String checkNum) {
            this.checkNum = checkNum;
        }

        public int getMemberType() {
            return memberType;
        }

        public void setMemberType(int memberType) {
            this.memberType = memberType;
        }

        public Long getPopAdminId() {
            return popAdminId;
        }

        public void setPopAdminId(Long popAdminId) {
            this.popAdminId = popAdminId;
        }

        public Long getBuyerAdminId() {
            return buyerAdminId;
        }

        public void setBuyerAdminId(Long buyerAdminId) {
            this.buyerAdminId = buyerAdminId;
        }
    }
}
