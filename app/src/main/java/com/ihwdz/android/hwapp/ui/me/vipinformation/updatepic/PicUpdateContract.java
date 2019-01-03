package com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic;

import android.net.Uri;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.LicenseData;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/17
 * desc :  修改 上传 开票资料、营业执照
 * version: 1.0
 * </pre>
 */
public interface PicUpdateContract {

    interface View extends BaseView {
        void showPromptMessage(String string); //  提示信息
        void showLicenseInfo(LicenseData.LicenseEntity data);
    }

    interface Presenter extends BasePresenter {

        Uri getUri(String path);
        void setLicensePath(String path);
        boolean isLicenseUpload();
        void postLicense(String token, String filePath);    // 上传营业执照（企业信息）
    }
}
