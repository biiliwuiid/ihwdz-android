package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/20
 * desc :   选中某个仓库时，记录仓库信息
 * version: 1.0
 * </pre>
 */
public interface OnItemClickListener {
    void onItemClicked(String warehouseJsonInfo, String warehouseName);
}
