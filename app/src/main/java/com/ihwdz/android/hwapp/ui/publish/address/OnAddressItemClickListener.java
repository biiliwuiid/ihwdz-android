package com.ihwdz.android.hwapp.ui.publish.address;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :  收货地址点击事件
 * version: 1.0
 * </pre>
 */
public interface OnAddressItemClickListener {

    // 点击选中该条
    interface OnItemClickListener {
        void onItemClicked();
    }

    // 点击编辑
    interface OnEditClickListener{
        void onEditClicked(int apply, String id); // apply == 1: 审核中
    }

}
