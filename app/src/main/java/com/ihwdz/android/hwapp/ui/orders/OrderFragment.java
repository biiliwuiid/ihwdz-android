package com.ihwdz.android.hwapp.ui.orders;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.home.infoday.OnItemTitleClickListener;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.Arrays;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_ALL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_COMPLETE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_BILL;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_DELIVER;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_INVOICE;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_PAY;
import static com.ihwdz.android.hwapp.ui.orders.OrderContract.INDEX_ORDER_WAIT_TAKE;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :   订单 Fragment 订单
 * 全部订单/ 待付款/ 待发货/ 待收货/  待开票 - 交易
 * 全部订单/ 待收款/ 待发货/ 待收货/交易成功 - 商家
 * version: 1.0
 * </pre>
 */
public class OrderFragment extends BaseFragment<OrderPresenter> implements OrderContract.View, SwipeRefreshLayout.OnRefreshListener {

    String TAG = "OrderFragment";
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.title_recycler_view) RecyclerView titleRecyclerView;  // 订单种类

    @BindView(R.id.empty) LinearLayout emptyLinear;

    @BindView(R.id.grey_line) View boundaryView;

    @BindView(R.id.refresher) SwipeRefreshLayout refresher;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private OrderContract.Presenter mPresenter = new OrderPresenter(this);

    private MainActivity activity;

    private Handler handler = new Handler();
    private boolean isScrollDown;              // 是否是向下滑动

    private String ALL = "";     // 全部订单
    private String PAY = "";     // 待付款 - 交易
    private String DELIVER = ""; // 待发货
    private String TAKE = "";    // 待收货
    private String INVOICE = ""; // 待开票 - 交易
    private String COMPLETE = "";// 交易成功 - 商家
    private String BILL = "";    // 待收款 - 商家

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {
        initStrings();
        initToolBar();
        initTitleRecyclerView();
        initOrderRecyclerView();

    }

    private void initStrings() {
        ALL = getResources().getString(R.string.order_all);               // 全部订单
        PAY = getResources().getString(R.string.order_wait_pay);          // 待付款 - 交易
        DELIVER = getResources().getString(R.string.order_wait_deliver);  // 待发货
        TAKE = getResources().getString(R.string.order_wait_take);        // 待收货
        INVOICE = getResources().getString(R.string.order_wait_invoice);  // 待开票 - 交易
        COMPLETE = getResources().getString(R.string.order_success);     // 交易成功 - 商家
        BILL = getResources().getString(R.string.order_wait_collection);  // 待收款 - 商家
    }

    private void initToolBar() {
        title.setText(getResources().getString(R.string.order_title));
    }

    // 初始化 标题栏
    private void initTitleRecyclerView() {

        String[] titles = getResources().getStringArray(R.array.order_deal_title);
        int vipType = Constant.VIP_TYPE; // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
        switch (vipType){
            case -1:
                break;
            case 0:
                break;
            case 1: // 交易会员
                LogUtils.printCloseableInfo(TAG, "交易会员");
                titles = getResources().getStringArray(R.array.order_deal_title);
                break;
            case 2: // 商家会员
                LogUtils.printCloseableInfo(TAG, "商家会员");
                titles = getResources().getStringArray(R.array.order_business_title);
                break;
            default:
                LogUtils.printCloseableInfo(TAG, "未登录");
                break;
        }

//        if (activity.getCurrentFragment() == 2){
//            LogUtils.printCloseableInfo(TAG, "===================== OrderFragment");
//        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        titleRecyclerView.setLayoutManager(layoutManager);
        mPresenter.getTitleAdapter().setDataList(Arrays.asList(titles));
        titleRecyclerView.setAdapter(mPresenter.getTitleAdapter());

        mPresenter.getTitleAdapter().addItemTitleCheckListener(new OnItemTitleClickListener() {
            @Override
            public void onItemTitleClick(String title) {
                LogUtils.printCloseableInfo(TAG, "===================== title："+ title);
                // 更换数据
                if (TextUtils.equals(ALL, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_ALL);
                }
                else if (TextUtils.equals(BILL, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_WAIT_BILL);
                }
                else if (TextUtils.equals(PAY, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_WAIT_PAY);
                }
                else if (TextUtils.equals(DELIVER, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_WAIT_DELIVER);
                }
                else if (TextUtils.equals(TAKE, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_WAIT_TAKE);
                }
                else if (TextUtils.equals(INVOICE, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_WAIT_INVOICE);
                }
                else if (TextUtils.equals(COMPLETE, title)){
                    mPresenter.setCurrentMode(INDEX_ORDER_COMPLETE);
                }

            }
        });


    }

    // 初始化 订单列表
    private void initOrderRecyclerView() {
        // init order content
        LinearLayoutManager contentLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(contentLayoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);

        // 点击 查看订单详情
        mPresenter.getAdapter().setOnItemClickListener(new OnOrderItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClicked(String id, String orderStatus, int orderOption) {
                mPresenter.gotoDetail(id, orderStatus, orderOption);
            }
        });

        // 点击 支付手续费
        mPresenter.getAdapter().setOnPayBtClickListener(new OnOrderItemClickListener.OnPayClickListener() {
            @Override
            public void onPayClicked(String id, String orderSn, String charge) {
                mPresenter.gotoPay(id, orderSn, charge);
            }
        });

        // 点击 申请开票
        mPresenter.getAdapter().setOnInvoiceBtClickListener(new OnOrderItemClickListener.OnInvoiceClickListener() {
            @Override
            public void onInvoiceClicked(String id) {
                mPresenter.gotoInvoice(id);
            }
        });

        // 点击 申请展期
        mPresenter.getAdapter().setOnExtensionBtClickListener(new OnOrderItemClickListener.OnExtensionClickListener() {
            @Override
            public void onExtensionClicked(String id) {
                mPresenter.gotoExtension(id);
            }
        });

    }

    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
    }

    @Override
    public void initData() {

    }


    // 每次进入都刷新数据
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.refreshData();

        // 支付手续费，申请展期，申请开票等操作完成后返回  加载数据 & 隐藏按钮
//        if (Constant.orderOptionDone){
//            mPresenter.refreshData();
//        }

    }

    // 登录成功后重新加载数据
    @Override
    public void initLoginSuccess() {
        LogUtils.printCloseableInfo(TAG, "=========== initLoginSuccess ==============");
        initTitleRecyclerView();
        initOrderRecyclerView();
        mPresenter.setCurrentPageNum(1);
        mPresenter.refreshData();
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
    public void showPromptMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        emptyLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        emptyLinear.setVisibility(View.GONE);
    }


    int distance = 0;
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
            distance += dy;
            if (distance >= 10){
                boundaryView.setVisibility(View.GONE);
            }else {
                boundaryView.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // Log.d(TAG, " ================================================================  onScrollStateChanged ===============: newState: " + newState );
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

                    /**
                     * Recommend more
                     */
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
                                mPresenter.getAllData();

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


    @Override
    public void onRefresh() {
        mPresenter.refreshData();
    }



}
