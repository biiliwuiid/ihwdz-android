package com.ihwdz.android.hwapp.ui.purchase;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc : 求购池 商家会员 报价 监听
 * version: 1.0
 * </pre>
 */
public interface OnQuoteBtClickListener {
    void onQuoteBtClicked(boolean isQuoteEnable, String id,String status,String breed,String qty, String address, String dateStr);
}
