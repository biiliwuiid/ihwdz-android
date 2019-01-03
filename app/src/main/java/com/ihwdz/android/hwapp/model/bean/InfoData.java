package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :  the properties can less or more than the json result,adjust it depend on your need
 * version: 1.0
 * </pre>
 */
public class InfoData {

    public String code;
    public String msg;
    private InfoDay data;

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

    public InfoDay getData() {
        return data;
    }

    public void setData(InfoDay data) {
        this.data = data;
    }

    public static class InfoDay{
        String day = "";
        String yesterday = "";
        String systemTime = "";
        List<NewsModel> newsFastList;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getYesterday() {
            return yesterday;
        }

        public void setYesterday(String yesterday) {
            this.yesterday = yesterday;
        }

        public String getSystemTime() {
            return systemTime;
        }

        public void setSystemTime(String systemTime) {
            this.systemTime = systemTime;
        }

        public List<NewsModel> getNewsFastList() {
            return newsFastList;
        }

        public void setNewsFastList(List<NewsModel> newsFastList) {
            this.newsFastList = newsFastList;
        }
        public void addNewsFastList(List<NewsModel> newsFastList) {
            this.newsFastList.addAll(newsFastList) ;
        }
    }

    public static class NewsModel{

        private boolean isOpen = false;
        private boolean isItem = true;
        private String today = "";

        private String id = "";
        private String catId = "";    //
        private String catName = "";    //
        private String title = "";    //
        private String author = "";    //
        private String source = "";
        private String keywords = "";
        private String dateTimeStr = "";    //
        private String shorDate = "";    //
//        private String monthDate = "";    //
        private String shorTime = "";    //
        private String articleImg = "";    //
        private String content = "";    //
        private String pdfUrl = "";    //
        private String pptxUrl = "";    //
        private String xlsxUrl = "";    //
        private String viewTimes = "";    //
        private String likeTimes = "";    //
        private String recommends = "";    //

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        public boolean isItem() {
            return isItem;
        }

        public void setItem(boolean item) {
            isItem = item;
        }

        public String getToday() {
            return today;
        }

        public void setToday(String today) {
            this.today = today;
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

//        public String getMonthDate() {
//            return monthDate;
//        }
//
//        public void setMonthDate(String monthDate) {
//            this.monthDate = monthDate;
//        }

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

        public String getRecommends() {
            return recommends;
        }

        public void setRecommends(String recommends) {
            this.recommends = recommends;
        }
    }

}
