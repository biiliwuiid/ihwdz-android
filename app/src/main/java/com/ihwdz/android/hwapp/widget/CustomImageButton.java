package com.ihwdz.android.hwapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :
 * version: 1.0
 * </pre>
 */
public class CustomImageButton extends android.support.v7.widget.AppCompatImageButton {

    private String text = null;      // 要显示的文字
    private int color;               // 文字的颜色
    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public void setText(String text){
        this.text = text;       //设置文字
    }

    public void setColor(int color){
        this.color = color;    //设置文字颜色
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(color);
        //canvas.drawText(text, 15, 20, paint);  //绘制文字
        canvas.drawText(text, 5, 10, paint);  //绘制文字
    }
}
