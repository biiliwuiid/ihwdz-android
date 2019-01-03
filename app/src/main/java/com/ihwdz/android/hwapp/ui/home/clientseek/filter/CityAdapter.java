package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.ProvinceData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{

    String TAG = "CityAdapter";
    Context mContext;
    // private List<ProvinceData.Bean> mData;
    private List<ProvinceData.City> mData;
    int color = 0;
    Drawable selected;
    Drawable normal;
    OnCityItemClickListener mListener;
    private boolean clearAllSelected = false; // 清除所有城市

    private boolean clearSelected = false;
    private String clearProvince = "";         // 清除该省份下所有城市

    private List<ProvinceData.City> mSelectedCities;  // 已选城市

    @Inject
    public CityAdapter(Context context){
        this.mContext = context;
        color = mContext.getResources().getColor(R.color.orangeTab);
        selected = mContext.getResources().getDrawable(R.drawable.selected);
        normal = mContext.getResources().getDrawable(R.drawable.unselected);

    }
    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_client_filter_city, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CityAdapter.ViewHolder holder, int position) {
        final ProvinceData.City model = mData.get(position);
        final String city = model.mCity.name;

        // 清除所有已选所有城市
        if (clearAllSelected){
            LogUtils.printInfo(TAG, "=========================== 清除所有已选所有城市: isSelected = false" );
            model.isSelected = false;
        }

        // 更新当前所选城市
        // String provinceName = model.mProvince.name;
        if (mSelectedCities != null && mSelectedCities.size() > 0){
            for (int i = 0; i < mSelectedCities.size(); i++){
                // if (TextUtils.equals(provinceName, mSelectedCities.get(i).mProvince.name)){ // 省份相同
                // }
                if (TextUtils.equals(city, mSelectedCities.get(i).mCity.name)){         // 城市相同
                    model.isSelected = true;
                    LogUtils.printInfo(TAG, "mSelectedCities: isSelected = true : city: " + city );
                }
            }
        }



        holder.iv.setVisibility(View.VISIBLE);        // show select tag
        holder.tv.setGravity(Gravity.CENTER_VERTICAL);
        holder.tv.setText(city);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllSelected = false;
                model.isSelected = !model.isSelected;  // change select state
                if (model.isSelected){
                    mListener.onItemSelected(model);   // state : selected -> add to selected list
                }else{
                    mListener.onItemUnSelected(model);
                }
                notifyDataSetChanged();
            }
        });

        if (model.isSelected){
            holder.iv.setImageDrawable(selected);
        }else {
            holder.iv.setImageDrawable(normal);
        }
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

    public void addItemClickListener(OnCityItemClickListener listener){
        this.mListener = listener;
    }

    public void setDataList(List<ProvinceData.City> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }

    // 清空该 -province 下的城市
    public void clearSelected(String province){
        clearSelected = true;
        clearProvince = province;
        notifyDataSetChanged();
    }

    // 清空所有城市
    public void clearAllSelected(){
        clearAllSelected = true;
        notifyDataSetChanged();
    }

    // 清除已选区域某个省份 更新已选城市
    public void setCitiesSelected(List<ProvinceData.City> list){
        clearAllSelected = true;
        mSelectedCities = list;
        notifyDataSetChanged();
    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
