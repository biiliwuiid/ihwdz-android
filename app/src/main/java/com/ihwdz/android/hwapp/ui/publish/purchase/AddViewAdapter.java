package com.ihwdz.android.hwapp.ui.publish.purchase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.DropEditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/26
 * desc :   发布求购 - 产品明细 条目
 *
 *          输入内容变化时 更新加载数据
 *          选中时赋值
 * version: 1.0
 * </pre>
 */
public class AddViewAdapter extends RecyclerView.Adapter {

    private String TAG = "Purchase_AddViewAdapter";
    private Context mContext;
    private List<PublishData.PublishPurchaseData> mData;


    private PublishData.PublishPurchaseData currentData;
    private PublishData.ProductEntity currentBreed;
    private PublishData.ProductEntity currentSpec;
    private PublishData.ProductEntity currentFactory;
    private String  currentQty;

    private PublishData.PublishPurchaseData selectedData;
    private PublishData.ProductEntity selectedBreed;
    private PublishData.ProductEntity selectedSpec;
    private PublishData.ProductEntity selectedFactory;
    private String  selectedQty;


    private OnFocusChangeListener.onPopupWindowShow mPopupListener;
    private OnFocusChangeListener.onBreedFocusChanged mBreedListener;
    private OnFocusChangeListener.onSpecFocusChanged mSpecListener;
    private OnFocusChangeListener.onFactoryFocusChanged mFactoryListener;


    private BreedInfoAdapter mBreedAdapter;
    private BreedInfoAdapter mSpecAdapter;
    private BreedInfoAdapter mFactoryAdapter;

    private boolean isClearFocus = false;
    private boolean isDeleteClicked = false;  // 点击 delete 后 Selected_qty 不再动态监听，保持上一份数据

    @Inject
    public AddViewAdapter(Context context){
        this.mContext = context;
        addData();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_purchase_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final PublishData.PublishPurchaseData model = mData.get(position);

        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (isClearFocus){
                viewHolder.editAmount.clearFocus();
                viewHolder.editAmount.setFocusable(false);
                viewHolder.editAmount.setFocusableInTouchMode(false);
            }

            /**
             * DELETE item
             */
            if (getItemCount() == 1){
                viewHolder.tvDelete.setVisibility(View.GONE);
            }
            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // LogUtils.printCloseableInfo(TAG, "position: " + position);
                    if (getItemCount() >1){
                        deleteData(model);
                        //mDeleteListener.onDeleteClicked(model);
                    }else {
                        // 至少一个不可删除
                        // LogUtils.printCloseableInfo(TAG, "至少一个不可删除 position : " + position);
                    }

                }
            });

            //
            viewHolder.editBreed.setAdapter(mBreedAdapter);

            viewHolder.editSpec.setAdapter(mSpecAdapter);
            viewHolder.editSpec.setSpec(true); // 当前为Spec

            viewHolder.editFactory.setAdapter(mFactoryAdapter);


            //
            viewHolder.editBreed.setText(model.breed);
            viewHolder.editSpec.setText(model.spec);
            viewHolder.editFactory.setText(model.factory);
            viewHolder.editAmount.setText(model.qty);


            ////////  监听 点击 DropDown 获取 breed spec brand  数据

            /**
             * breed
             */
            viewHolder.editBreed.addPopupWindowListener(new OnItemDropDownClickListener.onPopupWindowShow() {
                @Override
                public void onPopupWindowShow(View view) {
                    mPopupListener.onPopupWindowShow(view);
                }
            });
            viewHolder.editBreed.addOnItemDropDownClickListener(new OnItemDropDownClickListener() {
                @Override
                public void onItemDropDownClicked(String s) {
                    mBreedListener.onTextChanged(s);
                }

                @Override
                public void onTextChanged(String s) {
                    mBreedListener.onTextChanged(s);
//                    if (s != null){
//                        // 输入内容变化 加载数据
//                        mBreedListener.onTextChanged(s.trim());
//                    }
                }

                @Override
                public void onItemSelected(PublishData.ProductEntity selectedItem) {
                    hideKeyboard();

                    currentBreed = selectedItem;
                    selectedBreed = selectedItem;

                    currentData.breed = selectedItem.name;
                    currentData.breedCode = selectedItem.code;
                    currentData.breedAlias = selectedItem.pySName;

                    viewHolder.editSpec.setIsBreedSelected(true);  // 当前已选择breed

                    selectedData.breed = selectedItem.name;
                    selectedData.breedCode = selectedItem.code;
                    selectedData.breedAlias = selectedItem.pySName;

                }
            });


            /**
             * spec
             */
            viewHolder.editSpec.addPopupWindowListener(new OnItemDropDownClickListener.onPopupWindowShow() {
                @Override
                public void onPopupWindowShow(View view) {
                    mPopupListener.onPopupWindowShow(view);
                }
            });
            viewHolder.editSpec.addOnItemDropDownClickListener(new OnItemDropDownClickListener() {
                @Override
                public void onItemDropDownClicked(String s) {
                    if (selectedBreed != null && selectedBreed.name.length() > 0){
                        mSpecListener.onTextChanged(s);
                    }else {
                        Toast.makeText(mContext, "请先选择原料品名",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onTextChanged(String s) {
                    if (selectedBreed != null && selectedBreed.name.length()>0){
                        mSpecListener.onTextChanged(s);
//                        if ( s!= null){
//                            mSpecListener.onTextChanged(s.trim());
//                        }
                    }else {
                        Toast.makeText(mContext, "请先选择原料品名",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onItemSelected(PublishData.ProductEntity selectedItem) {

                    hideKeyboard();

                    currentSpec = selectedItem;
                    selectedSpec = selectedItem;

                    currentData.spec = selectedItem.name;
                    selectedData.spec = selectedItem.name;
                }
            });

            /**
             * factory
             */
            viewHolder.editFactory.addPopupWindowListener(new OnItemDropDownClickListener.onPopupWindowShow() {
                @Override
                public void onPopupWindowShow(View view) {
                    mPopupListener.onPopupWindowShow(view);
                }
            });
            viewHolder.editFactory.addOnItemDropDownClickListener(new OnItemDropDownClickListener() {
                @Override
                public void onItemDropDownClicked(String s) {
                    mFactoryListener.onTextChanged(s);
                }

                @Override
                public void onTextChanged(String s) {
                    mFactoryListener.onTextChanged(s);
//                    if (s != null){
//                        mFactoryListener.onTextChanged(s.trim());
//                    }
                }

                @Override
                public void onItemSelected(PublishData.ProductEntity selectedItem) {

                    hideKeyboard();

                    currentFactory = selectedItem;
                    selectedFactory = selectedItem;

                    currentData.factory = selectedItem.name;
                    selectedData.factory = selectedItem.name;
                }
            });

            /**
             * QTY
             */
            controlInput(viewHolder.editAmount);
            viewHolder.editAmount.addTextChangedListener(mTextWatcher); // 动态监听输入内容
            viewHolder.editAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClearFocus = false;
                    viewHolder.editAmount.setFocusableInTouchMode(true);
                    viewHolder.editAmount.setFocusable(true);
                    viewHolder.editAmount.requestFocus();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_delete) TextView tvDelete;

        @BindView(R.id.edit_breed) DropEditText editBreed;      // 原料品名
        @BindView(R.id.edit_spec) DropEditText editSpec;        // 原料牌号
        @BindView(R.id.edit_factory) DropEditText editFactory;  // 生产厂家
        @BindView(R.id.edit_amount) EditText editAmount;        // 数量

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            editAmount.clearFocus();
            editAmount.setFocusable(false);
            editAmount.setFocusableInTouchMode(false);
        }
    }


    // PopupWindow 弹出 监听
    public void setOnPopupWindowListener(OnFocusChangeListener.onPopupWindowShow listener){
        this.mPopupListener = listener;
    }

    // breed 输入监听
    public void setOnBreedChangeListener(OnFocusChangeListener.onBreedFocusChanged listener){
        this.mBreedListener = listener;
    }
    // SPEC 输入监听
    public void setOnSpecChangeListener(OnFocusChangeListener.onSpecFocusChanged listener){
        this.mSpecListener = listener;
    }
    // FACTORY 输入监听
    public void setOnFactoryChangeListener(OnFocusChangeListener.onFactoryFocusChanged listener){
        this.mFactoryListener = listener;
    }


    public void setBreedAdapter(BreedInfoAdapter adapter){
        mBreedAdapter = adapter;
    }
    public void setSpecAdapter(BreedInfoAdapter adapter){
        mSpecAdapter = adapter;
    }
    public void setFactoryAdapter(BreedInfoAdapter adapter){
        mFactoryAdapter = adapter;
    }

    // 获取当前产品明细列表
    public List<PublishData.PublishPurchaseData> getDataList(){
        return mData;
    }

    // 删除一项(最后一项不能删除)
    public void deleteData(PublishData.PublishPurchaseData data){
        isDeleteClicked = true;
        isClearFocus = true;
        if (mData != null && mData.size() > 1){
            if (mData.contains(data)){
                mData.remove(data);
                notifyDataSetChanged();
            }
        }
    }

    // 增加一项
    public void addData(){
        isClearFocus = true;
        isDeleteClicked = false;
        notifyDataSetChanged();

        boolean isFirstAdd = false;
        if (mData == null){
            mData = new ArrayList<>();
            isFirstAdd = true;
        }
        if (isFirstAdd){
            currentData = new PublishData.PublishPurchaseData();  // 当前数据
            mData.add(currentData);
            selectedData = new PublishData.PublishPurchaseData(); // 存储已选数据
        }else {

            if (isCurrentItemCompleted()){
                currentData = new PublishData.PublishPurchaseData();   // 当前数据
                mData.add(currentData);
                selectedData = new PublishData.PublishPurchaseData();  // 已选数据
            }else {
                //Toast.makeText(mContext, "请您填完尚未填写的内容",Toast.LENGTH_SHORT).show();
            }
        }

        notifyDataSetChanged();
    }

    // 判断当前 产品明细是否填写完整 否则不可以增加 新 item  （判断当前最新已填明细 是否完整）
    public boolean isCurrentItemCompleted(){

        selectedData = mData.get(getItemCount()-1);
        boolean isCompleted = true;
        if (TextUtils.isEmpty(selectedData.breed)){
            Toast.makeText(mContext, "请您填完尚未填写的内容",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(selectedData.spec)){
            Toast.makeText(mContext, "请您填完尚未填写的内容",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(selectedData.factory)){
            Toast.makeText(mContext, "请您填完尚未填写的内容",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(selectedData.qty)){
            Toast.makeText(mContext, "请您填完尚未填写的内容",Toast.LENGTH_SHORT).show();
            return false;
        }
        return isCompleted;
    }

    public String getCurrentBreedCode(){
//        return selectedData.breedCode;
        return currentData.breedCode;
    }


    // 动态监测 Qty 输入内容
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //LogUtils.printCloseableInfo(TAG,"=============  TextWatcher beforeTextChanged: " + s);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //LogUtils.printCloseableInfo(TAG,"=============  TextWatcher onTextChanged: " + s);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // LogUtils.printCloseableInfo(TAG,"============= afterTextChanged  currentQty: " + s);
            if (isClearFocus){
                //currentData.qty = null;
                // selectedData.qty = currentQty;
            }else {
                currentQty = s.toString().trim();
                currentData.qty = currentQty;
                // LogUtils.printCloseableInfo(TAG, "*********** afterTextChanged editAmount - currentQty: "+ currentQty);
                if (!isDeleteClicked){
                    // 没点击删除
                    selectedQty = currentQty;
                    selectedData.qty = currentQty;
                }

            }

        }
    };


    // 控制输入 只能有4位小数
    private void controlInput(EditText editText){
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
                    if(length == 5){
                        return "";
                    }
                }
                return null;
            }
        }});
    }


    public void hideKeyboard() {
//        LogUtils.printCloseableInfo(TAG, "hideKeyboard");
//        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  // 得到InputMethodManager的实例
//        if (imm != null && imm.isActive()){
//            LogUtils.printCloseableInfo(TAG, "hideKeyboard ========== imm != null && imm.isActive()");
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }



}
