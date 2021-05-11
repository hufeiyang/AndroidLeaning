package com.hfy.demo01.kotlin

import java.util.*
import kotlin.collections.HashSet

/**
 * @author hufeiyang
 * @data 2021/3/29
 * @Description:
 */

fun main() {
    //集合操作1：map，转换
    val a = intArrayOf(1, 2, 3)
    val map = a.map { it * 2 }
    println("map = $map")


    val student1 = Student( "student1",11,"m", 81)
    val student2 = Student( "student2",12,"f", 82)
    val student3 = Student( "student3",13,"f", 83)
    val student4 = Student( "student4",14,"m", 84)

    val students = listOf(student1,student2,student3,student4)

    //集合操作2：filter，过滤，传入的表达式返回值必须为Boolean
    val maleStudents = students.filter { it.sex == "m" }
    //过滤掉，与filter相反
    val femaleStudents = students.filterNot { it.sex == "m" }
    //过滤非空
    val notNullStudents = students.filterNotNull()
    //过滤满足条件的数量,count，男生的数量
    students.count { it.sex == "m" }

    //求和，sumBy，学生的总分
    val sumScore = students.sumBy { it.score }
    //sum 只能对数值类型的列表求和
    val sum = listOf(1, 2, 3).sum()

    //折叠，fold，递归思想- 一个操作的结果 给到下一个操作，例如阶乘。
    val fold = listOf(1, 2, 3).fold(1, { mul, item -> mul * item })
    println("fold = $fold")

    listOf(1, 2, 3).reduce { mul, item -> mul * item }


    //zip
    val numberPairs = listOf("one" to 1, "two" to 2, "three" to 3, "four" to 4)
    println(numberPairs.unzip())

    //关联：associateWith
    val numbers9 = listOf("one", "two", "three", "four")
    println(numbers9.associateWith { it.length })


    val names = listOf("Alice Adams", "Brian Brown", "Clara Campbell")
//    println(names.associate { name -> parseFullName(name).let { it.lastName to it.firstName } })

    val numbers5 = listOf("one", "two", "three", "four")

    println(numbers5)
    println(numbers5.joinToString())

    val listString = StringBuffer("The list of numbers: ")
    numbers5.joinTo(listString)
    println(listString)

    val numbers6 = (0..13).toList()
    println(numbers6.chunked(3))

    val numbers7 = (0..13).toList()
    println(numbers7.chunked(3) { it.sum() })


    //集合:
    // MutableCollection 是一个具有写操作的 Collection 接口，例如 add 以及 remove。
    val words = "A long time ago in a galaxy far far away".split(" ")
    val shortWords = mutableListOf<String>()
    words.getShortWordsTo(shortWords, 3)
    println(shortWords)

    //集合构造
    val doubled = List(3, { it * 2 })  // 如果你想操作这个集合，应使用 MutableList
    println(doubled)

    val linkedList = LinkedList<String>(listOf("one", "two", "three"))
    val presizedSet = HashSet<Int>(32)

    //构造map
    val numbersMap1 = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)
    val numbersMap2 = mutableMapOf<String,String>().apply { this.put("one","1") ; this["two"] = "2" }

    //复制
    val sourceList = mutableListOf(1, 2, 3)
    val copyList = sourceList.toMutableList()
    val readOnlyCopyList = sourceList.toList()
    sourceList.add(4)
    println("Copy size: ${copyList.size}")

//readOnlyCopyList.add(4)             // 编译异常
    println("Read-only copy size: ${readOnlyCopyList.size}")

    //集合迭代
    val numbers = listOf("one", "two", "three", "four")
    val numbersIterator = numbers.iterator()
    while (numbersIterator.hasNext()) {
        println(numbersIterator.next())
    }

    val numbers1 = listOf("one", "two", "three", "four")
    numbers1.forEach {
        println(it)
    }

    //双向迭代：listIterator()
    val numbers2 = listOf("one", "two", "three", "four")
    val listIterator = numbers2.listIterator()
    //向后迭代
    while (listIterator.hasNext()) listIterator.next()
    println("Iterating backwards:")
    //向前迭代
    while (listIterator.hasPrevious()) {
        print("Index: ${listIterator.previousIndex()}")
        println(", value: ${listIterator.previous()}")
    }

    //可变迭代器：可
    val numbers3 = mutableListOf("one", "two", "three", "four")
    val mutableIterator = numbers3.iterator()

    mutableIterator.next()
    mutableIterator.remove()
    println("After removal: $numbers3")

    val numbers4 = mutableListOf("one", "four", "four")
    val mutableListIterator = numbers4.listIterator()

    mutableListIterator.next()
    mutableListIterator.add("two")
    mutableListIterator.next()
    mutableListIterator.set("three")
    println(numbers4)

    //区间  ClosedRange
    val i:Int = 2
    //IntRange
    if (i in 1..4) {  // 等同于 1 <= i && i <= 4
        print(i)
    }

    for (x in 1.rangeTo(4)) println(x)
    for (x in 1..4) println(x)
    for (x in 1 downTo  4  step 2) println(x)

    //创建一个区间: Version实现了Comparable
    val closedRange = Version(1, 100)..Version(5, 500)

    if (Version(2,100) in closedRange){
        println("Version(1,100) in closedRange")
    }

    //数列：IntProgression，实现了数列实现 Iterable<N>， IntRange就继承自IntProgression，
    println( (1..10).filter { it % 2 == 0 } )




    //序列
    val numbersSequence = sequenceOf("four", "three", "two", "one")

    val oddNumbers = generateSequence(1) { it + 2 } // `it` 是上一个元素
    println(oddNumbers.take(5).toList())
//println(oddNumbers.count())     // 错误：此序列是无限的。

    val words1 = "The quick brown fox jumps over the lazy dog".split(" ")
    val lengthsList = words1.filter { println("filter: $it"); it.length > 3 }
            .map { println("length: ${it.length}"); it.length }
            .take(4)

    println("Lengths of first 4 words longer than 3 chars:")
    println(lengthsList)


    (1..10).filter { it % 2 == 0 }
//
//    val numbers = listOf("one", "two", "three", "four")
//    val (match, rest) = numbers.partition { it.length > 3 }
//
//    val numbers = listOf("one", "two", "three", "four")

    val plusList = numbers.plus("five")
    val minusList = numbers - listOf("three", "four")
    println(plusList)
    println(minusList)
}


fun List<String>.getShortWordsTo(shortWords: MutableList<String>, maxLength: Int) {
    this.filterTo(shortWords) { it.length <= maxLength }
    // throwing away the articles
    val articles = setOf("a", "A", "an", "An", "the", "The")
    shortWords -= articles
}

data class Student(val name: String, val age: Int, val sex: String, val score: Int)

data class Version(val high:Int, val low:Int): Comparable<Version> {
    override fun compareTo(other: Version): Int {
        if (this.high != other.high) {
            return this.high - other.high
        }
        return this.low - other.low
    }
}
