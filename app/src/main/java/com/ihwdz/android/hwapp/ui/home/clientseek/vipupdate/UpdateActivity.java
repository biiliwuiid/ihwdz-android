package com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserGoodsData;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.orders.payment.PaymentReceiver;
import com.ihwdz.android.hwapp.utils.log.LogUtils;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class UpdateActivity extends BaseActivity implements UpdateContract.View {

    String TAG = "UpdateActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.bt_1) RadioButton radioButton1;
    @BindView(R.id.bt_2) RadioButton radioButton2;
    @BindView(R.id.bt_3) RadioButton radioButton3;

    @BindView(R.id.name1) TextView name1;
    @BindView(R.id.price1) TextView price1;
    @BindView(R.id.days1) TextView days1;
    @BindView(R.id.name2) TextView name2;
    @BindView(R.id.price2) TextView price2;
    @BindView(R.id.days2) TextView days2;
    @BindView(R.id.name3) TextView name3;
    @BindView(R.id.price3) TextView price3;
    @BindView(R.id.days3) TextView days3;

    @BindView(R.id.bt1) RadioButton radioAli;
    @BindView(R.id.bt2) RadioButton radioWechat;

    @Inject UpdateContract.Presenter mPresenter;
    
    private String cardId1, cardId2, cardId3;
    private String daysStr1, daysStr2, daysStr3;

    Drawable cardBg,sliverBg,goldBg,experienceBg;

    //private PaymentReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    public int getContentView() {
        return R.layout.activity_update;
    }

    @Override
    public void initView() {

        initToolBar();
        radioButton2.setChecked(true);   // 默认选择凤凰
        radioAli.setChecked(true);       // 默认选择支付宝
        initBgDrawable();



        // 注册广播
        // receiver = new PaymentReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.PAYMENT_RESULT");
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(receiver,intentFilter);
    }

    String ACTION_NAME = "android.PAYMENT_RESULT";
    String PAY_MODE = "PAY_MODE";
    String PAY_RESULT = "PAY_RESULT";
    private BroadcastReceiver receiver = new PaymentReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_NAME)){
                LogUtils.printError(TAG, "onReceive  处理action名字相对应的广播: " + action );
                int payMode = intent.getIntExtra(PAY_MODE, 0);
                int errorCode = intent.getIntExtra(PAY_RESULT, 0);
                LogUtils.printError(TAG, "onReceive  payMode: " + payMode );
                LogUtils.printError(TAG, "onReceive   errorCode: " + errorCode );
                // 升级权益
                if (payMode == Constant.PayMode.PAY_UPDATE){
                    if (errorCode == 0){  // 支付成功返回上级
                        //LogUtils.printError(TAG, "onReceive  升级权益: 支付成功返回上级 " );

                        mPresenter.backToClientActivity();
                        // showPromptMessage("onReceive: 支付成功");
                    }else {
                        mPresenter.setIsWeChatPaySubmitClicked(false);
                    }
                    LogUtils.printError(TAG, "onReceive  升级权益: " );
                }
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initBgDrawable() {
        cardBg = getResources().getDrawable(R.drawable.card);
        sliverBg = getResources().getDrawable(R.drawable.sliver_vip);
        goldBg = getResources().getDrawable(R.drawable.gold_vip);
        experienceBg = getResources().getDrawable(R.drawable.experience_vip);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getUserGoodsData();
    }

    // toolbar
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_up));
    }

    @OnCheckedChanged(R.id.bt_1)
    public void onRadio1CheckedChanged(){
        // LogUtils.printCloseableInfo(TAG, "radioButton1.isChecked(): "+ radioButton1.isChecked());
        if (radioButton1.isChecked()){
//            sliverBg.setBounds(0, 0, 0, 0); //第一0是距左右边距离，第二0是距上下边距离
            //radioButton1.setBackground(zoomDrawable(sliverBg, 90, 130));
        }else {
//            cardBg.setBounds(10, 10, 10, 10); //第一0是距左右边距离，第二0是距上下边距离
            //radioButton1.setBackground(zoomDrawable(cardBg, 80, 120));
        }
    }
    @OnCheckedChanged(R.id.bt_2)
    public void onRadio2CheckedChanged(){
        // LogUtils.printCloseableInfo(TAG, "radioButton2.isChecked(): "+ radioButton2.isChecked());
        if (radioButton2.isChecked()){
            //radioButton2.setBackground(zoomDrawable(sliverBg, 90, 130));
        }else {
            //radioButton2.setBackground(zoomDrawable(cardBg, 80, 120));
        }
    }
    @OnCheckedChanged(R.id.bt_3)
    public void onRadio3CheckedChanged(){
        // LogUtils.printCloseableInfo(TAG, "radioButton3.isChecked(): "+ radioButton3.isChecked());
        if (radioButton3.isChecked()){
            //radioButton3.setBackground(zoomDrawable(sliverBg, 90, 130));
        }else {
            //radioButton3.setBackground(zoomDrawable(cardBg, 80, 120));

        }
    }


    public void onVipCardChecked(){
        if (radioButton1.isChecked()){
            mPresenter.setCardId(cardId1);
            mPresenter.setDays(daysStr1);
        }
        if (radioButton2.isChecked()){
            mPresenter.setCardId(cardId2);
            mPresenter.setDays(daysStr2);
        }
        if (radioButton3.isChecked()){
            mPresenter.setCardId(cardId3);
            mPresenter.setDays(daysStr3);
        }
    }

    // 点击立即开通会员
    @OnClick(R.id.bt_vip)
    public void onUpdateVipClicked(){
        LogUtils.printCloseableInfo(TAG, "=========  onUpdateVipClicked  ============");
        onVipCardChecked();  // 检查选择了那种会员
        if (radioAli.isChecked()){
            // 支付宝支付
            LogUtils.printCloseableInfo(TAG, ":IsAliPaySubmitClicked() " + mPresenter.getIsAliPaySubmitClicked());
            mPresenter.postAliPayData();

//            if (!mPresenter.getIsAliPaySubmitClicked()){
//                LogUtils.printCloseableInfo(TAG, ":IsAliPaySubmitClicked() " + mPresenter.getIsAliPaySubmitClicked());
//                mPresenter.postAliPayData();
//            }
        }else if (radioWechat.isChecked()){
            // 微信支付
            mPresenter.postWeChatPayData();
            LogUtils.printCloseableInfo(TAG, "IsWeChatPaySubmitClicked() "+mPresenter.getIsWeChatPaySubmitClicked()+"   Constant.weChatPayFailed: " + Constant.weChatPayFailed);

            //            if (!mPresenter.getIsWeChatPaySubmitClicked() || Constant.weChatPayFailed){
//                LogUtils.printCloseableInfo(TAG, "IsWeChatPaySubmitClicked() "+mPresenter.getIsWeChatPaySubmitClicked()+"   Constant.weChatPayFailed: " + Constant.weChatPayFailed);
//                mPresenter.postWeChatPayData();
//            }
        }else {
            Toast.makeText(this, getResources().getString(R.string.no_pay_up), Toast.LENGTH_SHORT).show();
        }


    }

    public void backToClientActivity(){
        //mPresenter.backToClientActivity();
        Intent intent = new Intent(UpdateActivity.this, ClientActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    @Override
    public void updateView(UserGoodsData data) {

        String sign = getResources().getString(R.string.currency_sign);
        String price;
        for (int i = 0; i < data.data.size(); i++){
            UserGoodsData.UserGoodsEntity model = data.data.get(i);
            price = sign + model.catPrice;
            String str = model.catName + "\n\n"+ price+ "\n\n\n"+ model.days;
            if (TextUtils.equals(model.catName, getResources().getString(R.string.magpie))){
                cardId1 = model.goodsId;
                daysStr1 = model.days;
                radioButton1.setText(str);
//                name1.setText(model.catName);
//                price1.setText(price);
//                days1.setText(model.days);

            }else if (TextUtils.equals(model.catName, getResources().getString(R.string.phoenix))){
                cardId2 = model.goodsId;
                daysStr2 = model.days;
                radioButton2.setText(str);
//                name2.setText(model.catName);
//                price2.setText(price);
//                days2.setText(model.days);

            }else if (TextUtils.equals(model.catName, getResources().getString(R.string.sparrow))){
                cardId3 = model.goodsId;
                daysStr3 = model.days;
                radioButton3.setText(str);
//                name3.setText(model.catName);
//                price3.setText(price);
//                days3.setText(model.days);
            }
        }

    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


}
