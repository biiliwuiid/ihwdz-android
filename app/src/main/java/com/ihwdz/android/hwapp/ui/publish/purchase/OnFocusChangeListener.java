package com.ihwdz.android.hwapp.ui.publish.purchase;


import android.view.View;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/26
 * desc : 联系 PurchaseActivity 和 AddViewAdapter(的每个DropEditText)
 * version: 1.0
 * </pre>
 */
public interface OnFocusChangeListener {

    interface onPopupWindowShow{
        void onPopupWindowShow(View view);
    }

     interface onBreedFocusChanged{
        void onTextChanged(String s);
    }

     interface onSpecFocusChanged{
        void onTextChanged(String s);
    }

     interface onFactoryFocusChanged{
        void onTextChanged(String s);
    }


}
