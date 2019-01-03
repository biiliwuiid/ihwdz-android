package com.ihwdz.android.hwapp.ui.me.infovip;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.records.RecordsActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc : 资讯会员
 * version: 1.0
 * </pre>
 */
public class InfoVipFragment extends BaseFragment implements InfoVipContract.View {

    String TAG = "InfoVipFragment";

    @BindView(R.id.iv_msg) ImageView msgIv;                         //　右上角消息提醒
    @BindView(R.id.login_layout) RelativeLayout loginLayout;        //　登陆 -> 完善信息
    @BindView(R.id.tv_user_state) TextView tvUser;                  //　登陆 -> accountNo
    @BindView(R.id.tv_expireTime) TextView tvExpireTime;            //　ExpireTime

//    @BindView(R.id.collection) LinearLayout collectionLayout;       //　我的收藏
//    @BindView(R.id.deals_record) LinearLayout dealLayout;           //　交易记录

    @BindView(R.id.linear01) RelativeLayout myVipLayout;            //　我的资讯会员
    @BindView(R.id.grey_line01) View greyLine01;
    @BindView(R.id.boundary01) View greyBoundary01;

    @BindView(R.id.linear1) RelativeLayout openDealVipLayout;       //　我的交易会员
    @BindView(R.id.grey_line1) View greyLine1;
    @BindView(R.id.boundary) View greyBoundary;

    @BindView(R.id.linear2) RelativeLayout logisticsLayout;         //　物流小助手
    @BindView(R.id.grey_line2) View greyLine2;

    @BindView(R.id.linear3) RelativeLayout customerServiceLayout;   //　联系客服 - hide
    @BindView(R.id.grey_line3) View greyLine3;

    @BindView(R.id.linear4) RelativeLayout settingsLayout;          //　设置

    @BindView(R.id.iv01)ImageView iv01;
    @BindView(R.id.tv01)TextView tv01;

    @BindView(R.id.iv1)ImageView iv1;
    @BindView(R.id.tv1)TextView tv1;

    @BindView(R.id.iv2)ImageView iv2;
    @BindView(R.id.tv2)TextView tv2;


    private MainActivity activity;
    private InfoVipContract.Presenter mPresenter = new InfoVipPresenter(this);

    private String mAccount;             // 账号
    private long mExpireTime;            // 权益+期限

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
//        return R.layout.fragment_user_info;
    }

    @Override
    public void initView() {
       initUserInfoView();
       initInfoVipView();

    }

    void initUserInfoView(){
        mAccount = Constant.user_account;
        tvUser.setText(mAccount);  // 账号

        LogUtils.printInfo(TAG, "Constant.endDate: "+ Constant.endDate);
        LogUtils.printInfo(TAG, "Constant.APPLY_STATUS: " + Constant.APPLY_STATUS);
        if (Constant.endDateStr.length() <= 1 ){
            tvExpireTime.setVisibility(View.GONE);
        }else {
            tvExpireTime.setVisibility(View.VISIBLE);
            tvExpireTime.setText(Constant.endDate);  // 权益 + 有效期
        }
    }

    void initInfoVipView(){

        // 我的资讯会员
        iv01.setImageDrawable(getResources().getDrawable(R.drawable.mvip));
        tv01.setText(getResources().getString(R.string.title_info_vip));
        greyLine01.setVisibility(View.GONE);
        greyBoundary01.setVisibility(View.VISIBLE);

        // 我的交易会员
        iv1.setImageDrawable(getResources().getDrawable(R.drawable.mvip));
        tv1.setText(getResources().getString(R.string.title_deal_vip));

        greyLine1.setVisibility(View.GONE);
        greyBoundary.setVisibility(View.VISIBLE);

        // 物流小助手
        iv2.setImageDrawable(getResources().getDrawable(R.drawable.logistics));
        tv2.setText(getResources().getString(R.string.logistics_per));

        // hide tel
        customerServiceLayout.setVisibility(View.GONE);
        greyLine3.setVisibility(View.GONE);

        switch (Constant.VIP_TYPE){
            case -1:// 普通用户 hide myVipLayout
                myVipLayout.setVisibility(View.GONE);
                greyLine01.setVisibility(View.GONE);
                greyBoundary01.setVisibility(View.GONE);
                break;
            case 0: // 资讯会员
                break;
        }
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

    // 用户信息
    @OnClick(R.id.login_layout)
    public void onLoginClicked(){
        mPresenter.gotoUpdateUserInfo();
    }



    // 我的资讯会员
    @OnClick(R.id.linear01)
    public void onMyVipClicked(){
        mPresenter.gotoMyVipInfo();
    }

    // 点击 我的交易会员
    @OnClick(R.id.linear1)
    public void onQuotaClicked(){
        mPresenter.gotoOpenDealVip();
    }

    // 点击 物流小助手
    @OnClick(R.id.linear2)
    public void onLogisticsClicked(){
        mPresenter.gotoLogistics();
    }

    // 点击 设置
    @OnClick(R.id.linear4)
    public void onSettingsClicked(){
        mPresenter.gotoSettings();
    }



    /**
     *  implement Contract.View =============================
     */

    // 交易记录
    public void showDealRecords() {
        Intent intent = new Intent(activity.getBaseContext(), RecordsActivity.class);
        startActivity(intent);
    }

}
