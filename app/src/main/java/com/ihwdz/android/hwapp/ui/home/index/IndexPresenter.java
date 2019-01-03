package com.ihwdz.android.hwapp.ui.home.index;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.model.bean.IndexData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.indexData.IndexDataModel;

import java.beans.IndexedPropertyChangeEvent;

import javax.inject.Inject;

import retrofit2.http.PUT;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class IndexPresenter implements IndexContract.Presenter{

    String TAG = "IndexPresenter";
    @Inject IndexActivity parentActivity;
    @Inject IndexContract.View mView;

    @Inject CompositeSubscription mSubscriptions;

    @Inject IndexViewAdapter mAdapter;

    @Inject
    public IndexPresenter(IndexActivity activity){
        this.parentActivity = activity;
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
    public IndexViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void refreshData() {

        mAdapter.clear();
        getAllData();
    }

    @Override
    public void getAllData() {
        IndexDataModel model = new IndexDataModel(parentActivity);
        Subscription rxSubscription = model
                .getAllData()
                .compose(RxUtil.<IndexData>rxSchedulerHelper())
                .subscribe(new Subscriber<IndexData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(IndexData data) {
                        Log.d(TAG, "================ onNext === DATA : "+data.getData().size());
                        mView.showWaitingRing();
                        if (data.getData() != null){
                            mAdapter.clear();
                            mAdapter.setDataList(data.getData());
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getAllData(int pageNum, int pageSize) {

    }
}
