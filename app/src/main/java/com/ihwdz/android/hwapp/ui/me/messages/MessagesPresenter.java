package com.ihwdz.android.hwapp.ui.me.messages;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.me.messages.MessagesContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/09
 * desc :
 * version: 1.0
 * </pre>
 */
public class MessagesPresenter implements MessagesContract.Presenter {

    String TAG = "MessagesPresenter";

    @Inject MessagesActivity parentActivity;
    @Inject MessagesContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;
    @Inject MessageAdapter mAdapter;

    int currentPageNum = 1;

    @Inject
    public MessagesPresenter(MessagesActivity activity){
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
    public void refreshData() {
        currentPageNum = 1;
        getMessageData();
    }

    @Override
    public MessageAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void getMessageData() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getMessageData(currentPageNum, PageSize)
                .compose(RxUtil.<NewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<NewsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        if (mAdapter.getLoadMoreStatus() != Constant.loadStatus.STATUS_EMPTY){
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(NewsData data) {
                        mView.hideWaitingRing();
                        mView.hideEmptyView();
                        if ("0".equals(data.getCode())) {
                            if (data.getData()!= null && data.getData().getRecordList() != null && data.getData().getRecordList().size()>0 ){
                                if (currentPageNum == 1){
                                    mAdapter.clear();
                                    mAdapter.setDataList(data.getData().getRecordList());
                                }else {
                                    mAdapter.addDataList(data.getData().getRecordList());
                                }
                                currentPageNum++;

                            }else {
                                if (currentPageNum == 1){
                                    mView.showEmptyView();
                                }else {
                                    mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                }

                            }
                        }else {
                            mView.showEmptyView();
                            mView.showPromptMessage(data.getMsg());
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }
}
