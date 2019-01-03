package com.ihwdz.android.hwapp.ui.me.infoupdate;

import android.os.Bundle;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.common.Constant.InfoUpdate.INFO_QUOTE_PRICE;
import static com.ihwdz.android.hwapp.common.Constant.InfoUpdate.INFO_USER;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/30
 * desc :  修改-保存数据 : 用户信息0；报价-单价1；
 * version: 1.0
 * </pre>
 */
public class InfoUpdatePresenter implements InfoUpdateContract.Presenter {

    String TAG = "InfoUpdatePresenter";

    @Inject InfoUpdateActivity parentActivity;
    @Inject InfoUpdateContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LoginDataModel model;

    private int currentMode = 0;               // 信息类型（INFO_QUOTE_PRICE；INFO_USER）
    private String currentContent;             // 信息内容

    private String name,email,mobile, provinceCode,province, cityCode,city, districtCode,district, address;

    @Inject
    public InfoUpdatePresenter(InfoUpdateActivity activity){
        this.parentActivity = activity;
        model = LoginDataModel.getInstance(parentActivity);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(parentActivity != null){
            parentActivity = null;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void store(Bundle outState) {

    }

    @Override
    public void restore(Bundle inState) {

    }

    @Override
    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    @Override
    public void setCurrentContent(String content) {
        this.currentContent = content;
    }

    @Override
    public void doSave() {

        switch (currentMode){
            case INFO_USER:
                postUserInformation();
                parentActivity.onBackClicked();
                break;
            case INFO_QUOTE_PRICE:
                // 报价 - 单价
                if (checkQuote()){
                    Constant.quotePrice = currentContent;
                    parentActivity.onBackClicked();
                }
                break;
            default:
                break;
        }

    }

    // 校验报价 5000-40000
    private boolean checkQuote() {
        String quoteLowRemind = parentActivity.getResources().getString(R.string.quote_too_low);
        String quoteHighRemind = parentActivity.getResources().getString(R.string.quote_too_high);
        if (!TextUtils.isEmpty(currentContent)){
            double quote = Double.valueOf(currentContent);
            if (quote < 5000){
                mView.showPromptMessage(quoteLowRemind);
                return false;
            }
            if (quote > 40000){
                mView.showPromptMessage(quoteHighRemind);
                return false;
            }
            return true;
        }else {
            mView.showPromptMessage(quoteLowRemind);
            return false;
        }
    }

    // 会员中心 - 完善信息 - 修改信息（name email district detail_address.）
    @Override
    public void postUserInformation() {

        name = Constant.name;
        mobile = Constant.tel;
        email = Constant.email;
        province = Constant.provinceName;
        provinceCode = Constant.provinceCode;
        city = Constant.cityName;
        cityCode = Constant.cityCode;
        district = Constant.districtName;
        districtCode = Constant.districtCode;
        address = Constant.address;

        Subscription rxSubscription = model
                .postUserData(name,email,mobile,
                        provinceCode,province,
                        cityCode,city,
                        districtCode,district, address
                )
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        if ("0".equals(data.code)) {
                            if (data.data){
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }




}
