package com.ihwdz.android.hwapp.ui.home.priceinquiry;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/24
 * desc :   点击 收藏star
 * version: 1.0
 * </pre>
 */
public interface OnPriceItemClickListener {
    void onPriceStarClicked(int collectionType,       // 0 市场价 1 出厂价
                            String breed,
                            String spec,
                            String brand,
                            String area,
                            boolean collect);     // 收藏/取消收藏
}
