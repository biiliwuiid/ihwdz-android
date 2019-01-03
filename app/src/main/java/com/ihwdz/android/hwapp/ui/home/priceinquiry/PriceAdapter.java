package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.FactoryPriceData;
import com.ihwdz.android.hwapp.model.bean.MarketPriceData;
import com.ihwdz.android.hwapp.model.bean.MarketPriceData.MarketPriceModel;
import com.ihwdz.android.hwapp.model.bean.PriceData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.tuacy.fuzzysearchlibrary.IFuzzySearchItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :   查价格
 * version: 1.0
 * </pre>
 */
public class PriceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "PriceAdapter";
    private Context mContext;
    private int currentPriceType = Constant.priceType.typeMarket;
    private PriceModel mData;
    private List<PriceData.PriceModel> mData1;
    private String price;

    private Drawable blackStarDrawable;   // 未收藏 star
    private Drawable orangeStarDrawable;  // 已收藏 star

    private int blackText;      // updown text color == 0
    private int greenText;      // < 0
    private int redText;        // > 0

    private OnPriceItemClickListener mListener;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;
    int FOOTER = -1;

    @Inject
    PriceAdapter(Context context){
        this.mContext = context;
        if (mData == null){
            mData = new PriceModel();
        }
        price = mContext.getResources().getString(R.string.price);
        blackStarDrawable = mContext.getResources().getDrawable(R.drawable.collection_black);
        orangeStarDrawable = mContext.getResources().getDrawable(R.drawable.star);

        blackText = mContext.getResources().getColor(R.color.blackText);
        greenText = mContext.getResources().getColor(R.color.scroll_words_green);
        redText = mContext.getResources().getColor(R.color.scroll_words_red);
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_price, parent, false);
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
            viewHolder.spec.getPaint().setFakeBoldText(true);

            final PriceData.PriceModel model = mData1.get(position);

            int priceType = 0;  // 0 市场价 1 出厂价
            String area = "";

            viewHolder.spec.setText( model.spec);
            viewHolder.brand.setText(model.brand);
            viewHolder.price.setText(String.format(price, model.price));

            if (model.wareArea != null && model.wareArea.length()>0){
                priceType = 0;
                area = model.wareCity;
                viewHolder.area.setText(model.wareCity);
            }

            if (model.region != null && model.region.length()>0){
                priceType = 1;
                area = model.region;
                viewHolder.area.setText(model.region);
            }

            viewHolder.up_down.setTextColor(getUpDownColor(model.upDown));
            viewHolder.up_down.setText(model.upDown);

            // "0":未收藏 "1"收藏;  只允许收藏 不允许取消收藏
            LogUtils.printCloseableInfo(TAG, "model.collection: " + model.collection);

            if (TextUtils.equals("0", model.collection)){
                // viewHolder.starCheck.setChecked(false);
                viewHolder.starIv.setImageDrawable(blackStarDrawable);
            }else if (TextUtils.equals("1", model.collection)){
                // viewHolder.starCheck.setChecked(true);
                viewHolder.starIv.setImageDrawable(orangeStarDrawable);
            }
            final int collectionType = priceType;
            LogUtils.printCloseableInfo(TAG, "collectionType: " + collectionType);
            final String collectionArea = area;

            // 只允许收藏 不允许取消收藏
            viewHolder.collectionLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.equals("0", model.collection)){       // 未收藏
                        // viewHolder.starIv.setImageDrawable(orangeStarDrawable);
                        mListener.onPriceStarClicked(collectionType, model.breed, model.spec, model.brand, collectionArea, true);

                    }else if (TextUtils.equals("1", model.collection)){ // 已收藏
                        // viewHolder.starIv.setImageDrawable(orangeStarDrawable);
                        mListener.onPriceStarClicked(collectionType, model.breed, model.spec, model.brand, collectionArea, false);

                    }
                }
            });


//            viewHolder.starCheck.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (viewHolder.starCheck.isChecked()){
//                        mListener.onPriceStarClicked(collectionType, model.breed, model.spec, model.brand, collectionArea, true);
//                    }else {
//                        viewHolder.starCheck.setChecked(true);
//                        mListener.onPriceStarClicked(collectionType, model.breed, model.spec, model.brand, collectionArea, false);
//                    }
//                }
//            });

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

//        int upDownColor = blackText;
//        double upDownD = 0d;
//        if (upDown != null && upDown.length()>0){
//            upDownD = Double.valueOf(upDown);
//        }
//        if (upDownD == 0){
//            upDownColor = blackText;
//        }else if (upDownD > 0){
//            upDownColor = redText;
//        }else {
//            upDownColor = greenText;
//        }
//
//        return upDownColor;
    }

    @Override
    public int getItemCount() {
        if (mIsLoadMore){
            return mData1 == null ? 0 : mData1.size() + 1;// add footer
        }else {
            return mData1 == null ? 0 : mData1.size();    // don't need footer
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

        @BindView(R.id.spec) TextView spec;
        @BindView(R.id.brand) TextView brand;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.area) TextView area;
        @BindView(R.id.up_down) TextView up_down;
        //@BindView(R.id.date) TextView date;
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

    public void setCurrentPriceType(int currentPriceType) {
        this.currentPriceType = currentPriceType;
    }

    public static class PriceModel{

        private List<MarketPriceData.MarketPriceModel> mMarketData;
        private List<FactoryPriceData.FactoryPriceModel> mFactoryData;

        public List<MarketPriceData.MarketPriceModel> getMarketData() {
            return mMarketData;
        }

        public void setMarketData(List<MarketPriceData.MarketPriceModel> marketData) {
            if (mMarketData == null){
                mMarketData = new ArrayList<>();
            }
            this.mMarketData.addAll(marketData);
        }

        public List<FactoryPriceData.FactoryPriceModel> getFactoryData() {
            return mFactoryData;
        }

        public void setFactoryData(List<FactoryPriceData.FactoryPriceModel> factoryData) {
            if (mFactoryData == null){
                mFactoryData = new ArrayList<>();
            }
            this.mFactoryData.addAll(factoryData);
        }

        void clearMarketData(){
            if (mMarketData != null){
                mMarketData.clear();
            }

        }
        void clearFactoryData(){
            if (mFactoryData!=null){
                mFactoryData.clear();
            }

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
    public void setDataList(List<PriceData.PriceModel> dataList){
        mData1 = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<PriceData.PriceModel> dataList){
        mData1.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

}
