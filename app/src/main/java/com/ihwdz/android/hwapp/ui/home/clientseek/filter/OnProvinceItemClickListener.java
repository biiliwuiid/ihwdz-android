package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import com.ihwdz.android.hwapp.model.bean.ProvinceData;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/29
 * desc :
 * version: 1.0
 * </pre>
 */
public interface OnProvinceItemClickListener {
    void onProvinceItemClicked(ProvinceData.Bean province, List<ProvinceData.Bean> cityList);
}
