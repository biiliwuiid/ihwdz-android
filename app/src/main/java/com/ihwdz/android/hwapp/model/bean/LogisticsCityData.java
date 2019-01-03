package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */
public class LogisticsCityData {
    public String code;
    public String msg;
    public AddressList data;

    public static class CityEntity{
        boolean isSelected = false;
        public String text;
        public List<CityEntity> children;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public static class AddressList{
        public List<CityEntity> addressList;
    }
}
