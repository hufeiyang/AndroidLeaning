package com.hfy.demo01.common

import com.hfy.demo01.MainActivity

/**
 *
 * @author bytedance
 * @date 2022/9/22
 * @desc
 */
object Constant {
    const val commonTagString = "hufeiyang"
    val HomeActivityLogTag = commonTagString +MainActivity::class.java.simpleName//hufeiyangMainActivity
}