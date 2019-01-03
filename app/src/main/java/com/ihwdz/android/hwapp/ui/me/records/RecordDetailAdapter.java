package com.ihwdz.android.hwapp.ui.me.records;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.DealRecordData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class RecordDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "RecordDetailAdapter";
    Context mContext;
    List<DealRecordData.OrderItem> mData;

    @Inject
    public RecordDetailAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_record_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            DealRecordData.OrderItem model = mData.get(position);

            Log.d(TAG, "============ mData.size()  | position: " + mData.size() + " | " + position);
            if (mData.size() <= 1 || position == mData.size() - 1){
                viewHolder.dottedLine.setVisibility(View.GONE);
            }
            String str1 = model.breed +" | "+ model.spec + " | "+ model.brand;
            String str2 = model.qty + "吨          " + model.salePrice + "元/吨";
            viewHolder.breedTv.setText(str1);
            viewHolder.priceTv.setText(str2);

        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_breed)
        TextView breedTv;
        @BindView(R.id.tv_price)
        TextView priceTv;
        @BindView(R.id.dotted_line)
        View dottedLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setDataList(List<DealRecordData.OrderItem> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<DealRecordData.OrderItem> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

}
