package com.ihwdz.android.hwapp.model.entity;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class MarketPriceEntity {

    private List<MarketPriceModel> productPrices;
    private List<String> brands;
    private List<String> specs;
    private List<String> cities;
    private List<String> breeds;

    private String pageNum;
    private String pageSize;
    private String count;
    private String totalPageSize;

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
