package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

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
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder>{
    String TAG = "ProvinceAdapter";
    Context mContext;
    private List<ProvinceData.ProvinceEntity> mData;
    //private ProvinceData mData;
    int color = 0;
    int normalColor = 0;
    Drawable selectedBackground;
    Drawable normalBackground;
    OnProvinceItemClickListener mListener;

    @Inject
    public ProvinceAdapter(Context context){
        this.mContext = context;
        color = mContext.getResources().getColor(R.color.orangeTab);
        normalColor = mContext.getResources().getColor(R.color.blackText3);
        selectedBackground = mContext.getResources().getDrawable(R.drawable.item_selected_bg_cl);
        normalBackground = mContext.getResources().getDrawable(R.drawable.item_normal_bg_cl);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_client_filter_city, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ProvinceData.ProvinceEntity model = mData.get(position);

        //final ProvinceData.ProvinceEntity model = mData.data.get(position);
        final String province = model.getProvince().getName();

        holder.iv.setVisibility(View.GONE); // hide select tag
        holder.tv.setText(province);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "========= Clicked province: "+ province);

                if (model.isSelected() == false){
                    model.setSelected(true);
                }
                mListener.onProvinceItemClicked(model.getProvince(), model.city); // remind city list change date
                notifyDataSetChanged();
            }
        });

        if (model.isSelected()){
            holder.linear.setBackground(selectedBackground);
            holder.tv.setTextColor(color);
        }else {
            holder.linear.setBackground(normalBackground);
            holder.tv.setTextColor(normalColor);
        }
        model.setSelected(false);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
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
    public void addItemClickListener(OnProvinceItemClickListener listener){
        this.mListener = listener;
    }

//    public void setDataList(ProvinceData data){
//        makeFirstSelected(data.data);
//        mData = data;
//        notifyDataSetChanged();
//    }

    public void setDataList(List<ProvinceData.ProvinceEntity> dataList){
        makeFirstSelected(dataList);
        mData = dataList;
        notifyDataSetChanged();
    }
    private void makeFirstSelected(List<ProvinceData.ProvinceEntity> dataList) {
        dataList.get(0).setSelected(true);
    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
