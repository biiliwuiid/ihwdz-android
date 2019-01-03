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
public class FactoryPriceEntity {

    private List<FactoryPriceModel> productPrices;
    private List<String> brands;
    private List<String> specs;
    private List<String> breeds;
    private List<String> regions;

    private String page;
    private String pageSize;
    private String count;
    private String pageNum;

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
