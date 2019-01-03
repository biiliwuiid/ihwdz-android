package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class FactoryPriceData {

    private String code = "0";
    private String msg = "OK";
    private FactoryPriceEntity data;

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

    public FactoryPriceEntity getData() {
        return data;
    }

    public void setData(FactoryPriceEntity data) {
        this.data = data;
    }
}
