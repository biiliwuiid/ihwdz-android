package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/20
 * desc :  报价 -> 选择仓库(历史仓库)
 * version: 1.0
 * </pre>
 */
public class HistoryWarehouseAdapter extends RecyclerView.Adapter {
    private String TAG = "HistoryWarehouseAdapter";
    private Context mContext;
    private List<WarehouseData.WarehouseForQuotePost> mData;
    private OnItemClickListener mListener;

    @Inject
    public HistoryWarehouseAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final WarehouseData.WarehouseForQuotePost model = mData.get(position);
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.warehouseTv.setText(model.warehouse);

            viewHolder.warehouseTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 2018/11/20 object change to json.
                    Gson gson = new GsonBuilder().create();
                    final String jsonStr = gson.toJson(model);
                    LogUtils.printCloseableInfo(TAG, jsonStr);

                    Constant.id = ""+ model.id;
                    Constant.warehouse = model.warehouse;

                    Constant.provinceName = model.province;
                    Constant.provinceCode = model.provinceCode;

                    Constant.cityName = model.city;
                    Constant.cityCode = model.cityCode;

                    Constant.districtName = model.district;
                    Constant.districtCode = model.districtCode;

                    Constant.warehouseAddress = model.warehouseAddress;
                    Constant.warehousePhone =  model.warehousePhone;
                    Constant.warehouseContact = model.warehouseContact;

                    mListener.onItemClicked(jsonStr, model.warehouse);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_warehouse_name) TextView warehouseTv;   // 仓库名称

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setDataList(List<WarehouseData.WarehouseForQuotePost> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<WarehouseData.WarehouseForQuotePost> dataList){
        mData.addAll(dataList);
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
