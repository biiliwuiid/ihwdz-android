package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/28
 * desc :  已选区域的 省份
 * version: 1.0
 * </pre>
 */
class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ViewHolder>{
    String TAG = "SelectedAdapter";
    Context mContext;
    private List<String> mData;
    int color = 0;
    Drawable selected;
    OnItemClickListener mListener;

    @Inject
    public SelectedAdapter(Context context){
        this.mContext = context;
        color = mContext.getResources().getColor(R.color.orangeTab);
        selected = mContext.getResources().getDrawable(R.drawable.selected);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_client_filter_selected, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String province = mData.get(position);
        holder.tv.setText(province);
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(province);  // delete all cities belong to this province.
                mData.remove(province);
                notifyDataSetChanged();
            }
        });

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void addData(String province){
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mData.size() < 5){
            mData.add(province);
            notifyDataSetChanged();
        }
    }

    public void setDataList(List<String> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }

    public void removeData(String province){
        if (mData != null && mData.contains(province)){
            mData.remove(province);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
