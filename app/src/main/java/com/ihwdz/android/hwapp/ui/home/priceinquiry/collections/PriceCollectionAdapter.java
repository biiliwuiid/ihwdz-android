package com.ihwdz.android.hwapp.ui.home.priceinquiry.collections;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PriceCollectionData;
import com.ihwdz.android.hwapp.model.bean.PriceData;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.OnPriceItemClickListener;
import com.ihwdz.android.hwapp.ui.home.priceinquiry.PriceAdapter;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/21
 * desc :
 * version: 1.0
 * </pre>
 */
public class PriceCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "PriceCollectionAdapter";
    private Context mContext;
    private List<PriceCollectionData.PriceModel> mData;
    private String price;

    private Drawable blackStarDrawable;   // 未收藏 star
    private Drawable orangeStarDrawable;  // 已收藏 star

    private int blackText;      // updown text color == 0
    private int greenText;      // < 0
    private int redText;        // > 0

    private Drawable marketPriceDrawable;   // 市场价
    private Drawable factoryPriceDrawable;  // 出厂价

    private OnPriceItemClickListener mListener;

    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;
    int FOOTER = -1;

    @Inject
    public PriceCollectionAdapter(Context context){
        this.mContext = context;
        price = mContext.getResources().getString(R.string.price);
        blackStarDrawable = mContext.getResources().getDrawable(R.drawable.collection_black);
        orangeStarDrawable = mContext.getResources().getDrawable(R.drawable.star);
        marketPriceDrawable = mContext.getResources().getDrawable(R.drawable.price_market);
        factoryPriceDrawable = mContext.getResources().getDrawable(R.drawable.price_factory);

        blackText = mContext.getResources().getColor(R.color.blackText);
        greenText = mContext.getResources().getColor(R.color.scroll_words_green);
        redText = mContext.getResources().getColor(R.color.scroll_words_red);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_price_collection, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;

            final PriceCollectionData.PriceModel model = mData.get(position);
            viewHolder.breed.setText( model.breed);
            viewHolder.spec.setText( model.spec);
            viewHolder.brand.setText(model.brand);
            viewHolder.price.setText(String.format(price, model.price));

            // model.collectionType   // 0 市场价 1 出厂价
            if (model.collectionType == 0){
                viewHolder.priceIv.setImageDrawable(marketPriceDrawable);
            }else if (model.collectionType == 1){
                viewHolder.priceIv.setImageDrawable(factoryPriceDrawable);
            }
            viewHolder.area.setText(model.area);

            viewHolder.up_down.setTextColor(getUpDownColor(model.upDown));
            viewHolder.up_down.setText(model.upDown);
            // viewHolder.starCheck.setChecked(true);
            viewHolder.starIv.setImageDrawable(orangeStarDrawable);

            // 我的收藏 点击取消收藏
            viewHolder.collectionLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPriceStarClicked(model.collectionType, model.breed, model.spec, model.brand, model.area, false);
//                    if (!viewHolder.starCheck.isChecked()){
//                        viewHolder.starCheck.setChecked(true);
//                    }
                }
            });

        }
    }

    // updown == 0 black; >0 red; <0 green;
    private int getUpDownColor(String upDown) {
        int upDownColor = blackText;
        double upDownD = 0d;
        try{
            upDownD = Double.valueOf(upDown);
            if (upDown != null && upDown.length()>0){
                upDownD = Double.valueOf(upDown);
            }
            if (upDownD == 0){
                upDownColor = blackText;
            }else if (upDownD > 0){
                upDownColor = redText;
            }else {
                upDownColor = greenText;
            }

        }catch (Exception e){
            upDownD = 0d;
            upDownColor = blackText;
        }

        return upDownColor;
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
        }else{
            return super.getItemViewType(position);
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

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.breed) TextView breed;
        @BindView(R.id.spec) TextView spec;
        @BindView(R.id.brand) TextView brand;
        @BindView(R.id.iv_price) ImageView priceIv;  // 价格标签（出/市）
        @BindView(R.id.price) TextView price;
        @BindView(R.id.area) TextView area;
        @BindView(R.id.up_down) TextView up_down;
        @BindView(R.id.linear_collection) LinearLayout collectionLinear; // 收藏
        @BindView(R.id.iv_collection) ImageView starIv;                  // 收藏星标
        // @BindView(R.id.checkbox_collection) CheckBox starCheck;          // 收藏星标

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


    public void setOnPriceItemClickListener(OnPriceItemClickListener listener){
        mListener = listener;
    }

    public void setDataList(List<PriceCollectionData.PriceModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<PriceCollectionData.PriceModel> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }




}
