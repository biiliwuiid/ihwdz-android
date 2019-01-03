package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/24
 * desc :   收藏的价格数据
 * version: 1.0
 * </pre>
 */
public class PriceCollectionData {

    public String code;
    public String msg;
    public PriceEntity data;

    public static class PriceEntity {
        public List<PriceModel> recordList;
        public int pageCount;
    }

    public static class PriceModel{
        public String token;
        public int collectionType;    // 0 市场价 1 出厂价
        public String price;
        public String upDown ;
        public String dateTimeStr ;

        public String breed;
        public String spec;
        public String brand;
        public String area;
    }

}
