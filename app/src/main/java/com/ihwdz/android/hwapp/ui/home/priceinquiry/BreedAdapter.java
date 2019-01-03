package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :
 * version: 1.0
 * </pre>
 */
public class BreedAdapter extends RecyclerView.Adapter<BreedAdapter.ViewHolder> {

    String TAG = "BreedAdapter";
    Context mContext;
    private List<String> mData;
    int color = 0;
    int normal = 0;
    OnItemClickListener mListener;
    private int selectedPosition = 0; //默认一个参数

    @Inject
    public BreedAdapter(Context context){
        this.mContext = context;
        color = mContext.getResources().getColor(R.color.orangeTab);
        normal = mContext.getResources().getColor(R.color.blackText3);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_price_breed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String model = mData.get(position);
        holder.breed.setText(model);

        holder.breed.setSelected(selectedPosition == position);

        if (selectedPosition == position){
            holder.breed.setTextColor(color);
        }else {
            holder.breed.setTextColor(normal);
        }

        // 点击该 item 移动到 first.
        holder.breed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;          // 选择的position赋值给参数,
                //notifyItemChanged(selectedPosition); //刷新当前点击item
                mListener.onItemClicked(model);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.breed)
        TextView breed;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setDataList(List<String> dataList){
        LogUtils.printInfo(TAG, "dataList: " + dataList.size());
        mData = dataList;
        notifyDataSetChanged();
    }
    public void clear(){
        if (mData != null){
            mData.clear();
        }
        notifyDataSetChanged();
    }


    static class Bean{
        String breed;
        boolean isSelected = false;
    }

}
