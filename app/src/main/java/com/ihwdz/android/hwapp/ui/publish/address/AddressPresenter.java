package com.ihwdz.android.hwapp.ui.publish.address;

import android.os.Bundle;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :
 * version: 1.0
 * </pre>
 */
public class AddressPresenter implements AddressContract.Presenter {

    String TAG = "AddressPresenter";

    @Inject AddressActivity parentActivity;
    @Inject AddressContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;
    IAppSettings settings;
    @Inject AddressAdapter mAdapter;


    @Inject
    public AddressPresenter(AddressActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);
        settings = new SlimAppSettings(activity);
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
    public AddressAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void refreshData() {
        mAdapter.clear();

        getAddressData();
    }

    @Override
    public void getAddressData() {
        mView.showWaitingRing();
        //currentPageNum, PageSize
        Subscription rxSubscription = model
                .getAddressData()
                .compose(RxUtil.<PublishData.AddressData>rxSchedulerHelper())
                .subscribe(new Subscriber<PublishData.AddressData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printCloseableInfo(TAG, "getAddressData onError: "+ e.toString());
                        mView.showPromptMessage(e.toString());
                    }

                    @Override
                    public void onNext(PublishData.AddressData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){

                            if (data.data != null && data.data.size() >0){
                                mAdapter.setDataList(data.data);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void gotoAddAddress() {
        //mView.showPromptMessage("添加新地址");
        WarehouseActivity.startWarehouseActivity(parentActivity,1,null);

    }

    @Override
    public void gotoEditAddress(String id) {
        //mView.showPromptMessage("编辑地址");
        WarehouseActivity.startWarehouseActivity(parentActivity,2, id);
    }

    @Override
    public void goBackPurchase() {
        // Constant 记录 选中地址
        parentActivity.onBackClicked();
    }


}
