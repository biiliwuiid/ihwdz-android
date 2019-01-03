package com.ihwdz.android.hwapp.base.mvp;

import android.os.Bundle;

/**
 * Created by dell on 2018/7/24.
 */

public interface BasePresenter {


    //绑定数据
    void subscribe();
    //解除绑定
    void unSubscribe();

    void start();
    void stop();
    void store(Bundle outState);
    void restore(Bundle inState);
}
