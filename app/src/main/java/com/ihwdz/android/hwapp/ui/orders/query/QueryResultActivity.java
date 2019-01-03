package com.ihwdz.android.hwapp.ui.orders.query;

import android.graphics.Paint;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LogisticsResultData;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class QueryResultActivity extends BaseActivity implements QueryContract.View {

    String TAG = "QueryResultActivity";

    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_city_from)
    TextView fromCityTv;
    @BindView(R.id.tv_city_destination)
    TextView destinationCityTv;
    @BindView(R.id.tv_area_from)
    TextView fromTv;
    @BindView(R.id.tv_area_destination)
    TextView destinationTv;

    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_marketPrice)
    TextView tvMarketPrice;     // 市场价
    @BindView(R.id.tv_logistics)
    TextView tvPriceLogistics;
    @BindView(R.id.tv_aokaiPrice)
    TextView tvAokaiPrice;     // 鸿网物流

    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_telephone)
    TextView tvTelephone;
    @BindView(R.id.tv_company)
    TextView tvCompany;

    String amount = "";
    String currencySign;

    @Inject
    QueryContract.Presenter mPresenter;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_query_result);
//    }

    @Override
    public int getContentView() {
        return R.layout.activity_query_result;
    }

    @Override
    public void initView() {
        initToolBar();
        currencySign = getResources().getString(R.string.currency_sign);
        if (getIntent() != null) {
            amount = getIntent().getStringExtra("AMOUNT_TON");
            mPresenter.setCurrentAmount(amount);
        }
        initAddress();
        Linkify.addLinks(tvTelephone, Linkify.PHONE_NUMBERS);
    }

    private void initAddress() { String cityFrom = "";
        if (TextUtils.equals(Constant.provFrom, Constant.cityFrom)){
            cityFrom = getResources().getString(R.string.municipal_district);

        }else {
            cityFrom = Constant.cityFrom;
        }
        String cityTo = "";
        if (TextUtils.equals(Constant.provTo, Constant.cityTo)){
            cityTo = getResources().getString(R.string.municipal_district);

        }else {
            cityTo = Constant.cityTo;
        }

        fromCityTv.setText(Constant.provFrom + "/" + cityFrom);
        fromTv.setText(Constant.distinctFrom);
        destinationCityTv.setText(Constant.provTo +"/"+ cityTo);
        destinationTv.setText(Constant.distinctTo);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getPriceData();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        Log.d(TAG, "onBackPressed");
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.title_query_result));
    }

    @Override
    public void updateView(LogisticsResultData.LogisticsResult result) {

        if (Integer.parseInt(amount) < 10) {
            tvPrice.setText(getResources().getString(R.string.amount_less_logistics));
            tvMarketPrice.setVisibility(View.GONE);
            tvPriceLogistics.setVisibility(View.GONE);
            tvAokaiPrice.setVisibility(View.GONE);
        } else {
            tvMarketPrice.setVisibility(View.VISIBLE);
            tvPriceLogistics.setVisibility(View.VISIBLE);
            tvAokaiPrice.setVisibility(View.VISIBLE);
            tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);   // 删除线
            tvMarketPrice.setText(" "+currencySign+result.marketPrice);
            tvAokaiPrice.setText(" "+currencySign+result.aokaiPrice);
        }
        tvContact.setText(result.contract);
        tvTelephone.setText(result.telephone);
        tvCompany.setText(result.company);
    }

}
