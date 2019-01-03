package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ihwdz.android.hwapp.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class WaterfallAdapter extends RecyclerView.Adapter{

    String TAG = "WaterfallAdapter";
    Context mContext;
    private List<String> mData;
    OnItemCheckListener mListener;

    @Inject
    public WaterfallAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_waterfall, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            final String model = mData.get(position);
            viewHolder.checkBox.setText(model);

            // 默认选中首选项
//            if (position == 0){
//                viewHolder.checkBox.setChecked(true);
//                if (mListener != null){
//                    mListener.onItemCheckListener(model);
//                }else {
//                    Log.e(TAG, "=================== mListener == null");
//                }
//            }

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.d("ApplyProgress", "on check box clicked");
                    if (viewHolder.checkBox.isChecked()){  // 选中
                        if (mListener != null){
                            mListener.onItemCheckListener(model);
                        }

                    }else {
                        if (mListener != null){           // 取消
                            mListener.onItemCancelListener(model);
                        }

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void addItemCheckListener(OnItemCheckListener listener){
        Log.d(TAG, "================= addItemCheckListener");
        this.mListener = listener;
        notifyDataSetChanged();
    }

    public void setDataList(List<String> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
