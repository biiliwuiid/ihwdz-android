package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class IndexData {

    public String code;
    public String msg;
    public List<IndexModel> data;

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

    public List<IndexModel> getData() {
        return data;
    }

    public void setData(List<IndexModel> data) {
        this.data = data;
    }

    public static class IndexModel{
        String baseId;
        String breed;
        String dateTimeStr;
        String price;
        String technology;
        String type;
        String upDown;

        public String getBaseId() {
            return baseId;
        }

        public void setBaseId(String baseId) {
            this.baseId = baseId;
        }

        public String getBreed() {
            return breed;
        }

        public void setBreed(String breed) {
            this.breed = breed;
        }

        public String getDateTimeStr() {
            return dateTimeStr;
        }

        public void setDateTimeStr(String dateTimeStr) {
            this.dateTimeStr = dateTimeStr;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTechnology() {
            return technology;
        }

        public void setTechnology(String technology) {
            this.technology = technology;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpDown() {
            return upDown;
        }

        public void setUpDown(String upDown) {
            this.upDown = upDown;
        }
    }
}
