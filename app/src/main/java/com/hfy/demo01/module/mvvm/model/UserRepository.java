package com.hfy.demo01.module.mvvm.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储区
 * @author hufeiyang
 * @data 2021/1/24
 * @Description:
 */
public class UserRepository {

    private static UserRepository mUserRepository;

    public static UserRepository getUserRepository(){
        if (mUserRepository == null) {
            mUserRepository = new UserRepository();
        }
        return mUserRepository;
    }

    /**
     * 从服务端获取
     * @param callback
     */
    public void getUsersFromServer(Callback<List<User>> callback){

        new AsyncTask<Void, Void, List<User>>() {

            @Override
            protected void onPostExecute(List<User> users) {
                callback.onSuccess(users);
                //存本地数据库
                saveUsersToLocal(users);
            }

            @Override
            protected List<User> doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //假装从服务端获取的
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    User user = new User("user"+i, i);
                    users.add(user);
                }
                return users;
            }
        }.execute();
    }

    /**
     * 从本地数据库获取
     */
    public void getUsersFromLocal(){
        // TODO: 2021/1/24 从本地数据库获取
    }

    /**
     * 存入本地数据库
     * @param users
     */
    private void saveUsersToLocal(List<User> users){
        // TODO: 2021/1/24 存入本地数据库
    }
}
