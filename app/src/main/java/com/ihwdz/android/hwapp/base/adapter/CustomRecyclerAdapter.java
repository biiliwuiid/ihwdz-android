package com.ihwdz.android.hwapp.base.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/23
 * desc :   Add Header and Footer for RecyclerView.Adapter (decorator model)
 * version: 1.0
 * </pre>
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int headtype = 0x11111;
    private int normaltype = 0x11112;
    private int foottype = 0x11113;
    private View mHeaderView;
    private View mFooterView;
    private RecyclerView.Adapter mApter;//目标adapter
    private OnLoadMoreListener onloadMoreListener;

    /**
     * 判断是否到底部了
     * @param recyclerView
     * @return
     */
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }



    /**
     * 加载更多回调接口
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public CustomRecyclerAdapter(RecyclerView.Adapter targetApter){
        this.mApter = targetApter;
    }

    public void addHeader(View view){
        this.mHeaderView = view;
    }
    public void addFooter(View view){
        this.mFooterView = view;
    }
    // implement load more
    public void setOnLoadMoreListener(final OnLoadMoreListener onloadMoreListener, RecyclerView recyclerView){
        this.onloadMoreListener = onloadMoreListener;
        if(recyclerView != null && onloadMoreListener != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(isSlideToBottom(recyclerView)) {
                        if(onloadMoreListener != null) {
                            onloadMoreListener.onLoadMore();
                        }
                    }
                }
            });

//            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if(isSlideToBottom(recyclerView))
//                    {
//                        if(onloadMoreListener != null)
//                        {
//                            onloadMoreListener.onLoadMore();
//                        }
//                    }
//                }
//            });
        }
    }

    /**
     * 根据头部尾部返回相应的type，这里if else没有简写，方便看逻辑
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if(mHeaderView != null && mFooterView != null)//同时加了头部和尾部
        {
            if(position == 0) //当position为0时，展示header
            {
                return headtype;
            }
            else if(position == getItemCount() - 1)//当position为最后一个时，展示footer
            {
                return foottype;
            }
            else//其他时候就展示原来adapter的
            {
                return normaltype;
            }
        }
        else if(mHeaderView != null) {//只有头部
            if (position == 0)
                return headtype;
            return normaltype;
        }
        else if(mFooterView != null)//只有尾部
        {
            if(position == getItemCount() - 1)
            {
                return foottype;
            }
            else
            {
                return normaltype;
            }
        }
        else {
            return normaltype;
        }
    }

    /**
     * 返回item的数量，
     * @return
     */
    @Override
    public int getItemCount() {

        if(mHeaderView != null && mFooterView != null)//有头部和尾部，就多了2
        {
            return mApter.getItemCount() + 2;
        }
        else if(mHeaderView != null)//只有头部多了1
        {
            return mApter.getItemCount() + 1;
        }
        else if(mFooterView != null)//只有尾部也多了1
        {
            return mApter.getItemCount() + 1;
        }
        return mApter.getItemCount();//其他就是默认的值， 不多也不少
    }

    /**
     * 头部的ViewHolder
     */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 尾部的ViewHolder
     */
    private class FoogerViewHolder extends RecyclerView.ViewHolder {
        public FoogerViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 处理当时Gridview类型的效果时，也把头部和尾部设置成一整行（这就是RecyclerView的其中一个优秀之处，列表的每行可以不同数量的列）
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager) {
            /**
             * getSpanSize的返回值的意思是：position位置的item的宽度占几列
             * 比如总的是4列，然后头部全部显示的话就应该占4列，此时就返回4
             * 其他的只占一列，所以就返回1，剩下的三列就由后面的item来依次填充。
             */
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(mHeaderView != null && mFooterView != null)
                    {
                        if(position == 0)
                        {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        else if(position == getItemCount() - 1) {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        else
                        {
                            return 1;
                        }
                    }
                    else if(mHeaderView != null) {
                        if (position == 0) {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        return 1;
                    }
                    else if(mFooterView != null)
                    {
                        if(position == getItemCount() - 1)
                        {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        return 1;
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * 这里就根据getItemViewType返回的值来返回相应的ViewHolder
     * 头部和尾部的ViewHolder只是一个集成RecyclerView.ViewHolder的简单默认类，里面并没有任何处理。
     * 这样就完成了类型的返回了（需注意为什么这样做）
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == headtype)//返回头部的ViewHolder
            return new HeaderViewHolder(mHeaderView);
        else if(viewType == foottype)//返回尾部的ViewHolder
            return new FoogerViewHolder(mFooterView);//其他就直接返回传入的adapter的ViewHolder
        return mApter.onCreateViewHolder(parent, viewType);
    }

    /**
     * 绑定ViewHolder，当时header或footer时，直接返回，因为不用绑定，
     * 当是传入的adapter时，就直接调用adapter.onBindViewHolder就行了
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(mHeaderView != null && mFooterView != null) //有头部和尾部
        {
            if(position == 0){  //头部直接返回，无需绑定
                return;
            }
            else if(position == getItemCount() -1){//尾部直接返回，也无需绑定
                return;
            }
            else {
                mApter.onBindViewHolder(holder, position - 1);//其他就调用adapter的绑定方法
            }
        }
        else if(mHeaderView != null) {
            if(position == 0) {
                return;
            }
            else {
                mApter.onBindViewHolder(holder, position - 1);
            }
        }
        else if(mFooterView != null)
        {
            if(position == getItemCount() - 1) {
                return;
            }
            else {
                mApter.onBindViewHolder(holder, position);
            }
        }
        else {
            mApter.onBindViewHolder(holder, position);
        }
    }

}
