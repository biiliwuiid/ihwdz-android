package com.ihwdz.android.hwapp.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.HomePageData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/31
 * desc : 情绪指数－走马灯　适配器
 * version: 1.0
 * </pre>
 */
public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> implements View.OnClickListener{

    String TAG = "IndexAdapter";
    private Context mContext;
    private List<HomePageData.IndexModel> mData;
    private OnItemClickListener onItemClickListener;

    public IndexAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_marquee, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData == null || mData.size() == 0){
            return;
        }
        int newPos = position % mData.size() ;
        HomePageData.IndexModel model = mData.get(newPos);
        holder.breed.setText(model.breed);
        holder.price.setText(model.priceIndex);

        String str = model.upDown;

//        float upDownF = new Float(str);
//        upDownF = (float)(Math.round(upDownF * 10))/10; //　保留一位小数
        int upDownF = new Integer(str);

        //Log.d(TAG, "====== upDown string: " + str);
        //Log.d(TAG, "====== upDownF: " + upDownF);

        if (upDownF > 0){
            holder.indexTag.setImageResource(R.drawable.up_red);
            holder.price.setTextColor(mContext.getResources().getColor(R.color.scroll_words_red));
            holder.upDown.setTextColor(mContext.getResources().getColor(R.color.scroll_words_red));
        }
        else if (upDownF == 0){
            holder.indexTag.setVisibility(View.GONE);
            holder.price.setTextColor(mContext.getResources().getColor(R.color.scroll_words_black));
            holder.upDown.setTextColor(mContext.getResources().getColor(R.color.scroll_words_black));
        }
        else if (upDownF < 0){
            holder.indexTag.setImageResource(R.drawable.down_green);
            holder.price.setTextColor(mContext.getResources().getColor(R.color.scroll_words_green));
            holder.upDown.setTextColor(mContext.getResources().getColor(R.color.scroll_words_green));

        }

        //holder.upDown.setText(String.valueOf(Math.abs(upDownF)));
        holder.upDown.setText(String.valueOf(upDownF)+"%");
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.breed)
        TextView breed;
        @BindView(R.id.priceIndex)
        TextView price;
        @BindView(R.id.upDown)
        TextView upDown;
        @BindView(R.id.indexTag)
        ImageView indexTag;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;

    }
    interface OnItemClickListener{
        void onItemClick(View view, int tag);
    }

    public void setDataList(List<HomePageData.IndexModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
