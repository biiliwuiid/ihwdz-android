package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ApplyDealVipData;
import com.ihwdz.android.hwapp.model.bean.LicenseData;
import com.ihwdz.android.hwapp.model.bean.ProductData;
import com.ihwdz.android.hwapp.model.bean.UserStateData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.me.infovip.searchadmin.SearchActivity;
import com.ihwdz.android.hwapp.utils.DateUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.DATE_ESTABLISH;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.DATE_VALID;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_ENTERPRISE;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_PRODUCT;
import static com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressContract.STATE_RESULT_APPLYING;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :   开通交易会员
 * version: 1.0
 * </pre>
 */
public class ApplyProgressPresenter implements ApplyProgressContract.Presenter {

    String TAG = "ApplyProgressPresenter";
    @Inject ApplyProgressActivity parentActivity;
    @Inject ApplyProgressContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    @Inject WaterfallAdapter mGoodsAdapter;
    @Inject WaterfallAdapter mMaterialAdapter;

    private int currentApplyResult = -1;  // 开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料
    private int currentState = STATE_ENTERPRISE;       // 当前申请进度 - 企业信息 -产品信息 - 管理员信息

    private ApplyDealVipData applyData;// 提交数据类

    private String licensePath;        //

    private String currentCreditCode;  // 社会信用代码 需调接口校验是否已存在
    private String currentCompanyName; // 单位名称 需调接口校验是否已存在
    private String currentLegalName;   // 法人
    private String currentStartTimeStr = null; // 成立日期
    private String currentEndTimeStr = null;   // 有效期

    // private String currentTaxNumber;   // get from POST LICENSE , use for check TaxNumber  社会信用代码
    // private String currentMemberName;  // get from POST LICENSE , use for check MemberName 公司名称

    private boolean isLicenseUpload = false;
    private boolean isChangeable = false;         // 信息是否允许修改

    private boolean isCreditCodeChecked = false;  // 社会信用代码校验 是否已存在 isTaxNumberChecked
    private boolean isCompanyNameChecked = false; // 公司名称校验 是否已存在     isMemberNameChecked

    private String lastSelectedM = "";  // 采购原料
    private String lastSelectedG = "";  // 主营商品类型
    private String material = "";       // "material: "
    private String goods = "";          // "goods: "

    private String admin = null;   // "admin: "
    private String adminId = null;      // "adminId: "

    private boolean acceptAssign = false; // 接受平台指派营销顾问


    private boolean isSubmitClicked = false;

    @Inject
    public ApplyProgressPresenter(ApplyProgressActivity activity){
        this.parentActivity = activity;
        model = new LoginDataModel(parentActivity);
        applyData = new ApplyDealVipData();
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
    public WaterfallAdapter getGoodsAdapter() {
        return mGoodsAdapter;
    }

    @Override
    public WaterfallAdapter getMaterialAdapter() {
        return mMaterialAdapter;
    }

    @Override
    public void getApplyStateData() {
        Subscription rxSubscription = model
                .getUserStatus()
                .compose(RxUtil.<UserStateData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserStateData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserStateData data) {
                        if ("0".equals(data.code)){
                            // Constant.APPLY_STATUS = Integer.parseInt(data.data.type);
                            // 开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料
                            Constant.APPLY_STATUS = Integer.parseInt(data.data.tradeMemberApplyStatus);

                            // 交易会员是否通过认证
                            boolean isAuthenticated = false;
                            if (data.data.totalAmount != null && data.data.totalAmount.length()> 0){
                                isAuthenticated = true;
                                Constant.totalAmount = data.data.totalAmount;
                                Constant.usedAmount = data.data.usedAmount;
                                Constant.availableAmount = data.data.availableAmount;
                            }
                            Constant.isAuthenticated = isAuthenticated;

                            Constant.goodsName = data.data.goodsName;
                            Constant.endDateStr = data.data.endDateStr;
                            Constant.endDate = data.data.goodsName + data.data.endDateStr;

                            LogUtils.printCloseableInfo(TAG,"更新 申请状态:Constant.APPLY_STATUS:  ======================= "+ Constant.APPLY_STATUS);
                            setApplyResult(Constant.APPLY_STATUS);  // 更新 申请状态

                        }else {
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setApplyState(int state) {
        if (currentState != state){
            currentState = state;
            mView.updateView();
        }
    }

    @Override
    public int getApplyState() {
        return currentState;
    }

    @Override
    public void setApplyResult(int result) {
        if (currentApplyResult != result){
            currentApplyResult = result;
            mView.updateView();
        }
    }

    @Override
    public int getApplyResult() {
        return currentApplyResult;
    }


    @Override
    public boolean isEnterpriseInfoComplete() {
        Log.d(TAG, " =============== isEnterpriseInfoComplete");
        boolean isComplete = true;

        // check CreditCode 社会信用代码18位
        if (TextUtils.isEmpty(currentCreditCode)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.social_credit_code_remind));
            return false;
        }else {
            if (currentCreditCode.length() != 18){
                mView.showPromptMessage(parentActivity.getResources().getString(R.string.social_credit_code_remind));
                return false;
            }
        }

        // check CompanyName
        if (TextUtils.isEmpty(currentCompanyName)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.company_name_empty));
            return false;
        }

        // check LegalName
        if (TextUtils.isEmpty(currentLegalName)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.name_legal_person_null));
            return false;
        }
//        if (!checkCreditCode()){
//
//            return false;
//        }

        return isComplete;
    }

    // 选择日期（成立日期/有效期）
    @Override
    public void selectDate(final int mode) {
        // 当日日期
        int todayYear, todayMonth, todayDay;
        int currentYear, currentMonth, currentDay;
        String todayStr = DateUtils.getDateTodayString();  // 当天日期
        //LogUtils.printCloseableInfo(TAG, todayStr);

        String[] todayDate = todayStr.split("-");
        todayYear = Integer.valueOf(todayDate[0]);
        todayMonth = Integer.valueOf(todayDate[1]);
        todayDay = Integer.valueOf(todayDate[2]);

        switch (mode){
            case DATE_ESTABLISH:
                if (currentStartTimeStr != null){
                    todayStr = currentStartTimeStr;
                }
                break;
            case DATE_VALID:
                if (currentEndTimeStr != null){
                    todayStr = currentEndTimeStr;
                }
                break;
        }

        String[] strs = todayStr.split("-");
        currentYear = Integer.valueOf(strs[0]);
        currentMonth = Integer.valueOf(strs[1]);
        currentDay = Integer.valueOf(strs[2]);

        final DatePicker picker = new DatePicker(parentActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(parentActivity, 10));
        picker.setRangeEnd(todayYear + 1000, currentMonth, currentDay);
        picker.setRangeStart(1945, todayMonth, todayDay);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                switch (mode){
                    case DATE_ESTABLISH:
                        setStartTime(year + "-" + month + "-" + day);
                        break;
                    case DATE_VALID:
                        setEndTime(year + "-" + month + "-" + day);
                        break;
                }


            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.setLabel(" "," ", " ");
        picker.setTitleText(currentYear + "-" + currentMonth + "-" + currentDay);
        picker.show();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 验证身份证格式
     */
    public static boolean isIdCardNO(String idNo) {
        //String telRegex = "[1][3456789]\\d{9}";
        String telRegex = "/^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$/";
        if (TextUtils.isEmpty(idNo))
            return false;
        else
            return idNo.matches(telRegex);
    }

    /**
     * 验证身份证号是否符合规则
     * @param text 身份证号
     * @return
     */
    public boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }


    @Override
    public void setLicensePath(String path) {
        this.licensePath = path;
    }

    @Override
    public boolean isLicenseUpload() {
        if (!isLicenseUpload){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.license_null));
        }
        return isLicenseUpload;
    }

    // 上传 营业执照 后初始化 上传数据
    @Override
    public void postLicense(String token, String filePath) {
        Subscription rxSubscription = model
                .postLicense(Constant.token, filePath)
                .subscribe(new Subscriber<LicenseData>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "=========   onCompleted   =========== " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLicenseUpload = false;
                        Log.e(TAG, "=========   onError   =========== " + e.toString());
                    }

                    @Override
                    public void onNext(LicenseData data) {
                        Log.e(TAG, "=========   onNext  needChange =========== " + data.data.needChange);
                        if ("0".equals(data.code)){
                            mView.hideWaitingRing();

                            isLicenseUpload = true;
                            isChangeable = TextUtils.equals("true",data.data.needChange);
                            mView.setEditable(isChangeable);
                            mView.showLicenseInfo(data.data);

                            currentCompanyName = data.data.companyName;
                            currentCreditCode = data.data.creditCode;

                            initApplyData(data.data);

                        }else {
                            mView.showMsg(data.msg);
                        }
                    }
                });



        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setCreditCode(String code) {
        applyData.creditCode = code;
        currentCreditCode = code;

    }


    @Override
    public void setCompanyName(String name) {
        applyData.companyName = name;
        currentCompanyName = name;

    }

    @Override
    public void setLegalName(String name) {
        this.currentLegalName = name;
    }

    @Override
    public void setStartTime(String time) {
        this.currentStartTimeStr = time;
        mView.updateEstablishDate(currentStartTimeStr);
    }

    @Override
    public void setEndTime(String time) {
        this.currentEndTimeStr = time;
        mView.updateValidDate(currentEndTimeStr);
    }

    // 初始化 上传数据
    private void initApplyData(LicenseData.LicenseEntity data) {
        applyData.imageUrl = data.fileUrl;
        applyData.creditCode = data.creditCode;
        applyData.companyName = data.companyName;
        applyData.legalPerson = data.legalPerson;
        applyData.startTimeStr = data.startTimeStr;
        applyData.endTimeStr = data.endTimeStr;

        applyData.materials = null;
        applyData.goodsTypes = null;

        applyData.buyerAdminId = null;
    }


    // 校验 社会信用代码 data.code = "0";正常
    @Override
    public boolean checkCreditCode() {
        mView.showChecking();
        Subscription rxSubscription = model
                .getCheckTaxNumberData(currentCreditCode)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideChecking();
                        mView.showPromptMessage(e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        LogUtils.printCloseableInfo(TAG, "checkCreditCode  onNext: " + data.msg);
                        if (TextUtils.equals("0", data.code)){
                            isCreditCodeChecked = true;
                            checkCompanyName();
                        }else {
                            mView.hideChecking();
                            mView.showPromptMessage(data.msg);
                            isCreditCodeChecked = false;
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

        return true;
//        if (isCreditCodeChecked){
//            return checkCompanyName();
//        }else {
//            mView.hideChecking();
//            return isCreditCodeChecked;
//        }

    }
    // 校验 单位名称  data.code = "0";正常
    @Override
    public boolean checkCompanyName() {
        Subscription rxSubscription = model
                .getCheckCompanyNameData(currentCompanyName)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideChecking();
                        mView.showPromptMessage(e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        LogUtils.printCloseableInfo(TAG, "checkCompanyName  onNext: " + data.msg);
                        mView.hideChecking();

                        if (TextUtils.equals("0", data.code)){
                            isCompanyNameChecked = true;
                            setApplyState(STATE_PRODUCT);   // 跳转到step2: 产品信息
                        }else {
                            mView.showPromptMessage(data.msg);
                            isCompanyNameChecked = false;

                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
        return isCompanyNameChecked;
    }

    // 获取生产信息
    @Override
    public void getProductData() {
        Subscription rxSubscription = model
                .getProductData()
                .compose(RxUtil.<ProductData>rxSchedulerHelper())
                .subscribe(new Subscriber<ProductData>() {
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
                    public void onNext(ProductData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                if (data.data.goodsTypeList != null && data.data.goodsTypeList.size() > 0){
                                    mGoodsAdapter.setDataList(data.data.goodsTypeList);
                                }
                                if (data.data.materialList != null && data.data.materialList.size() > 0){
                                    mMaterialAdapter.setDataList(data.data.materialList);
                                }

                            }
                        }else {
                            mView.showMsg(data.msg);
                        }

                    }
                });

        mSubscriptions.add(rxSubscription);
    }


    @Override
    public void setMaterialProductInfo(String selected) {
        if (selected != null && !selected.equals(lastSelectedM)){
            lastSelectedM = selected;
            material = material + selected +",";
        }
    }

    @Override
    public void setGoodsProductInfo(String selected) {
        if (selected != null && !selected.equals(lastSelectedG)){
            lastSelectedG = selected;
            goods = goods + selected +",";
        }
    }

    @Override
    public void setAdmin(String s) {
//        Log.d(TAG, "========  admin.length() ==========  " + admin.length());
//        admin = "admin: ";
//        admin = admin + s;
        admin = s;
    }

    @Override
    public void setAdminId(String id) {
        adminId = id;
    }

    @Override
    public void setAcceptAssign(boolean accept) {
        this.acceptAssign = accept;
    }

    @Override
    public void goSelectAdmin() {
        Intent intent = new Intent(parentActivity, SearchActivity.class);
        parentActivity.startActivity(intent);
    }

    @Override
    public boolean isAdminComplete() {
        LogUtils.printCloseableInfo(TAG, "======== acceptAssign ========== " + acceptAssign);
        if (TextUtils.isEmpty(admin) && !acceptAssign){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.admin_null));
            return false;
        }else {
            return true;
        }
    }

    // 设置 生产信息 及 管理人
    @Override
    public void setAdminData() {
        applyData.materials = material.trim();
        applyData.goodsTypes = goods.trim();
        applyData.buyerAdminId = adminId;

        // LogUtils.printCloseableInfo(TAG, material + "\r\n"+ goods +"\r\n"+admin +"\r\n"+ adminId);
        // mView.showPromptMessage(material + "\r\n"+ goods +"\r\n"+admin +"\r\n"+ adminId);
    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    @Override
    public void postApplyData() {
        isSubmitClicked = true;
        applyData.token = Constant.token;
        LogUtils.printCloseableInfo(TAG, "postApplyData ================================================= " );
        LogUtils.printCloseableInfo(TAG, "applyData.token:  "+ applyData.token );
        LogUtils.printCloseableInfo(TAG, "applyData.imageUrl:  "+ applyData.imageUrl );
        LogUtils.printCloseableInfo(TAG, "currentCreditCode:  "+ currentCreditCode );
        LogUtils.printCloseableInfo(TAG, "currentCompanyName:  "+ currentCompanyName );
        LogUtils.printCloseableInfo(TAG, "currentLegalName:  "+ currentLegalName );
        LogUtils.printCloseableInfo(TAG, "currentStartTimeStr:  "+ currentStartTimeStr );
        LogUtils.printCloseableInfo(TAG, "currentEndTimeStr:  "+ currentEndTimeStr );
        LogUtils.printCloseableInfo(TAG, "applyData.materials:  "+ applyData.materials );
        LogUtils.printCloseableInfo(TAG, "applyData.goodsTypes:  "+ applyData.goodsTypes );
        LogUtils.printCloseableInfo(TAG, "applyData.buyerAdminId:  "+ applyData.buyerAdminId );

        if (adminId == null){
            LogUtils.printCloseableInfo(TAG, "applyData.buyerAdminId:  null!");
        }else {
            LogUtils.printCloseableInfo(TAG, "applyData.buyerAdminId:  "+ applyData.buyerAdminId );
        }
        LogUtils.printCloseableInfo(TAG, "postApplyData ================================================= " );

        Subscription rxSubscription = model
               // .postApplyData(applyData)
                .postApplyData(applyData.imageUrl, currentCreditCode, currentCompanyName, currentLegalName, currentStartTimeStr,
                               currentEndTimeStr, applyData.materials, applyData.goodsTypes, applyData.buyerAdminId  )
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showPromptMessage(e.toString());
                        LogUtils.printError(TAG, "postApplyData  onError: " + e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        LogUtils.printCloseableInfo(TAG, "postApplyData  onNext: " + data.msg);
                        if (TextUtils.equals("0", data.code)){
                            mView.showPromptMessage(data.msg);
                            Constant.APPLY_STATUS = STATE_RESULT_APPLYING;
                            setApplyResult(STATE_RESULT_APPLYING);  // 提交成功 强制改为 1申请中。更新 申请状态
                            mView.updateView();
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }




    ////////////////////////////
//    @Override
//    public boolean isTaxNumberChecked() {
//        if (!isCreditCodeChecked){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.credit_code_error));
//        }
//        return isCreditCodeChecked;
//    }
//
//    @Override
//    public boolean isCompanyNameChecked() {
//        if (!isCompanyNameChecked){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.company_name_error));
//        }
//        return isCompanyNameChecked;
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////// 上传法人信息 以前是step1
//    private String legalTel;       //
//    private String legalIdCardNo;
//    @Override
//    public void setLegalTel(String tel) {
//        this.legalTel = tel;
//    }
//
//    @Override
//    public void setLegalIdCard(String idCard) {
//        this.legalIdCardNo = idCard;
//    }
//
//    @Override
//    public boolean isLegalInfoComplete() {
//        Log.d(TAG, " =============== isLegalInfoComplete");
//        boolean isComplete = false;
//        //check telephone no
//        boolean isPhoneNum = isMobileNO(legalTel);
//        if (TextUtils.isEmpty(legalTel)){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_no_null));
//            return false;
//        }else if (!isPhoneNum){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_not_right));
//            return false;
//        }else {
//            isComplete = true;
//        }
//
//        // check name
//        if (TextUtils.isEmpty(currentLegalName)){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.name_null));
//            return false;
//        }
//
//        //check id card no
//        boolean isIdCard = personIdValidation(legalIdCardNo);
//        if (TextUtils.isEmpty(legalIdCardNo)){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.id_no_null));
//            return false;
//        }
//        else if (!isIdCard){
//            mView.showPromptMessage(parentActivity.getResources().getString(R.string.id_not_right));
//            return false;
//        }
//        else {
//            isComplete = true;
//        }
//        return isComplete;
//    }

    // 提交 法人信息
//    @Override
//    public void postLegalPersonInfo() {
//
//        Subscription rxSubscription = model
//                .postLegalPersonInfo(Constant.token, currentLegalName, legalTel, legalIdCardNo)
//                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
//                .subscribe(new Subscriber<VerifyData>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.e(TAG, "postLegalPersonInfo  onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        mView.showMsg(e.toString());
//                    }
//
//                    @Override
//                    public void onNext(VerifyData data) {
//                        mView.showMsg(data.msg);
//                    }
//                });
//
//        mSubscriptions.add(rxSubscription);
//    }



}
