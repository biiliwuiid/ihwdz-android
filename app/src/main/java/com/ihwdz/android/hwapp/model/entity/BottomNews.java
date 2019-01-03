package com.ihwdz.android.hwapp.model.entity;


import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */
public class BottomNews {

    private String currentPage;
    private String numPerPage;
    private String totalCount;
    private List<NewsModel> recordList;
    private String pageCount;
    private String beginPageIndex;
    private String endPageIndex;
    private String countResultMap;

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(String numPerPage) {
        this.numPerPage = numPerPage;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<NewsModel> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<NewsModel> recordList) {
        this.recordList = recordList;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getBeginPageIndex() {
        return beginPageIndex;
    }

    public void setBeginPageIndex(String beginPageIndex) {
        this.beginPageIndex = beginPageIndex;
    }

    public String getEndPageIndex() {
        return endPageIndex;
    }

    public void setEndPageIndex(String endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

    public String getCountResultMap() {
        return countResultMap;
    }

    public void setCountResultMap(String countResultMap) {
        this.countResultMap = countResultMap;
    }
}
