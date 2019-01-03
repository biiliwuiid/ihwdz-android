package com.ihwdz.android.hwapp.ui.orders.filter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.model.bean.CityResultBean;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  除了物流   其余选择地址都读本地json文件
 *  1.物流 2.会员信息 3.新建仓库
 */
public class CityFilterActivity extends BaseActivity implements CityFilterContract.View {

    String TAG = "CityFilterActivity";
    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;


    @BindView(R.id.recyclerView_province)
    RecyclerView provinceRecyclerView;

    @BindView(R.id.recyclerView_city)
    RecyclerView cityRecyclerView;

    @BindView(R.id.recyclerView_area)
    RecyclerView areaRecyclerView;

    boolean isFromAddress = false;       // 物流出发地
    boolean isVipDistrict = false;       // 用户信息修改
    boolean isWarehouseDistrict = false; // 仓库地址
    boolean isAddressDistrict = false;   // 收货地址

    @Inject CityFilterContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_city_filter;
    }

    @Override
    public void initView() {

        if (getIntent() != null){
            LogUtils.printInfo(TAG, "=======  getIntent() != null  ====== ");
            isFromAddress = getIntent().getBooleanExtra("FROM_ADDRESS", false);
            isVipDistrict = getIntent().getBooleanExtra("VIP_DISTRICT", false);
            isWarehouseDistrict = getIntent().getBooleanExtra("WAREHOUSE", false);
            isAddressDistrict = getIntent().getBooleanExtra("ADDRESS", false);
        }
        mPresenter.setIsFromAddress(isFromAddress);
        mPresenter.setIsVipDistrict(isVipDistrict);
        mPresenter.setIsWarehouse(isWarehouseDistrict);
        mPresenter.setIsAddress(isAddressDistrict);

        initToolBar();
        initProvinceRecyclerView();
        initCityRecyclerView();
        initAreaRecyclerView();
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if (isVipDistrict || isWarehouseDistrict  || isAddressDistrict){
            LogUtils.printCloseableInfo(TAG, "isVipDistrict: " + isVipDistrict);
            LogUtils.printCloseableInfo(TAG, "isWarehouseDistrict: " + isWarehouseDistrict);
            LogUtils.printCloseableInfo(TAG, "isAddressDistrict: " + isAddressDistrict);
            // 我的会员信息 - 选择所在地 - 读取本地Json文件
            mPresenter.getCityData();
        }else {
            LogUtils.printInfo(TAG, "isFromAddress: "+isFromAddress);
            if(isFromAddress){
                mPresenter.getFromData();
            }else {
                mPresenter.getDestinationData();
            }
        }

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        Log.d(TAG, "onBackPressed");
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_choose_district));
    }

    // Province RecyclerView
    private void initProvinceRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        provinceRecyclerView.setLayoutManager(layoutManager);
        if (isVipDistrict || isWarehouseDistrict || isAddressDistrict){
            mPresenter.getProvinceAdapter2().setIsVip(true);
            provinceRecyclerView.setAdapter(mPresenter.getProvinceAdapter2());
            mPresenter.getProvinceAdapter2().addItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(LogisticsCityData.CityEntity data) {

                }

                @Override
                public void onItemClicked(CityResultBean data) {
                    if (data != null && !data.equals(mPresenter.getCurrentProvince2())){
                        mPresenter.setCurrentProvince2(data);
                        if (data.children != null){
                            Log.d(TAG, "========= ON province clicked: data.children.size() =========  " + data.children.size());
                        }else {
                            Log.d(TAG, "========== ON province : data.children == null");
                        }
                        mPresenter.getCityAdapter2().setData2List(data.children);
                    }
                }
            });
        } else {
            provinceRecyclerView.setAdapter(mPresenter.getProvinceAdapter());
            mPresenter.getProvinceAdapter().addItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(LogisticsCityData.CityEntity data) {
                    if (data != null && !data.equals(mPresenter.getCurrentProvince())){
                        mPresenter.setCurrentProvince(data);
                        if (data.children != null){
                            Log.d(TAG, "========= ON province clicked: data.children.size() =========  " + data.children.size());
                        }else {
                            Log.d(TAG, "========== ON province : data.children == null");
                        }
                        mPresenter.getCityAdapter().setDataList(data.children);
                    }
                }

                @Override
                public void onItemClicked(CityResultBean data) {

                }
            });
        }

    }
    // City RecyclerView
    private void initCityRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cityRecyclerView.setLayoutManager(layoutManager);
        if (isVipDistrict || isWarehouseDistrict || isAddressDistrict){
            mPresenter.getCityAdapter2().setIsVip(true);
            cityRecyclerView.setAdapter(mPresenter.getCityAdapter2());
            mPresenter.getCityAdapter2().addItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(LogisticsCityData.CityEntity data) {

                }

                @Override
                public void onItemClicked(CityResultBean data) {
                    if (data != null && !data.equals(mPresenter.getCurrentCity2())){
                        mPresenter.setCurrentCity2(data);
                        if (data.children != null){
                            Log.d(TAG, "================= ON City clicked: data.children.size(): ===== " + data.children.size());
                        }else {
                            Log.d(TAG, "================== ON City : data.children == null");
                        }
                        mPresenter.getAreaAdapter2().setData2List(data.children);
                    }
                }
            });
        }else {
            cityRecyclerView.setAdapter(mPresenter.getCityAdapter());
            mPresenter.getCityAdapter().addItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(LogisticsCityData.CityEntity data) {
                    if (data != null && !data.equals(mPresenter.getCurrentCity())){
                        mPresenter.setCurrentCity(data);
                        if (data.children != null){
                            LogUtils.printInfo(TAG, "================= ON City clicked: data.children.size(): ===== " + data.children.size());
                        }else {
                            LogUtils.printInfo(TAG, "================== ON City : data.children == null");
                        }
                        mPresenter.getAreaAdapter().setDataList(data.children);
                    }
                }

                @Override
                public void onItemClicked(CityResultBean data) {

                }
            });
        }

    }
    // Area(District) RecyclerView
    private void initAreaRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        areaRecyclerView.setLayoutManager(layoutManager);
        if (isVipDistrict || isWarehouseDistrict || isAddressDistrict){
            /**
             * 会员信息 & 添加仓库 & 添加收货地址
             */
            mPresenter.getAreaAdapter2().setIsVip(true);
            areaRecyclerView.setAdapter(mPresenter.getAreaAdapter2());
            mPresenter.getAreaAdapter2().addItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(LogisticsCityData.CityEntity data) {
                }

                @Override
                public void onItemClicked(CityResultBean data) {
                    if (data != null && !data.equals(mPresenter.getCurrentArea2())){
                        mPresenter.setCurrentArea2(data);
                        // save vip district
                        mPresenter.saveSelectedDistrict();
                        finish();
                    }
                }
            });
        }else {
            /**
             * 物流
             */
            areaRecyclerView.setAdapter(mPresenter.getAreaAdapter());
            mPresenter.getAreaAdapter().addItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClicked(LogisticsCityData.CityEntity data) {
                    if (data != null && !data.equals(mPresenter.getCurrentArea())){
                        mPresenter.setCurrentArea(data);
                        mPresenter.saveAddress();
                        finish();
                    }
                }

                @Override
                public void onItemClicked(CityResultBean data) {
                }
            });
        }

    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string,Toast.LENGTH_SHORT).show();
    }
}
