package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class MaterialData {

    public String code;
    public String msg;
    public MaterialEntity data;

    public static class MaterialEntity{

        public List<String> brands;
        public List<String> breeds;
        public List<String> cities;
        public List<String> specs;
        public PageNation pagenation;
        public String keyword;

    }

    public static class PageNation{
        public String beginPageIndex; //
        public String countResultMap; //
        public String currentPage;
        public String endPageIndex;
        public String numPerPage;
        public String pageCount;
        public String totalCount;
        public List<MaterialModel> recordList;

    }

    public static class MaterialModel{
        public double amount; //
        public String baseId;
        public String brand; //
        public String breed;  //
        public String createTime;
        public String dateTime;
        public String dateTimeStr;//
        public String id;
        public String indexKey;
        public String isOutDate;
        public String keyword;
        public String lastAccess;
        public String linker;
        public String linkerMobile;//
        public double price; //
        public int rank;  // if rank > 0
        public String source;
        public String spec;  //
        public String summaryCode;
        public String supplier; //
        public String supplierId;
        public String wareCity;//
        public String wareProvince;
    }

    // 可选中的菜单选项卡
    public static class CheckableItem{
        public String name;
        public boolean isChecked = false;
    }
}
