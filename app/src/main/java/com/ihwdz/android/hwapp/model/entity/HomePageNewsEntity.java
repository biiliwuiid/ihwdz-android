package com.ihwdz.android.hwapp.model.entity;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/06
 * desc :
 * version: 1.0
 * </pre>
 */
public class HomePageNewsEntity {

    private BottomNews bottomNews;

    public HomePageNewsEntity(BottomNews bottomNews) {
        this.bottomNews = bottomNews;
    }

    public BottomNews getBottomNews() {
        return bottomNews;
    }

    public void setBottomNews(BottomNews bottomNews) {
        this.bottomNews = bottomNews;
    }
}
