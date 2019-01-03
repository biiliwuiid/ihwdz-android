package com.ihwdz.android.hwapp.ui.home.infoday;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.BottomNewsData;
import com.ihwdz.android.hwapp.model.bean.InfoData;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.infoData.InfoDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.ALL_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.DYNAMIC_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.FY_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.HY_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.PRICE_ADJUST_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.SD_DATA;
import static com.ihwdz.android.hwapp.ui.home.infoday.InfoDayContract.TT_DATA;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :
 * version: 1.0
 * </pre>
 */
public class InfoDayPresenter implements InfoDayContract.Presenter {

    String TAG = "InfoDayPresenter";
    @Inject InfoDayActivity parentActivity;
    @Inject InfoDayContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    InfoDataModel model;
    @Inject InfoTitleAdapter mTitleAdapter;
    @Inject InfoDayAdapter mAdapter;
    @Inject NewsAdapter mNewsAdapter;

    int currentMode = PRICE_ADJUST_DATA;  // 默认 - 企业调价
    boolean isNewsContent = false;   // 默认 - 每日讯

    String yesterday = null;
    int currentPageNum = 1;

    @Inject
    public InfoDayPresenter(InfoDayActivity activity){
        parentActivity = activity;
        model = new InfoDataModel(parentActivity);
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
    public InfoTitleAdapter getTitleAdapter() {
        return mTitleAdapter;
    }

    @Override
    public InfoDayAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public NewsAdapter getNewsAdapter() {
        return mNewsAdapter;
    }

    @Override
    public void setIsNewsContent(boolean isNews) {
        if(this.isNewsContent != isNews){
            this.isNewsContent = isNews;
            mView.updateRecyclerView(isNews);
        }
    }

    @Override
    public boolean getIsNewsContent() {
        return isNewsContent;
    }

    @Override
    public void setCurrentMode(int mode) {
        LogUtils.printInfo(TAG, "========== setCurrentMode ======= : " + mode);
        if (this.currentMode != mode){
            this.currentMode = mode;

            if (currentMode == PRICE_ADJUST_DATA || currentMode == DYNAMIC_DATA){
                setIsNewsContent(false);
            }else {
                setIsNewsContent(true);
            }
            refreshData();
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    @Override
    public String getYesterday() {
        return yesterday;
    }

    @Override
    public int getCurrentPageNum() {
        return currentPageNum;
    }

    @Override
    public void setCurrentPageNum(int pageNum) {
        currentPageNum = pageNum;
    }

    @Override
    public void refreshData() {
        yesterday = null;
        currentPageNum = 1;
        mAdapter.clear();
        mView.showWaitingRing();
        Log.d(TAG, "================ refreshData == currentMode: " + currentMode);
        switch (currentMode){
            case ALL_DATA:
                getAllData();
                if (mAdapter.getItemCount() < 10){
                    getAllData();
                }
                break;
            case PRICE_ADJUST_DATA: // 企业调价
                getPriceAdjustInfoData();
                if (mAdapter.getItemCount() < 10){
                    getPriceAdjustInfoData();
                }
                break;
            case DYNAMIC_DATA: // 装置动态
                getDynamicInfoData();
                if (mAdapter.getItemCount() < 10){
                    getDynamicInfoData();
                }
                break;
            case FY_DATA: // 市场风云
                getFyNewsData(currentPageNum);
                break;
            case SD_DATA: // 深度研报
                getSdNewsData(currentPageNum);
                break;
            case HY_DATA: // 行业热点
                getHyNewsData(currentPageNum);
                break;
            case TT_DATA: // 宏观头条
                getTtNewsData(currentPageNum);
                break;
            default:
                break;
        }

    }

    // 全部
    @Override
    public void getAllData() {
        Subscription rxSubscription = model
                .getAllData(yesterday)
                .compose(RxUtil.<InfoData>rxSchedulerHelper())
                .subscribe(new Subscriber<InfoData>() {
                    @Override
                    public void onCompleted() {

                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(InfoData data) {
                        mView.hideWaitingRing();
                        if (data.getData() != null && data.getData().getNewsFastList().size() > 0){
                            Constant.systemTime = data.getData().getSystemTime();  // use for getHomeFlushData(获取小红点)
                            Log.d(TAG, "------------------------------------------------------");
                            Log.d(TAG, "================ onNext === Constant.systemTime: "+ Constant.systemTime);
                            Log.d(TAG, "------------------------------------------------------");
                            if (yesterday == null){  // load first (size < 10 -> load more to full screen) :set in Activity initAll
                                mAdapter.clear();
                                mAdapter.setDataList(data.getData());

                            }else {                  // load more
                                mAdapter.addDataList(data.getData());
                            }
                        }else {
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                        }
                        Log.d(TAG, "================ onNext === mAdapter.getItemCount() : "+ mAdapter.getItemCount());
                        yesterday = data.getData().getYesterday();

                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 企业调价
    @Override
    public void getPriceAdjustInfoData() {
        LogUtils.printInfo(TAG, "GET DATA ------------ 企业调价");
        Subscription rxSubscription = model
                .getPriceAdjustInfoData(yesterday)
                .compose(RxUtil.<InfoData>rxSchedulerHelper())
                .subscribe(new Subscriber<InfoData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(InfoData data) {
                        if (data.getData() != null && data.getData().getNewsFastList().size() > 0){
                            Constant.systemTime = data.getData().getSystemTime();  // use for getHomeFlushData(获取小红点)
                            if (yesterday == null){  // load first (size < 10 -> load more to full screen)
                                mAdapter.clear();
                                mAdapter.setDataList(data.getData());

                            }else {                  // load more
                                mAdapter.addDataList(data.getData());
                            }
                        }else {
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                        }
                        Log.d(TAG, "================ onNext === mAdapter.getItemCount() : "+ mAdapter.getItemCount());
                        yesterday = data.getData().getYesterday();

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 装置动态
    @Override
    public void getDynamicInfoData() {
        LogUtils.printInfo(TAG, "GET DATA ------------ 装置动态");
        Subscription rxSubscription = model
                .getDynamicInfoData(yesterday)
                .compose(RxUtil.<InfoData>rxSchedulerHelper())
                .subscribe(new Subscriber<InfoData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(InfoData data) {
                        if (data.getData() != null){
                            Constant.systemTime = data.getData().getSystemTime();   // use for getHomeFlushData(获取小红点)
                            if (yesterday == null && data.getData().getNewsFastList().size() > 0){  // load first (size < 10 -> load more to full screen)
                                mAdapter.clear();
                                mAdapter.setDataList(data.getData());

                            }else {                  // load more
                                mAdapter.addDataList(data.getData());
                            }
                        }else {
                            mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                        }
                        Log.d(TAG, "================ onNext === mAdapter.getItemCount() : "+ mAdapter.getItemCount());
                        yesterday = data.getData().getYesterday();
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 市场风云
    @Override
    public void getFyNewsData(int pageNum) {
        LogUtils.printInfo(TAG, "GET DATA ------------ 市场风云");
        Subscription rxSubscription = model
                .getHomeBottomFyNew(pageNum)
                .compose(RxUtil.<BottomNewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<BottomNewsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString());  //  提示信息(e.toString());
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(BottomNewsData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null && data.data.bottomNews != null){
                                if (data.data.bottomNews.recordList != null && data.data.bottomNews.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mNewsAdapter.clear();
                                        mNewsAdapter.setDataList(data.data.bottomNews.recordList);
                                        LogUtils.printInfo(TAG, "mNewsAdapter.setDataList: "+ data.data.bottomNews.recordList.size());
                                    }else {
                                        mNewsAdapter.addDataList(data.data.bottomNews.recordList);
                                        LogUtils.printInfo(TAG, "mNewsAdapter.addDataList: "+ data.data.bottomNews.recordList.size());
                                    }
                                    currentPageNum ++;
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

    // 深度研报
    @Override
    public void getSdNewsData(int pageNum) {
        LogUtils.printInfo(TAG, "GET DATA ------------ 深度研报");
        Subscription rxSubscription = model
                .getHomeBottomSdNew(pageNum)
                .compose(RxUtil.<BottomNewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<BottomNewsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString());  //  提示信息(e.toString());
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(BottomNewsData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null && data.data.bottomNews != null){
                                if (data.data.bottomNews.recordList != null && data.data.bottomNews.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mNewsAdapter.clear();
                                        mNewsAdapter.setDataList(data.data.bottomNews.recordList);
                                    }else {
                                        mNewsAdapter.addDataList(data.data.bottomNews.recordList);
                                    }
                                    currentPageNum ++;
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

    // 行业热点
    @Override
    public void getHyNewsData(int pageNum) {
        LogUtils.printInfo(TAG, "GET DATA ------------ 行业热点");
        Subscription rxSubscription = model
                .getHomeBottomHyNew(pageNum)
                .compose(RxUtil.<BottomNewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<BottomNewsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString());  //  提示信息(e.toString());
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(BottomNewsData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null && data.data.bottomNews != null){
                                if (data.data.bottomNews.recordList != null && data.data.bottomNews.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mNewsAdapter.clear();
                                        mNewsAdapter.setDataList(data.data.bottomNews.recordList);
                                    }else {
                                        mNewsAdapter.addDataList(data.data.bottomNews.recordList);
                                    }
                                    currentPageNum ++;
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

    // 宏观头条
    @Override
    public void getTtNewsData(int pageNum) {
        LogUtils.printInfo(TAG, "GET DATA ------------ 宏观头条");
        Subscription rxSubscription = model
                .getHomeBottomTtNew(pageNum)
                .compose(RxUtil.<BottomNewsData>rxSchedulerHelper())
                .subscribe(new Subscriber<BottomNewsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString());  //  提示信息(e.toString());
                        mNewsAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(BottomNewsData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null && data.data.bottomNews != null){
                                if (data.data.bottomNews.recordList != null && data.data.bottomNews.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mNewsAdapter.clear();
                                        mNewsAdapter.setDataList(data.data.bottomNews.recordList);
                                    }else {
                                        mNewsAdapter.addDataList(data.data.bottomNews.recordList);
                                    }
                                    currentPageNum ++;
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

}
