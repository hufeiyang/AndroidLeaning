package com.hfy.demo01.kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hfy.demo01.R
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * kotlin学习
 * 文章：https://juejin.cn/post/6908271959381901325
 */

class KotlinTestActivity : AppCompatActivity(),
    //使用委托模式来让 Activity 实现 CoroutineScope 接口，从而可以在 Activity 内直接启动协程而不必显示地指定它们的上下文，并且在 onDestroy()中自动取消所有协程
    CoroutineScope by CoroutineScope(Dispatchers.Default) {

    val TAG = "hfyKotlinTestActivity"


    companion object {
        @JvmStatic
        fun launch(activity: Activity) {
            val intent = Intent(activity, KotlinTestActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)


        //启动了一个协程
//        test1()

        //挂起与恢复
//        test2_suspend()

        //作用域：CoroutineScope
//        test3_GlobalScope()
//        test3_runBlocking()
//        test4_coroutineScope()
//        test5_supervisorScope()
//        test6_customCoroutineScope()

        //协程构建器：CoroutineBuilder
//        test7_launch()
//        test8_job()
//        test9_async()
//        test10_async()
//        test11_async()

        //协程上下文：CoroutineContext，使用以下元素集定义协程的行为：
        //Job：控制协程的生命周期
        //CoroutineDispatcher：将任务指派给适当的线程
        //CoroutineName：协程的名称，可用于调试
        //CoroutineExceptionHandler：处理未捕获的异常

//        test12_CoroutineContext_Job()
//        test13_CoroutineContext_CoroutineDispatcher()
//        test14_CoroutineContext_withContext()
//        test15_CoroutineContext_CoroutineName()

        //取消协程
        test16_cancel()
    }

    /**
     * 通过 GlobalScope（全局作用域）启动了一个协程，在延迟一秒后输出一行日志。从输出结果可以看出来，启动的协程是运行在协程内部的线程池中。
     * 虽然从表现结果上来看，启动一个协程类似于我们直接使用 Thread 来执行耗时任务，但实际上协程和线程有着本质上的区别。
     * 通过使用协程，可以极大的提高线程的并发效率，避免以往的嵌套回调地狱，极大提高了代码的可读性
     *  涉及到了协程的四个基础概念：
     *  suspend function。即挂起函数，delay() 就是协程库提供的一个用于实现非阻塞式延时的挂起函数
     *  CoroutineScope。即协程作用域，GlobalScope 是 CoroutineScope 的一个实现类，用于指定协程的作用范围，可用于管理多个协程的生命周期，所有协程都需要通过 CoroutineScope 来启动
     *  CoroutineContext。即协程上下文，包含多种类型的配置参数。Dispatchers.IO 就是 CoroutineContext 这个抽象概念的一种实现，用于指定协程的运行载体，即用于指定协程要运行在哪类线程上
     *  CoroutineBuilder。即协程构建器，协程在 CoroutineScope 的上下文中通过 launch、async 等协程构建器来进行声明并启动。launch、async 均被声明为 CoroutineScope 的扩展方法
     */
    private fun test1() {

        GlobalScope.launch(context = Dispatchers.IO) {
            log("launch start")
            delay(1000)
            log("launch finish")
        }

        log("test() finish")

//        2023-08-23 20:22:40.388 15307-15307/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main test() finish
//        2023-08-23 20:22:40.389 15307-15390/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 launch start
//        2023-08-23 20:22:41.394 15307-15390/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 launch finish
    }

    /**
     * 协程在常规函数的基础上添加了两项操作用于处理长时间运行的任务，在invoke（或 call）和return之外，协程添加了suspend和 resume：
     *  suspend 用于暂停执行当前协程，并保存所有局部变量
     *  resume 用于让已暂停的协程从暂停处继续执行
     *
     *  suspend 函数只能由其它 suspend 函数调用，或者是由协程来调用
     *
     *  【挂起函数不会阻塞其所在线程，而是会【将协程挂起】，在特定的时候才再恢复执行】
     *  doSome()、delay() 函数类似于 Java 中的 Thread.sleep()，而之所以说 delay() 函数是非阻塞的，是因为它和单纯的线程休眠有着本质的区别。
     *  例如，当在 ThreadA 上运行的 CoroutineA 调用了delay(1000L)函数指定延迟一秒后再运行，ThreadA 会转而去执行 CoroutineB，等到一秒后再来继续执行 CoroutineA。
     *  所以，ThreadA 并不会因为 CoroutineA 的延时而阻塞，而是能继续去执行其它任务，所以挂起函数并不会阻塞其所在线程，这样就极大地提高了线程的并发灵活度，最大化了线程的利用效率。
     *  而如果是使用Thread.sleep()的话，线程就只能干等着而不能去执行其它任务，降低了线程的利用效率
     */
    private fun test2_suspend() {
        GlobalScope.launch(context = Dispatchers.Main) {
            log("launch2 start")
            val res = doSome() //此处挂起协程（即此处到作用域结束之前的内容暂不执行），doSome()执行完后会resume协程-即执行后面的两个log
            log(res)
            log("launch2 finish")
        }
    }

    private suspend fun doSome(): String {
        log("doSome start")
        withContext(Dispatchers.IO) { //此处挂起(withContext是挂起函数)，{}内执行完后再执行后面的 log("doSome finish")
            log("doSome delay start")
            delay(1000)
            log("doSome delay finish")
        }
        log("doSome finish")

        return "success"
    }

    /**
     * lobalScope 属于 全局作用域，这意味着通过 GlobalScope 启动的协程的生命周期只受整个应用程序的生命周期的限制，只要整个应用程序还在运行且协程的任务还未结束，协程就可以一直运行
     *  GlobalScope 不会阻塞其所在线程，所以以下代码中主线程的日志会早于 GlobalScope 内部输出日志
     *  GlobalScope.launch 会创建一个顶级协程，尽管它很轻量级，但在运行时还是会消耗一些内存资源，且可以一直运行直到整个应用程序停止（只要任务还未结束），这可能会导致内存泄露，所以在日常开发中应该谨慎使用 GlobalScope
     */
    private fun test3_GlobalScope() {
        log("test3() start")

        //GlobalScope不会阻塞当前线程
        //launch中未指定context，则使用Dispatchers.Default，即CPU密集型的工作线程
        GlobalScope.launch {
            log("test3() 1")

            this.launch {
                delay(100)
                log("test3() 2")
            }

            this.launch {
                delay(200)
                log("test3() 3")
            }

            log("test3() 4")
        }

        log("test3() finish")

//        2023-08-23 21:30:26.718 19580-19580/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main test3() start
//        2023-08-23 21:30:26.736 19580-19580/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main test3() finish
//        2023-08-23 21:30:26.737 19580-19673/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test3() 1
//        2023-08-23 21:30:26.741 19580-19673/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test3() 4
//        2023-08-23 21:30:26.844 19580-19673/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test3() 2
//        2023-08-23 21:30:26.945 19580-19673/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test3() 3
    }

    /**
     * runBlocking ：是一个顶层函数，用来启动协程
     * 只有当内部相同作用域的所有协程都运行结束后，声明在 runBlocking 之后的代码才能执行
     * 即 runBlocking 会阻塞其所在线程、等内部相同作用域的协程运行结束后才会才会退出
     *
     * runBlocking会阻塞主当前线程，等到launchA和launchB两个协程都执行结束后才会退出
     * runBlocking 内部启动的协程是非阻塞式的，但 runBlocking 阻塞了其所在线程
     *
     */
    private fun test3_runBlocking() {
        log("start")
        runBlocking {
            log("runBlocking start") // runBlocking执行在当前线程

            this.launch(Dispatchers.IO) {
                repeat(2) {
                    delay(100)
                    log("launchA-$it")
                }
            }

            this.launch { //未指定context 则取在上级协程的context中
                repeat(3) {
                    delay(100)
                    log("launchB-$it")
                }
            }

            GlobalScope.launch { //对runBlocking无影响
                repeat(4) {
                    delay(100)
                    log("GlobalScope-$it")
                }
            }

            log("runBlocking finish") //launchA、launchB、GlobalScope是非阻塞的，所以runBlocking start后就直接走到这里

        }
        log("end") //和runBlocking相同作用域的launchA和launchB执行完、以及runBlocking本身执行完，就不阻塞了，即走这句了

//        2023-08-24 16:13:11.535 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main start
//        2023-08-24 16:13:11.548 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking start
//        2023-08-24 16:13:11.556 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking finish
//        2023-08-24 16:13:11.659 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB-0
//        2023-08-24 16:13:11.660 23808-23896/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-3 GlobalScope-0
//        2023-08-24 16:13:11.660 23808-23894/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 launchA-0
//        2023-08-24 16:13:11.761 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB-1
//        2023-08-24 16:13:11.761 23808-23897/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-4 launchA-1
//        2023-08-24 16:13:11.761 23808-23894/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 GlobalScope-1
//        2023-08-24 16:13:11.862 23808-23894/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 GlobalScope-2
//        2023-08-24 16:13:11.862 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB-2
//        2023-08-24 16:13:11.863 23808-23808/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main end
//        2023-08-24 16:13:11.963 23808-23894/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 GlobalScope-3

    }

    /**
     * coroutineScope 是一个suspend函数，创建一个新的协程作用域，并在该作用域内执行指定代码块，它并不启动协程。
     * 其存在的目的是进行符合结构化并发的并行分解（即，将长耗时任务拆分为并发的多个短耗时任务，并等待所有并发任务完成后再返回）。
     *
     * coroutineScope与runBlocking的区别在于runBlocking会阻塞当前线程，
     * 而coroutineScope 【会挂起所在的协程】，直至其内部任务(包括子协程)执行完成，它不会阻塞所在的线程。
     */
    private fun test4_coroutineScope() {
        log("test4_coroutineScope start")

        runBlocking {  //runBlocking会阻塞当前线程
            log("runBlocking start")

            this.launch {
                delay(100)  //挂起launch协程
                log("launch in runBlocking") //恢复launch协程
            }

            coroutineScope { //挂起runBlocking协程。    coroutineScope是挂起函数，仅创建一个独立的协程作用域，{}内并不是协程代码。
                launch {     //这里才是启动了一个协程，作用域就是coroutineScope
                    delay(500)  //挂起launch协程
                    log("launch in coroutineScope")//恢复launch协程。同时作为coroutineScope的子协程就执行完了，所以coroutineScope的 内部任务和子协程都执行完成了，所以也恢复了runBlocking协程
                }
                delay(50)
                log("coroutineScope") //所以这里会比log("launch in coroutineScope")先走，因为 launch{}非阻塞。
            }

            log("runBlocking finish") //上面log("launch in coroutineScope")处恢复了runBlocking协程，所以紧接着就会走这句
        }

        log("test4_coroutineScope finish") //上面log("runBlocking finish")执行后，runBlocking就执行完了，也就不阻塞了，所以词句执行。

//        2023-08-24 16:20:35.805 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main test4_coroutineScope start
//        2023-08-24 16:20:35.818 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking start
//        2023-08-24 16:20:35.892 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main coroutineScope
//        2023-08-24 16:20:35.964 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launch in runBlocking
//        2023-08-24 16:20:36.364 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launch in coroutineScope
//        2023-08-24 16:20:36.365 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking finish
//        2023-08-24 16:20:36.365 24294-24294/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main test4_coroutineScope finish
    }


    /**
     * supervisorScope 函数用于创建一个使用了 SupervisorJob 的 coroutineScope，该作用域的特点就是抛出的异常不会连锁取消同级协程和父协程
     *
     * 在 coroutineScope中，只要任意一个子协程发生异常，整个 scope都会执行失败，并且其余的所有子协程都会被取消掉；
     * 在 supervisorScope中，一个子协程的异常不会影响整个 scope的执行，也不会影响其余子协程的执行；
     */
    private fun test5_supervisorScope() {

        val context = CoroutineExceptionHandler { coroutineContext, throwable ->
            log("异常：${throwable.message}")
        }
        runBlocking(context) {

            this.launch {
                delay(50)
                log("launch in runBlocking")
            }

            supervisorScope {
                this.launch {
                    delay(100)
                    log("exception in launchA")
                    throw Exception("launchA throw an exception!")
                }

                this.launch {
                    delay(300)
                    log("launchB")
                }
            }

            log("runBlocking finished")

        }

//        2023-08-24 18:21:33.833 327-327/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launch in runBlocking
//        2023-08-24 18:21:33.884 327-327/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main exception in launchA
//        2023-08-24 18:21:33.885 327-327/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main 异常：launchA throw an exception!
//        2023-08-24 18:21:34.084 327-327/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB
//        2023-08-24 18:21:34.084 327-327/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking finished
    }


    /**
     * CoroutineScope，来管理多个协程的生命周期-使用作用域来统一取消子协程
     *
     * 假设我们在 Activity 中先后启动了多个协程用于执行异步耗时操作，那么当 Activity 退出时，必须取消所有协程以避免内存泄漏。
     * 我们可以通过保留每一个 Job 引用然后在 onDestroy方法里来手动取消，但这种方式相当来说会比较繁琐和低效。
     *
     * 可以通过创建与 Activity 生命周期相关联的【协程作用域来管理协程的生命周期】。
     * CoroutineScope 的实例可以通过 CoroutineScope() 或 MainScope() 的工厂函数来构建。
     * 前者创建通用作用域，后者创建 UI 应用程序的作用域并使用 Dispatchers.Main 作为默认的调度器
     *
     * 或者，我们可以通过委托模式来让 Activity 实现 CoroutineScope 接口，
     * 从而可以在 Activity 内直接启动协程而不必显示地指定它们的上下文，并且在 onDestroy()中自动取消所有协程
     */
    val mainScope = MainScope()
    private fun test6_customCoroutineScope() {
        mainScope.launch {
            repeat(10) {
                delay(1000)
                log("mainScope.launch-$it")
            }
        }

        launch {
            repeat(10) {
                delay(1000)
                log("CoroutineScope.launch-$it")
            }
        }
        //这里还可以launch多个协程，在onDestroy中会统一取消

//        2023-08-24 21:17:18.029 15325-15407/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 CoroutineScope.launch-0
//        2023-08-24 21:17:18.090 15325-15325/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main mainScope.launch-0
//        2023-08-24 21:17:19.031 15325-15407/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 CoroutineScope.launch-1
//        2023-08-24 21:17:19.092 15325-15325/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main mainScope.launch-1
//        2023-08-24 21:17:20.034 15325-15407/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 CoroutineScope.launch-2
//        2023-08-24 21:17:20.099 15325-15325/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main mainScope.launch-2
//        2023-08-24 21:17:20.611 15325-15325/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main activity onDestroy
    }

    //已取消的作用域无法再创建协程。因此，仅当控制其生命周期的类被销毁时，才应调用 scope.cancel()
    override fun onDestroy() {
        super.onDestroy()
        //使用作用域的cancel（取代多个协程的cancel）
        mainScope.cancel()
        cancel()
        log("activity onDestroy")
    }


    /**
     * launch
     * context：用于指定协程的上下文
     * start：用于指定协程的启动方式，默认值为 CoroutineStart.DEFAULT，即协程会在声明的同时就立即进入等待调度的状态，即可以立即执行的状态。可以通过将其设置为CoroutineStart.LAZY来实现延迟启动，即懒加载
     * block：用于传递协程的执行体，即希望交由协程执行的任务

     * public fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
    ): Job
     */
    private fun test7_launch() {
        runBlocking {
            val launchA = launch {
                repeat(3) {
                    delay(100)
                    log("launchA-$it")
                }
            }

            val launchB = launch {
                repeat(3) {
                    delay(100)
                    log("launchB-$it")
                }
            }
        }

//        2023-08-25 11:07:54.214 15274-15274/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchA-0
//        2023-08-25 11:07:54.215 15274-15274/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB-0
//        2023-08-25 11:07:54.356 15274-15274/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchA-1
//        2023-08-25 11:07:54.356 15274-15274/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB-1
//        2023-08-25 11:07:54.493 15274-15274/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchA-2
//        2023-08-25 11:07:54.494 15274-15274/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchB-2
    }

    /**
     * job 是协程的句柄。使用 launch 或 async 创建的每个协程都会返回一个 Job 实例，该实例唯一标识协程并管理其生命周期
     */
    private fun test8_job() {
        //将协程设置为延迟启动
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            repeat(100) {
                delay(100)
                log("test8_job:$it")
            }
        }

        job.invokeOnCompletion {
            log("invokeOnCompletion:${it?.message}")
        }

        log("isActive:${job.isActive}")
        log("isCancelled:${job.isCancelled}")
        log("isCompleted:${job.isCompleted}")

        job.start()
        log("isActive:${job.isActive}")
        log("isCancelled:${job.isCancelled}")
        log("isCompleted:${job.isCompleted}")

        //等500ms取消协程
        Thread.sleep(500)
        job.cancel(CancellationException("exception msg"))
        log("isActive:${job.isActive}")
        log("isCancelled:${job.isCancelled}")
        log("isCompleted:${job.isCompleted}")

//        2023-08-25 11:24:25.647 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isActive:false
//        2023-08-25 11:24:25.647 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isCancelled:false
//        2023-08-25 11:24:25.647 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isCompleted:false
//        2023-08-25 11:24:25.655 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isActive:true
//        2023-08-25 11:24:25.655 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isCancelled:false
//        2023-08-25 11:24:25.655 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isCompleted:false
//        2023-08-25 11:24:25.759 16769-16873/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test8_job:0
//        2023-08-25 11:24:25.865 16769-16873/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test8_job:1
//        2023-08-25 11:24:25.968 16769-16873/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test8_job:2
//        2023-08-25 11:24:26.074 16769-16873/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test8_job:3
//        2023-08-25 11:24:26.175 16769-16873/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 test8_job:4
//        2023-08-25 11:24:26.197 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isActive:false
//        2023-08-25 11:24:26.197 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isCancelled:true
//        2023-08-25 11:24:26.197 16769-16769/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main isCompleted:false
//        2023-08-25 11:24:26.197 16769-16873/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 invokeOnCompletion:exception msg
    }

    /**
     * async 也是一个作用于 CoroutineScope 的扩展函数
     * 和 launch 的区别主要就在于：【async 可以返回协程的执行结果，而 launch 不行】
     *
     * 通过await()方法可以拿到 async 协程的执行结果，可以看到两个协程的总耗时是远少于3秒的，总耗时基本等于耗时最长的协程
     *
     * Deferred：async 函数的返回值是一个 Deferred 对象。
     * Deferred 是一个接口类型，继承于 Job 接口，所以 Job 包含的属性和方法 Deferred 都有，其主要是在 Job 的基础上扩展了 await()方法
     */
    private fun test9_async() {
        val timeMillis = measureTimeMillis {
            runBlocking {
                val asyncA = async {
                    delay(1000)
                    "asyncA"
                }

                val asyncB = async {
                    delay(2000)
                    ",asyncB"
                }

                log(asyncA.await() + asyncB.await())
            }
        }

        log("timeMillis:$timeMillis")

//        2023-08-25 11:42:47.734 17846-17846/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main asyncA,asyncB
//        2023-08-25 11:42:47.734 17846-17846/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main timeMillis:2001
    }


    /**
     * async错误用法，start = CoroutineStart.LAZY
     * 可以发现两个协程的总耗时就会变为3秒左右
     *
     * 是因为 CoroutineStart.LAZY 不会主动启动协程，而是直到调用async.await()或者async.satrt()后才会启动（即懒加载模式），
     * 所以asyncA.await() + asyncB.await()会导致两个协程其实是在顺序执行。
     * 而默认值 CoroutineStart.DEFAULT 参数会使得协程在声明的同时就被启动了（实际上还需要等待被调度执行，但可以看做是立即就执行了），
     * 所以此时调用第一个 async.await()时两个协程其实都是处于运行状态，所以总耗时就是四秒左右
     * 此时可以通过先调用start()再调用await()来实现第一个例子的效果     【并且，start()是普通函数，而await()是挂起函数】

     */
    private fun test10_async() {
        val timeMillis = measureTimeMillis {
            runBlocking {
                val asyncA = async(start = CoroutineStart.LAZY) {
                    delay(1000)
                    "asyncA"
                }

                val asyncB = async(start = CoroutineStart.LAZY) {
                    delay(2000)
                    ",asyncB"
                }

                log(asyncA.await() + asyncB.await())//suspend是挂起函数，所以asyncA.await()此处挂起，也就是它完成后才会走到asyncB.await()，所以造成了两个启动是串行的
            }
        }

        log("timeMillis:$timeMillis")

//        2023-08-25 11:49:44.810 18477-18477/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main asyncA,asyncB
//        2023-08-25 11:49:44.810 18477-18477/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main timeMillis:3091
    }


    /**
     * 使用 awaitAll() 等待启动的协程完成
     */
    private fun test11_async() {
        runBlocking {

            val list = listOf(
                async {
                    delay(100)
                    log("asyncA")
                },
                async {
                    delay(200)
                    log("asyncA")
                })

            list.awaitAll() //挂起runBlocking

            log("runBlocking finish")
        }

//        2023-08-25 14:28:56.134 25237-25237/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main asyncA
//        2023-08-25 14:28:56.235 25237-25237/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main asyncA
//        2023-08-25 14:28:56.235 25237-25237/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking finish
    }


    /**
     * CoroutineContext 使用以下元素集定义协程的行为：
     * Job：控制协程的生命周期，协程的Job是归属其上下文（Context）的一部分，可以通过 coroutineContext[Job] 表达式来访问上下文中的Job对象
     */

    val job = Job()
    val context = job + Dispatchers.IO
    val scope = CoroutineScope(context)
    private fun test12_CoroutineContext_Job() {

        runBlocking {
            //kotlin中一个类的名字本身就可以作为其伴生对象的引用。如下Key是Job的半生对象，Job也表示其伴生对象Key
            //public interface Job : CoroutineContext.Element {
            //    public companion object Key : CoroutineContext.Key<Job>
            //即通过Key<Job>来获取CoroutineContext中的元素 Job对象
            log("runBlocking job：${this.coroutineContext[Job]}")

            log("job：$job") //【1】JobImpl{Active}@9554785

            val launchJob = scope.launch {
                log("scope job：${this.coroutineContext[Job]}") //【2】StandaloneCoroutine{Active}@af829da

                try {
                    delay(3000)
                } catch (e: CancellationException) {
                    log("job is cancelled")
                    throw e
                }

                log("end")
            }

            //【1】是原始new的Job实例，【2】是在协程启动后创建的新的job-StandaloneCoroutine，
            log("launchJob job：$launchJob") //【2】StandaloneCoroutine{Active}@af829da

            log("scope.coroutineContext[Job]：${scope.coroutineContext[Job]}") //【1】JobImpl{Active}@9554785
            log("scope.coroutineContext.job：${scope.coroutineContext.job}")   //【1】JobImpl{Active}@9554785

            delay(1000)
            scope.coroutineContext[Job]?.cancel()
//            scope.cancel()
        }

//        2023-08-25 17:32:52.274 2691-2691/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main runBlocking job：BlockingCoroutine{Active}@8e0f4fc
//        2023-08-25 17:32:52.274 2691-2691/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main job：JobImpl{Active}@9554785
//        2023-08-25 17:32:52.282 2691-2691/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main launchJob job：StandaloneCoroutine{Active}@af829da
//        2023-08-25 17:32:52.282 2691-2691/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main scope.coroutineContext[Job]：JobImpl{Active}@9554785
//        2023-08-25 17:32:52.282 2691-2691/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main scope.coroutineContext.job：JobImpl{Active}@9554785
//        2023-08-25 17:32:52.282 2691-2773/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 scope job：StandaloneCoroutine{Active}@af829da
//        2023-08-25 17:32:53.327 2691-2773/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-1 job is cancelled

    }

    /**
     * CoroutineContext 包含一个 CoroutineDispatcher（协程调度器）用于指定执行协程的目标载体，即 运行于哪个线程
     * - Dispatchers.Default。默认调度器，适合用于执行占用大量 CPU 资源的任务。例如：对列表排序和解析 JSON
     * - Dispatchers.IO。适合用于执行磁盘或网络 I/O 的任务。例如：使用 Room 组件、读写磁盘文件，执行网络请求
     * - Dispatchers.Unconfined。对执行协程的线程不做限制，可以直接在当前调度器所在线程上执行
     * - Dispatchers.Main。使用此调度程序可用于在 Android 主线程上运行协程，只能用于与界面交互和执行快速工作，例如：更新 UI、调用 LiveData.setValue
     */
    private fun test13_CoroutineContext_CoroutineDispatcher() {
        runBlocking<Unit> {
            launch {
                log("main runBlocking")
            }
            launch(Dispatchers.Default) {
                log("Default")
                launch(Dispatchers.Unconfined) {
                    log("Unconfined 1")
                }
            }
            launch(Dispatchers.IO) {
                log("IO")
                launch(Dispatchers.Unconfined) {
                    log("Unconfined 2")
                }
            }
            launch(newSingleThreadContext("MyOwnThread")) { //限制在特定线程-MyOwnThread
                log("newSingleThreadContext")
                launch(Dispatchers.Unconfined) {
                    log("Unconfined 4")
                }
            }
            launch(Dispatchers.Unconfined) {
                log("Unconfined 3")
            }
            GlobalScope.launch {
                log("GlobalScope")
            }
        }

//        2023-08-25 18:15:34.869 6130-6307/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-3 Default
//        2023-08-25 18:15:34.869 6130-6307/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-3 Unconfined 1
//        2023-08-25 18:15:34.870 6130-6307/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-3 IO
//        2023-08-25 18:15:34.870 6130-6307/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-3 Unconfined 2
//        2023-08-25 18:15:34.870 6130-6130/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main Unconfined 3
//        2023-08-25 18:15:34.870 6130-6306/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-2 GlobalScope
//        2023-08-25 18:15:34.870 6130-6130/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main main runBlocking
//        2023-08-25 18:15:34.870 6130-6418/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:MyOwnThread newSingleThreadContext
//        2023-08-25 18:15:34.871 6130-6418/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:MyOwnThread Unconfined 4


    }

    /**
     * withContext 是一个非常有用的挂起函数，用于在协程中切换上下文。
     * 通常，使用协程来执行异步任务，例如网络请求或数据库查询，但是在执行异步任务时，我们可能需要更改协程的上下文，例如从 I/O 线程池切换到主线程以更新 UI。
     * withContext 可以让我们在协程中切换上下文而不必手动处理线程切换的逻辑。
     *
     * 与基于回调的等效实现相比，withContext() 不会增加额外的开销。
     * 此外，在某些情况下，还可以优化 withContext() 调用，使其超越基于回调的等效实现。
     * 例如，如果某个函数需要先后调用十次网络请求，你可以在最外层调用 withContext() 让协程只切换一次线程，这样即使每个网络请求内部均会使用 withContext()，它也会留在同一调度程序上，从而避免频率切换线程。
     * 此外，协程还优化了 Dispatchers.Default 与 Dispatchers.IO 之间的切换，以尽可能避免线程切换
     */
    private fun test14_CoroutineContext_withContext() {
        val url = ""
        runBlocking {
            val res = requestHttp(url) //假装一个网络请求，Dispatchers.Main
            log("res: $res") //Dispatchers.Main
        }

//        2023-08-28 20:04:05.846 28732-28818/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:DefaultDispatcher-worker-2 requestHttp ing
//        2023-08-28 20:04:05.848 28732-28732/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main res: success
    }

    /**
     * CoroutineName 用于为协程指定一个名字，方便调试和定位问题
     */
    private fun test15_CoroutineContext_CoroutineName() {
        runBlocking(CoroutineName("runBlocking")) {
            log(this.coroutineContext[CoroutineName].toString())
            launch(CoroutineName("launch")) {
                log(this.coroutineContext[CoroutineName].toString())
            }
        }

//        2023-08-28 20:13:55.363 29697-29697/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main CoroutineName(runBlocking)
//        2023-08-28 20:13:55.364 29697-29697/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main CoroutineName(launch)
    }

    private suspend fun requestHttp(url: String): String {
        return withContext(Dispatchers.IO){ //切换到IO线程执行,Dispatchers.Main
            delay(2000) //Dispatchers.IO
            log("requestHttp ing") //Dispatchers.IO
            "success" //Dispatchers.IO
        }
    }



    /**
     * job.cancel()就用于取消协程，job.join()用于阻塞等待协程运行结束。
     * 因为 cancel() 函数调用后会马上返回而不是等待协程结束后再返回，所以此时协程不一定就是已经停止运行了。
     * 如果需要确保协程结束运行后再执行后续代码，就需要调用 join() 方法来阻塞等待。
     * 也可以通过调用 Job 的扩展函数 cancelAndJoin() 来完成相同操作，它结合了 cancel 和 join两个操作
     */
    private fun test16_cancel() {
        runBlocking {
            val job = launch {
                repeat(1000) { i ->
                    delay(500L)
                    log("job: I'm sleeping $i ...")
                }
            }
            delay(1400L)
            log("main: I'm tired of waiting!")
            job.cancel() //取消协程,调用后会马上返回而不是等待协程结束后再返回，所以此时协程不一定就是已经停止运行了
            job.join() //阻塞等待协程运行结束，可以确保协程结束运行后再执行后续代码
//            job.cancelAndJoin()
            log("main: Now I can quit.")
        }

//        2023-08-28 20:27:47.335 30682-30682/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main job: I'm sleeping 0 ...
//        2023-08-28 20:27:47.876 30682-30682/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main job: I'm sleeping 1 ...
//        2023-08-28 20:27:48.234 30682-30682/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main main: I'm tired of waiting!
//        2023-08-28 20:27:48.236 30682-30682/com.hfy.androidlearning I/hfyKotlinTestActivity: threadName:main main: Now I can quit.
    }




    private fun log(string: String?) {
        Log.i(TAG, "threadName:${Thread.currentThread().name} $string")
    }

}