package com.ihwdz.android.hwapp.ui.home.materialpurchase;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.MaterialData;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoDayAdapter;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :   买原料
 * version: 1.0
 * </pre>
 */
public interface MaterialContract {

    interface View extends BaseView {

        void showWaitingRing();  // "查看更多"、"上拉加载更多"
        void hideWaitingRing();

        void showMenuView();     // 展示菜单选项卡
        void hideMenuView();     // 隐藏菜单选项卡

        void updateBreedTitle(String title);  // 更新 Menu name 重置 radio button checked = false
        void updateSpecTitle(String title);
        void updateCityTitle(String title);
        void updateBrandTitle(String title);

        void showEmptyView();
        void hideEmptyView();

        void hideKeyboard();
        void showPromptMessage(String message);

    }

    int PageNum = 1;
    int PageSize = 15;

    int MENU_HIDE = 0;
    int BREED = 1;
    int SPEC = 2;
    int CITY = 3;
    int BRAND = 4;

    int ALL_DATA = 0;
    int KEYWORDS_DATA = 1;

    interface Presenter extends BasePresenter {

        MaterialAdapter getAdapter();

        CheckableAdapter getMenuAdapter();

        void refreshData();

        void getAllData();                              // 拿到菜单栏 各项数据 集合

        void getSearchData(int pageNum, int pageSize);  // 搜索数据  加载 所有数据 | 关键字查询 |

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        void setCurrentMode(int mode);
        int getCurrentMode();

        void setCurrentMenu(int menu);                     // 当前菜单选择项
        int getCurrentMenu();

        void setCurrentMenuSelected(String menuSelected);  // 选中某项菜单栏下的某个选项卡时 - hide recycler view and update menu button(name & checked)

        void setSearchKeywords(String keywords);           // 关键字

    }
}
