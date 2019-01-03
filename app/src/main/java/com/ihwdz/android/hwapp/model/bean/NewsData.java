package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/30
 * desc :
 * version: 1.0
 * </pre>
 */
public class NewsData {

    public String code;
    public String msg;
    public NewsEntity data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NewsEntity getData() {
        return data;
    }

    public void setData(NewsEntity data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class BottomNews{
        public NewsEntity bottomNews;
    }

    public static class NewsEntity {
        public int currentPage;
        public int numPerPage;
        public int totalCount;
        //public List<NewsModel> bottomNews;
        public List<NewsModel> recordList;
        public int pageCount;
        public int beginPageIndex;
        public int endPageIndex;
        public String countResultMap;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getNumPerPage() {
            return numPerPage;
        }

        public void setNumPerPage(int numPerPage) {
            this.numPerPage = numPerPage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<NewsModel> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<NewsModel> recordList) {
            this.recordList = recordList;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getBeginPageIndex() {
            return beginPageIndex;
        }

        public void setBeginPageIndex(int beginPageIndex) {
            this.beginPageIndex = beginPageIndex;
        }

        public int getEndPageIndex() {
            return endPageIndex;
        }

        public void setEndPageIndex(int endPageIndex) {
            this.endPageIndex = endPageIndex;
        }

        public String getCountResultMap() {
            return countResultMap;
        }

        public void setCountResultMap(String countResultMap) {
            this.countResultMap = countResultMap;
        }
    }


    /**
     * used in collections ; messages;
     */
    public static class NewsModel{
        public String id = "";
        public String catId = "";    //
        public String catName = "";    //
        public String title = "";    //
        public String author = "";    //
        public String source = "";
        public String keywords = "";
        public String dateTimeStr = "";    //
        public String shorDate = "";    //
        public String monthDate = "";    //
        public String shorTime = "";    //
        public String articleImg = "";    //
        public String content = "";    //
        public String pdfUrl = "";    //
        public String pptxUrl = "";    //
        public String xlsxUrl = "";    //
        public String viewTimes = "";    //
        public String likeTimes = "";    //

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


}
