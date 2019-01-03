package com.ihwdz.android.hwapp.ui.publish.purchase;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.HwAppDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * publish purchase
 */

public class PurchaseActivity extends BaseActivity implements PurchaseContract.View {

    String TAG = "Publish_PurchaseActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    // 收货信息
    @BindView(R.id.linear_receiver) LinearLayout receiverLinear;

    @BindView(R.id.linear_mobile_name) LinearLayout mobileLinear;
    @BindView(R.id.tv_order_receiver) TextView tvOrderReceiver;
    @BindView(R.id.tv_tel) TextView tvTel;
    @BindView(R.id.tv_address) TextView tvAddress;

    @BindView(R.id.contentView) FrameLayout mFrameLayout;
    @BindView(R.id.scroll_view) ScrollView mScrollView;
    @BindView(R.id.linear_content) LinearLayout contentLinear;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView; // 求购单 产品明细

    @BindView(R.id.linear_add) LinearLayout linearAdd;       // 增加明细

    @BindView(R.id.bt) Button publishButton;

    private String receiverStr, mobileStr, addressStr;

    private Drawable disableDrawable;  // 按钮不可用
    private Drawable enableDrawable;   // 按钮可用

    @Inject PurchaseContract.Presenter mPresenter;



    @Override
    public int getContentView() {
        // Constant.token="dd743f25bc9a40eab197e60842b053b7_18521548759";  for test
        return R.layout.activity_purchase;
    }

    @Override
    public void initView() {
        initStr();
        initToolBar();
        initRecyclerView();
        // topToKeyboardLayout(mFrameLayout, contentLinear);  // 布局在 软键盘之上
        topToKeyboardLayout(contentLinear, linearAdd);  // 布局在 软键盘之上
    }


    private void initStr() {
        disableDrawable = getResources().getDrawable(R.drawable.bt_disable_oval_bg);
        enableDrawable = getResources().getDrawable(R.drawable.gradient_orange_background);

        receiverStr = getResources().getString(R.string.order_receiver);
        addressStr = getResources().getString(R.string.order_address);
    }


    @Override
    public void initListener() {}

    @Override
    public void initData() {
        // LogUtils.printCloseableInfo(TAG, "=========== initData ============");
        mPresenter.getAddressData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // LogUtils.printCloseableInfo(TAG, "=========== PurchaseActivity onStart ============");
        if (Constant.addressSelected_province != null){
            mPresenter.setPostAddress();
        }else {
            mPresenter.getAddressData(); // 新增地址后返回 更新数据
        }
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.purchase_publish_title));
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // item edit box adapter
        mPresenter.getAdapter().setBreedAdapter(mPresenter.getBreedAdapter());
        mPresenter.getAdapter().setSpecAdapter(mPresenter.getSpecAdapter());
        mPresenter.getAdapter().setFactoryAdapter(mPresenter.getFactoryAdapter());

        /**
         *  PopupWindow 弹出
         */
        mPresenter.getAdapter().setOnPopupWindowListener(new OnFocusChangeListener.onPopupWindowShow() {
            @Override
            public void onPopupWindowShow(View view) {
                LogUtils.printCloseableInfo(TAG, "=========== onPopupWindowShow");
                // topToKeyboardLayout(mFrameLayout, view);  // 布局在 软键盘之上
                topToKeyboardLayout(contentLinear, linearAdd);  // "增加明细"布局在 软键盘之上
            }
        });


        /**
         * breed
         */
        mPresenter.getAdapter().setOnBreedChangeListener(new OnFocusChangeListener.onBreedFocusChanged() {
            @Override
            public void onTextChanged(String s) {
                // LogUtils.printCloseableInfo(TAG, "onTextChanged - BREED");
                mPresenter.setKeyword(s);
                mPresenter.getBreedData();
            }
        });

        /**
         * spec
         */
        mPresenter.getAdapter().setOnSpecChangeListener(new OnFocusChangeListener.onSpecFocusChanged() {
            @Override
            public void onTextChanged(String s) {
                // LogUtils.printCloseableInfo(TAG, "onTextChanged - SPEC");
                mPresenter.setKeyword(s);
                mPresenter.getSpecData();
            }
        });

        /**
         * factory
         */
        mPresenter.getAdapter().setOnFactoryChangeListener(new OnFocusChangeListener.onFactoryFocusChanged() {
            @Override
            public void onTextChanged(String s) {
                // LogUtils.printCloseableInfo(TAG, "onTextChanged - FACTORY");
                mPresenter.setKeyword(s);
                mPresenter.getFactoryData();
            }
        });

        mRecyclerView.setAdapter(mPresenter.getAdapter());
    }

    /**
     * 布局在 软键盘之上
     */
    private void topToKeyboardLayout(final View root, final View button) {
        // 监听根布局的视图变化
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取内容布局在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);

                        // 获取内容布局在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = root.getHeight() - rect.bottom;
//                        LogUtils.printCloseableInfo(TAG, " 布局-软键盘 root.getHeight(): " + root.getHeight());
//                        LogUtils.printCloseableInfo(TAG, " 布局-软键盘 rect.bottom: " + rect.bottom);
//                        LogUtils.printCloseableInfo(TAG, " 布局-软键盘 rootInvisibleHeight: " + rootInvisibleHeight);

                        // 若不可视区域高度大于100，则键盘显示
//                        if (rootInvisibleHeight > 100) {
                        if (rootInvisibleHeight > 100) {
                            int[] location = new int[2];

                            // 获取须顶上去的控件在窗体的坐标
                            button.getLocationInWindow(location);

                            // 计算内容滚动高度，使button在可见区域
                            LogUtils.printCloseableInfo(TAG, " 布局-软键盘 rootInvisibleHeight: " + rootInvisibleHeight + "   button.getHeight(): " + button.getHeight() + "    rect.bottom: " + rect.bottom);
                            int buttonHeight = (location[1] + button.getHeight()) - rect.bottom;

                            root.scrollTo(0, buttonHeight);
                        } else {
                            // 键盘隐藏
                             root.scrollTo(0, 0);
                        }
                    }
                });
    }


    public void hideKeyboard() {
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  // 得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void updateAddress(PublishData.ReceiverInfo data) {
        if (data != null && data.districtCode != null && data.mobile!= null){
            mobileLinear.setVisibility(View.VISIBLE);
            String address = data.province + " "+ data.city + " "+ data.district +" "+data.address;
            tvOrderReceiver.setText(String.format(receiverStr, data.contact));
            tvAddress.setText(String.format(addressStr, address));
            tvTel.setText(data.mobile);
        }else {
            mobileLinear.setVisibility(View.GONE);
            tvAddress.setText(getResources().getString(R.string.empty_address));
        }

    }


    @Override
    public void showRemindDialog(String message) {
        // 弹框
        HwAppDialog dialog = new HwAppDialog
                .Builder(this)
                //.setTitle("用户状态异常")
                .setInsideContentView(mPresenter.getDialogContentView(message))
                .setPositiveButton(getResources().getString(R.string.contract_service), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击 联系客服
                        mPresenter.doContract();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击 取消
                    }
                })
                .create();
        dialog.show();
    }


    // 点击增加 求购明细
    @OnClick(R.id.linear_add)
    public void onAddClicked(){
        mPresenter.addItem();
//        topToKeyboardLayout(mFrameLayout, linearAdd);  // 布局在 软键盘之上
        topToKeyboardLayout(contentLinear, linearAdd);  // 布局在 软键盘之上
    }

    // 选择收货地址
    @OnClick(R.id.linear_receiver)
    public void onAddressClicked(){
        mPresenter.gotoSelectAddress();
    }

    @OnClick(R.id.bt)
    public void onSubmitClicked(){
        // 求购信息填写完整 且 未提交
        if (mPresenter.checkCurrentDataComplete() && !mPresenter.getIsSubmitClicked()){
            mPresenter.doSubmit();
        }
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void setPublishButtonClickable(boolean isClickable) {
//        publishButton.setClickable(isClickable);
//    }

    @Override
    public void makeButtonDisable(boolean disable) {
        if (disable){
            publishButton.setBackground(disableDrawable);// 不可用
        }else {
            publishButton.setBackground(enableDrawable); // 可用
        }
        publishButton.setClickable(!disable);
    }


}
