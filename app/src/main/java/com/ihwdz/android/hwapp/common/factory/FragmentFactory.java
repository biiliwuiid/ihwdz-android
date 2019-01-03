package com.ihwdz.android.hwapp.common.factory;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.ui.home.HomeFragment;
import com.ihwdz.android.hwapp.ui.me.dealvip.DealVipFragment;
import com.ihwdz.android.hwapp.ui.me.infovip.InfoVipFragment;
import com.ihwdz.android.hwapp.ui.me.businessvip.BusinessVipFragment;
import com.ihwdz.android.hwapp.ui.orders.OrderFragment;
import com.ihwdz.android.hwapp.ui.purchase.PurchaseFragment;
import com.ihwdz.android.hwapp.ui.publish.PublishFragment;
import com.ihwdz.android.hwapp.ui.me.UserFragment;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc : FragmentFactory
 * version: 1.0
 * </pre>
 */
public class FragmentFactory {

    String TAG = "FragmentFactory";
    private static FragmentFactory mInstance;
    private HomeFragment mHomeFragment;
    private PurchaseFragment mPurchaseFragment;
    private PublishFragment mPublishFragment;
    private OrderFragment mOrderFragment;
    private Fragment mUserFragment;
    int userType = 100;   // 未登录

    Context mContext;
    IAppSettings settings;

    private FragmentFactory(Context context) {
        this.mContext = context;
        settings = new SlimAppSettings(context);
    }

    public static FragmentFactory getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new FragmentFactory(context);
                }
            }
        }
        return mInstance;
    }


    public HomeFragment getHomeFragment() {
        if (mHomeFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
            }
        }

        return mHomeFragment;
    }

    public PurchaseFragment getPurchaseFragment() {
        if (mPurchaseFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mPurchaseFragment == null) {
                    mPurchaseFragment = new PurchaseFragment();
                }
            }
        }
        return mPurchaseFragment;
    }
    public PurchaseFragment getPurchaseFragment(int purchaseMode) {
        if (mPurchaseFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mPurchaseFragment == null) {
                    mPurchaseFragment = new PurchaseFragment();
                }
            }
        }
        mPurchaseFragment.setCurrentMode(purchaseMode);
        return mPurchaseFragment;
    }

    public PublishFragment getPublishFragment() {
        if (mPublishFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mPublishFragment == null) {
                    mPublishFragment = new PublishFragment();
                }
            }
        }
        return mPublishFragment;
    }

    public OrderFragment getOrderFragment() {
        if (mOrderFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mOrderFragment == null) {
                    mOrderFragment = new OrderFragment();
                }
            }
        }
        return mOrderFragment;
    }

    public BaseFragment getUserFragment() {
       // LogUtils.printCloseableInfo(TAG,"---------- getUserFragment -------- Constant.LOGOUT : " + Constant.LOGOUT );
        int currentType = Constant.VIP_TYPE;

       // LogUtils.printCloseableInfo(TAG, "currentType: "+ currentType + " | userType: "+ userType);

        if (mUserFragment == null || userType != currentType ) {
            synchronized (FragmentFactory.class) {
                /**
                 * // -1普通用户 0-咨询会员，1-交易会员，2-商家会员
                 * userType: 100 未登录
                 * userType: -1  普通用户
                 * userType: 0　 已登录－资讯会员
                 * userType: 1　 已登录－交易会员
                 * userType: 2　 已登录－商家会员
                 */
                if (mUserFragment == null || userType != currentType ) {
                    userType = currentType;
                    if (mUserFragment != null){
                        mUserFragment.onDetach();
                        mUserFragment = null;
                    }
                    switch (userType){
                        case 100:
                            mUserFragment = new UserFragment();
                            break;
                        case -1:
                            mUserFragment = new InfoVipFragment();
                            break;
                        case 0:
                            mUserFragment = new InfoVipFragment();
                            break;
                        case 1:
                            mUserFragment = new DealVipFragment();
                            break;
                        case 2:
                            mUserFragment = new BusinessVipFragment();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return (BaseFragment) mUserFragment;
    }
}
