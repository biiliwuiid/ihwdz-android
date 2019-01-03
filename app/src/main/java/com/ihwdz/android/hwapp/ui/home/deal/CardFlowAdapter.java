package com.ihwdz.android.hwapp.ui.home.deal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.HomePageData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/26
 * desc :
 * version: 1.0
 * </pre>
 */
public class CardFlowAdapter extends RecyclerView.Adapter<CardFlowAdapter.ViewHolder>{

    String TAG = "CardFlowAdapter";
    private Context mContext;
    private List<HomePageData.CardModel> mData;
    private int mCurrentPosition = -1;

    int mVisibility = View.VISIBLE;

    int mTagType =  R.drawable.up_red;
    int tagRed = R.drawable.up_red;
    int tagGreen = R.drawable.down_green;

    public CardFlowAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int window_width = dm.widthPixels;
        int width = window_width/2;
        int height = viewGroup.getHeight();

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_deal_card, viewGroup, false);

        LinearLayout linearLayout = (LinearLayout) v;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        linearLayout.setLayoutParams(params);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if (mData == null || mData.size() == 0){
            return;
        }
        HomePageData.CardModel data = mData.get(position % mData.size());
//        Log.d(TAG, "onBindViewHolder data position:　"+ position % mData.size());

        setCurrentPosition(position % mData.size());
        viewHolder.cardArea.setText(data.area);
        viewHolder.cardBreed.setText(data.breed);
        viewHolder.cardPrice.setText(data.price);
        viewHolder.cardSpec.setText(data.spec);
//        Log.d(TAG, "onBindViewHolder data CITY:=======================　"+ data.getArea());
        String dayRate = data.dayRate;
        String weekRate = data.weekRate;
//        Log.d(TAG, "dayRate :=======================　"+ data.getDayRate());
//        Log.d(TAG, "weekRate :=======================　"+ data.getWeekRate());

        viewHolder.cardDayRate.setText(changeToPercent(dayRate, "日"));
        viewHolder.dealDayImg.setVisibility(mVisibility);
        if (mVisibility != View.GONE){
            viewHolder.dealDayImg.setImageResource(mTagType);
        }


        viewHolder.cardWeekRate.setText(changeToPercent(weekRate, "周"));
        viewHolder.dealWeekImg.setVisibility(mVisibility);
        if (mVisibility != View.GONE){
            viewHolder.dealWeekImg.setImageResource(mTagType);
        }

        viewHolder.itemView.setTag(position);


    }

    private String changeToPercent(String input, String type){
        String outString = "";
        float dataF = new Float(input);
        if (dataF > 1){
            outString = type +""+ String.valueOf(dataF*100)+"%";
            return outString;
        }
        if (dataF > 0){
            mTagType = tagRed;
            mVisibility = View.VISIBLE;
        }else if (dataF == 0){
            mVisibility = View.GONE;
        }else if (dataF < 0){
            mTagType = tagGreen;
            mVisibility = View.VISIBLE;
        }
        float dataAbs0 = Math.abs(dataF);
        float dataAbs = (float)(Math.round(dataAbs0 * 10 * 100))/10; //　保留一位小数

        outString = type +""+ String.valueOf(dataAbs)+"%";
//        Log.d(TAG, "changeToPercent:　==================== outString:　"+ outString);
        return outString;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    private void setCurrentPosition(int position){
        this.mCurrentPosition = position;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout cardLayout;
        private TextView cardArea;
        private TextView cardBreed;
        private TextView cardPrice;
        private TextView cardDayRate;
        private TextView cardSpec;
        private TextView cardWeekRate;

        @BindView(R.id.dealTag_day)
        ImageView dealDayImg;
        @BindView(R.id.dealTag_week)
        ImageView dealWeekImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardLayout = itemView.findViewById(R.id.layout_item_deal_card);
            cardArea = itemView.findViewById(R.id.area);
            cardBreed = itemView.findViewById(R.id.breed);
            cardPrice = itemView.findViewById(R.id.price);
            cardDayRate = itemView.findViewById(R.id.day_rate);
            cardSpec = itemView.findViewById(R.id.spec);
            cardWeekRate = itemView.findViewById(R.id.week_week);
        }
    }

    public void setDataList(List<HomePageData.CardModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
