package com.ihwdz.android.hwapp.ui.orders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.model.bean.OrdersData;
import com.ihwdz.android.hwapp.ui.purchase.purchasedetail.OnOrderClickListener;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/21
 * desc :   订单列表  0 - 交易会员；1 - 商家会员 隐藏 手续费 和 按钮；
 * version: 1.0
 * </pre>
 */
public class OrderAdapter extends RecyclerView.Adapter{

    String TAG = "OrderAdapter";
    private Context mContext;
    private List<OrdersData.OrdersModel> mData;
    private int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;

    private OnOrderItemClickListener.OnItemClickListener mItemListener;           //order detail
    private OnOrderItemClickListener.OnPayClickListener mPayListener;             //A0- 支付手续费(0)
    private OnOrderItemClickListener.OnInvoiceClickListener mInvoiceListener;     //A1- 开票申请(1)
    private OnOrderItemClickListener.OnExtensionClickListener mExtensionListener; //A2- 申请展期(2)


    private int vipType;
    private boolean isDealVip = false;

    // private int vipMode = 0;        // 0 - 交易会员；1 - 商家会员；

    private int buttonIntTag = -1;     // 按钮状态：null没有(-1); A0- 支付手续费(0); A1- 开票申请(1); A2- 申请展期(2)
    final private int BT_NULL = -1;
    final private int BT_PAY = 0;
    final private int BT_INVOICE = 1;
    final private int BT_EXTENSION = 2;

    private OrderItemAdapter mAdapter;

    // 按钮状态
    private Drawable buttonBg;
    private int buttonColor;
    private String buttonStr;       // 按钮文字
    // private String cancelBtStr;  // 取消订单 -》 cancelled now
    private String extensionBtStr;  // 申请展期
    private String invoiceBtStr;    // 申请展期
    private String payBtStr;        // 支付手续费

    // 订单状态
    private String statusStr;       // 订单状态文字
    private String waitCheck;       // 待审核
    private String waitPay;         // 待付款
    private String waitCollection;  // 待收款
    private String waitDeliver;     // 待发货
    private String waitTake;        // 待收货
    private String waitInvoice;     // 待开票
    private String orderSuccess;    // 交易成功
    private String orderFailure;    // 交易失败
    private String waitConfirm;     // 待付款/待收货 判断还款是否完成（加逾期）？？？？？？？？？？

    private String chargeStr, totalStr; // 手续费，合计

    public OrderAdapter(Context context){
        this.mContext = context;

        // order status
        waitCheck = mContext.getResources().getString(R.string.order_wait_check);    // 待审核
        waitPay = mContext.getResources().getString(R.string.order_wait_pay);      // 待付款
        waitCollection = mContext.getResources().getString(R.string.order_wait_collection);   // 待收款
        waitDeliver = mContext.getResources().getString(R.string.order_wait_deliver);  // 待发货
        waitTake = mContext.getResources().getString(R.string.order_wait_take);     // 待收货
        waitInvoice = mContext.getResources().getString(R.string.order_wait_invoice);     // 待开票
        orderSuccess = mContext.getResources().getString(R.string.order_success);    // 交易成功
        orderFailure = mContext.getResources().getString(R.string.order_failure);    // 交易失败
        waitConfirm = mContext.getResources().getString(R.string.order_wait_take);    //

        chargeStr = mContext.getResources().getString(R.string.charges);        // 手续费：
        totalStr = mContext.getResources().getString(R.string.total);           // 合计：

        // button style: orange ring + orange text
        buttonBg = mContext.getResources().getDrawable(R.drawable.orange_ring_button);
        buttonColor = mContext.getResources().getColor(R.color.orangeTab);

        // button text
        // cancelBtStr = mContext.getResources().getString(R.string.order_cancel);         // 取消订单
        payBtStr = mContext.getResources().getString(R.string.order_pay);                  // 支付手续费
        extensionBtStr = mContext.getResources().getString(R.string.extension_apply_title);// 申请展期
        invoiceBtStr = mContext.getResources().getString(R.string.invoice_apply);  // 申请开票

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }
        v = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        vipType = Constant.VIP_TYPE;
        isDealVip = vipType == 1;
        // LogUtils.printCloseableInfo(TAG, "vipType: "+ vipType+ "  isDealVip: " + isDealVip);
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final OrdersData.OrdersModel model = mData.get(position);

            // 订单状态
            viewHolder.orderStatus.setText(getStatusTag(model.subStatus));

            // 按钮状态：  null没有; A0- 支付手续费; A1- 开票申请; A2- 申请展期
            buttonIntTag = getOptionTag(model.operateStatus);
            buttonBg = mContext.getResources().getDrawable(R.drawable.orange_ring_button);
            // 点击查看订单详情
            viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemClicked(model.orderId, getStatusTag(model.subStatus), getOptionTag(model.operateStatus));
                }
            });

            String companyName = "";
            String totalAmount = "";
            // company name & 合计
            // -1 普通用户；0 资讯会员；1 交易会员；2 商家会员
            if (vipType == 1){
                // 交易会员
                viewHolder.charges.setVisibility(View.VISIBLE);    // 展示手续费
                companyName = model.memberName;
                totalAmount = model.saleSumAmt;
                // LogUtils.printCloseableInfo(TAG, "合计======================== saleSumAmt: " + model.saleSumAmt);
            }else if (vipType == 2){
                // 商家会员 隐藏 手续费 和 按钮
                viewHolder.charges.setVisibility(View.GONE);
                viewHolder.btLinear.setVisibility(View.GONE);
                companyName = model.sellMemberName;
                totalAmount = ""+ model.purchaseSumAmt;
                LogUtils.printCloseableInfo(TAG, "合计======================== purchaseSumAmt: " + model.purchaseSumAmt);
            }else {
                // 非商家/交易 会员
            }

            // viewHolder.companyName.setText(companyName); - 公司名称 -》 订单号
            viewHolder.companyName.setText(model.contractCode);
            viewHolder.total.setText(String.format(totalStr,totalAmount));


            // orderItems
            mAdapter = new OrderItemAdapter(mContext);
            mAdapter.setDataList(model.orderItems,  model.orderId,getStatusTag(model.subStatus), getOptionTag(model.operateStatus), mItemListener);
            initRecyclerView(viewHolder.recyclerView);
            // LogUtils.printCloseableInfo(TAG, "=======    model.orderItems : " + model.orderItems.size());

            // 手续费
            if (model.serviceCharge != null){
                viewHolder.charges.setText(String.format(chargeStr, model.serviceCharge ));
            }else {
                viewHolder.charges.setText(String.format(chargeStr, 0.00 ));
            }




            // button
            if (buttonStr != null){
                viewHolder.btLinear.setVisibility(View.VISIBLE);
                viewHolder.tvButton.setText(buttonStr);
                viewHolder.tvButton.setTextColor(buttonColor);
                viewHolder.tvButton.setBackground(buttonBg);
            }else {
                viewHolder.btLinear.setVisibility(View.GONE);
            }
//            viewHolder.tvButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LogUtils.printCloseableInfo(TAG, "onClick=========== orderId: " + model.orderId +"  | model.operateStatus"+ model.operateStatus +  "buttonIntTag: "+ getOptionTag(model.operateStatus));
//                    switch (getOptionTag(model.operateStatus)){
//                        case BT_PAY:
//                            mPayListener.onPayClicked(model.orderId, model.orderSn, model.serviceCharge);
//                            break;
//                        case BT_INVOICE:
//                            mInvoiceListener.onInvoiceClicked(model.orderId);
//                            break;
//                        case BT_EXTENSION:
//                            mExtensionListener.onExtensionClicked(model.orderId);
//                            break;
//                    }
//
//                }
//            });



        }
    }

    @Override
    public int getItemCount() {
        if (mIsLoadMore){
            return mData == null ? 0 : mData.size() + 1;// add footer
        }else {
            return mData == null ? 0 : mData.size();    // don't need footer
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsLoadMore && position == getItemCount() - 1) {
            return FOOTER;
        }
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear) LinearLayout linear;
        @BindView(R.id.tv_company) TextView companyName;
        @BindView(R.id.tv_order_status) TextView orderStatus;

        @BindView(R.id.recyclerView) RecyclerView recyclerView;

        @BindView(R.id.tv_charges) TextView charges;
        @BindView(R.id.tv_total) TextView total;
        @BindView(R.id.tv_button) TextView tvButton;
        @BindView(R.id.bt_linear) LinearLayout btLinear;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv)
        TextView tv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 展示FooterView
     * @param holder
     */
    protected void bindFooterItem(RecyclerView.ViewHolder holder) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        switch (mLoadMoreStatus) {
            case Constant.loadStatus.STATUS_LOADING:
                holder.itemView.setVisibility(View.VISIBLE);
                //footerViewHolder.pb.setVisibility(View.VISIBLE);
                footerViewHolder.tv.setText(mContext.getResources().getString(R.string.load_more));
                break;
            case Constant.loadStatus.STATUS_EMPTY:
                holder.itemView.setVisibility(View.VISIBLE);
                //footerViewHolder.pb.setVisibility(View.GONE);
                footerViewHolder.tv.setText(mContext.getResources().getString(R.string.load_more_no));
                holder.itemView.setOnClickListener(null);
                break;
            case Constant.loadStatus.STATUS_ERROR:
                holder.itemView.setVisibility(View.VISIBLE);
                //footerViewHolder.pb.setVisibility(View.GONE);
                footerViewHolder.tv.setText(mContext.getResources().getString(R.string.load_more_error));
                //holder.itemView.setOnClickListener(mListener);
                break;
            case Constant.loadStatus.STATUS_PREPARE:
                holder.itemView.setVisibility(View.INVISIBLE);
                break;
            case Constant.loadStatus.STATUS_DISMISS:
                holder.itemView.setVisibility(View.GONE);
        }
    }

    public void setDataList(List<OrdersData.OrdersModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<OrdersData.OrdersModel> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

    public int getLoadMoreStatus(){
        return mLoadMoreStatus;
    }
    /**
     * 设置footer的状态,并通知更改
     */
    public void setLoadMoreStatus(int status) {
        this.mLoadMoreStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }

    // scroll to load set true; load complete set false
    public void setIsLoadMore( boolean isLoadMore) {
        this.mIsLoadMore = isLoadMore;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnOrderItemClickListener.OnItemClickListener listener){
        this.mItemListener = listener;
    }
    public void setOnPayBtClickListener(OnOrderItemClickListener.OnPayClickListener listener){
        this.mPayListener = listener;
    }
    public void setOnInvoiceBtClickListener(OnOrderItemClickListener.OnInvoiceClickListener listener){
        this.mInvoiceListener = listener;
    }
    public void setOnExtensionBtClickListener(OnOrderItemClickListener.OnExtensionClickListener listener){
        this.mExtensionListener = listener;
    }


    private void initRecyclerView(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    //订单状态 (后台状态：买：订单状态 | 卖：订单状态)
    private String getStatusTag(String status){
        String result = "";
        if (status != null){
            int statusInt = Integer.valueOf(status);
            switch (statusInt){
                case 10: //待审核： 待审核（买） - 取消订单 | 待审核（卖）
                    statusStr = waitCheck;
                    break;
                case 11: //待上传采购合同： 待发货（买） - 取消订单 | 待收款（卖）
                    if (isDealVip){
                        statusStr = waitDeliver;
                    }else {
                        statusStr = waitCollection;
                    }
                    break;
                case 12: //待支付手续费： 待付款（买） - 取消订单 | 待收款（卖）
                    if (isDealVip){
                        statusStr = waitPay;
                    }else {
                        statusStr = waitCollection;
                    }

                    break;
                case 20: //待收款： 待付款（买） - 取消订单 | 待收款（卖）
                    if (isDealVip){
                        statusStr = waitPay;
                    }else {
                        statusStr = waitCollection;
                    }
                    break;
                case 30: //待付款：  待发货（买） - 取消订单 | 待收款（卖）
                    if (isDealVip){
                        statusStr = waitDeliver;
                    }else {
                        statusStr = waitCollection;
                    }
                    break;
                case 40: // 待发货：  待发货（买） - 取消订单 | 待发货（卖）
                    statusStr = waitDeliver;
                    break;
                case 50: // 待确认收货：  待发货（买） - 取消订单 | 待收货（卖）
                    if (isDealVip){
                        statusStr = waitConfirm;
                    }else {
                        statusStr = waitTake;
                    }
                    break;
                case 90: //交易失败： 交易失败（买） - 取消订单 | 交易失败（卖）
                    statusStr = orderFailure;
                    break;
                case 100: //待开票： 待开票（买） - 取消订单 | 交易成功（卖）
                    if (isDealVip){
                        statusStr = waitInvoice;
                    }else {
                        statusStr = orderSuccess;
                    }
                    break;
                case 101: //待开票审核： 待开票（买） - 取消订单 | 交易成功（卖）
                    if (isDealVip){
                        statusStr = waitInvoice;
                    }else {
                        statusStr = orderSuccess;
                    }
                    break;
                case 102: //交易成功： 交易成功（买） - 交易成功 | 待收款（卖）
                    statusStr = orderSuccess;
                    break;
            }
            result = statusStr;
        }else {
            LogUtils.printCloseableInfo(TAG, "=======    status == null   =======");
        }
        return result;
    }

    /**
     *   button 状态 null没有; A0- 支付手续费; A1- 开票申请; A2- 申请展期
     *   卖家不显示 button
     */
    private int getOptionTag(String operateStatus){
        if (operateStatus != null){
            if (TextUtils.equals(operateStatus, "A0")){
                //  A0- 支付手续费
                buttonStr = payBtStr;
                buttonIntTag = 0;
                return 0;
            }
            if (TextUtils.equals(operateStatus, "A1")){
                //  A1- 开票申请
                buttonStr = invoiceBtStr;
                buttonIntTag = 1;
                return 1;
            }
            if (TextUtils.equals(operateStatus, "A2")){
                //  A2- 申请展期
                buttonStr = extensionBtStr;
                buttonIntTag = 2;
                return 2;
            }
            return buttonIntTag;
        }else {
            // 取消 “取消订单”按钮  -> 按钮只有一种样式 -orange
//            buttonStr = cancelBtStr;
//            buttonBg = mContext.getResources().getDrawable(R.drawable.grey_ring_button);
//            buttonColor = mContext.getResources().getColor(R.color.grayLineDarker);
            buttonStr = null;
            buttonIntTag = -1;
            return buttonIntTag;
        }


    }

}
