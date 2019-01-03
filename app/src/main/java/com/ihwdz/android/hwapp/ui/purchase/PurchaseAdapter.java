package com.ihwdz.android.hwapp.ui.purchase;

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

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.PurchaseData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/08
 * desc :
 *  全部求购
 *  交易会员 - 我的求购 - item clicked - goto“求购报价”
 *  商家会员已认证 - 我的报价 - item clicked - goto“报价详情” ( 未登录用户 也展示 “我的报价”-> 登录页面)
 * version: 1.0
 * </pre>
 */
public class PurchaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "PurchaseAdapter";
    Context mContext;
    List<PurchaseData.PurchaseModel> mData;

    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;


    private String dataId, dataStatus, dataBreed, dataQty, dataAddress, dateStr;
    private String breed, amount, address, price, remarks; // 种类，采购量，配送地， 单价， 复议备注
    String purchaseAddress;  // 求购 - 地址
    String quoteAddress;     // 我的报价 - 地址

    private String quoteStr;          // 报价状态(商家报价数) -  交易会员 || 报价/已报价 -  商家会员
    //private String quoteButtonStr;          //

    private Drawable buttonBg = null; // black box 按钮背景
    private Drawable reviewBtBg = null; // black box 按钮背景
    private int  textColor; //

    private boolean isQuoteBtShow = false; // 是否可以点击报价
    private boolean isQuoteEnable = false; // 是否可以报价(商家未认证不能报价)
    private boolean isLocked = false;      // 是否锁定

    private boolean isReviewBtShow = false; // 是否显示 “复议报价”（商家会员 已认证 订单待复议状态时 true）
    private String reviewStr = "";          // “复议报价”

    private OnPurchaseItemClickListener mListener;
    private OnQuoteBtClickListener mQuoteListener;
    private OnReviewBtClickListener mReviewListener;
    private int mode = 0; // all data (0) or my data(1)


    @Inject
    public PurchaseAdapter(Context context){
        this.mContext = context;

        breed = mContext.getResources().getString(R.string.purchase_breed);
        amount = mContext.getResources().getString(R.string.purchase_amount);
        address = mContext.getResources().getString(R.string.address_distribution);
        price = mContext.getResources().getString(R.string.unit_price);
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
        v = LayoutInflater.from(mContext).inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final PurchaseData.PurchaseModel model = mData.get(position);
            // LogUtils.printCloseableInfo(TAG, "===================== 状态 STATUS: ===================== " + model.status);

            // 初始化 求购池 item
            initPurchasePoolItem(viewHolder, model);

            switch (Constant.VIP_TYPE){
                case -1:
                    // 普通用户
                    break;
                case 0:
                    // 资讯用户
                    break;
                case 1:
                    // 交易会员 (求购/ 我的求购)
                    initPurchaseItem(viewHolder, model);
                    break;
                case 2:
                    // 商家会员 (求购/ 我的报价)
                    initQuoteItem(viewHolder, model);
                    break;
                default:
                    initQuoteItem(viewHolder, model);
                    // 未登录
                    break;
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


    // 求购池 -item
    private void initPurchasePoolItem(ViewHolder viewHolder, final PurchaseData.PurchaseModel model){
        isReviewBtShow = false;
        isQuoteBtShow = false;
        isQuoteEnable = false;

        String breedStr = model.breed + " | " + model.spec + " | " + model.brand;

        viewHolder.breedTv.setText(String.format(breed, model.breed, model.spec, model.brand));
        viewHolder.amountTv.setText(String.format(amount, model.qty));
        purchaseAddress = model.provinceName + " "+ model.cityName + " "+ model.districtName;
        viewHolder.addressTv.setText(String.format(address, purchaseAddress));  //model.memberPurchaseAddress

        viewHolder.dateTv.setText(model.dateTimestr);

        viewHolder.unitPriceTv.setVisibility(View.GONE);      // 单价
        viewHolder.reviewNoteTv.setVisibility(View.GONE);     // 备注

        viewHolder.orderStatus.setVisibility(View.GONE);      // 订单状态
        viewHolder.quoteStatus.setVisibility(View.GONE);      // 报价状态
        viewHolder.enterIv.setVisibility(View.GONE);          // enter icon
    }

    // 交易会员 (求购/ 我的求购) -item
    private void initPurchaseItem(ViewHolder viewHolder, final PurchaseData.PurchaseModel model){
        if (mode == 0){
            viewHolder.enterIv.setVisibility(View.GONE);
            viewHolder.linearLayout.setClickable(false);
        }else if (mode == 1){
            viewHolder.linearLayout.setClickable(true);
            viewHolder.quoteStatus.setVisibility(View.VISIBLE);
            viewHolder.quoteStatus.setTextColor(getQuoteStatusColor(model.sellMemberQuoteCount));
            viewHolder.quoteStatus.setText(quoteStr);

            viewHolder.enterIv.setVisibility(View.VISIBLE);
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.printCloseableInfo(TAG, "点击 “我的求购详情”按钮");
                    if (mode == 1){
                        dataId = model.id;
                        dataStatus = "";
                        dataBreed = String.format(breed, model.breed, model.spec, model.brand);
                        dataQty = String.format(amount, model.qty);

                        dataAddress = String.format(address, purchaseAddress);
                        dateStr = model.dateTimestr;
                        mListener.onItemClicked( dataId, dataStatus, dataBreed, dataQty, dataAddress, dateStr, false);
                    }

                }
            });
        }
    }

    // 商家会员 (求购/ 我的报价) -item
    private void initQuoteItem(ViewHolder viewHolder, final PurchaseData.PurchaseModel model){
        getQuoteColor(model.status);  // 更新 报价按钮 数据
        if (mode == 0){
            dataId = model.id;
            // 报价按钮(black box) / 已报价（orange）
            viewHolder.quoteStatus.setVisibility(View.VISIBLE);
            viewHolder.quoteStatus.setTextColor(textColor);
            viewHolder.quoteStatus.setText(quoteStr);
            viewHolder.quoteStatus.setBackground(buttonBg);
            viewHolder.linearLayout.setClickable(false);
            if (isQuoteBtShow){
                // 点击 “报价”按钮
                viewHolder.quoteStatus.setClickable(true);
                viewHolder.quoteStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getQuoteEnable(model.status); // 点击时 判断能否报价

                        if (mQuoteListener != null){
                            dataId = model.id;
                            LogUtils.printCloseableInfo(TAG, "ID: "+ dataId);

                            dataBreed = String.format(breed, model.breed, model.spec, model.brand);
                            dataQty = String.format(amount, model.qty);   // “采购量：200.0吨”
                            dataQty =  model.qty;   // 200.0
                            dataAddress = String.format(address, purchaseAddress);

                            dateStr = model.dateTimestr;
                            mQuoteListener.onQuoteBtClicked(isQuoteEnable, dataId, dataStatus, dataBreed, dataQty, dataAddress, dateStr);
                        }
                    }
                });
            }else {
                //LogUtils.printCloseableInfo(TAG, "isQuoteBtShow: " +isQuoteBtShow);
                viewHolder.quoteStatus.setClickable(false);
            }

        }else if (mode == 1){

            /**
             * 我的报价 start
             */
            viewHolder.breedTv.setText(String.format(breed, model.breed, model.spec, model.factory));
            viewHolder.amountTv.setText(String.format(amount, model.qty));
            quoteAddress = model.memberPurchaseProvince + " "+ model.memberPurchaseCity + " "+ model.memberPurchaseDistrict;
            viewHolder.addressTv.setText(String.format(address, quoteAddress));  //model.memberPurchaseAddress
            // LogUtils.printCloseableInfo(TAG, "我的报价 =========== model.qty: " + model.qty);
            // 订单状态
            viewHolder.orderStatus.setVisibility(View.VISIBLE);
            viewHolder.orderStatus.setText(getOrderStatus(model.status));

            // 隐藏报价状态
            viewHolder.quoteStatus.setVisibility(View.GONE);


            // 复议报价 是否显示
            if (isReviewBtShow){
                viewHolder.dateTv.setVisibility(View.VISIBLE);
                viewHolder.dateTv.setClickable(true);
                viewHolder.dateTv.setText(reviewStr);
                viewHolder.dateTv.setBackground(reviewBtBg);
                // 点击 “复议报价”
                viewHolder.dateTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.printCloseableInfo(TAG, "点击 “复议报价”按钮");
                        if (mReviewListener != null){
                            dataId = model.sellMemberQuoteId;
                            mReviewListener.onReviewBtClicked(dataId, model.price);
                        }
                    }
                });
            }else {
                viewHolder.dateTv.setClickable(false);
                viewHolder.dateTv.setBackground(null);
            }


            // 单价
            viewHolder.unitPriceTv.setVisibility(View.VISIBLE);
            viewHolder.unitPriceTv.setText(String.format(price, model.price));


            // 备注
            if (model.reviewNote != null && model.reviewNote.length() > 0){
                viewHolder.reviewNoteTv.setVisibility(View.VISIBLE);
                viewHolder.reviewNoteTv.setText(String.format(remarks, model.reviewNote));
            }
            viewHolder.linearLayout.setClickable(true);
            // 我的报价 item click->  报价详情
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // LogUtils.printCloseableInfo(TAG, "点击 “我的报价详情”按钮 ==== model.qty: " + model.qty + " position: " +position+ " dataQty: " + dataQty);
                    if (mode == 1){
                        dataId = model.sellMemberQuoteId;

                        dataStatus = getOrderStatus(model.status);
                        dataBreed = String.format(breed, model.breed, model.spec, model.factory);
                        dataQty = String.format(amount, model.qty);   // “采购量：200.0吨”
                        dataQty =  model.qty;   // 200.0
                        dataAddress = String.format(address, quoteAddress);
                        dateStr = model.dateTimestr;
                        mListener.onItemClicked( dataId, dataStatus, dataBreed, dataQty, dataAddress, dateStr,true);
                    }

                }
            });

        }
        /**
         * 我的报价 end
         */
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear_purchase) LinearLayout linearLayout;

        @BindView(R.id.tv_breed) TextView breedTv;          // 种类
        @BindView(R.id.tv_amount) TextView amountTv;        // 采购量
        @BindView(R.id.tv_address) TextView addressTv;      // 配送地
        @BindView(R.id.tv_unit_price) TextView unitPriceTv; // 单价
        @BindView(R.id.tv_remarks) TextView reviewNoteTv;      // 复议备注

        @BindView(R.id.tv_status_order) TextView orderStatus;// 订单状态 ：（已认证商家会员 - 我的报价）待确认 - 待复议 - 待二次确认 - 已下单
        @BindView(R.id.tv_status_quote) TextView quoteStatus;// 报价状态 ： 已报价 报价按钮
        @BindView(R.id.tv_date) TextView dateTv;             // 时间/复议报价按钮

        @BindView(R.id.iv_enter) ImageView enterIv;          // 右箭头

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

    public void setDataList(List<PurchaseData.PurchaseModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<PurchaseData.PurchaseModel> dataList){
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


    public void setMode(int mode){
        if (this.mode != mode){
            this.mode = mode;
            LogUtils.printCloseableInfo(TAG, "MODE: " + mode);
        }
    }

    public void setOnPurchaseItemClickListener(OnPurchaseItemClickListener listener){
        this.mListener = listener;
    }
    public void setOnQuoteClickListener(OnQuoteBtClickListener listener){
        this.mQuoteListener = listener;
    }
    public void setOnReviewClickListener(OnReviewBtClickListener listener){
        this.mReviewListener = listener;
    }

    // 交易会员 我的求购 - 报价商家数: >0 green else: red
    private int getQuoteStatusColor(String num){
        int color = 0;
        //LogUtils.printCloseableInfo(TAG, "==================== 交易会员 报价状态 报价商家数 num: " + num);

        if (num != null && num.length() > 0){
            if (Integer.parseInt(num) > 0){
                color = mContext.getResources().getColor(R.color.scroll_words_green);
                quoteStr = mContext.getResources().getString(R.string.quote_count);
                quoteStr = String.format(quoteStr, num);

            }else {
                color = mContext.getResources().getColor(R.color.scroll_words_red);
                quoteStr = mContext.getResources().getString(R.string.quote_null);


            }
        }
        return color;
    }


    // 商家报价状态展示 every item ---商家会员  all data (0) : 报价button grey / 已报价text - orange

    /**
     *  -3 已报价 -2: 未登录; -1: 用户;  0: 资讯;
     *  1: 交易;  2: 商家;
     *  3: 交易未认证; 4: 商家未认证
     *  5: 交易失效;   6:商家失效（锁定）；
     * @param status
     * @return
     */
    private int getQuoteColor(String status) {
        LogUtils.printCloseableInfo(TAG, "Quote button status: " + status);
        int color = 0;
        if (status != null && status.length() > 0){
            isLocked = false;
            switch (Integer.parseInt(status)){
                case -3:// 商家已报价;   - 显示“已报价”
                    isQuoteBtShow = false;
                    color = mContext.getResources().getColor(R.color.orangeTab);
                    quoteStr = mContext.getResources().getString(R.string.quote_showed);
                    buttonBg = null;
                case -2: // 未登录 -  显示 “报价”按钮 点击-> 登录
                    isQuoteBtShow = true;
                    isQuoteEnable = true;
                    color = mContext.getResources().getColor(R.color.blackText);
                    quoteStr = mContext.getResources().getString(R.string.quote_button);
                    buttonBg = mContext.getResources().getDrawable(R.drawable.grey_ring_quote);
                    break;
                case -1: // 用户
                    break;
                case 0:  // 资讯
                    break;
                case 1:  // 交易
                    break;
                case 2:  // 商家;   - 显示 “报价”按钮
                    isQuoteBtShow = true;
                    isQuoteEnable = true;
                    color = mContext.getResources().getColor(R.color.blackText);
                    quoteStr = mContext.getResources().getString(R.string.quote_button);
                    buttonBg = mContext.getResources().getDrawable(R.drawable.grey_ring_quote);
                    break;
                case 3: // 交易未认证
                    break;
                case 4: // 商家未认证;   - 显示 “报价”按钮 - 点击提示：“商家未认证，不能进行报价”
                    isQuoteBtShow = true;
                    isQuoteEnable = false;
                    color = mContext.getResources().getColor(R.color.blackText);
                    quoteStr = mContext.getResources().getString(R.string.quote_button);
                    buttonBg = mContext.getResources().getDrawable(R.drawable.grey_ring_quote);
                    break;
                case 5: // 交易失效(锁定)
                    break;

                case 6:// 商家状（锁定）;   - 显示 “报价”按钮 - 点击提示：“锁定 - 联系客服”
                    isQuoteBtShow = true;
                    isQuoteEnable = false;
                    isLocked = true;
                    color = mContext.getResources().getColor(R.color.blackText);
                    quoteStr = mContext.getResources().getString(R.string.quote_button);
                    buttonBg = mContext.getResources().getDrawable(R.drawable.grey_ring_quote);
                    break;
                default:
                    // 只显示时间
                    break;
            }
        }
        LogUtils.printCloseableInfo(TAG, "Quote button status:  isQuoteBtShow " + isQuoteBtShow);
        LogUtils.printCloseableInfo(TAG, "Quote button status: isQuoteEnable " + isQuoteEnable);
        textColor = color;
        return color;
    }

    // 商家 报价按钮 能否点击
    private void getQuoteEnable(String status){
        if (status != null && status.length() > 0){
            switch (Integer.parseInt(status)){
                case 2: // 商家未报价;   - 显示 “报价”按钮
                    isQuoteBtShow = true;
                    isQuoteEnable = true;
                    break;
                case 3:
                    break;
                case 4: // 商家未认证;   - 显示 “报价”按钮 - 点击提示：“商家未认证，不能进行报价”
                    isQuoteBtShow = true;
                    isQuoteEnable = false;
                    break;
                case 6: // 商家锁定;   - 显示 “报价”按钮 - 点击提示：“锁定”
                    isQuoteBtShow = true;
                    isQuoteEnable = false;
                    break;
                default:
                    // 只显示时间
                    isQuoteEnable = false;
                    break;
            }
        }
    }


    //  商家 我的报价: 订单状态  0-待物流确认（买家不显示/买家显示)
    //  1-待买家确认() 10-待复议 20-待二次确认 88-已确认报价（显示已下单） 99-失效（失效） (交易会员 求购报价 详情)
    private String getOrderStatus(String status){
        String orderStatus = "";
        if (status != null && status.length() > 0){
            switch (Integer.parseInt(status)){
                case 0:  // 待物流确认
                    orderStatus = mContext.getResources().getString(R.string.unconfirmed_logistic);
                    break;
                case 1:  // 待买家确认-商家    ； 待确认 -交易会员
                    orderStatus = mContext.getResources().getString(R.string.unconfirmed);
                    break;
                case 10: // 待复议
                    orderStatus = mContext.getResources().getString(R.string.reconsideration);
                    isReviewBtShow = true;
                    reviewStr = mContext.getResources().getString(R.string.reconsider);
                    reviewBtBg = mContext.getResources().getDrawable(R.drawable.grey_ring);
                    break;
                case 20: // 待二次确认
                    orderStatus = mContext.getResources().getString(R.string.unconfirmed2);
                    break;
                case 88: // 已下单
                    orderStatus = mContext.getResources().getString(R.string.ordered);
                    break;
                case 99: // 失效
                    orderStatus = mContext.getResources().getString(R.string.invalid);
                    break;
                default:
                    break;
            }
        }
        return orderStatus;
        }


}



