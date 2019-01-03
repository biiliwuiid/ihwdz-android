package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/26
 * desc :
 * version: 1.0
 * </pre>
 */
public class CardModel {

    private String breed = "";
    private String spec = "";
    private String price = "";
    private String unit = "";
    private String dayRate = "";
    private String weekRate = "";
    private String area = "";
    private String dateTimerStr = "";

    public CardModel(){}
    public CardModel(String dataTimerStr, String area, String breed, String price, String dayRate, String spec, String weekRate) {
        this.dateTimerStr = dataTimerStr;
        this.area = area;
        this.breed = breed;
        this.price = price;
        this.dayRate = dayRate;
        this.spec = spec;
        this.weekRate = weekRate;
    }

    public CardModel(String breed, String spec, String price, String unit, String dayRate, String weekRate, String area, String dateTimerStr) {
        this.breed = breed;
        this.spec = spec;
        this.price = price;
        this.unit = unit;
        this.dayRate = dayRate;
        this.weekRate = weekRate;
        this.area = area;
        this.dateTimerStr = dateTimerStr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDateTimerStr() {
        return dateTimerStr;
    }

    public void setDateTimerStr(String dateTimerStr) {
        this.dateTimerStr = dateTimerStr;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDayRate() {
        return dayRate;
    }

    public void setDayRate(String dayRate) {
        this.dayRate = dayRate;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getWeekRate() {
        return weekRate;
    }

    public void setWeekRate(String weekRate) {
        this.weekRate = weekRate;
    }
}
