package com.ihwdz.android.hwapp.ui.publish.purchase;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/25
 * desc :  发布求购 产品明细 产品数据Adapter
 * version: 1.0
 * </pre>
 */
public class BreedInfoAdapter extends BaseAdapter{

    String TAG = "BreedInfoAdapter";
    private Context mContext;
    private List<PublishData.ProductEntity> mData;
    private boolean isDataEmpty = false;

    private Drawable tvBackground;  // 带灰色下划线

    @Inject
    public BreedInfoAdapter(Context context){
        this.mContext = context;
        tvBackground = mContext.getResources().getDrawable(R.drawable.item_popup_window);
    }

    @Override
    public int getCount() {
        if (mData != null){
            isDataEmpty = false;
            if (mData.size() > 6){
                return 6;
            }else {
                return mData.size();
            }
        }else {
            isDataEmpty = true;
            return 0;
        }

        //return mData == null ? 0 : mData.size();
    }


    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        TextView tv = new TextView(mContext);

        int parentWidth = parent.getWidth();
        tv.setWidth(parentWidth);          // item TextView 宽度 与父容器同宽

        tv.setPadding(0,0,0,0);
        tv.setText(mData.get(position).name);
        tv.setBackground(tvBackground);
        tv.setTextSize(12);
        return tv;
    }

    static class ViewHolder {
         TextView tv;
         TextView tvEmpty;
    }


    public void setDataList(List<PublishData.ProductEntity> dataList){
        clear();
        mData = dataList;
        notifyDataSetChanged();
    }

    public void setEmptyDataList(String emptyRemind){
        clear();
        List<PublishData.ProductEntity> dataList = new ArrayList<>();
        PublishData.ProductEntity entity = new PublishData.ProductEntity();
        entity.name = emptyRemind;
        dataList.add(entity);
        mData = dataList;

        notifyDataSetChanged();
    }

    public void setEmptyDataList(){
        mData = null;
        notifyDataSetChanged();
    }

    public void clear(){
        if (mData != null){
            mData.clear();
            mData = null;
            notifyDataSetChanged();
        }
    }

}
