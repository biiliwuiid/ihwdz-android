package com.ihwdz.android.hwapp.ui.orders.filter;

import com.ihwdz.android.hwapp.model.bean.CityResultBean;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;


/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */
public interface OnItemClickListener {
    void onItemClicked(LogisticsCityData.CityEntity data);
    void onItemClicked(CityResultBean data);
}
