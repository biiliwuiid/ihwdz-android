package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */
public class HomePageNews {

    public String code = "0";
    public String msg = "OK";
    public HomePageNewsEntity data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HomePageNewsEntity getData() {
        return data;
    }

    public void setData(HomePageNewsEntity data) {
        this.data = data;
    }


}
