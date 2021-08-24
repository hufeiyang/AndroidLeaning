package com.hfy.demo01.hook

import android.app.Activity
import android.app.Instrumentation
import android.os.Bundle
import android.util.Log
import android.view.View
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Hook 工具
 */
class HookUtil {

    companion object{

        val TAG: String = "HookUtil"
        /**
         * hookInstrumentation(静态代理)
         */
        @JvmStatic
        fun hookInstrumentation(){
            //1.拿到 sCurrentActivityThread 对象（静态对象，可以直接拿）
            val classActivityThread = Class.forName("android.app.ActivityThread")
            val fieldCurrentActivityThread = classActivityThread.getDeclaredField("sCurrentActivityThread")
            fieldCurrentActivityThread.isAccessible = true
            val sCurrentActivityThread = fieldCurrentActivityThread.get(null)

            //2.拿到原来的instrumentation对象，非静态，需要sCurrentActivityThread
            val filedInstrumentation = classActivityThread.getDeclaredField("mInstrumentation")
            filedInstrumentation.isAccessible = true
            val oriInstrumentation = filedInstrumentation.get(sCurrentActivityThread)

            //3. 创建代理对象，传入源对象
            val instrumentationDelegate = InstrumentationDelegate(oriInstrumentation as Instrumentation)

            //4. 原流程注入代理对象
            filedInstrumentation.set(sCurrentActivityThread, instrumentationDelegate)
        }

        /**
         * hookViewClick（静态代理、动态代理）
         */
        @JvmStatic
        fun hookViewClick(view: View){
            //1
            //            val viewClass = view.javaClass
//            val fieldListenerInfo = viewClass.getDeclaredField("mListenerInfo")
//            fieldListenerInfo.isAccessible = true
//            val mListenerInfo = fieldListenerInfo.get(view)

            val getListenerInfo = View::class.java.getDeclaredMethod("getListenerInfo")
            //修改getListenerInfo为可访问(View中的getListenerInfo不是public)
            getListenerInfo.isAccessible = true
            val mListenerInfo = getListenerInfo.invoke(view)

            //2
            val javaClassListenerInfo = mListenerInfo.javaClass
            val declaredFieldOnClickListener = javaClassListenerInfo.getDeclaredField("mOnClickListener")
            declaredFieldOnClickListener.isAccessible = true
            val oriClickListener = declaredFieldOnClickListener.get(mListenerInfo)

            //3
            // Koltin 之 动态代理InvocationHandler: https://juejin.cn/post/6844903997929684999

            val clickListenerProxyInstance = Proxy.newProxyInstance(view.javaClass.classLoader, oriClickListener.javaClass.interfaces, object : InvocationHandler {
                override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
                    Log.i(TAG, "ViewClick invoke: hook!")
                    val obj = method!!.invoke(oriClickListener, *(args ?: emptyArray()))
                    return obj
                }

            })

            //4
//            declaredFieldOnClickListener.set(mListenerInfo, OnClickListenerProxy(oriClickListener as View.OnClickListener))
            declaredFieldOnClickListener.set(mListenerInfo, clickListenerProxyInstance)
        }

    }

    class InstrumentationDelegate(private val oriInstrumentation: Instrumentation) : Instrumentation(){

        override fun callActivityOnCreate(activity: Activity?, icicle: Bundle?) {
            Log.i(TAG, "Instrumentation callActivityOnCreate: hooked!")
            oriInstrumentation.callActivityOnCreate(activity, icicle)
        }
    }

    /**
     * 代理类，同样实现自 View.OnClickListener，同时传入originalListener
     */
    class OnClickListenerProxy(private val mOriginalListener: View.OnClickListener) : View.OnClickListener {
        override fun onClick(v: View) {
            mOriginalListener.onClick(v)
            Log.i(TAG, "view onClick: hooked!")
        }
    }
}