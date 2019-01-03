package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;
import com.tuacy.fuzzysearchlibrary.PinyinUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :
 * version: 1.0
 * </pre>
 */
public class SearchPresenter implements SearchContract.Presenter {

    String TAG = "SearchPresenter";
    @Inject SearchActivity parentActivity;
    @Inject SearchContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    @Inject
    FuzzySearchAdapter mFuzzySearchAdapter;  // 模糊搜索的结果 Adapter
    @Inject
    AZListAdapter mAdapter;
    //AdminAdapter mAdapter;


    @Inject
    public SearchPresenter(SearchActivity activity){
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

    }

    @Override
    public FuzzySearchAdapter getFuzzySearchAdapter() {
        return mFuzzySearchAdapter;
    }

    @Override
    public AZListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void getAdminData() {
        Subscription rxSubscription = model
                .getAdministrator()
                .compose(RxUtil.<AdminData>rxSchedulerHelper())
                .subscribe(new Subscriber<AdminData>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "getProductData  onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showMsg(e.toString());
                    }

                    @Override
                    public void onNext(AdminData data) {
                        if ("0".equals(data.code)){
                            if (data.data!= null && data.data.size() > 0){

                                mAdapter.setDataList(fillData(data.data));
                                mFuzzySearchAdapter.setDataList(fillData(data.data));
                            }
                        }else {
                            mView.showMsg(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);
    }


    private List<ItemEntity> fillData(List<AdminData.AdminEntity> date) {
        List<ItemEntity> sortList = new ArrayList<>();
        for (AdminData.AdminEntity item : date) {
            String letter;
            //汉字转换成拼音
            List<String> pinyinList = PinyinUtil.getPinYinList(item.name);
            if (pinyinList != null && !pinyinList.isEmpty()) {
                // A-Z导航
                String letters = pinyinList.get(0).substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (letters.matches("[A-Z]")) {
                    letter = letters.toUpperCase();
                } else {
                    letter = "#";
                }
            } else {
                letter = "#";
            }
            sortList.add(new ItemEntity(item.name, item, letter, pinyinList));
        }
        return sortList;

    }
}
