package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/05
 * desc :   申请开通交易会员
 * version: 1.0
 * </pre>
 */
public class ApplyDealVipData {

    private static final long serialVersionUID = 1L;

    public String token;            //·
    public String accountId;
    public String memberId;
    public String userName;				 // 姓名 ·
    public String idCard;				 // 身份证
    public String phone;				 //	手机号 ·

    public String imageUrl;			     // 营业执照路径 ·
    public String creditCode;			 // 信用代码·
    public String companyName;			 // 公司名称·
    public String legalPerson;			 // 公司法人·
    public String startTimeStr;			 // 成立时间·
    public String endTimeStr;			 // 有效期·

    public List<String> material;		 // 原料 多个, 隔开
    public List<String> goodsType;		 // 主营类型  多个, 隔开
    public String materials;             // 原料 多个, 隔开 ·
    public String goodsTypes;            // 主营类型  多个, 隔开·
    public String buyerAdminId;          // 营销顾问 id ·

}
