package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/25
 * desc :
 * version: 1.0
 * </pre>
 */
public class ClientDetailData {

    public String code;
    public String msg;
    public ClientDetailEntity data;

    public static class ClientDetailEntity{

        public String address;                // 地址
        public String city;
        public String cityCode;
        public String companyBusinessVolume;
        public String companyCreatedTimeStr;  // 注册时间          ----------工商信息3
        public String companyDesc;            // 公司简介  （null -> hide）
        public String companyEmployeeNumber;  // 公司规模          ----------工商信息1
        public String companyFax;
        public String companyMajor;           // 经营范围  （null -> hide）
        public String companyName;
        public String companyRank;            // STAR
        public String companyRegisterMony;    // 注册资本 + 万元    ----------工商信息2
        public String companyWebsite;
        public String companyWorkshopArea;
        public String follow;                 // "0"-取消关注(blank star - un_follow); "1" - 添加关注;(white star - followed)
        public String linker;                 //
        public String linkerEmail;            //
        public String linkerMobile;           //

        public String province;
        public String provinceCode;

       // 显示时排列顺序如下                               // 工商信息
        public PlantVO plantVO;                            // 生产信息
        public List<MaterialVOs> materialVOs;              // 使用原料
        public List<DeviceVOs> deviceVOs;                  // 设备清单     （null -> hide）
        public List<MemberAttachment> memberAttachment;    // 产品设备展示 （null -> hide）
    }

    // 生产信息
    public static class PlantVO{
        public String factoryArea;    // 厂房面积
        public String kgRate;         // 机器开工率
        public String marketFlow;     // 市场流向
        public String materialType;   // 原料类型
        public String productTpe;     // 产品类型
        public String yearSaleAmount; // 年销售额
    }

    // 使用原料
    public static class MaterialVOs{
        public long createTime;
        public int creatorId;
        public int id;
        public long lastAccess;
        public String materialGrade;           // 常用型号
        public String materialManufactory;     // 常用商家
        public String materialName;            // 常用品种
        public String materialQuantity;
        public int memberId;
        public int version;
    }

    // 设备清单
    public static class DeviceVOs{
        public long createTime;
        public int creatorId;
        public String deviceBrand;     // 品牌
        public String deviceGrade;     // 型号
        public String deviceName;      // 设备名称
        public String deviceQuantity;  // 设备数量
        public int id;
        public long lastAccess;
        public int memberId;
        public String performanceIndex;
        public String purchaseDate;     // 购买日期
        public int version;
    }

    // 产品设备展示
    public static class MemberAttachment{
        public String attachmentName;    // 图片名称
        public String attachmentPath;    // 图片地址
        public String attachmentType;
        public String createTime;
        public String creatorId;
        public String id;
        public String lastAccess;
        public String memberId;
        public String version;
    }
}
