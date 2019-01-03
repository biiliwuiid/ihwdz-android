package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/19
 * desc :
 * version: 1.0
 * </pre>
 */
public class UserGoodsData {

    public String code;
    public String msg;
    public List<UserGoodsEntity> data;

    public static class UserGoodsEntity{

        public String catDesc;
        public String catName;
        public String catPrice;
        public String catShortName;
        public String days;
        public String goodsId;
    }
}
