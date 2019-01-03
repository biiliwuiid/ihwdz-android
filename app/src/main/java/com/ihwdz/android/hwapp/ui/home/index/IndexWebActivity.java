package com.ihwdz.android.hwapp.ui.home.index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;


import butterknife.BindView;
import butterknife.OnClick;

public class IndexWebActivity extends BaseActivity {

    static String TAG = "IndexWebActivity";

    static final String SELECT_INDEX = "selectIndex";
    static final String TECHNOLOGY_INDEX = "technology";
    static final String TYPE_INDEX = "type";
    static final String ID_INDEX = "id";
    String titleStr = "";
    String technology = "电石法";
    String baseId = "1";

    String type = "1";    // 1 -> profitUrl; 2 -> futureUrl

    String profitUrl = "http://mb.ihwdz.com/ios/price_trend.html?technology=" + technology + "&baseId=" + baseId; // 利润走势H5
    String futureUrl = "http://mb.ihwdz.com/ios/price_difference.html?baseId=" + baseId;                          // 期现价差H5


    @BindView(R.id.iv_back)
    ImageView backBt;
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.web)
    WebView webView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public static void startIndexWebActivity(Context context, String selectIndex, String technology, String type, String baseId) {
        Log.i(TAG, "=================================== startIndexWebActivity ===================");
        Intent intent = new Intent(context, IndexWebActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SELECT_INDEX, selectIndex);
        intent.putExtra(TECHNOLOGY_INDEX, technology);
        intent.putExtra(TYPE_INDEX, type);
        intent.putExtra(ID_INDEX, baseId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_index_web;
    }

    @Override
    public void initView() {
        LogUtils.printCloseableInfo(TAG, "=================================== initView ===================");
        if (getIntent()!= null){
            titleStr = getIntent().getStringExtra(SELECT_INDEX);
            technology = getIntent().getStringExtra(TECHNOLOGY_INDEX);
            type = getIntent().getStringExtra(TYPE_INDEX);
            baseId = getIntent().getStringExtra(ID_INDEX);
            LogUtils.printCloseableInfo(TAG, "===== getIntent ==== titleString/ TECHNOLOGY_PARAM/ type: " + titleStr + "/ "+ technology + "/ "+ type);
        }
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
        initWebView();


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked(){
        onBackPressed();
    }


    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        if (type.equals("1")){
            webView.loadUrl(profitUrl);
        }else {
            webView.loadUrl(futureUrl);
        }
        webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) { //页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) { //页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG,"拦截url:"+ url);
            if(url.equals("http://www.google.com/")){
                Toast.makeText(IndexWebActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true; //表示已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();
            Log.i(TAG,"onJsAlert:");


            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i(TAG,"网页标题:"+ title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown ========= 是否有上一个页面:"+ webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){//点击返回按钮的时候判断有没有上一页
            webView.goBack();  // goBack()表示返回webView的上一页面
            Log.i(TAG,"onKeyDown ========= webView.canGoBack():"+ webView.canGoBack());
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Log.i(TAG,"onKeyDown ========= onBackPressed ");
            onBackPressed();
        }
        return super.onKeyDown(keyCode,event);
    }

    /**
     * JS调用android的方法
     * @param str
     * @return
     */
    @JavascriptInterface //仍然必不可少
    public void  getClient(String str){
        Log.i(TAG,"@JavascriptInterface ========== getClient =========== html调用客户端:"+str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        webView.destroy();
        webView = null;
    }


}
