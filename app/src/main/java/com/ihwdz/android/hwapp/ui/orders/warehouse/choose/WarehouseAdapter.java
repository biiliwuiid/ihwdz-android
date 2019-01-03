package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
 * desc :   报价 -> 选择仓库
 * version: 1.0
 * </pre>
 */
public class WarehouseAdapter extends RecyclerView.Adapter {

    String TAG = "WarehouseAdapter";
    Context mContext;
    List<WarehouseData.Warehouse> mData;
    OnItemClickListener mListener;
    OnWarehouseItemClickListener mWarehouseClickListener;

    @Inject
    public WarehouseAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_warehouse, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final WarehouseData.Warehouse model = mData.get(position);
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;

            // 有别名 则: 仓库名称 +（别名）
            String warehouseName = model.companyShortName;
            List<String> tempList = null;
            String alias = "";
            if (model.aliasList != null && model.aliasList.size()>0){
                tempList = model.aliasList;
                alias = tempList.toString();
                alias = alias.substring(1, alias.length()-1);     // 去除中括号 [test]->test
                warehouseName = warehouseName + " (" + alias +")"; // 加小括号
            }



            viewHolder.warehouse.setText(warehouseName);

            String addressStr = model.addressProvinceName + ","+ model.addressCityName + "," + model.addressDistrictName + model.address;
            viewHolder.address.setText(addressStr);

            if (!TextUtils.isEmpty(model.name)){
                viewHolder.linkman.setText(model.name);
            }else {
                viewHolder.linkman.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(model.mobile)){
                viewHolder.tel.setText(model.mobile);
            }else {
                viewHolder.tel.setVisibility(View.GONE);
            }

            viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.id = ""+ model.id;
                    Constant.warehouse = model.companyShortName;

                    Constant.provinceName = model.addressProvinceName;
                    Constant.provinceCode = model.addressProvinceCode;

                    Constant.cityName = model.addressCityName;
                    Constant.cityCode = model.addressCityCode;

                    Constant.districtName = model.addressDistrictName;
                    Constant.districtCode = model.addressDistrictCode;

                    Constant.warehouseAddress = model.address;
                    Constant.warehousePhone =  model.mobile;
                    Constant.warehouseContact = model.name;

                    String jsonStr = changeToWarehouse(model);
                    LogUtils.printCloseableInfo(TAG, jsonStr);
                    mListener.onItemClicked(jsonStr, model.companyShortName);

                }
            });
        }
    }

    // WarehouseData.Warehouse model -> WarehouseData.WarehouseForQuotePost warehouseObject
    // 搜索获取到的 仓库类型 转换成 报价上传的 仓库类型。
    private String changeToWarehouse(WarehouseData.Warehouse model) {

        WarehouseData.WarehouseForQuotePost warehouseObject = new WarehouseData.WarehouseForQuotePost();
        warehouseObject.id = model.id;
        warehouseObject.warehouse = model.companyShortName;

        warehouseObject.province = model.addressProvinceName;
        warehouseObject.provinceCode = model.addressProvinceCode;

        warehouseObject.city = model.addressCityName;
        warehouseObject.cityCode = model.addressCityCode;

        warehouseObject.district = model.addressDistrictName;
        warehouseObject.districtCode = model.addressDistrictCode;

        warehouseObject.warehouseAddress = model.address;
        warehouseObject.warehousePhone = model.mobile;
        warehouseObject.warehouseContact = model.name;

        mWarehouseClickListener.onWarehouseClicked(warehouseObject);
        // 2018/11/20 object change to json.
        Gson gson = new GsonBuilder().create();
        String jsonStr = gson.toJson(warehouseObject);
        return jsonStr;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear) LinearLayout linear;

        @BindView(R.id.tv_warehouse) TextView warehouse;   // 仓库名称
        @BindView(R.id.tv_address) TextView address;       // 仓库地址
        @BindView(R.id.tv_linkman) TextView linkman;       // 联系人
        @BindView(R.id.tv_tel) TextView tel;               // 电话

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setOnWarehouseItemClickListener(OnWarehouseItemClickListener listener){
        this.mWarehouseClickListener = listener;
    }

    public void setDataList(List<WarehouseData.Warehouse> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<WarehouseData.Warehouse> dataList){
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
