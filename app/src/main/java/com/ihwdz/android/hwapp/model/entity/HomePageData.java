package com.ihwdz.android.hwapp.model.entity;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
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
public class HomePageData {

    private String code = "0";
    private String msg = "OK";
//    @SerializedName(value = "subjects")
    private HomePageEntity data;

    public HomePageData() {
    }
    public HomePageData(JsonArray asJsonArray) {
    }


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

    public HomePageEntity getData() {
        return data;
    }

    public void setData(HomePageEntity data) {
        this.data = data;
    }

    //////////////////////////////
    // json array
    static public List<HomePageData> parseDataList(JsonReader reader) throws IOException {
        List<HomePageData> jobDefinitionList = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()) {
            HomePageData data = parseData(reader);
            jobDefinitionList.add(data);
        }
        reader.endArray();
        return jobDefinitionList;
    }

    // json object
    static public HomePageData parseData(JsonReader reader) throws IOException{
        HomePageData homePageData = new HomePageData();
        reader.beginObject();
        String name;

        while(reader.hasNext()){
            name = reader.nextName();
            switch (name){
                case "code":
                    homePageData.code = reader.nextString();
                    break;
                case "msg":
                    homePageData.msg = reader.nextString();
                    break;
                case "data":
                    homePageData.data = new HomePageEntity();
                    HomePageEntity homePageEntity = new HomePageEntity();
                    reader.beginObject();
                    while(reader.hasNext()) {
                        name = reader.nextName();
                        switch (name){

                            case "ads":
                                homePageEntity.ads = new ArrayList<>();
                                reader.beginArray();
                                while(reader.hasNext()) {
                                    reader.beginObject();
                                    BannerModel bannerModel = new BannerModel();
                                    while(reader.hasNext()) {
                                        name = reader.nextName();
                                        switch (name) {
                                            case "id":
                                                bannerModel.id = reader.nextString();
                                                break;
                                            case "siteId":
                                                bannerModel.siteId = reader.nextString();
                                                break;
                                            case "sort":
                                                bannerModel.sort = reader.nextString();
                                                break;
                                            case "link":
                                                bannerModel.link = reader.nextString();
                                                break;
                                            case "background":
                                                bannerModel.background = reader.nextString();
                                                break;
                                            case "note":
                                                bannerModel.note = reader.nextString();
                                                break;
                                            case "picUrl":
                                                bannerModel.picUrl = reader.nextString();
                                                break;
                                            default:
                                                reader.skipValue();
                                                break;
                                        }

                                    }
                                    homePageEntity.ads.add(bannerModel);
                                    reader.endObject();

                                }
                                reader.endArray();
                                break;



                            case "indexes":
                                homePageEntity.indexes = new ArrayList<>();
                                reader.beginArray();
                                while(reader.hasNext()) {
                                    reader.beginObject();
                                    IndexModel model = new IndexModel();
                                    while(reader.hasNext()) {
                                        name = reader.nextName();
                                        switch (name) {
                                            case "breed":
                                                model.setBreed(reader.nextString());
                                                break;
                                            case "siteId":
                                                model.setPriceIndex(reader.nextString());
                                                break;
                                            case "sort":
                                                model.setRate(reader.nextString());
                                                break;
                                            case "link":
                                                model.setUpNum(reader.nextString());
                                                break;
                                            case "background":
                                                model.setFlatNum(reader.nextString());
                                                break;
                                            case "note":
                                                model.setDownNum(reader.nextString());
                                                break;
                                            case "picUrl":
                                                model.setUpDown(reader.nextString());
                                                break;
                                            default:
                                                reader.skipValue();
                                                break;
                                        }

                                    }
                                    homePageEntity.indexes.add(model);
                                    reader.endObject();

                                }
                                reader.endArray();
                                break;
                            case "tewntyFourHours":
                                homePageEntity.tewntyFourHours = new ArrayList<>();
                                reader.beginArray();
                                while(reader.hasNext()) {
                                    reader.beginObject();
                                    Hw24hModel model = new Hw24hModel();
                                    while(reader.hasNext()) {
                                        name = reader.nextName();
                                        switch (name) {
                                            case "id":
                                                model.setId(reader.nextString());
                                                break;
                                            case "catId":
                                                model.setCatId(reader.nextString());
                                                break;
                                            case "catName":
                                                model.setCatName(reader.nextString());
                                                break;
                                            case "title":
                                                model.setTitle(reader.nextString());
                                                break;
                                            case "author":
                                                model.setAuthor(reader.nextString());
                                                break;
                                            case "source":
                                                model.setSource(reader.nextString());
                                                break;
                                            case "keywords":
                                                model.setKeywords(reader.nextString());
                                                break;
                                            case "dateTimeStr":
                                                model.setDateTimeStr(reader.nextString());
                                                break;
                                            case "shorDate":
                                                model.setShorDate(reader.nextString());
                                                break;
                                            case "monthDate":
                                                model.setMonthDate(reader.nextString());
                                                break;
                                            case "articleImg":
                                                model.setArticleImg(reader.nextString());
                                                break;
                                            case "description":
                                                model.setDescription(reader.nextString());
                                                break;
                                            case "content":
                                                model.setContent(reader.nextString());
                                                break;
                                            case "pdfUrl":
                                                model.setPdfUrl(reader.nextString());
                                                break;
                                            case "pptxUrl":
                                                model.setPptxUrl(reader.nextString());
                                                break;
                                            case "xlsxUrl":
                                                model.setXlsxUrl(reader.nextString());
                                                break;
                                            case "viewTimes":
                                                model.setViewTimes(reader.nextString());
                                                break;
                                            case "likeTimes":
                                                model.setLikeTimes(reader.nextString());
                                                break;
                                            default:
                                                reader.skipValue();
                                                break;
                                        }

                                    }
                                    homePageEntity.tewntyFourHours.add(model);
                                    reader.endObject();

                                }
                                reader.endArray();
                                break;
                            case "todayTop":
                                homePageEntity.todayTop = new ArrayList<>();
                                reader.beginArray();
                                while(reader.hasNext()) {
                                    reader.beginObject();
                                    RecommendModel model = new RecommendModel();
                                    while(reader.hasNext()) {
                                        name = reader.nextName();
                                        switch (name) {
                                            case "id":
                                                model.setId(reader.nextString());
                                                break;
                                            case "catId":
                                                model.setCatId(reader.nextString());
                                                break;
                                            case "catName":
                                                model.setCatName(reader.nextString());
                                                break;
                                            case "title":
                                                model.setTitle(reader.nextString());
                                                break;
                                            case "author":
                                                model.setAuthor(reader.nextString());
                                                break;
                                            case "source":
                                                model.setSource(reader.nextString());
                                                break;
                                            case "keywords":
                                                model.setKeywords(reader.nextString());
                                                break;
                                            case "dateTimeStr":
                                                model.setDateTimeStr(reader.nextString());
                                                break;
                                            case "shorDate":
                                                model.setShorDate(reader.nextString());
                                                break;
                                            case "monthDate":
                                                model.setMonthDate(reader.nextString());
                                                break;
                                            case "shorTime":
                                                model.setShorTime(reader.nextString());
                                                break;
                                            case "articleImg":
                                                model.setArticleImg(reader.nextString());
                                                break;
                                            case "description":
                                                model.setDescription(reader.nextString());
                                                break;
                                            case "content":
                                                model.setContent(reader.nextString());
                                                break;
                                            case "pdfUrl":
                                                model.setPdfUrl(reader.nextString());
                                                break;
                                            case "pptxUrl":
                                                model.setPptxUrl(reader.nextString());
                                                break;
                                            case "xlsxUrl":
                                                model.setXlsxUrl(reader.nextString());
                                                break;
                                            case "viewTimes":
                                                model.setViewTimes(reader.nextString());
                                                break;
                                            case "likeTimes":
                                                model.setLikeTimes(reader.nextString());
                                                break;
                                            default:
                                                reader.skipValue();
                                                break;
                                        }

                                    }
                                    homePageEntity.todayTop.add(model);
                                    reader.endObject();

                                }
                                reader.endArray();
                                break;
                            case "prices":
                                homePageEntity.prices = new ArrayList<>();
                                reader.beginArray();
                                while(reader.hasNext()) {
                                    reader.beginObject();
                                    CardModel model = new CardModel();
                                    while(reader.hasNext()) {
                                        name = reader.nextName();
                                        switch (name) {
                                            case "breed":
                                                model.setBreed(reader.nextString());
                                                break;
                                            case "spec":
                                                model.setSpec(reader.nextString());
                                                break;
                                            case "price":
                                                model.setPrice(reader.nextString());
                                                break;
                                            case "unit":
                                                model.setUnit(reader.nextString());
                                                break;
                                            case "dayRate":
                                                model.setDayRate(reader.nextString());
                                                break;
                                            case "weekRate":
                                                model.setWeekRate(reader.nextString());
                                                break;
                                            case "area":
                                                model.setArea(reader.nextString());
                                                break;
                                            case "dateTimerStr":
                                                model.setDateTimerStr(reader.nextString());
                                                break;
                                            default:
                                                reader.skipValue();
                                                break;
                                        }

                                    }
                                    homePageEntity.prices.add(model);
                                    reader.endObject();

                                }
                                homePageData.data = homePageEntity;
                                reader.endArray();
                                break;

                        }

                    }
                    break;

                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return homePageData;
    }
}
