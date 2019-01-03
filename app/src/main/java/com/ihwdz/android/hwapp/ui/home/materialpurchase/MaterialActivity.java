package com.ihwdz.android.hwapp.ui.home.materialpurchase;

import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceInquiryContract;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.CleanableEditText;
import com.ihwdz.android.hwapp.widget.ToggleRadioButton;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.PageNum;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.ALL_DATA;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.BRAND;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.BREED;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.CITY;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.KEYWORDS_DATA;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.MENU_HIDE;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.PageSize;
import static com.ihwdz.android.hwapp.ui.home.materialpurchase.MaterialContract.SPEC;

public class MaterialActivity  extends BaseActivity implements MaterialContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "MaterialActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    // @BindView(R.id.iv_right) ImageView rightBt; // 物性表
    @BindView(R.id.tv_title) TextView title;

    //@BindView(R.id.editText) EditText editText;
    @BindView(R.id.editText) CleanableEditText mEditSearchInput; // 搜索框

    @BindView(R.id.empty_tv) TextView emptyTv;

    @BindView(R.id.group) RadioGroup group;
    @BindView(R.id.radio1) ToggleRadioButton breedRadio;  // 种类
    @BindView(R.id.radio2) ToggleRadioButton specRadio;   // 牌号
    @BindView(R.id.radio3) ToggleRadioButton cityRadio;   // 城市
    @BindView(R.id.radio4) ToggleRadioButton brandRadio;  // 厂商

    @BindView(R.id.tv1) TextView breedTv;
    @BindView(R.id.iv1) ImageView breedIv;
    @BindView(R.id.tv2) TextView specTv;
    @BindView(R.id.iv2) ImageView specIv;
    @BindView(R.id.tv3) TextView cityTv;
    @BindView(R.id.iv3) ImageView cityIv;
    @BindView(R.id.tv4) TextView brandTv;
    @BindView(R.id.iv4) ImageView brandIv;

    @BindView(R.id.refresher) SwipeRefreshLayout refresher;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.layout_fuzzy_search) LinearLayout mMenuRecyclerViewLayout;      // 点击某项菜单 后 弹出布局 - 显示或隐藏
    @BindView(R.id.recycler_fuzzy_search) RecyclerView mMenuRecyclerView;          // 点击某项菜单 后 弹出 该菜单 下的选项 数据
    RecyclerView.LayoutManager layoutManager;

    private Handler handler = new Handler();

    private boolean isScrollDown;  //是否是向下滑动
    @Inject MaterialContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_material;
    }

    @Override
    public void initView() {
        initToolBar();
        refresher.setOnRefreshListener(this);
        initRecyclerView();
        initMenuRecyclerView();
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
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.setSearchKeywords(s.toString().trim());
            }
        });

        mEditSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    LogUtils.printInfo(TAG, "=========== actionId == EditorInfo.IME_ACTION_SEARCH" );
                    hideKeyboard();
                    mPresenter.setCurrentPageNum(1);
                    mPresenter.getSearchData(PageNum, PriceInquiryContract.PageSize);
                    return true;
                }
                return false;
            }
        });
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
    public void updateBreedTitle(String title) {
        breedRadio.setText(title);
        //breedRadio.setChecked(false);
    }

    @Override
    public void updateSpecTitle(String title) {
        specRadio.setText(title);
        //specRadio.setChecked(false);
    }

    @Override
    public void updateCityTitle(String title) {
        cityRadio.setText(title);
        //cityRadio.setChecked(false);
    }

    @Override
    public void updateBrandTitle(String title) {
        brandRadio.setText(title);
        //brandRadio.setChecked(false);
    }

    // 菜单栏 操作
    @OnCheckedChanged(R.id.radio1)
    public void onRadio1CheckedChanged(){
        if (breedRadio.isChecked()){
            breedRadio.getPaint().setFakeBoldText(true);
            mPresenter.setCurrentMenu(BREED);
        }else {
            breedRadio.getPaint().setFakeBoldText(false);
            if (mPresenter.getCurrentMenu() == BREED){ // 双击当前菜单
                mPresenter.setCurrentMenu(MENU_HIDE);
            }
        }
    }

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

    @OnCheckedChanged(R.id.radio3)
    public void onRadio3CheckedChanged(){
        if (cityRadio.isChecked()){
            cityRadio.getPaint().setFakeBoldText(true);
            mPresenter.setCurrentMenu(CITY);
        }else {
            cityRadio.getPaint().setFakeBoldText(false);
            if (mPresenter.getCurrentMenu() == CITY){ // 双击当前菜单
                mPresenter.setCurrentMenu(MENU_HIDE);
            }
        }
    }

    @OnCheckedChanged(R.id.radio4)
    public void onRadio4CheckedChanged(){
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

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // 左键 - 回退
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }
    // 右键 - 物性表
    @OnClick(R.id.iv_right)
    public void onRightClicked(){
        // TODO: 2018/8/24 物性表 - hide now
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        // rightBt.setImageDrawable(this.getResources().getDrawable(R.drawable.physical_property_ma));
        // rightBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_ma));
    }

    // 原料 数据
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    // 菜单栏 数据
    private void initMenuRecyclerView() {

        //layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mMenuRecyclerView.setAdapter(mPresenter.getMenuAdapter());
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                int count = state.getItemCount();
//                LogUtils.printInfo(TAG, "========= =============onMeasure state.getItemCount(): " + count);
//                LogUtils.printInfo(TAG, "========= onMeasure widthSpec: " + widthSpec);
//                LogUtils.printInfo(TAG, "========= onMeasure heightSpec: " + heightSpec);

                if (count > 0) {
                    if (count/3 <= 1 ){
                        count = 1;
                    }
                    if(count > 21){
                        count = 21;
                    }
                    int realHeight = 0;
                    int realWidth = 0;
                    for(int i = 0;i <= count/3 ; i++){
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
        //mMenuRecyclerView.setLayoutManager(layoutManager);
        mPresenter.getMenuAdapter().addItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onItemChecked(String string) {
                // 选中某项 数据 后 查询数据
                mPresenter.setCurrentPageNum(1);
                mPresenter.setCurrentMenuSelected(string);
            }
        });

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
            if (newState == SCROLL_STATE_IDLE){

                /**
                 *  method1
                 *
                 */

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
                LogUtils.printInfo(TAG, "onScrollStateChanged---------------");
                if (lastVisibleItem >= totalItemCount - 1 && isScrollDown) {
                    LogUtils.printInfo(TAG, "lastVisibleItem---------------" + lastVisibleItem +"  | totalItemCount: "+totalItemCount);
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
                                    case ALL_DATA:
                                        mPresenter.getAllData();
                                        break;
                                    case KEYWORDS_DATA:
                                        mPresenter.getSearchData(mPresenter.getCurrentPageNum(), PageSize);
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


    /**
     * may be not use
     */
    /*

        @Override
    public void initBreedSelected(String str) {
        normalStyle(breedTv, breedIv);
        breedTv.setText(str);
    }

    @Override
    public void initSpecSelected(String str) {
        normalStyle(specTv, specIv);
        specTv.setText(str);
    }

    @Override
    public void initCitySelected(String str) {
        normalStyle(cityTv, cityIv);
        cityTv.setText(str);
    }

    @Override
    public void initBrandSelected(String str) {
        normalStyle(brandTv, brandIv);
        brandTv.setText(str);
    }

     // bold style
    public void boldStyle(TextView tv, ImageView iv){
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.pull_up));

    }
    public void normalStyle(TextView tv, ImageView iv){
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(false);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.pull_white));
    }

    @OnClick(R.id.layout1)
    public void onBreedClicked(){
        Log.d(TAG, "onSpecClicked");
        // change bold style (bold -> normal; normal -> bold)
        mPresenter.setIsBreedBold( !mPresenter.getIsBreedBold());
        if (mPresenter.getIsBreedBold()){
            boldStyle(breedTv, breedIv);

            normalStyle(specTv, specIv);
            normalStyle(cityTv, cityIv);
            normalStyle(brandTv, brandIv);
        }else {
            normalStyle(breedTv, breedIv);
            normalStyle(specTv, specIv);
            normalStyle(cityTv, cityIv);
            normalStyle(brandTv, brandIv);
        }
        showSingleAlertDialog(mPresenter.getBreedList(), "Breeds", BREED);
    }
    @OnClick(R.id.layout2)
    public void onSpecClicked(){
        Log.d(TAG, "onSpecClicked");
        mPresenter.setIsSpecBold(!mPresenter.getIsSpecBold());
        if (mPresenter.getIsSpecBold()){
            boldStyle(specTv, specIv);

            normalStyle(breedTv, breedIv);
            normalStyle(cityTv, cityIv);
            normalStyle(brandTv, brandIv);
        }else {
            normalStyle(breedTv, breedIv);
            normalStyle(specTv, specIv);
            normalStyle(cityTv, cityIv);
            normalStyle(brandTv, brandIv);
        }
        showSingleAlertDialog(mPresenter.getSpecList(), "Specs", SPEC);
    }
    @OnClick(R.id.layout3)
    public void onCityClicked(){
        Log.d(TAG, "onSpecClicked");
        mPresenter.setIsCityBold(!mPresenter.getIsCityBold());
        if (mPresenter.getIsCityBold()){
            boldStyle(cityTv, cityIv);

            normalStyle(breedTv, breedIv);
            normalStyle(specTv, specIv);
            normalStyle(brandTv, brandIv);
        }else {
            normalStyle(breedTv, breedIv);
            normalStyle(specTv, specIv);
            normalStyle(cityTv, cityIv);
            normalStyle(brandTv, brandIv);
        }
        showSingleAlertDialog(mPresenter.getCityList(), "Cities", CITY);
    }
    @OnClick(R.id.layout4)
    public void onBrandClicked(){
        Log.d(TAG, "onSpecClicked");
        mPresenter.setIsBrandBold(mPresenter.getIsBrandBold());
        if (mPresenter.getIsBrandBold()){
            boldStyle(brandTv, brandIv);

            normalStyle(breedTv, breedIv);
            normalStyle(specTv, specIv);
            normalStyle(cityTv, cityIv);
        }else{
            normalStyle(breedTv, breedIv);
            normalStyle(specTv, specIv);
            normalStyle(cityTv, cityIv);
            normalStyle(brandTv, brandIv);
        }
        showSingleAlertDialog(mPresenter.getBrandList(), "Brands", BRAND);
    }

    //单选框
    private AlertDialog alertDialog2;
    public void showSingleAlertDialog(final List<String> list, String title, final int type){
        String[] strs = list.toArray(new String[list.size()]);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(title);
        alertBuilder.setSingleChoiceItems(strs, 0, new DialogInterface.OnClickListener() {
            String result ="";
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "SELECT: "+ list.get(i));

                Log.d(TAG, "SELECT: position:================= "+ i);
                Log.d(TAG, "SELECT:  list.size():================================= "+ list.size());
                Log.d(TAG, "SELECT:  list.get(i):================================= "+ list.get(i));
                result = list.get(i);
                switch (type){
                    case BREED:
                        mPresenter.setCurrentBreed(result);
                        initBreedSelected(result);
                        break;
                    case SPEC:
                        mPresenter.setCurrentSpec(result);
                        break;
                    case BRAND:
                        mPresenter.setCurrentBrand(result);
                        break;
                    case CITY:
                        mPresenter.setCurrentCity(result);
                        break;
                }
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();

            }

        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();

            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }
     */

}
