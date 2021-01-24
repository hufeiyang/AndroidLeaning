package com.hfy.demo01.module.jetpack.databinding.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;

import com.blankj.utilcode.util.ObjectUtils;
import com.hfy.demo01.BR;


/**
 * 侠客 ViewModel
 */
public class User extends BaseObservable{

    private String name;
    private String level;
    private String avatar;

    /**
     * 直接使用 基础的 ObservableField，就不用 @Bindable + notifyPropertyChanged 了
     */
    public ObservableBoolean isFired = new ObservableBoolean();
//    public ObservableInt age;
//    public ObservableArrayMap<String, String> tag;
//    public ObservableArrayList<String> list;

    public User(String name, String level) {
        this.name = name;
        this.level = level;
        isFired.set(false);

        avatar = "https://avatars2.githubusercontent.com/u/20123477?s=400&u=3ea5988670fd59076aecd1f4030ba7ca51402982&v=4";
    }

    /**
     * @return
     * @Bindable BR中生成一个对应的字段，BR编译时生成，类似R文件
     */
    @Bindable
    public String getName() {
        return name;
    }

    /**
     * notifyPropertyChanged(BR.name),通知系统BR.name已发送变化，并更新UI
     *
     * @param name
     */
    public void setName(String name) {
        if (ObjectUtils.equals(name, this.name)) {
            //解决双向绑定的死循环：数据没变就return'
            return;
        }
        this.name = name;
        notifyPropertyChanged(BR.name);//已绑定的数据，发生变化时通知View
    }

    @Bindable
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        if (ObjectUtils.equals(level, this.level)) {
            //解决双向绑定的死循环：数据没变就return'
            return;
        }
        this.level = level;
        notifyPropertyChanged(BR.level);//已绑定的数据，发生变化时通知View
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        if (ObjectUtils.equals(avatar, this.avatar)) {
            //解决双向绑定的死循环：数据没变就return'
            return;
        }
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);//已绑定的数据，发生变化时通知View
    }
}
