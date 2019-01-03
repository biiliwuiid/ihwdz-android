package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/31
 * desc :  详情页　热门推荐、　点赞
 * version: 1.0
 * </pre>
 */
public class CommentData {

    public String code;
    public String msg;
    private NewsEntity data;

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

    public NewsEntity getData() {
        return data;
    }

    public void setData(NewsEntity data) {
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
        private String articleId = "";
        private String commentId = "";    //
        private String content = "";    //
        private String dateTimeStr = "";    //
        private String likeTimes = "";    //
        private String useName = "";    //
        private boolean isThumb = false; //　点赞标记

        public boolean isThumb() {
            return isThumb;
        }

        public void setThumb(boolean thumb) {
            isThumb = thumb;
        }

        private List<NewsModel> twoArticleCommentList;

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDateTimeStr() {
            return dateTimeStr;
        }

        public void setDateTimeStr(String dateTimeStr) {
            this.dateTimeStr = dateTimeStr;
        }

        public String getLikeTimes() {
            return likeTimes;
        }

        public void setLikeTimes(String likeTimes) {
            this.likeTimes = likeTimes;
        }

        public String getUseName() {
            return useName;
        }

        public void setUseName(String useName) {
            this.useName = useName;
        }

        public List<NewsModel> getTwoArticleCommentList() {
            return twoArticleCommentList;
        }

        public void setTwoArticleCommentList(List<NewsModel> twoArticleCommentList) {
            this.twoArticleCommentList = twoArticleCommentList;
        }
    }
}
