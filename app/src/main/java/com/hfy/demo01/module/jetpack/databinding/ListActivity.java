package com.hfy.demo01.module.jetpack.databinding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hfy.demo01.R;
import com.hfy.demo01.databinding.ActivityListBinding;
import com.hfy.demo01.databinding.ItemUserBinding;
import com.hfy.demo01.module.jetpack.databinding.bean.User;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用DataBinding绑定列表数据
 */
public class ListActivity extends AppCompatActivity {

    private ActivityListBinding mViewDataBinding;
    private static UserListAdapter mAdapter;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        mViewDataBinding.rvUserList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mAdapter = new UserListAdapter();
        mAdapter.setNewInstance(getUserList());
        mViewDataBinding.rvUserList.setAdapter(mAdapter);

        mViewDataBinding.setClickPresenter(new ClickPresenter());
    }


    private List<User> getUserList() {
        List<User> list = new ArrayList<>();
        list.add(new User("小明","Lv1"));
        list.add(new User("小红","Lv2"));
        list.add(new User("小q","Lv3"));
        list.add(new User("小a","Lv4"));
        return list;
    }


    /**
     * 点击监听处理
     */
    public class ClickPresenter {

        public void addUser(View view) {
            Toast.makeText(ListActivity.this, "addUser", Toast.LENGTH_SHORT).show();
            mAdapter.addData(new User("小z","Lv5"));
        }

        public void removeUser(View view) {
            Toast.makeText(ListActivity.this, "removeUser", Toast.LENGTH_SHORT).show();
            mAdapter.remove(0);
        }
    }

    private static class UserListAdapter extends BaseQuickAdapter<User, UserItemViewHolder> {

        public UserListAdapter() {
            super(R.layout.item_user);
        }

        @Override
        protected void convert(@NonNull UserItemViewHolder holder, User user) {
            // 精髓所在1，不需要去一个个setText等等
            holder.getItemUserBinding().setUser(user);
            holder.getItemUserBinding().executePendingBindings();

            //当获取的DataBinding不是具体类时，只是ViewDataBinding，那就要使用setVariable了
//            holder.getViewDataBinding().setVariable(BR.user, user);
//            holder.getViewDataBinding().executePendingBindings();
        }
    }

    private static class UserItemViewHolder extends BaseViewHolder {

        // 精髓所在2，只需要持有 binding即可，不用去findViewById
        private final ItemUserBinding binding;
//        private final ViewDataBinding binding2;

        public UserItemViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
//            binding2 = DataBindingUtil.bind(view);
        }

        public ItemUserBinding getItemUserBinding() {
            return binding;
        }

//        public ViewDataBinding getViewDataBinding() {
//            return binding2;
//        }
    }

}