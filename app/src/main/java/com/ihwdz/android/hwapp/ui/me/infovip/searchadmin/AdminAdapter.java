package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.ihwdz.android.hwapp.model.bean.LogisticsCityData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :
 * version: 1.0
 * </pre>
 */
public class AdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    String TAG = "AdminAdapter";
    Context mContext;
    private List<AdminData.AdminEntity> mData;
    int color = 0;
    int normalColor = 0;
    Drawable selectedBackground;
    Drawable normalBackground;
    OnItemClickListener mListener;

    @Inject
    public AdminAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AdminData.AdminEntity model = mData.get(position);
        final String name = model.name;

        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv.setText(name);
            viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "========= Clicked : "+ name);

//                    if (model.isSelected() == false){
//                        model.setSelected(true);
//                    }
                    mListener.onItemClicked(model);
                    notifyDataSetChanged();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.linear)
        LinearLayout linear;
        @BindView(R.id.tv)
        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setDataList(List<AdminData.AdminEntity> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
