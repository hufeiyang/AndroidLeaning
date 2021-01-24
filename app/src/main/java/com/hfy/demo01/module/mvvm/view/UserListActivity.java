package com.hfy.demo01.module.mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hfy.demo01.R;
import com.hfy.demo01.module.jetpack.databinding.ListActivity;
import com.hfy.demo01.module.mvvm.model.User;
import com.hfy.demo01.module.mvvm.viewmodel.UserListViewModel;

import java.util.List;

/**
 * MVVM 的View层（含xml）
 */
public class UserListActivity extends AppCompatActivity {

    private UserListViewModel mUserListViewModel;
    private ProgressBar mProgressBar;
    private RecyclerView mRvUserList;
    private UserAdapter mUserAdapter;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, UserListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        initView();

        initViewModel();

        getData();

        observeLivaData();
    }

    private void initView() {
        mProgressBar = findViewById(R.id.pb_loading_users);
        mRvUserList = findViewById(R.id.rv_user_list);

        mRvUserList.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(R.layout.item_user_mvvm);
        mRvUserList.setAdapter(mUserAdapter);
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        mUserListViewModel = viewModelProvider.get(UserListViewModel.class);
    }

    /**
     * 获取数据，调用ViewModel的方法获取
     */
    private void getData() {
        mUserListViewModel.getUserInfo();
    }

    /**
     * 观察ViewModel的数据，且此数据 是 View 直接需要的，不需要再做逻辑处理
     */
    private void observeLivaData() {
        mUserListViewModel.getUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users == null) {
                    Toast.makeText(UserListActivity.this, "获取user失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mUserAdapter.setNewInstance(users);
            }
        });

        mUserListViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mProgressBar.setVisibility(aBoolean? View.VISIBLE:View.GONE);
            }
        });
    }


}