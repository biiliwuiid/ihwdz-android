package com.ihwdz.android.hwapp.base.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihwdz.android.hwapp.base.listener.LoginListenerHandler;
import com.ihwdz.android.hwapp.base.listener.OnLoginSuccessListener;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.common.factory.FragmentFactory;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/24
 * desc : BaseFragment
 * version: 1.0
 * </pre>
 */
public abstract class BaseFragment <T extends BasePresenter> extends Fragment {

    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;


    LoginListenerHandler mLoginListenHandler;

    private Callback callback;

    interface Callback{
        public void callback();
    }

    public void onLoginSuccess(){
        callback.callback();
    }

    Unbinder mButterKnifeUnbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container , false);
        mButterKnifeUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null){
            mPresenter.subscribe();
        }
        if (mLoginListenHandler == null){
            mLoginListenHandler = new LoginListenerHandler(new OnLoginSuccessListener() {
                @Override
                public void onLoginSuccess() {
                    LogUtils.printCloseableInfo("BaseFragment", "====================== onLoginSuccess ======================== VIP_TYPE: " + Constant.VIP_TYPE);
                    //initData(); // 子 Fragment 选择性的 设置是否在 登录成功后重新加载数据
                    initLoginSuccess();
                    FragmentFactory.getInstance(getContext()).getPurchaseFragment().initLoginSuccess(); // 0:全部求购数据 1: 我的
                    FragmentFactory.getInstance(getContext()).getPublishFragment().initLoginSuccess();
                    FragmentFactory.getInstance(getContext()).getOrderFragment().initLoginSuccess();
                }
            });
        }

        initView();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButterKnifeUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.unSubscribe();
        }
        initLeakCanary();             //测试内存泄漏，正式一定要隐藏
    }




    /** 返回一个用于显示界面的布局id */
    public abstract int getContentView();

    /** 初始化View的代码写在这个方法中 */
    public abstract void initView();

    /** 初始化监听器的代码写在这个方法中 */
    public abstract void initListener();

    /** 初始数据的代码写在这个方法中，用于从服务器获取数据 */
    public abstract void initData();

    /** 初始数据的代码写在这个方法中，用于判断是否已经登录 */
    // TODO: 2018/11/28  监听登录成功
    public abstract void initLoginSuccess();

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**用来检测所有Fragment的内存泄漏*/
    private void initLeakCanary() {
        /*RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }

}
