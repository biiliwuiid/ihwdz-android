package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.LicenseData;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :
 * version: 1.0
 * </pre>
 */
public interface ApplyProgressContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void showChecking();      // 信息校验中请稍后... hide
        void hideChecking();

        void showDialogAnim();    // 点击 上传 营业执照
        void showLicenseInfo(LicenseData.LicenseEntity data);
        void setEditable(boolean editable);  // 获取到的信息是否可编辑
        void updateView();
        void updateEstablishDate(String date);      // 更新成立日期
        void updateValidDate(String date);          // 更新有效期

        void showMsg(String msg);
        void showPromptMessage(String message);       //  提示信息
    }

    // 开通交易会员结果 : 0未申请； 1 申请中- 对应布局模型 no2； 2申请失败 - 对应布局模型 no3；3完善资料 - 对应布局模型 no1
    int STATE_RESULT_NO = 0;
    int STATE_RESULT_APPLYING = 1;
    int STATE_RESULT_FAIL = 2;
    int STATE_RESULT_IMPROVE = 3;

    // 额度申请状态  0未申请：（ 0: 法人信息; 0: 企业信息; 1: 生产信息; 2：管理员信息）
    //int STATE_LEGAL_PERSON = 0;
    int STATE_ENTERPRISE = 11;
    int STATE_PRODUCT = 12;
    int STATE_ADMIN = 13;

    int DATE_ESTABLISH = 0; // 成立日期
    int DATE_VALID = 1;     // 有效期

    interface Presenter extends BasePresenter {

        WaterfallAdapter getGoodsAdapter();
        WaterfallAdapter getMaterialAdapter();


        void getApplyStateData();         // 获取开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料

        void setApplyState(int state);    // 当前申请进度
        int getApplyState();

        void setApplyResult(int result);  // 开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料
        int getApplyResult();


//        void setLegalTel(String tel);
//        void setLegalIdCard(String idCard);
//        boolean isLegalInfoComplete();
//        void postLegalPersonInfo();                         // 上传法人信息

        void setLicensePath(String path);
        boolean isLicenseUpload();                          // 上传营业执照 完成
        void postLicense(String token, String filePath);    // 上传营业执照（企业信息）


        boolean checkCreditCode();              // 上传营业执照后调接口 校验 社会信用代码 -setCreditCode
        boolean checkCompanyName();             // 上传营业执照后调接口 校验 单位名称 -setCompanyName

        void setCreditCode(String code);     // 社会信用代码-必填-18位；
        void setCompanyName(String name);    // 单位名称-必填；
        void setLegalName(String name);      // 法人-必填；
        void setStartTime(String time);      // 成立日期
        void setEndTime(String time);        // 有效期
        boolean isEnterpriseInfoComplete();  // 企业信息是否完成

        void selectDate(int mode);           // 选择日期（成立日期/有效期）

        //boolean isTaxNumberChecked();
        //boolean isCompanyNameChecked();


        void getProductData();               // 获取 生产信息
        void setMaterialProductInfo(String selected);
        void setGoodsProductInfo(String selected);

        void setAdmin(String s);
        void setAdminId(String id);
        void setAcceptAssign(boolean accept);

        void goSelectAdmin();        // 选择管理人

        boolean isAdminComplete();   // 管理员信息 是否完成
        void setAdminData();         // 设置 管理员信息

        boolean getIsSubmitClicked();
        void postApplyData();        // 提交 申请信息

        //void getQuotaData(String token);                // 获取额度

    }
}
