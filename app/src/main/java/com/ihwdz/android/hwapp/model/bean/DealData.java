package com.ihwdz.android.hwapp.model.bean;


import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/10
 * desc :
 * version: 1.0
 * </pre>
 */
public class DealData {
    public String code;
    public String msg;
    private List<HomePageData.CardModel> data;

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

    public List<HomePageData.CardModel> getData() {
        return data;
    }

    public void setData(List<HomePageData.CardModel> data) {
        this.data = data;
    }
}
