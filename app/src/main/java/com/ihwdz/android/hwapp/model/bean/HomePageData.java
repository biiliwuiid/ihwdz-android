package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/06
 * desc :
 * version: 1.0
 * </pre>
 */
public class HomePageData {
    public String code;
    public String msg;
    public HomePageEntity data;

    public static class HomePageEntity{
        public List<BannerModel> ads;            // ads
        public List<IndexModel> indexes;         // indexes
        public List<Hw24hModel> tewntyFourHours; // tewntyFourHours  (should be twentyFourHours)
        public List<RecommendModel> todayTop;    // todayTop
        public List<CardModel> prices;           // prices
    }

    public static class BannerModel{
        public String id;
        public String siteId;
        public String sort;
        public String link;
        public String background;
        public String note;
        public String picUrl;
    }

    public static class IndexModel{
        public String breed = "";       // 产品名称
        public String priceIndex = "";  // the color of "priceIndex" is the same with "upDown"
        public String rate = "";
        public String upNum = "";
        public String flatNum = "";
        public String downNum = "";
        public String upDown = "";      // 正数 up_red_arrow ; 负数 down_green_arrow
    }

    public static class Hw24hModel{
        public String id = "";
        public String catId = "";
        public String catName = "";   //
        public String title = "";     //
        public String name = "";      //
        public String author = "";    //
        public String source = "";
        public String keywords = "";
        public String dateTimeStr = "";

        public String shorDate = "";  //
        public String monthDate = "";  //
        public String shorTime = "";  //
        public String articleImg = "";
        public String description = "";
        public String content = "";
        public String pdfUrl = "";
        public String pptxUrl = "";
        public String xlsxUrl = "";
        public String viewTimes = "";   //
        public String likeTimes = "";
    }
    public static class RecommendModel{
        public String id = "";
        public String catId = "";
        public String catName = "";   //
        public String title = "";     //
        public String name = "";      //  no
        public String author = "";    //
        public String source = "";
        public String keywords = "";
        public String dateTimeStr = "";

        public String shorDate = "";  //
        public String monthDate = "";  //
        public String shorTime = "";  //
        public String articleImg = "";
        public String description = "";
        public String content = "";
        public String pdfUrl = "";
        public String pptxUrl = "";
        public String xlsxUrl = "";
        public String viewTimes = "";   //
        public String likeTimes = "";

    }

    public static class CardModel{
        public String breed = "";
        public String spec = "";
        public String price = "";
        public String unit = "";
        public String dayRate = "";
        public String weekRate = "";
        public String area = "";
        public String dateTimerStr = "";
    }
}
