package com.hfy.demo01.module.mvvm.model;

/**
 * @author hufeiyang
 * @data 2021/1/24
 * @Description:
 */
public interface Callback<T> {

    public void onSuccess(T t);

    public void onFailed(String msg);
}
