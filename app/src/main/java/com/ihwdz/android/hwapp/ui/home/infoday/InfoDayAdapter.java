package com.ihwdz.android.hwapp.ui.home.infoday;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.InfoData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.CustomImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :  reference LoadMoreRecyclerView
 * version: 1.0
 * </pre>
 */
public class InfoDayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private String shareUrlRoot;

    String TAG = "InfoDayAdapter";
    Context mContext;
    private List<InfoData.NewsModel> mData;
    int FOOTER = -1;
    int HEADER = 1;
    int ITEM = 2;
    private boolean mIsLoadMore = true;
    protected int mLoadMoreStatus = Constant.loadStatus.STATUS_PREPARE;
    Drawable openBg;
    Drawable closeBg;

    @Inject
    public InfoDayAdapter(Context context){
        this.mContext = context;
        mData = new ArrayList<>();
        shareUrlRoot = mContext.getResources().getString(R.string.share_url);
        openBg = mContext.getResources().getDrawable(R.drawable.close);
        closeBg = mContext.getResources().getDrawable(R.drawable.open);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        if (viewType == FOOTER){
            v = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new FooterViewHolder(v);
        }
        if (viewType == HEADER){
             v = LayoutInflater.from(mContext).inflate(R.layout.item_info_day_header, parent, false);
             return new HeaderViewHolder(v);
        }else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_info_day, parent, false);
            return new ViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mIsLoadMore && getItemViewType(position) == FOOTER) {
            bindFooterItem(holder);
        }

        if(holder instanceof HeaderViewHolder){
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            viewHolder.header.getPaint().setFakeBoldText(true);
            viewHolder.header.setText(mData.get(position).getToday());

        }else if (holder instanceof ViewHolder){
            final InfoData.NewsModel model = mData.get(position);
            final String shareMsg = model.getContent();
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.getPaint().setFakeBoldText(true);

            viewHolder.time.setText(model.getShorTime());
            viewHolder.title.setText(model.getTitle());

            viewHolder.foldTag.setClickable(true);
            viewHolder.foldTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setOpen(!(model.isOpen()));
                    notifyDataSetChanged();
                }
            });

            if (model.isOpen()){  // open
                viewHolder.content.setVisibility(View.VISIBLE);
//                String contentStr = model.getContent();
//                String[] strs = contentStr.split("\\\">");
//                Spanned spanned = Html.fromHtml(model.getContent());

                // viewHolder.content.setText(getStringFromHtml(model.getContent()));
                viewHolder.content.setText(removeHtmlTag(model.getContent()));

                viewHolder.foldTag.setBackground(openBg);
                viewHolder.foldTag.setText("open");
            }else {             // close
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.foldTag.setBackground(closeBg);
                viewHolder.foldTag.setText("close");
            }


            // 分享 按钮
            final String shareUrl = String.format(shareUrlRoot, model.getId(), model.getShorDate());
            viewHolder.share.setClickable(true);
            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                    sendIntent.setType("text/plain");
                    mContext.startActivity(Intent.createChooser(sendIntent, "share to ..."));
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        if (mIsLoadMore){
            return mData == null ? 0 : mData.size() + 1;// add footer
        }else {
            return mData == null ? 0 : mData.size();    // don't need footer
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mIsLoadMore && position == getItemCount() - 1) {
            return FOOTER;
        }
        if (mData.get(position).isItem()){
            return ITEM;
        }
        else {
            return HEADER;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.ib_open)
        CustomImageButton foldTag;
        @BindView(R.id.ib_share)
        ImageButton share;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_info_header)
        TextView header;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv)
        TextView tv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 展示FooterView
     * @param holder
     */
    protected void bindFooterItem(RecyclerView.ViewHolder holder) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        switch (mLoadMoreStatus) {
            case Constant.loadStatus.STATUS_LOADING:
                holder.itemView.setVisibility(View.VISIBLE);
                //footerViewHolder.pb.setVisibility(View.VISIBLE);
                footerViewHolder.tv.setText(mContext.getResources().getString(R.string.load_more));
                break;
            case Constant.loadStatus.STATUS_EMPTY:
                holder.itemView.setVisibility(View.VISIBLE);
                //footerViewHolder.pb.setVisibility(View.GONE);
                footerViewHolder.tv.setText(mContext.getResources().getString(R.string.load_more_no));
                holder.itemView.setOnClickListener(null);
                break;
            case Constant.loadStatus.STATUS_ERROR:
                holder.itemView.setVisibility(View.VISIBLE);
                //footerViewHolder.pb.setVisibility(View.GONE);
                footerViewHolder.tv.setText(mContext.getResources().getString(R.string.load_more_error));
                //holder.itemView.setOnClickListener(mListener);
                break;
            case Constant.loadStatus.STATUS_PREPARE:
                holder.itemView.setVisibility(View.INVISIBLE);
                break;
            case Constant.loadStatus.STATUS_DISMISS:
                holder.itemView.setVisibility(View.GONE);
        }
    }

    int firstOpenCount = 0;
    boolean needAdd = false;
    public List<InfoData.NewsModel> makeFirstThreeOpen(InfoData.InfoDay data, boolean isFirstLoad){
        List<InfoData.NewsModel> resultList = data.getNewsFastList();
        if (resultList != null  && resultList.size() > 0){
            int count = resultList.size();

            if (isFirstLoad){
                // make the first three be opened
                for (int i = 0; i < count; i++){
                    if (i <= 2){
                        resultList.get(i).setOpen(true);
                    }
                }
                if (count < 3){    // 第一次加载不够３条，需要再次加载时补足open条数
                    firstOpenCount = count;
                    needAdd = true;
                }else {
                    needAdd = false;
                }
            }else {
                if (needAdd){
                    for (int i = 0; i < count; i++){
                        if (i < (3 - firstOpenCount)){
                            resultList.get(i).setOpen(true);
                        }
                    }
                }

            }

            // duplicate one item as section header
            InfoData.NewsModel model = new InfoData.NewsModel();
            model.setItem(false);
            model.setToday(data.getDay());
            resultList.add(0, model);
//            for (int i = 0; i < resultList.size(); i++){
//                Log.d(TAG, " position:　i ＝　"+ i+" isItem:  "+ resultList.get(i).isItem());
//            }
        }
        return resultList;
    }

    public void setDataList(InfoData.InfoDay dataList){
        setModelList(dataList);
        notifyDataSetChanged();
    }
    public void addDataList(InfoData.InfoDay dataList){
        addModelList(dataList);
        notifyDataSetChanged();
    }

    public void setModelList(InfoData.InfoDay data){

        mData = makeFirstThreeOpen(data, true);
    }

    private void addModelList( InfoData.InfoDay data) {
        mData.addAll(makeFirstThreeOpen(data, false));
    }
    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }


    public int getLoadMoreStatus(){
        return mLoadMoreStatus;
    }
    /**
     * 设置footer的状态,并通知更改
     */
    public void setLoadMoreStatus(int status) {
        this.mLoadMoreStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }

    // scroll to load set true; load complete set false
    public void setIsLoadMore( boolean isLoadMore) {
        this.mIsLoadMore = isLoadMore;
        notifyDataSetChanged();
    }


    private String getStringFromHtml(String data){
        LogUtils.printCloseableInfo(TAG, "getStringFromHtml: "+ data);
        String result = "";
        String[] strs = null;
        String[] strs1 = null;
        if (data.contains("\\\">")){
            LogUtils.printCloseableInfo(TAG, "getStringFromHtml: data.contains(\"\\\\\\\">\")");
            strs = data.split(">");
            if (strs.length > 1){
                LogUtils.printCloseableInfo(TAG, "getStringFromHtml strs[1].toString() : "+ strs[1].toString());
                strs1 = strs[1].split("</");
                result = strs1[0];
            }
        }else {
            return data;
        }


//        LogUtils.printCloseableInfo(TAG, "str0: " + strs[0]);
//        LogUtils.printCloseableInfo(TAG, "str1: " + strs[1]);
//        LogUtils.printCloseableInfo(TAG, "str10: " + strs1[0]);
//        LogUtils.printCloseableInfo(TAG, "str11: " + strs1[1]);

        return result;
    }


    /**
     * 删除Html标签
     * @param inputString
     * @return
     */
    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_special;
        java.util.regex.Matcher m_special;
        try {
//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
// 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
// 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

}
