package com.example.jason.examination.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.example.jason.examination.R;
import com.example.jason.examination.adapter.BookListAdapter;
import com.example.jason.examination.base.BaseActivity;
import com.example.jason.examination.data.BookList;
import com.example.jason.examination.event.DeleteBookEvent;
import com.example.jason.examination.utils.ToastHelper;
import com.example.jason.examination.utils.db.DBBookListUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class BookListActivity extends BaseActivity {

    @BindView(R.id.rlv_book_list_activity) RecyclerView mRecyclerView;
    @BindView(R.id.tv_title_book_list_activity) TextView tvTitleBookListActivity;
    @BindView(R.id.iv_search_book_list_activity) ImageView ivSearchBookListActivity;
    @BindView(R.id.barTitle) Toolbar barTitle;
    @BindView(R.id.edt_search_book_list_activity) EditText edtSearchBookListActivity;
    @BindView(R.id.tv_search_book_list_activity) TextView tvSearchBookListActivity;
    @BindView(R.id.rl_search_book_list_activity) RelativeLayout rlSearchBookListActivity;

    private List<BookList> bookLists = new ArrayList<>();
    private List<BookList> mGetDateFromServiceBookLists = new ArrayList<>();
    private List<BookList> mNameSearchBookLists = new ArrayList<>();
    private List<BookList> mWriteNameSearchBookLists = new ArrayList<>();
    private String title;
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        title = getIntent().getStringExtra("intentToBookListActivity");
        tvTitleBookListActivity.setText(title);
        if ("已阅读的书籍".equals(title)) {
            bookLists = DBBookListUtils.getInstance().queryUserDependIsRead(true);
        } else {
            bookLists = DBBookListUtils.getInstance().queryUserDependlassification(title);
        }
        if (bookLists.size() == 0) {
            ToastHelper.showShortMessage("此分类未有任何书籍");
        } else {
            initRecyclerView();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveDeleteFriendEvent(DeleteBookEvent event) {
        LogUtils.d("InstagramDialog receiveDeleteFriendEvent send Event = ");
        if ("已阅读的书籍".equals(title)) {
            bookLists = DBBookListUtils.getInstance().queryUserDependIsRead(true);
        }
        if (bookLists.size() == 0) {
            ToastHelper.showShortMessage("此分类未有任何书籍");
            initRecyclerView();
        } else {
            initRecyclerView();
        }

    }

    @OnClick(R.id.iv_search_book_list_activity)
    public void imageSearchClicked() {
        //点击搜索Incon
        if (rlSearchBookListActivity.getVisibility() == View.VISIBLE) {
            rlSearchBookListActivity.setVisibility(View.GONE);
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            rlSearchBookListActivity.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_search_book_list_activity)
    public void startSearchClicked() {
        //点击搜索开始文案
        if (TextUtils.isEmpty(edtSearchBookListActivity.getText().toString())) {
            ToastHelper.showShortMessage("请输入用书名再点击查询");
        } else {
            mNameSearchBookLists = DBBookListUtils.getInstance().queryUserDependBookName(edtSearchBookListActivity.getText().toString());
            bookLists.clear();
            bookLists.addAll(mNameSearchBookLists);
            if (bookLists.size() > 0) {
                initRecyclerView();
            } else {
                ToastHelper.showShortMessage("未搜索到与之匹配的书");
            }
        }
    }

    @OnTextChanged(R.id.edt_search_book_list_activity)
    public void onSearchTextChanged() {
        if (TextUtils.isEmpty(edtSearchBookListActivity.getText().toString())) {
            bookLists.clear();
            bookLists.addAll(DBBookListUtils.getInstance().queryUserDependlassification(title));
            initRecyclerView();
        }
    }

    public void initRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        bookListAdapter = new BookListAdapter(bookLists, this);
        bookListAdapter.setTitle(title);
        mRecyclerView.setAdapter(bookListAdapter);
    }
}
