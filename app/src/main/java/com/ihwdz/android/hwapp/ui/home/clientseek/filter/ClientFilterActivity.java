package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.MultiLineRadioGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterContract.ALL_MODE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterContract.MORE_MODE;

public class ClientFilterActivity extends BaseActivity implements ClientFilterContract.View, View.OnTouchListener{

    String TAG = "ClientFilterActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.iv_right)
    ImageView rightBt;

    @BindView(R.id.layout1)
    LinearLayout allLinear;   // 全部地区
    @BindView(R.id.layout2)
    LinearLayout moreLinear;  // 更多
    @BindView(R.id.tv_all)
    TextView allTv;
    @BindView(R.id.tv_more)
    TextView moreTv;
    @BindView(R.id.iv1)
    ImageView allIv;
    @BindView(R.id.iv2)
    ImageView moreIv;

    @BindView(R.id.frame_all)
    LinearLayout allFrame;
    @BindView(R.id.frame_more)
    LinearLayout moreFrame;

    @BindView(R.id.recyclerView_selected)
    RecyclerView selectedRecyclerView;

    @BindView(R.id.recyclerView_province)
    RecyclerView provinceRecyclerView;

    @BindView(R.id.recyclerView)
    RecyclerView cityRecyclerView;

    // 更多
    @BindView(R.id.title) TextView linkTitle;                      // 手机/邮箱
    @BindView(R.id.link_phone) CheckBox checkPhone;
    @BindView(R.id.link_email) CheckBox checkEmail;
    @BindView(R.id.recyclerView1) RecyclerView linkRecyclerView;


    @BindView(R.id.title_reg_money) TextView moneyTitle;            // 注册资本
    // @BindView(R.id.recyclerView2) RecyclerView moneyRecyclerView;
//    @BindView(R.id.group_money)RadioGroup moneyGroup;
    @BindView(R.id.group_money)MultiLineRadioGroup moneyGroup;
    @BindView(R.id.reg_money100)RadioButton radio100;
    @BindView(R.id.reg_money100_200)RadioButton radio100_200;
    @BindView(R.id.reg_money200_500)RadioButton radio200_500;
    @BindView(R.id.reg_money500_1000)RadioButton radio500_1000;
    @BindView(R.id.reg_money1000)RadioButton radio1000;

    @BindView(R.id.title_reg_date) TextView dateTitle;              // 注册时间
    // @BindView(R.id.recyclerView3) RecyclerView dateRecyclerView;
//    @BindView(R.id.group_date)RadioGroup dateGroup;
    @BindView(R.id.group_date)MultiLineRadioGroup dateGroup;
    @BindView(R.id.reg_date1)RadioButton radio1;
    @BindView(R.id.reg_date1_5)RadioButton radio1_5;
    @BindView(R.id.reg_date5_10)RadioButton radio5_10;
    @BindView(R.id.reg_date10_15)RadioButton radio10_15;
    @BindView(R.id.reg_date15)RadioButton radio15;

    String phoneString;
    String emailString;

    String money100;
    String money100_200;
    String money200_500;
    String money500_1000;
    String money1000;

    String year1;
    String year1_5;
    String year5_10;
    String year10_15;
    String year15;

    @Inject
    ClientFilterContract.Presenter mPresenter;


    @Override
    public int getContentView() {
        return R.layout.activity_client_filter;
    }

    @Override
    public void initView() {

        initString();
        initToolBar();
        initProvinceRecyclerView();
        initCityRecyclerView();
        initSelectedRecyclerView();

        // initLinkRecyclerView();
        // initMoneyRecyclerView();
        // initDateRecyclerView();
    }

    private void initString() {
        phoneString = getResources().getString(R.string.phone_fi);
        emailString = getResources().getString(R.string.email_fi);

        money100 = getResources().getString(R.string.money_100);
        money100_200 = getResources().getString(R.string.money_100_200);
        money200_500 = getResources().getString(R.string.money_200_500);
        money500_1000 = getResources().getString(R.string.money_500_1000);
        money1000 = getResources().getString(R.string.money_1000);

        year1 = getResources().getString(R.string.date_1);
        year1_5 = getResources().getString(R.string.date_1_5);
        year5_10 = getResources().getString(R.string.date_5_10);
        year10_15 = getResources().getString(R.string.date_10_15);
        year15 = getResources().getString(R.string.date_15);
    }


    @Override
    public void initListener() {
        allLinear.setOnTouchListener(this);
        moreLinear.setOnTouchListener(this);

    }

    @Override
    public void initData() {

        mPresenter.getAllData();
        LogUtils.printInfo(TAG, "initData ============= mPresenter.getSelectedCity().size(): "+ mPresenter.getSelectedCity().size());
        //mPresenter.getSelectedCity().size();

        // mPresenter.getLinkWayData();
        // mPresenter.getRegMoneyData();
        // mPresenter.getRegDateData();
    }

    @Override
    public void showWaitingRing() {
//        refresher.post(new Runnable() {
//            @Override
//            public void run() {
//                refresher.setRefreshing(true);
//            }
//        });
//        refresher.setEnabled(false);
    }

    @Override
    public void hideWaitingRing() {
//        refresher.post(new Runnable() {
//            @Override
//            public void run() {
//                refresher.setRefreshing(false);
//            }
//        });
//        refresher.setEnabled(true);
    }


    @Override
    public void initAllMode() {
        allTv.setTextColor(getResources().getColor(R.color.orangeTab));
        allIv.setImageDrawable(getResources().getDrawable(R.drawable.pull_up));
        moreTv.setTextColor(getResources().getColor(R.color.blackText3));
        moreIv.setImageDrawable(getResources().getDrawable(R.drawable.pull2));
        allFrame.setVisibility(View.VISIBLE);
        moreFrame.setVisibility(GONE);
    }

    @Override
    public void initMoreMode() {
        moreTv.setTextColor(getResources().getColor(R.color.orangeTab));
        moreIv.setImageDrawable(getResources().getDrawable(R.drawable.pull_up));
        allTv.setTextColor(getResources().getColor(R.color.blackText3));
        allIv.setImageDrawable(getResources().getDrawable(R.drawable.pull2));
        allFrame.setVisibility(GONE);
        moreFrame.setVisibility(View.VISIBLE);
    }



    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        Log.d(TAG, "onBackPressed");
        onBackPressed();
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_filter_cl));
    }

    // 点击 “全部地区”
    @OnClick(R.id.layout1)
    public void onALLBtClicked(){
        mPresenter.setCurrentMode(ALL_MODE);
    }
    // 点击 “更多”
    @OnClick(R.id.layout2)
    public void onMoreBtClicked(){
        mPresenter.setCurrentMode(MORE_MODE);
    }

    // 点击 “清除条件”
    @OnClick(R.id.bt_clear)
    public void onClearBtClicked(){
        mPresenter.clearConditions();
    }

    // 点击 “确认”
    @OnClick(R.id.bt_confirm)
    public void onConfirmBtClicked(){
        mPresenter.confirmation();
        //onBackPressed();
    }



    // Province RecyclerView
    private void initProvinceRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        provinceRecyclerView.setLayoutManager(layoutManager);
        provinceRecyclerView.setAdapter(mPresenter.getProvinceAdapter());

        mPresenter.getProvinceAdapter().addItemClickListener(new OnProvinceItemClickListener() {
            @Override
            public void onProvinceItemClicked(ProvinceData.Bean province, List<ProvinceData.Bean> cityList) {
                if (province != null && !province.equals(mPresenter.getCurrentProvince())){
                    // LogUtils.printInfo(TAG, "onProvinceItemClicked : " + cityList.size());
                    mPresenter.setCurrentProvince(province);
                    // 更新city 数据
                    mPresenter.getCityAdapter().clear();
                    mPresenter.getCityAdapter().setDataList(mPresenter.getCityFromBean(mPresenter.getCurrentProvince(), cityList));
                }

            }
        });
    }

    // City RecyclerView
    private void initCityRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cityRecyclerView.setLayoutManager(layoutManager);
        cityRecyclerView.setAdapter(mPresenter.getCityAdapter());
        mPresenter.getCityAdapter().setCitiesSelected(mPresenter.getSelectedCity());
        mPresenter.getCityAdapter().addItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onItemSelected(ProvinceData.City city) {
                LogUtils.printInfo(TAG, "City  onItemSelected + : " + city.mCity.name);
                mPresenter.addSelectedCity(city);
                LogUtils.printInfo(TAG, "City  currentSelectedCities + : " + mPresenter.getSelectedCity().size());
                mPresenter.addSelectedProvince(city.mProvince.name);
            }

            @Override
            public void onItemUnSelected(ProvinceData.City city) {
                LogUtils.printInfo(TAG, "City  onItemUnSelected - : " + city.mCity.name);
                mPresenter.removeSelectedCity(city);
                LogUtils.printInfo(TAG, "City  currentSelectedCities - : " + mPresenter.getSelectedCity().size());
                mPresenter.removeSelectedProvince(city.mProvince.name);
            }

        });

    }

    // Selected RecyclerView  已选区域 - 选中城市的省份
    private void initSelectedRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        selectedRecyclerView.setLayoutManager(layoutManager);
        selectedRecyclerView.setAdapter(mPresenter.getSelectedProvincesAdapter());
        mPresenter.getSelectedProvincesAdapter().addItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(String string) {
                LogUtils.printInfo(TAG, "initSelectedRecyclerView -- click and delete: " + string);
                // 1. delete all cities belong to this province
                // 2. delete this province
                mPresenter.clickToDeleteProvince(string);
            }
        });
    }


    // 联系方式
    private void initLinkRecyclerView() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        linkRecyclerView.setLayoutManager(layoutManager);
//        linkRecyclerView.setAdapter(mPresenter.getLinkWayAdapter());
//        mPresenter.getLinkWayAdapter().addItemCheckListener(new OnItemCheckListener() {
//            @Override
//            public void onItemCheckListener(String checkedStr) {
//
//                if (checkedStr.contains(phoneString)){
//                    mPresenter.setHasPhone("1");
//                }else {
//                    mPresenter.setHasEmail("1");
//                }
//            }
//
//            @Override
//            public void onItemCancelListener(String cancelStr) {
//                if (cancelStr.contains(phoneString)){
//                    mPresenter.setHasPhone("0");
//                }else {
//                    mPresenter.setHasEmail("0");
//                }
//
//            }
//        });
    }
    // 注册资金
    private void initMoneyRecyclerView() {
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        moneyRecyclerView.setLayoutManager(layoutManager);
//        moneyRecyclerView.setAdapter(mPresenter.getRegMoneyAdapter());
//        mPresenter.getRegMoneyAdapter().addItemCheckListener(new OnItemCheckListener() {
//            @Override
//            public void onItemCheckListener(String checkedStr) {
//                mPresenter.setStartMoney(checkedStr);
//            }
//
//            @Override
//            public void onItemCancelListener(String cancelStr) {
//                mPresenter.setStartMoney("");
//            }
//        });

    }
    // 注册时间
    private void initDateRecyclerView() {
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        dateRecyclerView.setLayoutManager(layoutManager);
//        dateRecyclerView.setAdapter(mPresenter.getRegDateAdapter());
//        mPresenter.getRegDateAdapter().addItemCheckListener(new OnItemCheckListener() {
//            @Override
//            public void onItemCheckListener(String checkedStr) {
//
//                mPresenter.setStartDate(checkedStr);
//            }
//
//            @Override
//            public void onItemCancelListener(String cancelStr) {
//                mPresenter.setStartDate("");
//            }
//        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            switch (v.getId()){
                case R.id.layout1:
                    mPresenter.setCurrentMode(ALL_MODE);  // 全部地区
                    break;
                case R.id.layout2:
                    mPresenter.setCurrentMode(MORE_MODE); // 更多
                    break;
            }
        }
        return false;
    }

    @Override
    public void getLinkWay() {
        if (checkPhone.isChecked()){
            mPresenter.setHasPhone("1");
        }
        if (checkEmail.isChecked()){
            mPresenter.setHasEmail("1");
        }

    }

    @Override
    public void getRegMoney() {
        if (radio100.isChecked()){
            mPresenter.setStartMoney("");
            mPresenter.setEndMoney("100");
        }
        if (radio100_200.isChecked()){
            mPresenter.setStartMoney("100");
            mPresenter.setEndMoney("200");
        }
        if (radio200_500.isChecked()){
            mPresenter.setStartMoney("200");
            mPresenter.setEndMoney("500");
        }

        if (radio500_1000.isChecked()){
            mPresenter.setStartMoney("500");
            mPresenter.setEndMoney("1000");
        }
        if (radio1000.isChecked()){
            mPresenter.setStartMoney("1000");
            mPresenter.setEndMoney("");
        }
    }

    @Override
    public void getRegDate() {
        LogUtils.printInfo(TAG, "getRegDate===================================");
        if (radio1.isChecked()){
            mPresenter.setStartDate("");
            mPresenter.setEndDate("1");
            LogUtils.printInfo(TAG, "radio1.isChecked()");
        }
        if (radio1_5.isChecked()){
            mPresenter.setStartDate("1");
            mPresenter.setEndDate("5");
            LogUtils.printInfo(TAG, "radio1_5.isChecked()");
        }
        if (radio5_10.isChecked()){
            mPresenter.setStartDate("5");
            mPresenter.setEndDate("10");
            LogUtils.printInfo(TAG, "radio5_10.isChecked()");
        }
        if (radio10_15.isChecked()){
            mPresenter.setStartDate("10");
            mPresenter.setEndDate("15");
            LogUtils.printInfo(TAG, "radio10_15.isChecked()");
        }
        if (radio15.isChecked()){
            mPresenter.setStartDate("15");
            mPresenter.setEndDate("");
            LogUtils.printInfo(TAG, "radio15.isChecked()");
        }
    }

    @Override
    public void cleanSelected() {
        // 全部地区 - 清空选择

        mPresenter.clearSelectedProvinces();
        mPresenter.getSelectedProvincesAdapter().clear(); // 清除已选区域省份
        mPresenter.clearSelectedCities();
        mPresenter.getCityAdapter().clearAllSelected(); // 清除所选城市

        // 更多 - 清空选择
        checkPhone.setChecked(false);
        checkEmail.setChecked(false);

        radio100.setChecked(false);
        radio100_200.setChecked(false);
        radio200_500.setChecked(false);
        radio500_1000.setChecked(false);
        radio1000.setChecked(false);

        radio1.setChecked(false);
        radio1_5.setChecked(false);
        radio5_10.setChecked(false);
        radio10_15.setChecked(false);
        radio15.setChecked(false);

//        switch (mPresenter.getCurrentMode()){
//            case ALL_MODE:
//                mPresenter.getSelectedAdapter().clear();          // 清除已选区域
//                mPresenter.getCityAdapter().setNothingSelected(); // 清除所选城市
//                break;
//            case MORE_MODE:
//                // 清空选择
//                //mPresenter.getLinkWayAdapter().clear();
//                checkPhone.setChecked(false);
//                checkEmail.setChecked(false);
//
//                radio100.setChecked(false);
//                radio100_200.setChecked(false);
//                radio200_500.setChecked(false);
//                radio500_1000.setChecked(false);
//                radio1000.setChecked(false);
//
//                radio1.setChecked(false);
//                radio1_5.setChecked(false);
//                radio5_10.setChecked(false);
//                radio10_15.setChecked(false);
//                radio15.setChecked(false);
//                break;
//        }


    }


}

