package com.hfy.demo01.module.jetpack.databinding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfy.demo01.module.jetpack.databinding.bean.User;

/**
 * 结合DataBinding使用的ViewModel
 * @author hufeiyang
 * @data 2021/1/18
 * @Description:
 */
public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public void getUser(){
        User user = new User("My name is DataBinding with LiveData !","Lv1000");
        userLiveData.setValue(user);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
