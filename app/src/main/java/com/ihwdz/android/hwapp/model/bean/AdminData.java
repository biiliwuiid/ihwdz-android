package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :
 * version: 1.0
 * </pre>
 */
public class AdminData {

    public String code;
    public String msg;
    public List<AdminEntity> data;

    public static class AdminEntity{
        public String name;
        public String id;
    }
}
