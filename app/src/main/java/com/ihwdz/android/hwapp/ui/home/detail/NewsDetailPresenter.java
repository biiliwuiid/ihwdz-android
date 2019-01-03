package com.ihwdz.android.hwapp.ui.home.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.adapter.SubAdapter;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.CommentData;
import com.ihwdz.android.hwapp.model.bean.DetailData;
import com.ihwdz.android.hwapp.model.bean.NewsData;
import com.ihwdz.android.hwapp.model.bean.RecommendData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.utils.bitmap.ImageUtils;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwapp.widget.RoundImageView;
import com.ihwdz.android.hwslimcore.API.homeData.HomeDataModel;
import com.yc.cn.ycbaseadapterlib.BaseViewHolder;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.qqtheme.framework.util.ScreenUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.home.detail.NewsDetailContract.COMMENT_LEVEL_1;
import static com.ihwdz.android.hwapp.ui.home.detail.NewsDetailContract.COMMENT_LEVEL_2;
import static com.ihwdz.android.hwapp.ui.home.detail.NewsDetailContract.COMMENT_THUMB;
import static com.ihwdz.android.hwapp.ui.home.detail.NewsDetailContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/01
 * desc :
 * version: 1.0
 * </pre>
 */

public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    String TAG = "NewsDetailPresenter";
    @Inject
    NewsDetailActivity parentActivity;
    @Inject NewsDetailContract.View mView;

    @Inject
    CompositeSubscription mSubscriptions;
    HomeDataModel model;

    SubAdapter mNewsDetailAdapter;       // 新闻详情　适配器
    SubAdapter mCommentTitleAdapter;     // 评论标题　适配器
    SubAdapter mCommentAdapter;          // 一级评论　适配器
    SubAdapter mRecommendTitleAdapter;   // 推荐标题　适配器
    SubAdapter mRecommendAdapter;        // 热门推荐　适配器
    SubAdapter mFooterAdapter;           // 尾布局　　适配器

    DetailData.NewsModel mDetailModel;                // 新闻详情 数据源
    List<CommentData.NewsModel> mCommentList;         // 一级评论 数据源
    List<RecommendData.NewsModel> mRecommendList;     // 热门推荐 数据源

    @Inject CommentAdapter mCommentReplyAdapter;  //　二级评论（回复）　适配器

    String sourceString;

    int currentPageNum = 1;
    String currentId = null;
    String currentCommentCount;

    String currentCommentId = null;
    String currentEditText;

    int currentCommentMode = COMMENT_LEVEL_1;
    boolean isThumbClicked = false;
    int currentCommentPosition;

    private String shareUrlRoot;  // need id and shortDate
    private String mShortDate;

    String shareUrl = "http://mb.ihwdz.com/article/article.html?id=";

    ProgressBar progressBar;

    @Inject
    public NewsDetailPresenter(NewsDetailActivity activity){
        this.parentActivity = activity;
        sourceString = activity.getResources().getString(R.string.source_de);
        model = new HomeDataModel(parentActivity);
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(parentActivity != null){
            parentActivity = null;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void store(Bundle outState) {

    }

    @Override
    public void restore(Bundle inState) {

    }

    @Override
    public void setCurrentId(String id) {
       this.currentId = id;
    }

    @Override
    public String getCurrentId() {
        return currentId;
    }

    @Override
    public void setEditText(String editText) {
        this.currentEditText = editText;
    }

    @Override
    public String getEditText() {
        return currentEditText;
    }

    @Override
    public void setCommentId(String commentId) {
        this.currentCommentId = commentId;
    }

    @Override
    public String getCommentId() {
        return currentCommentId;
    }

    @Override
    public void setCurrentCommentPosition(int position) {
        this.currentCommentPosition = position;
    }

    @Override
    public int getCurrentCommentPosition() {
        return currentCommentPosition;
    }

    @Override
    public void setCommentMode(int mode) {
        this.currentCommentMode = mode;
    }

    @Override
    public int getCommentMode() {
        return currentCommentMode;
    }

    @Override
    public void setIsThumbClicked(boolean isThumbClicked) {
        this.isThumbClicked = isThumbClicked;
    }

    @Override
    public boolean getIsThumbClicked() {
        return isThumbClicked;
    }

    @Override
    public void getNewsDetailData() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getNewsDetailData(currentId)
                .compose(RxUtil.<DetailData>rxSchedulerHelper())
                .subscribe(new Subscriber<DetailData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(DetailData data) {
                        mView.hideWaitingRing();
                        if (TextUtils.equals("0", data.code)){
                            mShortDate = data.getData().getShorDate();

                            if (mDetailModel == null){
                                mDetailModel = new DetailData.NewsModel();
                            }
                            mDetailModel = data.getData();
                            mNewsDetailAdapter.notifyDataSetChanged();
                        }else {
                            mView.showPromptMessage(data.msg);
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void getCommentData() {
        LogUtils.printInfo(TAG, "===========   getCommentData   =============");
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getCommentData(currentId, currentPageNum, PageSize)
                .compose(RxUtil.<CommentData>rxSchedulerHelper())
                .subscribe(new Subscriber<CommentData>() {
                    @Override
                    public void onCompleted() {
                        setCommentMode(COMMENT_LEVEL_1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(CommentData data) {
                        mView.hideWaitingRing();

                        if (data != null && data.getData() != null && data.getData().getRecordList() != null && data.getData().getRecordList().size() > 0){

                            if (mCommentList == null){
                                mCommentList = new ArrayList<>();
                            }else {
                                mCommentList.clear();
                            }
                            mCommentList = data.getData().getRecordList();

                            switch (currentCommentMode){
                                case COMMENT_THUMB:  // 点赞
                                    // 点赞只更改该条评论数据
                                    LogUtils.printInfo(TAG, "=====点赞只更改该条评论数据====== POSITION: "+ getCurrentCommentPosition());
                                    mCommentList.get(getCurrentCommentPosition()).setThumb(true);
                                    mCommentAdapter.notifyItemChanged(getCurrentCommentPosition());
                                    break;
                                case COMMENT_LEVEL_1: // 一级评论
                                    LogUtils.printInfo(TAG, "=====回复只更改该条评论数据====== POSITION: "+ getCurrentCommentPosition());

                                    mCommentTitleAdapter.setItemCount(mCommentList.size());
                                    mCommentAdapter.setItemCount(mCommentList.size());
                                    mCommentTitleAdapter.notifyDataSetChanged();
                                    mCommentAdapter.notifyDataSetChanged();
                                    break;
                                case COMMENT_LEVEL_2: // 二级评论
                                    mCommentAdapter.notifyItemChanged(getCurrentCommentPosition());
                                    break;
                            }
//                            if (currentCommentMode == COMMENT_THUMB){
//                                // 点赞只更改该条评论数据
//                                LogUtils.printInfo(TAG, "=====点赞只更改该条评论数据====== POSITION: "+ getCurrentCommentPosition());
//                                mCommentList.get(getCurrentCommentPosition()).setThumb(true);
//                                mCommentAdapter.notifyItemChanged(getCurrentCommentPosition());
//
//                            }else if (currentCommentMode == COMMENT_LEVEL_2){
//                                // 回复只更改该条评论数据
//                                LogUtils.printInfo(TAG, "=====回复只更改该条评论数据====== POSITION: "+ getCurrentCommentPosition());
//                                mCommentAdapter.notifyItemChanged(getCurrentCommentPosition());
//                            }else {
//                                mCommentTitleAdapter.notifyDataSetChanged();
//                                mCommentAdapter.notifyDataSetChanged();
//                            }

                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void getRecommendData() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getRecommendData(currentId)
                .compose(RxUtil.<RecommendData>rxSchedulerHelper())
                .subscribe(new Subscriber<RecommendData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onNext(RecommendData data) {
                        mView.hideWaitingRing();
                        if (data != null && data.getData() != null && data.getData() != null && data.getData().size() > 0){
                            if (mRecommendList == null){
                                mRecommendList = new ArrayList<>();
                            }else {
                                mRecommendList.clear();
                            }
                            mRecommendList = data.getData();
                            mRecommendAdapter.notifyDataSetChanged();
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    // 一级品论
    @Override
    public void postCommentLevel1Data() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .postCommentLevel1Data(Constant.token, currentId, currentEditText)
                .compose(RxUtil.<CommentData>rxSchedulerHelper())
                .subscribe(new Subscriber<CommentData>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.printInfo(TAG, "postCommentLevel1Data=== onCompleted");
                        mView.hideKeyboard();
                        mView.updateEditBox();
                        // 提交完数据后　更新comment.
                        getCommentData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CommentData data) {
                        mView.hideWaitingRing();
                        if ("0".equals(data.getCode())){
                            //提交成功　增加该条评论 - data 为该条评论
                            mView.showPromptMessage(data.getMsg());
                            LogUtils.printInfo(TAG, "postCommentLevel1Data ============== onNext");
                            // 2018/9/6  增加该条评论 needn't update all, getCommentData in onCompleted();
                        }else {
                            mView.showErrorMsg(data.getMsg());
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 二级品论
    @Override
    public void postCommentLevel2Data() {
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .postCommentLevel2Data(Constant.token, currentId, currentEditText, currentCommentId)
                .compose(RxUtil.<CommentData>rxSchedulerHelper())
                .subscribe(new Subscriber<CommentData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideKeyboard();
                        mView.updateEditBox();
                        // 提交完数据后　更新comment. 将评论模式改回：　COMMENT_LEVEL_1
                        getCommentData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CommentData data) {
                        mView.hideWaitingRing();
                        LogUtils.printInfo(TAG, "postCommentLevel2 ============== onNext data: "+ data.getData());

                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    // 评论点赞
    @Override
    public void getLikeCommentData() {
        Subscription rxSubscription = model
                .getLikeCommentData(currentCommentId)
                .compose(RxUtil.<CommentData>rxSchedulerHelper())
                .subscribe(new Subscriber<CommentData>() {
                    @Override
                    public void onCompleted() {
                        // 提交完数据后　更新comment.
                        getCommentData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CommentData data) {

                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    @Override
    public void doComment(String comment) {
        currentEditText = comment;
        if (currentCommentMode == COMMENT_LEVEL_1){
            postCommentLevel1Data();
        }else if (currentCommentMode == COMMENT_LEVEL_2){
            postCommentLevel2Data();
        }

    }

    @Override
    public void doShare() {
        shareUrlRoot = parentActivity.getResources().getString(R.string.share_url);
        final String shareUrl = String.format(shareUrlRoot, currentId, mShortDate);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
        sendIntent.setType("text/plain");
        parentActivity.startActivity(Intent.createChooser(sendIntent, "share to ..."));
    }

    @Override
    public void doCollect() {
        LogUtils.printError(TAG, "=========== doCollect =============== ");
        mView.showWaitingRing();
        Subscription rxSubscription = model
                .getCollectionData(currentId)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideWaitingRing();
                        LogUtils.printError(TAG, "doCollect onError: " + e.toString());
                        mView.showPromptMessage(e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        mView.hideWaitingRing();
                        LogUtils.printInfo(TAG, "doCollect ============== onNext data: "+ data.data);
                        if (TextUtils.equals("0", data.code)){
                            mView.showPromptMessage(data.msg);
                            mView.updateCollectionIcon();

                        }else {
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 登陆/注册
    @Override
    public void gotoLoginPage() {
        LoginPageActivity.startLoginPageActivity(parentActivity);
    }

    @Override
    public DelegateAdapter initRecyclerView(RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(parentActivity);
        recyclerView.setLayoutManager(layoutManager);

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    /**
     * WebViewClient
     * WebChromeClient
     * @return
     */
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(android.webkit.WebView view, String url) { //页面加载完成
            progressBar.setVisibility(View.GONE);

            String javascript = "javascript:function ResizeImages() {" +
                    "var myimg,oldwidth;" +
                    "var maxwidth = document.body.clientWidth;" +
                    "for(i=0;i <document.images.length;i++){" +
                    "myimg = document.images[i];" +
                    "if(myimg.width > maxwidth){" +
                    "oldwidth = myimg.width;" +
                    "myimg.width = maxwidth-30;" +
                    "}" +
                    "}" +
                    "}";
            String width = String.valueOf(ScreenUtils.widthPixels(parentActivity.getApplicationContext()));
            view.loadUrl(javascript);
            view.loadUrl("javascript:ResizeImages();");

        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) { //页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            Log.i(TAG,"拦截url:"+ url);
            if(url.equals("http://www.google.com/")){
                Log.e(TAG, "国内不能访问google,拦截该url");
                return true; //表示已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(android.webkit.WebView webView, String url, String message, JsResult result) {
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
        public void onReceivedTitle(android.webkit.WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i(TAG,"onReceivedTitle ==== 网页标题:"+ title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    // 新闻详情
    @Override
    public SubAdapter initNewsDetail() {
        Log.d(TAG, "=============================== initNewsDetail ==========: ");
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(0);
        mNewsDetailAdapter = new SubAdapter(parentActivity, linearLayoutHelper, 1, R.layout.news_detial, Constant.NewsDetailViewType.typeDetail){
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (mDetailModel != null){
                    DetailData.NewsModel model = mDetailModel;
                    Log.d(TAG, "==================================== DetailData.NewsModel ==========: "+ model);
                    // get view
                    TextView detailTitle = holder.getView(R.id.title);
                    detailTitle.getPaint().setFakeBoldText(true);
                    TextView detailViewTimes = holder.getView(R.id.viewTimes);
                    TextView detailAuthor = holder.getView(R.id.author);
                    TextView detailSource = holder.getView(R.id.source);
                    TextView detailDate = holder.getView(R.id.date);
                    progressBar = holder.getView(R.id.progress_bar);
                    WebView webView = holder.getView(R.id.web_view);

                    // set data
                    detailTitle.setText(model.getTitle());
                    detailViewTimes.setText(model.getViewTimes());
                    detailAuthor.setText(model.getAuthor());
                    detailSource.setText(String.format(sourceString,model.getSource()));
                    detailDate.setText(model.getDateTime());

                    // LogUtils.printCloseableInfo(TAG, "HTML:   " + getHtmlContent(model.getContent()));;

                    webView.loadDataWithBaseURL(null, getHtmlContent(model.getContent()), "text/html", "UTF-8", null);
                    webView.setWebChromeClient(webChromeClient);
                    webView.setWebViewClient(webViewClient);
                    // webView.setInitialScale(100);
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);         //允许使用js
                    webSettings.setBuiltInZoomControls(false);
                    webSettings.setSupportZoom(false);

                    /**
                     * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
                     * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
                     * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
                     * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
                     */
                    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存，只从网络获取数据.

                }else {
                    Log.d(TAG, "COUNT DetailData.NewsModel == null");
                }
                super.onBindViewHolder(holder, position);
            }

            @Override
            public int getItemCount() {
                return 1;
            }
        };
        return mNewsDetailAdapter;
    }


    // 拼接成 html 格式
    private String getHtmlContent(String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!doctype html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\\\"UTF-8\\\">");
        sb.append("<meta name=\\\"viewport\\\" content=\\\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\\\"/>");
        sb.append("<style type=\\\"text/css\\\"> p{font-size: 14px; color: #333;line-height:22px; margin-bottom: -10px;} p:last-child{ margin-bottom:0} #content{padding:0 15px;text-align: justify;}  </style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div class=\\\"allbg\\\" id=\\\"allbg\\\"></div>");
        sb.append(content);
        sb.append("</div></section></section>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public SubAdapter initCommentTitle() {
        Log.d(TAG, "============ initCommentTitle ==========: ");
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(0);
        mCommentTitleAdapter = new SubAdapter(parentActivity, linearLayoutHelper, 1, R.layout.comment_title_detail, Constant.NewsDetailViewType.typeCommentTitle){
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                currentCommentCount = parentActivity.getResources().getString(R.string.comment_de);
                TextView commentTv = holder.getView(R.id.comment_tv);
                if (mCommentList != null){
                    commentTv.setText(String.format(currentCommentCount,  mCommentList.size()));
                }else {
                    commentTv.setText(parentActivity.getResources().getString(R.string.comment0_de));
                }

            }

            @Override
            public int getItemCount() {
                return 1;
            }
        };
        return mCommentTitleAdapter;
    }

    @Override
    public SubAdapter initCommentList() {
        Log.d(TAG, "============ initCommentList ==========: ");
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(0);

        mCommentAdapter = new SubAdapter(parentActivity, linearLayoutHelper, mCommentList == null ? 1 : mCommentList.size(),
                R.layout.item_detail_comment, Constant.NewsDetailViewType.typeComment){

            @Override
            public void onBindViewHolder(BaseViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
                Log.d(TAG, "============ initCommentList onBindViewHolder ==========: ");
                if (mCommentList == null){
                    holder.getView(R.id.empty_view).setVisibility(View.VISIBLE);
                    holder.getView(R.id.content_news).setVisibility(View.GONE);     //评论内容

                }else {
                    holder.getView(R.id.empty_view).setVisibility(View.GONE);
                    holder.getView(R.id.content_news).setVisibility(View.VISIBLE);     //评论内容

                    final CommentData.NewsModel model = mCommentList.get(position);
                    Log.d(TAG, "*********** Reply comment *************** "+model.getTwoArticleCommentList().size());


                    // 回复内容列表（二级评论）数据加载
                    // 每个一级评论下各自维护一个二级评论列表　所以 new CommentAdapter.
                    mCommentReplyAdapter = new CommentAdapter(parentActivity);
                    if (model.getTwoArticleCommentList() != null && model.getTwoArticleCommentList().size()>0){
                        Log.d(TAG, "*********** Reply comment: "+ model.getTwoArticleCommentList().size());
                        mCommentReplyAdapter.setDataList(model.getTwoArticleCommentList());
                        mCommentReplyAdapter.notifyDataSetChanged();
                    }


                    //get View
                    ImageView userPic = holder.getView(R.id.articleImg);     // 用户头像
                    TextView userName = holder.getView(R.id.name);           // 用户名
                    final ImageView thumbIcon = holder.getView(R.id.thumb);  // 点赞图标
                    TextView thumbCount = holder.getView(R.id.thumb_count);  // 点赞数
                    TextView content = holder.getView(R.id.content);         // 评论内容
                    TextView date = holder.getView(R.id.date);               // 评论日期
                    final TextView reply = holder.getView(R.id.reply);             // 回复
                    final RecyclerView recyclerView = holder.getView(R.id.recyclerView); // 回复内容列表（二级评论）
                    initCommentLevel2RecyclerView(recyclerView);                         // 回复内容列表（二级评论）


                    Log.d(TAG, "CurrentCommentID: "+ model.getCommentId());
                    Log.d(TAG, "getLikeTimes: "+ model.getLikeTimes());
                    // set Data
                    // ImageUtils.loadImgByPicasso(this, model.getArticleId(), userPic); // 暂时没有用户头像
                    userName.setText(model.getUseName());



                    // 点赞
                    thumbIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (model.isThumb()){
                                Log.d(TAG, "model.isThumb(): true -> return "+ model.isThumb());
                                return;
                            }
                            setCommentId(model.getCommentId());  // 设置当前评论的 id
                            setCommentMode(COMMENT_THUMB);       // 点赞模式
                            setCurrentCommentPosition(position); // 当前位置
                            Log.d(TAG, "POSITION: "+position);

                            Log.d(TAG, "model.isThumb(): "+ model.isThumb());
                            model.setThumb(!model.isThumb());
                            if (model.isThumb()){
                                // 点赞
                                getLikeCommentData();

                            }else {
                                // 撤销点赞
                            }
                            //notifyItemChanged(position);
                        }
                    });
                    if (model.isThumb()){
                        Log.d(TAG, "model.isThumb(): "+ model.isThumb());
                        thumbIcon.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.thumb_lighten));
                    }else {
                        Log.d(TAG, "model.isThumb(): "+ model.isThumb());
                        thumbIcon.setImageDrawable(parentActivity.getResources().getDrawable(R.drawable.thumb));
                    }
                    thumbCount.setText(model.getLikeTimes());

                    content.setText(model.getContent());
                    date.setText(model.getDateTimeStr());

                    // 回复
                    reply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setCommentId(model.getCommentId());  // 设置当前评论的 id
                            setCommentMode(COMMENT_LEVEL_2);     // 回复模式
                            setCurrentCommentPosition(position); // 当前位置
                            mView.showKeyboard();                // 弹出软键盘　－输入回复内容（二级评论）
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return mCommentList == null ? 1 : mCommentList.size();
            }
        };
        return mCommentAdapter;
    }

    // init 二级评论列表
    private void initCommentLevel2RecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mCommentReplyAdapter);
    }

    @Override
    public SubAdapter initRecommendTitle() {
        Log.d(TAG, "============ initRecommendTitle ==========: ");
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(0);
        mRecommendTitleAdapter = new SubAdapter(parentActivity, linearLayoutHelper, 1, R.layout.recommend_title_detail, Constant.NewsDetailViewType.typeRecommendTitle);
        return mRecommendTitleAdapter;
    }

    @Override
    public SubAdapter initRecommendList() {
        Log.d(TAG, "============ initRecommendList ==========: ");
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(0);
        mRecommendAdapter = new SubAdapter(parentActivity, linearLayoutHelper, mRecommendList == null ? 0 : mRecommendList.size(),
                R.layout.item_home_news, Constant.NewsDetailViewType.typeRecommend){
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Log.d(TAG, "============ initRecommendList onBindViewHolder ==========: ");
                if (mRecommendList != null ){
                    holder.getView(R.id.empty_view).setVisibility(View.GONE);
                    holder.getView(R.id.content_news).setVisibility(View.VISIBLE);     //新闻内容

                    final RecommendData.NewsModel model = mRecommendList.get(position);
                    // get View
                    LinearLayout linear = holder.getView(R.id.layout_item_news);  // 新闻条目
                    ImageView imageView1 = holder.getView(R.id.articleImg1);        // 新闻图片
                    RoundImageView imageView = holder.getView(R.id.articleImg);        // 新闻图片(圆角)
                    // imageView.setRadian(50);

                    TextView title = holder.getView(R.id.title);                  // 新闻标题
                    TextView viewTimes = holder.getView(R.id.viewTimes);          // 浏览次数
                    TextView author = holder.getView(R.id.author);                // 新闻作者
                    TextView date = holder.getView(R.id.date_news);               // 新闻日期

                    //set Data
                    linear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.printCloseableInfo(TAG, "onClick model.getId(): " + model.getId());
                            NewsDetailActivity.startNewsDetailActivity(parentActivity, model.getId());
                            //Toast.makeText(parentActivity, "news item clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ImageUtils.loadImgByPicassoPerson(parentActivity, model.getArticleImg(), R.drawable.img_default, imageView1);
                    ImageUtils.loadImgByPicasso(parentActivity, model.getArticleImg(), imageView);


                    title.setText(model.getTitle());
                    viewTimes.setText(model.getViewTimes());
                    author.setText(model.getAuthor());
                    date.setText(model.getShorDate());
                }else {
                    holder.getView(R.id.empty_view).setVisibility(View.VISIBLE);
                    holder.getView(R.id.content_news).setVisibility(View.GONE);     //新闻内容
                    TextView tv = holder.getView(R.id.empty_tv);
                    tv.setText(parentActivity.getResources().getString(R.string.recommend_null_de));

                }


            }

            @Override
            public int getItemCount() {
                return mRecommendList == null ? 0 : mRecommendList.size();
            }
        };
        return mRecommendAdapter;
    }

    @Override
    public SubAdapter initFooter() {
        Log.d(TAG, "============ initNewsDetail ==========: ");
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(0);
        mFooterAdapter = new SubAdapter(parentActivity, linearLayoutHelper, 1, R.layout.foot_detail, Constant.NewsDetailViewType.typeFooter);
        return mFooterAdapter;
    }


}
