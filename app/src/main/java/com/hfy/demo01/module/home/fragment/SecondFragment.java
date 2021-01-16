package com.hfy.demo01.module.home.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hfy.demo01.R;
import com.hfy.demo01.module.home.glide.GlideTestActivity;
import com.hfy.demo01.module.home.okhttp.OkHttpTestActivity;
import com.hfy.demo01.module.jetpack.LifecycleTestActivity;
import com.hfy.demo01.module.jetpack.old.LocationActivity;
import com.hfy.demo01.module.jetpack.viewmodel.UserActivity;
import com.hfy.demo01.module.jetpack.viewmodel.fragment.FragmentShareActivity;
import com.hfy.demo01.module.databinding.DataBindingActivity;

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
            R.id.btn_fragment_share_view_model
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mvvm_data_binding_test:
                DataBindingActivity.launch(getActivity());
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
            default:
                break;
        }
    }

}
