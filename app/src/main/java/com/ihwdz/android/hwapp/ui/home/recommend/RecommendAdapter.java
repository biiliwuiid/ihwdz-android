package com.ihwdz.android.hwapp.ui.home.recommend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.entity.RecommendModel;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/27
 * desc :
 * version: 1.0
 * </pre>
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private Context mContext;
    private List<RecommendModel> mData;

    public RecommendAdapter(Context context, List<RecommendModel> data){
        this.mContext = context;
        this.mData = data;
    }
    public RecommendAdapter(Context context){
        this.mContext = context;
    }

    @OnItemClick(R.id.recyclerView)
    public void onItemClick(int position){
        ToastUtil.showToast(mContext, "Recommend item clicked:" + position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int width = parent.getWidth()/3;
        int height = parent.getHeight();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_home_recommend, parent, false);
        LinearLayout linearLayout = (LinearLayout) v;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        linearLayout.setLayoutParams(params);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RecommendModel model = mData.get(position);
        holder.title.setText(model.getTitle());
//        Log.d("ImageUtil", "path :"+ model.getArticleImg());
        ImageUtils.loadImgByPicasso(mContext, model.getArticleImg(), holder.articleImg);
//        Picasso.with(mContext).load(Uri.parse(model.getArticleImg())).into(holder.articleImg);
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showToast(v.getContext(), "Recommend item clicked");
//            }
//        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        TextView title;
        ImageView articleImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linearLayout = itemView.findViewById(R.id.recommend_item);
            title = itemView.findViewById(R.id.title);
            articleImg = itemView.findViewById(R.id.articleImg);
        }
    }

    public void setDataList(List<RecommendModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

}
