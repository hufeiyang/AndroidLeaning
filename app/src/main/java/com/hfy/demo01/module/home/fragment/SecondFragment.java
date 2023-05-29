package com.hfy.demo01.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hfy.demo01.R;
import com.hfy.demo01.module.home.glide.GlideTestActivity;
import com.hfy.demo01.module.home.notification.AppInnerNotificationActivity;
import com.hfy.demo01.module.home.okhttp.OkHttpTestActivity;
import com.hfy.demo01.module.jetpack.databinding.DataBindingLearningActivity;
import com.hfy.demo01.module.jetpack.databinding.ListActivity;
import com.hfy.demo01.module.jetpack.lifecycle_livedata.LifecycleTestActivity;
import com.hfy.demo01.module.jetpack.lifecycle_livedata.old.LocationActivity;
import com.hfy.demo01.module.jetpack.viewmodel.UserActivity;
import com.hfy.demo01.module.jetpack.viewmodel.fragment.FragmentShareActivity;
import com.hfy.demo01.module.mvvm.view.UserListActivity;
import com.hfy.secondfloor.SecondFloorTest1Activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SecondFragment extends Fragment {

    @BindView(R.id.btn_mvvm_data_binding_test)
    Button mButton;
    private Unbinder mUnbind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_second, null);

        mUnbind = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_mvvm_data_binding_test,
            R.id.btn_glide_test,
            R.id.btn_okhttp_test,
            R.id.btn_ui_test,
            R.id.btn_optimize_test,
            R.id.btn_lifecycle_old_test,
            R.id.btn_lifecycle_test,
            R.id.btn_mvvm_view_model_test,
            R.id.btn_fragment_share_view_model,
            R.id.btn_data_binding_list_test,
            R.id.btn_mvvm,
            R.id.btn_second_floor_test1,
            R.id.btn_app_inner_notification
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_notification:
                goToNotificationSettingsPage(this.getActivity());
                break;
            case R.id.btn_mvvm_data_binding_test:
                DataBindingLearningActivity.launch(getActivity());
                break;
            case R.id.btn_data_binding_list_test:
                ListActivity.launch(getActivity());
                break;
            case R.id.btn_glide_test:
                GlideTestActivity.launch(getActivity());
                break;
            case R.id.btn_okhttp_test:
                OkHttpTestActivity.launch(getActivity());
                break;

            case R.id.btn_ui_test:
                UITestActivity.launch(getActivity());
                break;

            case R.id.btn_optimize_test:
                OptimizeActivity.launch(getActivity());
                break;
            case R.id.btn_lifecycle_old_test:
                LocationActivity.launch(getActivity());
                break;
            case R.id.btn_lifecycle_test:
                LifecycleTestActivity.launch(getActivity());
                break;
            case R.id.btn_mvvm_view_model_test:
                UserActivity.launch(getActivity());
                break;
            case R.id.btn_fragment_share_view_model:
                FragmentShareActivity.launch(getActivity());
                break;
            case R.id.btn_mvvm:
                UserListActivity.launch(getActivity());
                break;
            case R.id.btn_second_floor_test1:
                SecondFloorTest1Activity.Companion.launch(getActivity());
                break;
            case R.id.btn_app_inner_notification:
                AppInnerNotificationActivity.Companion.launch(getActivity());
                break;
            default:
                break;
        }
    }


    /**
     * 假设没有开启通知权限，点击之后就需要跳转到 APP的通知设置界面，对应的Action是：Settings.ACTION_APP_NOTIFICATION_SETTINGS, 这个Action是 API 26 后增加的
     * 如果在部分手机中无法精确的跳转到 APP对应的通知设置界面，那么我们就考虑直接跳转到 APP信息界面，对应的Action是：Settings.ACTION_APPLICATION_DETAILS_SETTINGS*/
    public static void goToNotificationSettingsPage(Context context) {

        if (context == null) {
            return;
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);

        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
