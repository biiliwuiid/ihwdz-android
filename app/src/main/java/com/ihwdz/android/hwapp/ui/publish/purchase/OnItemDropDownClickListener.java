package com.ihwdz.android.hwapp.ui.publish.purchase;

import android.view.View;

import com.ihwdz.android.hwapp.model.bean.PublishData;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/26
 * desc :  联系 DropEditText 和 AddViewAdapter(的每个DropEditText)
 * version: 1.0
 * </pre>
 */
public interface OnItemDropDownClickListener {

    // PopupWindow 弹出
    interface onPopupWindowShow{
        void onPopupWindowShow(View view);
    }

    // 点击 DropEditText 的下拉按钮 或 点击输入框
    void onItemDropDownClicked(String s);

    // 输入内容变化
    void onTextChanged(String s);

    // 选中某项
    void onItemSelected(PublishData.ProductEntity selectedItem);

}
