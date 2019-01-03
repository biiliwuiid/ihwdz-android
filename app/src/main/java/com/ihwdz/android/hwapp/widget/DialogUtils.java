package com.ihwdz.android.hwapp.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.pedaily.yc.ycdialoglib.selectDialog.CustomSelectDialog;

import org.yczbj.ycvideoplayerlib.VideoPlayerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/16
 * desc :   拍照/相册 弹窗
 * version: 1.0
 * </pre>
 */
public class DialogUtils {

    private static ProgressDialog dialog;
    public static void showProgressDialog(Activity activity) {
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("玩命加载中……");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /**
     * 展示对话框视图，构造方法创建对象
     */
    public static CustomSelectDialog showDialog(Activity activity ,
                                                CustomSelectDialog.SelectDialogListener listener,
                                                List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(activity,
                R.style.transparentFrameWindowStyle, listener, names);
        if(VideoPlayerUtils.isActivityLiving(activity)){
            dialog.show();
        }
        return dialog;
    }

}
