package com.hfy.demo01.module.home.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.hfy.demo01.module.home.glide.component.OkHttpGlideUrlLoader;
import com.hfy.demo01.module.home.glide.component.ProgressInterceptor;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 自定义Glide模块
 */
public class MyGlideModule implements GlideModule {

    public static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //更改配置
//        builder.setBitmapPool()
//        builder.setDecodeFormat()
//        builder.setDiskCache()
//        builder.setDiskCacheService()
//        builder.setMemoryCache()
//        builder.setResizeService()

        //DiskCache改为外部存储 且改为500M（默认是用InternalCacheDiskCacheFactory-私有内部存储,250M）
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,DISK_CACHE_SIZE));
        //图片格式改为ARGB_8888，会更细腻，但内存开销更多（默认是ARGB_565）
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //注册组件：替换成okHttp加载（或者使用implementation 'com.github.bumptech.glide:okhttp3-integration:1.5.0'）
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory(okHttpClient));

    }
}
