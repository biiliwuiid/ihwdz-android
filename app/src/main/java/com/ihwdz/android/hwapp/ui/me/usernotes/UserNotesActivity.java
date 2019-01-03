package com.ihwdz.android.hwapp.ui.me.usernotes;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.ui.home.index.IndexWebActivity;
import com.ihwdz.android.hwslimcore.API.HwApi;

import butterknife.BindView;
import butterknife.OnClick;


public class UserNotesActivity extends BaseActivity {


    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.linear_note_client) LinearLayout userNoteLinear;
    @BindView(R.id.linear_note_order) LinearLayout orderNoteLinear;

    // WebView 用户须知嵌 h5
    @BindView(R.id.linear_web) LinearLayout webLinear;
    @BindView(R.id.web) WebView webView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    String userNotesUrl = HwApi.USER_NOTES_URL;       // 用户须知 currentMode == 0
    String orderNotesUrl = HwApi.ORDER_NOTES_URL;     // 订单须知 currentMode == 1


    static String TAG = "UserNotesActivity";

    private String titleStr;
    private int currentMode;

    static final String MODE_NOTE = "mode_note";       // 用户须知类型（用户须知-找客户- 0；订单须知-确认订单 - 1）

    /**
     *
     * @param context
     * @param mode 0: 用户须知; 1:订单须知
     */
    public static void startUserNotesActivity(Context context, int mode) {
        Intent intent = new Intent(context, UserNotesActivity.class);
        intent.putExtra(MODE_NOTE, mode);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_notes;
    }

    @Override
    public void initView() {
        initIntentData();  // 初始化 Intent携带数据
        initToolBar();
    }

    // Intent 携带数据
    private void initIntentData() {
        if (getIntent() != null){
            currentMode = getIntent().getIntExtra(MODE_NOTE, 0);

            if (currentMode == 0){
                showUserNoteView();   // 用户须知
            }else if (currentMode == 1){
                showOrderNoteView();  // 订单须知
            }

            initWebView();  // WebView H5
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    // toolbar
    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    private void initToolBar() {
        backBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }

    // 用户须知
    void showUserNoteView(){
        titleStr = getResources().getString(R.string.title_user_notes);
        userNoteLinear.setVisibility(View.GONE);
        orderNoteLinear.setVisibility(View.GONE);
    }
    // 订单须知
    void showOrderNoteView(){
        titleStr = getResources().getString(R.string.title_order_notes);
        userNoteLinear.setVisibility(View.GONE);
        orderNoteLinear.setVisibility(View.GONE);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {

        if (currentMode == 0){
            webView.loadUrl(userNotesUrl);   // 用户须知
        }else if (currentMode == 1){
            webView.loadUrl(orderNotesUrl);  // 订单须知
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
                Toast.makeText(UserNotesActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
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
