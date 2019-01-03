package com.ihwdz.android.hwapp.ui.purchase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PurchaseData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressActivity;
import com.ihwdz.android.hwapp.ui.purchase.purchasedetail.PurchaseDetailActivity;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailActivity;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.ihwdz.android.hwapp.common.Constant.Remarks.REVIEW_PRICE;
import static com.ihwdz.android.hwapp.ui.purchase.PurchaseFragmentContract.ALL_DATA_MODE;
import static com.ihwdz.android.hwapp.ui.purchase.PurchaseFragmentContract.MY_DATA_MODE;
import static com.ihwdz.android.hwapp.ui.purchase.PurchaseFragmentContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/25
 * desc :
 * version: 1.0
 * </pre>
 */
public class PurchaseFragmentPresenter implements PurchaseFragmentContract.Presenter {


    String TAG = "PurchaseFragmentPresenter";

    private MainActivity activity;
    private PurchaseFragmentContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    LogisticsModel model;

    @Inject PurchaseAdapter mAdapter;

    private int currentMode = ALL_DATA_MODE;
    private int currentPageNum = 1;

    private boolean mIsLoadingData = false;   // 正在加载数据


    @Inject
    public PurchaseFragmentPresenter(PurchaseFragmentContract.View view) {
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
        model = new LogisticsModel(activity);

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
        if (activity != null) {
            activity = null;
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
    public void bindActivity(MainActivity activity) {
        this.activity = activity;
        model = new LogisticsModel(activity);
    }

    @Override
    public PurchaseAdapter getAdapter() {
        if(mAdapter == null){
            mAdapter = new PurchaseAdapter(activity);
        }
        return mAdapter;
    }

    @Override
    public void refreshData() {
        getUserType();
        mAdapter.clear();
        setCurrentPageNum(1);
        switch (currentMode){
            case ALL_DATA_MODE:
                getPurchasePoolData();
                break;
            case MY_DATA_MODE:
                // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员;
                switch (Constant.VIP_TYPE){
                    case -1:
                        // 普通用户
                        break;
                    case 0:
                        // 资讯会员
                        break;
                    case 1:
                        // 交易
                        getMyPurchaseListData();
                        break;
                    case 2:
                        // 商家
                        getMyQuoteListData();
                    default:
                        // 未登录
                        break;
                }
                break;
        }
    }

    // 判断用户类型、锁定、认证状态
    @Override
    public void getUserType() {
        Subscription rxSubscription = model
                .getUserType()
                .compose(RxUtil.<UserTypeData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserTypeData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getUserType onError: " + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserTypeData data) {
                        if ("0".equals(data.code)){
                            if (data != null && data.data != null){

                                Constant.VIP_TYPE = data.data.type;               //会员类型：-1 用户; 0 资讯; 1 交易; 2 商家;
                                Constant.VIP_LOCK_STATUS = data.data.status;      // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                                Constant.VIP_AUTHENTIC = data.data.authStatus == 1;   // 认证状态 0==未认证, 1==已认证

                            }
                        }else {
                            LogUtils.printCloseableInfo(TAG,"getUserType === data.msg: ======================= "+ data.msg);
                            LogUtils.printCloseableInfo(TAG,"getUserType === Constant.token ======================= "+ Constant.token);
//                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
//                                if (Constant.token != null && Constant.token.length()>0){
//                                    mView.showPromptMessage(data.msg);
//                                }
//                            }
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 求购池 - 获取全部求购数据
    @Override
    public void getPurchasePoolData() {
        LogUtils.printError(TAG + "PurchasePool", "************** getPurchasePoolData ========== ");
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getPurchasePoolData(currentPageNum, PageSize)
                .compose(RxUtil.<PurchaseData>rxSchedulerHelper())
                .subscribe(new Subscriber<PurchaseData>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, "getPurchasePoolData ========== onError: "+ e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                    }

                    @Override
                    public void onNext(PurchaseData data) {
                        mView.hideWaitingRing();
                        mView.hideEmptyView();
                        //LogUtils.printCloseableInfo(TAG, "getPurchasePoolData ========== onNext: " + data.data.recordList.size());
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                if (data.data.recordList != null && data.data.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }
                            }else {
                                mAdapter.clear();
                                mView.showEmptyView();
                            }
                        }else {
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 求购池 - 获取我的求购数据
    @Override
    public void getMyPurchaseListData() {
        mIsLoadingData = true;
        LogUtils.printError(TAG + "MyPurchaseList", "************** getMyPurchaseListData ========== currentPageNum: " + currentPageNum);
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getMyPurchaseData(currentPageNum, PageSize)
                .compose(RxUtil.<PurchaseData>rxSchedulerHelper())
                .subscribe(new Subscriber<PurchaseData>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        mIsLoadingData = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, "getMyPurchaseListData ========== onError: "+ e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                        mIsLoadingData = false;
                    }

                    @Override
                    public void onNext(PurchaseData data) {
                        mView.hideWaitingRing();
                        mView.hideEmptyView();
                        //LogUtils.printCloseableInfo(TAG, "getMyPurchaseListData ========== onNext: " + data.data.recordList.size());
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                if (data.data.recordList != null && data.data.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }
                            }else {
                                mAdapter.clear();
                                mView.showEmptyView();
                            }
                        }else {
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }

                        mIsLoadingData = false;

                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 求购池 - 获取我的报价数据
    @Override
    public void getMyQuoteListData() {
        mIsLoadingData = true;
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getMyQuoteData(currentPageNum, PageSize)
                .compose(RxUtil.<PurchaseData>rxSchedulerHelper())
                .subscribe(new Subscriber<PurchaseData>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_PREPARE);
                        mIsLoadingData = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, "getMyQuoteListData ========== onError: "+ e.toString());
                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_ERROR);
                        mIsLoadingData = false;
                    }

                    @Override
                    public void onNext(PurchaseData data) {
                        mView.hideWaitingRing();
                        mView.hideEmptyView();
                        //LogUtils.printCloseableInfo(TAG, "getMyQuoteListData ========== onNext: " + data.data.recordList.size());
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                if (data.data.recordList != null && data.data.recordList.size() > 0){
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mAdapter.setDataList(data.data.recordList);
                                    }else {
                                        mAdapter.addDataList(data.data.recordList);
                                    }
                                    currentPageNum ++;
                                }else {
                                    if (currentPageNum == 1){
                                        mAdapter.clear();
                                        mView.showEmptyView();
                                    }else {
                                        mAdapter.setLoadMoreStatus(Constant.loadStatus.STATUS_EMPTY);
                                    }
                                }
                            }else {
                                mAdapter.clear();
                                mView.showEmptyView();
                            }
                        }else {
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                        }
                        mIsLoadingData = false;

                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public boolean isLoadingData() {
        return mIsLoadingData;
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
    public void setCurrentMode(int mode) {
        if (mode != currentMode){
            this.currentMode = mode;
            LogUtils.printCloseableInfo(TAG, "setCurrentMode mode != currentMode: " + mode);
            mAdapter.setMode(mode);
            mView.updateView();          //  更新视图 + 数据
            LogUtils.printCloseableInfo(TAG, "setCurrentMode ============ refreshData  currentMode: " + currentMode);
            refreshData();
        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void gotoLoginPage() {
        // setCurrentMode(ALL_DATA_MODE);
        Intent intent = new Intent(activity.getBaseContext(), LoginPageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoOpenDealVip() {
        // setCurrentMode(ALL_DATA_MODE);
        ApplyProgressActivity.startApplyProgressActivity(activity, Constant.ApplyFrom.PURCHASE_POOL);
    }

    @Override
    public void gotoBusinessAuthentic() {
        Toast.makeText(activity,"开启商家认证", Toast.LENGTH_SHORT);
    }

    // 我的求购 item - 求购报价
    @Override
    public void gotoPurchaseDetail(String id, String status, String breed, String qty, String address) {
        Intent intent = new Intent(activity, PurchaseDetailActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("STATUS", status);
        intent.putExtra("BREED", breed);
        intent.putExtra("QTY", qty);
        intent.putExtra("ADDRESS", address);
        activity.startActivity(intent);
    }

    // 我的报价 item -》 报价详情
    @Override
    public void gotoQuoteDetail(String id, String status, String breed, String qty, String address, String dateStr) {

        QuoteDetailActivity.startQuoteDetailActivity(activity,true, id, status, breed, qty, address,dateStr);

    }

    // 报价 -> QuoteDetailActivity
    @Override
    public void doQuote(boolean isQuoteEnable, String id, String status, String breed, String qty, String address, String dateStr) {
        if (isQuoteEnable){
            LogUtils.printCloseableInfo(TAG, "id: " + id);
            LogUtils.printCloseableInfo(TAG, "status: " + status);
            LogUtils.printCloseableInfo(TAG, "dataBreed: " + breed);
            LogUtils.printCloseableInfo(TAG, "dataQty: " + qty);
            LogUtils.printCloseableInfo(TAG, "dataAddress: " + address);

            QuoteDetailActivity.startQuoteDetailActivity(activity, false, id, status, breed, qty, address, dateStr);

        }else {
            mView.showRemindDialog(activity.getResources().getString(R.string.business_auth_remind));
        }


    }

    // 商家会员已认证  复议报价
    @Override
    public void doReview(String id, String price) {
        String content = activity.getResources().getString(R.string.quote_range);  // 仅一次
        boolean isHint = true;
        ReviewActivity.startReviewActivity(activity, REVIEW_PRICE, content, isHint,null, id);
    }

    // 联系客服弹窗
    @Override
    public View getDialogContentView(String message) {
        LogUtils.printError(TAG, "getDialogContentView message: " + message);
        if (message.contains(":")){
            String[] strings =  message.split(":");
            phoneNumber = strings[1];
        }

        View view = View.inflate(activity, R.layout.contract_dialog, null);
        TextView messageTv = view.findViewById(R.id.tv);
        messageTv.setText(message);  // 弹窗内容
        return view;
    }

    private String phoneNumber;
    @Override
    public void doContract() {
        //mView.showPromptMessage("请直接点击客服电话");
        Intent Intent =  new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
        startActivity(Intent);
    }

}
