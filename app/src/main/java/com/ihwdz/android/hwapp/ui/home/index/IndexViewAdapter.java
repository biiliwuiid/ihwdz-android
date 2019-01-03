package com.ihwdz.android.hwapp.ui.home.index;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ClientData;
import com.ihwdz.android.hwapp.model.bean.IndexData;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class IndexViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "IndexViewAdapter";
    Context mContext;
    List< IndexData.IndexModel> mData;

    int redWords, greenWords, blackWords;


    @Inject
    public IndexViewAdapter (Context context){
        this.mContext = context;
        redWords = mContext.getResources().getColor(R.color.scroll_words_red);
        greenWords = mContext.getResources().getColor(R.color.scroll_words_green);
        blackWords = mContext.getResources().getColor(R.color.blackText);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_index_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final IndexData.IndexModel model = mData.get(position);
        String str = model.getUpDown();
        double upDown = Double.parseDouble(str.trim());
        //int upDown = new Integer(str);

        final String title = model.getBreed();
        final String technology = model.getTechnology();
        final String type = model.getType();

        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(model.getBreed());


            viewHolder.newest.setText(model.getPrice()); // the newest price
            viewHolder.upDown.setText(model.getUpDown());
            if (upDown > 0){
                // red
                viewHolder.upDown.setTextColor(redWords);
                viewHolder.newest.setTextColor(redWords);
            }else if (upDown == 0){
                viewHolder.upDown.setTextColor(blackWords);
                viewHolder.newest.setTextColor(blackWords);
            }else {
                viewHolder.upDown.setTextColor(greenWords);
                viewHolder.newest.setTextColor(greenWords);
            }
            viewHolder.date.setText(model.getDateTimeStr().substring(5));

            String baseId = "";
            viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IndexWebActivity.startIndexWebActivity(mContext, title, technology, type,  model.getBaseId());
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
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.newest)
        TextView newest;
        @BindView(R.id.up_down)
        TextView upDown;
        @BindView(R.id.date)
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setDataList(List< IndexData.IndexModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void addDataList(List< IndexData.IndexModel> dataList){
        mData.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
