package com.hfy.demo01.hook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hfy.demo01.databinding.ActivityTestHookBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * test hook
 */
public class TestHookActivity extends AppCompatActivity {

    public static final String TAG = "hfy TestHookActivity";
    private ActivityTestHookBinding binding;

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, TestHookActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestHookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            testReflection("com.hfy.demo01.hook.reflect.Cat");
        }

        initView();
    }

    /**
     * 反射
     * 由于可以使用 路径字符串 就可以创建对应的对象，及使用其属性和方法，
     * 所以，可以动态配置 路径字符串，实现不同的功能。而这里的代码无需修改de。
     * 优点：使用灵活，一般可支撑框架的底层实现
     * 缺点：解释执行，速度有影响。（可用setAccessible(true)，提高效率）
     * @param className 可配置的 类名全路径
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void testReflection(String className) {

        try {
            //1.通过目标类路径拿到 Class对象，
            Class<?> aClass = Class.forName(className);

            //2.使用Constructor对象创建目标类实例instance
            Constructor<?> aClassDeclaredConstructor = aClass.getDeclaredConstructor();
            Object instance = aClassDeclaredConstructor.newInstance();

            //3.使用Field对象 设置、获取 目标类实例instance的目标属性值
            Field aClassField = aClass.getDeclaredField("name");
            aClassField.setAccessible(true); //非public需要 关闭访问检查。（如果本身就是public，可也可设置true，可提高效率）
            aClassField.set(instance, "哆啦A梦");
            Log.i(TAG, "testReflection aClassField.get : "+aClassField.get(instance));

            //4.使用Method对象 进行目标方法的执行
            Method aClassMethod = aClass.getMethod("eat");
            aClassMethod.invoke(instance);

        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        binding.btnTesHook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestHookActivity.this, "点击了", Toast.LENGTH_SHORT).show();
            }
        });

        HookUtil.hookViewClick(binding.btnTesHook);
    }


}