package com.hfy.demo01.module.jetpack.databinding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.BR;
import com.hfy.demo01.module.jetpack.databinding.bean.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionManager;

import com.hfy.demo01.R;
import com.hfy.demo01.databinding.ActivityDataBindingLearningBinding;

import java.util.Date;

/**
 * DataBinding 基础 学习
 */
public class DataBindingLearningActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, DataBindingLearningActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_data_binding_learning);

        //这里使用DataBindingUtil.setContentView()
        //ActivityMvvmBinding是在写好<layout>布局后,make project,自动生成的Binding辅助类
//        ActivityDataBindingLearningBinding inflate = ActivityDataBindingLearningBinding.inflate(getLayoutInflater());
//        setContentView(inflate.getRoot());

        ActivityDataBindingLearningBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_learning);

//        binding.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//            }
//        });
//        binding.addOnRebindCallback(new OnRebindCallback() {
//            @Override
//            public boolean onPreBind(ViewDataBinding binding) {
//                ViewGroup view = (ViewGroup)binding.getRoot();
//                //实现 动画
//                TransitionManager.beginDelayedTransition(view);
//                return super.onPreBind(binding);
//            }
//            @Override
//            public void onCanceled(ViewDataBinding binding) {
//                super.onCanceled(binding);
//            }
//            @Override
//            public void onBound(ViewDataBinding binding) {
//                super.onBound(binding);
//            }
//        });

        //用于测试，TestDataBindingComponent
//        DataBindingUtil.setDefaultComponent(new TestDataBindingComponent());

        LinearLayout llUser = binding.llUser;

        User user = new User("小明","Lv1");
        //值 改变 的回调监听
        user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.name) {
                    //name变化了
                }
            }
        });

        binding.setUser(user);

        binding.setTime(new Date());


        //事件处理方法一，binding回去控件再set
        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已绑定的数据实例 发生变化
                user.setLevel("Lv2");
            }
        });
//
        //事件处理方法二：赋值监听器实例
        binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定新的数据实例
                User user = new User("段誉","爱情高手");
                binding.setUser(user);
            }
        });

        //事件处理方法三：引用同名同参方法
        //事件处理方法三：引用自定义方法
        binding.setClickPresenter(new ListenerPresenter());

        //结合DataBinding使用的ViewModel
        //要使用LiveData对象作为数据绑定来源，需要设置LifecycleOwner
        binding.setLifecycleOwner(this);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        mUserViewModel = viewModelProvider.get(UserViewModel.class);
        //设置变量ViewModel
        binding.setVm(mUserViewModel);
    }

    /**
     * 各种监听处理
     */
    public class ListenerPresenter {
        /**
         * 原有同参方法
         * @param view
         */
        public void onClick(View view){
            Toast.makeText(DataBindingLearningActivity.this, "监听处理：同参方法", Toast.LENGTH_SHORT).show();
        }

        /**
         * 自定义方法
         * @param user
         */
        public void onBindingClick(User user){
            Toast.makeText(DataBindingLearningActivity.this, "监听处理：lambda 方法应用，参数："+ user.getName(), Toast.LENGTH_SHORT).show();
        }

        /**
         * 自定义方法
         * @param
         */
        public void onGetUser(View view){
            mUserViewModel.getUser();
        }

    }




}
