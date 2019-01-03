package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public class ProvinceData {

    public String code;
    public String msg;
    public List<ProvinceEntity> data;

    public static class ProvinceEntity{
        public boolean isSelected = false;
        public Bean province;
        public List<Bean> city;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public Bean getProvince() {
            return province;
        }

        public void setProvince(Bean province) {
            this.province = province;
        }

        public List<Bean> getCity() {
            return city;
        }

        public void setCity(List<Bean> city) {
            this.city = city;
        }
    }

    public static class Bean{
        public boolean isSelected = false;
        public String name;
        public String code;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class City{
        public boolean isSelected = false;
        public Bean mCity;
        public Bean mProvince;
    }


}
