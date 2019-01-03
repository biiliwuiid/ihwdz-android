package com.ihwdz.android.hwapp.ui.orders.query;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.LogisticsResultData;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/09
 * desc :
 * version: 1.0
 * </pre>
 */
public interface QueryContract {

    interface View extends BaseView {

        void updateView(LogisticsResultData.LogisticsResult result);
    }

    interface Presenter extends BasePresenter {

        void refreshData();
        void getPriceData();

        void setCurrentAmount(String amount);
        String getCurrentAmount();
    }
}
