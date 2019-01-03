package com.ihwdz.android.hwapp.ui.publish.address;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PublishData;
import com.ihwdz.android.hwapp.model.bean.WarehouseData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :   收货地址
 * version: 1.0
 * </pre>
 */
public class AddressAdapter extends RecyclerView.Adapter{
    private String TAG = "AddressAdapter";
    private Context mContext;
    private List<PublishData.AddressEntity> mData;
    private OnAddressItemClickListener.OnItemClickListener mListener;
    private OnAddressItemClickListener.OnEditClickListener mEditListener;
    private String checkingRemind;

    @Inject
    public AddressAdapter(Context context){
        this.mContext = context;
        checkingRemind = mContext.getResources().getString(R.string.address_checking);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PublishData.AddressEntity model = mData.get(position);
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.linkmanTv.setText(model.contactName);

            // 审核中 不可用（隐藏 "编辑"）
            if (model.apply == 1){
                viewHolder.defaultTv.setVisibility(View.GONE);       // 默认
                viewHolder.editLinear.setVisibility(View.GONE);      // 编辑
                viewHolder.checkingTv.setVisibility(View.VISIBLE);   // 审核中

                // 点击 收货地址
                viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, checkingRemind, Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                viewHolder.editLinear.setVisibility(View.VISIBLE);   // 编辑
                viewHolder.checkingTv.setVisibility(View.GONE);      // 审核中

                // 默认地址
                if (model.isDefault == 1){
                    viewHolder.defaultTv.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.defaultTv.setVisibility(View.GONE);
                }

                // 点击编辑 收货地址
                viewHolder.editLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditListener.onEditClicked(model.apply, model.memberDeliveryAddressId);
                    }
                });

                // 点击选中 收货地址
                viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.addressSelected_province = model.provinceName;
                        Constant.addressSelected_provinceCode = model.provinceCode;
                        Constant.addressSelected_city = model.cityName;
                        Constant.addressSelected_cityCode= model.cityCode;
                        Constant.addressSelected_district = model.districtName;
                        Constant.addressSelected_districtCode = model.districtCode;
                        Constant.addressSelected_address = model.address;
                        Constant.addressSelected_mobile = model.mobile;
                        Constant.addressSelected_contact = model.contactName;
                        mListener.onItemClicked();
                    }
                });
            }


            String addressStr = model.provinceName + ","+ model.cityName + "," + model.districtName + model.address;
            viewHolder.addressTv.setText(addressStr);


        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear) LinearLayout linear;          // ADDRESS ITEM

        @BindView(R.id.tv_linkman) TextView linkmanTv;       // 收货人
        @BindView(R.id.tv_default) TextView defaultTv;       // 默认标签
        @BindView(R.id.tv_checking) TextView checkingTv;     // 审核中标签
        @BindView(R.id.tv_address) TextView addressTv;       // 收货地址

        @BindView(R.id.linear_edit) LinearLayout editLinear; // 编辑

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnAddressItemClickListener.OnItemClickListener listener){
        this.mListener = listener;
    }
    public void setOnEditClickListener(OnAddressItemClickListener.OnEditClickListener listener){
        this.mEditListener = listener;
    }

    public void setDataList(List<PublishData.AddressEntity> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<PublishData.AddressEntity> dataList){
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
