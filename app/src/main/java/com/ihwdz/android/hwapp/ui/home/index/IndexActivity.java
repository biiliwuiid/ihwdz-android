package com.ihwdz.android.hwapp.ui.home.index;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoDayActivity;
import com.ihwdz.android.hwapp.widget.RecyclerOnScrollListener;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.PageSize;

public class IndexActivity extends BaseActivity implements IndexContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "IndexActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

//    @BindView(R.id.line)
//    View line;
    @BindView(R.id.title) TextView title2;
    @BindView(R.id.newest) TextView newest;
    @BindView(R.id.up_down) TextView upDown;
    @BindView(R.id.date) TextView date;

    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Inject IndexContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_index;
    }

    @Override
    public void initView() {

        initToolBar();
        initContextBar();
        initRecyclerView();
    }

    private void initContextBar() {
        title2.getPaint().setFakeBoldText(true);
        newest.getPaint().setFakeBoldText(true);
        upDown.getPaint().setFakeBoldText(true);
        date.getPaint().setFakeBoldText(true);

    }


    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
        mPresenter.getAllData();
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

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_index));
    }
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());
//        recyclerView.addOnScrollListener(new RecyclerOnScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                performLoadMoreData(currentPage);
//            }
//        });
    }
//    private void performLoadMoreData(final int currentPage) {
//        Observable
//                .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//                .map(new Func1<Long, Object>() {
//                    @Override
//                    public Object call(Long aLong) {
//                        // loadMoreData();
//                        mPresenter.getAllData(currentPage, PageSize);
//                        mPresenter.getAdapter().notifyDataSetChanged();
//                        Toast.makeText(IndexActivity.this, "Load Finished!", Toast.LENGTH_SHORT).show();
//                        return null;
//                    }
//                }).subscribe();
//    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }
}
