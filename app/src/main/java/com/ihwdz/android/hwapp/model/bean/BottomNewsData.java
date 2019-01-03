package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/31
 * desc :  BottomNews _info_day
 * version: 1.0
 * </pre>
 */
public class BottomNewsData {


    public String code;
    public String msg;
    public BottomNews data;

    public static class BottomNews{
        public NewsEntity bottomNews;
    }

    public static class NewsEntity {
        public int currentPage;
        public int numPerPage;
        public int totalCount;
        public List<NewsModel> recordList;
        public int pageCount;
        public int beginPageIndex;
        public int endPageIndex;
        public String countResultMap;
    }


    /**
     * used in BottomNews _info_day;
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
    }

}
