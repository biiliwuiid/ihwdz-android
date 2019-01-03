package com.ihwdz.android.hwapp.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.HomePageData;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class BaseBannerPagerAdapter extends AbsStaticPagerAdapter {

    private Context mContext;
    private List<HomePageData.BannerModel> mData;
    private List<Object> mData1;

    public BaseBannerPagerAdapter(Context ctx, List<HomePageData.BannerModel> list) {
        this.mContext = ctx;
        this.mData = list;
    }

    public BaseBannerPagerAdapter(Context ctx) {
        this.mContext = ctx;
    }


    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //加载图片
        if(mData != null){
            if(mData.get(position).picUrl instanceof String){
                ImageUtils.loadImgByPicasso(mContext, mData.get(position).picUrl, R.drawable.img_default,imageView);
            }
//            else if(mData.get(position).picUrl instanceof Integer){
//                ImageUtils.loadImgByPicasso(mContext,(Integer) mData.get(position).picUrl, R.drawable.img_default,imageView);
//            }else if(mData.get(position).picUrl instanceof Bitmap){
//                ImageUtils.loadImgByPicasso(mContext,(Bitmap) mData.get(position).picUrl, R.drawable.img_default,imageView);
//            }

        }else {
            ImageUtils.loadImgByPicasso(mContext, R.drawable.img_default,imageView);
        }
        return imageView;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setDataList(List<HomePageData.BannerModel> dataList){
        mData = dataList;
        notifyDataSetChanged();
    }

//    public void setDataList1(List<Object> dataList){
//        mData = dataList;
//        notifyDataSetChanged();
//    }

    public void clear(){
        mData = null;
        notifyDataSetChanged();
    }
}
