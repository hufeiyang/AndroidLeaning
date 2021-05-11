package com.hfy.demo01.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.FragmentActivity;

import com.hfy.demo01.MainActivity;
import com.hfy.demo01.R;
import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 布局优化
 * 绘制优化
 * 内存优化： 1、集合类泄漏 2、单例/静态变量造成的内存泄漏 3、匿名内部类/非静态内部类 4、资源未关闭造成的内存泄漏
 * 启动优化：
 * apk瘦身：
 *
 */

@Xml(layouts = "activity_rotate_view")
public class OptimizeActivity extends AppCompatActivity {

    @BindView(R.id.iv_dog)
    ImageView ivDog;

    private MyHandler mMyHander = new MyHandler(this);


    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, OptimizeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.beginSection("hufeiyang");
        }

        //布局加载耗时：setContentView()中的 LayoutInflater.from(mContext).inflate(resId, contentParent);
        //即， IO读xml文件 + 反射创建View实例

        //布局异步加载：在子线程进行 布局加载
        //缺点：它内部的onCreateView并没有使用Factory做AppCompat控件兼容的处理；不能自定义。
        //所以 一般不用。
//        new AsyncLayoutInflater(this).inflate(R.layout.activity_main, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
//            @Override
//            public void onInflateFinished(@NonNull View view, int i, @Nullable ViewGroup viewGroup) {
//                setContentView(view);
//                // findViewById、视图操作等
//            }
//        });

        // 使用LayoutInflaterCompat.Factory2全局监控Activity界面每一个控件的加载耗时，
        // 也可以做全局的自定义控件替换处理，比如：将TextView全局替换为自定义的TextView。

        //setFactory、setFactory2 是系统提供的hook，如果没有设置就是使用反射创建所有View
        //这里 setFactory2 一定要在super.onCreate前，
        // 因为CompatAppActivity中使用了delegate.installViewFactory() 有尝试setFactory2，如果在它之后就会异常
//        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new LayoutInflater.Factory2() {
//            @Override
//            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//
//                long time = System.currentTimeMillis();
//
//                // 这句是一定要调用的：因为你覆盖了系统 在CompatAppActivity设置的Factory2，而下面这句就是它本来要做的事（1、TextView等替换成兼容版本；2、其他的使用反射（带. 和 不带.））
//                View view = getDelegate().createView(parent, name, context, attrs);
//
//                if (view != null && view instanceof TextView) {
//                    // 可以直接创建生成自定义TextView
//                    //也可以统一修改 TextView的字体等
//                }
//
//                //这里就是 每个View创建所需时间
//                Log.i("OptimizeActivity",name + " cost " + (System.currentTimeMillis() - time));
//                return view;
//            }
//
//            @Override
//            public View onCreateView(String name, Context context, AttributeSet attrs) {
//                return null;
//            }
//        });


        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_rotate_view);
        X2C.setContentView(this, R.layout.activity_rotate_view);


        ButterKnife.bind(this);


        new MyThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //这里delay了10秒，很可能activity已经在不使用了，却发现handleMessage持有activity的view，可能泄漏，所以需要转为持有 弱引用即可~
        mMyHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivDog.getWidth();
            }
        },10000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

    }

    //静态内部类
    private static class MyHandler extends Handler{

        private final WeakReference<OptimizeActivity> mActivity;

        MyHandler(OptimizeActivity activity) {
            //持有 弱引用
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mActivity.get() != null) {
                mActivity.get().ivDog.setVisibility(View.VISIBLE);
            }
        }
    }


    //非静态内部类会自持有外部类引用，当生命周期长于外部类时，就会泄漏了，所以转为静态，这样不会持有外部了。
    private static class MyThread extends Thread{
        MyThread(Runnable target) {
            super(target);
        }
    }


}
