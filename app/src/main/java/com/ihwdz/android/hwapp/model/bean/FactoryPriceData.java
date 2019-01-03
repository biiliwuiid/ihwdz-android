package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/05
 * desc :   查价格 - 出厂价
 * version: 1.0
 * </pre>
 */
public class FactoryPriceData {
    public String code;
    public String msg;
    public FactoryPriceEntity data;

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

    public static class FactoryPriceEntity{
        public List<FactoryPriceModel> productPrices;
        public List<String> brands;
        public List<String> specs;
        public List<String> breeds;
        public List<String> regions;

        public String page;
        public String pageSize;
        public String count;
        public String pageNum;

        public List<FactoryPriceModel> getProductPrices() {
            return productPrices;
        }

        public void setProductPrices(List<FactoryPriceModel> productPrices) {
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

        public List<String> getBreeds() {
            return breeds;
        }

        public void setBreeds(List<String> breeds) {
            this.breeds = breeds;
        }

        public List<String> getRegions() {
            return regions;
        }

        public void setRegions(List<String> regions) {
            this.regions = regions;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
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

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }
    }

    public static class FactoryPriceModel{
        public String baseId = "";
        public String brand = "";
        public String breed = "";   //
        public String city = "";     //
        public String dateTimeStr = "";      //  no
        public String price = "";
        public String priceCondition = "";
        public String province = "";  //
        public String region = "";  //
        public String resourceId = "";  //
        public String spec = "";
        public String unit = "";
        public String upDown = "";

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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
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
    }
}
