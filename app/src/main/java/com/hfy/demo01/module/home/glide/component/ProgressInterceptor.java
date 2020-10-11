package com.hfy.demo01.module.home.glide.component;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * okHttp拦截器
 * @author hufeiyang
 */
public class ProgressInterceptor implements Interceptor {

    static final Map<String, ProgressListener> LISTENER_MAP = new HashMap<>();
    private long mAlreadyDownLength;

    public ProgressInterceptor() {
    }

    public ProgressInterceptor(long alreadyDownLength) {
        mAlreadyDownLength = alreadyDownLength;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();
        ResponseBody body = response.body();
        Response newResponse = response.newBuilder().body(new ProgressResponseBody(url, body, mAlreadyDownLength)).build();
        return newResponse;
    }

    public static void addListener(String url, ProgressListener listener) {
        LISTENER_MAP.put(url, listener);
    }

    public static void removeListener(String url) {
        LISTENER_MAP.remove(url);
    }

    public interface ProgressListener {

        void onProgress(int progress);
    }


    /**
     * 能获取进度的ResponseBody
     */
    public static class ProgressResponseBody extends ResponseBody {

        private static final String TAG = "ProgressResponseBody";
        private final long mAlreadyDownLength;

        private BufferedSource bufferedSource;

        private ResponseBody responseBody;

        private ProgressListener listener;

        public ProgressResponseBody(String url, ResponseBody responseBody, long alreadyDownLength) {
            this.responseBody = responseBody;
            listener = ProgressInterceptor.LISTENER_MAP.get(url);
            mAlreadyDownLength = alreadyDownLength;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            //BufferedSource可以理解为一个带有缓冲区的响应体，因为从网络流读入响应体的时候，
            // Okio先把响应体读入一个缓冲区内，也即是BufferedSource。
            if (bufferedSource == null) {
                //Source相当于一个输入流InputStream，即响应的数据流。
                bufferedSource = Okio.buffer(new ProgressSource(responseBody.source(), mAlreadyDownLength));
            }
            return bufferedSource;
        }

        private class ProgressSource extends ForwardingSource {

            long totalBytesRead = 0;

            int currentProgress;

            ProgressSource(Source source, long alreadyDownLength) {
                super(source);
                totalBytesRead = alreadyDownLength;
            }

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                long fullLength = responseBody.contentLength();
                //不断统计当前下载好的数据
                if (bytesRead == -1) {
                    totalBytesRead = fullLength;
                } else {
                    totalBytesRead += bytesRead;
                }
                int progress = (int) (100f * totalBytesRead / fullLength);
                Log.d(TAG, "download progress is " + progress);
                if (listener != null && progress != currentProgress) {
                    listener.onProgress(progress);
                }
                if (listener != null && totalBytesRead == fullLength) {
                    listener = null;
                }
                currentProgress = progress;
                return bytesRead;
            }
        }

    }
}
