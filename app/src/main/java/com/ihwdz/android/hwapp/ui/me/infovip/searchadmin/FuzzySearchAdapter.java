package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.AdminData;
import com.tuacy.fuzzysearchlibrary.FuzzySearchBaseAdapter;
import com.tuacy.fuzzysearchlibrary.IFuzzySearchRule;

import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :  模糊搜索适配器
 * version: 1.0
 * </pre>
 */
public class FuzzySearchAdapter extends FuzzySearchBaseAdapter<ItemEntity, FuzzySearchAdapter.ItemHolder> {

    private Context mContext;
    OnItemClickListener mListener;

    @Inject
    public FuzzySearchAdapter(Context context) {
        super(null);
        this.mContext = context;
    }

    public FuzzySearchAdapter(IFuzzySearchRule rule) {
        super(rule);
    }

    public FuzzySearchAdapter(List<ItemEntity> dataList) {
        super(null, dataList);
    }

    public FuzzySearchAdapter(IFuzzySearchRule rule, List<ItemEntity> dataList) {
        super(rule, dataList);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FuzzySearchAdapter.ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final AdminData.AdminEntity model = mDataList.get(position).getData();
        holder.mTextName.setText(model.name);
        holder.mTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(model);
                //Toast.makeText(mContext, "onFuzzySearchItem clicked: " +  model.name+ " id: "+ model.id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        TextView mTextName;

        ItemHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.tv);
        }
    }
    public void addItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
