package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class RecommendModel {

    private String id = "";
    private String catId = "";
    private String catName = "";   //
    private String title = "";     //
    private String name = "";      //  no
    //private String cardId = "";
    private String author = "";    //
    private String source = "";
    private String keywords = "";
    private String dateTimeStr = "";

    private String shorDate = "";  //
    private String monthDate = "";  //
    //private String shortTime = "";  //
    private String shorTime = "";  //
    private String articleImg = "";
    private String description = "";
    private String content = "";
    private String pdfUrl = "";
    private String pptxUrl = "";
    private String xlsxUrl = "";
    private String viewTimes = "";   //
    private String likeTimes = "";

    public RecommendModel(){}

    public RecommendModel(String title, String articleImg) {
        this.title = title;
        this.articleImg = articleImg;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(String viewTimes) {
        this.viewTimes = viewTimes;
    }

    public String getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(String likeTimes) {
        this.likeTimes = likeTimes;
    }
}
