package com.hfy.demo01.module.home.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.hfy.demo01.module.home.glide.component.ProgressInterceptor;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 自定义Glide模块
 */
@GlideModule
public class MyGlideModule extends AppGlideModule {

    public static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //更改配置

        //DiskCache改为外部存储 且改为500M（默认是用InternalCacheDiskCacheFactory-私有内部存储,250M）
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context,DISK_CACHE_SIZE));
        //图片格式改为ARGB_8888，会更细腻，但内存开销更多（默认是ARGB_565）
        builder.setDefaultRequestOptions(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

        //注册组件：替换成okHttp加载（或者使用implementation 'com.github.bumptech.glide:okhttp3-integration:1.5.0'）
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory(okHttpClient));
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());


    }


    /**
     * 禁止解析Manifest文件
     * 主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
