package com.ihwdz.android.hwslimcore.OfflineConnector.database;

import com.google.gson.Gson;
import com.ihwdz.android.hwapp.model.entity.HomePageData;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :   DBFlow  Data BASE  TABLE
 * version: 1.0
 * </pre>
 */

//@Table(database = DatabaseOfflineInfo.class)
public class ModelOfflineData {

//    @PrimaryKey(autoincrement = true)
//    @Column
//    String code;
//
//    @Column
//    String msg;

//    @Column
//    String dataJson;

//    @Column
//    @ForeignKey(saveForeignKeyModel = false)
//    ModelOfflineSite site;

    HomePageData data;


//    public void setData(HomePageData data){
//        this.data = data;
//        this.dataJson = new Gson().toJson(data);
//    }
//    public HomePageData getData(){
//
//        if(data == null && dataJson != null)
//        {
//            Gson gson = new Gson();
//            data = gson.fromJson(dataJson, HomePageData.class);
//        }
//        return data;
//    }

//    public void setSiteUrl(ModelOfflineSite site){
//        this.site = site;
//    }
//
//    public ModelOfflineSite getSite(){
//        return site;
//    }

}
