package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/19
 * desc :  仓库信息
 * version: 1.0
 * </pre>
 */
public class WarehouseData {

    public String code;
    public String msg;
    public List<Warehouse> data;    // 仓库

    // 仓库信息
    public static class Warehouse{
         public Long id = 0l;	            // 仓库id （搜索出的仓库 有ID）

        public String  companyShortName = ""; // 仓库名称
        public List<String>  aliasList ;      // 仓库别名（展示时,隔开）


        public String  addressProvinceCode;
        public String  addressProvinceName;

        public String  addressCityCode;
        public String  addressCityName;

        public String  addressDistrictCode;
        public String  addressDistrictName;

        public String address;          // 仓库地址
        public String mobile;			// 仓库联系电话
        public String name;				// 仓库联系人

    }

    // 报价上传 仓库信息 ->json  & 历史记录
    public static class WarehouseForQuotePost{
        public Long id;                  	// 仓库id ( 新添加的仓库没有ID ; )
        public String warehouse = "";		// 仓库名称

        public String provinceCode = "";	// 省code
        public String province = "";		// 省

        public String cityCode = "";		// 城市code
        public String city = "";			// 城市

        public String districtCode = "";	// 区code
        public String district = "";		// 区

        public String warehouseAddress = "";	// 仓库地址
        public String warehousePhone = "";		// 仓库联系电话
        public String warehouseContact = "";	// 仓库联系人

    }


    public static class WarehouseCheckData{
        public String code;
        public String msg;
        public Warehouse data;    // 仓库
    }

}
