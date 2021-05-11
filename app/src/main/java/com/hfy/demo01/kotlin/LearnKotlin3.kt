package com.hfy.demo01.kotlin


fun main() {

    val animal1 = Animal(height = 10)
    println(" animal= $animal1")
    animal1.printSex()
    val animal2 = Animal("白色", "北极星")
    val animal3 = Animal("白色", "北极星", 50)
}

//接口
interface Bird{

    val height :Int
    //接口汇总属性的默认赋值方法
    val name: String
        get() = "小鸟"

    //接口中 方法可以有默认实现
    fun eat(){
        println("吃虫子")
    }
}

/**
 * Animal类
 * @constructor
 * 构造方法方法 含 默认参数，创建实例时 可随意初始化想要的属性
 * 构造方法内的参数 前 声明 var/val 后 会在类中自动生成对应的属性，不声明 var/val 则没有
 *
 * @param color
 */
open class Animal(var color:String = "黑色",  name: String = "小动物", var height: Int = 0){

    //延迟初始化：lateinit，修饰var的属性，可以不用 在初始化对象的时候就有值
    lateinit var sex:String

    //延迟初始化：by lazy，修饰val的属性，属性被首次调用时 赋值
    val sex2: String by lazy {
        if (color == "黑色") "雌性" else "雄性"
    }

    //init,初始化，在 创建实例时调用，从上到下
    init {
        name.length
        sex = if (color == "黑色") "雌性" else "雄性"
        println("sex = ${sex}")
    }

    init {
        name.length
    }

    /**
     * 从构造方法
     * this("红色",getNameByDescribe(describe),2) 表示委托柱构造方法，意思是开始只知道动物的描述，然后查明动物，再委托主构造方法
     */
    constructor(describe:String):this("红色", getNameByDescribe(describe),2){
        this.height = height;
    }

    fun eat() {
    }

    fun printSex(){
        sex = if (color == "黑色") "雌性" else "雄性"
        println("sex = ${sex}")
    }

}

/**
 * Duck继承自Animal，Animal需要用open修饰，表示可被继承
 *
 */
data class Duck(val name:String) {
    companion object{
        val age = 10
    }
}

private fun getNameByDescribe(describe: String): String {
    return "通过描述得知，是火烈鸟"
}