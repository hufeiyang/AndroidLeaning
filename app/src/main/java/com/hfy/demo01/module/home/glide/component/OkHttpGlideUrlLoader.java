package com.hfy.demo01.module.home.glide.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * OkHttpGlideUrlLoader
 * @author hufeiyang
 */
public class OkHttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private static final Set<String> SCHEMES =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList("http", "https")));

    private final OkHttpClient okHttpClient;

    public OkHttpGlideUrlLoader(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl glideUrl, int width, int height, @NonNull Options options) {
//        return okHttpClient.bui(new GlideUrl(model.toString()), width, height, options);
        return null;
    }

    @Override
    public boolean handles(@NonNull GlideUrl glideUrl) {
//        return SCHEMES.contains(glideUrl.toStringUrl().getScheme());
        return false;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        private OkHttpClient mOkHttpClient;

        public Factory() {
        }

        public Factory(OkHttpClient okHttpClient) {
            mOkHttpClient = okHttpClient;
        }

        private synchronized OkHttpClient getOkHttpClient() {
            if (mOkHttpClient == null) {
                mOkHttpClient = new OkHttpClient();
            }
            return mOkHttpClient;
        }


        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new OkHttpGlideUrlLoader(getOkHttpClient());
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

}
