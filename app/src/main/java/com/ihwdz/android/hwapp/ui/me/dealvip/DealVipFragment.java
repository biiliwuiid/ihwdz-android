package com.ihwdz.android.hwapp.ui.me.dealvip;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist.PurchaseListActivity;
import com.ihwdz.android.hwapp.ui.me.records.RecordsActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc : 交易会员： 1. 我的交易会员 （2.查看额度）(+ 5.保证金) 3.物流小助手  4. 设置
 * version: 1.0
 * </pre>
 */
public class DealVipFragment extends BaseFragment implements DealVipContract.View{

    String TAG = "DealVipFragment";

    @BindView(R.id.iv_msg) ImageView msgIv;                         //　右上角消息提醒
    @BindView(R.id.login_layout) RelativeLayout loginLayout;        //　登陆 -> 完善信息
    @BindView(R.id.tv_user_state) TextView tvUser;                  //　登陆 -> accountNo
    @BindView(R.id.tv_expireTime) TextView tvExpireTime;            //　ExpireTime

//    @BindView(R.id.collection) LinearLayout collectionLayout;       //　我的收藏
//    @BindView(R.id.deals_record) LinearLayout dealLayout;           //　交易记录

    @BindView(R.id.linear01) RelativeLayout myVipLayout;            //　物流小助手 -> 我的交易会员
    @BindView(R.id.grey_line01) View greyLine01;

    @BindView(R.id.linear1) RelativeLayout quotaLayout;             //　鸿网公众号 ->查看额度
    @BindView(R.id.tv_quota) TextView quotaTv;                      //　额度
    @BindView(R.id.grey_line1) View greyLine1;

    @BindView(R.id.linear_deposit) RelativeLayout depositLayout;    //　保证金
    @BindView(R.id.tv_deposit) TextView depositTv;                  //　保证金value
    @BindView(R.id.boundary) View greyBoundary;

    @BindView(R.id.linear2) RelativeLayout logisticsLayout;         //　关于我们 -> 物流小助手
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
    private DealVipContract.Presenter mPresenter = new DealVipPresenter(this);

    private String mAccount;          // 账号
    boolean isAuthenticated = false;  // 是否通过认证
    String currencySign;              // 货币符号

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
    }

    @Override
    public int getContentView() {
        //return R.layout.fragment_user;
        return R.layout.fragment_user_deal;
    }

    @Override
    public void initView() {
        currencySign = getResources().getString(R.string.currency_sign);
        initUserInfoView();
        //initDealVipView();     // 用了单独的fragment_user_deal 取代 fragment_user
        checkIsAuthenticated();  // 检验 交易会员是否认证
    }

    void initUserInfoView(){
        mAccount = Constant.user_account;
        tvUser.setText(mAccount);  // 账号

        if (Constant.endDateStr.length() <= 1 ){
            tvExpireTime.setVisibility(View.GONE);
        }else {
            tvExpireTime.setVisibility(View.VISIBLE);
            tvExpireTime.setText(Constant.endDate);  // 权益 + 有效期
        }
    }

    void initDealVipView(){

        // 我的交易会员
        iv01.setImageDrawable(getResources().getDrawable(R.drawable.mvip));
        tv01.setText(getResources().getString(R.string.title_deal_vip));

        // 查看额度
        iv1.setImageDrawable(getResources().getDrawable(R.drawable.quota));
        tv1.setText(getResources().getString(R.string.quota_check_per));
        greyLine1.setVisibility(View.GONE);
        greyBoundary.setVisibility(View.VISIBLE);

        // 物流小助手
        iv2.setImageDrawable(getResources().getDrawable(R.drawable.logistics));
        tv2.setText(getResources().getString(R.string.logistics_per));

        // hide tel
        customerServiceLayout.setVisibility(View.GONE);
        greyLine3.setVisibility(View.GONE);

        checkIsAuthenticated();

    }


    // 检验 交易会员是否认证
    void checkIsAuthenticated(){
        isAuthenticated = Constant.isAuthenticated;
        if (isAuthenticated){
            initLevel1View();
        }else {
            initLevel0View();
        }
    }
    // 未认证
    void initLevel0View(){
        quotaLayout.setVisibility(View.GONE);
        greyLine1.setVisibility(View.GONE);
    }

    // 已认证
    void initLevel1View(){
        quotaLayout.setVisibility(View.VISIBLE);
        greyLine1.setVisibility(View.VISIBLE);
        quotaTv.setVisibility(View.VISIBLE);
        quotaTv.setText(currencySign + Constant.totalAmount);
    }

    // 未缴纳保证金
    void initDepositNullView(){
        depositLayout.setVisibility(View.GONE);
        depositTv.setVisibility(View.GONE);
    }
    // 已缴纳保证金
    void initDepositView(){
        depositLayout.setVisibility(View.VISIBLE);
        depositTv.setVisibility(View.VISIBLE);
        depositTv.setText(currencySign + Constant.depositAmount);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        mPresenter.getApplyStateData();// 获取保证金 获取开通交易会员结果
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



    // 我的交易会员
    @OnClick(R.id.linear01)
    public void onMyVipClicked(){
        mPresenter.gotoMyVipInfo();
    }

    // 点击 查看额度
    @OnClick(R.id.linear1)
    public void onQuotaClicked(){
        mPresenter.gotoCheckQuota();
    }

    // 点击 保证金
    @OnClick(R.id.linear_deposit)
    public void onDepositClicked(){
        mPresenter.gotoDeposit();
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

    //  交易记录
    public void showDealRecords() {
        Intent intent = new Intent(activity.getBaseContext(), RecordsActivity.class);
        startActivity(intent);
    }


    //  我的求购单
    public void showMyPurchaseList() {
        Intent intent = new Intent(activity.getBaseContext(), PurchaseListActivity.class);
        startActivity(intent);
    }

    // 查看额度 保证金
    @Override
    public void updateView(String quota, String deposit) {

        if (quota != null && quota.length() > 0){
            initLevel1View();
        }else {
            initLevel0View();
        }
        if (deposit != null && deposit.length() > 0){
            initDepositView();
        }else {
            initDepositNullView();
        }
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

}
