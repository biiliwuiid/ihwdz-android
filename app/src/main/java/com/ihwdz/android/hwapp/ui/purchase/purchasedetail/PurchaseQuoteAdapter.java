package com.ihwdz.android.hwapp.ui.purchase.purchasedetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PurchaseData;
import com.ihwdz.android.hwapp.model.bean.PurchaseQuoteData;
import com.ihwdz.android.hwapp.ui.purchase.OnPurchaseItemClickListener;
import com.ihwdz.android.hwapp.ui.purchase.OnQuoteBtClickListener;
import com.ihwdz.android.hwapp.ui.purchase.OnReviewBtClickListener;
import com.ihwdz.android.hwapp.ui.purchase.PurchaseAdapter;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.EventListener;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :
 * version: 1.0
 * </pre>
 */
public class PurchaseQuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "PurchaseQuoteAdapter";
    Context mContext;
    List<PurchaseQuoteData.QuoteEntity> mData;

    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;

    private String businessName, purchasePrice, logisticFee, remarks; // 商家，采购报价，物流费， 复议备注

    private String statusStr;                // 订单状态

    private boolean isOrderBtShow = false;   // 是否显示 “一键下单”按钮
    private boolean isReviewBtShow = false;  // 是否显示 “价格复议”按钮 - 待确认状态时
    private boolean isReviewShow = false;    // 是否显示 “复议备注” - reviewNote 有值则显示

    private OnReviewClickListener mReviewClickListener;// 价格复议 button 监听
    private OnOrderClickListener mOrderClickListener;  // 一键下单 button 监听

    @Inject
    public PurchaseQuoteAdapter(Context context){
        this.mContext = context;

        businessName = mContext.getResources().getString(R.string.business_name);
        purchasePrice = mContext.getResources().getString(R.string.purchase_price);
        logisticFee = mContext.getResources().getString(R.string.logistic_fee);
        remarks = mContext.getResources().getString(R.string.remarks);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }
        v = LayoutInflater.from(mContext).inflate(R.layout.item_purchase_quote, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final PurchaseQuoteData.QuoteEntity model = mData.get(position);
            viewHolder.businessName.setText(String.format( businessName, model.sellMemberName));
            viewHolder.purchasePrice.setText(String.format(purchasePrice, model.price));
            viewHolder.logisticFee.setText(String.format(logisticFee, model.logisticsAmt));
            if (model.reviewNote != null && model.reviewNote.length()>0){
                viewHolder.reviewNoteTv.setVisibility(View.VISIBLE);
                viewHolder.reviewNoteTv.setText(String.format(remarks, model.reviewNote));
            }else {
                viewHolder.reviewNoteTv.setVisibility(View.GONE);
            }


            String orderStatusStr = getOrderStatus(model.status);
            // 价格复议
            if (isReviewBtShow){
                LogUtils.printCloseableInfo(TAG, "isReviewBtShow: " + isReviewBtShow);
                viewHolder.reviewLinear.setVisibility(View.VISIBLE);
                viewHolder.reviewBt.setVisibility(View.VISIBLE);
                viewHolder.reviewBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReviewClickListener.onReviewClicked(model.sellMemberQuoteId);
                    }
                });
            }else {
                LogUtils.printCloseableInfo(TAG, "isReviewBtShow: " + isReviewBtShow);
                viewHolder.reviewLinear.setVisibility(View.GONE);
            }
            // 一键下单
            viewHolder.orderStatus.setText(orderStatusStr);
            if (isOrderBtShow){
                LogUtils.printCloseableInfo(TAG, "isOrderBtShow: " + isOrderBtShow);
                viewHolder.orderBt.setVisibility(View.VISIBLE);
                viewHolder.orderBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOrderClickListener.onOrderClicked(model.sellMemberQuoteId);
                    }
                });
            }else {
                LogUtils.printCloseableInfo(TAG, "isOrderBtShow: " + isOrderBtShow);
                viewHolder.orderBt.setVisibility(View.GONE);
            }
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

        @BindView(R.id.linear_purchase) LinearLayout linearLayout;

        @BindView(R.id.tv_business_name) TextView businessName;   // 商家名称
        @BindView(R.id.tv_purchase_price) TextView purchasePrice; // 采购报价
        @BindView(R.id.tv_logistic_fee) TextView logisticFee;     // 物流费
        @BindView(R.id.tv_remarks) TextView reviewNoteTv;         // 复议备注

        @BindView(R.id.linear_review) LinearLayout reviewLinear;// 价格复议
        @BindView(R.id.tv_review_bt) TextView reviewBt;         // 价格复议 按钮
        @BindView(R.id.tv_status_order) TextView orderStatus;   // 订单状态
        @BindView(R.id.tv_order_bt) TextView orderBt;           // 一键下单 按钮


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

    public void setDataList(List<PurchaseQuoteData.QuoteEntity> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<PurchaseQuoteData.QuoteEntity> dataList){
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


    public void setOnOrderClickListener(OnOrderClickListener listener){
        this.mOrderClickListener = listener;
    }
    public void setOnReviewClickListener(OnReviewClickListener listener){
        this.mReviewClickListener = listener;
    }


    // 0-待物流确认(不显示) 1-待买家确认 10-待卖家复议 20-待二次确认 88-已确认报价（已下单） 99-失效 (交易会员 求购报价 详情)
    private String getOrderStatus(int status){
        String orderStatus = "";
        isReviewBtShow = false;
        switch (status){
            case 0:  // 待物流确认
                orderStatus = mContext.getResources().getString(R.string.unconfirmed);
                isOrderBtShow = false;
                break;
            case 1:  // 待买家确认
                orderStatus = mContext.getResources().getString(R.string.unconfirmed);
                isReviewBtShow = true;
                isOrderBtShow = true;
                break;
            case 10: // 待复议
                orderStatus = mContext.getResources().getString(R.string.reconsideration);
                isOrderBtShow = true;
                break;
            case 20: // 待二次确认
                orderStatus = mContext.getResources().getString(R.string.unconfirmed2);
                isOrderBtShow = true;
                break;
            case 88: // 已下单
                orderStatus = mContext.getResources().getString(R.string.ordered);
                isOrderBtShow = false;
                break;
            case 99: // 失效
                orderStatus = mContext.getResources().getString(R.string.invalid);
                isOrderBtShow = false;
                break;
            default:
                isOrderBtShow = false;
                break;
        }
        return orderStatus;
    }

    // 商家 我的报价: 订单状态  0-待物流确认(不显示)
    // 1-待买家确认 10-待复议 20-待二次确认 88-已确认报价（已下单） 99-失效 (交易会员 求购报价 详情)
//    private String getOrderStatus(String status){
//        String orderStatus = "";
//        if (status != null && status.length() > 0){
//            switch (Integer.parseInt(status)){
//                case 0:  // 待物流确认
//                    orderStatus = mContext.getResources().getString(R.string.unconfirmed);
//                    break;
//                case 1:  // 待买家确认
//                    orderStatus = mContext.getResources().getString(R.string.unconfirmed);
//                    break;
//                case 10: // 待复议
//                    orderStatus = mContext.getResources().getString(R.string.reconsideration);
//                    isReviewBtShow = true;
//                    break;
//                case 20: // 待二次确认
//                    orderStatus = mContext.getResources().getString(R.string.unconfirmed2);
//                    break;
//                case 88: // 已下单
//                    orderStatus = mContext.getResources().getString(R.string.ordered);
//                    break;
//                case 99: // 失效
//                    orderStatus = mContext.getResources().getString(R.string.invalid);
//                    break;
//                default:
//                    break;
//            }
//        }
//        return orderStatus;
//    }


}
