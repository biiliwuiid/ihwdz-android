package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/19
 * desc :
 * version: 1.0
 * </pre>
 */
public class CityResultBean {

    public String value;
    public String label;
    public boolean isSelected = false;
    public List<CityResultBean> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<CityResultBean> getChildren() {
        return children;
    }

    public void setChildren(List<CityResultBean> children) {
        this.children = children;
    }

//    public static class CityBean{
//        public String value;
//        public String label;
//        public CityBean children;
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//        public String getLabel() {
//            return label;
//        }
//
//        public void setLabel(String label) {
//            this.label = label;
//        }
//
//        public CityBean getChildren() {
//            return children;
//        }
//
//        public void setChildren(CityBean children) {
//            this.children = children;
//        }
//    }

}
