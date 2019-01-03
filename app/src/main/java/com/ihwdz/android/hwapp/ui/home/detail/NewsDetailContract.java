package com.ihwdz.android.hwapp.ui.home.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.ihwdz.android.hwapp.base.adapter.SubAdapter;
import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;


/**
 * <pre>
 * author : Duan
 * time : 2018/09/01
 * desc :
 * version: 1.0
 * </pre>
 */
public interface NewsDetailContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void initRecyclerView();

        void showBackTopIcon();   // 显示返回顶部按钮
        void hideBackTopIcon();

        void showKeyboard(); //　弹出软键盘
        void hideKeyboard();

        void updateEditBox();
        void updateCollectionIcon();
        void showPromptMessage(String message);
        void showErrorMsg(String message);
    }


    int COMMENT_LEVEL_1 = 0;
    int COMMENT_LEVEL_2 = 1;
    int COMMENT_THUMB = 2;

    int LOAD_FIRST = 0;
    int LOAD_MORE = 1;

    int PageNum = 1;
    int PageSize = 15;
    interface Presenter extends BasePresenter {

        void setCurrentId(String id);
        String getCurrentId();


        void setEditText(String editText);      // 编辑评论内容
        String getEditText();                   // 获取评论内容

        void setCommentId(String commentId);
        String getCommentId();

        void setCurrentCommentPosition(int position); // 方便对适配器更该单条数据
        int getCurrentCommentPosition();

        void setCommentMode(int mode);                // 设置提交模式（一级评论/ 二级评论/ 点赞）
        int getCommentMode();

        void setIsThumbClicked(boolean isThumbClicked);
        boolean getIsThumbClicked();

        //void refreshData();

        void getNewsDetailData();       // 详情数据
        void getCommentData();          // 精彩评论数据
        void getRecommendData();        // 热门推荐数据

        void postCommentLevel1Data();   // 一级评论
        void postCommentLevel2Data();   // 二级评论
        void getLikeCommentData();      // 点赞

        void doComment(String comment);  // 提交评论
        void doShare();                  // 分享
        void doCollect();                // 收藏

        void gotoLoginPage();            // 登陆/注册

        DelegateAdapter initRecyclerView(RecyclerView recyclerView);
        SubAdapter initNewsDetail();      // 详情
        SubAdapter initCommentTitle();    // 精彩评论标题
        SubAdapter initCommentList();     // 精彩评论
        SubAdapter initRecommendTitle();  // 热门推荐标题
        SubAdapter initRecommendList();   // 热门推荐
        SubAdapter initFooter();          // 尾布局:　让 bottom bar　遮不住最后的 item
    }

}
