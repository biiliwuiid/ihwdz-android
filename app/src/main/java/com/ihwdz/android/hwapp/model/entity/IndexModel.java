package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class IndexModel {

    private String breed = "";       // 产品名称
    private String priceIndex = "";  // the color of "priceIndex" is the same with "upDown"
    private String rate = "";
    private String upNum = "";
    private String flatNum = "";
    private String downNum = "";
    private String upDown = "";      // 正数 up_red_arrow ; 负数 down_green_arrow

    public IndexModel(){}

    public IndexModel(String breed, String priceIndex, String upDown) {
        this.breed = breed;
        this.priceIndex = priceIndex;
        this.upDown = upDown;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getPriceIndex() {
        return priceIndex;
    }

    public void setPriceIndex(String priceIndex) {
        this.priceIndex = priceIndex;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpNum() {
        return upNum;
    }

    public void setUpNum(String upNum) {
        this.upNum = upNum;
    }

    public String getFlatNum() {
        return flatNum;
    }

    public void setFlatNum(String flatNum) {
        this.flatNum = flatNum;
    }

    public String getDownNum() {
        return downNum;
    }

    public void setDownNum(String downNum) {
        this.downNum = downNum;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }
}
