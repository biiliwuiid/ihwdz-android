package com.ihwdz.android.hwapp.ui.me.improveinfo;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.EnterpriseInformation;
import com.ihwdz.android.hwapp.model.bean.UserInformation;
import com.ihwdz.android.hwapp.ui.me.infoupdate.InfoUpdateActivity;
import com.ihwdz.android.hwapp.ui.orders.filter.CityFilterActivity;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *  完善用户信息
 */
public class ImproveInfoActivity extends BaseActivity implements ImproveInfoContract.View{

    String TAG = "ImproveInfoActivity";

    String titleStr = "";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.tv_name) TextView tvName;                    // 姓名
    @BindView(R.id.linear1) RelativeLayout linear1;

    @BindView(R.id.tv_tel) TextView tvTel;                      // 电话
    @BindView(R.id.linear_tel) RelativeLayout linearTel;

    @BindView(R.id.tv_email) TextView tvEmail;                   // 电子邮件
    @BindView(R.id.linear2) RelativeLayout linear2;

    //@BindView(R.id.tv_enterprise_name) TextView tvEnterpriseName;
    @BindView(R.id.tv_district) TextView tvDistrict;             // 地区
    @BindView(R.id.linear3) RelativeLayout linear3;

    //@BindView(R.id.tv_enterprise_type) TextView tvEnterpriseType;
    @BindView(R.id.tv_address_detail) TextView tvDetailAddress;   // 详细地址
    @BindView(R.id.linear4) RelativeLayout linear4;

    //@BindView(R.id.tv_enterprise_nature) TextView tvEnterpriseNature;
    //@BindView(R.id.linear5) RelativeLayout linear5;

    @Inject
    CompositeSubscription mSubscriptions;

    final int UPDATE_NAME = 1;
    final int UPDATE_EMAIL = 2;
    final int UPDATE_ADDRESS = 3;

    static final String NAME_INDEX = "name";
    static final String EMAIL_INDEX = "email";
    static final String ADDRESS_INDEX = "address";

    String name = "name";
    String email = "email";
    String address = "address";

    List<EnterpriseInformation.EnterpriseBean> companyTypeList;
    List<EnterpriseInformation.EnterpriseBean> companyNatureList;

    @Inject ImproveInfoContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_improve_info;
    }

    @Override
    public void initView() {
        Log.d(TAG, "------------- initView --------------");
        if (getIntent()!= null){
            Log.d(TAG, "============ getIntent()!= null: ");

            if (getIntent().getStringExtra(NAME_INDEX) != null){
                name = getIntent().getStringExtra(NAME_INDEX);
                //Log.d(TAG, "============ NAME: " + name);

            }
            if (getIntent().getStringExtra(EMAIL_INDEX) != null){
                email = getIntent().getStringExtra(EMAIL_INDEX);
                //Log.d(TAG, "============ email: " + email);

            }
            if (getIntent().getStringExtra(ADDRESS_INDEX) != null){
                address = getIntent().getStringExtra(ADDRESS_INDEX);
                //Log.d(TAG, "============ ADDRESS_INDEX: " + email);

            }
        }
        titleStr = getResources().getString(R.string.title_improve);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);

        tvName.setText(name);
        tvEmail.setText(email);
        tvDetailAddress.setText(address);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //getEnterpriseData();  // 获取所有企业类型
        getUserData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateView();
    }

    private void updateView() {
        tvName.setText(Constant.name);
        tvTel.setText(Constant.tel);
        tvEmail.setText(Constant.email);

        String district = Constant.provinceName +" "+Constant.cityName+" "+Constant.districtName;
        tvDistrict.setText(district);
//        String detailAddress = district +" "+ Constant.address;
        String detailAddress = Constant.address;
        tvDetailAddress.setText(detailAddress);

    }


    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    @OnClick({R.id.linear1, R.id.linear2, R.id.linear3, R.id.linear4, R.id.linear5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear1:  // update name
                InfoUpdateActivity.startInfoUpdateActivity(this, Constant.InfoUpdate.INFO_USER, getResources().getString(R.string.name), tvName.getText().toString());
//                Intent intent = new Intent(ImproveInfoActivity.this, InfoUpdateActivity.class);
//                intent.putExtra("title", getResources().getString(R.string.name));
//                intent.putExtra("content", tvName.getText());
//                startActivity(intent);
                //finish();
                break;
            case R.id.linear2:  // update email
                InfoUpdateActivity.startInfoUpdateActivity(this, Constant.InfoUpdate.INFO_USER, getResources().getString(R.string.email), tvEmail.getText().toString());

//                Intent emailIntent = new Intent(ImproveInfoActivity.this, InfoUpdateActivity.class);
//                emailIntent.putExtra("title", getResources().getString(R.string.email));
//                emailIntent.putExtra("content", tvEmail.getText());
//                startActivity(emailIntent);
                //finish();
                break;
            case R.id.linear3:  // update district
                Intent districtIntent = new Intent(ImproveInfoActivity.this, CityFilterActivity.class);
                districtIntent.putExtra("VIP_DISTRICT",true);
                startActivity(districtIntent);
                break;
            case R.id.linear4: // update detail address
                InfoUpdateActivity.startInfoUpdateActivity(this, Constant.InfoUpdate.INFO_USER, getResources().getString(R.string.address_detail), tvDetailAddress.getText().toString());
//                Intent addressIntent = new Intent(ImproveInfoActivity.this, InfoUpdateActivity.class);
//                addressIntent.putExtra("title", getResources().getString(R.string.address_detail));
//                addressIntent.putExtra("content", tvDetailAddress.getText());
//                startActivity(addressIntent);
                break;
//            case R.id.linear5:
//                break;
        }
    }


    public void getEnterpriseData(){
        LoginDataModel model = new LoginDataModel(this);
        Subscription rxSubscription = model
                .getEnterpriseInfo()
                .compose(RxUtil.<EnterpriseInformation>rxSchedulerHelper())
                .subscribe(new Subscriber<EnterpriseInformation>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "------- onCompleted ---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "------- onError ---------" +e.toString());
                    }

                    @Override
                    public void onNext(EnterpriseInformation data) {
                        Log.d(TAG, "------- onNext ---------");
                        Log.d(TAG, "------- data: " + data.msg);
                        if ("0".equals(data.code)) {
                            if (data.data!= null){
                                Log.e(TAG, "EnterpriseInformation:companyNatureList: "+ data.data.companyNatureList.size());
                                Log.e(TAG, "EnterpriseInformation:companyTypeList: "+ data.data.comapnyTypeList.size());
                                companyNatureList = data.data.companyNatureList;
                                companyTypeList = data.data.comapnyTypeList;
                                Log.e(TAG, "companyNatureList: "+ companyNatureList.size());
                                Log.e(TAG, "companyTypeList: "+ companyTypeList.size());
                            }
                        }else {
                            Toast.makeText(getBaseContext(), data.msg,Toast.LENGTH_SHORT).show();

                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }

    public void getUserData(){
        Log.d(TAG, "=========== getUserData ====================== token:" + Constant.token);
        LoginDataModel model = new LoginDataModel(this);
        Subscription rxSubscription = model
                .getUserInfo(Constant.token)
                .compose(RxUtil.<UserInformation>rxSchedulerHelper())
                .subscribe(new Subscriber<UserInformation>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "------- onCompleted ---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "------- onError ---------" +e.toString());
                    }

                    @Override
                    public void onNext(UserInformation data) {
                        Log.d(TAG, "------- onNext ---------");
                        Log.d(TAG, "------- data: " + data.msg);
                        if ("0".equals(data.code)) {
                            if (data.data!= null){
                                Constant.name = data.data.name;
                                Constant.tel = data.data.mobile;
                                Constant.email = data.data.email;

                                Constant.provinceName = data.data.province;
                                Constant.provinceCode = data.data.provinceCode;
                                Constant.cityName = data.data.city;
                                Constant.cityCode = data.data.cityCode;
                                Constant.districtName = data.data.district;
                                Constant.districtCode = data.data.districtCode;
                                Constant.address = data.data.address;

                                Constant.companyFullName = data.data.companyFullName;
                                Constant.companyNature = data.data.companyNature;
                                Constant.companyType = data.data.companyType;

                                updateView();
                                //updateUserInfoView(data.data);   // 更新用户信息
                            }
                        }else {
                            Toast.makeText(getBaseContext(), data.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }

//    private void updateUserInfoView(String name,String email,String companyNature, String companyType,String companyFullName){
    private void updateUserInfoView(UserInformation.UserInfo userInfo){
        tvName.setText(userInfo.name);
        tvTel.setText(userInfo.mobile);
        tvEmail.setText(userInfo.email);
        String district = userInfo.province +""+userInfo.city+""+userInfo.district;
        tvDistrict.setText(district);
        String detailAddress = district + userInfo.address;
        tvDetailAddress.setText(detailAddress);

        //tvEnterpriseName.setText(userInfo.companyFullName);
        //tvEnterpriseNature.setText(getCompanyNature(userInfo.companyNature));
        //tvEnterpriseType.setText(getCompanyType(userInfo.companyType));
        //Log.d(TAG, "getCompanyNature: " + getCompanyNature(userInfo.companyNature));
        //Log.d(TAG, "getCompanyType: " + getCompanyType(userInfo.companyType));

    }

    String getCompanyType(String index){
        String result = "";
        if (companyTypeList != null && companyTypeList.size()>0){
            Log.e(TAG, "============== getCompanyType ============== " +index);
            for (int i = 0; i < companyTypeList.size(); i++){
                EnterpriseInformation.EnterpriseBean temp = companyTypeList.get(i);
                Log.e(TAG, "temp: " + temp.value +" - "+ temp.content);
                if (index.equals(temp.value)){
                    Log.e(TAG, "find it  " +index.equals(temp.value));
                    result = temp.content;
                    return result;
                }
            }
        }
        return result;
    }

    String getCompanyNature(String index){
        String result = "";
        if (companyNatureList != null && companyNatureList.size()>0){
            Log.e(TAG, "============== getCompanyNature ============== " +index);
            for (int i = 0; i < companyNatureList.size(); i++){
                EnterpriseInformation.EnterpriseBean temp = companyNatureList.get(i);
                Log.e(TAG, "temp: " + temp.value +" - "+ temp.content);


                if (index.equals(temp.value)){
                    Log.e(TAG, "find it  " +index.equals(temp.value));
                    result = temp.content;
                    return result;
                }
            }
        }
        return result;
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
    }

}
