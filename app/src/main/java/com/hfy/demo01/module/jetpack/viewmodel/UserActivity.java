package com.hfy.demo01.module.jetpack.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hfy.demo01.R;

/**
 * ViewModel 实现 MVVM 架构
 * 1、activity内不存数据，数据都在ViewModel中
 * 2、ViewModel中不持有Activity引用
 * 3、activity没有业务逻辑
 *
 * 4、ViewModel在屏幕后不会销毁，和重建的Activity依然联系
 * 5、Activity/fragment 使用ViewModel可以通信
 */
public class UserActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, UserActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_m_v_v_m_test);

        Log.i(TAG, "onCreate: ");


        TextView tvUserName = findViewById(R.id.textView);
        ProgressBar pbLoading = findViewById(R.id.pb_loading);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        UserViewModel userViewModel = viewModelProvider.get(UserViewModel.class);

        //观察 用户信息
        userViewModel.getUserLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // update ui.
                tvUserName.setText(s);
            }
        });

        userViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                pbLoading.setVisibility(aBoolean?View.VISIBLE:View.GONE);
            }
        });

        //点击按钮获取用户信息
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUserInfo();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
