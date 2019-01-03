package com.ihwdz.android.hwapp.ui.home.detail;

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
import com.ihwdz.android.hwapp.model.bean.CommentData;
import com.ihwdz.android.hwapp.model.bean.IndexData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/31
 * desc : 新闻详情－回复列表适配器
 * version: 1.0
 * </pre>
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    String TAG = "CommentAdapter";
    private Context mContext;
    private List<CommentData.NewsModel> mData;

    @Inject
    public CommentAdapter(Context context){
        this.mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "==== onCreateViewHolder :  viewType: "+ viewType);
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_detail_comment_reply, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentData.NewsModel model = mData.get(position);
        Log.d(TAG, "==== onBindViewHolder  :  model getContent: " + model.getContent());
        holder.userName.setText(model.getUseName()+ ": ");
        holder.content.setText(model.getContent());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView userName;
        @BindView(R.id.content)
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setDataList(List<CommentData.NewsModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<CommentData.NewsModel> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
