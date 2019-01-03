package com.ihwdz.android.hwapp.ui.home.infoday;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/31
 * desc :   可滚动标题栏 : 看资讯& 订单栏
 * version: 1.0
 * </pre>
 */
public class InfoTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "InfoTitleAdapter";
    Context mContext;
    private List<CheckableModel> mData;
    OnItemTitleClickListener mListener;
    boolean isClickable = true;

    @Inject
    public InfoTitleAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder){
            //LogUtils.printInfo(TAG, "onBindViewHolder:-------- position:  " + position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.checkBox.getPaint().setFakeBoldText(true);
            final CheckableModel model = mData.get(position);
            viewHolder.checkBox.setText(model.title);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.printInfo(TAG, "onClick:--------   " + position);
                    if (!model.isChecked){
                        LogUtils.printInfo(TAG, "onItemTitleClick " );
                        mListener.onItemTitleClick(model.title);
                    }
                    model.isChecked = true;
                    setCheckedItem(model.title);
                }
            });
            if (model.isChecked){

                viewHolder.checkBox.setChecked(model.isChecked);
            }else {
                viewHolder.checkBox.setChecked(model.isChecked);
            }


        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addItemTitleCheckListener(OnItemTitleClickListener listener){
        mListener = listener;
    }

    public void setDataList(List<String> dataList){
        List<CheckableModel> models = new ArrayList<>();
        CheckableModel model;
        for (int i = 0; i< dataList.size(); i++ ){
            model = new CheckableModel();
            if (i == 0){
                model.isChecked = true;
            }
            model.title = dataList.get(i);
            models.add(model);
        }
        mData = models;
        notifyDataSetChanged();
    }
    public void addDataList(String data){
        CheckableModel model = new CheckableModel();
        model.title = data;
        mData.add(model);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

    private void setCheckedItem(String checkedItem){
        for (int i = 0; i< mData.size(); i++ ){
            if (TextUtils.equals(checkedItem, mData.get(i).title)){
                mData.get(i).isChecked = true;
            }else {
                mData.get(i).isChecked = false;
            }
        }
        notifyDataSetChanged();
    }

    static class CheckableModel{
        public String title = "";
        public boolean isChecked = false;
        public boolean isClickable = true;
    }
}
