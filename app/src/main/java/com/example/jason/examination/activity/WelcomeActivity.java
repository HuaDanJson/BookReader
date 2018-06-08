package com.example.jason.examination.activity;

import android.os.Bundle;

import com.example.jason.examination.R;
import com.example.jason.examination.base.BaseActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //第一：默认初始化
        Bmob.initialize(this, "f57b092ef5fa0c28152abc0ca9106341");
        doInUI(new Runnable() {
            @Override
            public void run() {
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if (bmobUser != null) {
                    // 允许用户使用应用
                    toActivity(MainActivity.class);
                    WelcomeActivity.this.finish();
                } else {
                    toActivity(LoginActivity.class);
                    WelcomeActivity.this.finish();
                }
            }
        }, 50);
    }
}

