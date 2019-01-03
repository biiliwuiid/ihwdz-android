package com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LicenseData;
import com.ihwdz.android.hwapp.utils.LQRPhotoSelectUtils;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 修改开票资料  修改营业执照
 */
public class PicUpdateActivity extends BaseActivity implements PicUpdateContract.View{

    String TAG = "PicUpdateActivity";
    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.imageView) ImageView ivBusinessLicense;
    @BindView(R.id.button_linear) LinearLayout buttonLinear;

    private String titleStr = "";
    private String filePath = "";
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    @Inject PicUpdateContract.Presenter mPresenter;


    @Override
    public int getContentView() {
        return R.layout.activity_pic_update;
    }

    @Override
    public void initView() {
        initToolBar();
        init();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        if (getIntent() != null){
            titleStr = getIntent().getStringExtra("TITLE");
            filePath = getIntent().getStringExtra("PATH");
            title.setText(titleStr);
            ImageUtils.loadImgByPicasso(this, filePath, ivBusinessLicense);
//            mPresenter.getUri(filePath);
//            Glide.with(PicUpdateActivity.this).load(mPresenter.getUri(filePath)).into(ivBusinessLicense);
        }
    }


    @OnClick({R.id.tv_pick_photo, R.id.tv_take_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pick_photo:  // 从相册选一张
                // 3、调用从图库选取图片方法
                PermissionGen.needPermission(PicUpdateActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );

                break;
            case R.id.tv_take_photo:  // 拍一张照片
                // 3、调用拍照方法
                PermissionGen.with(PicUpdateActivity.this)
                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).request();
                break;
        }
    }

    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                String filePath = outputFile.getAbsolutePath();
                Log.d(TAG, "当拍照或从图库选取图片成功后回调: getAbsolutePath: "+ outputFile.getAbsolutePath());
                Log.d(TAG, "当拍照或从图库选取图片成功后回调: outputUri.toString(): "+ outputUri.toString());
                Glide.with(PicUpdateActivity.this).load(outputUri).into(ivBusinessLicense);
                mPresenter.setLicensePath(filePath);
                mPresenter.postLicense(Constant.token, filePath);
            }
        }, false);//true裁剪，false不裁剪

        //        mLqrPhotoSelectUtils.setAuthorities("com.lqr.lqrnativepicselect.fileprovider");
        //        mLqrPhotoSelectUtils.setImgPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    public void showDialog() {
        //创建对话框创建器
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + PicUpdateActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLicenseInfo(LicenseData.LicenseEntity data) {

    }
}
