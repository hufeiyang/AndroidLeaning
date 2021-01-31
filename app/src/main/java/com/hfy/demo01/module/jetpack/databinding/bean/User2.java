package com.hfy.demo01.module.jetpack.databinding.bean;

import com.blankj.utilcode.util.ObjectUtils;


/**
 * User2，使用LiveData包裹后，搭配DataBinding使用
 * 对比 User， 不需要 继承BaseObservable、也不需要 @Bindable、notifyPropertyChanged(BR.name)
 */
public class User2{

    private String name;
    private String level;
    private String avatar;


    public User2(String name, String level) {
        this.name = name;
        this.level = level;
        avatar = "https://avatars2.githubusercontent.com/u/20123477?s=400&u=3ea5988670fd59076aecd1f4030ba7ca51402982&v=4";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (ObjectUtils.equals(name, this.name)) {
            //解决双向绑定的死循环：数据没变就return'
            return;
        }
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
