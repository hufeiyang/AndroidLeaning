package com.hfy.demo01.kotlin

/**
 * @author hufeiyang
 * @data 2021/4/2
 * @Description:
 */

fun main() {
    val mutableListOf = mutableListOf(1, 2, 3)
    mutableListOf.switch(2,3)
    println("mutableListOf = ${mutableListOf}")
}

/**
 * 扩展函数
 *
 * @param T 申明的fan'xin
 * @param index1
 * @param index2
 */
fun <T> MutableList<T>.switch(index1:Int, index2:Int){
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}