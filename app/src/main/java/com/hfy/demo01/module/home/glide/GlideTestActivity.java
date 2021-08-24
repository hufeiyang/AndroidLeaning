package com.hfy.demo01.module.home.glide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.sahasbhop.apngview.ApngDrawable;
import com.github.sahasbhop.apngview.ApngImageLoader;
import com.github.sahasbhop.apngview.assist.AssistUtil;
import com.github.sahasbhop.flog.FLog;
import com.hfy.demo01.R;
import com.hfy.demo01.module.home.glide.component.ProgressInterceptor;
import com.hfy.demo01.module.home.glide.transform.CircleCropTransformation;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.github.sahasbhop.apngview.ApngImageLoader.enableDebugLog;

/**
 * Glide test
 *
 * @author hufeiyang
 */
public class GlideTestActivity extends AppCompatActivity {

    @BindView(R.id.iv_glide_test)
    ImageView ivGlideTest;

    @BindView(R.id.iv_glide_test_gif)
    ImageView ivGlideTestGif;

    @BindView(R.id.iv_glide_test_apng)
    ImageView ivGlideTestApng;

    @BindView(R.id.tv_glide_test)
    TextView tvGlideTest;

    private ProgressDialog progressDialog;

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, GlideTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_test);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        //使用Glide，我们就完全不用担心图片内存浪费，甚至是内存溢出的问题。
        // 因为Glide从来都不会直接将图片的完整尺寸全部加载到内存中，而是用多少加载多少。
        // Glide会自动判断ImageView的大小，然后只将这么大的图片像素加载到内存当中，帮助我们节省内存开支。
        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
//                .asGif()//只允许加载静态图片,若原本url就是静态图，则会加载失败
                .placeholder(R.drawable.brvah_sample_footer_loading_progress)
//                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
//                .override(200, 200)//指定要加载到控件的图片大小（由下载的原图压缩所得）
                .into(ivGlideTest);

        Glide.with(this)
                .load("https://n.sinaimg.cn/tech/transform/550/w330h220/20200103/0f09-imrkkfx2327344.gif")
//                .asBitmap()//只允许加载静态图片，gif就展示第一帧
                .placeholder(R.drawable.brvah_sample_footer_loading_progress)
//                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivGlideTestGif);

        //自定义GlideUrl，可用于修改cache key
        GlideApp.with(this)
                .load(new GlideUrl("https://n.sinaimg.cn/tech/transform/550/w330h220/20200103/0f09-imrkkfx2327344.gif"))
                .into(ivGlideTestGif);


        //会阻塞，直到获取到结果或超时，用于子线程同步请求。with的context也不能传activity了
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<Drawable> submit = GlideApp.with(getApplicationContext())
                        .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                        .submit(100, 100);
                try {
                    submit.get(2000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        //自定义Target
        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
//                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ivGlideTest.setImageDrawable(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        ivGlideTest.setImageDrawable(errorDrawable);
                    }
                });

        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
//                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .into(new CustomViewTarget<TextView, Drawable>(tvGlideTest) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });

        //预加载，先缓存下来，后面再用来展示就很快
        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .preload();

        //downloadOnly
        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        //和into(100, 100)类似，会阻塞，用于子线程，with的context也不能传activity了。
        FutureTarget<Drawable> submit = GlideApp.with(getApplicationContext())
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .submit(100, 100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Drawable drawable = submit.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        //listener
        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Toast.makeText(GlideTestActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .preload();

        //图片转换
        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .dontTransform()
//                .transform(...)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .fitCenter()
                .centerCrop()
                .into(ivGlideTest);

        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .dontTransform()
                .transform(new CircleCropTransformation())
                .into(ivGlideTest);



        //多种转换器
        RoundedCornersTransformation roundedCornersTransformation1 = new RoundedCornersTransformation(AdaptScreenUtils.pt2Px(5), 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
        RoundedCornersTransformation roundedCornersTransformation2 = new RoundedCornersTransformation(AdaptScreenUtils.pt2Px(20), 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT);
        BlurTransformation blurTransformation = new BlurTransformation(20);
        GlideApp.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .transform(roundedCornersTransformation1, roundedCornersTransformation2, blurTransformation)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivGlideTest);


        //下载进度(使用okHttp的拦截器)
//        String url = "http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg";
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMessage("加载中");
//
//        ProgressInterceptor.addListener(url, new ProgressInterceptor.ProgressListener() {
//            @Override
//            public void onProgress(int progress) {
//                progressDialog.setProgress(progress);
//            }
//        });
//
//        GlideApp.with(this)
//                .load(url)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(new DrawableImageViewTarget(ivGlideTest){
//                    @Override
//                    public void onLoadStarted(Drawable placeholder) {
//                        super.onLoadStarted(placeholder);
//                        progressDialog.show();
//                    }
//
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        super.onResourceReady(resource, transition);
//                        progressDialog.dismiss();
//                    }
//                });

//        GlideApp.with(this)
//                .load(R.drawable.apng)
//                .into(ivGlideTestApng);
//
//        String uri = "assets://apng.png";
//        String uri = "http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg";
        String uri = "https://d17qpog9ccnbkb.cloudfront.net/v5res/vova/2018-12-20/images/banners/0yuangou/0yuangou.png";
//        ApngImageLoader.getInstance()
//                .displayApng(uri, ivGlideTestApng,
//                        new ApngImageLoader.ApngConfig(5, true));

//        ivGlideTestApng.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ApngDrawable apngDrawable = ApngDrawable.getFromView(v);
//                if (apngDrawable == null) return;
//                if (apngDrawable.isRunning()) {
//                    apngDrawable.stop();  // 停止播放动画
//                } else {
//                    apngDrawable.setNumPlays(3); // 动画循环次数
//                    apngDrawable.start(); // 开始播放动画
//                }
//            }
//        });

//        ApngImageLoader.getInstance().displayImage(uri, ivGlideTestApng);


        GlideApp.with(this)
                .load(uri)
                .into(new DrawableImageViewTarget(ivGlideTestApng){
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
//                        progressDialog.show();
                        if (view == null) return;
                        view.setTag(R.id.tag_image, uri.toString());

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
//                        progressDialog.dismiss();


                        if (view == null) return;

                        Object tag = view.getTag(R.id.tag_image);
                        if (enableDebugLog) FLog.d("tag: %s", tag);

                        if (tag != null && tag instanceof String) {
                            String actualUri = tag.toString();

                            File pngFile = AssistUtil.getCopiedFile(GlideTestActivity.this, actualUri);

                            if (pngFile == null) {
                                if (enableDebugLog) FLog.w("Can't locate the file!!! %s", actualUri);

                            } else if (pngFile.exists()) {
                                boolean isApng = AssistUtil.isApng(pngFile);

                                if (isApng) {
                                    if (enableDebugLog) FLog.d("Setup apng drawable");
                                    ApngDrawable drawable = new ApngDrawable(GlideTestActivity.this, drawableToBitmap(resource), Uri.fromFile(pngFile));
                                    ((ImageView) view).setImageDrawable(drawable);
                                } else {
                                    ((ImageView) view).setImageBitmap(drawableToBitmap(resource));
                                }

                            } else {
                                if (enableDebugLog) FLog.d("Clear cache and reload");
//                                MemoryCacheUtils.removeFromCache(actualUri, ApngImageLoader.getInstance().getMemoryCache());
//                                DiskCacheUtils.removeFromCache(actualUri, ApngImageLoader.getInstance().getDiskCache());
//
//                                ApngImageLoader.getInstance().displayImage(actualUri, (ImageView) view, this);
                            }
                        }

                        ApngDrawable apngDrawable = ApngDrawable.getFromView(view);
                        if (apngDrawable == null) return;
                        if (apngDrawable.getNumPlays() > 0) apngDrawable.setNumPlays(apngDrawable.getNumPlays());
                        apngDrawable.start();
                    }
                });

    }


    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
