package com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.ClientDetailData;
import com.ihwdz.android.hwapp.ui.home.clientseek.filter.OnItemClickListener;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/26
 * desc :  产品设备展示 - 四列图片
 * version: 1.0
 * </pre>
 */
public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "ProductAdapter";
    Context mContext;
    private List<ClientDetailData.MemberAttachment> mData;
    private OnItemClickListener mListener;


    @Inject
    public ProductAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int width = parent.getWidth()/4;
        int height = width;
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_product_client_detail, parent, false);
        LinearLayout linearLayout = (LinearLayout) v;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        linearLayout.setLayoutParams(params);
        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final ClientDetailData.MemberAttachment model = mData.get(position);
            final String path = model.attachmentPath;
            ImageUtils.loadImgByPicasso(mContext, path, viewHolder.imageView);

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(path);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setDataList(List<ClientDetailData.MemberAttachment> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List<ClientDetailData.MemberAttachment> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
