package com.ihwdz.android.hwapp.ui.home.clientseek;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.me.usernotes.UserNotesActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.CleanableEditText;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.FOLLOWED_CLIENTS_MODE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.POTENTIAL_CLIENTS_MODE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.PageNum;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.PageSize;

/**
 * 找客户
 */
public class ClientActivity  extends BaseActivity implements ClientContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "ClientActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.iv_right) ImageView rightBt;

    @BindView(R.id.editText) CleanableEditText mEditSearchInput; // 搜索框

    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.radio_potential) RadioButton potentialBt;
    @BindView(R.id.radio_follow) RadioButton followBt;

    @BindView(R.id.empty_tv) TextView emptyTv;
    @BindView(R.id.refresher) SwipeRefreshLayout refresher;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.filter_layout) LinearLayout linearFilter;

    @BindView(R.id.update_layout) LinearLayout linearUpdate;

    @BindView(R.id.update_bt_layout) LinearLayout linearUpdateBt;

    private Handler handler = new Handler();

    private boolean isScrollDown; //是否是向下滑动

    boolean isFromFilter = false;

    @Inject ClientContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_client;
    }

    @Override
    public void initView() {

        initToolBar();
        if (getIntent() != null){
            isFromFilter = getIntent().getBooleanExtra("FILTER", false);  // 筛选之后返回
            mPresenter.setIsFromFilter(isFromFilter);
        }
        LogUtils.printInfo(TAG, "=========== initView ========== isFromFilter: " + isFromFilter);
        if (!isFromFilter){
            updateView();
            showUpdateButton(mPresenter.getHasRight());
        }
        initRecyclerView();
    }

    @Override
    public void initListener() {

        refresher.setOnRefreshListener(this);

        mEditSearchInput.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG, "mEditSearchInput ================ hasFocus");
                    //mLayoutFuzzySearch.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "mEditSearchInput ================ has   NO  Focus");
                    //mLayoutFuzzySearch.setVisibility(View.GONE);
                }
            }
        });

        // ClearEditText (custom EditText)  addTextChangedListener
        mEditSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // LogUtils.printInfo(TAG, "=========== beforeTextChanged ==========" + s + " start: " + start +" count: " + count+" after: " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtils.printInfo(TAG, "=========== onTextChanged ==========" + s + " before: " + before +" count: " + count);
                //mPresenter.getFuzzySearchAdapter().getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

                mPresenter.setSearchKeywords(s.toString().trim());

//                if (s != null && s.length() > 0){
//                    LogUtils.printInfo(TAG, "=========== afterTextChanged ========== s != null" + s);
//                   //  mPresenter.getSearchClientsData(PageNum, PageSize, s.toString());
//                }else {
//                    LogUtils.printInfo(TAG, "=========== afterTextChanged ========== s == null" );
//                    showWaitingRing();
//                    mPresenter.setCurrentPageNum(1);
//                    mPresenter.getPotentialClientsData(PageNum, PageSize);
//                }

            }
        });

        // 监听“搜索”
        mEditSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    LogUtils.printInfo(TAG, "=========== actionId == EditorInfo.IME_ACTION_SEARCH" );
                    hideKeyboard();
                    mPresenter.setCurrentPageNum(1);
                    mPresenter.getSearchClientsData(PageNum, PageSize);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
//        LogUtils.printInfo(TAG, "initData ====================");
//        if (isFromFilter){
//            LogUtils.printInfo(TAG, "isFromFilter");
//            mPresenter.getFilterClientData(PageNum, PageSize);
//        }else {
//            LogUtils.printInfo(TAG, "is not From Filter");
//            mPresenter.getUpdateRightData();    // 是否升级了权限
//            mPresenter.getPotentialClientsData(PageNum, PageSize);  // 获取数据 放在判断权限之后
//        }

    }

    @Override
    protected void onResume() {
        LogUtils.printInfo(TAG, "onResume ====================");
        super.onResume();
        if (isFromFilter){
            LogUtils.printInfo(TAG, "isFromFilter");
            mPresenter.getFilterClientData(PageNum, PageSize);
        }else {
            LogUtils.printInfo(TAG, "is not From Filter");
            mPresenter.getUpdateRightData();    // 是否升级了权限
            mPresenter.getPotentialClientsData(PageNum, PageSize);  // 获取数据 放在判断权限之后
        }
        // 支付成功隐藏按钮
        if (Constant.updateOptionDone){
            linearUpdate.setVisibility(View.GONE);
        }
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

    // 升级按钮 是否隐藏
    @Override
    public void showUpdateButton(boolean hasRight) {
        if (hasRight){
            LogUtils.printCloseableInfo(TAG, "================ 有权限 隐藏 升级按钮");
            // 有权限 隐藏 升级按钮
            linearUpdate.setVisibility(View.GONE);
        }else {
            // 无权限 - 登录/注册 或 升级权限
            linearUpdate.setVisibility(View.VISIBLE);
            linearUpdateBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constant.LOGOUT){
                        // 登录/注册
                        mPresenter.goLogin();
                    }else {
                        // 升级权限
                        mPresenter.goUpdate();
                    }
                }
            });
        }

    }

    @Override
    public void updateView() {
        if (mPresenter.getCurrentMode() == POTENTIAL_CLIENTS_MODE){
            // 潜在客户
            potentialBt.setChecked(true);
            followBt.setChecked(false);
            // 加载-潜在客户
            mPresenter.getPotentialClientsData(mPresenter.getCurrentPageNum(), PageSize);
        }else if (mPresenter.getCurrentMode() == FOLLOWED_CLIENTS_MODE){
            // 关注客户
            potentialBt.setChecked(false);
            followBt.setChecked(true);
            // 未登录状态 - 登录/注册; 否则 加载-关注客户
            if (Constant.LOGOUT){
                mPresenter.setCurrentMode(POTENTIAL_CLIENTS_MODE);
                mPresenter.goLogin();
            }else {
                mPresenter.getFollowClientsData(mPresenter.getCurrentPageNum(), PageSize);
            }
        }
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        rightBt.setImageDrawable(this.getResources().getDrawable(R.drawable.user_notes));
        rightBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_cl));

    }

    // 返回
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }

    // 用户须知
    @OnClick(R.id.fl_title_menu_right)
    public void onRightClicked(){
        UserNotesActivity.startUserNotesActivity(this, 0);
    }

    // 检测 RadioButton checked 状态
    @OnCheckedChanged(R.id.radio_potential)
    public void onPotentialBtChanged(){
        if (potentialBt.isChecked()){
            mPresenter.setCurrentMode(POTENTIAL_CLIENTS_MODE);
        }else {
            mPresenter.setCurrentMode(FOLLOWED_CLIENTS_MODE);
        }
    }


    // 筛选
    @OnClick(R.id.filter_layout)
    public void onFilterBtClicked(){
        // Toast.makeText(ClientActivity.this, getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
        // coming soon
        mPresenter.goFilter();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mPresenter.getAdapter().addItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(String clientId) {

                if(Constant.LOGOUT){
                    // 未登录 - 登录/注册
                        mPresenter.goLogin();
                }else {
                    // 已登录 - 判断权益
                    LogUtils.printInfo(TAG, "mPresenter.getHasRight(): " + mPresenter.getHasRight());
                    if (mPresenter.getHasRight()){
                        // 有权益 - 看详情
                        mPresenter.goDetail(clientId);
                    }else {
                        // 没权益 - 升级（付款）
                        mPresenter.goUpdate();
                    }

                }
            }
        });
        recyclerView.setAdapter(mPresenter.getAdapter());

        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, " ================================================================  onScrollStateChanged ===============: newState: " + newState );
            // SCROLL_STATE_IDLE     : 0
            // SCROLL_STATE_DRAGGING : 1
            // SCROLL_STATE_SETTLING : 2
            if (newState == SCROLL_STATE_IDLE){
                int lastVisibleItem;
                // 获取RecyclerView的LayoutManager
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                // 获取到最后一个可见的item
                if (layoutManager instanceof LinearLayoutManager) {
                    // 如果是 LinearLayoutManager
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    // 如果是 StaggeredGridLayoutManager
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItem = findMax(into);
                } else {
                    // 否则抛出异常
                    throw new RuntimeException("Unsupported LayoutManager used");
                }
                // 获取item的总数
                int totalItemCount = layoutManager.getItemCount();
                /**
                 最后一个可见的item为最后一个item
                 是向下滑动
                 */
                if (lastVisibleItem >= totalItemCount - 1 && isScrollDown) {

                    // STATUS_PREPARE
                    if (mPresenter.getAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_PREPARE){
                        // 此处调用 加载更多回调接口 的回调方法
                        // set footer : loading
                        mPresenter.getAdapter().setIsLoadMore(true);  // add footer
                        mPresenter.getAdapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING); // set footer : loading
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //load more
                                if (Constant.LOGOUT){  // 未登录状态
                                    mPresenter.goLogin();

                                }else {                // 登录状态
                                    if (mPresenter.getHasRight()){
                                        // 有权益可加载更多
                                        if (mPresenter.getCurrentMode() == POTENTIAL_CLIENTS_MODE){      // 潜在客户
                                            mPresenter.getPotentialClientsData(mPresenter.getCurrentPageNum(), PageSize);
                                        }else if (mPresenter.getCurrentMode() == FOLLOWED_CLIENTS_MODE){ // 关注客户
                                            mPresenter.getFollowClientsData(mPresenter.getCurrentPageNum(), PageSize);
                                        }else {

                                        }
                                    }else {
                                        // 无权益 不再加载
                                        showPromptMessage(getString(R.string.no_more_cl));
                                    }
                                }


                            }
                        }, 2000);
                    }

                    // STATUS_EMPTY
                    if (mPresenter.getAdapter().getLoadMoreStatus() == Constant.loadStatus.STATUS_EMPTY){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getAdapter().setIsLoadMore(false);
                            }
                        }, 2000);
                    }

                }

            }
        }

    };

    /**
     * 获取数组中的最大值
     *
     * @param lastPositions 需要找到最大值的数组
     * @return 数组中的最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
