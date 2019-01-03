package com.ihwdz.android.hwapp.ui.me.infoupdate;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;


/**
 * 修改-保存数据 : 用户信息；报价-单价；
 */
public class InfoUpdateActivity extends BaseActivity implements InfoUpdateContract.View {

    String TAG = "InfoUpdateActivity";

    private String titleStr = "";
    private String contentStr = "";
    private boolean isHint = false;
    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_right) TextView saveBt;
    
    @BindView(R.id.edit) EditText editText;
    @Inject CompositeSubscription mSubscriptions;

    static final String TITLE_INDEX = "title";
    static final String CONTENT_INDEX = "content";
    static final String HINT_INDEX = "hint";   // 是否为提示文字
    static final String MODE_INDEX = "mode";   // 用户信息；报价-单价；

    String name,email,mobile, provinceCode,province, cityCode,city, districtCode,district, address;

    private int mode = 0;

    @Inject InfoUpdateContract.Presenter mPresenter;

    public static void startInfoUpdateActivity(Context context, int mode, String title, String content ) {
        Log.i("InfoUpdateActivity", "=================================== startInfoUpdateActivity ===================");
        Intent intent = new Intent(context, InfoUpdateActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(MODE_INDEX, mode);
        intent.putExtra(TITLE_INDEX, title);
        intent.putExtra(CONTENT_INDEX, content);
        context.startActivity(intent);
    }

    public static void startInfoUpdateActivity(Context context, int mode, String title, String content, boolean isHint ) {
        Log.i("InfoUpdateActivity", "=================================== startInfoUpdateActivity ===================");
        Intent intent = new Intent(context, InfoUpdateActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(MODE_INDEX, mode);
        intent.putExtra(TITLE_INDEX, title);
        intent.putExtra(CONTENT_INDEX, content);
        intent.putExtra(HINT_INDEX, isHint);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_info_update;
    }

    @Override
    public void initView() {
        initIntentData();  // 初始化 intent data (包含 title)
        initToolbar();
    }

    private void initIntentData() {

        if (getIntent()!= null){

            mode = getIntent().getIntExtra(MODE_INDEX, 0);
            mPresenter.setCurrentMode(mode);

            isHint = getIntent().getBooleanExtra(HINT_INDEX, true);
            if(getIntent().getStringExtra(TITLE_INDEX) != null){
                titleStr = getIntent().getStringExtra(TITLE_INDEX);
            }

            if (getIntent().getStringExtra(CONTENT_INDEX) != null){
                contentStr = getIntent().getStringExtra(CONTENT_INDEX);
                if (isHint){
                    editText.setHint(contentStr);
                }else {
                    editText.setText(contentStr);
                }
                mPresenter.setCurrentContent(contentStr);
            }

            if (mode == Constant.InfoUpdate.INFO_QUOTE_PRICE){
                // 报价 设置输入 数字
                controlInput();
            }

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
        onBackPressed();
    }
    public void initToolbar(){
        backBt.setVisibility(View.VISIBLE);
        saveBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }

    // 点击保存
    @OnClick(R.id.fl_title_menu_right)
    public void onSaveClicked(){
        hideKeyboard();
        String input = editText.getText().toString().trim();
        mPresenter.setCurrentContent(input);

        if (mode == Constant.InfoUpdate.INFO_USER){
            updateUserInfo();
        }

        mPresenter.doSave();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 个人中心 信息修改
    private void updateUserInfo(){
        // 保存更改信息
        if (titleStr.equals(getResources().getString(R.string.name))){
            name = editText.getText().toString().trim();
            Constant.name = editText.getText().toString();

        }else if (titleStr.equals(getResources().getString(R.string.email))){
            email = editText.getText().toString().trim();
            Constant.email = editText.getText().toString();

        }else if (titleStr.equals(getResources().getString(R.string.address_detail))){
            address = editText.getText().toString().trim();
            Constant.address = editText.getText().toString();
        }

    }


    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
    }

    // 控制输入 只能有两位小数
    private void controlInput(){
        //设置Input的类型两种都要
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);

        //设置字符过滤
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(".") && dest.toString().length() == 0){
                    return "0.";
                }
                if(dest.toString().contains(".")){
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if(length == 3){
                        return "";
                    }
                }
                return null;
            }
        }});
    }

}
