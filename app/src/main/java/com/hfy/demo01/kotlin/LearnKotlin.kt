package com.hfy.demo01.kotlin

import android.util.Log
import kotlin.math.max

/**
 * @author hufeiyang
 * @data 2021/3/18
 * @Description:
 *
 * 1.永远使用val声明变量，不满足时再使用var
 * 2.
 */

//顶层常量
const val TAG1 = "LearnKotlin"

fun main(){

    val kFunction0 = ::LearnKotlin

//    ManInKotlin::sum2(ManInKotlin::sum, 3)
//    println("学习kotlin")

    val larger = larger(1, 5)
    println("larger:$larger")

    val items = listOf("apple", "banana", "kiwifruit")
    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }

    val testWhen = testWhen(1)
    println("testWhen = ${testWhen}")

    val testWhen1 = testWhen(1000L)
    println("testWhen1 = ${testWhen1}")

//    val testWhen2 = testWhen(2)
//    println("testWhen2 = ${testWhen2}")

    val testWhen3 = testWhen(35)
    println("testWhen2 = ${testWhen3}")

//    val testWhen4 = testWhen("other")
//    println("testWhen3 = ${testWhen4}")

    val p:String = "p"

    val length1 = p?.length

    val length = p.length

    // 对一个对象实例调用多个方法:with
    val myTurtle = Turtle()
    with(myTurtle) { // 画一个 100 像素的正方形
        penDown()
        for (i in 1..4) {
            forward(100.0)
            turn(90.0)
        }
        penUp()
        name = "hehe"
    }

    //配置对象的属性（apply）,这对于配置未出现在对象构造函数中的属性非常有用。
    val myRectangle = Turtle().apply {
        name = "hehe"
        age = 5
        penDown()
    }

    //交换两个变量
//    var a = 1
//    var b = 2
//    a = b.also { b = a }

    val oneMillion = 1000_000
    val creditCardNumber = 1234_5678_9012_3456L
    val socialSecurityNumber = 999_99_9999L
    val hexBytes = 0xFF_EC_DE_5E
    val bytes = 0b11010010_01101001_10010100_10010010

    val a: Int = 100
    val toLong = a.toLong()
    val c :Long = a.toLong()
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a

    val b: Int = 10000
    val boxedB: Int? = b
    val anotherBoxedB: Int? = b

    println(boxedA === anotherBoxedA) // true
    println(boxedB === anotherBoxedB) // false

    val arrayOf = arrayOf(1, 2, 3)
    val intArrayOf = intArrayOf(1, 2, 3)

    val text1 :String = """ hhhhh \n 
        |aaaa 
    """.trimMargin()
    println()
    println("text1 = ${text1}")
    val text2 :String = " hhhhh \naaaaa"
    println("text2 = ${text2}")

    val max = if (a > b) {
        print("Choose a")
        a //分支最后一行作为值返回
    } else {
        print("Choose b")
        b //分支最后一行作为值返回
    }

//    return:默认从最直接包围它的函数或者匿名函数返回。
//    break:终止最直接包围它的循环。
//    continue:继续下一次最直接包围它的循环。

    loop@ for (i in 1..3) {
        for (j in 1..3) {
            if (j>1 && i>1) break@loop
            println("inner j = ${j}")
        }
        println("outer i = ${i}")
    }

    println()

    foo()
    foo1()
    foo2()
}

fun foo() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return // 非局部直接返回到 foo() 的调用者 (forEach是内联函数，所以可以 返回到 foo() 的调用者 )
        print(it)
    }
    println("this point is unreachable")
}

/*****以下三个中的 return，即局部返回， 类似于在常规循环中使用 continue *****/
fun foo1() {
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
        print(it)
    }
    print(" done with explicit label")
}

fun foo2() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
        print(it)
    }
    print(" done with implicit label")
}

fun foo3() {
    listOf(1, 2, 3, 4, 5).forEach(fun(value: Int) {
        if (value == 3) return  // 局部返回到匿名函数的调用者，即 forEach 循环
        print(value)
    })
    print(" done with anonymous function")
}

class Turtle {
    var name:String = ""
    var age:Int = 0

    fun penDown() {}
    fun penUp() {}
    fun turn(degrees: Double) {}
    fun forward(pixels: Double) {}
}

fun foo(param: Int) {
    val result = when (param) {
        1 -> {
            "one"
        }
        2 -> {
            "two"
        }
        else -> {
            "three"
        }
    }
}

fun testWhen(param:Any):String{

    return when(param){
        1->"param:$param"
        is Long -> "is Long:$param"
//        !is String -> "!is String:$param"
        param as Int in 30..40 -> "param as Int in 30..40：$param"
        else -> "else:$param"
    }
}

fun larger(num1:Int, num2:Int):Int{
    return max(num1,num2)
}

class LearnKotlin {

    var name = "小明"
    var age = 20
    var female = true

//    val height1: Int //报错
    val height2: Int = 0
    val height3 = 0

    var a = "a"


    companion object {
        const val TAG = "ManInKotlin"
    }

    /**
     * 工作
     * city:城市
     * date:时间
     * return 工资
     */
    fun work(city:String, date:String):Int {
        var salary = 1000
        val height: Int

        //非空
        var a  = "a"
//        a = null

        //？表示可空
        var b:String?= "b"
//        b = null
        val length = b?.length



        Log.d(TAG, toString())
        Log.d(TAG, "work, city:$city、date:$date、salary:$salary")
        return salary;
    }

    /**
     * 工作外包
     */
    fun wrapWork1(wrapper: String, city:String, date:String): Int {

        val list = listOf("a", "b", null)
        for (s in list) {
            //?.搭配let{}，只会打印a、b ，忽略 null
            s?.let { println(s) }
        }

        var a :String?= "a"

        //Elvis 操作符 ?:
        // 如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式。
        // 请注意，当且仅当左侧为空时，才会对右侧表达式求值。
        //这里 a?.length 是可能为空的
        var b = a?.length ?: -1

        //非空断言运算符（!!），如果你想要一个 NPE，你可以得到它
        //所以 这里可能会产生空指针
        val length = a!!.length

        val c = 1000
        val cc = c as? Int

        //过滤出非空item
        val listOf = listOf("a", "b", null)
        val filterNotNull = listOf.filterNotNull()

        //类型检测、自动转换
        if (c is Int) {
            val compareTo = c.compareTo(2)
        }

        //区间判断：in
        c in 1..2000

        return work(city,date);
    }

    /**
     * 工作外包(语法糖写法)
     */
    fun wrapWork2(wrapper: String, city: String, date: String): Int = work(city,date)

    /**
     * 工作外包(语法糖写法+类型推导)
     */
    fun wrapWork3(wrapper: String, city:String, date:String) = work(city, date)

    /**
     * 上班方式
     */
    fun goCompony1(time: Int): String {
        return if (time <= 9) {
            "metro"
        } else {
            "texi"
        }
    }

    /**
     * 上班方式
     *
     * @param time
     * @return
     */
    fun goCompony2(time: Int): String = if (time <= 9) { "metro" } else { "texi" }

    /**
     * 求和
     * @param x
     * @param y
     * @return
     */
    fun sum(x: Int, y: Int): Int {
        //局部函数
        fun sumTotal(y:Int): Int{
            Log.i(TAG, "sumTotal: ")

            return x + y
        }

        return x + y
    }

    /**
     * TODO
     *
     * @param su 函数作为参数
     * @param y
     * @return
     */
    fun sum2(su: (Int, Int) -> Int , y: Int): Int {

        return su(1, 2) + y
    }

    fun sum3(su: (Int, Int) -> Int , y: Int): Int {

        return su(1, 2) + y
    }

    fun main(){

        val kFunction0 = ::LearnKotlin

//    ManInKotlin::sum2(ManInKotlin::sum, 3)
//    println("学习kotlin")
    }


    override fun toString(): String {
        return "Man(name='$name', age=$age, female=$female)"
    }


}