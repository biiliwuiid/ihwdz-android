package com.ihwdz.android.hwapp.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;

import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/11
 * desc :
 * version: 1.0
 * </pre>
 */
public class CustomDialog extends Dialog implements View.OnClickListener{

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    private int mTheme;

    static String TAG = "CustomDialog";


    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTheme = themeResId;
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.app_dialog);
        mContext = context;
    }

    public CustomDialog(Context context, String title, String confirmButtonText, String cancelButtonText) {
        super(context, R.style.AppDialog_Light);
        this.mContext = context;
        this.title = title;
//        this.confirmButtonText = confirmButtonText;
//        this.cancelButtonText = cancelButtonText;
    }

    public CustomDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }


    public CustomDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CustomDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    protected static class AppDialogParameters {
        Context mContext;
        String mTitle;
        String mMessage;
        Boolean mEnableDefaultButton;
        String mPositiveButtonText;
        OnClickListener mPositiveButtonClickListener;
        String mNegativeButtonText;
        OnClickListener mNegativeButtonClickListener;
        View mInsideContentView;
        OnDismissListener mOnDismissListener;
        AppDialogParameters()
        {
            mEnableDefaultButton = false;
        }
    }

    static int resolveDialogTheme(Context context, int resId) {
        if (resId >= 0x01000000) {   // start of real resource IDs.
            return resId;
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.AppDialogStyle, outValue, true);
            return outValue.resourceId;
        }
    }

    AppDialogParameters parameters;
    public static class Builder{
        protected AppDialogParameters parameters;
        protected int mTheme;

        public Builder(Context context) {
            this(context, resolveDialogTheme(context, 0));
        }

        public Builder(Context context, int theme) {
            parameters = new AppDialogParameters();
            parameters.mContext = context;
            mTheme = theme;
            Log.d(TAG, "dialog Builder()");
        }
        private Context getContext() {
            return parameters.mContext;
        }

        public Builder setTitle(String title) {
            parameters.mTitle = title;
            return this;
        }

        public Builder setTitle(int titleID) {
            String title = getContext().getString(titleID);
            return setTitle(title);
        }

        public Builder setMessage(String message) {
            parameters.mMessage = message;
            return this;
        }

        public Builder setMessage(int messageID) {
            String message = getContext().getString(messageID);
            return setMessage(message);
        }
        public Builder setPositiveButton(String buttonText, OnClickListener onClickListener) {
            parameters.mPositiveButtonText = buttonText;
            parameters.mPositiveButtonClickListener = onClickListener;
            return this;
        }
        public Builder setPositiveButton(int buttonTextID, OnClickListener onClickListener) {
            String buttonText = getContext().getString(buttonTextID);
            return setPositiveButton(buttonText, onClickListener);
        }

        public Builder setNegativeButton(String buttonText, OnClickListener onClickListener) {
            parameters.mNegativeButtonText = buttonText;
            parameters.mNegativeButtonClickListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(int buttonTextID, OnClickListener onClickListener) {
            String buttonText = getContext().getString(buttonTextID);
            return setNegativeButton(buttonText, onClickListener);
        }

        public Builder setOnDismissListener(OnDismissListener listener) {
            parameters.mOnDismissListener = listener;
            return this;
        }

        public Builder setInsideContentView(View view){
            parameters.mInsideContentView = view;
            return this;
        }

        public CustomDialog create() {
            Log.d(TAG, "dialog create()");
            final CustomDialog dialog = new CustomDialog(parameters.mContext, mTheme);
            dialog.parameters = parameters;
            return dialog;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }

    }

    @Override
    public void show() {
        Log.d(TAG, "dialog show()");
        if(getContext() instanceof Activity) {
            Activity activity = (Activity)getContext();
            if(activity.isFinishing()){
                //DO NOT CREATE DIALOG WHEN ACTIVITY IS FINISHED!!!
                return;
            }
        }
        super.show();
        ButterKnife.bind(this);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        this.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
