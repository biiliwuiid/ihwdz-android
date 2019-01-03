package com.ihwdz.android.hwapp.ui.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

public class GuideActivity extends BaseActivity {

    String TAG = "GuideActivity";
    private Handler handler = new Handler();

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guide);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//        }, 1000);
//    }

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 0);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
