package com.ihwdz.android.hwapp.model.bean;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :  交易会员- 申请额度 - 产品信息
 * version: 1.0
 * </pre>
 */
public class ProductData {
    public String code;
    public String msg;
    public ProductEntity data;

    public static class ProductEntity{
        public List<String> goodsTypeList;
        public List<String> materialList;
    }
}
