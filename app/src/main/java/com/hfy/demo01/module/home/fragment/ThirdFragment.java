package com.hfy.demo01.module.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hfy.demo01.R;
import com.hfy.demo01.hook.TestHookActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ThirdFragment  extends Fragment {

    private Unbinder mUnbind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_third, null);

        mUnbind = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_learn_hook,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_learn_hook:
                TestHookActivity.launch(getActivity());
                break;
            default:
                break;
        }
    }

}