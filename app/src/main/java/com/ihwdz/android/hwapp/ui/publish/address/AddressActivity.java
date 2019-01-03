package com.ihwdz.android.hwapp.ui.publish.address;


import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 收货地址
 */
public class AddressActivity extends BaseActivity implements AddressContract.View, SwipeRefreshLayout.OnRefreshListener {

    String TAG = "AddressActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_right) TextView addAddress;  // 添加新地址

    @BindView(R.id.tv_empty) TextView emptyTv;     //
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refresher) SwipeRefreshLayout refresher;

    @Inject AddressContract.Presenter mPresenter;

    public static void startAddressActivity(Context context) {
        Intent intent = new Intent(context, AddressActivity.class);
        context.startActivity(intent);

        Constant.addressSelected_province = null;  // 选择的地址置空
    }


    @Override
    public int getContentView() {
        return R.layout.activity_address;
    }

    @Override
    public void initView() {
        initToolbar();
        initRecyclerView();
    }


    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        //mPresenter.getAddressData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getAddressData();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.address_title));
        addAddress.setVisibility(View.VISIBLE);
        addAddress.setTextSize(12);
        addAddress.setText(getResources().getString(R.string.address_add));
    }

    // 添加新地址
    @OnClick(R.id.fl_title_menu_right)
    public void onRightClicked() {
        mPresenter.gotoAddAddress();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mPresenter.getAdapter().setOnEditClickListener(new OnAddressItemClickListener.OnEditClickListener() {
            @Override
            public void onEditClicked(int apply, String id) {
                mPresenter.gotoEditAddress(id);
//                if (apply == 1){ // 审核中 不展示“编辑”
//                    String checkingRemind = getResources().getString(R.string.address_checking);
//                    showPromptMessage(checkingRemind);
//                }else {
//                    mPresenter.gotoEditAddress(id);
//                }
            }
        });

        mPresenter.getAdapter().setOnItemClickListener(new OnAddressItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClicked() {
                mPresenter.goBackPurchase();
            }
        });
        recyclerView.setAdapter(mPresenter.getAdapter());
    }


    @Override
    public void showWaitingRing() {
        refresher.post(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(true);
            }
        });
        refresher.setEnabled(false);
    }

    @Override
    public void hideWaitingRing() {
        refresher.post(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(false);
            }
        });
        refresher.setEnabled(true);
    }

    @Override
    public void showEmptyView() {
        emptyTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        emptyTv.setVisibility(View.GONE);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }
}
