package com.ihwdz.android.hwapp.ui.home.materialpurchase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.MaterialData;
import com.ihwdz.android.hwapp.ui.home.infoday.InfoDayAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.PUT;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class MaterialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "MaterialAdapter";
    Context mContext;
    List<MaterialData.MaterialModel> mData;

    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;
    String price;
    String address;
    String telephone;
    String amount;
    String brand;
    String date;
    String supplier;
    String negotiation; // 面议

    @Inject
    public MaterialAdapter(Context context) {
        this.mContext = context;
        price = mContext.getResources().getString(R.string.price_ma);
        address = mContext.getResources().getString(R.string.address_ma);
        telephone = mContext.getResources().getString(R.string.telephone_ma);
        amount = mContext.getResources().getString(R.string.amount_ma);
        brand = mContext.getResources().getString(R.string.manufacturer_ma);
        date = mContext.getResources().getString(R.string.date_ma);
        supplier = mContext.getResources().getString(R.string.supplier_ma);
        negotiation = supplier = mContext.getResources().getString(R.string.negotiation);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }
        v = LayoutInflater.from(mContext).inflate(R.layout.item_material, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            Log.d(TAG, "==== onBindViewHolder :  FOOTER /position: "+ position);
            bindFooterItem(holder);
        }

        if (holder instanceof ViewHolder){
            MaterialData.MaterialModel model = mData.get(position);
            String title = model.breed + "/" + model.spec;
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.getPaint().setFakeBoldText(true);
            viewHolder.title.setText(title);

            String modelPrice = "";
            if (model.price > 0){
                modelPrice += model.price;
            }else {
                modelPrice = negotiation; // 面议
            }
            viewHolder.price.setText(String.format(price, modelPrice));
            viewHolder.address.setText(String.format(address, model.wareCity));
            viewHolder.supplier.setText(String.format(supplier, model.supplier));

            String modelAmount = "";
            if (model.amount > 0){
                modelAmount += model.amount;
            }else {
                modelAmount = negotiation; // 面议
            }
            viewHolder.number.setText(String.format(amount, modelAmount));
            viewHolder.manufacturer.setText(String.format(brand, model.brand));
            viewHolder.date.setText(String.format(date, model.dateTimeStr));
            if (model.rank > 0){
                viewHolder.certificationIv.setVisibility(View.VISIBLE);
            }else {
                viewHolder.certificationIv.setVisibility(View.GONE);
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

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.telephone)
        TextView telephone;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.manufacturer)
        TextView manufacturer;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.supplier)
        TextView supplier;
        @BindView(R.id.certification_iv)
        ImageView certificationIv;


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


    public void setDataList( List<MaterialData.MaterialModel> dataList){
        if (mData != null){
            mData.clear();
        }
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList( List<MaterialData.MaterialModel> dataList){
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
