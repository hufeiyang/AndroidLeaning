package com.hfy.demo01.kotlin

/**
 * @author hufeiyang
 * @data 2021/4/1
 * @Description:
 */

fun main() {
    val people = People("小明",20, 100)
    val firstProperty = people.firstProperty
}

/**
 * 子类 构造函数 必须 初始化其基类型
 *
 * 在构造派生类的新实例的过程中，第一步完成其基类的初始化（在之前只有对基类构造函数参数的求值），因此发生在派生类的初始化逻辑运行之前。
 */
class Work11: Worker1() {
    override fun eat(food: String){
    }

}

 open class Worker1 : People("name",10){

     //已经override的函数 默认 可被重写，不用在加open了
    override fun eat(food: String){
         //通过super调用父类的方法
        super.run(100)
    }

     fun play(){
         super.eat("apple");
         eat("apple");
     }
}

/**
 * 如果派生类有一个主构造函数，其基类可以（并且必须） 用派生类主构造函数的参数就地初始化。
 */
class Worker2(override val name: String = "hehe", age: Int, weight: Int) : People(name, age) {

}

/**
 * 如果派生类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，
 * 或委托给另一个构造函数做到这一点。 注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数
 */
class Worker3 : People{
    constructor(name: String, age: Int) : super(name,age)
    constructor(name: String, age: Int, weight: Int) : super(name,age,weight)

    inner class Book{
        fun sale(){
            run(100)
        }
    }
}

/**
 * open 可被继承
 *
 * @param name
 */
open class People constructor(open val name: String, val age: Int){

    var firstProperty = "First property: $name".also(::println)

    //自定义 getter、setter
    var home get() = if(age>30) this.toString() else "sss"
        set(value){ firstProperty = value+"dd" }

    //如果属性至少一个访问器使用默认实现，或者自定义访问器通过 field 引用幕后字段，将会为该属性生成一个幕后字段。

    var color = "blue"
        get() = "red"
    set(value){
        field = if (value == "red") "green" else  "yellow"
    }

    //by lazy,只能作用val，本质是属性委托
    val sex1: Int by lazy{
        if (true) 100 else 200
    }

    val sex2: String by lazy {
        if (color == "黑色") "雌性" else "雄性"
    }

    //lateinit，只能作用 var的类属性，编译器检查到未赋值，不会报错
    lateinit var people :People

    init {
        println("age = ${age}")
        firstProperty
    }

    /**
     * 次构造函数 委托给 主构造函数：如果类有一个主构造函数，每个次构造函数需要委托给主构造函数，
     * 可以直接委托或者通过别的次构造函数间接委托。委托到同一个类的另一个构造函数用 this 关键字即可
     *
     * 初始化块中的代码实际上会成为主构造函数的一部分。
     * 委托给主构造函数会作为次构造函数的第一条语句，因此所有初始化块与属性初始化器中的代码都会在次构造函数体之前执行。
     * 即使该类没有主构造函数，这种委托仍会隐式发生，并且仍会执行初始化块
     *
     */
    constructor (name: String, age: Int, weight: Int): this(name, age){
        println("weight = ${weight}")
    }

    open fun eat(food: String){
    }

    fun run(distance: Int){

    }

    override fun toString(): String {
        return super.toString()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }


    open class Rectangle {
        open fun draw() { /* …… */ }
    }

    interface Polygon {
        fun draw() { /* …… */ } // 接口成员默认就是“open”的
    }

    class Square() : Rectangle(), Polygon {
        // 编译器要求覆盖 draw()：
        //可以同时继承 Rectangle 与 Polygon，
        // 但是二者都有各自的 draw() 实现，所以我们必须在 Square 中覆盖 draw()， 并提供其自身的实现以消除歧义。
        override fun draw() {
            //使用由尖括号中超类型名限定的 super,表示采用从哪个超类型继承的实现，
            super<Rectangle>.draw() // 调用 Rectangle.draw()
//            super<Polygon>.draw() // 调用 Polygon.draw()
        }
    }



    //object:  创建匿名类实例
//    var man: Man = object:Man, Runnable {
//        override fun eat() {
//            TODO("Not yet implemented")
//        }
//
//        override fun invoke() {
//            TODO("Not yet implemented")
//        }
//    }
//
//    var man2 = object :Man{
//        override fun eat() {
//            TODO("Not yet implemented")
//        }
//
//    }
//
//    //SAM 转换 后的 匿名类实例
//    var man3 = Man { TODO("Not yet implemented") }
}

/**
 * 函数式接口
 */
//fun interface Man{
//    fun eat()
//    fun walk(){}
//}
//
//fun interface Runnable {
//    fun invoke()
//
//}