package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.ihwdz.android.hwapp.widget.CleanableEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchContract.View{

    String TAG = "SearchActivity";

//    @BindView(R.id.iv_search) ImageView ivSearch;
//    @BindView(R.id.editText) EditText editText;
//    @BindView(R.id.iv_delete) ImageView ivDelete;

    @BindView(R.id.view_for_focus) View mViewHolder;
    @BindView(R.id.editText) CleanableEditText mEditSearchInput;
    @BindView(R.id.tv_cancel) TextView mTextCancel;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindView(R.id.layout_fuzzy_search) LinearLayout mLayoutFuzzySearch;
    @BindView(R.id.recycler_fuzzy_search) RecyclerView mRecyclerSearch;

    private Context mContext;
    @Inject SearchContract.Presenter mPresenter;


    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {

        mContext = this;
        initRecyclerView();
        initFuzzyRecyclerView();
    }

    @Override
    public void initListener() {
        // delete button in edit box
//        mTextCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditSearchInput.setText("");
//                hideKeyboard(mContext, mEditSearchInput);
//                mViewHolder.requestFocus();
//            }
//        });

        mEditSearchInput.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLayoutFuzzySearch.setVisibility(View.VISIBLE);
                } else {
                    mLayoutFuzzySearch.setVisibility(View.GONE);
                }
            }
        });

        // ClearEditText (custom EditText)  addTextChangedListener
        mEditSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "=========== beforeTextChanged ==========" + s + " start: " + start +" count: " + count+" after: " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "=========== onTextChanged ==========" + s + " before: " + before +" count: " + count);
                mPresenter.getFuzzySearchAdapter().getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "=========== afterTextChanged ==========" + s );
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getAdminData();
    }

    private void initRecyclerView() {
        Log.d(TAG, "========== initRecyclerView ===========");
        //recyclerView.setHasFixedSize(true);
        //RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mPresenter.getAdapter());

        mPresenter.getAdapter().addItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(AdminData.AdminEntity data) {
                //Toast.makeText(getBaseContext(), "onItemClicked" + data.name +" id: "+data.id, Toast.LENGTH_SHORT).show();
                Constant.adminId = data.id;
                Constant.adminName = data.name;
                onBackPressed();
            }
        });

    }

    private void initFuzzyRecyclerView(){
        Log.d(TAG, "========== initFuzzyRecyclerView ===========");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerSearch.setLayoutManager(layoutManager);
        mRecyclerSearch.setAdapter(mPresenter.getFuzzySearchAdapter());
        mPresenter.getFuzzySearchAdapter().addItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(AdminData.AdminEntity data) {
                //Toast.makeText(getBaseContext(), "onItemClicked" + data.name +" id: "+data.id, Toast.LENGTH_SHORT).show();
                Constant.adminId = data.id;
                Constant.adminName = data.name;
                onBackPressed();
            }
        });
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (im != null) {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



//    @OnClick(R.id.iv_search)
//    public void onSearchClicked(){
//        Toast.makeText(this, "Search：" + editText.getText().toString(), Toast.LENGTH_SHORT).show();
//    }

//    // 清空输入框
//    @OnClick(R.id.iv_delete)
//    public void onDeleteClicked(){
//        editText.setText("");
//    }

    @OnClick(R.id.tv_cancel)
    public void onCancelClicked(){
        //Toast.makeText(this, "onCancelClicked", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

    }
}
