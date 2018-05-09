package com.example.jason.examination.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.example.jason.examination.R;
import com.example.jason.examination.base.BaseActivity;
import com.example.jason.examination.data.BookList;
import com.example.jason.examination.utils.PermissionHelper;
import com.example.jason.examination.utils.ToastHelper;
import com.example.jason.examination.utils.db.DBBookListUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ivMainActivityMenu) ImageView ivMainActivityMenu;
    @BindView(R.id.ivMainActivityCamera) ImageView ivMainActivityCamera;
    @BindView(R.id.barTitle) Toolbar barTitle;
    @BindView(R.id.nvMainActivity) NavigationView nvMainActivity;
    @BindView(R.id.dlMain) DrawerLayout dlMain;
    @BindView(R.id.edt_search_main_activity) EditText edtSearchMainActivity;
    @BindView(R.id.tv_search_main_activity) TextView tvSearchMainActivity;
    @BindView(R.id.rl_search_main_activity) RelativeLayout rlSearchMainActivity;

    @BindView(R.id.btn_story_main_activity) Button btnStoryMainActivity;
    @BindView(R.id.btn_history_main_activity) Button btnHistoryMainActivity;
    @BindView(R.id.btn_philosophy_main_activity) Button btnPhilosophyMainActivity;
    @BindView(R.id.btn_politics_main_activity) Button btnPoliticsMainActivity;
    @BindView(R.id.btn_finance_main_activity) Button btnFinanceMainActivity;
    @BindView(R.id.btn_art_main_activity) Button btnArtMainActivity;
    @BindView(R.id.btn_technology_main_activity) Button btnTechnologyMainActivity;
    @BindView(R.id.btn_building_main_activity) Button btnBuildingMainActivity;
    @BindView(R.id.btn_english_cet_main_activity) Button btnEnglishCetMainActivity;
    @BindView(R.id.btn_collection_main_activity) Button btnCollectionMainActivity;

    private ImageView ivMainDrawerBg;
    private ImageView ivMainDrawerUserAvatar;
    private ImageView ivMainDrawerSex;
    private TextView tvMainDrawerNickname;
    private TextView tvMainDrawerNotUploadVideoCount;
    private TextView tvMainDrawerAttention;
    private LinearLayout llMainDrawerVideo;
    private View headView;
    private ImageView ivMainDrawerNotLoginUserAvatar;
    private long firstBack = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        //有权限自然会走onGranted 没权限会走 onDenied
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.PHONE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        PermissionHelper.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        LogUtils.d("有权限自然会走onGranted");
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            PermissionHelper.showOpenAppSettingDialog();
                            LogUtils.d("permissionsDeniedForever.isEmpty()  没权限会走 onDenied");
                        }
                        LogUtils.d(permissionsDeniedForever, permissionsDenied);
                    }
                })
                .theme(new PermissionUtils.ThemeCallback() {
                    @Override
                    public void onActivityCreate(Activity activity) {
                        ScreenUtils.setFullScreen(activity);// 设置全屏
                        LogUtils.d("onActivityCreate");
                    }
                })
                .request();

        getBookData();
    }

    private void initView() {
        headView = nvMainActivity.inflateHeaderView(R.layout.main_activity_drawerlayout_header_layout);
        ivMainDrawerNotLoginUserAvatar = (ImageView) headView.findViewById(R.id.ivMainDrawerNotLoginUserAvatar);
        ivMainDrawerUserAvatar = (ImageView) headView.findViewById(R.id.ivMainDrawerUserAvatar);
        ivMainDrawerSex = (ImageView) headView.findViewById(R.id.ivMainDrawerSex);
        tvMainDrawerNickname = (TextView) headView.findViewById(R.id.tvMainDrawerNickname);
        tvMainDrawerNotUploadVideoCount = (TextView) headView.findViewById(R.id.tvMainDrawerNotUploadVideoCount);
        tvMainDrawerAttention = (TextView) headView.findViewById(R.id.tvMainDrawerAttention);
        llMainDrawerVideo = (LinearLayout) headView.findViewById(R.id.llMainDrawerVideo);

        headView.findViewById(R.id.flMainDrawerUser).setOnClickListener(this);
        llMainDrawerVideo.setOnClickListener(this);
        headView.findViewById(R.id.llMainDrawerNews).setOnClickListener(this);
        headView.findViewById(R.id.llMainDrawerFeedback).setOnClickListener(this);
        headView.findViewById(R.id.llMainDrawerSetting).setOnClickListener(this);
        headView.findViewById(R.id.ivMainDrawerUserAvatar).setOnClickListener(this);
        headView.findViewById(R.id.llMainDrawerLogin).setOnClickListener(this);
        ivMainActivityMenu.setOnClickListener(this);
        ivMainActivityCamera.setOnClickListener(this);
        tvSearchMainActivity.setOnClickListener(this);
        btnEnglishCetMainActivity.setOnClickListener(this);
        btnCollectionMainActivity.setOnClickListener(this);
        btnStoryMainActivity.setOnClickListener(this);
        btnHistoryMainActivity.setOnClickListener(this);
        btnPhilosophyMainActivity.setOnClickListener(this);
        btnPoliticsMainActivity.setOnClickListener(this);
        btnFinanceMainActivity.setOnClickListener(this);
        btnArtMainActivity.setOnClickListener(this);
        btnTechnologyMainActivity.setOnClickListener(this);
        btnBuildingMainActivity.setOnClickListener(this);

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            tvMainDrawerNickname.setText(bmobUser.getUsername());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMainActivityMenu:
                openDrawer();
                break;
            case R.id.ivMainActivityCamera:
                break;
            case R.id.flMainDrawerUser:
                //进入自己的主页
                closeDrawer();
                break;
            case R.id.ivMainDrawerUserAvatar:
                //进入自己的主页
                closeDrawer();
                break;
            case R.id.llMainDrawerVideo:
                //视频管理页
//                toActivity(VideoManageActivity.class);
                closeDrawer();
                break;
            case R.id.llMainDrawerNews:
                //消息通知页
                // toActivity(MessageActivity
                // .class);
                closeDrawer();
                break;
            case R.id.llMainDrawerFeedback:
                //系统反馈
//                toActivity(FeedbackActivity.class);
                closeDrawer();
                break;
            case R.id.llMainDrawerSetting:
                //设置页
                //  toActivity(SettingActivity.class);
                closeDrawer();
                break;
            case R.id.llMainDrawerLogin:
                //登陆页
                toActivity(LoginActivity.class);
                closeDrawer();
                break;

            case R.id.btn_story_main_activity:
                //小说
                intentToBookList("Biography");
                break;
            case R.id.btn_history_main_activity:
                //历史
                intentToBookList("ShortStory");
                break;
            case R.id.btn_philosophy_main_activity:
                //哲学
                intentToBookList("Novel");
                break;
            case R.id.btn_politics_main_activity:
                //政治
                intentToBookList("Inspirational");
                break;
            case R.id.btn_finance_main_activity:
                //财经
                intentToBookList("Science");
                break;
            case R.id.btn_art_main_activity:
                //美术
                intentToBookList("Children");
                break;
            case R.id.btn_technology_main_activity:
                //技术
                intentToBookList("Religious");
                break;
            case R.id.btn_building_main_activity:
                //建筑
                intentToBookList("CET4");
                break;

            case R.id.btn_english_cet_main_activity:
                //英语四六级
                intentToBookList("CET6");
                break;

            case R.id.btn_collection_main_activity:
                //已阅读的书籍
                intentToBookList("MyBooks");
                break;
            default:
                break;
        }
    }

    public void intentToBookList(String classification) {
        Intent intent = new Intent(this, BookListActivity.class);
        intent.putExtra("intentToBookListActivity", classification);
        startActivity(intent);
    }

    private boolean isDrawerOpen() {
        return dlMain.isDrawerOpen(Gravity.LEFT);
    }

    private void openDrawer() {
        //打开抽屉
        if (!isDrawerOpen()) {
            dlMain.openDrawer(Gravity.LEFT);
        }
    }


    private void closeDrawer() {
        doInUI(new Runnable() {
            @Override
            public void run() {
                //关闭抽屉
                if (isDrawerOpen()) {
                    dlMain.closeDrawer(Gravity.LEFT);
                    dlMain.closeDrawers();
                }
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            if (System.currentTimeMillis() - firstBack < 2000) {
                super.onBackPressed();
            } else {
                firstBack = System.currentTimeMillis();
                ToastHelper.showShortMessage(R.string.quit_app);
            }
        }
    }

    public void getBookData() {
        BmobQuery<BookList> query = new BmobQuery<BookList>();
        // 按时间降序查询
        query.order("-createdAt");
        query.setLimit(50);
        query.findObjects(new FindListener<BookList>() {
            @Override
            public void done(List<BookList> list, BmobException e) {
                boolean isContent = false;
                if (e == null) {
                    LogUtils.d("getMediaData success = " + list.size());
                    List<BookList> bookListListDB = DBBookListUtils.getInstance().queryAllData();

                    if (bookListListDB.size() == 0) {
                        DBBookListUtils.getInstance().insertManyData(list);
                    } else {
                        for (BookList bookList : list) {
                            if (!isContent(bookList, bookListListDB)) {
                                DBBookListUtils.getInstance().insertOneData(bookList);
                            }
                        }
                    }
                    LogUtils.d("getBookData  DB =  " + DBBookListUtils.getInstance().queryAllData().size());
                }
            }
        });
    }

    public boolean isContent(BookList bookList, List<BookList> bookListListDB) {
        for (BookList bookListdb : bookListListDB) {
            if ((bookList.getId().equals(bookListdb.getId()))) {
                return true;
            }
        }

        return false;
    }
}
