package com.ihwdz.android.hwapp.ui.me.businessvip.myquote;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商家会员 -  我的报价
 */
public class QuoteActivity extends BaseActivity {

    String TAG = "QuoteActivity";

    String titleStr = "";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.grey_line)View greyLine;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    public int getContentView() {
        return R.layout.activity_quote;
    }

    @Override
    public void initView() {
        titleStr = getResources().getString(R.string.quote_mine_per);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);

        initRecyclerView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

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
}
