package com.hfy.demo01;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.github.sahasbhop.apngview.ApngImageLoader;
import com.hfy.demo01.module.home.glide.GlideApp;

import org.jay.launchstarter.MainTask;
import org.jay.launchstarter.Task;
import org.jay.launchstarter.TaskCallBack;
import org.jay.launchstarter.TaskDispatcher;
import org.jay.launchstarter.utils.DispatcherExecutor;

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
}
