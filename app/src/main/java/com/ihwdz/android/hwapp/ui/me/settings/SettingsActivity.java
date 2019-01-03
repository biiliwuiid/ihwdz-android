package com.ihwdz.android.hwapp.ui.me.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.HwVIPCN.HwVipCNActivity;
import com.ihwdz.android.hwapp.ui.me.aboutus.AboutActivity;
import com.ihwdz.android.hwapp.ui.me.feedback.FeedbackActivity;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * if is not login: -> LoginPageActivity
 *  info_vip: -> only response + logout
 *  business_vip ->
 */

public class SettingsActivity extends BaseActivity implements SettingContract.View{

    String TAG = "SettingsActivity";
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title)TextView title;

    @BindView(R.id.linear1) RelativeLayout hwVipCnLayout;             //　鸿网公众号
    @BindView(R.id.linear2) RelativeLayout aboutUsLayout;             //　关于我们
    @BindView(R.id.linear3) RelativeLayout feedbackLayout;            //　意见反馈
    @BindView(R.id.linear4) RelativeLayout customerServiceLayout;     //　联系客服
    @BindView(R.id.tv_telephone) TextView tv_tel;     //　联系客服

    static final String TYPE_INDEX = "type";

    // 100 -> "未登录";  -1 -> "普通用户"; 0 -> "资讯会员";1 -> "交易会员"; 2 -> "商家会员"
    int type = -1;    // -1 -> "未登录"; 0 -> "资讯会员";1 -> "交易会员"; 2 -> "商家会员"
    String titleStr;

    @Inject SettingContract.Presenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void initView() {
        if (getIntent()!= null){
            type = getIntent().getIntExtra(TYPE_INDEX, -1);
        }
        updateSettingsView();
        titleStr = getResources().getString(R.string.settings_per);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
        // Linkify.addLinks(tv_tel, Linkify.PHONE_NUMBERS);

    }

    private void updateSettingsView() {
        // 之前设计的资讯会员的设置与其他 会员不同
//        switch (type){
//            case -1:
//                LoginPageActivity.startLoginPageActivity(this);
//                break;
//            case 0:
//                hwVipCnLayout.setVisibility(View.GONE);
//                aboutUsLayout.setVisibility(View.GONE);
//                customerServiceLayout.setVisibility(View.GONE);
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }

    public static void startSettingsActivity(Context context, int type ) {
        Intent intent = new Intent(context, SettingsActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(TYPE_INDEX, type);
        context.startActivity(intent);
    }


    // 鸿网公众号
    @OnClick(R.id.linear1)
    public void onHwVipCnClicked(){
        Intent intent = new Intent(SettingsActivity.this, HwVipCNActivity.class);
        startActivity(intent);
    }

    // 关于我们
    @OnClick(R.id.linear2)
    public void onAboutUsClicked(){
        Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    // 意见反馈
    @OnClick(R.id.linear3)
    public void onFeedbackClicked(){
        Intent intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.bt_logout)
    public void onLogoutClicked(){
        mPresenter.logout();
    }

    @Override
    public void showPersonalCenter() {
        MainActivity.startActivity(this, 4);
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
