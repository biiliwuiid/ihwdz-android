package com.ihwdz.android.hwapp.model.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * <pre>
 * author : Duan
 * time : 2018/7/24.
 * desc : TabEntity
 * version: 1.0
 * </pre>
 */

public class TabEntity implements CustomTabEntity {

    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
