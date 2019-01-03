package com.ihwdz.android.hwapp.ui.home.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.adapter.SubAdapter;
import com.ihwdz.android.hwapp.base.mvp.BaseActivity;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.utils.log.LogUtils;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.CURRENT_NEWS;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HAS_BOTTOM;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HAS_MORE;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.HY_NEWS;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.INDUSTRY_FOCUSED;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.MARKET_COMMENT;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.NO_MORE;
import static com.ihwdz.android.hwapp.ui.home.HomeFragmentContract.PageSize;

public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View
//        , EditText.OnEditorActionListener
{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btn_back)
    ImageView backBt;

    @BindView(R.id.back_to_top)
    ImageView backToTop;

    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.collection_iv) ImageView collectionIv;     // 收藏按钮
    @BindView(R.id.share_iv) ImageView shareIv;               // 分享按钮


    String currentId = null;        //"197282";
    private List<DelegateAdapter.Adapter> mAdapters;

    RecyclerView.LayoutManager layoutManager;
    int distance = 0;                  // 滑动距离
    private int distance_top = 1080;   // 上滑一定距离显示“TOP BUTTON”　否则隐藏
    private Handler handler = new Handler();
    private boolean isScrollDown;      // 是否是向下滑动

    static String TAG = "NewsDetailActivity";
    static final String ID_SELECTED = "id";




    @Inject NewsDetailContract.Presenter mPresenter;

    public static void startNewsDetailActivity(Context context, String selectId) {
        Log.i(TAG, "=================================== startNewsDetailActivity ===================");
        Intent intent = new Intent(context, NewsDetailActivity.class);
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(ID_SELECTED, selectId);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        if (getIntent()!= null){
            if (getIntent().getStringExtra(ID_SELECTED) != null){
                currentId = getIntent().getStringExtra(ID_SELECTED);
            }
            Log.i(TAG, "===== getIntent ==== ID_SELECTED: " + currentId);
        }
        mPresenter.setCurrentId(currentId);
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }

    @Override
    public void initListener() {
        //editText.setOnEditorActionListener(this);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch(actionId){
//                    case EditorInfo.IME_ACTION_SEND:
//                        LogUtils.printInfo(TAG, "================= send : " + v.getText());
//                        hideKeyboard();
//                        mPresenter.doComment(v.getText().toString());
//                        return true;
//                }

                if (actionId == EditorInfo.IME_ACTION_SEND){
                    LogUtils.printInfo(TAG, "================= send : " + v.getText());
                    // hideKeyboard();
                    mPresenter.doComment(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getNewsDetailData();
        mPresenter.getCommentData();
        mPresenter.getRecommendData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        distance = 0;
    }

    @Override
    public void showWaitingRing() {

    }

    @Override
    public void hideWaitingRing() {

    }

    @Override
    public void initRecyclerView() {
        DelegateAdapter delegateAdapter = mPresenter.initRecyclerView(recyclerView);

        // 把 详情 添加到集合
        SubAdapter detailAdapter = mPresenter.initNewsDetail();
        mAdapters.add(detailAdapter);

        // 把 精彩评论-标题 添加到集合
        SubAdapter commentTitleAdapter = mPresenter.initCommentTitle();
        mAdapters.add(commentTitleAdapter);
        // 把 精彩评论 添加到集合
        SubAdapter commentAdapter = mPresenter.initCommentList();
        mAdapters.add(commentAdapter);

        // 把 热门推荐-标题 添加到集合
        SubAdapter recommendTitleAdapter = mPresenter.initRecommendTitle();
        mAdapters.add(recommendTitleAdapter);
        // 把 热门推荐 添加到集合
        SubAdapter recommendAdapter = mPresenter.initRecommendList();
        mAdapters.add(recommendAdapter);
        // 把 尾布局 添加到集合
        SubAdapter footerAdapter = mPresenter.initFooter();
        mAdapters.add(footerAdapter);

        // 设置适配器
        delegateAdapter.setAdapters(mAdapters);

        recyclerView.requestLayout();
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void showBackTopIcon() {
        backToTop.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBackTopIcon() {
        backToTop.setVisibility(View.GONE);
    }


    @Override
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null){
            editText.requestFocus();
            imm.showSoftInput(editText, 0);
        }
    }
    @Override
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
        //editText.setHint(getResources().getString(R.string.search_de));
    }

    @Override
    public void updateEditBox() {
        editText.setHint(getResources().getString(R.string.search_de));
    }

    @Override
    public void updateCollectionIcon() {
        collectionIv.setImageDrawable(getResources().getDrawable(R.drawable.star));
    }

    @Override
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMsg(String message) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }


    // 点击 返回按钮　
    @OnClick(R.id.btn_back)
    public void onBackBtClicked(){
        onBackPressed();
    }

    // 点击按钮回到顶部
    @OnClick(R.id.back_to_top)
    public void onBackToTopClicked(){
        layoutManager.scrollToPosition(0);
        distance = 0;
    }

    // 点击 收藏按钮
    @OnClick(R.id.collection_iv)
    public void onCollectionBtClicked(){
        if (Constant.LOGOUT ){
            mPresenter.gotoLoginPage();
        }else {
            mPresenter.doCollect();
        }
    }

    // 点击 分享按钮　
    @OnClick(R.id.share_iv)
    public void onShareBtClicked(){
        mPresenter.doShare();

    }




    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE ) {

                int lastVisibleItem;
                // 获取RecyclerView的LayoutManager
                layoutManager = recyclerView.getLayoutManager();
                // 获取到最后一个可见的item

                if (layoutManager instanceof LinearLayoutManager) {
                    // 如果是 LinearLayoutManager
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    // 如果是 StaggeredGridLayoutManager
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItem = findMax(into);
                } else {
                    // 否则抛出异常
                    throw new RuntimeException("Unsupported LayoutManager used");
                }

                int totalItemCount = layoutManager.getItemCount();
                /**
                 最后一个可见的item为最后一个item
                 是向下滑动
                 */
                if (lastVisibleItem >= totalItemCount - 1 && isScrollDown) {
                    // 说明滚动到底部,触发加载更多

                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
            distance += dy;
            LogUtils.printInfo(TAG, "distance: " + distance);
            // 上滑一定距离显示“TOP BUTTON”　否则隐藏
            if (distance > distance_top){
                showBackTopIcon();
            }else{
                hideBackTopIcon();
            }

            VirtualLayoutManager lm = (VirtualLayoutManager)recyclerView.getLayoutManager();
            int lastVisibleItem = lm.findLastVisibleItemPosition();
            LogUtils.printInfo(TAG, "lastVisibleItem: " + lastVisibleItem);

        }
    };
    /**
     * 获取数组中的最大值
     *
     * @param lastPositions 需要找到最大值的数组
     * @return 数组中的最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        switch(actionId){
////            case EditorInfo.IME_NULL:
////                Log.d(TAG, "================= Done_content: ======== editText" + v.getText());
////                break;
//
////            case EditorInfo.IME_ACTION_DONE:
////                // 回车键　－软键盘
////                LogUtils.printInfo(TAG, "================= action done: ======== editText: " + v.getText());
////                break;
//
//            case EditorInfo.IME_ACTION_SEND:
//                LogUtils.printInfo(TAG, "================= send a email: ======== editText" + v.getText());
//                mPresenter.doComment(v.getText().toString());
//                break;
//        }
//
//        hideKeyboard();
//
//        return true;
//    }

}
