package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.tuacy.azlist.IAZItem;
import com.tuacy.fuzzysearchlibrary.IFuzzySearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :
 * version: 1.0
 * </pre>
 */
public class ItemEntity implements IAZItem, IFuzzySearchItem {

    private String       mValue;
    private AdminData.AdminEntity mData;
    private String       mSortLetters;
    private List<String> mFuzzySearchKey;

    public ItemEntity(String value,AdminData.AdminEntity data, String sortLetters, List<String> fuzzySearchKey) {
        mValue = value;
        mData = data;
        mSortLetters = sortLetters;
        mFuzzySearchKey = fuzzySearchKey;
    }

    public String getValue() {
        return mValue;
    }

    public AdminData.AdminEntity getData() {
        return mData;
    }

    @Override
    public String getSortLetters() {
        return mSortLetters;
    }

    @Override
    public String getSourceKey() {
        return mValue;
    }

    @Override
    public List<String> getFuzzyKey() {
//        List<String> strings = new ArrayList<>();
//        for (int i = 0; i < mFuzzySearchKey.size(); i ++){
//            strings.add(mFuzzySearchKey.get(i).name);
//        }
//        return strings;

        return mFuzzySearchKey;
    }
}
