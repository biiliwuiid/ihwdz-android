package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/31
 * desc : 详情页　详情内容
 * version: 1.0
 * </pre>
 */
public class DetailData {

    public String code;
    public String msg;
    private NewsModel data;

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

    public NewsModel getData() {
        return data;
    }

    public void setData(NewsModel data) {
        this.data = data;
    }

    // data type
    public static class NewsEntity{
        private int currentPage;
        private int numPerPage;
        private int totalCount;
        private List<NewsModel> recordList;
        private int pageCount;
        private int beginPageIndex;
        private int endPageIndex;
        private String countResultMap;

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
     * could use in Hw24h list, Recommend list, BottomNews list
     */
    public static class NewsModel{
        private String articleImg = "";
        private String author = "";    //
        private String catName = "";    //
        private String content = "";    //
        private String dateTime = "";    //
        private String description = "";
        private String id = "";
        private String keywords = "";    //
        private String likeTimes = "";    //
        private String monthDate = "";    //
        private String pdfUrl = "";    //
        private String pptxUrl = "";    //
        private String recommends = "";    //
        private String shorDate = "";    //
        private String shorTime = "";    //
        private String source = "";    //
        private String title = "";    //
        private String viewTimes = "";    //
        private String xlsxUrl = "";    //


        public String getArticleImg() {
            return articleImg;
        }

        public void setArticleImg(String articleImg) {
            this.articleImg = articleImg;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getLikeTimes() {
            return likeTimes;
        }

        public void setLikeTimes(String likeTimes) {
            this.likeTimes = likeTimes;
        }

        public String getMonthDate() {
            return monthDate;
        }

        public void setMonthDate(String monthDate) {
            this.monthDate = monthDate;
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

        public String getRecommends() {
            return recommends;
        }

        public void setRecommends(String recommends) {
            this.recommends = recommends;
        }

        public String getShorDate() {
            return shorDate;
        }

        public void setShorDate(String shorDate) {
            this.shorDate = shorDate;
        }

        public String getShorTime() {
            return shorTime;
        }

        public void setShorTime(String shorTime) {
            this.shorTime = shorTime;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getViewTimes() {
            return viewTimes;
        }

        public void setViewTimes(String viewTimes) {
            this.viewTimes = viewTimes;
        }

        public String getXlsxUrl() {
            return xlsxUrl;
        }

        public void setXlsxUrl(String xlsxUrl) {
            this.xlsxUrl = xlsxUrl;
        }
    }
}
