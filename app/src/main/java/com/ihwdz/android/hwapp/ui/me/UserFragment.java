package com.ihwdz.android.hwapp.ui.me;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :  Personal Center (未登录)
 * version: 1.0
 * </pre>
 */
public class UserFragment extends BaseFragment<UserPresenter> implements UserContract.View{

    String TAG = "UserFragment";

    @BindView(R.id.iv_msg) ImageView msgIv;                         //　右上角消息提醒
    @BindView(R.id.login_layout) RelativeLayout loginLayout;        //　登陆
    @BindView(R.id.tv_expireTime) TextView tvExpireTime;            //　ExpireTime

//    @BindView(R.id.collection) LinearLayout collectionLayout;       //　我的收藏
//    @BindView(R.id.deals_record) LinearLayout dealLayout;           //　交易记录

    @BindView(R.id.linear01) RelativeLayout myVipLayout;              //　物流小助手
    @BindView(R.id.grey_line01) View greyLine01;

    @BindView(R.id.linear1) RelativeLayout hwVipCnLayout;             //　鸿网公众号
    @BindView(R.id.grey_line1) View greyLine1;

    @BindView(R.id.linear2) RelativeLayout aboutUsLayout;             //　关于我们
    @BindView(R.id.grey_line2) View greyLine2;

    @BindView(R.id.linear3) RelativeLayout customerServiceLayout;     //　联系客服
    @BindView(R.id.grey_line3) View greyLine3;

    @BindView(R.id.linear4) RelativeLayout settingsLayout;            //　设置

    @BindView(R.id.tv_telephone) TextView tv_tel;                     //　客服电话

    private UserContract.Presenter mPresenter = new UserPresenter(this);
    private MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public void initView() {
        // Linkify.addLinks(tv_tel, Linkify.PHONE_NUMBERS);
        // tv_tel.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        tvExpireTime.setVisibility(View.GONE); // 隐藏 会员过期日期
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initLoginSuccess() {

    }

    // 我的收藏
    @OnClick(R.id.iv_collection)
    public void onCollectionsClicked(){
        mPresenter.gotoCollections();
    }
    // 消息中心
    @OnClick(R.id.iv_msg)
    public void onMsgClicked(){
        mPresenter.gotoMsgCenter();
    }

    // 登录/注册
    @OnClick(R.id.login_layout)
    public void onLoginClicked(){
        mPresenter.gotoLoginPage();
    }

//   // 我的收藏
//    @OnClick(R.id.collection)
//    public void onCollectionClicked(){
//        Log.d(TAG, "onMsgClicked");
//        showMyCollections();
//    }
//
//    // 交易记录
//    @OnClick(R.id.deals_record)
//    public void onDealsRecordClicked(){
//        Log.d(TAG, "onDealsRecordClicked");
//        showDealRecords();
//    }

    // 物流小助手
    @OnClick(R.id.linear01)
    public void onLogisticsClicked(){
        mPresenter.gotoLogistics();
    }

    // 鸿网公众号
    @OnClick(R.id.linear1)
    public void onHwVipCnClicked(){
        mPresenter.gotoHwVipCn();
    }

    // 关于我们
    @OnClick(R.id.linear2)
    public void onAboutUsClicked(){
        mPresenter.gotoAboutUs();
    }

    // 设置
    @OnClick(R.id.linear4)
    public void onSettingsClicked(){
        mPresenter.gotoSettings();
    }


}
