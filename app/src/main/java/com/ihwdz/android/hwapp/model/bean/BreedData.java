package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/28
 * desc :
 * version: 1.0
 * </pre>
 */
public class BreedData {

    public String code;
    public String msg;
    public BreedEntity data;

    public static class BreedEntity{
        public List<String> breeds;
        public List<String> provinces;
    }

    // 可选中的选项卡
    public static class CheckableItem{
        public String name;
        public boolean isChecked = false;
    }
}
