package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.PriceData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/25
 * desc :  查价格 菜单栏 适配器（ spec; city; brand 栏目下的内容）
 * 三个菜单栏 用一个 adapter ，3 个 selectedItem： currentBreed...；
 * the same as CheckableAdapter in materialPurchase(买原料)
 *
 *
 * 首页 关注 breeds
 * version: 1.0
 * </pre>
 */
public class PriceMenuAdapter  extends RecyclerView.Adapter {

    String TAG = "PriceMenuAdapter";
    Context mContext;
    private List<PriceData.CheckableItem> mData;
    private OnItemCheckListener mListener;
    private String selectedItem = "";

    @Inject
    public PriceMenuAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_waterfall_material_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            final PriceData.CheckableItem model = mData.get(position);
            viewHolder.checkBox.setText(model.name);

            if (selectedItem != null && selectedItem.length() > 0){
                if (TextUtils.equals(selectedItem, model.name)){
                    model.isChecked = true;
                }
            }else {
                if (position == 0){
                    model.isChecked = true;
                }
            }


            if (model.isChecked){
                viewHolder.checkBox.setChecked(true);
            }else {
                viewHolder.checkBox.setChecked(false);
            }

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!model.isChecked){
                        model.isChecked = true;
                        setSelectedItem(model.name);

                    }
                    if (mListener != null){
                        mListener.onItemChecked(model.name);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
        this.mListener = listener;
        notifyDataSetChanged();
    }

    public void setDataList(String selectedItem, List<PriceData.CheckableItem> dataList){
        this.selectedItem = selectedItem;
        if (mData != null){
            mData.clear();
            mData.addAll(dataList);
        }else {
            mData = dataList;
        }
        notifyDataSetChanged();
    }

    public void setSelectedItem(String item){
        selectedItem = item;
    }

    public void clear(){
        if (mData != null){
            mData.clear();
        }
        notifyDataSetChanged();
    }

}
