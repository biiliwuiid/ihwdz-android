package com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/26
 * desc :  设备清单 - 5 列表格
 * version: 1.0
 * </pre>
 */
public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "DeviceAdapter";
    Context mContext;
    private List<ClientDetailData.DeviceVOs> mData;

    @Inject
    public DeviceAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_device_client_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            ClientDetailData.DeviceVOs model = mData.get(position);

            viewHolder.deviceName.setText(model.deviceName);
            viewHolder.deviceBrand.setText(model.deviceBrand);
            viewHolder.deviceGrade.setText(model.deviceGrade);
            viewHolder.deviceQuantity.setText(model.deviceQuantity);
            viewHolder.purchaseDate.setText(model.purchaseDate);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.deviceName)
        TextView deviceName;
        @BindView(R.id.deviceBrand)
        TextView deviceBrand;
        @BindView(R.id.deviceGrade)
        TextView deviceGrade;
        @BindView(R.id.deviceQuantity)
        TextView deviceQuantity;
        @BindView(R.id.purchaseDate)
        TextView purchaseDate;
        @BindView(R.id.device_table_layout)
        TableLayout deviceTableLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setDataList(List<ClientDetailData.DeviceVOs> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<ClientDetailData.DeviceVOs> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
