package com.ihwdz.android.hwapp.ui.home.clientseek;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ClientData;
import com.ihwdz.android.hwapp.model.bean.RightsData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail.ClientDetailActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.filter.ClientFilterActivity;
import com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate.UpdateActivity;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.clientData.ClientDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.KEYWORDS_MODE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.POTENTIAL_CLIENTS_MODE;
import static com.ihwdz.android.hwapp.ui.home.clientseek.ClientContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class ClientPresenter implements ClientContract.Presenter{

    String TAG = "IndexPresenter";
    @Inject ClientActivity parentActivity;
    @Inject ClientContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    ClientDataModel model;
    @Inject ClientAdapter mAdapter;

    private int currentMode = POTENTIAL_CLIENTS_MODE;
    private String currentKeywords = null;

    int currentPageNum = 1;
    boolean hasRight = false;
    boolean isFromFilter = false;  // 当前是否是从 筛选界面返回

    @Inject
    public ClientPresenter(ClientActivity activity){
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
    public ClientAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void refreshData() {
        setCurrentPageNum(1);
        mAdapter.clear();
        mView.updateView();
    }

    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    @Override
    public void setCurrentPageNum(int pageNum) {
        this.currentPageNum = pageNum;
    }

    @Override
    public boolean getHasRight() {
        //return true;
        return hasRight;
    }

    @Override
    public void setIsFromFilter(boolean isFromFilter) {
        this.isFromFilter = isFromFilter;
    }

    @Override
    public void getPotentialClientsData(int pageNum, int pageSize) {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getPotentialClientsData(pageNum, PageSize, Constant.token)
                .compose(RxUtil.<ClientData>rxSchedulerHelper())
                .subscribe(new Subscriber<ClientData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString()); //  提示信息(e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(ClientData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                mView.hideEmptyView();
                                if (data.data.recordList != null && data.data.recordList.size() > 0){

                                    if (currentPageNum == 1){
                                        Log.d(TAG, "===========  getPotentialClientsData  =========== currentPageNum == 1");
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }
                                }

                            }else {
                                mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getFollowClientsData(int pageNum, int pageSize) {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getFollowedClientsData(pageNum, PageSize)
                .compose(RxUtil.<ClientData>rxSchedulerHelper())
                .subscribe(new Subscriber<ClientData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString()); //  提示信息(e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(ClientData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                if (data.data.recordList != null && data.data.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }
                                }

                            }else {
                                mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                            }
                        }else {
                            mView.showEmptyView();
                            //mView.showPromptMessage(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getSearchClientsData(int pageNum, int pageSize) {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getSearchClientsData(pageNum, PageSize, currentKeywords)
                .compose(RxUtil.<ClientData>rxSchedulerHelper())
                .subscribe(new Subscriber<ClientData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        mView.hideWaitingRing();
                        mView.hideKeyboard();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString()); //  提示信息(e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(ClientData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                mView.hideEmptyView();
                                if (data.data.recordList != null && data.data.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        LogUtils.printInfo(TAG, "===========  getSearchClientsData  =========== currentPageNum == 1");
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    LogUtils.printInfo(TAG, "===========  getSearchClientsData null");
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }
                                }

                            }else {

                                mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getFilterClientData(int pageNum, int pageSize) {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getFilterClientsData(pageNum, pageSize, Constant.hasMobile, Constant.hasEmail, Constant.startRegMoney, Constant.endRegMoney,
                        Constant.startCompanyCreated, Constant.endCompanyCreated, Constant.selectedCityCodes)
                .compose(RxUtil.<ClientData>rxSchedulerHelper())
                .subscribe(new Subscriber<ClientData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString()); //  提示信息(e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(ClientData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                mView.hideEmptyView();
                                if (data.data.recordList != null && data.data.recordList.size() > 0){
                                    LogUtils.printInfo(TAG, "===========  getFilterClientsData  ===========");
                                    if (currentPageNum == 1){
                                        LogUtils.printInfo(TAG, "===========  getFilterClientsData  =========== currentPageNum == 1");
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }

                                }

                            }else {
                                mView.showEmptyView();
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void setSearchKeywords(String keywords) {
        currentKeywords = keywords;
        currentMode = KEYWORDS_MODE;
    }

    @Override
    public void getUpdateRightData() {
        Log.d(TAG, "============  getUpdateRightData ==============");
        Subscription rxSubscription = model
                .getUpdateRightData()
                .compose(RxUtil.<RightsData>rxSchedulerHelper())
                .subscribe(new Subscriber<RightsData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.printError(TAG, "onError: "+ e.toString());
                    }

                    @Override
                    public void onNext(RightsData data) {
                        // mView.hideWaitingRing(); // 加载出数据后再隐藏
                        if (TextUtils.equals("0", data.code)){
                            hasRight = true;
                        }else {
                            hasRight = false;
                        }
                        // mView.showUpdateButton(hasRight); 支付成功后 自行隐藏 不从接口获取

                    }
                });

        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setCurrentMode(int mode) {
        if (currentMode != mode){
            currentMode = mode;
            setCurrentPageNum(1);
            mView.updateView();
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void goLogin() {
        Intent intent = new Intent(parentActivity, LoginPageActivity.class);
        parentActivity.startActivity(intent);
    }

    @Override
    public void goUpdate() {
        Intent intent = new Intent(parentActivity, UpdateActivity.class);
        parentActivity.startActivity(intent);
    }

    @Override
    public void goDetail(String id) {
        Intent intent = new Intent(parentActivity, ClientDetailActivity.class);
        intent.putExtra("ID", id);
        parentActivity.startActivity(intent);
    }

    @Override
    public void goFilter() {
        if (isFromFilter){
            parentActivity.onBackClicked();
        }else {
            Intent intent = new Intent(parentActivity, ClientFilterActivity.class);
            parentActivity.startActivity(intent);
            parentActivity.finish();
        }

    }
}
