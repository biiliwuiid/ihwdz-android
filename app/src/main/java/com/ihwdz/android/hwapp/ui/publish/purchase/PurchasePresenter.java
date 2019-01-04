package com.ihwdz.android.hwapp.ui.publish.purchase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.publish.address.AddressActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;


/**
 * <pre>
 * author : Duan
 * time : 2018/10/05
 * desc :   发布求购
 * version: 1.0
 * </pre>
 */
public class PurchasePresenter implements PurchaseContract.Presenter{

    String TAG = "PurchasePresenter";

    @Inject PurchaseActivity parentActivity;
    @Inject PurchaseContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    IAppSettings settings;

    @Inject AddViewAdapter mAdapter;
    @Inject BreedInfoAdapter mBreedAdapter;
    @Inject BreedInfoAdapter mSpecAdapter;
    @Inject BreedInfoAdapter mFactoryAdapter;


    boolean isCurrentDataComplete = false;

    PublishData.ReceiverInfo addressInfoPost;             // presenter 中记住当前 收货信息
    List<PublishData.PublishPurchaseData> mPurchaseList;  // presenter 中记住当前 求购产品明细列表

    PublishData.PublishPurchaseData currentData0;

    private PublishData.ProductEntity currentBreed0;
    private PublishData.ProductEntity currentSpec0;
    private PublishData.ProductEntity currentFactory0;
    private String currentQty0;

    PublishData.PublishPurchaseData currentData;

    private PublishData.ProductEntity currentBreed;
    private PublishData.ProductEntity currentSpec;
    private PublishData.ProductEntity currentFactory;
    private String currentQty;

    private String emptyRemindStr;

    private String currentKeyword;

    private String tokenJson = null;
    private String itemsJson = null;
    private String addressJson = null;

    private boolean isSubmitClicked = false;  // 是否在提交

    @Inject
    public PurchasePresenter(PurchaseActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);
        settings = new SlimAppSettings(activity);
        currentData0 = new PublishData.PublishPurchaseData(); // 暂存 current data
        currentData = new PublishData.PublishPurchaseData();

        emptyRemindStr = parentActivity.getResources().getString(R.string.empty_search_result);
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
    public AddViewAdapter getAdapter() {
//        if (mPurchaseList == null){
//            mPurchaseList = new ArrayList<>();
//
////            currentData0 = new PublishData.PublishPurchaseData(); // 暂存 current data
////            currentData = new PublishData.PublishPurchaseData();
//
//            currentData.breed = "";
//            currentData.breedCode = "";
//            currentData.breedAlias = "";
//            currentData.spec = "";
//            currentData.factory = "";
//            currentData.qty = "";
//
//            mPurchaseList.add(currentData);
//            mAdapter.setDataList(mPurchaseList);
//        }
        return mAdapter;
    }

    @Override
    public BreedInfoAdapter getBreedAdapter() {
        return mBreedAdapter;
    }

    @Override
    public BreedInfoAdapter getSpecAdapter() {
        return mSpecAdapter;
    }

    @Override
    public BreedInfoAdapter getFactoryAdapter() {
        return mFactoryAdapter;
    }

    // 收货地址 数据
    @Override
    public void getAddressData() {
        Subscription rxSubscription = model
                .getAddressData()
                .compose(RxUtil.<PublishData.AddressData>rxSchedulerHelper())
                .subscribe(new Subscriber<PublishData.AddressData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showPromptMessage("网络异常");
                    }

                    @Override
                    public void onNext(PublishData.AddressData data) {
                        LogUtils.printCloseableInfo(TAG, "====== getAddressData ==== onNext " + data);
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null && data.data.size()>0){
                                // 设置默认地址 - 遍历有默认选默认 否则取第一条
                                boolean hasDefault  = false;
                                PublishData.AddressEntity addressEntity;
                                List<PublishData.AddressEntity> list = data.data;
                                for (int i = 0; i< list.size(); i++){
                                    addressEntity = list.get(i);
                                    if (addressEntity.isDefault == 1){
                                        hasDefault = true;
                                        setPostAddress(addressEntity);
                                    }
                                }

                                if (!hasDefault){
                                    addressEntity = data.data.get(0);
                                    setPostAddress(addressEntity);
                                }


                            }else {
                                setPostAddress(null);
                                //mView.showPromptMessage(data.msg); // 操作成功但没有数据
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // set post address info
    @Override
    public void setPostAddress(PublishData.AddressEntity data) {
        if (addressInfoPost == null){
            addressInfoPost = new PublishData.ReceiverInfo();
        }
        if (data != null){
            addressInfoPost.province = data.provinceName;// == null ? "" : data.provinceName;
            addressInfoPost.provinceCode = data.provinceCode;//  == null ? "" : data.provinceCode;
            addressInfoPost.city = data.cityName;//  == null ? "" : data.cityName;
            addressInfoPost.cityCode = data.cityCode;//  == null ? "" : data.cityCode;
            addressInfoPost.district = data.districtName;//  == null ? "" : data.districtName;
            addressInfoPost.districtCode = data.districtCode;//  == null ? "" : data.districtCode;
            addressInfoPost.address = data.address;//  == null ? "" : data.address;
            addressInfoPost.mobile = data.mobile;//  == null ? "" : data.mobile;
            addressInfoPost.contact = data.contactName;//  == null ? "" : data.contactName;
        }
        mView.updateAddress(addressInfoPost);
    }

    // 选择地址后 更新收货地址
    @Override
    public void setPostAddress() {
        if (addressInfoPost == null){
            addressInfoPost = new PublishData.ReceiverInfo();
        }
        addressInfoPost.province = Constant.addressSelected_province;
        addressInfoPost.provinceCode = Constant.addressSelected_provinceCode;
        addressInfoPost.city = Constant.addressSelected_city;
        addressInfoPost.cityCode = Constant.addressSelected_cityCode;
        addressInfoPost.district = Constant.addressSelected_district;
        addressInfoPost.districtCode = Constant.addressSelected_districtCode;
        addressInfoPost.address = Constant.addressSelected_address;
        addressInfoPost.mobile = Constant.addressSelected_mobile;
        addressInfoPost.contact = Constant.addressSelected_contact;

        mView.updateAddress(addressInfoPost);
    }

    @Override
    public void setKeyword(String s) {
        this.currentKeyword = s;
    }

    @Override
    public void getBreedData() {
        LogUtils.printCloseableInfo(TAG, "====== getBreedData");
        Subscription rxSubscription = model
                .getBreedData(currentKeyword)
                .compose(RxUtil.<PublishData>rxSchedulerHelper())
                .subscribe(new Subscriber<PublishData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PublishData data) {
                        if (TextUtils.equals("0", data.code)){

                            if (data.data!= null && data.data.size() > 0){
                                mBreedAdapter.setDataList(data.data);
                            }else {
                                // 暂时没有你要搜索的数据
                                // mBreedAdapter.setEmptyDataList(emptyRemindStr);
                                mBreedAdapter.setEmptyDataList();
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getSpecData() {
        LogUtils.printCloseableInfo(TAG, "====== getSpecData");
        String breedCode = mAdapter.getCurrentBreedCode();
        Subscription rxSubscription = model
                .getSpecData(currentKeyword, breedCode)
                .compose(RxUtil.<PublishData>rxSchedulerHelper())
                .subscribe(new Subscriber<PublishData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PublishData data) {
                        if (TextUtils.equals("0", data.code)){

                            if (data.data!= null && data.data.size() > 0){
                                mSpecAdapter.setDataList(data.data);
                            }else {
                                // 暂时没有你要搜索的数据
                                mSpecAdapter.setEmptyDataList();
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getFactoryData() {
        LogUtils.printCloseableInfo(TAG, "====== getFactoryData");
        Subscription rxSubscription = model
                .getBrandData(currentKeyword)
                .compose(RxUtil.<PublishData.PublishBrandData>rxSchedulerHelper())
                .subscribe(new Subscriber<PublishData.PublishBrandData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PublishData.PublishBrandData data) {
                        if (TextUtils.equals("0", data.code)){

                            if (data.data!= null && data.data.recordList.size() > 0){
                                mFactoryAdapter.setDataList(data.data.recordList);
                            }else {
                                // 暂时没有你要搜索的数据
                                mFactoryAdapter.setEmptyDataList();
                            }

                        }else {
                            mView.showPromptMessage(data.msg);
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);
    }

//    @Override
//    public PublishData.ProductEntity getCurrentBreed() {
//        return currentBreed;
//    }
//
//    @Override
//    public PublishData.ProductEntity getCurrentSpec() {
//        return currentSpec;
//    }
//
//    @Override
//    public PublishData.ProductEntity getCurrentFactory() {
//        return currentFactory;
//    }
//
//    @Override
//    public String getCurrentQty() {
//        return currentQty;
//    }
//
//    @Override
//    public void setCurrentBreed(PublishData.ProductEntity breed) {
//        this.currentBreed = breed;
//        this.currentBreed0 = breed;
//
//        if (breed != null){
//            if (TextUtils.isEmpty(breed.name) || breed.name.length() <= 0){
//                mView.showPromptMessage("222222222222请先选择品名！");
//                LogUtils.printCloseableInfo(TAG,"=======  setCurrentBreed  =========== 请先选择品名！");
//                return;
//            }else {
//                LogUtils.printCloseableInfo(TAG, "currentBreed: " + currentBreed.name);
//                LogUtils.printCloseableInfo(TAG, "currentBreed0: " + currentBreed0.name);
//                currentData.breed = currentBreed.name;
//                currentData.breedAlias = currentBreed.pySName;
//                currentData.breedCode =  currentBreed.code;
//                getSpecData();
//            }
//        }
//    }
//
//    @Override
//    public void setCurrentSpec(PublishData.ProductEntity spec) {
//        this.currentSpec = spec;
//        this.currentSpec0 = spec;
//        if (spec != null){
//            if (TextUtils.isEmpty(spec.name) || spec.name.length() <= 0){
//                mView.showPromptMessage("请先选择牌号！");
//                return;
//            }else {
//                LogUtils.printCloseableInfo(TAG, "currentSpec: " + currentSpec.name);
//                currentData.spec = currentSpec.name;
//                getFactoryData();
//            }
//        }
//
//
//    }
//
//    @Override
//    public void setCurrentFactory(PublishData.ProductEntity factory) {
//        this.currentFactory = factory;
//        this.currentFactory0 = factory;
//        if (factory != null){
//            if (TextUtils.isEmpty(factory.name) || factory.name.length() <= 0){
//                mView.showPromptMessage("请先选择厂家！");
//                return;
//            }else {
//                LogUtils.printCloseableInfo(TAG, "currentFactory: " + currentFactory.name);
//                currentData.factory = currentFactory.name;
//            }
//        }
//
//    }
//
//    @Override
//    public void setCurrentQty(String qty) {
//        if (!TextUtils.isEmpty(qty) && !TextUtils.equals(qty, currentQty)){
//            this.currentQty = qty;
//            this.currentQty0 = qty;
//            LogUtils.printCloseableInfo(TAG, "currentQty: " + currentQty);
//            currentData.qty = currentQty;
//        }
//
//    }


    // 添加一组 产品明细
    @Override
    public void addItem() {
        mAdapter.addData();

//        currentKeyword = null;
//        currentData0 = currentData;     // 暂存 current data
//
//        // 当前数据填写完整才允许 增加新条目
//        boolean isComplete = checkCurrentDataComplete();
//        if (!isComplete){
//            mView.showPromptMessage("请先完成当前的求购明细");
//            return;
//        }else {
//            //mAdapter.setDataList(mPurchaseList);
//            currentBreed = null;
//            currentSpec = null;
//            currentFactory = null;
//            currentQty = null;
//
//            currentData = new PublishData.PublishPurchaseData();
//            currentData.breed = "";
//            currentData.breedCode = "";
//            currentData.breedAlias = "";
//            currentData.spec = "";
//            currentData.factory = "";
//            currentData.qty = "";
//
//            mPurchaseList.add(currentData);
//
//            LogUtils.printCloseableInfo(TAG, "======================== addItem: " );
//            for (int i = 0; i< mPurchaseList.size(); i++){
//                PublishData.PublishPurchaseData tempData = mPurchaseList.get(i);
//                LogUtils.printCloseableInfo(TAG, "i: " +i+" - qty: " + tempData.qty);
//                LogUtils.printCloseableInfo(TAG, "i: " +i+" - breed: " + tempData.breed);
//                LogUtils.printCloseableInfo(TAG, "i: " +i+" - spec: " + tempData.spec);
//                LogUtils.printCloseableInfo(TAG, "i: " +i+" - factory: " + tempData.factory);
//            }
//            LogUtils.printCloseableInfo(TAG, "======================== addItem: " );
//
//            mAdapter.setDataList(mPurchaseList);
//            //mAdapter.addData(currentData);
//        }
    }


    @Override
    public boolean checkCurrentDataComplete() {

        return mAdapter.isCurrentItemCompleted();
//        boolean isComplete = true;
//
//        LogUtils.printCloseableInfo(TAG, " mPurchaseList.size(): " +  mPurchaseList.size());
//        PublishData.PublishPurchaseData temp;
//
////        for (int i= 0; i< mPurchaseList.size(); i++){
////            temp = mPurchaseList.get(i);
////            LogUtils.printCloseableInfo(TAG, " mPurchaseList: breed - " +  temp.breed);
////            LogUtils.printCloseableInfo(TAG, " mPurchaseList: spec - " +  temp.spec);
////            LogUtils.printCloseableInfo(TAG, " mPurchaseList: factory - " +  temp.factory);
////            LogUtils.printCloseableInfo(TAG, " mPurchaseList: qty - " +  temp.qty);
////        }
//
//        if (mPurchaseList.size() >= 1){
//            temp = mPurchaseList.get(mPurchaseList.size() - 1);
//            LogUtils.printCloseableInfo(TAG, " mPurchaseList: breed - " +  temp.breed);
//            LogUtils.printCloseableInfo(TAG, " mPurchaseList: spec - " +  temp.spec);
//            LogUtils.printCloseableInfo(TAG, " mPurchaseList: factory - " +  temp.factory);
//            LogUtils.printCloseableInfo(TAG, " mPurchaseList: qty - " +  temp.qty);
//
//            if (TextUtils.isEmpty(temp.breed)){
//                isComplete = false;
//            }
//            if (TextUtils.isEmpty(temp.breedCode)){
//                isComplete = false;
//            }
//            if (TextUtils.isEmpty(temp.spec)){
//                isComplete = false;
//            }
//            if (TextUtils.isEmpty(temp.factory)){
//                isComplete = false;
//            }
//            if (TextUtils.isEmpty(temp.qty)){
//                isComplete = false;
//            }
//        }
//
//       return isComplete ;
    }

    // 删除一组 产品明细
//    @Override
//    public void deleteItem(PublishData.PublishPurchaseData data) {
////        mPurchaseList.remove(data);
//    }

    // 弹出 选择地址
    @Override
    public void gotoSelectAddress() {
        AddressActivity.startAddressActivity(parentActivity);
    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    @Override
    public void setIsSubmitClicked(boolean isClicked) {
        this.isSubmitClicked = isClicked;
        mView.makeButtonDisable(isSubmitClicked);
    }


    @Override
    public void doSubmit() {
        // isSubmitClicked = true;
        // mView.makeButtonDisable(isSubmitClicked);   // 控制按钮不可用
        setIsSubmitClicked(true);

        mPurchaseList = mAdapter.getDataList();


        // 求购数据
        PublishData.MemberPurchaseDTOs  purchaseData =  new PublishData.MemberPurchaseDTOs();
        purchaseData.token = Constant.token;
        purchaseData.items = mPurchaseList;
        purchaseData.address = addressInfoPost;

        Gson gson = new Gson();

//        itemsJson = gson.toJson(mPurchaseList);
//        LogUtils.printCloseableInfo(TAG, "itemsJson: " + itemsJson);
//
//        addressJson = gson.toJson(addressInfoPost);
//        LogUtils.printCloseableInfo(TAG, "addressJson: " + addressJson);

        final String dataJson = gson.toJson(purchaseData);

        Subscription rxSubscription = model
                .postPurchaseData(dataJson)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.printCloseableInfo(TAG, "onCompleted doSubmit ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "onError doSubmit: " + e.toString());
                        mView.showPromptMessage(e.toString());
                        // isSubmitClicked = false;
                        setIsSubmitClicked(false);
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        LogUtils.printCloseableInfo(TAG, "onNext doSubmit: " + data);
                        if (TextUtils.equals("0", data.code)){
                            gotoMyPurchase();
                        }else {
                            // isSubmitClicked = false;
                            setIsSubmitClicked(false);
                            if (data.msg.contains(":")){
                                LogUtils.printCloseableInfo(TAG, "onNext doSubmit:data.msg:  " + data.msg);
                                mView.showRemindDialog(data.msg);
                            }else {
                                LogUtils.printCloseableInfo(TAG, "onNext doSubmit:data.msg:  " + data.msg);
                                mView.showPromptMessage(data.msg);
                            }

                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void gotoMyPurchase() {
        // 发布成功后 前往求购池 - 我的求购
        MainActivity.startActivity(parentActivity,1);
    }

    // 联系客服弹窗
    @Override
    public View getDialogContentView(String message) {
        LogUtils.printError(TAG, "getDialogContentView message: " + message);
        if (message.contains(":")){
            String[] strings =  message.split(":");
            phoneNumber = strings[1];
        }

        View view = View.inflate(parentActivity, R.layout.contract_dialog, null);
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
