package com.ihwdz.android.hwapp.ui.me.feedback;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class FeedbackActivity extends BaseActivity {

    String TAG = "FeedbackActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.edit_telephone) EditText editText;
    @Inject CompositeSubscription mSubscriptions;
    @BindView(R.id.edit_feedback) EditText editFeedback;

    String titleStr;
    String type; //feedType: 1 价格 2原料 3体验 4 其它

    @Override
    public int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        titleStr = getResources().getString(R.string.feedback_per);
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
        editText.setText(Constant.user_account);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.bt_submit)
    public void onSubmitClicked() {
        Log.d(TAG, "token:" + Constant.token);
        Log.d(TAG, "feedback:" + editFeedback.getText().toString());
        LoginDataModel model = new LoginDataModel(this);
        Subscription rxSubscription = model
                .postFeedback(Constant.token, type, editFeedback.getText().toString().trim())
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "------- onCompleted ---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "------- onError ---------");
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        Log.d(TAG, "------- onNext ---------");
                        Log.d(TAG, "------- data: " + data.msg);
                        if ("0".equals(data.code)) {
                            Toast.makeText(getBaseContext(), "success",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }


    @OnClick({R.id.radio_price, R.id.radio_material, R.id.radio_experience, R.id.radio_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_price:
                type = "1";
                break;
            case R.id.radio_material:
                type = "2";
                break;
            case R.id.radio_experience:
                type = "3";
                break;
            case R.id.radio_other:
                type = "4";
                break;
        }
    }
}
