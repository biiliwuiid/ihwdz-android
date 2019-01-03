package com.ihwdz.android.hwapp.ui.orders.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/28
 * desc :   微信支付结果 广播
 * version: 1.0
 * </pre>
 */
public class PaymentReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
    }
}
