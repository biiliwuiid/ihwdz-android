package com.ihwdz.android.hwapp.ui.orders.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.CityResultBean;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */
public class CityFilterAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "CityFilterAdapter";
    Context mContext;
    private boolean isVipDistrict = false;
    private List<LogisticsCityData.CityEntity> mData; // 出发地 - 目的地
    private List<CityResultBean> mData2;              // 会员信息 - 选择所在地区
    int color = 0;
    int normalColor = 0;
    Drawable selectedBackground;
    Drawable normalBackground;
    OnItemClickListener mListener;

    @Inject
    public CityFilterAdapter(Context context){
        this.mContext = context;
        color = mContext.getResources().getColor(R.color.orangeTab);
        normalColor = mContext.getResources().getColor(R.color.blackText3);
        selectedBackground = mContext.getResources().getDrawable(R.drawable.item_selected_bg_cl);
        normalBackground = mContext.getResources().getDrawable(R.drawable.item_normal_bg_cl);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_client_filter_city, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isVipDistrict){
            final CityResultBean model = mData2.get(position);
            final String province = model.label;
            if (holder instanceof ViewHolder){
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.iv.setVisibility(View.GONE); // hide select tag
                viewHolder.tv.setText(province);
                viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "========= Clicked : "+ province);

                        if (model.isSelected() == false){
                            model.setSelected(true);
                        }
                        mListener.onItemClicked(model);
                        notifyDataSetChanged();
                    }
                });

                if (model.isSelected()){
                    //viewHolder.linear.setBackground(selectedBackground);
                    viewHolder.tv.setTextColor(color);
                }else {
                    //viewHolder.linear.setBackground(normalBackground);
                    viewHolder.tv.setTextColor(normalColor);
                }
                model.setSelected(false);
            }

        }else {
            final LogisticsCityData.CityEntity model = mData.get(position);
            final String province = model.text;
            if (holder instanceof ViewHolder){
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.iv.setVisibility(View.GONE); // hide select tag
                viewHolder.tv.setText(province);
                viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "========= Clicked : "+ province);

                        if (model.isSelected() == false){
                            model.setSelected(true);
                        }
                        mListener.onItemClicked(model);
                        notifyDataSetChanged();
                    }
                });

                if (model.isSelected()){
                    //viewHolder.linear.setBackground(selectedBackground);
                    viewHolder.tv.setTextColor(color);
                }else {
                    //viewHolder.linear.setBackground(normalBackground);
                    viewHolder.tv.setTextColor(normalColor);
                }
                model.setSelected(false);
            }

        }


    }

    @Override
    public int getItemCount() {
        if (isVipDistrict){
            return mData2 == null ? 0 : mData2.size();
        }else {
            return mData == null ? 0 : mData.size();
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear)
        LinearLayout linear;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.iv)
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setIsVip(boolean is){
        this.isVipDistrict = is;
    }

    public void setDataList(List<LogisticsCityData.CityEntity> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }

    public void setData2List(List<CityResultBean> dataList){

        mData2 = dataList;
        notifyDataSetChanged();
    }

    public void clear(){
        mData = null;
        mData2 = null;
        notifyDataSetChanged();
    }
}
