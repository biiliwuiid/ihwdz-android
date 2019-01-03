package com.ihwdz.android.hwapp.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.adapter.BasePagerAdapter;
import com.ihwdz.android.hwapp.base.adapter.BaseStatePagerAdapter;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.base.mvp.BaseFragment;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.common.factory.FragmentFactory;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.widget.AdDialog;
import com.ihwdz.android.hwapp.widget.NoSlidingViewPager;
import com.ihwdz.android.hwslimcore.API.HwApi;
import com.ihwdz.android.hwslimcore.Settings.versionUpdate.UpdateAppHttpUtil;
import com.ns.yc.ycutilslib.managerLeak.InputMethodManagerLeakUtils;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.utils.AppUpdateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity<MainPresenter> implements View.OnClickListener, MainContract.View{

    @BindView(R.id.vp_home)
    NoSlidingViewPager vpHome;
    @BindView(R.id.ctl_table)
    CommonTabLayout mTable;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    String TAG = "MainActivity";
    int selectIndex = 0;
    int currentIndex = 0;
    boolean isPurchasePool = false;   // 判断是要进入 求购池 还是（我的求购/我的报价）

    public static boolean isForeground = false;

    @Inject MainPresenter mPresenter;

    public static final int HOME = 0;
    public static final int ORDER = 1;
    public static final int PUBLISH = 2;
    public static final int LOGISTIC = 3;
    public static final int USER = 4;

    static final String SELECT_INDEX = "selectIndex";
    static final String IS_PURCHASE_POOL = "is_purchase_pool";

    private int currentFragment = 0;

    List<BaseFragment> fragments = new ArrayList<>();
    BasePagerAdapter adapter;
    BaseStatePagerAdapter mAdapter;

    AdDialog dialog;
    ImageView ivAd;
    ImageView ivClose;

    @IntDef({HOME,ORDER,PUBLISH,LOGISTIC,USER})
    private  @interface PageIndex{}

    /**
     * 跳转首页
     * @param context               上下文
     * @param selectIndex           添加注解限制输入值
     */
//    public static void startActivity(Context context, @PageIndex int selectIndex) {
    public static void startActivity(Context context, int selectIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SELECT_INDEX, selectIndex);
        context.startActivity(intent);
    }

    public static void startPurchasePool(Context context, int selectIndex, boolean isPurchasePool) {
        Intent intent = new Intent(context, MainActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SELECT_INDEX, selectIndex);
        intent.putExtra(IS_PURCHASE_POOL, isPurchasePool);
        context.startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.printInfo(TAG, "----------------- MainActivity onNewIntent -------------------------");
        super.onNewIntent(intent);
        if(intent != null){
            selectIndex = intent.getIntExtra(SELECT_INDEX, HOME);
            isPurchasePool = intent.getBooleanExtra(IS_PURCHASE_POOL, false);

            LogUtils.printInfo(TAG, "-----------------  onNewIntent -------------------------currentIndex: " + currentIndex +"   | selectIndex: "+selectIndex);

            switch (selectIndex){
                case 0:
                    LogUtils.printInfo(TAG, "-----------------  onNewIntent --- 首页 " );
                    break;
                case 1:
                    LogUtils.printInfo(TAG, "-----------------  onNewIntent --- 求购池 " );
                    fragments.remove(1);
                    if (isPurchasePool){
                        fragments.add(1, FragmentFactory.getInstance(this).getPurchaseFragment(0));
                    }else {
                        fragments.add(1, FragmentFactory.getInstance(this).getPurchaseFragment(1));  // 发布求购后返回 求购池 -> 我的求购 || 报价后 -> 我的报价
                    }

                    vpHome.setCurrentItem(selectIndex);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    LogUtils.printInfo(TAG, "-----------------  onNewIntent --- 发布 " );
                    break;
                case 3:
                    LogUtils.printInfo(TAG, "-----------------  onNewIntent --- 订单 " );
                    fragments.remove(3);
                    fragments.add(3, FragmentFactory.getInstance(this).getOrderFragment());  // 提交订单 -> 订单
                    vpHome.setCurrentItem(selectIndex);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    LogUtils.printInfo(TAG, "-----------------  onNewIntent --- 我的 ");

                    fragments.remove(4);
                    fragments.add(4, FragmentFactory.getInstance(this).getUserFragment());
                    vpHome.setCurrentItem(selectIndex);
                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        JPushInterface.onResume(this);
    }

    /**
     * 自动检测更新
     */
    private String mUpdateUrl1 = HwApi.HWDZ_UPGRADE_URL;
    public void constraintUpdate() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Map<String, String> params = new HashMap<String, String>();

        //params.put("appKey", "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f");
        String version = AppUpdateUtils.getVersionName(this);
        LogUtils.printError(TAG, "version: " + version);
        params.put("version", AppUpdateUtils.getVersionName(this));  // appVersion
        //params.put("key1", "value2");
        //params.put("key2", "value3");

        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl(mUpdateUrl1)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .setParams(params)
                .build()
                .update();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        InputMethodManagerLeakUtils.fixInputMethodManagerLeak(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        LogUtils.printInfo(TAG, "----------------------- initView -------------------------");

        if(getIntent() != null){
            selectIndex = getIntent().getIntExtra("selectIndex", HOME);
        }
        if (!mPresenter.getIsLogout()){
            // LogUtils.printInfo(TAG, "----------------------- - 尝试自动登录 ");
            mPresenter.login();
        }else{
            // LogUtils.printInfo(TAG, "----------------------- - 用户退出登录 ");
        }
        init();
        registerMessageReceiver();  // Jpush used for receive msg
        initTabLayout();
        initViewPager();

        // APP 升级
        constraintUpdate();   // 自动检测更新


        /**
         * 广告弹窗
         */
//        if (mPresenter.getIsFirstLoad()){
//            LogUtils.printInfo(TAG, "----------------------- 首次加载 ");
//            mPresenter.setIsFirstLoad(false);
//            // 首次加载 - 广告弹窗
//            showAdDialog();
//        }else {
//            LogUtils.printInfo(TAG, "-----------------------非 首次加载 ");
//        }

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.getUpdate();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ad:
                mPresenter.goAdvertisement();
                dialog.dismiss();
                break;
            case R.id.close_ad:
                dialog.dismiss();
                break;
        }
    }


    /**
     * 初始化底部导航栏数据
     */
    private void initTabLayout() {
        ArrayList<CustomTabEntity> mTabEntities = mPresenter.getTabEntity();
        mTable.setTabData(mTabEntities);
        mTable.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpHome.setCurrentItem(position);
                LogUtils.printCloseableInfo(TAG, "initTabLayout onTabSelect: "+ position);
                switch (position) {
                    case 0: // 首页
//                        tvTitle.setVisibility(View.VISIBLE);
//                        tvTitle.setText(getResources().getString(R.string.title_tab0));
                        break;
                    case 1: // 求购池
//                        tvTitle.setVisibility(View.VISIBLE);
//                        tvTitle.setText(getResources().getString(R.string.title_tab1));
                        break;
                    case 2: // 发布
//                        tvTitle.setVisibility(View.VISIBLE);
//                        tvTitle.setText(getResources().getString(R.string.title_tab2));
                        break;
                    case 3: // 订单
//                        tvTitle.setVisibility(View.VISIBLE);
//                        tvTitle.setText(getResources().getString(R.string.title_tab3));
                        break;
                    case 4: // 我的（会员中心）
                        //tvTitle.setVisibility(View.VISIBLE);
                        //tvTitle.setText("我的");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
                LogUtils.printInfo(TAG,"===================================== " );
                LogUtils.printInfo(TAG,"------------- onTabReselect --------------- " + position );
                currentIndex = position;
                LogUtils.printInfo(TAG,"===================================== " );
            }
        });
    }

    /**
     * 初始化ViewPager数据
     */
    private void initViewPager() {
        LogUtils.printCloseableInfo(TAG,"------------- initViewPager --------------- " );
        fragments.add(FragmentFactory.getInstance(this).getHomeFragment());
        fragments.add(FragmentFactory.getInstance(this).getPurchaseFragment());
        fragments.add(FragmentFactory.getInstance(this).getPublishFragment());
        fragments.add(FragmentFactory.getInstance(this).getOrderFragment());
        fragments.add(FragmentFactory.getInstance(this).getUserFragment());

        adapter = new BasePagerAdapter(getSupportFragmentManager(), fragments);
        mAdapter = new BaseStatePagerAdapter(getSupportFragmentManager(), fragments);
        //vpHome.setAdapter(adapter);
        vpHome.setAdapter(mAdapter);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = position;
                if (position != 0){
                    //hideToolbar();
                }
                if (position == 4){
                    if (Constant.VIP_TYPE != 100){
                        fragments.remove(4);
                        fragments.add(FragmentFactory.getInstance(getBaseContext()).getUserFragment());
                        mAdapter.notifyDataSetChanged();
                        LogUtils.printCloseableInfo(TAG, "onPageSelected: Constant.VIP_TYPE != 100: " + Constant.VIP_TYPE);
                    }

                }
                mTable.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vpHome.setOffscreenPageLimit(5);
        currentIndex = selectIndex;
        vpHome.setCurrentItem(selectIndex);
    }

    /**
     * 首页 广告弹窗
     */
    @Override
    public void showAdDialog() {
        dialog = new AdDialog
                .Builder(this)
                //.setTitle(getResources().getString(R.string.title_dialog_deal))
                .setInsideContentView(getAdView())
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtils.printInfo(TAG, " =================  negative button clicked! " );

                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public int getCurrentFragment() {
        return currentFragment;
    }

    public View getAdView() {
        View view = View.inflate(this, R.layout.ad_dialog, null);
        ivAd = view.findViewById(R.id.iv_ad);
        ivClose = view.findViewById(R.id.close_ad);
        ivAd.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        return view;
    }

    @Override
    public void showPromptMessage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Jpush
     */
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String message = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + message + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    LogUtils.printInfo(TAG, "onReceive: showMsg.toString: "+ showMsg.toString());
                    //setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

}
