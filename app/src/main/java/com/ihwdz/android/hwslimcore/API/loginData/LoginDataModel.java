package com.ihwdz.android.hwslimcore.API.loginData;

import android.content.Context;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.ihwdz.android.hwapp.model.bean.ApplyDealVipData;
import com.ihwdz.android.hwapp.model.bean.DealRecordData;
import com.ihwdz.android.hwapp.model.bean.EnterpriseInformation;
import com.ihwdz.android.hwapp.model.bean.InvoiceData;
import com.ihwdz.android.hwapp.model.bean.LicenseData;
import com.ihwdz.android.hwapp.model.bean.LoginData;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.model.bean.ProductData;
import com.ihwdz.android.hwapp.model.bean.QuotaData;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.model.bean.UserInformation;
import com.ihwdz.android.hwapp.model.bean.UserStateData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.API.RetrofitWrapper;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc :
 * version: 1.0
 * </pre>
 */
public class LoginDataModel {

    private static LoginDataModel model;
    private LoginDataApi mApiService;

    public LoginDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(HwApi.HWDZ_URL)
                .create(LoginDataApi.class);
    }

    public static LoginDataModel getInstance(Context context){
        if(model == null) {
            model = new LoginDataModel(context);
        }
        return model;
    }

    /**
     * 验证手机是否已注册
     */
    public Observable<VerifyData> getIsRegistered(String mobile){
        return mApiService.getIsRegistered(mobile);
    }

    /**
     * 发送验证码  0-注册; 1-登陆; 2-修改密码
     */
    public Observable<UserData> getRegisterVerifyCode(String mobile){
        return mApiService.getVerificationCode(mobile,"0");
    }
    /**
     * 发送验证码  1-登陆
     */
    public Observable<UserData> getLoginVerifyCode(String mobile){
        return mApiService.getVerificationCode(mobile, "1");
    }
    /**
     * 发送验证码  2-修改密码
     */
    public Observable<UserData> getPwdUpdateVerifyCode(String mobile){
        return mApiService.getVerificationCode(mobile, "2");
    }


    /**
     * 注册
     */
    public Observable<UserData> postRegisterData( UserData.UserEntity user){
        return mApiService.postRegisterData(user);
    }

    /**
     * 注册
     */
    public Observable<LoginData> postRegisterData( String accountNo,
                                                   String password,
                                                   String name,
                                                   //int source,    // 30
                                                   String checkNum
                                                   //int memberType // 0 注册 资讯会员（改为-1：普通用户）； 交易会员需要通过升级
    ){
        return mApiService.postRegisterData(accountNo, password, name,30,checkNum);
    }




    /**
     * 用户名密码登录
     */
    public Observable<LoginData> postLoginData(String accountNo, String password){
        return mApiService.postLoginData(accountNo, password);
    }
    /**
     * 动态密码登录
     */
    public Observable<LoginData> postLoginByCodeData(String accountNo, String checkCode){
        return mApiService.postLoginByCodeData(accountNo, checkCode);
    }

    /**
     * 修改密码 -POST
     */
    public Observable<UserData> updatePwd(String accountNo, String password, String checkCode){
        return mApiService.updatePwd(accountNo, password, checkCode);
    }


    /**
     * 用户类型: 0 - 咨询会员; 1 - 交易会员; 2 - 商家会员; -1 - 用户
     * 会员认证 状态: 0 -未认证, 1 -已认证
     * 会员锁定 状态: 0 -正常, 1 -锁定, 2 -注销
     */
    public Observable<UserTypeData> getUserType(){
        return mApiService.getUserType(Constant.token);
    }


    /**
     * 用户状态
     * 1.交易用户认证状态 isAuthenticated
     */
    public Observable<UserStateData> getUserStatus(){
        return mApiService.getUserStatus(Constant.token);
    }

    /**
     * 退出登陆
     */
    public Observable<UserData> doLogout(String token){
        return mApiService.doLogout(token);
    }

    /**
     * 提交反馈
     */
    public Observable<VerifyData> postFeedback(String token, String feedType, String feedContent){
        return mApiService.postFeedback(token, feedType, feedContent);
    }

    /**
     * 获取企业信息
     */
    public Observable<EnterpriseInformation> getEnterpriseInfo(){
        return mApiService.getEnterpriseInfo();
    }

    /**
     * 完善信息 - 用户信息展示
     */
    public Observable<UserInformation> getUserInfo(String token){
        return mApiService.getUserInfo(token);
    }
    /**
     * 提交修改后的用户信息
     */
    public Observable<VerifyData> postUserData(String name,String email,String mobile,
                                               String provinceCode,String province,
                                               String cityCode,String city,
                                               String districtCode,String district, String address
    ){
        return mApiService.postUserData(Constant.token,name,email,mobile,
                provinceCode,province,
                cityCode,city,
                districtCode,district, address
        );
    }

    /**
     * 我的交易会员 - 会员信息展示
     */
    public Observable<UserInformation> getVipInfo(String token){
        return mApiService.getVipInfo(token);
    }

    /**
     * 消息中心消息
     */
    public Observable<NewsData> getMessageData(int pageNum, int pageSize){
        return mApiService.getMessageData(pageNum, pageSize);
    }

    /**
     * 我的收藏
     */
    public Observable<NewsData> getCollectionData( int pageNum, int pageSize){
        return mApiService.getCollectionData(Constant.token, pageNum, pageSize);
    }

    /**
     * 交易会员交易记录
     */
    public Observable<DealRecordData> getRecordData(String token, int pageNum, int pageSize){
        return mApiService.getRecordData(token, pageNum, pageSize);
    }

    /**
     * 商家会员交易记录
     */
    public Observable<DealRecordData> getShopRecordData(String token, int pageNum, int pageSize){
        return mApiService.getShopRecordData(token, pageNum, pageSize);
    }

    /**
     * 开票信息
     */
    public Observable<InvoiceData> getInvoiceData(String orderId){
        return mApiService.getInvoiceData(Constant.token, orderId);
    }
    /**
     * 提交申请
     */
    public Observable<InvoiceData> applyInvoiceData(String orderId, String amount){
        return mApiService.applyInvoiceData(Constant.token, orderId, amount);
    }


    /**
     * 交易会员 - 额度申请 - 法人三要素校验
     */
    public Observable<VerifyData> postLegalPersonInfo(String token, String userName, String phone, String idCard){
        return mApiService.postLegalPersonInfo(token, userName, phone, idCard );
    }

    /**
     * 交易会员 - 额度申请 - 上传营业执照
     */
    public Observable<LicenseData> postLicense(String token, String filePath){

        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestBody);

        return mApiService.postLicense(token, body )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 开通交易会员- 校验 社会信用代码
     */
    public Observable<VerifyData> getCheckTaxNumberData(String taxNumber){
        return mApiService.getCheckTaxNumberData(Constant.token, taxNumber);
    }
    /**
     * 开通交易会员- 校验 单位名称
     */
    public Observable<VerifyData> getCheckCompanyNameData(String memberName){
        return mApiService.getCheckCompanyNameData(Constant.token, memberName);
    }




    /**
     * 交易会员 - 额度申请 - 获取生产信息
     */
    public Observable<ProductData> getProductData(){
        return mApiService.getProductData();
    }

    /**
     * 交易会员 - 额度申请 - 提交数据
     */
    public Observable<VerifyData> postApplyData(ApplyDealVipData data){
        return mApiService.postApplyData(Constant.token, data);
    }
    public Observable<VerifyData> postApplyData(String imageUrl,
                                                String creditCode,
                                                String companyName,
                                                String legalPerson,
                                                String startTimeStr,
                                                String endTimeStr,
                                                String materials,
                                                String goodsTypes,
                                                String buyerAdminId){
        return mApiService.postApplyData(Constant.token, imageUrl, creditCode, companyName,legalPerson,
                startTimeStr, endTimeStr, materials, goodsTypes, buyerAdminId);
    }

    /**
     * 获取管理人员列表　departmentType=1: 营销管理事业部(营销顾问);  departmentType=2: 供应商管理事业部（商家管理）
     */
    public Observable<AdminData> getAdministrator(int departmentType){
        return mApiService.getAdministrator(departmentType);
    }
    /**
     * 获取管理人员列表　departmentType=1: 营销管理事业部(营销顾问);
     */
    public Observable<AdminData> getAdministrator(){
        return mApiService.getAdministrator(1);
    }

    /**
     * 交易会员 - 额度申请 - 获取额度
     */
    public Observable<QuotaData> getQuotaData(String token){
        return mApiService.getQuotaData(token);
    }

    /**
     * 申请退还保证金
     */
    public Observable<VerifyData> getDepositRefundData(String memberServeId){
        return mApiService.getDepositRefundData(Constant.token, memberServeId);
    }


}
