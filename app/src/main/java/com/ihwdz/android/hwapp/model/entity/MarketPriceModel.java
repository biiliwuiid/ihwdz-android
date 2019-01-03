package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class MarketPriceModel {

    private String baseId = "";
    private String brand = "";
    private String breed = "";   //
    private String dateTimeStr = "";      //
    private String price = "";
    private String priceCondition = "";
    private String resourceId = "";  //
    private String spec = "";
    private String unit = "";
    private String upDown = "";
    private String wareArea = "";   //
    private String wareCity = "";
    private String wareProvince = "";

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getPriceCondition() {
        return priceCondition;
    }

    public void setPriceCondition(String priceCondition) {
        this.priceCondition = priceCondition;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public String getWareArea() {
        return wareArea;
    }

    public void setWareArea(String wareArea) {
        this.wareArea = wareArea;
    }

    public String getWareCity() {
        return wareCity;
    }

    public void setWareCity(String wareCity) {
        this.wareCity = wareCity;
    }

    public String getWareProvince() {
        return wareProvince;
    }

    public void setWareProvince(String wareProvince) {
        this.wareProvince = wareProvince;
    }
}
