package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class NewsModel {

//    private String title = "";        //
//    private String author = "";       //
//    private String shortDate = "";    //
//    private String articleImg = "";   //
//    private String viewTimes = "";    //
//



    private String id = "";
    private String catId = "";    //
    private String catName = "";    //
    private String title = "";    //
    private String author = "";    //
    private String source = "";
    private String keywords = "";
    private String dateTimeStr = "";    //
    private String shorDate = "";    //
    private String monthDate = "";    //
    private String shorTime = "";    //
    private String articleImg = "";    //
    private String content = "";    //
    private String pdfUrl = "";    //
    private String pptxUrl = "";    //
    private String xlsxUrl = "";    //
    private String viewTimes = "";    //
    private String likeTimes = "";    //


    public NewsModel(){}

    public NewsModel(String viewTimes){
        this.viewTimes =  viewTimes;
        this.shorDate = "2018-07-20";
        this.title = "7月19日至28日，习近平主席将对阿联酋、塞内加尔、卢旺达和南非进行国事访问";
        this.articleImg = "";
        this.author = "Luffy";
    }

    public NewsModel(String title, String author, String shortDate, String articleImg, String viewTimes) {
        this.title = title;
        this.author = author;
        this.shorDate = shortDate;
        this.articleImg = articleImg;
        this.viewTimes = viewTimes;
    }

    public NewsModel(String id, String catId, String catName, String title, String author, String source, String keywords, String dateTimeStr, String shorDate, String monthDate, String shorTime, String articleImg, String content, String pdfUrl, String pptxUrl, String xlsxUrl, String viewTimes, String likeTimes) {
        this.id = id;
        this.catId = catId;
        this.catName = catName;
        this.title = title;
        this.author = author;
        this.source = source;
        this.keywords = keywords;
        this.dateTimeStr = dateTimeStr;
        this.shorDate = shorDate;
        this.monthDate = monthDate;
        this.shorTime = shorTime;
        this.articleImg = articleImg;
        this.content = content;
        this.pdfUrl = pdfUrl;
        this.pptxUrl = pptxUrl;
        this.xlsxUrl = xlsxUrl;
        this.viewTimes = viewTimes;
        this.likeTimes = likeTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDateTimeStr() {
        return dateTimeStr;
    }

    public void setDateTimeStr(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
    }

    public String getShorDate() {
        return shorDate;
    }

    public void setShorDate(String shorDate) {
        this.shorDate = shorDate;
    }

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public String getShorTime() {
        return shorTime;
    }

    public void setShorTime(String shorTime) {
        this.shorTime = shorTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPptxUrl() {
        return pptxUrl;
    }

    public void setPptxUrl(String pptxUrl) {
        this.pptxUrl = pptxUrl;
    }

    public String getXlsxUrl() {
        return xlsxUrl;
    }

    public void setXlsxUrl(String xlsxUrl) {
        this.xlsxUrl = xlsxUrl;
    }

    public String getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(String likeTimes) {
        this.likeTimes = likeTimes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortDate() {
        return shorDate;
    }

    public void setShortDate(String shortDate) {
        this.shorDate = shortDate;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public String getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(String viewTimes) {
        this.viewTimes = viewTimes;
    }
}
