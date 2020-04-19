package com.hfy.demo01.module.home.glide.component;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class OkHttpFetcher implements DataFetcher<InputStream> {
    private InputStream stream;
    private volatile boolean isCancelled;
    private final GlideUrl glideUrl;


    private final OkHttpClient okHttpClient;
    private ResponseBody responseBody;

    public OkHttpFetcher(OkHttpClient okHttpClient, GlideUrl model) {
        this.okHttpClient = okHttpClient;
        glideUrl = model;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Request.Builder builder = new Request.Builder();
        builder.url(glideUrl.toStringUrl());

        for (Map.Entry<String, String> headerEntry : glideUrl.getHeaders().entrySet()) {
            builder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        builder.addHeader("httplib", "OkHttp");

        Request request = builder.build();
        if (isCancelled) {
            return null;
        }

        Response response = okHttpClient.newCall(request).execute();
        responseBody = response.body();
        if (!response.isSuccessful() || responseBody == null) {
            throw new IOException("Request failed with code: " + response.code());
        }
        stream = ContentLengthInputStream.obtain(responseBody.byteStream(),
                responseBody.contentLength());
        return stream;
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        if (responseBody != null) {
            responseBody.close();
        }
    }

    @Override
    public String getId() {
        return glideUrl.getCacheKey();
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}
