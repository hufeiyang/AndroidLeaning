package com.hfy.demo01.kotlin

/**
 * @author hufeiyang
 * @data 2021/3/26
 * @Description:
 */

fun main(){

    //val：引用不可变，但引用的对象可修改
    val a = intArrayOf(1,2,3)
//    a = intArrayOf(1,2,3) //报错
    a[0] = 10 //可以改

    //val 的 book 其属性可变
    val book1 = Book("《Android1》", 10)
    val book2 = Book("《Android2》",20)
    val book3 = Book("《Android3》",30)
//    book = Book("《Java》") //报错
    book1.name = "《Kotlin》"

    //双冒号引用属性、防范
    println(book1::name)
    val name = book1::printName

    //匿名函数
    fun (book : Book):Boolean{
        return book.price > 10
    }

    //高阶函数：参数是函数类型
    val books = listOf(book1,book2,book3)
    //过滤书本1：双冒号的方式引用函数 （引用已有的固定的函数）
    val filterBooks1 = filterBooks(books, book1::isHighPrice)
    //过滤书本2：匿名函数的方法（可随意写，不需要提前定义）
    val filterBooks2 = filterBooks(books, fun (book : Book):Boolean{
        return book.price > 10
    })
    //过滤书本3.1：lambda表达式（简化表达后的匿名函数，一种语法糖）
    val filterBooks3 = filterBooks(books, { b -> b.price > 10 })
    //过滤书本3.2：lambda表达式 拖尾，柯里化风格（最后一个参数为函数类型时）
    val filterBooks4 = filterBooks(books) { b -> b.price > 10 }
    println(filterBooks1)


    //定义一个lambda表达式：两数相加
    val sum1: (Int,Int) -> Int = {x:Int,y:Int -> x+y}
    //类型推导，省略 函数类型声明
    val sum2 = {x:Int,y:Int -> x+y}
    //类型推导，省略 参数类型
    val sum3: (Int,Int) -> Int = {x,y -> x+y}
    println(sum1(2,3))

    //扩展函数:
    //1.给Int 扩展一个函数sum
    val sum :Int.(Int) -> Int = {i -> plus(i)}
    1.sum(2)
    //给Book扩展一个函数getPageCount
    val getPageCount :Book.(Boolean) -> Int =  {b -> if(b)100 else 0}
    val pageCount = Book("<书>", 20).getPageCount(true)
    println("pageCount = $pageCount")

    forTest()
    infixTest()
}

data class Book(var name: String, var price: Int){
    fun printName(): String{
        println(this.name)

        return name
    }

    /**
     * 高于10块钱 的书
     * @param book
     * @return
     */
    fun isHighPrice(book:Book):Boolean{
        return book.price > 10
    }
}


/**
 * 代码块函数体
 */
fun sum(x:Int, y:Int): Int{
    return x+y
}

/**
 * 表达式函数体
 */
fun sum2(x:Int, y:Int):Int = x+y

/**
 * 表达式函数体，省略返回值
 */
fun sum3(x:Int, y:Int) = x+y

/**
 * 高阶函数：按条件 过滤书本
 *
 * @param books
 * @param condition 过滤条件
 */
fun filterBooks(books:List<Book>, condition: (Book)->Boolean): List<Book>{
    val filttedBooks = mutableListOf<Book>()
    for (book in books) {
        if (condition(book)) {
            filttedBooks.add(book)
        }
    }

    return filttedBooks
}

fun ifStatement(flag: Boolean){
    val a = if (flag) "a" else "b"
    println("a = $a")
}

/**
 * 复合表达式
 * if-else的返回值 就是 try部分的返回值，try-catch的返回值就是 res的值
 */
val res : Int?= try {
    if (true) 1 else null
}catch (e: Exception){
    null
}

// ?: ，getOrElse，为 可空类型的变量 指定 为空时 的值
val a:String? = null
val b = a?:"1"


/**
 * 表达式：when， else相当于default
 * when 表达式 的返回值类型 就是 每个分支的返回值类型
 * @param day
 * @param sunny
 */
fun weekPlan1(day: Day, sunny:Boolean){
    when (day) {
        Day.Mon -> 1
        Day.Tue -> 2
        Day.WEN -> 3
        else ->  when(sunny){
            sunny -> "sunny"
            else -> "no sunny"
        }
    }
}

fun weekPlan2(day: Day, sunny:Boolean) = when (day) {
    Day.Mon -> 1
    Day.Tue -> 2
    Day.WEN -> 3
    else ->  when(sunny){
        sunny -> "sunny"
        else -> "no sunny"
    }
}

//Boolean 类型 when内的参数可省略
fun weekPlan3(day: Day, sunny:Boolean) = when {
    day == Day.Mon -> 1
    day == Day.Tue -> 2
    day == Day.WEN -> 3
    sunny -> "sunny"
    else -> "no sunny"
}

/**
 * 枚举类
 */
enum class Day{
    Mon,
    Tue,
    WEN,
    THU,
    FRI,
    SAT,
    SUN,
}

/**
 * 枚举类
 * @property day
 */
enum class DayOfWeek(val day:Int){
    Mon(1),
    Tue(2)

    ; //如果以下有额外的方法或属性定义，则必须强制加上分号
    fun getDayNum(): Int {
        return day
    }
}


fun forTest(){

    //表达式：for 循环
    for(i in 1..10 step 2){
        println(i)
    }

    for (i in 10 downTo 1 step 3){
        println("i = $i")
    }

    //不含10
    for (i in 1 until 5){
        println("i = $i")
    }

    //检查成员关系 in 和 !in
    val isIn1 = 1 in intArrayOf(1, 2, 3)
    val isIn2 = "a" !in listOf("a", 'b', "c")

    val intArrayOf = intArrayOf(1, 2, 3, 4, 5)
    for ((index, value) in intArrayOf.withIndex()){
        println("index = ${index}, value = $value")
    }

}

fun infixTest() {
    val person = Person()
    //中缀表达式，使得更像自然语言
    person eat "苹果"
    person talk "你好！"
//    person.eat( "苹果")
}


 class Person{
     /**
      * 中缀函数， infix
      * @param food
      */
    infix fun eat(food:String){
        println("I eat food = $food")
    }
}

infix fun Person.talk(msg:String){
    println("Person talk msg = $msg")
}

//字符串
fun stringTest(){
    val a = "hello world!"
    val b = "hello world! \n"
    if (a.isEmpty()) {
        println("a = $a")
    }
    //== ,内容是都相等
    println(a == b)
    //=== ,引用是否相等
    println(a === b)

    val c = """ 三个引号代表 原生字符串，遇到特殊字符 不会转义  \n """

}