package io.dcloud.ihwdz.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate.UpdateActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate.UpdatePresenter;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/23
 * desc :   微信支付结果处理
 * version: 1.0
 * </pre>
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    public static final String APP_ID = "wx51ee42f8a498e2aa"; // 微信

    private static IWXAPI api;

    private static int mPayMode;  // Constant.PayMode.PAY_CHARGE, Constant.PayMode.PAY_UPDATE;
    String TAG = "WXPayEntryActivity";

    public static IWXAPI getApiInstance(Context context, int payMode){
        if (api == null){
            api = WXAPIFactory.createWXAPI(context, APP_ID);
        }
        mPayMode = payMode;

        return api;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("支付结果");
        setContentView(tv);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(this, "onReq", Toast.LENGTH_LONG).show();
        BaseReq req = baseReq;
        Log.e(TAG, "onReq  baseReq======================== " + baseReq.openId);
        Log.e(TAG, "onReq  baseReq======================== " + baseReq.transaction);

    }

    @Override
    public void onResp(BaseResp resp) {

        Toast.makeText(this, resp.errStr, Toast.LENGTH_LONG).show();

//        BaseResp baseResp = resp;
//        Log.e("Pay_wechat ", "resp.getType ======================== " + resp.getType());
//        Log.e("Pay_wechat ", "resp.errCode ======================== " + resp.errCode);
//        Log.e("Pay_wechat ", "resp.errStr ======================== " + resp.errStr);
//        Log.e("Pay_wechat ", "resp.transaction ======================== " + resp.transaction);

        if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){

            if (resp.errCode == 0){
                // 支付成功
                Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
                paySuccess();
            }else {
                LogUtils.printError(TAG, "resp.errCode: " + resp.errCode);
                Constant.weChatPayFailed = true;
                Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
            }

            //Intent intent = new Intent("com,example.mymessage");
            Intent intent = new Intent();
            intent.setAction("android.PAYMENT_RESULT");
            intent.putExtra("PAY_MODE", mPayMode);
            intent.putExtra("PAY_RESULT", resp.errCode);
            sendBroadcast(intent);               // 发送标准广播
            //sendOrderedBroadcast(intent,null); // 发送有序广播

//            switch (resp.errCode){
//                case 0:
//                    // 支付成功
//                    Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
//                    paySuccess();
//                    break;
//                case -1:
//                    Constant.weChatPayFailed = true;
//                    Toast.makeText(this, "配置异常", Toast.LENGTH_LONG).show();
//                    // 签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//                    break;
//                case -2:
//                    Constant.weChatPayFailed = true;
//                    // 无需处理。发生场景：用户不支付了，点击取消，返回APP
//                    Toast.makeText(this, "已取消支付", Toast.LENGTH_LONG).show();
//
//                    finish();
//                    break;
//            }
            finish();

        }

    }

    private void paySuccess(){
        switch (mPayMode){
            case Constant.PayMode.PAY_CHARGE:
                Constant.orderOptionDone = true; // 微信支付成功 （手续费支付成功）->返回 订单详情
                //OrderDetailActivity.startOrderDetailActivity(this, Constant.orderOptionId, Constant.orderOptionStatus, Constant.orderOption);
                break;
            case Constant.PayMode.PAY_UPDATE:
                Constant.updateOptionDone = true;  // 微信支付成功 （升级会员权益支付成功） -> 返回 找客户
                // UpdateActivity.backToClientActivity();
                // backToClientActivity();
                break;
        }
    }

    public void backToClientActivity() {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
        finish();
    }
}
