package com.hfy.demo01.module.home.glide.component;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * OkHttpGlideUrlLoader
 * @author hufeiyang
 */
public class OkHttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {


    private final OkHttpClient okHttpClient;

    public OkHttpGlideUrlLoader(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
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

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new OkHttpGlideUrlLoader(getOkHttpClient());
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }


    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        return new OkHttpFetcher(okHttpClient,model);
    }
}
