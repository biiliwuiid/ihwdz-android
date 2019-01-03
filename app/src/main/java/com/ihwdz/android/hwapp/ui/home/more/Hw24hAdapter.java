package com.ihwdz.android.hwapp.ui.home.more;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class Hw24hAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "Hw24hAdapter";
    private Context mContext;
    private List<NewsData.NewsModel> mData;

    int FOOTER = -1;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;

    @Inject
    public Hw24hAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            Log.d(TAG, "==== onBindViewHolder :  FOOTER /position: "+ position);
            bindFooterItem(holder);
        }
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            NewsData.NewsModel model = mData.get(position);
            viewHolder.title.setText(model.getTitle());

            Log.d(TAG, "=========== onBindViewHolder :  position: "+ position + "| tag: "+ model.getKeywords());
            //
            String[] strs = model.getKeywords().split(",");
            if (model.getKeywords().contains(",")){
                strs = model.getKeywords().split(",");
            }
            if (strs.length >= 1 && strs[0] != null){
                viewHolder.name1.setText(strs[0]);
            }
            if (strs.length >= 2 && strs[1] != null){
                viewHolder.name2.setVisibility(View.VISIBLE);
                viewHolder.name2.setText(strs[1]);
            }
            if (strs.length >= 3 && strs[2] != null){
                viewHolder.name3.setVisibility(View.VISIBLE);
                viewHolder.name3.setText(strs[2]);
            }

            viewHolder.viewTimes.setText(model.getViewTimes());
            viewHolder.author.setText(model.getAuthor());
            viewHolder.date.setText(model.getShorDate());
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(v.getContext(), "hw24h item clicked");
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

        @BindView(R.id.layout_item_hw24h)
        LinearLayout linearLayout;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.name1)
        TextView name1;
        @BindView(R.id.name2)
        TextView name2;
        @BindView(R.id.name3)
        TextView name3;
        @BindView(R.id.viewTimes)
        TextView viewTimes;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.showTimer)
        TextView date;

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

    public List<NewsData.NewsModel> getData(){
        return mData;
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
