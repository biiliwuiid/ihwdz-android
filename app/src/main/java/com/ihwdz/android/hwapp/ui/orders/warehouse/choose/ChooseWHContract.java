package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :   报价 -> 选择仓库
 * version: 1.0
 * </pre>
 */
public interface ChooseWHContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void initHistoryView();         // 历史搜索 仓库  horizontal 3 item newest.
        void initWarehouseView();       // 所有仓库  输入 并点击 软键盘 搜索 后

        void showEmptyHistory();
        void showPromptMessage(String message);  //  提示信息
    }

    int MODE_HISTORY = 0;    // 历史搜索
    int MODE_WAREHOUSE = 1;  // 搜索仓库

    interface Presenter extends BasePresenter {

        void setCurrentKeyWord(String keyWord); // 输入框内容

        void getWarehouseData();       // 获取 精确仓库数据
        void getFuzzyWarehouseData();  // 获取 模糊仓库数据

        void getHistoryWarehouse();    // 获取 历史仓库数据（本地存储） 进入该页面时

        HistoryWarehouseAdapter getHistoryAdapter();
        WarehouseAdapter getWarehouseAdapter();

        void addWarehouse();           // 添加新仓库

        void setCurrentWarehouse(WarehouseData.WarehouseForQuotePost warehouseForQuotePost);
        void warehouseSelected(String warehouseJson, String warehouseName);    // 选中某仓库（选中后 store and back）

        // 选中某个 历史仓库时（当该仓库为上次新建仓库时 可能此时并未 添加进数据库），需校验
        void historyWarehouseSelected(String warehouseJson, String warehouseName);

        String changeToWarehouseForPost(WarehouseData.Warehouse warehouse);

        //void storeWarehouse();    // 记住选中的仓库 json string -> 历史仓库数据 （确认报价后 再存储）

    }

}
