package com.ihwdz.android.hwapp.ui.me.collections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.ui.home.detail.NewsDetailActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/14
 * desc :
 * version: 1.0
 * </pre>
 */
public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "CollectionAdapter";
    Context mContext;
    List<NewsData.NewsModel> mData;
    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;

    @Inject
    public CollectionAdapter(Context context){
        this.mContext = context;
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
        v = LayoutInflater.from(mContext).inflate(R.layout.item_home_hw24h, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            //Log.d(TAG, "==== onBindViewHolder :  FOOTER /position: "+ position);
            bindFooterItem(holder);
        }
        if (holder instanceof  ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final NewsData.NewsModel model = mData.get(position);

            viewHolder.title.setText(model.getTitle());
            viewHolder.viewTimes.setText(model.getViewTimes());
            viewHolder.author.setText(model.getAuthor());
            viewHolder.showTimer.setText(model.getShorDate());
            viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsDetailActivity.startNewsDetailActivity(mContext, model.getId());
                    //Toast.makeText(mContext, "onItemClicked", Toast.LENGTH_SHORT).show();
                }
            });
            String[] strs;
            if (model.getKeywords().contains(",")){
                strs = model.getKeywords().split(",");
                Log.e(TAG, "strs.length: "+ strs.length);

                if (strs.length >= 1 && strs[0] != null){
                    viewHolder.tag1.setVisibility(View.VISIBLE);
                    viewHolder.tag1.setText(strs[0]);
                }
                if (strs.length >= 2 && strs[1] != null){
                    viewHolder.tag2.setVisibility(View.VISIBLE);
                    viewHolder.tag2.setText(strs[1]);
                }
                if (strs.length >= 3 && strs[2] != null){
                    viewHolder.tag3.setVisibility(View.VISIBLE);
                    viewHolder.tag3.setText(strs[2]);
                }
            }else {
                if (model.getKeywords().length() > 0){
                    viewHolder.tag1.setVisibility(View.VISIBLE);
                    viewHolder.tag1.setText(model.getKeywords());
                }else {
                    // model.getKeywords() = "";
                }
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

        @BindView(R.id.layout_item_hw24h)
        LinearLayout linear;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.name1)
        TextView tag1;
        @BindView(R.id.name2)
        TextView tag2;
        @BindView(R.id.name3)
        TextView tag3;
        @BindView(R.id.viewTimes)
        TextView viewTimes;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.showTimer)
        TextView showTimer;

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
        LogUtils.printCloseableInfo(TAG, "bindFooterItem: "+ mLoadMoreStatus);
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
                break;
        }
    }

    public void setDataList(List<NewsData.NewsModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<NewsData.NewsModel> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        if (mData != null){
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
        LogUtils.printCloseableInfo(TAG, "setLoadMoreStatus: " + mLoadMoreStatus);
        notifyItemChanged(getItemCount() - 1);
    }

    // scroll to load set true; load complete set false
    public void setIsLoadMore( boolean isLoadMore) {
        this.mIsLoadMore = isLoadMore;
        notifyDataSetChanged();
    }
}
