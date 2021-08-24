package com.hfy.demo01;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.github.sahasbhop.apngview.ApngImageLoader;
import com.hfy.demo01.hook.HookUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jay.launchstarter.ITask;
import org.jay.launchstarter.MainTask;
import org.jay.launchstarter.Task;
import org.jay.launchstarter.TaskDispatcher;
import org.jay.launchstarter.utils.DispatcherExecutor;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author hufeiyang
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationLifecycleObserver());

        TaskDispatcher.init(getBaseContext());
        TaskDispatcher taskDispatcher = TaskDispatcher.createInstance();

        // task2依赖task1；
        // task3未完成时taskDispatcher.await()处需要等待；
        // test4在主线程执行
        Task1 task1 = new Task1();
        Task2 task2 = new Task2();
        Task3 task3 = new Task3();
        Task4 task4Main = new Task4();

        taskDispatcher.addTask(task1);
        taskDispatcher.addTask(task2);
        taskDispatcher.addTask(task3);
        taskDispatcher.addTask(task4Main);

        Log.i(TAG, "onCreate: taskDispatcher.start()");
        taskDispatcher.start();

        taskDispatcher.await();
        Log.i(TAG, "onCreate: end.");

        ApngImageLoader.getInstance().init(getApplicationContext());

        HookUtil.hookInstrumentation();

//        //微信sdk初始化
//        regToWx();
//
//        //开始微信登录
//        // send oauth request
//         SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo_test";
//        api.sendReq(req);
    }

    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wx88888888";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }



    private static class Task1 extends Task {
        @Override
        public void run() {
            Log.i(TAG, Thread.currentThread().getName()+" run start: task1");
            doTask();
            Log.i(TAG, Thread.currentThread().getName()+" run end: task1");
        }


        @Override
        public ExecutorService runOn() {
            return DispatcherExecutor.getCPUExecutor();
        }
    }

    private static class Task2 extends Task {
        @Override
        public void run() {
            Log.i(TAG, Thread.currentThread().getName()+" run start: task2");
            doTask();
            Log.i(TAG, Thread.currentThread().getName()+" run end: task2");
        }

        @Override
        public List<Class<? extends Task>> dependsOn() {
            ArrayList<Class<? extends Task>> classes = new ArrayList<>();
            classes.add(Task1.class);
            return classes;
        }
    }

    private static class Task3 extends Task {
        @Override
        public void run() {
            Log.i(TAG, Thread.currentThread().getName()+" run start: task3");
            doTask();
            Log.i(TAG, Thread.currentThread().getName()+" run end: task3");
        }

        @Override
        public boolean needWait() {
            return true;
        }
    }

    private static class Task4 extends MainTask {
        @Override
        public void run() {
            Log.i(TAG, Thread.currentThread().getName()+" run start: task4");
            doTask();
            Log.i(TAG, Thread.currentThread().getName()+" run end: task4");
        }
    }

    private static void doTask() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Application生命周期观察，提供整个应用进程的生命周期
     *
     * Lifecycle.Event.ON_CREATE只会分发一次，Lifecycle.Event.ON_DESTROY不会被分发。
     *
     * 第一个Activity进入时，ProcessLifecycleOwner将分派Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME。
     * 而Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP，将在最后一个Activit退出后后延迟分发。如果由于配置更改而销毁并重新创建活动，则此延迟足以保证ProcessLifecycleOwner不会发送任何事件。
     *
     * 作用：你希望在应用程序进入前台或后台时做出反应，并且在接收生命周期事件时不需要毫秒的精确度。
     */
    public static class ApplicationLifecycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onAppForeground() {
            Log.w(TAG, "ApplicationObserver: app moved to foreground");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onAppBackground() {
            Log.w(TAG, "ApplicationObserver: app moved to background");
        }
    }

}
