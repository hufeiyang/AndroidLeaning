package com.hfy.demo01.module.jetpack.viewmodel.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FragmentUtils;
import com.hfy.demo01.R;

/**
 * Activity内部的多个Fragment 通过ViewModel 共享数据
 */
public class FragmentShareActivity extends AppCompatActivity {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, FragmentShareActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_share);

        FragmentUtils.add(getSupportFragmentManager(), MyListFragment.newInstance(), R.id.fl_list);
        FragmentUtils.add(getSupportFragmentManager(), DetailFragment.newInstance() , R.id.fl_detail);
    }


}