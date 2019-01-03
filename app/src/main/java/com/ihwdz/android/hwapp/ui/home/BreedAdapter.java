package com.ihwdz.android.hwapp.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.BreedData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/28
 * desc :   首页关注 breed
 * version: 1.0
 * </pre>
 */
public class BreedAdapter extends RecyclerView.Adapter{

    String TAG = "BreedAdapter";
    Context mContext;
    private List<BreedData.CheckableItem> mData;
     // private OnItemCheckListener mListener;
    private List<String> mSelectedList = null;

    @Inject
    public BreedAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_waterfall_breed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            final BreedData.CheckableItem model = mData.get(position);
            viewHolder.checkBox.setText(model.name);

            if (mSelectedList != null && mSelectedList.size() > 0){
                LogUtils.printCloseableInfo(TAG, "mSelectedList.size："+ mSelectedList.size() + " : "+ mSelectedList.toString());
                if (mSelectedList.contains(model.name)){
                    LogUtils.printCloseableInfo(TAG, "mSelectedList.contains: " + model.name);
                    model.isChecked = true;
                }else {
                    model.isChecked = false;
                    LogUtils.printCloseableInfo(TAG, "mSelectedList.不包含: " + model.name);
                }
//                for (int i = 0; i < mSelectedList.size(); i++){
//                }

            }else {
                // 没有关注则全选
                LogUtils.printCloseableInfo(TAG, "mSelectedList == null ");
                model.isChecked = true;
            }

            if (model.isChecked){
                viewHolder.checkBox.setChecked(true);
            }else {
                viewHolder.checkBox.setChecked(false);
            }

            // checked 状态改变
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (viewHolder.checkBox.isChecked()){  // 选中 添加 +
                        LogUtils.printCloseableInfo(TAG, "add isChecked: " + viewHolder.checkBox.isChecked() + " : "+model.name);
                        addSelectedItem(model.name);
                    }else {                                // 取消选中 删除 -
                        LogUtils.printCloseableInfo(TAG, "delete isChecked: " + viewHolder.checkBox.isChecked()+ " : "+model.name);
                        if (!deleteSelectedItem(model.name)){
                            viewHolder.checkBox.setChecked(true);
                        }
                    }
                }
            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!model.isChecked){
//                        model.isChecked = true;
//                        addSelectedItem(model.name);
//                    }else {
//
//                    }
//                    if (mListener != null){
//                        mListener.onItemChecked(model.name);
//                    }
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

    public void setDataList(List<String> selectedList, List<BreedData.CheckableItem> dataList){
        mSelectedList = selectedList;
        LogUtils.printCloseableInfo(TAG, "setDataList: mSelectedList "+ mSelectedList.size() + " mSelectedList: "+mSelectedList.toString());
        mData = dataList;
        LogUtils.printCloseableInfo(TAG, "setDataList: mSelectedList "+ dataList.size() + " mSelectedList: "+ dataList.toString());

//        if (mSelectedList == null){
//            mSelectedList = new ArrayList<>();
//        }
//        mSelectedList.clear();
//        mSelectedList.addAll(selectedList);


//        if (mData != null){
//            mData.clear();
//            mData.addAll(dataList);
//        }else {
//            mData = dataList;
//        }
        notifyDataSetChanged();
    }

    public void addSelectedItem(String item){
        if (mSelectedList == null){
            mSelectedList = new ArrayList<>();
        }
        if (!mSelectedList.contains(item)){
            mSelectedList.add(item);
        }

    }
    public boolean deleteSelectedItem(String item){
        if (mSelectedList.size()>1){
            if (mSelectedList.contains(item)){
                mSelectedList.remove(item);
            }
            return true;
        }else {
            Toast.makeText(mContext, "至少选择一个选项", Toast.LENGTH_SHORT).show();
            return false;

//            if (mSelectedList.size()==1){
//
//                return false;
//            }else {
//                return true;     // 本地没有关注数据（默认全选）
//            }


        }

    }

    // 当前选择的breeds
    public List<String> getSelectedList(){
        return mSelectedList;
    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }

}
