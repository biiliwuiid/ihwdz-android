package com.ihwdz.android.hwapp.ui.purchase;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/13
 * desc :  求购池 我的求购/我的报价  item click listener
 * version: 1.0
 * </pre>
 */
public interface OnPurchaseItemClickListener {
    /**
     *
     * @param id
     * @param status
     * @param breed
     * @param qty
     * @param address
     * @param dateStr
     * @param isQuoteDetail 是否是报价详情（false 为 求购报价）
     */
    void onItemClicked(String id,String status,String breed,String qty, String address, String dateStr, boolean isQuoteDetail);
}
