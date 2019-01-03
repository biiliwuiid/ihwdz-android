package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 报价 -> 选择仓库
 */
public class ChooseWHActivity extends BaseActivity implements ChooseWHContract.View{


    String TAG = "ChooseWHActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_right) TextView buildWarehouse;  // 添加新仓库

    @BindView(R.id.edit) EditText editText;

    @BindView(R.id.tv_history) TextView historyTv;
    @BindView(R.id.tv_history_empty) TextView historyEmptyTv;
    @BindView(R.id.recycler_history) RecyclerView historyRecyclerView;  // 历史搜索 3 horizontal
    @BindView(R.id.history_linear) LinearLayout historyLinear;

    @BindView(R.id.warehouse_linear) LinearLayout warehouseLinear;
    @BindView(R.id.recycler_warehouse) RecyclerView warehouseRecyclerView; // 搜仓库 vertical

    @Inject ChooseWHContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_choose_wh;
    }

    @Override
    public void initView() {
        initToolbar();
        initHistoryView();
        initHistoryRecyclerView();
        initWarehouseRecyclerView();
    }

    // 历史搜索记录 RecyclerView
    private void initHistoryRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);
        mPresenter.getHistoryAdapter().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(String warehouseJsonInfo, String warehouseName) {
                mPresenter.historyWarehouseSelected(warehouseJsonInfo, warehouseName);
            }
        });
        historyRecyclerView.setAdapter(mPresenter.getHistoryAdapter());
    }
    // 搜索框 搜索结果 RecyclerView
    private void initWarehouseRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        warehouseRecyclerView.setLayoutManager(layoutManager);
        mPresenter.getWarehouseAdapter().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(String warehouseJsonInfo, String warehouseName) {
                mPresenter.warehouseSelected(warehouseJsonInfo, warehouseName);
            }
        });
        mPresenter.getWarehouseAdapter().setOnWarehouseItemClickListener(new OnWarehouseItemClickListener() {
            @Override
            public void onWarehouseClicked(WarehouseData.WarehouseForQuotePost warehouse) {
                mPresenter.setCurrentWarehouse(warehouse);
            }
        });
        warehouseRecyclerView.setAdapter(mPresenter.getWarehouseAdapter());
    }


    @Override
    public void initListener() {

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    LogUtils.printCloseableInfo(TAG, "editText hasFocus: " +hasFocus);
                }else {
                    LogUtils.printCloseableInfo(TAG, "editText hasFocus: " +hasFocus);
                }
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    initWarehouseView();
                    LogUtils.printInfo(TAG, "================= search : " + v.getText());
                     hideKeyboard();
                    mPresenter.setCurrentKeyWord(v.getText().toString());
                    mPresenter.getFuzzyWarehouseData();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getHistoryWarehouse();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.warehouse_title));
        buildWarehouse.setVisibility(View.VISIBLE);
        buildWarehouse.setTextSize(12);
        buildWarehouse.setText(getResources().getString(R.string.build_warehouse_title));
    }
    @OnClick(R.id.fl_title_menu_right)
    public void onRightClicked() {
        mPresenter.addWarehouse();
    }


    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    // 历史搜索 仓库  horizontal 3 item newest.
    @Override
    public void initHistoryView() {
        editText.clearFocus();
        historyLinear.setVisibility(View.VISIBLE);
        warehouseLinear.setVisibility(View.GONE);
    }
    // 所有仓库 vertical
    @Override
    public void initWarehouseView() {
        historyLinear.setVisibility(View.GONE);
        warehouseLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyHistory() {
        historyTv.setVisibility(View.GONE);
        historyEmptyTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
