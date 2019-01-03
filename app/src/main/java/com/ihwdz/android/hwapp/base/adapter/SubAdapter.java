package com.ihwdz.android.hwapp.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.ui.home.HomeFragmentPresenter;
import com.yc.cn.ycbaseadapterlib.BaseViewHolder;

import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/07
 * desc :
 * version: 1.0
 * </pre>
 */
public class SubAdapter  extends DelegateAdapter.Adapter<BaseViewHolder> {

    Context mContext;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mLayoutParams;
    private int mCount = 0;
    private int mLayoutId = -1;
    private int mEmptyLayoutId = 0;
    private int mViewTypeItem = -1;

    public SubAdapter(Context context, int viewTypeItem, LayoutHelper layoutHelper) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mViewTypeItem = viewTypeItem;
    }
    public SubAdapter(Context context, LayoutHelper layoutHelper, int count) {
        this(context, layoutHelper, count, null);
    }
    public SubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mCount = count;
        this.mLayoutParams = layoutParams;
    }
    public SubAdapter(Context context, LayoutHelper layoutHelper, int layoutId, int viewTypeItem) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mLayoutId = layoutId;
        this.mViewTypeItem = viewTypeItem;
    }
    public SubAdapter(Context context, LayoutHelper layoutHelper, int count, int layoutId, int viewTypeItem) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mCount = count;
        this.mLayoutId = layoutId;
        this.mViewTypeItem = viewTypeItem;
    }
    public SubAdapter(Context context, LayoutHelper layoutHelper, int count, int layoutId, @NonNull VirtualLayoutManager.LayoutParams layoutParams, int viewTypeItem) {
        this.mContext = context;
        this.mLayoutHelper = layoutHelper;
        this.mCount = count;
        this.mLayoutId = layoutId;
        this.mLayoutParams = layoutParams;
        this.mViewTypeItem = viewTypeItem;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    protected void onBindViewHolderWithOffset(BaseViewHolder holder, int position, int offsetTotal) {
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == mViewTypeItem) {
//            if (mCount == 0){
//                return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_detail_comment_empty, parent, false));
//            }
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }
        return null;
    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if(mLayoutParams != null) {
            holder.itemView.setLayoutParams(new VirtualLayoutManager.LayoutParams(mLayoutParams));
        }
    }
    @Override
    public int getItemCount() {
        return mCount;
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return mViewTypeItem;
    }

    public void setItemCount(int count){
        this.mCount = count;
        this.notifyDataSetChanged();
    }

}
