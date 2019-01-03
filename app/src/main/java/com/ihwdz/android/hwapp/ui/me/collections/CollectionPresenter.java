package com.ihwdz.android.hwapp.ui.me.collections;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.ui.login.login.LoginActivity;
import com.ihwdz.android.hwapp.ui.login.login.LoginContract;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.me.collections.CollectionContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class CollectionPresenter implements CollectionContract.Presenter {

    String TAG = "CollectionPresenter";

    @Inject CollectionsActivity parentActivity;
    @Inject CollectionContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;
    @Inject CollectionAdapter mAdapter;

    int currentPageNum = 1;

    @Inject
    public CollectionPresenter(CollectionsActivity activity){
        this.parentActivity = activity;
        model = new LoginDataModel(parentActivity);
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
        currentPageNum = 1;
        getCollectionData();
    }

    @Override
    public CollectionAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void getCollectionData() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getCollectionData(currentPageNum, PageSize)
                .compose(RxUtil.<NewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<NewsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        // 当前数据不为空 则-> STATUS_PREPARE
                        if (mAdapter.getLoadMoreStatus() != Constant.loadStatus.STATUS_EMPTY){
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        e.printStackTrace();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(NewsData data) {
                        mView.hideWaitingRing();
                        if ("0".equals(data.getCode())) {
                            if (data.getData()!= null && data.data.getRecordList() != null && data.data.getRecordList().size() > 0 ){
                                if (currentPageNum == 1){
                                    mAdapter.clear();
                                    mAdapter.setDataList(data.getData().getRecordList());
                                }else {
                                    mAdapter.addDataList(data.getData().getRecordList());
                                }
                                currentPageNum++;

                            }else {
                                LogUtils.printCloseableInfo(TAG, "getCollectionData currentPageNum: "+currentPageNum);
                                if (currentPageNum == 1){
                                    mView.showEmptyView();
                                }else {
                                    mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                }

                            }
                        }else {
                            mView.showEmptyView();
                            // mView.showPromptMessage(data.getMsg());
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }
}
