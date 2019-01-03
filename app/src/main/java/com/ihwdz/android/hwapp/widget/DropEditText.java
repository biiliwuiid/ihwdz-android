package com.ihwdz.android.hwapp.widget;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.ui.publish.purchase.OnItemDropDownClickListener;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.tuacy.fuzzysearchlibrary.IFuzzySearchItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/25
 * desc :   带下拉选择框的 EditText
 *     popupWindow 在
 * version: 1.0
 * </pre>
 */
public class DropEditText extends FrameLayout implements View.OnClickListener, OnItemClickListener, View.OnFocusChangeListener{

    String TAG = "DropEditText";
    private EditText mEditText;         // 输入框
    private ImageView mDropImage;       // 右边的图片按钮

    private OnItemDropDownClickListener mDropDownClickListener;
    private OnItemDropDownClickListener.onPopupWindowShow mPopupListener;

    private PopupWindow mPopup;    // 点击图片弹出popupwindow
    private WrapListView mPopView; // popupwindow的布局

    private int mDrawableLeft;
    private int mDropMode;  // flow_parent or wrap_content
    private String mHit;
    private String currentInput;

    private PublishData.ProductEntity currentData;
//    private Object currentData;

    public DropEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.edit_layout, this);

        mPopView = (WrapListView) LayoutInflater.from(context).inflate(R.layout.pop_view, null);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DropEditText, defStyle, 0);
//        mDrawableLeft = ta.getResourceId(R.styleable.DropEditText_drawableRight, R.mipmap.ic_launcher);
        mDrawableLeft = ta.getResourceId(R.styleable.DropEditText_drawableRight, R.drawable.pull2);
        mDropMode = ta.getInt(R.styleable.DropEditText_dropMode, 0);
        mHit = ta.getString(R.styleable.DropEditText_hint);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mEditText = findViewById(R.id.dropview_edit);
        mDropImage = findViewById(R.id.dropview_image);

        //mEditText.setSelectAllOnFocus(true);
        mEditText.clearFocus();
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);

        mDropImage.setImageResource(mDrawableLeft);

        if(!TextUtils.isEmpty(mHit)) {
            mEditText.setHint(mHit);
        }

        mEditText.setOnClickListener(this);
        mDropImage.setOnClickListener(this);

        mPopView.setOnItemClickListener(this);

        mEditText.setOnFocusChangeListener(this);       // 监听焦点
        mEditText.addTextChangedListener(mTextWatcher); // 动态监听输入内容

    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 如果布局发生改变
        // 并且dropMode是flower_parent
        // 则设置ListView的宽度
        if(changed && 0 == mDropMode) {
            mPopView.setListWidth(getMeasuredWidth());
        }
    }

    /**
     * 设置Adapter
     * @param adapter ListView的Adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null){
            Toast.makeText(getContext(), "adapter == null", Toast.LENGTH_SHORT).show();
        }
        mPopView.setAdapter(adapter);
        mPopup = new PopupWindow(mPopView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blackText4)));
//            mPopup.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTransparent)));
        //mPopup.setFocusable(true);   // 让 PopupWindow 获取焦点

    }

    private boolean mIsSpec = false;
    private boolean mIsBreedSelected = false;

    public void setSpec(boolean isSpec){
        this.mIsSpec = isSpec;
    }
    public void setIsBreedSelected(boolean isBreedSelected){
        this.mIsBreedSelected = isBreedSelected;
    }

    /**
     * 获取输入框内的内容
     * @return String content
     */
    public String getText() {
        return mEditText.getText().toString().trim();
    }

    public void setText(String content){
        mEditText.setText(content);
    }

    public void setHint(String content){
        mEditText.setHint(content);
    }

    // 点击 EditText 或 image
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dropview_image || v.getId() == R.id.dropview_edit) {

            if (mDropDownClickListener != null ){
                mDropDownClickListener.onItemDropDownClicked(getText());

                // 如果是 Spec 只有在Breed 已选的情况下才 弹出 popupWindow
                if (!mIsSpec || mIsBreedSelected){
                    mEditText.setFocusableInTouchMode(true);
                    mEditText.setFocusable(true);
                    mEditText.requestFocus();

                    if (mPopup != null){
                        if( !mPopup.isShowing()) {
                            mPopup.showAsDropDown(this, 0, 5);
                        }
                        mPopupListener.onPopupWindowShow(mPopView); // 监测 PopupWindow
                    }else {
                        Toast.makeText(getContext(), "mPopup == null", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }
    }

   // mPopView OnItemClickListener  选择某项内容后 获取选择的值
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        currentData = (PublishData.ProductEntity) mPopView.getAdapter().getItem(position);
        mPopup.dismiss();   // 选择 PopView 的某项后 hide it.

        mEditText.setText(currentData.name);

        //mEditText.setSelection(currentData.name.length()); // 将光标移至文字末尾

        if (mDropDownClickListener != null){
            mDropDownClickListener.onItemSelected(getSelectObject());

            mEditText.clearFocus();
            mEditText.setFocusable(false);
            mEditText.setFocusableInTouchMode(false);
        }

    }

    public PublishData.ProductEntity getSelectObject(){
        return currentData;
    }


    public void addOnItemDropDownClickListener(OnItemDropDownClickListener listener){
        this.mDropDownClickListener = listener;
    }

    public void addPopupWindowListener(OnItemDropDownClickListener.onPopupWindowShow listener){
        this.mPopupListener = listener;
    }


    // 动态监测 输入内容
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
            currentInput = s.toString().trim();
            LogUtils.printCloseableInfo(TAG,"============= afterTextChanged  TextWatcher afterTextChanged: " + currentInput);
                if (mDropDownClickListener != null){

                    // 获取焦点后 获取当前输入文字改变时 更新关键字 搜索
                    // LogUtils.printCloseableInfo(TAG, "============ afterTextChanged getText(): " + getText());

                    if (!mIsSpec || mIsBreedSelected){
                        mDropDownClickListener.onTextChanged(currentInput);
                    }

                }
        }
    };

    // EditText onFocusChange
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == R.id.dropview_edit) {
            if (hasFocus){
//                LogUtils.printCloseableInfo(TAG, "EditText hasFocus: " + hasFocus);
//                if (mPopup != null){
//                    if( !mPopup.isShowing()) {
//                        mPopup.showAsDropDown(this, 0, 5);
//                        if (mDropDownClickListener != null){
//
//                            if (!mIsSpec || mIsBreedSelected){
//                                // 获取焦点后 获取当前输入文字 作为关键字 搜索
//                                LogUtils.printCloseableInfo(TAG, "onFocusChange getText(): " + getText());
//                                mDropDownClickListener.onTextChanged(getText());
//                            }
//                        }
//                    }
//                    mPopupListener.onPopupWindowShow(mPopView); // 监测 PopupWindow
//                }else {
//                    Toast.makeText(getContext(), "mPopup == null", Toast.LENGTH_SHORT).show();
//                }

            }else {
                LogUtils.printCloseableInfo(TAG, "EditText hasFocus: " + hasFocus);
                mPopup.dismiss();
            }

        }
    }



}
