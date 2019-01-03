package com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.model.bean.ClientData;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;
import com.ihwdz.android.hwapp.model.bean.UserGoodsData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientAdapter;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.clientData.ClientDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/19
 * desc :
 * version: 1.0
 * </pre>
 */
public class ClientDetailPresenter implements ClientDetailContract.Presenter {

    String TAG = "ClientDetailPresenter";
    @Inject ClientDetailActivity parentActivity;
    @Inject ClientDetailContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    ClientDataModel model;

    @Inject DeviceAdapter mDeviceAdapter;    // 设备清单 - 5 列表格
    @Inject ProductAdapter mProductAdapter;  // 产品设备展示 - 四列图片

    private String currentId;
    private String follow;     // "0"-取消关注 ; "1" - 关注;



    @Inject
    public ClientDetailPresenter(ClientDetailActivity activity){
        this.parentActivity = activity;
        model = new ClientDataModel(parentActivity);
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
    public void refreshData() {

    }

    @Override
    public void setClientId(String id) {
        this.currentId = id;
    }

    @Override
    public void setFollow(String follow) {
        this.follow = follow;
    }

    @Override
    public DeviceAdapter getDeviceAdapter() {
        return mDeviceAdapter;
    }

    @Override
    public ProductAdapter getProductAdapter() {
        return mProductAdapter;
    }

    @Override
    public void getClientDetailData() {
        // currentId = "262144"; // "352718";
        Subscription rxSubscription = model
                .getClientDetailData(currentId)
                .compose(RxUtil.<ClientDetailData>rxSchedulerHelper())
                .subscribe(new Subscriber<ClientDetailData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(ClientDetailData data) {
                        Log.d(TAG, "onNext");
                        if (data != null && data.data != null){

                            if (data.data.deviceVOs != null && data.data.deviceVOs.size() > 0){
                                // 设备清单
                                mView.initDeviceRecyclerView();
                                getDeviceAdapter().setDataList(data.data.deviceVOs);
                            }else {
                                mView.showEmptyDeviceLayout();
                            }

                            if (data.data.memberAttachment != null && data.data.memberAttachment.size() > 0){
                                // 产品设备展示
                                mView.initProductRecyclerView();
                                mView.showProductLayout();
                                mProductAdapter.setDataList(data.data.memberAttachment);
                            }else {
                                mView.hideProductLayout();
                            }
                            // 关注
                            mView.updateFollowIcon(TextUtils.equals("1",data.data.follow));
                            // 更新
                            mView.updateView(data);
                        }else {
                            LogUtils.printInfo(TAG, "data == null");
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    @Override
    public void updateClientFollowData() {
        // currentId = "262144"; // "352718";
        mView.setFollowStarClickable(false);
        Subscription rxSubscription = model
                .updateClientFollowData(currentId, follow)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        Log.d(TAG, "onNext");
                        // 返回data.data=null
                        if (TextUtils.equals("0",data.code)){
                            mView.showPromptMessage(data.msg); // 关注成功 或 取消关注
                            mView.setFollowStarClickable(true);
                        }else {
                            mView.showPromptMessage(data.msg); // 操作失败
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

}
