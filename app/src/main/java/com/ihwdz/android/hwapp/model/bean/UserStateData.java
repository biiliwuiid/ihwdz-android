package com.ihwdz.android.hwapp.model.bean;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/11
 * desc :
 * version: 1.0
 * </pre>
 */
public class UserStateData {

    public String code;
    public String msg;
    public long timeStamp = 0;
    public UserStateInfo data;

    public static class UserStateInfo{

        public String type;        // 申请额度 : 0 不能申请; 1 未申请; 2 申请中; 3 申请完成; 4 申请失败;//not use now
        public String endDateStr;  // vip过期时间
        public String goodsName;   // VIP level : 凤凰/喜鹊/麻雀
        public String totalAmount; // 交易用户是否认证: length>0 已认证   - 查看额度总值
        public String usedAmount;
        public String availableAmount;
        public String tradeMemberApplyStatus; //开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料

        public String deposit;          // 保证金
        public String memberServeId;    // use for 保证金
    }
}
