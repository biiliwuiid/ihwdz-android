package com.ihwdz.android.hwapp.ui.purchase;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.HwAppDialog;


import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.purchase.PurchaseFragmentContract.ALL_DATA_MODE;
import static com.ihwdz.android.hwapp.ui.purchase.PurchaseFragmentContract.MY_DATA_MODE;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :   求购池
 * version: 1.0
 * </pre>
 */
public class PurchaseFragment extends BaseFragment<PurchaseFragmentPresenter> implements PurchaseFragmentContract.View, SwipeRefreshLayout.OnRefreshListener{


    static String TAG = "PurchaseFragment";

    @BindView(R.id.cb_title) CheckBox cbTitle;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.iv_title) ImageView ivTitle;
    @BindView(R.id.rl_title) RelativeLayout rlTitle;

    @BindView(R.id.empty_tv) TextView emptyTv;
    @BindView(R.id.remind_linear) LinearLayout remindLinear;
    @BindView(R.id.tv_remind) TextView remindTv;             // 提醒文字
    @BindView(R.id.bt_remind) TextView remindBt;             // 提醒按钮：“去开通”

    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.radio1) RadioButton radio1;
    @BindView(R.id.radio2) RadioButton radio2;

    @BindView(R.id.layout_fuzzy) LinearLayout layoutFuzzy;  // 带模糊背景的 RadioButton布局

    @BindView(R.id.refresher) SwipeRefreshLayout refresher;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private MainActivity activity;

    private Handler handler = new Handler();
    private boolean isScrollDown;   // 是否是向下滑动

    PurchaseFragmentContract.Presenter mPresenter = new PurchaseFragmentPresenter(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_purchase;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecyclerView();
    }

    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
       // mPresenter.getUserType();
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.printInfo(TAG, "onResume ");
        if (!mPresenter.isLoadingData() && Constant.token != null){ //activity.getCurrentFragment() == 1 &&  当前为 PurchaseFragment 并且 没有在加载数据
            LogUtils.printInfo(TAG, "!mPresenter.isLoadingData() && Constant.token != null ==== refreshData  currentMode: " + mPresenter.getCurrentMode());
            mPresenter.refreshData();
        }else {
            //LogUtils.printInfo(TAG, "onResume ====  activity.getCurrentFragment() != 1 ");
        }

    }

    @Override
    public void initLoginSuccess() {
        LogUtils.printInfo(TAG, "initLoginSuccess ============ refreshData  currentMode: " + mPresenter.getCurrentMode());
        updateView();
        mPresenter.getUserType();
        mPresenter.refreshData();
    }

    private void initToolBar() {
        // LogUtils.printInfo(TAG, "initToolBar  currentMode: " + mPresenter.getCurrentMode());
        rlTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        cbTitle.setVisibility(View.VISIBLE);
        updateView();
    }

    @OnCheckedChanged (R.id.cb_title)
    public void onTitleCheckedChanged(){
        if (cbTitle.isChecked()){
            showRadioButton();
            // LogUtils.printInfo(TAG, "cbTitle.isChecked: " + cbTitle.isChecked());
        }else {
            hideRadioButton();
            // LogUtils.printInfo(TAG, "cbTitle.isChecked: " + cbTitle.isChecked());
        }
    }

    // 全部求购/我的求购 - use for FragmentFactory
    public void setCurrentMode(int mode){
        LogUtils.printCloseableInfo(TAG, "FragmentFactory -> 全部求购/我的求购 setCurrentPageNum(1) setCurrentMode: " + mode);
        mPresenter.setCurrentMode(mode);
        mPresenter.setCurrentPageNum(1);
    }

    // 点击“全部求购”
    @OnClick(R.id.radio1)
    public void onRadio1Clicked(){
        // LogUtils.printInfo(TAG, "onRadio1Clicked " );
        if (radio1.isChecked()){
            cbTitle.setText(getResources().getString(R.string.title_purchase));
            mPresenter.setCurrentMode(ALL_DATA_MODE);
        }else {
        }
        hideRadioButton();
    }

    // 点击“我的求购/报价”
    @OnClick(R.id.radio2)
    public void onRadio2Clicked(){
        // LogUtils.printInfo(TAG, "onRadio2Clicked " );
        String myTitle = "";
        switch (Constant.VIP_TYPE){
            case 1:
                myTitle = getResources().getString(R.string.purchase_mine);
                break;
            case 2:
                myTitle = getResources().getString(R.string.quote_mine);
                break;
            default:
                myTitle = getResources().getString(R.string.purchase_mine);
                break;
        }
        if (radio2.isChecked()){
            cbTitle.setText(myTitle);
            mPresenter.setCurrentMode(MY_DATA_MODE);
        }else {
        }
        hideRadioButton();
    }

    // 开通会员/ 认证商家
    @OnClick(R.id.bt_remind)
    public void onRemindBtClicked(){
        // 未开通会员
        // 商家未认证
        switch (Constant.VIP_TYPE){
            case -1:
                // 普通用户 // 未开通会员 去开通
                // LogUtils.printCloseableInfo(TAG, "普通用户 // 未开通会员 去开通");
                mPresenter.gotoOpenDealVip();
                break;
            case 0:
                // 资讯会员 // 未开通会员 去开通
                // LogUtils.printCloseableInfo(TAG, "资讯会员 // 未开通会员 去开通");
                mPresenter.gotoOpenDealVip();
                break;
            case 2:
                // 商家会员
                mPresenter.gotoBusinessAuthentic();
                break;
            default:
                break;
        }
    }

    // 切换 all/my 按钮
    private void showRadioButton() {
        layoutFuzzy.setVisibility(View.VISIBLE);
    }
    private void hideRadioButton() {
        layoutFuzzy.setVisibility(View.GONE);
        cbTitle.setChecked(false);
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
    public void updateView() {
        // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
        // LogUtils.printCloseableInfo(TAG, "============ updateView ========= Constant.VIP_TYPE: " + Constant.VIP_TYPE);

        rlTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        cbTitle.setVisibility(View.VISIBLE);

        switch (Constant.VIP_TYPE){
            case -1:
                // 普通用户
                initInfoView();
                break;
            case 0:
                // 资讯会员
                initInfoView();
                break;
            case 1:
                // 交易
                initDealView();
                break;
            case 2:
                // 商家
                initBusinessView();
                break;
            default:
                // 未登录
                initNotLoggedInView();
                break;
        }

    }

    // 用户状态异常（锁定、认证） 联系客服
    @Override
    public void showRemindDialog(String message) {
        // 弹框
        HwAppDialog dialog = new HwAppDialog
                .Builder(activity)
                //.setTitle("用户状态异常")
                .setInsideContentView(mPresenter.getDialogContentView(message))
                .setPositiveButton(getResources().getString(R.string.contract_service), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击 联系客服
                        mPresenter.doContract();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击 取消
                    }
                })
                .create();
        dialog.show();
    }


    // 未登录
    private void initNotLoggedInView() {
        switch (mPresenter.getCurrentMode()){
            case ALL_DATA_MODE:
                hideRemindView();
                cbTitle.setText(getResources().getString(R.string.title_purchase)); // 求购
                //mPresenter.getPurchasePoolData();
                break;
            case MY_DATA_MODE:
                cbTitle.setText(getResources().getString(R.string.purchase_mine)); // 我的求购
                mPresenter.getAdapter().clear();
                mPresenter.gotoLoginPage();
                break;
        }
    }

    String remindStr, remindButtonStr;
    // 普通用户/资讯会员
    private void initInfoView() {
        switch (mPresenter.getCurrentMode()){
            case ALL_DATA_MODE:
                hideRemindView();
                cbTitle.setText(getResources().getString(R.string.title_purchase)); // 求购
                //mPresenter.getPurchasePoolData();

                break;
            case MY_DATA_MODE:
                cbTitle.setText(getResources().getString(R.string.purchase_mine)); // 我的求购
                mPresenter.getAdapter().clear();

                remindStr = activity.getResources().getString(R.string.remind_open_deal_vip);
                remindButtonStr = activity.getResources().getString(R.string.remind_open);
                showRemindView(remindStr, remindButtonStr);
                break;
        }
    }
    // 交易会员
    private void initDealView() {
        switch (mPresenter.getCurrentMode()){
            case ALL_DATA_MODE:
                // LogUtils.printCloseableInfo(TAG, "============ updateView - initDealView ========= 求购 " );
                cbTitle.setText(getResources().getString(R.string.title_purchase));      // 求购
                // mPresenter.getPurchasePoolData();
                break;
            case MY_DATA_MODE:

                radio2.setChecked(true);
//                rlTitle.setVisibility(View.GONE);
//                cbTitle.setVisibility(View.VISIBLE);

                cbTitle.setText(getResources().getString(R.string.purchase_mine)); // 我的求购
                // LogUtils.printCloseableInfo(TAG, "============ updateView - initDealView ========= 我的求购 " );
                // LogUtils.printCloseableInfo(TAG, ""+cbTitle );
                // 获取 my purchase list data
//                mPresenter.setCurrentPageNum(1);
//                mPresenter.getMyPurchaseListData();
                break;
        }
    }
    // 商家会员
    private void initBusinessView() {
        radio2.setText(getResources().getString(R.string.quote_mine));
        switch (mPresenter.getCurrentMode()){
            case ALL_DATA_MODE:
                cbTitle.setText(getResources().getString(R.string.title_purchase)); // 求购
                //mPresenter.getPurchasePoolData();
                hideRemindView();
                break;
            case MY_DATA_MODE:
                radio2.setChecked(true);
                cbTitle.setText(getResources().getString(R.string.quote_mine));     // 我的报价
//                mPresenter.setCurrentPageNum(1);
//                mPresenter.getMyQuoteListData();
                break;
        }
    }


    // 展示 求购数据/报价数据 的 RecyclerView
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 点击 item 进入 求购/报价 详情
        mPresenter.getAdapter().setOnPurchaseItemClickListener(new OnPurchaseItemClickListener() {
            @Override
            public void onItemClicked(String id,String status,String breed,String qty, String address,  String dateStr, boolean isQuoteDetail) {
                if (isQuoteDetail){ // 点击 我的报价 item -> 报价详情
                    mPresenter.gotoQuoteDetail( id, status, breed, qty, address, dateStr);
                }else {       // 点击 我的求购 item -> 求购报价
                    mPresenter.gotoPurchaseDetail( id, status, breed, qty, address);
                }
            }
        });

        // 商家会员 点击 进行 报价
        mPresenter.getAdapter().setOnQuoteClickListener(new OnQuoteBtClickListener() {
            @Override
            public void onQuoteBtClicked(boolean isQuoteEnable,String id,String status,String breed,String qty, String address, String dateStr) {

                if (Constant.LOGOUT || Constant.token == null){
                    LogUtils.printCloseableInfo(TAG, "onQuoteBtClicked   Constant.LOGOUT = true || Constant.token == null");
                    mPresenter.gotoLoginPage(); // 未登录状态 去登录
                }else {
                    if (Constant.VIP_LOCK_STATUS == 1){ // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                        //showPromptMessage(getResources().getString(R.string.error_business_vip));
                        showRemindDialog(getResources().getString(R.string.business_error_remind));
                    }else {
                        mPresenter.doQuote(isQuoteEnable, id, status, breed, qty, address, dateStr);   // 报价
                    }

                }

            }
        });

        // 商家会员 点击 进行 复议报价
        mPresenter.getAdapter().setOnReviewClickListener(new OnReviewBtClickListener() {
            @Override
            public void onReviewBtClicked(String id, String price) {
                if (Constant.VIP_LOCK_STATUS == 1){
                    //showPromptMessage(getResources().getString(R.string.error_business_vip));
                    showRemindDialog(getResources().getString(R.string.business_error_remind));
                }else {
                    mPresenter.doReview(id, price);   // 复议报价
                }

            }
        });
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);
    }


    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    // 提示 开通商家会员/ 商家会员认证
    @Override
    public void showRemindView(String remind, String buttonTitle) {
        remindLinear.setVisibility(View.VISIBLE);
        remindTv.setText(remind);
        remindBt.setText(buttonTitle);
    }

    @Override
    public void hideRemindView() {
        remindLinear.setVisibility(View.GONE);
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
    public void onRefresh() {
        LogUtils.printInfo(TAG, "onRefresh ============ refreshData  currentMode: " + mPresenter.getCurrentMode());
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

                                switch (mPresenter.getCurrentMode()){
                                    case ALL_DATA_MODE:
                                        mPresenter.getPurchasePoolData();
                                        break;
                                    case MY_DATA_MODE:
                                        switch (Constant.VIP_TYPE){
                                            case -1:
                                                // 普通用户
                                                break;
                                            case 0:
                                                // 资讯会员
                                                break;
                                            case 1:
                                                // 交易会员
                                                mPresenter.getMyPurchaseListData();
                                                break;
                                            case 2:
                                                // 商家会员
                                                mPresenter.getMyQuoteListData();
                                                break;
                                            default:
                                                // 未登录
                                                break;
                                        }
                                        break;
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
