package com.ihwdz.android.hwapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.ihwdz.android.hwapp.R;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/02
 * desc :  自定义可取消的 RadioButton，可与 RadioGroup一起用
 * version: 1.0
 * </pre>
 */
public class ToggleRadioButton  extends android.support.v7.widget.AppCompatRadioButton {

    public ToggleRadioButton(Context context) {
        this(context, null);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
        if (!isChecked()) {
            ((RadioGroup)getParent()).clearCheck();
        }
    }

}
