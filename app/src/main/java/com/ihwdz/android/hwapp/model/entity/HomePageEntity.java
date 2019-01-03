package com.ihwdz.android.hwapp.model.entity;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class HomePageEntity {

    public List<BannerModel> ads;            // ads
    public List<IndexModel> indexes;         // indexes
    public List<Hw24hModel> tewntyFourHours; // tewntyFourHours  (should be twentyFourHours)
    public List<RecommendModel> todayTop;    // todayTop
    public List<CardModel> prices;           // prices


    public HomePageEntity(){}

    public HomePageEntity(JsonArray asJsonArray){}

    public HomePageEntity(List<BannerModel> bannerModelList, List<IndexModel> indexModelList, List<Hw24hModel> hw24hModelList, List<RecommendModel> recommendModelList, List<CardModel> cardModelList) {
//        this.ads = new ArrayList<>();
//        this.indexes = new ArrayList<>();
//        this.tewntyFourHours = new ArrayList<>();
//        this.todayTop = new ArrayList<>();
//        this.prices = new ArrayList<>();

        this.ads = bannerModelList;
        this.indexes = indexModelList;
        this.tewntyFourHours = hw24hModelList;
        this.todayTop = recommendModelList;
        this.prices = cardModelList;
    }

    public List<BannerModel> getAds() {
        return ads;
    }

    public void setAds(List<BannerModel> ads) {
        this.ads = ads;
    }

    public List<IndexModel> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<IndexModel> indexes) {
        this.indexes = indexes;
    }

    public List<Hw24hModel> getTewntyFourHours() {
        return tewntyFourHours;
    }

    public void setTewntyFourHours(List<Hw24hModel> tewntyFourHours) {
        this.tewntyFourHours = tewntyFourHours;
    }

    public List<RecommendModel> getTodayTop() {
        return todayTop;
    }

    public void setTodayTop(List<RecommendModel> todayTop) {
        this.todayTop = todayTop;
    }

    public List<CardModel> getPrices() {
        return prices;
    }

    public void setPrices(List<CardModel> prices) {
        this.prices = prices;
    }

}
