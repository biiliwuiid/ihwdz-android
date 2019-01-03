package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/02
 * desc :  查价格 - 市场价
 * version: 1.0
 * </pre>
 */
public class MarketPriceData {
    public String code;
    public String msg;
    public MarketPriceEntity data;

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

    public MarketPriceEntity getData() {
        return data;
    }

    public void setData(MarketPriceEntity data) {
        this.data = data;
    }

    public static class MarketPriceEntity{
        public List<MarketPriceModel> productPrices;
        public List<String> brands;
        public List<String> specs;
        public List<String> cities;
        public List<String> breeds;

        public String pageNum;
        public String pageSize;
        public String count;
        public String totalPageSize;

        public List<MarketPriceModel> getProductPrices() {
            return productPrices;
        }

        public void setProductPrices(List<MarketPriceModel> productPrices) {
            this.productPrices = productPrices;
        }

        public List<String> getBrands() {
            return brands;
        }

        public void setBrands(List<String> brands) {
            this.brands = brands;
        }

        public List<String> getSpecs() {
            return specs;
        }

        public void setSpecs(List<String> specs) {
            this.specs = specs;
        }

        public List<String> getCities() {
            return cities;
        }

        public void setCities(List<String> cities) {
            this.cities = cities;
        }

        public List<String> getBreeds() {
            return breeds;
        }

        public void setBreeds(List<String> breeds) {
            this.breeds = breeds;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getTotalPageSize() {
            return totalPageSize;
        }

        public void setTotalPageSize(String totalPageSize) {
            this.totalPageSize = totalPageSize;
        }
    }

    public static class MarketPriceModel{
        public String baseId = "";
        public String brand = "";
        public String breed = "";            //
        public String dateTimeStr = "";      //
        public String price = "";
        public String priceCondition = "";
        public String resourceId = "";       //
        public String spec = "";
        public String unit = "";
        public String upDown = "";
        public String wareArea = "";         //
        public String wareCity = "";
        public String wareProvince = "";

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


}
