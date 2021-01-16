package com.hfy.demo01.module.jetpack.viewmodel.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfy.demo01.module.jetpack.viewmodel.fragment.entity.UserContent;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<UserContent.UserItem> selected = new MutableLiveData<UserContent.UserItem>();

    public void select(UserContent.UserItem user) {
        selected.setValue(user);
    }

    public LiveData<UserContent.UserItem> getSelected() {
        return selected;
    }
}
