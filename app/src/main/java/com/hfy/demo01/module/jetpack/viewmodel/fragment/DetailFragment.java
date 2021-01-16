package com.hfy.demo01.module.jetpack.viewmodel.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hfy.demo01.R;
import com.hfy.demo01.module.jetpack.viewmodel.fragment.entity.UserContent;

public class DetailFragment extends Fragment {

    public static Fragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView detail = view.findViewById(R.id.tv_detail);

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), new Observer<UserContent.UserItem>() {
            @Override
            public void onChanged(UserContent.UserItem userItem) {
                //展示详情
                detail.setText(userItem.toString());
            }
        });
    }
}