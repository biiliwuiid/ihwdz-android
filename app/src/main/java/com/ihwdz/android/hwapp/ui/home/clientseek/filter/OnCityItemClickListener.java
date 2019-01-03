package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import com.ihwdz.android.hwapp.model.bean.ProvinceData;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/29
 * desc :
 * version: 1.0
 * </pre>
 */
public interface OnCityItemClickListener {

    void onItemSelected(ProvinceData.City city);
    void onItemUnSelected(ProvinceData.City city);

}
