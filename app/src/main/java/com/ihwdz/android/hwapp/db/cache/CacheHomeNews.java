package com.ihwdz.android.hwapp.db.cache;

import io.realm.RealmObject;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class CacheHomeNews extends RealmObject {

    private String title;
    private String logo;
    private String imageUrl;
    private String url;
    private String summary;
    private String time;
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
