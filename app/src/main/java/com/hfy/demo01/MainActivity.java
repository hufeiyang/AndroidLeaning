package com.hfy.demo01;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.TraceCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.TimeUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hfy.demo01.common.Constant;
import com.hfy.demo01.common.customview.MyToast;
import com.hfy.demo01.dagger2.bean.Car;
import com.hfy.demo01.dagger2.bean.Watch;
import com.hfy.demo01.module.home.adapter.HomePagerAdapter;
import com.hfy.demo01.module.home.fragment.FirstFragment;
import com.hfy.demo01.module.home.fragment.SecondFragment;
import com.hfy.demo01.module.home.fragment.ThirdFragment;
import com.hfy.demo01.performance.viewopt.ViewOpt;
import com.hfy.test_annotations.TestAnnotation;
import com.tencent.tauth.Tencent;

import org.jay.launchstarter.DelayInitDispatcher;
import org.jay.launchstarter.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

//import com.hfy.demo01.kotlin.LearnKotlin;

/*
import com.hfy.demo01.dagger2.component.DaggerMainActivityComponent;
import com.hfy.demo01.dagger2.component.DaggerMainActivityComponent;
*/

/**
 * @author hufy
 * test rebase
 * @date
 */
@TestAnnotation
public class MainActivity extends AppCompatActivity {

    private static final String TAG = Constant.INSTANCE.getHomeActivityLogTag();
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1000;

    @BindView(R.id.tl_home_page)
    TabLayout mTlHomeTab;

    @BindView(R.id.vp_home_page)
    ViewPager mVpHomePage;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

//    @BindView(R.id.nv_navigation)
//    NavigationView mNavigationView;

    /****** Dagger2 练习*****/
    @Inject
    Watch mWatch;

    @Inject
    Gson mGson;

    @Inject
    Gson mGson1;

    /**
     * 懒加载
     */
    @Inject
    Lazy<Car> mCarLazy;

    He he = new He();

    private String[] titles = {"艺术探索", "进阶实战","高级知识"};

    /**
     * 首页fragments
     */
    private List<Fragment> fragments = new ArrayList<Fragment>(titles.length);

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private Handler mHandler;
    private AsyncTask<Integer, Integer, String> task;


    DelayInitDispatcher delayInitDispatcher = new DelayInitDispatcher();
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate begin. ");

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (MainActivity.this){
//                    try {
//                        Log.i(TAG, "onCreate begin. sleep 20*1000");
//                        Thread.sleep(50*1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        TraceCompat.beginSection("MainActivity onCreate");

        Debug.startMethodTracing();//dmtrace.trace
//        Debug.startMethodTracing(getExternalFilesDir(null)+"test.trace");

        Log.i(TAG, "onCreate: setContentView begin：");
        long nowMills = TimeUtils.getNowMills();
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: setContentView end：,cost:" + (TimeUtils.getNowMills() - nowMills));

        delayInitDispatcher.addTask(new Task() {
            @Override
            public void run() {
                Log.i(TAG, "run: delay task begin");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "run: delay task end");
            }
        });
        delayInitDispatcher.start();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String age = intent.getStringExtra("age");
        Log.i(TAG, "onCreate: name = " + name);
        Log.i(TAG, "onCreate: age = " + age);

        initConfig();
        initView();
        initData();

        //jenkins 在push到github后 自动构建，test

        testToast();
        testThreadLocal();
//        testHandler();
//        testAsyncTask();
//        testHandlerThread();
//        testIntentService();
//        testThreadPoolExecutor();


        int i = testFinally();
        Log.i(TAG, "onCreate: 33333："+i);

        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {

                Choreographer.getInstance().postFrameCallback(this);
            }
        });

        testString();


        Debug.stopMethodTracing();

        TraceCompat.endSection();




        mTlHomeTab.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                return true;
            }
        });

        mTlHomeTab.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
//                Log.i(TAG, "onDraw: ");
            }
        });

        mTlHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
            }
        });

        //kotlin test
//        LearnKotlin learnKotlin = new LearnKotlin();
//        learnKotlin.work("深圳","996");


//        mTencent = Tencent.createInstance("APP_ID", this.getApplicationContext(), "Authorities");
//
//        if (!mTencent.isSessionValid())
//        {
//            mTencent.login(this, Scope, listener);
//        }

        synchronized (MainActivity.this){
            Log.i(TAG, "onCreate synchronized: ");
        }

        Log.i(TAG, "onCreate end. ");
    }

    /**
     * 布局优化-去反射
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = ViewOpt.createView(name, context, attrs);
        if (view != null) {
            return view;
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void testString() {
        String oriString = "abcdef";
        StringBuffer desString = new StringBuffer();
        char[] chars = oriString.toCharArray();
        for (int i1 = chars.length - 1; i1 >= 0; i1--) {
            desString.append(chars[i1]);
        }
        Log.i(TAG, "字符串逆序：onCreate: "+desString.toString());

        String oriString2 = "abcdef";
        StringBuffer desString2 = new StringBuffer(oriString2);
        Log.i(TAG, "字符串逆序2：onCreate: "+desString2.reverse().toString());

        String oriString3 = "abcdef";
        String deString3="";
        for (int length = oriString3.length()-1; length >= 0; length--) {
            deString3+=oriString3.charAt(length);
        }
        Log.i(TAG, "字符串逆序3：onCreate: "+desString.toString());

        String string = "abc";
        List<String> list = getFullList(string);
        Log.i(TAG, "字符串全排列：onCreate: "+list.toString());
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume begin. ");
        super.onResume();

        Log.i(TAG, "onResume end. ");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            reportFullyDrawn();
                        }
                    }
                });

            }
        }).start();
    }

    private List<String> getFullList(String string) {
        int lenth = string.length();
        ArrayList<String> result = new ArrayList<>();
        result.add(string.charAt(0)+"");

        for(int i=1; i<lenth;i++){
            ArrayList<String> temp = new ArrayList<>();
            char charAt = string.charAt(i);
            for(String str: result){
                temp.add(charAt+str);
                temp.add(str+charAt);
                for(int j=1; j<str.length();j++){
                    temp.add(str.substring(0,j) + charAt + str.substring(0,j));
                }
                temp.add(str+charAt);
            }
            result = temp;
        }

        return result;
    }

    private int testFinally() {
        try {
            Log.i(TAG, "onCreate: 11111");
            return 99;
        } finally {
            //虽然上面return了，但这行日志也会打印
            Log.i(TAG, "onCreate: 22222");
        }
    }

    private void testThreadPoolExecutor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "testThreadPoolExecutor: run begin");
                    Thread.sleep(4000);
                    Log.i(TAG, "testThreadPoolExecutor: run end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        fixedThreadPool.execute(runnable);

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(runnable);

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);
        scheduledThreadPool.execute(runnable);
        //延迟2秒执行
        scheduledThreadPool.schedule(runnable, 2, TimeUnit.SECONDS);
        //延迟2秒执行，然后以每次 任务开始的时间计时， 1秒后，如果任务是结束的 就立刻执行下一次；如果没有结束，就等它结束后立即执行下一次。
        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        //延迟3秒执行，然后以每次任务执行完后的时间计时， 2秒后，执行下一次~
        scheduledThreadPool.scheduleWithFixedDelay(runnable,1,2,TimeUnit.SECONDS);

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(runnable);
    }

    private void testIntentService() {

        Log.i(TAG, "testIntentService: task1");
        Intent intent= new Intent(this, MyIntentService.class);
        intent.putExtra("task_name","task1");
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "testIntentService: task2");
                intent.putExtra("task_name","task2");
                startService(intent);
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "testIntentService: task3");
                intent.putExtra("task_name","task3");
                startService(intent);
            }
        }, 6000);
    }

    public static class MyIntentService extends IntentService {

        public MyIntentService() {
            super("MyIntentServiceThread");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.i(TAG, "MyIntentService onHandleIntent: begin."+intent.getStringExtra("task_name"));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "MyIntentService onHandleIntent: done."+intent.getStringExtra("task_name"));
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "MyIntentService onDestroy: ");
            super.onDestroy();
        }
    }

    private void testHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("HandlerThreadName");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "handleMessage: thread name="+Thread.currentThread().getName()+"，what="+msg.what);
                        break;
                    case 1001:
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "handleMessage: thread name="+Thread.currentThread().getName()+"，what="+msg.what);
                        break;
                    default:
                        break;
                }
            }
        };

        Log.i(TAG, "sendMessage thread name="+Thread.currentThread().getName());
        handler.sendMessage(Message.obtain(handler, 1000));
        handler.sendMessage(Message.obtain(handler, 1001));
    }

    private void testAsyncTask() {
        //要在主线程实例化 并execute。
        //覆写的这几个方法不可以直接调用

        task = new AsyncTask<Integer, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //主线程执行，在异步任务之前
                Log.i(TAG, "testAsyncTask onPreExecute: ");
            }

            @Override
            protected String doInBackground(Integer... integers) {
                Log.i(TAG, "testAsyncTask doInBackground: ");
                //任务在 线程池中执行睡觉
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //发出进度
                publishProgress(50);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //再发出进度
                publishProgress(100);

                return "我是结果。参数是" + integers[0];
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //在主线程执行，在异步任务执行完之后
                Log.i(TAG, "testAsyncTask onPostExecute: " + s);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                //执行在主线程，调用publishProgress()后就会执行
                Log.i(TAG, "testAsyncTask onProgressUpdate: 进度：" + values[0] + "%");
//                Toast.makeText(MainActivity.this, "进度：" + values[0] + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onCancelled() {
                //取消,调用task.call(true)会回调
                super.onCancelled();
            }
        };


        //要在主线程执行execute，且只能执行一次
        task.execute(100);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.i(TAG, "task1 SERIAL_EXECUTOR doInBackground: ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.i(TAG, "task2 SERIAL_EXECUTOR doInBackground: ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.i(TAG, "task1 THREAD_POOL_EXECUTOR doInBackground: ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.i(TAG, "task2 THREAD_POOL_EXECUTOR doInBackground: ");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    /**
     * Handler 使用方法 test
     */
    private void testHandler() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //1、准备looper，即threadLocal<Looper>.set(new Looper())
                Looper.prepare();

                //2、创建handler实例
                // 这个重写了handleMessage，handler是属于Handler的子类的实例
//                Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                    }
//                };

                //这种就是Handler的实例
                mHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        Log.i(TAG, "child thread 1, handleMessage: what=" + msg.what);
                        return false;
                    }
                });

                //3、looper启动,sThreadLocal.get()拿到looper，拿到queue，开始queue.next
                Looper.loop();

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //4.1、Handler.post发送消息，queue.enqueueMessage(msg),即消息入队列。
                Log.i(TAG, "child thread2, mHandler.post");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "child thread2, mHandler.post, run()");

                    }
                });
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //4.2、handler.sendMessage发送消息，queue.enqueueMessage(msg),即消息入队列。
        Log.i(TAG, "main thread, sendMessage");
        Message message = Message.obtain();
        message.what = 100;
        mHandler.sendMessage(message);

    }

    /**
     * threadLocal原理验证
     */
    private void testThreadLocal() {

        ThreadLocal<Boolean> booleanThreadLocal = new ThreadLocal<>();
        ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();
        booleanThreadLocal.set(true);
        integerThreadLocal.set(0);
        Log.i(TAG, "testThreadLocal: main thread, boolean= " + booleanThreadLocal.get());
        Log.i(TAG, "testThreadLocal: main thread, int = " + integerThreadLocal.get());

        new Thread(new Runnable() {
            @Override
            public void run() {
                booleanThreadLocal.set(false);
                integerThreadLocal.set(1);
                Log.i(TAG, "testThreadLocal: a thread, boolean=" + booleanThreadLocal.get());
                Log.i(TAG, "testThreadLocal: a thread, int = " + integerThreadLocal.get());

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                booleanThreadLocal.set(null);
                integerThreadLocal.set(2);
                Log.i(TAG, "testThreadLocal: b thread, boolean=" + booleanThreadLocal.get());
                Log.i(TAG, "testThreadLocal: b thread, int = " + integerThreadLocal.get());

            }
        }).start();
    }

    private void testToast() {
//        Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    private void initData() {
        testDagger2();
    }

    private void testDagger2() {
        //调用此句，mWatch、mGson
//        DaggerMainActivityComponent.create().inject(this);
//        boolean equals = mWatch.equals("");
//        mWatch.work();

//        String json = "{'name':'hfy'}";
//        Man man = mGson.fromJson(json, Man.class);
//        Log.i(TAG, "initData: " + man.toString());
//        Log.i(TAG, "initData: mGson:" + mGson.hashCode() + ",mGson1:" + mGson1.hashCode());
//
//        Car car = mCarLazy.get();
//        car.run();
    }

    private void initConfig() {
        ButterKnife.bind(this);
    }

    private void initView() {
        //PagerAdapter
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());

        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), titles, fragments);
        mVpHomePage.setAdapter(adapter);

        //TabLayout关联ViewPage
        mTlHomeTab.setupWithViewPager(mVpHomePage);

        initToolbar();

        initDrawerLayout();

//        initCustomWindow();
    }

    private void initToolbar() {
        //设置Toolbar：意思是把Toolbar当做ActionBar来用。实际上可以不用这句。
//        setSupportActionBar(mToolbar);

        //设置menu（直接用mToolbar设置menu。因为上面没有设置setSupportActionBar(mToolbar)，即不用重写onCreateOptionsMenu(Menu menu)）
        mToolbar.inflateMenu(R.menu.main);

        //设置左侧箭头 监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });

        //设置menu item 点击监听
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.item_share:
                        Toast.makeText(MainActivity.this, "分享", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_settings:
                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        View close = findViewById(R.id.tv_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        //Toolbar的背景设置为 palette从图片提取到的活力色 (目的是动态 适应当前界面的色调)
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(@Nullable Palette palette) {
//                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
//                mToolbar.setBackgroundDrawable(new ColorDrawable(vibrantSwatch.getRgb()));
//            }
//        });

    }

    private void initDrawerLayout() {
        //用DrawerLayout实现侧滑
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.close);
        mActionBarDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
//        Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show();

        //侧滑页面的导航菜单 选中监听
//        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int itemId = menuItem.getItemId();
//                CharSequence title = menuItem.getTitle();
//                Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    private void initCustomWindow() {
        //6.0以上需要用户手动打开权限
        // (SYSTEM_ALERT_WINDOW and WRITE_SETTINGS, 这两个权限比较特殊，
        // 不能通过代码申请方式获取，必须得用户打开软件设置页手动打开，才能授权。Manifest申请该权限是无效的。)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                //打开设置页，让用户打开设置
                Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivity.this.getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                //已经打开了权限
                handleAddWindow();
            }
        } else {
            //6.0以下直接 Manifest申请该权限 就行。
            handleAddWindow();
        }
    }

    private void handleAddWindow() {

        //子线程创建window，只能由这个子线程访问 window的view
        Button button = new Button(MainActivity.this);
        button.setText("添加到window中的button");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showMsg(MainActivity.this, "点了button");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //因为添加window是IPC操作，回调回来时，需要handler切换线程，所以需要Looper
                Looper.prepare();

                addWindow(button);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setText("文字变了！！！");
                    }
                }, 3000);

                //这里也是可以showToast
                Toast.makeText(MainActivity.this, "子线程showToast", Toast.LENGTH_SHORT).show();

                //开启looper，循环取消息。
                Looper.loop();
            }
        }).start();

        //这里执行就会报错：Only the original thread that created a view hierarchy can touch its views.
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                button.setText("文字 you 变了！！！");
//            }
//        },4000);
    }

    private void addWindow(Button view) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );
        // flag 设置 Window 属性
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // type 设置 Window 类别（层级）
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.x = 100;
        layoutParams.y = 100;

        WindowManager windowManager = getWindowManager();
        windowManager.addView(view, layoutParams);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //加载menu；如果actionbar存在就把items添加到actionbar
//        //注意，因为上面没有调用setSupportActionBar(mToolbar)，且主题中NoActionbar，即没有Actionbar。所以本方法也不用重写。
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //打开了权限
                    if (Settings.canDrawOverlays(this)) {
                        handleAddWindow();
                    } else {
                        Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
