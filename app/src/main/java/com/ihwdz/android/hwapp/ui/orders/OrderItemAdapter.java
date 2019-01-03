package com.ihwdz.android.hwapp.ui.orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrdersData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/28
 * desc :   订单列表 明细条目 orderItems
 * OrderFragment OrderAdapter & OrderDetail Activity
 * version: 1.0
 * </pre>
 */
public class OrderItemAdapter extends RecyclerView.Adapter {

    String TAG = "OrderItemAdapter";
    private Context mContext;
    private List<OrdersData.OrderItem> mData;
    private String currency;     // 货币符号+占位
    private String specBrand;    // 牌号，厂家
    private String warehouse;    // 仓库

    private boolean isOrderDetail = false;

    private OnOrderItemClickListener.OnItemClickListener mItemListener;

    private String orderId;
    private String orderStatus;
    private int orderOption;     // null没有(-1); A0- 支付手续费(0); A1- 开票申请(1); A2- 申请展期(2)
    private int vipType;

    @Inject
    public OrderItemAdapter(Context context){
        this.mContext = context;
        currency = mContext.getResources().getString(R.string.currency_sign);                            // 货币符号
        specBrand = mContext.getResources().getString(R.string.spec_brand);  // 牌号，厂家
        warehouse = mContext.getResources().getString(R.string.order_warehouse);

        vipType = Constant.VIP_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (isOrderDetail){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_item, parent, false);
        }else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_order_item, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //LogUtils.printCloseableInfo(TAG, "onBindViewHolder");
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final OrdersData.OrderItem model = mData.get(position);

            // LogUtils.printCloseableInfo(TAG, "onBindViewHolder BREED: " + model.breed);

            if (mItemListener != null && orderId != null && orderId.length() > 0){
                // 点击查看订单详情(OrderAdapter)
                viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemListener.onItemClicked(orderId, orderStatus, orderOption);
                    }
                });
            }else {}

            viewHolder.breed.setText(model.breed);
            viewHolder.brand.setText(String.format(specBrand,model.spec, model.brand));


            // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
            if (vipType == 1){
                viewHolder.price.setText(currency + model.salePrice);
            }else if (vipType == 2){
               // LogUtils.printCloseableInfo(TAG, "========================purchasePrice: " + model.purchasePrice);
                viewHolder.price.setText(currency + model.purchasePrice);
            }else {
                // 非商家/交易 会员
            }


            viewHolder.qty.setText("x" + model.qty);

            if (isOrderDetail){
                viewHolder.warehouse.setText(String.format(warehouse,model.warehouse));
                viewHolder.warehouse.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear_order) LinearLayout linear;
        @BindView(R.id.tv_breed) TextView breed;
        @BindView(R.id.tv_brand) TextView brand;
        @BindView(R.id.tv_warehouse) TextView warehouse;

        @BindView(R.id.tv_price) TextView price;
        @BindView(R.id.tv_qty) TextView qty;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setDataList(List<OrdersData.OrderItem> dataList, boolean isDetail){
        this.isOrderDetail = isDetail;
        mData = dataList;
        notifyDataSetChanged();
    }
    public void setDataList(List<OrdersData.OrderItem> dataList, String id,String orderStatus, int orderOption, OnOrderItemClickListener.OnItemClickListener listener){
        this.orderId = id;
        this.orderStatus = orderStatus;
        this.orderOption = orderOption;

        this.mItemListener = listener;
        mData = dataList;
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

}
