package com.ihwdz.android.hwapp.ui.me.HwVIPCN;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 鸿网大宗公众号
 */
public class HwVipCNActivity extends BaseActivity {

    String titleStr = "";
    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @Override
    public int getContentView() {
        return R.layout.activity_hw_vip_cn;
    }

    @Override
    public void initView() {
        titleStr = getResources().getString(R.string.about_us_per);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
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


}
