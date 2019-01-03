package com.ihwdz.android.hwapp.ui.me.dealvip.deposit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.widget.HwAppDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositContract.MODE_DEPOSIT;
import static com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositContract.MODE_DEPOSIT_REFUND;

/**
 * 保证金
 */
public class DepositActivity extends BaseActivity implements DepositContract.View {

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    // 展示保证金
    @BindView(R.id.tv_deposit) TextView tvDeposit;
    @BindView(R.id.linear_deposit) LinearLayout linearDeposit;

    // 保证金退款成功
    @BindView(R.id.tv_refund) TextView tvRefund;
    @BindView(R.id.linear_deposit_refund) LinearLayout linearDepositRefund;
    @BindView(R.id.bt) Button bt;


    static final String DEPOSIT_ID = "deposit_id";
    static final String DEPOSIT = "deposit";
    static final String DEPOSIT_MODE = "deposit_mode";

    static String TAG = "DepositActivity";

    String currency;
    @Inject DepositContract.Presenter mPresenter;


    /**
     *
     * @param context
     * @param deposit
     * @param mode  0: 保证金正常 1:退款成功？
     */
    public static void startDepositActivity(Context context, String deposit,String id, int mode) {
        Log.i(TAG, "=================================== startDepositActivity ===================");
        Intent intent = new Intent(context, DepositActivity.class);
        intent.putExtra(DEPOSIT, deposit);
        intent.putExtra(DEPOSIT_ID, id);
        intent.putExtra(DEPOSIT_MODE, mode);
        context.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_deposit;
    }

    @Override
    public void initView() {
        currency = getResources().getString(R.string.currency_sign);
        initToolbar();
        initIntent();
    }

    private void initIntent() {
        if (getIntent()!= null){
            if (getIntent().getStringExtra(DEPOSIT) != null){
                String deposit = getIntent().getStringExtra(DEPOSIT);
                tvDeposit.setText(currency + deposit);
                tvRefund.setText(currency + deposit);
            }
            if (getIntent().getStringExtra(DEPOSIT_ID) != null){
                String id = getIntent().getStringExtra(DEPOSIT_ID);
                mPresenter.setCurrentId(id);
            }

            int mode = getIntent().getIntExtra(DEPOSIT_MODE, 0);
            mPresenter.setCurrentMode(mode);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        int currentMode = mPresenter.getCurrentMode();
        switch (currentMode){
            case MODE_DEPOSIT:
                onBackPressed();
                break;

            case MODE_DEPOSIT_REFUND:
                // 返回 更新 个人中心
                mPresenter.doComplete();
                break;
            default:
                onBackPressed();
                break;

        }

    }
    private void initToolbar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.deposit_title));
    }

    @OnClick(R.id.bt)
    public void onSubmitClicked(){
        mPresenter.doSubmitClick();
    }

    // 展示保证金
    @Override
    public void initDepositView() {
        linearDeposit.setVisibility(View.VISIBLE);
        linearDepositRefund.setVisibility(View.GONE);
        bt.setText(getResources().getString(R.string.deposit_refund));
    }

    // 保证金退款成功
    @Override
    public void initRefundSuccessView() {
        linearDeposit.setVisibility(View.GONE);
        linearDepositRefund.setVisibility(View.VISIBLE);
        bt.setText(getResources().getString(R.string.complete));
    }

    @Override
    public void showDialog() {
        // 弹框
        HwAppDialog dialog = new HwAppDialog
                .Builder(this)
                //.setTitle("用户状态异常")
                .setInsideContentView(mPresenter.getDialogContentView())
                .setPositiveButton(getResources().getString(R.string.deposit_refund_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // mPresenter.doCancel();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.doConfirm();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
