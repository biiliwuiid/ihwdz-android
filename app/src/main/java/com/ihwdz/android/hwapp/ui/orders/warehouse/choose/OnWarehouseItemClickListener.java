package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

import com.ihwdz.android.hwapp.model.bean.WarehouseData;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/03
 * desc :
 * version: 1.0
 * </pre>
 */
public interface OnWarehouseItemClickListener {
    void onWarehouseClicked(WarehouseData.WarehouseForQuotePost warehouse);
}
