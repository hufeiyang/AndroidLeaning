package com.hfy.demo01.module.home.okhttp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.hfy.demo01.R;
import com.hfy.demo01.module.home.glide.component.ProgressInterceptor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author hufeiyang
 */
public class OkHttpTestActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.progress_loading)
    ProgressBar progressLoading;

    private Call mDownloadCall;

    private OkHttpClient mDownloadHttpClient;

    private String mDownloadUrl;
    private String mPath;

    private long mAlreadyDownLength;
    private long mTotalLength;

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, OkHttpTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);
        ButterKnife.bind(this);


//        okHttpGet();

//        okHttpPost();


        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://www.baidu.com/";
        Request getRequest = new Request.Builder()
                .url(url)
                .get()
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("username", "hfy")
                .add("password", "qaz")
                .build();
        Request postRequest = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        Call call = httpClient.newCall(getRequest);
//        Call call = httpClient.newCall(postRequest);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });

    }


    /**
     * 解决 https 连接代理时 不能正常请求的的问题
     *
     * @param builder
     */
    private static void setIgnoreAll(OkHttpClient.Builder builder) {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[]{};
                return x509Certificates;
                // return null;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

            HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            builder.sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(DO_NOT_VERIFY);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


    private void okHttpPost() {


//        OkHttpClient httpClient = new OkHttpClient();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(getExternalCacheDir(), 500 * 1024 * 1024))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String url = request.url().toString();
                        Log.i(TAG, "intercept: proceed start: url" + url + ", at " + System.currentTimeMillis());
                        Response response = chain.proceed(request);
                        ResponseBody body = response.body();
                        Log.i(TAG, "intercept: proceed end: url" + url + ", at " + System.currentTimeMillis());
                        return response;
                    }
                })
                .build();


//        MediaType contentType = MediaType.parse("text/x-markdown; charset=utf-8");
//        String content = "hello!";
//        RequestBody body = RequestBody.create(contentType, content);

        //RequestBody:fileBody,上传文件
        File file = drawableToFile(this, R.mipmap.bigpic, new File("00.jpg"));
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);


        //RequestBody:multipartBody, 多类型 （用户名、密码、头像）
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "hufeiyang")
                .addFormDataPart("phone", "123456")
                .addFormDataPart("touxiang", "00.png", fileBody)
                .build();


        Request getRequest = new Request.Builder()
                .url("http://yun918.cn/study/public/file_upload.php")
                .post(multipartBody)
                .addHeader("key", "value")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .tag("requestTag")
                .build();

        Call call = client.newCall(getRequest);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.i(TAG, "okHttpPost enqueue: \n onFailure:" + call.request().toString() + "\n body:" + call.request().body().contentType()
                        + "\n IOException:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "okHttpPost enqueue: \n onResponse:" + response.toString() + "\n body:" + response.body().string());

                ResponseBody body = response.body();
                int code = response.code();
                String string = body.string();
                byte[] bytes = body.bytes();
                InputStream inputStream = body.byteStream();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //同步请求，要放到子线程执行
                    Response response = call.execute();
                    Log.i(TAG, "okHttpPost run: response:" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        Executors.newFixedThreadPool()

    }

    /**
     * drawable转为file。
     *
     * @param mContext
     * @param drawableId drawable的ID
     * @param fileName   转换后的文件名
     * @return
     */
    public File drawableToFile(Context mContext, int drawableId, File fileName) {
//        InputStream is = view.getContext().getResources().openRawResource(R.drawable.logo);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
//        Bitmap bitmap = BitmapFactory.decodeStream(is);

        String defaultPath = mContext.getFilesDir()
                .getAbsolutePath() + "/myimage";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            return null;
        }
        String defaultImgPath = defaultPath + "/" + fileName;
        file = new File(defaultImgPath);
        try {
            file.createNewFile();

            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fOut);
//            is.close();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void okHttpGet() {

        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://www.baidu.com/";

        Request getRequest = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = httpClient.newCall(getRequest);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //同步请求，要放到子线程执行
                    Response response = call.execute();
                    Log.i(TAG, "okHttpGet run: response:" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i(TAG, "okHttpGet enqueue: onResponse:"+ response.body().string());
//
//                ResponseBody body = response.body();
//                int code = response.code();
//                String string = body.string();
//                byte[] bytes = body.bytes();
//                InputStream inputStream = body.byteStream();
//
//            }
//        });

    }

    private void testOkhttp() {
        //1、构建OkHttpClient（OkHttpClient强烈建议全局单例使用，因为每一个OkHttpClient都有自己单独的连接池和线程池，复用连接池和线程池能够减少延迟、节省内存。）
        //内部有默认的Builder
        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(getExternalCacheDir(), 500 * 1024 * 1024))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String url = request.url().toString();
                        Log.i(TAG, "intercept: proceed start: url" + url + ", at " + System.currentTimeMillis());
                        Response response = chain.proceed(request);
                        ResponseBody body = response.body();
                        Log.i(TAG, "intercept: proceed end: url" + url + ", at " + System.currentTimeMillis());
                        return response;
                    }
                })
                .build();

        //2、构建Request

        //2.1 get
        String url = "https://avatar.csdnimg.cn/6/5/5/1_hfy8971613_1581217465.jpg";
        String requestTag = "requestTag";
        Request getRequest = new Request.Builder()
                .url(url)
                .get()
                .addHeader("key", "value")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .tag(requestTag)
                .build();

        //2.2 post，还要构建RequestBody，有多种

        //2.2.1 RequestBody:FormBody，键值对
        RequestBody formBody = new FormBody.Builder()
                .add("username", "hfy")
                .add("password", "qaz")
//                .addEncoded()
                .build();

        //2.2.2 RequestBody:jsonBody，字符串
        String json = "jsonString";
        RequestBody jsonBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        //2.2.3  RequestBody:fileBody,上传文件
        File file = new File(Environment.getExternalStorageDirectory(), "1.png");
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);

        //2.2.4 RequestBody:multipartBody, 多类型 （用户名、密码、头像）
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "hufeiyang")
                .addFormDataPart("phone", "123")
                .addFormDataPart("touxiang", "1.png", fileBody)
                .build();


        Request postRequest = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        //3、请求（同步、异步）
        Call call = client.newCall(getRequest);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //同步请求，会阻塞
//                    Response response = call.execute();
//                    ResponseBody body = response.body();
//                    int code = response.code();
//                    String string = body.string();
//                    Log.i(TAG, "run: call.execute code = "+code);
//                    Log.i(TAG, "run: call.execute body = "+string);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();

        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "run: call.enqueue onFailure = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //执行在子线程
                ResponseBody body = response.body();
                int code = response.code();
                String string = body.string();
                Log.i(TAG, "run: call.enqueue code = " + code);
                Log.i(TAG, "run: call.enqueue body = " + string);

                InputStream is = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIvAvatar.setImageBitmap(bitmap);
                    }
                });

            }
        });

//        call.cancel();
    }


    private void stopDownload() {
        if (mDownloadCall == null) {
            return;
        }
        if (!mDownloadCall.isCanceled()) {
            mDownloadCall.cancel();
        }
    }

    /**
     * 下载进度测试
     */
    private void initDownloadOkhttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        setIgnoreAll(builder);
        mDownloadHttpClient = builder
                .addInterceptor(new ProgressInterceptor(mAlreadyDownLength))
                .cache(new Cache(getExternalCacheDir(), 100 * 1024 * 1024))
                .build();
    }

    private void startDownload() {

        initDownloadOkhttpClient();

        if (mPath == null) {
            mPath = getFile().getAbsolutePath();
        }

        mAlreadyDownLength = getFileStart();

        if (mDownloadCall == null || mDownloadCall.isExecuted()) {
//            mDownloadUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593692477062&di=6eeff0ce3c17adba61dd7101f19f7ef4&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F6%2F5243c9380ddff.jpg";
            mDownloadUrl = "https://dldir1.qq.com/qqfile/QQIntl/QQi_PC/QQIntl2.11.exe";
//            mDownloadUrl = "http://s.toutiao.com/UsMYE/";
            Request getRequest = new Request.Builder()
                    .url(mDownloadUrl)
                    .header("RANGE", "bytes=" + mAlreadyDownLength + "-")
                    .get()
                    .build();
            mDownloadCall = mDownloadHttpClient.newCall(getRequest);

            ProgressInterceptor.addListener(mDownloadUrl, new ProgressInterceptor.ProgressListener() {
                @Override
                public void onProgress(int progress) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvProgress.setText(progress + "%");

                            progressLoading.setProgress(progress);
                        }
                    });
                }
            });
        }

        mDownloadCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i(TAG, "okHttpGet enqueue: onResponse:" + response.body().string());
//
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    return;
                }
                InputStream inputStream = responseBody.byteStream();//得到输入流
                RandomAccessFile randomAccessFile = new RandomAccessFile(mPath, "rw");//得到任意保存文件处理类实例
                if (mTotalLength == 0){
                    mTotalLength = responseBody.contentLength();//得到文件的总字节大小
                    randomAccessFile.setLength(mTotalLength);//预设创建一个总字节的占位文件
                }
                if (mAlreadyDownLength != 0){
                    randomAccessFile.seek(mAlreadyDownLength);
                }
                byte[] bytes = new byte[2048];
                int len = 0;
                try {
                    while ((len = inputStream.read(bytes)) != -1) {
                        randomAccessFile.write(bytes,0,len);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Get下载异常");
                } finally {
                    randomAccessFile.close();
                    inputStream.close();
                }

                ProgressInterceptor.removeListener(mDownloadUrl);
            }
        });
    }

    private File getFile() {
        String root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(root,"douyin.apk");
        return file;
    }

    private long getFileStart(){
        String root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(root,"douyin.apk");
        return file.length()>0 ? file.length()-1:file.length();
    }

    @OnClick({R.id.btn_start, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startDownload();
                break;
            case R.id.btn_stop:
                stopDownload();
                break;
            default:
                break;
        }
    }

}
