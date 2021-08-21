package com.hfy.demo01.hook

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * test 动态代理
 */
class TestInvocationHandler: InvocationHandler {



    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        TODO("Not yet implemented")
    }

}