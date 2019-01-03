package com.ihwdz.android.hwapp.ui.publish;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.publish.purchase.PurchaseActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/01
 * desc :   发布
 * version: 1.0
 * </pre>
 */
public class PublishFragment extends BaseFragment implements PublishContract.View{

    String TAG = "PublishFragment";

    @BindView(R.id.empty) LinearLayout empty;                     //　敬请期待

    @BindView(R.id.iv_purchase) ImageView purchaseIv;             //　我要采购

    @BindView(R.id.linear_purchase) LinearLayout purchaseLinear;  //　我要采购 LinearLayout
    @BindView(R.id.linear_quote) LinearLayout quoteLinear;        //　我要报价 LinearLayout

    private PublishContract.Presenter mPresenter = new PublishPresenter(this);
    private MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        mPresenter.bindActivity(activity);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_publish;
    }

    @Override
    public void initView() {
        // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员 + 3 交易未认证; 4 商家未认证; 5 交易失效; 6 商家失效; (8 种状态)
        int vipType = Constant.VIP_TYPE;
        switch (vipType){
            case 100:
                // 未登录
                showBoth();
                break;
            case -1:
                // 普通用户
                //break;
            case 0:
                // 资讯会员
                //break;
            case 1:
                // 交易
                showPurchaseView();
                break;
            case 2:
                // 商家
                showQuoteView();
                break;
            default:
                showWaitingView(); // 敬请期待
                break;
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //mPresenter.getUserType();
    }

    @Override
    public void initLoginSuccess() {
        mPresenter.getUserType();
        // -1 -普通用户； 0 -资讯； 1 -交易已认证； 2 -商家已认证； 3 -交易未认证； 4 -商家未认证； 5 -交易失效； 6 -商家失效;
        int vipType = Constant.VIP_TYPE;
        LogUtils.printCloseableInfo(TAG, "PublishFragment - initLoginSuccess - Constant.VIP_TYPE: " + vipType);
        switch (vipType){
            case 100:
                // 未登录
                showBoth();
                break;
            case -1:
                // 普通用户
                //break;
            case 0:
                // 资讯会员
                //break;
            case 1:
                // 交易
                showPurchaseView();
                break;
            case 2:
                // 商家
                showQuoteView();
                break;
            default:
                showWaitingView(); // 敬请期待
                break;
        }

    }


    // 点击 我要采购
    @OnClick(R.id.iv_purchase)
    public void onPurchaseClicked(){
        // -1 -普通用户； 0 -资讯； 1 -交易已认证； 2 -商家已认证； 3 -交易未认证； 4 -商家未认证； 5 -交易失效； 6 -商家失效;
        int vipType = Constant.VIP_TYPE;
        LogUtils.printCloseableInfo(TAG, "onPurchaseClicked - Constant.VIP_TYPE: " + vipType);
        switch (vipType){
            case 100:
                // 未登录
                mPresenter.gotoLoginPage();
                break;
            case -1:
            case 0:
                mPresenter.gotoApplyDealVip();
                break;
            case 1:
                // 交易 判断锁定状态 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                if (Constant.VIP_LOCK_STATUS == 1){
                    showPromptMessage(getResources().getString(R.string.error_deal_vip));
                }else {
                    mPresenter.gotoPublishPurchase();
                }
                break;
            default:
                break;
        }
    }

    // 点击 我要报价
    @OnClick(R.id.iv_quote)
    public void onQuoteClicked(){
        // -1 -普通用户； 0 -资讯； 1 -交易已认证； 2 -商家已认证； 3 -交易未认证； 4 -商家未认证； 5 -交易失效； 6 -商家失效;
        int vipType = Constant.VIP_TYPE;
        LogUtils.printCloseableInfo(TAG, "onQuoteClicked - Constant.VIP_TYPE: " + vipType);
        switch (vipType){
            case 100:
                // 未登录
                mPresenter.gotoLoginPage();
                break;
            case 2:
            case 4:
                // 商家
                mPresenter.gotoPurchasePool();
                break;
            default:
                mPresenter.gotoPurchasePool();
                break;
        }
    }

//    // 点击 X 按钮
//    @OnClick(R.id.iv_close)
//    public void onCloseBtClicked(){
//        // purchaseRl.setVisibility(View.GONE);
//        // activity.startActivity(getActivity(), 0);
//    }

    // 敬请期待
    @Override
    public void showWaitingView() {
        empty.setVisibility(View.VISIBLE);
    }

    // 发布求购 & 发布报价
    @Override
    public void showBoth() {
        empty.setVisibility(View.GONE);
        purchaseLinear.setVisibility(View.VISIBLE);
        quoteLinear.setVisibility(View.VISIBLE);
    }

    // 发布求购
    @Override
    public void showPurchaseView() {
        empty.setVisibility(View.GONE);
        purchaseLinear.setVisibility(View.VISIBLE);
        quoteLinear.setVisibility(View.GONE);
    }

    // 发布报价
    @Override
    public void showQuoteView() {
        empty.setVisibility(View.GONE);
        purchaseLinear.setVisibility(View.GONE);
        quoteLinear.setVisibility(View.VISIBLE);
    }

    //会员状态异常，请联系客服！
//    @Override
//    public void showAccountError() {
//        showPromptMessage(getResources().getString(R.string.error_deal_vip));
//    }


    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

}
