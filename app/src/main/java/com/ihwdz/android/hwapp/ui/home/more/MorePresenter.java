package com.ihwdz.android.hwapp.ui.home.more;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.homeData.HomeDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.HW24H_MODE;
import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.PageSize;
import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.REC_ABC_MODE;
import static com.ihwdz.android.hwapp.ui.home.more.MoreContract.REC_MODE;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/30
 * desc :
 * version: 1.0
 * </pre>
 */
public class MorePresenter implements MoreContract.Presenter {

    String TAG = "MorePresenter";
    @Inject MoreActivity parentActivity;
    @Inject MoreContract.View mView;

    @Inject CompositeSubscription mSubscriptions;

    @Inject Hw24hAdapter mHw24hAdapter;
    @Inject NewsAdapter mNewsAdapter;
    int currentPageNum = 1;

    int currentMode = HW24H_MODE;

    @Inject
    public MorePresenter(MoreActivity activity){
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
    public Hw24hAdapter getHw24Adapter() {
        return mHw24hAdapter;
    }

    @Override
    public NewsAdapter getNewsAdapter() {
        return mNewsAdapter;
    }

    @Override
    public void setCurrentMode(int mode) {
        Log.d(TAG, "========== setCurrentMode ======= : " + mode);
        if (this.currentMode != mode){
            this.currentMode = mode;
            refreshData();
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void refreshData() {
        currentPageNum = 1;
        mView.showWaitingRing();
        Log.d(TAG, "================ refreshData == currentMode: " + currentMode);
        switch (currentMode){
            case HW24H_MODE:
                //mView.initHw24hView();
                getHw24hData();
                break;
            case REC_ABC_MODE:
                //mView.initRecommendView();
                getRecommendABCData();
                break;
            case REC_MODE:
                //mView.initRecommendView();
                getRecommendData();
                break;
            default:
                break;
        }
    }

    @Override
    public void getHw24hData() {
        HomeDataModel model = new HomeDataModel(parentActivity);
        Subscription rxSubscription = model
                .getHw24hData(currentPageNum, PageSize)
                .compose(RxUtil.<NewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<NewsData>() {
                    @Override
                    public void onCompleted() {
                        // 加载完成　加页数
                        currentPageNum ++;
                        mView.hideWaitingRing();
                        mHw24hAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        mHw24hAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(NewsData data) {
                        if (data.getData() != null){
                            if (data.getData().getRecordList()!= null && data.getData().getRecordList().size()>0){
                                if (currentPageNum == 1){
                                    mHw24hAdapter.clear();
                                    mHw24hAdapter.setDataList(data.getData().getRecordList());
                                }
                                else {
                                    mHw24hAdapter.addDataList(data.getData().getRecordList());
                                }

                            }
                        }else {
                            mHw24hAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                        }
                    }
                });

        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void getRecommendABCData() {
        HomeDataModel model = new HomeDataModel(parentActivity);
        Subscription rxSubscription = model
                .getRecommendNewsABC(currentPageNum, PageSize)
                .compose(RxUtil.<NewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<NewsData>() {
                    @Override
                    public void onCompleted() {
                        // 加载完成　加页数
                        currentPageNum ++;
                        mView.hideWaitingRing();
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(NewsData data) {
                        if (data.getData() != null){
                            if (data.getData().getRecordList()!= null && data.getData().getRecordList().size()>0){
                                Log.d(TAG, "========= getRecommendABCData ==== onNext ========== data: "+data.getData().getRecordList().size());
                                if (currentPageNum == 1){
                                    mNewsAdapter.clear();
                                    mNewsAdapter.setDataList(data.getData().getRecordList());
                                }
                                else {
                                    mNewsAdapter.addDataList(data.getData().getRecordList());
                                }

                            }
                        }else {
                            mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                        }
                    }
                });

        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getRecommendData() {
        HomeDataModel model = new HomeDataModel(parentActivity);
        Subscription rxSubscription = model
                .getRecommendNews(currentPageNum, PageSize)
                .compose(RxUtil.<NewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<NewsData>() {
                    @Override
                    public void onCompleted() {
                        // 加载完成　加页数
                        currentPageNum ++;
                        mView.hideWaitingRing();
                        if (mNewsAdapter.getLoadMoreStatus() != Constant.loadStatus.STATUS_EMPTY){
                            mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        }else {
                            Log.d(TAG, "========= getRecommendData ==== onCompleted ========== setLoadMoreStatus: STATUS_EMPTY");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, e.toString());
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(NewsData data) {
                        if (data.getData() != null){
                            if (data.getData().getRecordList()!= null && data.getData().getRecordList().size()>0){
                                Log.d(TAG, "========= getRecommendData ==== onNext ========== data: "+data.getData().getRecordList().size());
                                if (currentPageNum == 1){
                                    mNewsAdapter.clear();
                                    mNewsAdapter.setDataList(data.getData().getRecordList());
                                }
                                else {
                                    mNewsAdapter.addDataList(data.getData().getRecordList());
                                }

                            }
                        }else {
                            Log.d(TAG, "========= getRecommendData ==== onNext ========== setLoadMoreStatus: STATUS_EMPTY");
                            mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                        }
                    }
                });

        mSubscriptions.add(rxSubscription);
    }
}
