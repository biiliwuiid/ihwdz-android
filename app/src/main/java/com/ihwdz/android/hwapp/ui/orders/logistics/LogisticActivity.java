package com.ihwdz.android.hwapp.ui.orders.logistics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.orders.filter.CityFilterActivity;
import com.ihwdz.android.hwapp.ui.orders.query.QueryResultActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LogisticActivity extends BaseActivity {

    String TAG = "LogisticActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.tv_city_from) TextView fromCityTv;  // 始发地 - 省市
    @BindView(R.id.tv_area_from) TextView fromTv;      // 始发地
    @BindView(R.id.tv_city_destination) TextView destinationCityTv; // 目的地 - 省市
    @BindView(R.id.tv_area_destination) TextView destinationTv;     // 目的地
    @BindView(R.id.edit_amount) EditText amountEt;   // 运输数量 单位吨
    @BindView(R.id.iv_switch) ImageView switchIv;

    boolean isFromAddress = false;

    private String originStr, destinationStr;

    @Override
    public int getContentView() {
        return R.layout.activity_logistic;
    }

    @Override
    public void initView() {
        initToolbar();
        originStr = getResources().getString(R.string.from_district);            // 始发地
        destinationStr = getResources().getString(R.string.destination_district);// 目的地
        fromCityTv.setText(Constant.provFrom + "/" + Constant.cityFrom);
        fromTv.setText(Constant.distinctFrom);
        destinationCityTv.setText(Constant.provTo +"/"+ Constant.cityTo);
        destinationTv.setText(Constant.distinctTo);
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.logistics_per));
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        String cityFrom = "";
        if (TextUtils.equals(Constant.provFrom, Constant.cityFrom)){
            cityFrom = getResources().getString(R.string.municipal_district);

        }else {
            cityFrom = Constant.cityFrom;
        }
        String cityTo = "";
        if (TextUtils.equals(Constant.provTo, Constant.cityTo)){
            cityTo = getResources().getString(R.string.municipal_district);

        }else {
            cityTo = Constant.cityTo;
        }

        fromCityTv.setText(Constant.provFrom + "/" + cityFrom);
        fromTv.setText(Constant.distinctFrom);
        destinationCityTv.setText(Constant.provTo +"/"+ cityTo);
        destinationTv.setText(Constant.distinctTo);
    }


    // 点击 查询
    @OnClick(R.id.bt_query)
    public void onQueryBtClicked(){
        // 判断 始发地 和目的地
        if (checkIsDestinationSelected()){
            // 判断 运输数量
            if (amountEt.getText() != null && amountEt.getText().toString().length() > 0){
                Intent intent = new Intent(LogisticActivity.this, QueryResultActivity.class);
                intent.putExtra("AMOUNT_TON", amountEt.getText().toString());
                startActivity(intent);
            }else {
                showPromptMessage(getResources().getString(R.string.amount_need_remind));
            }
        }


    }

    // 点击始发地
    @OnClick(R.id.tv_city_from)
    public void onCityFromClicked(){
        isFromAddress = true;
        chooseCity();
    }
    // 点击始发地
    @OnClick(R.id.tv_area_from)
    public void onAreaFromClicked(){
        isFromAddress = true;
        chooseCity();
    }

    // 点击目的地
    @OnClick(R.id.tv_city_destination)
    public void onCityDestinationClicked() {
        isFromAddress = false;
        if (checkIsFromSelected()){
            chooseCity();
        }
    }

    // 点击目的地
    @OnClick(R.id.tv_area_destination)
    public void onAreaDestinationClicked(){
        isFromAddress = false;
        if (checkIsFromSelected()){
            chooseCity();
        }
    }

    public void chooseCity(){
        Intent intent = new Intent(LogisticActivity.this, CityFilterActivity.class);
        intent.putExtra("FROM_ADDRESS", isFromAddress);
        startActivity(intent);
    }

    // 检查是否选择了始发地
    public boolean checkIsFromSelected() {
        String fromStr =  fromTv.getText().toString();
        // 不为null 且不是 “始发地”
        if (fromStr!= null && !TextUtils.equals(fromStr, originStr)){
            return true;
        }else {
            showPromptMessage(getResources().getString(R.string.from_select_remind));
        }
        return false;
    }

    // 检查是否选择了目的地
    public boolean checkIsDestinationSelected() {
        if (checkIsFromSelected()){
            String fromStr =  destinationTv.getText().toString();
            // 不为null 且不是 “目的地”
            if (fromStr!= null && !TextUtils.equals(fromStr, destinationStr)){
                return true;
            }else { // 提示选择目的地
                showPromptMessage(getResources().getString(R.string.destination_select_remind));
            }
        }

        return false;
    }

    public void showPromptMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
