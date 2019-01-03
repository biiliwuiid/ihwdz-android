package com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PurchaseListActivity extends BaseActivity implements PurchaseListContract.View{

    String TAG = "PurchaseListActivity";

    String titleStr = "";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.grey_line)View greyLine;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Inject PurchaseListContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_purchase_list;
    }

    @Override
    public void initView() {
        titleStr = getResources().getString(R.string.purchase_list_per);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);

        initRecyclerView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getPurchaseData();

        if (mPresenter.getAdapter().getItemCount() < 1){
            showEmptyView();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(mPresenter.getAdapter());
    }
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    @Override
    public void showPromptMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        greyLine.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

}
