package com.ihwdz.android.hwapp.ui.home.more;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/30
 * desc :
 * version: 1.0
 * </pre>
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "NewsAdapter";
    private Context mContext;
    private List<NewsData.NewsModel> mData;

    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;

    @Inject
    public NewsAdapter(Context context){
        this.mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "==== onCreateViewHolder :  viewType: "+ viewType);
        View v;
        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }
        v = LayoutInflater.from(mContext).inflate(R.layout.item_home_news, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            Log.d(TAG, "==== onBindViewHolder :  FOOTER /position: "+ position);
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            NewsData.NewsModel model = mData.get(position);
            Log.d(TAG, "==== onBindViewHolder  : Normal item ");

            viewHolder.title.setText(model.getTitle());
            ImageUtils.loadImgByPicasso(mContext, model.getArticleImg(), viewHolder.imageView);
//            viewHolder.author.setText(model.getAuthor());
//            viewHolder.date.setText(model.getShorDate());
//            viewHolder.viewTimes.setText(model.getViewTimes());

            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(v.getContext(), "Bottom new item clicked");
                    // TODO: 2018/8/30 go to DetailActivity
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        @BindView(R.id.layout_item_news)
        LinearLayout linearLayout;
        @BindView(R.id.articleImg)
        ImageView imageView;
        @BindView(R.id.title)
        TextView title;
//        @BindView(R.id.viewTimes)
//        TextView viewTimes;
//        @BindView(R.id.author)
//        TextView author;
//        @BindView(R.id.date_news)
//        TextView date;

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

    public void setDataList(List<NewsData.NewsModel> dataList){
        Log.d(TAG, "=============setDataList=========" + dataList.size());
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<NewsData.NewsModel> dataList){
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

