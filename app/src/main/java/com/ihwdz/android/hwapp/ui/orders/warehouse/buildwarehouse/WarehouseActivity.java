package com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.HwAppDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse.WarehouseContract.MODE_ADDRESS_UPDATE;

/**
 *  添加新仓库 - 0 & 添加收货地址 - 1 & 修改收货地址 - 2
 */
public class WarehouseActivity extends BaseActivity implements WarehouseContract.View, View.OnFocusChangeListener{

    static String TAG = "WarehouseActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.grey_line1) View greyLine1;
    @BindView(R.id.linear1) RelativeLayout warehouseLinear;        // 仓库名称
    @BindView(R.id.edit_name) EditText editWarehouseName;

    @BindView(R.id.tv_linkman) TextView tvLinkman;                 // 联系人； 收货人
    @BindView(R.id.edit_linkman) EditText editLinkman;             // 联系人； 收货人
    @BindView(R.id.edit_telephone) EditText editTelephone;         // 联系电话

    @BindView(R.id.linear_address) RelativeLayout linearAddress;   // 请选择省市区
    @BindView(R.id.tv_address) TextView tvAddress;                 // 省市区

    @BindView(R.id.edit_address_detail) EditText editAddressDetail;// 详细地址

    @BindView(R.id.grey_line6) View greyLine6;
    @BindView(R.id.linear6) RelativeLayout defaultAddressLinear;   // 设为默认 （收货地址）
    @BindView(R.id.checkbox) CheckBox defaultCheckbox;

    private String titleStr;
    private String existRemindStr;
    private int mode;

    static final String MODE = "build_mode";
    static final String ADDRESS_ID = "address_id";

    @Inject WarehouseContract.Presenter mPresenter;


    /**
     *
     * @param context
     * @param mode   添加新仓库 - 0 & 添加收货地址 - 1 & 修改收货地址 - 2
     * @param id     修改收货地址 - 2 id
     */
    public static void startWarehouseActivity(Context context, int mode, String id) {
        Log.i(TAG, "=================================== startWarehouseActivity ===================");
        Intent intent = new Intent(context, WarehouseActivity.class);
        intent.putExtra(MODE, mode);
        intent.putExtra(ADDRESS_ID, id);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_warehouse;
    }

    @Override
    public void initView() {

        if (getIntent() != null){
            LogUtils.printCloseableInfo(TAG, "getIntent() != null");
             mode = getIntent().getIntExtra(MODE,0);
             String id = getIntent().getStringExtra(ADDRESS_ID);
             mPresenter.setAddressId(id);
        }
        mPresenter.setCurrentMode(mode);

        initToolbar();
    }


    @Override
    public void initListener() {
        editWarehouseName.setOnFocusChangeListener(this);
        editLinkman.setOnFocusChangeListener(this);
        editTelephone.setOnFocusChangeListener(this);
        linearAddress.setOnFocusChangeListener(this);
        editAddressDetail.setOnFocusChangeListener(this);
    }

    @Override
    public void initData() {
        mPresenter.getLocalAddressData();
        if (mode == MODE_ADDRESS_UPDATE){  // 修改 收货地址
            mPresenter.getAddressData();
        }
        existRemindStr = getResources().getString(R.string.warehouse_exist);
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }


    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    // 添加仓库
    @Override
    public void initWarehouseView() {
        LogUtils.printCloseableInfo(TAG, "添加仓库");
        titleStr = getResources().getString(R.string.build_warehouse_title);

        warehouseLinear.setVisibility(View.VISIBLE);
        greyLine1.setVisibility(View.VISIBLE);

        tvLinkman.setText(getResources().getString(R.string.linkman));
        editLinkman.setHint(getResources().getString(R.string.linkman));

        defaultAddressLinear.setVisibility(View.GONE);
        greyLine6.setVisibility(View.GONE);

    }

    // 添加/修改收货地址
    @Override
    public void initAddressView(boolean isUpdate) {
        LogUtils.printCloseableInfo(TAG, "添加/修改 收货地址");
        if (isUpdate){
            titleStr = getResources().getString(R.string.update_address_title);
        }else {
            titleStr = getResources().getString(R.string.add_address_title);
        }

        warehouseLinear.setVisibility(View.GONE);
        greyLine1.setVisibility(View.GONE);

        tvLinkman.setText(getResources().getString(R.string.receiver));
        editLinkman.setHint(getResources().getString(R.string.name));

        defaultAddressLinear.setVisibility(View.VISIBLE);
        greyLine6.setVisibility(View.VISIBLE);

    }

    @Override
    public void updateReceiverInfo(PublishData.AddressEntity data) {
        editLinkman.setText(data.contactName);
        editTelephone.setText(data.mobile);

        String address = data.provinceName + " " + data.cityName +" "+data.districtName;

        tvAddress.setText(address);
        editAddressDetail.setText(data.address);

        defaultCheckbox.setChecked(data.isDefault == 1);
    }

    @Override
    public void updateAddress(String address) {
        tvAddress.setText(address);
    }

    // 提示 该仓库名已存在
    @Override
    public void showDialog(String warehouseName) {
        String remindStr = String.format(existRemindStr, warehouseName);

        // 弹框
        HwAppDialog dialog = new HwAppDialog
                .Builder(this)
                .setTitle(getResources().getString(R.string.title_dialog_deal))
                //.setInsideContentView(mPresenter.getBreedView())
                .setMessage(remindStr)
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //showPromptMessage("点击确认");
                        mPresenter.gotoQuoteActivity();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showPromptMessage("点击取消");
                        editWarehouseName.setFocusable(true);
                        editWarehouseName.requestFocus();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    @OnClick(R.id.linear_address)
    public void onAddressClicked(){
        mPresenter.selectAddress();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.edit_name:
                if (hasFocus){
                    LogUtils.printCloseableInfo(TAG, "edit_name: hasFocus:" + hasFocus);
                }else {
                    // 失去焦点时检测 该仓库是否存在
                    mPresenter.setWarehouseName(editWarehouseName.getText().toString().trim());
                    LogUtils.printCloseableInfo(TAG, "edit_name: hasFocus:" + hasFocus);
                }
                break;
//            case R.id.edit_linkman:
//                if (hasFocus){
//                }else {
//                    mPresenter.setLinkman(editLinkman.getText().toString().trim());
//                }
//                break;
//            case R.id.edit_telephone:
//                if (hasFocus){
//                }else {
//                    mPresenter.setTel(editTelephone.getText().toString().trim());
//                }
//                break;
//            case R.id.edit_address_detail:
//                if (hasFocus){
//                }else {
//                    mPresenter.setDetailAddress(editAddressDetail.getText().toString().trim());
//                }
//                break;
        }
    }

    @OnCheckedChanged(R.id.checkbox)
    public void onCheckedChanged(){
        mPresenter.setAsDefault(defaultCheckbox.isChecked());
    }

    // 点击 添加仓库 “提交”按钮 返回报价界面并更新
    @OnClick(R.id.bt)
    public void onSubmitBtClicked(){
        mPresenter.setLinkman(editLinkman.getText().toString().trim());
        mPresenter.setTel(editTelephone.getText().toString().trim());
        mPresenter.setDetailAddress(editAddressDetail.getText().toString().trim());

        mPresenter.doSubmit();
    }

}
