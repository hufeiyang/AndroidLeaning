package com.hfy.demo01.plugin;

/**
 * @author bytedance
 * @date 2023/7/24
 * @desc
 */
public class DexImpl implements IDex {
    @Override
    public String getMessage() {
        return new StringBuilder(getClass().getName()).append(" is loaded by DexClassLoader").toString();
    }
}
