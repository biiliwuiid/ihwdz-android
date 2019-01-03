package com.ihwdz.android.hwapp.base.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.ihwdz.android.hwapp.base.mvp.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/18
 * desc :
 * version: 1.0
 * </pre>
 */
public class BaseStatePagerAdapter extends FragmentStatePagerAdapter {

//    private ArrayList<BaseFragment> mFragments;
    private List<BaseFragment> mFragments;
    private List<String> mTitleList;
    private FragmentManager mFragmentManager;

    public BaseStatePagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    /**
     * 接收首页传递的标题
     */
    public BaseStatePagerAdapter(FragmentManager fm, List<BaseFragment> mFragment, List<String> mTitleList) {
        super(fm);
        this.mFragments = mFragment;
        this.mTitleList = mTitleList;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null) {
            return mTitleList.get(position);
        } else {
            return "";
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //return super.getItemPosition(object);
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
