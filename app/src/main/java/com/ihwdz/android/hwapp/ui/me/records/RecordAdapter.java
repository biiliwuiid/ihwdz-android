package com.ihwdz.android.hwapp.ui.me.records;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
 * time : 2018/09/14
 * desc :   交易记录
 * version: 1.0
 * </pre>
 */
public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static String TAG = "RecordAdapter";
    Context mContext;
    List<DealRecordData.RecordBean> mData;
    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;

    int textColor;
    String currencySign;
    static LinearLayoutManager layoutManager;
    static RecordDetailAdapter adapter;

    private OnItemClickListener mListener;


    @Inject
    public RecordAdapter(Context context){
        this.mContext = context;
        currencySign = mContext.getResources().getString(R.string.currency_sign);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG, "==== onCreateViewHolder :  viewType: "+ viewType);
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }
        v = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Log.e(TAG, "===========      ============  onBindViewHolder position: " + position);
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            //Log.d(TAG, "==== onBindViewHolder :  FOOTER /position: "+ position);
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            final DealRecordData.RecordBean model = mData.get(position);

            if (model.orderItems != null && model.orderItems.size() > 0){
                adapter = new RecordDetailAdapter(mContext);
                viewHolder.recyclerView.setAdapter(adapter);
                adapter.setDataList(model.orderItems);
            }

            viewHolder.sumTv.setText(currencySign + model.saleSumAmt);

            final String status = getTradingStatus(model.subStatus);
            // Log.e(TAG, "STATUS: " + status);
            // Log.e(TAG, "textColor: " + textColor);
            viewHolder.statusTv.setTextColor(textColor);
            viewHolder.statusTv.setText(status);

            viewHolder.checkBox.setClickable(model.subStatus.equals("100"));
            viewHolder.checkBox.setChecked(model.subStatus.equals("100"));
            if (model.subStatus.equals("100")){
                // enable
                // Log.e(TAG, "===========    viewHolder.checkBox   ============  enable - model.orderId: " + model.orderId );
                viewHolder.checkBox.setChecked(true);
                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemChecked(model.saleSumAmt, model.orderId);
                    }
                });
            }else {
                // disable
                // Log.e(TAG, "===========    viewHolder.checkBox   ============  disable! " );
            }



        }
    }

    private String getTradingStatus(String subStatus) {
        int status = Integer.parseInt(subStatus);
        // Log.e(TAG, "getTradingStatus : -------- subStatus: "+ subStatus);
        // Log.e(TAG, "getTradingStatus : -------- status: "+ status);
        String result;
        textColor = mContext.getResources().getColor(R.color.blackText2);
        switch (status){
            case 10:
                result = mContext.getResources().getString(R.string.status10_trade_records);
                break;
            case 11:
                result = mContext.getResources().getString(R.string.status11_trade_records);
                break;
            case 20:
                result = mContext.getResources().getString(R.string.status20_trade_records);
                break;
            case 30:
                result = mContext.getResources().getString(R.string.status30_trade_records);
                break;
            case 40:
                result = mContext.getResources().getString(R.string.status40_trade_records);
                break;
            case 50:
                result = mContext.getResources().getString(R.string.status50_trade_records);
                break;
            case 90:
                result = mContext.getResources().getString(R.string.status90_trade_records);
                textColor = mContext.getResources().getColor(R.color.scroll_words_red);
                break;
            case 100:
                result = mContext.getResources().getString(R.string.status100_trade_records);
                break;
            case 101:
                result = mContext.getResources().getString(R.string.status101_trade_records);
                break;
            case 102:
                result = mContext.getResources().getString(R.string.status102_trade_records);
                textColor = mContext.getResources().getColor(R.color.scroll_words_green);
                break;
            default:
                result = mContext.getResources().getString(R.string.status_trade_records);
                break;
        }
        return result;
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

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.tv_status)
        TextView statusTv;
        @BindView(R.id.checkbox)
        CheckBox checkBox;
        @BindView(R.id.tv_sum)
        TextView sumTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            // Log.e(TAG, "===========      ============  ViewHolder ");
//            adapter = new RecordDetailAdapter(itemView.getContext());
//            recyclerView.setAdapter(adapter);
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

    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setDataList(List<DealRecordData.RecordBean> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<DealRecordData.RecordBean> dataList){
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
}
