package com.hfy.demo01.module.jetpack.databinding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hfy.demo01.module.jetpack.databinding.bean.User2;

/**
 * 结合DataBinding使用的ViewModel
 * @author hufeiyang
 * @data 2021/1/18
 * @Description:
 */
public class UserViewModel extends ViewModel {

    private final MutableLiveData<User2> userLiveData = new MutableLiveData<>();

    public void getUser(){
        User2 user = new User2("My name is DataBinding with LiveData !","Lv1000");
        userLiveData.setValue(user);
        //这里可以直接更新user 的Lv，UI会对应刷新
        user.setLevel("Lv1001");
    }

    public LiveData<User2> getUserLiveData() {
        return userLiveData;
    }
}
