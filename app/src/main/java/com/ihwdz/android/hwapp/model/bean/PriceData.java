package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/15
 * desc :   查价格
 * version: 1.0
 * </pre>
 */
public class PriceData {

    public String code;
    public String msg;
    public PriceEntity data;

    public static class PriceEntity {

        public List<PriceModel> productPrices;
        public List<String> brands;
        public List<String> specs;
        public List<String> cities;   // market_price
        public List<String> regions;  // factory_price
        public List<String> breeds;
        public String pageNum;
        public String pageSize;
        public String count;
        public String totalPageSize;
        public String dateTimeStr;    // 按日期查询价格
    }

    public static class PriceModel{

        public String wareArea;     // market_price
        public String wareCity;     // market_price
        public String wareProvince ; // market_price

        public String province;     // factory_price
        public String region;       // factory_price
        public String city;         // factory_price


        ///// 共有字段
        public String baseId = "";
        public String brand = "";
        public String breed = "";
        public String collection = "";  // "0": 未收藏； "1"：收藏；
        public String dateTimeStr = ""; //  no
        public String price = "";
        public String priceCondition = "";
        public String resourceId = "";
        public String spec = "";
        public String unit = "";
        public String upDown = "";




    }

    // 可选中的菜单选项卡
    public static class CheckableItem{
        public String name;
        public boolean isChecked = false;
    }

}
