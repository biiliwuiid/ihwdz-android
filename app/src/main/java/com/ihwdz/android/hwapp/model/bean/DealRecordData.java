package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :  交易记录
 * version: 1.0
 * </pre>
 */
public class DealRecordData {
    public String code;
    public String msg;
    public RecordEntity data;

    public static class RecordEntity{
        public int currentPage;
        public int numPerPage;
        public int totalCount;
        public List<RecordBean> recordList;
        public int pageCount;
        public int beginPageIndex;
        public int endPageIndex;
        public String countResultMap;
    }

    public static class RecordBean{
        public String orderId;
        public String saleSumQty;
        public String saleSumAmt;
        public String subStatus;
        public List<OrderItem> orderItems;
    }

    public static class OrderItem{
        public String breed;
        public String spec;
        public String brand;
        public String qty;
        public String salePrice;
    }
}
