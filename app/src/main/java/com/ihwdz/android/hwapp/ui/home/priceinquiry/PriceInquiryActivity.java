package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.ToggleRadioButton;



import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.AREA;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.BRAND;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.BREED;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.FACTORY_PRICE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.MARKET_PRICE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.MENU_DATE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.MENU_HIDE;
import static com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract.SPEC;

/**
 *  查价格 - 未登录时只允许 查看一页内容
 */
public class PriceInquiryActivity extends BaseActivity implements PriceInquiryContract.View, View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "PriceInquiryActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.radio_right) RadioButton rightBt; // 我的收藏
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.horizontalRecycler) RecyclerView breedsRecyclerView;

    //@BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.market) TextView marketTv;
    @BindView(R.id.factory) TextView factoryTv;


    @BindView(R.id.group) RadioGroup group;
    @BindView(R.id.radio1) ToggleRadioButton breedRadio;      // 种类 needn't now

    @BindView(R.id.radio2) ToggleRadioButton specRadio;       // 型号
    @BindView(R.id.radio3) ToggleRadioButton brandRadio;      // 厂商
    @BindView(R.id.radio4) ToggleRadioButton cityRadio;       // 城市（市场价: "销售地"/ 出厂价: "区域"）
    @BindView(R.id.radio_date) ToggleRadioButton dateRadio;   // 日期

    @BindView(R.id.refresher) SwipeRefreshLayout refresher;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.empty_tv) TextView emptyTv;

    @BindView(R.id.layout_fuzzy_search) LinearLayout mMenuRecyclerViewLayout;      // 点击某项菜单 后 弹出布局 - 显示或隐藏
    @BindView(R.id.recycler_fuzzy_search) RecyclerView mMenuRecyclerView;          // 点击某项菜单 后 弹出 该菜单 下的选项 数据


//    @BindView(R.id.type) TextView breedTv;
//    @BindView(R.id.manufacturer) TextView brandTv;
//    @BindView(R.id.sell_area) TextView areaTv;

    String areaStr = "";     // 销售地
    String regionsStr = "";  // 区域


    @BindView(R.id.layout_load_more)LinearLayout loadMoreLinear;     // "查看更多"
    @BindView(R.id.tv_more)TextView moreTv;
    @BindView(R.id.bt_more)Button moreBt;

    private Handler handler = new Handler();
    private boolean isScrollDown; //是否是向下滑动

    @Inject PriceInquiryContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_price_inquiry;
    }

    @Override
    public void initView() {
        initToolBar();
        initBreedsRecyclerView();
        initMenuRecyclerView();
        initRecyclerView();
    }


    @Override
    public void initListener() {
        refresher.setOnRefreshListener(this);
        marketTv.setOnTouchListener(this);
        factoryTv.setOnTouchListener(this);
    }

    @Override
    public void initData() {
        mPresenter.getBreedData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.LOGOUT || Constant.token == null){
            // 未登录 (显示 “查看更多”)
            showLoadMore();
        }else {
            // 已登录（隐藏 “查看更多”）
            hideLoadMore();
        }
        // 加载价格数据
        if (!mPresenter.getIsLoadingData()){
            mPresenter.refreshData();
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

    // 市场价
    @Override
    public void initMarketPriceView() {
        LogUtils.printCloseableInfo(TAG, "市场价view");
        cityRadio.setText(areaStr);    // 销售地
        mPresenter.getBreedData();
    }

    // 出厂价
    @Override
    public void initFactoryPriceView() {
        LogUtils.printCloseableInfo(TAG, "出厂价view");
        cityRadio.setText(regionsStr);  // 区域
        mPresenter.getBreedData();
    }

    @Override
    public void showMenuView() {
        LogUtils.printCloseableInfo(TAG, "showMenuView ===============");
        hideEmptyView();
        mMenuRecyclerViewLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMenuView() {
        mMenuRecyclerViewLayout.setVisibility(View.GONE);
    }

    @Override
    public void updateSpecTitle(String title) {
        specRadio.setText(title);
    }

    @Override
    public void updateCityTitle(String title) {
        cityRadio.setText(title);
    }

    @Override
    public void updateBrandTitle(String title) {
        brandRadio.setText(title);
    }

    @Override
    public void updateDateTitle(String title) {
        dateRadio.setText(title);
    }

    @Override
    public void showLoadMore() {
        loadMoreLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadMore() {
        loadMoreLinear.setVisibility(View.GONE);
    }

    // 菜单栏 操作 品名
//    @OnCheckedChanged(R.id.radio1)
//    public void onRadio1CheckedChanged(){
//        if (breedRadio.isChecked()){
//            breedRadio.setTypeface(Typeface.DEFAULT_BOLD);
//            mPresenter.setCurrentMenu(BREED);
//        }else {
//            breedRadio.setTypeface(Typeface.DEFAULT);
//            if (mPresenter.getCurrentMenu() == BREED){ // 双击当前菜单
//                mPresenter.setCurrentMenu(MENU_HIDE);
//            }
//        }
//    }

    // 型号
    @OnCheckedChanged(R.id.radio2)
    public void onRadio2CheckedChanged(){
        if (specRadio.isChecked()){
            specRadio.getPaint().setFakeBoldText(true);
            mPresenter.setCurrentMenu(SPEC);
        }else {
            specRadio.getPaint().setFakeBoldText(false);
            if (mPresenter.getCurrentMenu() == SPEC){ // 双击当前菜单
                mPresenter.setCurrentMenu(MENU_HIDE);
            }
        }
    }

    // 厂商
    @OnCheckedChanged(R.id.radio3)
    public void onRadio3CheckedChanged(){
        if (brandRadio.isChecked()){
            brandRadio.getPaint().setFakeBoldText(true);
            mPresenter.setCurrentMenu(BRAND);
        }else {
            brandRadio.getPaint().setFakeBoldText(false);
            if (mPresenter.getCurrentMenu() == BRAND){ // 双击当前菜单
                mPresenter.setCurrentMenu(MENU_HIDE);
            }
        }


    }

    // 城市（销售地/区域）
    @OnCheckedChanged(R.id.radio4)
    public void onRadio4CheckedChanged(){
        if (cityRadio.isChecked()){
            cityRadio.getPaint().setFakeBoldText(true);
            mPresenter.setCurrentMenu(AREA);
        }else {
            cityRadio.getPaint().setFakeBoldText(false);
            if (mPresenter.getCurrentMenu() == AREA){ // 双击当前菜单
                mPresenter.setCurrentMenu(MENU_HIDE);
            }
        }
    }

    // 日期
    @OnCheckedChanged(R.id.radio_date)
    public void onDateCheckedChanged(){
        if (dateRadio.isChecked()){
            dateRadio.getPaint().setFakeBoldText(true);
            mPresenter.setCurrentMenu(MENU_DATE);          // 弹出 底部日期选择器
        }else {
            dateRadio.getPaint().setFakeBoldText(false);
            if (mPresenter.getCurrentMenu() == MENU_DATE){ // 双击当前菜单  收起 底部日期选择器
                //mPresenter.setCurrentMenu(MENU_HIDE);      //
                mPresenter.selectDate();
            }
        }
    }

    @Override
    public void showEmptyView() {
        LogUtils.printCloseableInfo(TAG, "************ showEmptyView ===============");
        emptyTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        LogUtils.printCloseableInfo(TAG, "************* hideEmptyView ===============");
        emptyTv.setVisibility(View.GONE);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // 切换 市场价/出厂价 -> 销售地/区域
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            switch (v.getId()){
                case R.id.factory:
                    mPresenter.setCurrentMode(FACTORY_PRICE);
                    break;
                case R.id.market:
                    mPresenter.setCurrentMode(MARKET_PRICE);
                    break;
            }
        }
        return false;
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }

    // 点击“我的收藏” 未登录时去登录
    @OnClick(R.id.radio_right)
    public void onRightBtClicked(){
        rightBt.setChecked(false);
        if (Constant.LOGOUT || Constant.token == null){
            mPresenter.gotoLoginPage();
        }else {
            mPresenter.gotoMyCollection();
        }
    }

    @OnClick(R.id.bt_more)
    public void onMoreBtClicked(){
        if (Constant.LOGOUT || Constant.token == null){
            mPresenter.gotoLoginPage();
        }else {
            // 已登录 （按钮隐藏）
        }
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        rightBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_price));
        areaStr = getResources().getString(R.string.area_p);
        regionsStr = getResources().getString(R.string.regions_p);

        marketTv.getPaint().setFakeBoldText(true);
        factoryTv.getPaint().setFakeBoldText(true);
    }

    // breed recycler view
    private void initBreedsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        breedsRecyclerView.setLayoutManager(layoutManager);

        mPresenter.getBreedAdapter().addItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(String breed) {
                mPresenter.setCurrentBreed(breed);
            }
        });

        breedsRecyclerView.setAdapter(mPresenter.getBreedAdapter());
    }

    // price recycler view
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // 点击收藏按钮
        mPresenter.getAdapter().setOnPriceItemClickListener(new OnPriceItemClickListener() {
            @Override
            public void onPriceStarClicked(int collectionType, String breed, String spec, String brand, String area, boolean collect) {
                if (Constant.LOGOUT || Constant.token == null){
                    mPresenter.gotoLoginPage();
                }else {
                    if (collect){
                        mPresenter.doCollect(collectionType, breed, spec, brand, area);
                    }else {
                        // 已收藏
                        showPromptMessage(getResources().getString(R.string.collected_remind));
                    }
                }
            }
        });
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);

    }

    // menu recycler view菜单栏 数据
    private void initMenuRecyclerView() {
        StaggeredGridLayoutManager layoutManager =
        // RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();
//                LogUtils.printInfo(TAG, "========= =============onMeasure state.getItemCount(): " + count);
//                LogUtils.printInfo(TAG, "========= onMeasure widthSpec: " + widthSpec);
//                LogUtils.printInfo(TAG, "========= onMeasure heightSpec: " + heightSpec);

                if (count > 0) {
                    if (count / 3 <= 1) {
                        count = 1;
                    }
                    if (count > 21) {
                        count = 21;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for (int i = 0; i <= count / 3; i++) {
//                        LogUtils.printInfo(TAG, "========= i: " + i);
                        View view = recycler.getViewForPosition(0);
                        if (view != null) {
                            measureChild(view, widthSpec, heightSpec);
                            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                            int measuredHeight = view.getMeasuredHeight();
//                            LogUtils.printInfo(TAG, "========= onMeasure measuredWidth: " + measuredWidth);
//                            LogUtils.printInfo(TAG, "========= onMeasure measuredHeight: " + measuredHeight);

                            realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                            realHeight += measuredHeight;
//                            LogUtils.printInfo(TAG, "========= ==onMeasure realWidth: " + realWidth);
//                            LogUtils.printInfo(TAG, "========= ==onMeasure realHeight: " + realHeight);
                        }
                        setMeasuredDimension(realWidth, realHeight);
                    }
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }

            }
        };
        mMenuRecyclerView.setLayoutManager(layoutManager);
        mPresenter.getMenuAdapter().addItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onItemChecked(String string) {
                // 选中某项 数据 后 查询数据
                mPresenter.setCurrentPageNum(1);
                mPresenter.setCurrentMenuSelected(string); // 选中某项菜单栏下的某个选项卡时
            }
        });
        mMenuRecyclerView.setAdapter(mPresenter.getMenuAdapter());
    }



    /**
     *  RecyclerView.OnScrollListener1
     */
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
                        if (Constant.LOGOUT || Constant.token == null){
                            // 未登录 不加载数据
                        }else {
                            // 已登录
                            // 此处调用 加载更多回调接口 的回调方法
                            // set footer : loading
                            mPresenter.getAdapter().setIsLoadMore(true);  // add footer
                            mPresenter.getAdapter().setLoadMoreStatus(Constant.loadStatus.STATUS_LOADING); // set footer : loading


                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //load more
                                    mPresenter.getPriceData();

                                }
                            }, 2000);
                        }


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





//
//    @OnClick(R.id.type_layout)
//    public void onSpecClicked(){
//        Log.d(TAG, "onSpecClicked");
//        showSingleAlertDialog(mPresenter.getSpecList(), "Spec", SPEC);
//    }
//    @OnClick(R.id.manufacturer_layout)
//    public void onBrandClicked(){
//        Log.d(TAG, "onBrandClicked");
//        mPresenter.getBrandList();
//        showSingleAlertDialog(mPresenter.getBrandList(), "Brand", BRAND);
//    }
//    @OnClick(R.id.sell_area_layout)
//    public void onAreaClicked(){
//        Log.d(TAG, "onAreaClicked");
//        showSingleAlertDialog(mPresenter.getAreaList(), "Area", AREA);
//    }

    //单选框
//    private AlertDialog alertDialog2;
//    public void showSingleAlertDialog(final List<String> list, String title, final int type){
//        String[] strs = list.toArray(new String[list.size()]);
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//        alertBuilder.setTitle(title);
//        alertBuilder.setSingleChoiceItems(strs, 0, new DialogInterface.OnClickListener() {
//            String result ="";
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.d(TAG, "SELECT: "+ list.get(i));
//
//                Log.d(TAG, "SELECT: position:================= "+ i);
//                Log.d(TAG, "SELECT:  list.size():================================= "+ list.size());
//                Log.d(TAG, "SELECT:  list.get(i):================================= "+ list.get(i));
//                result = list.get(i);
//                switch (type){
//                    case SPEC:
//                        mPresenter.setCurrentSpec(result);
//                        break;
//                    case BRAND:
//                        mPresenter.setCurrentBrand(result);
//                        break;
//                    case AREA:
//                        mPresenter.setCurrentArea(result);
//                        break;
//                }
//            }
//        });
//
//        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                alertDialog2.dismiss();
//
//            }
//
//        });
//
//        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                alertDialog2.dismiss();
//
//            }
//        });
//
//        alertDialog2 = alertBuilder.create();
//        alertDialog2.show();
//    }

}
