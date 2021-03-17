package com.ws.hi_concurrent.coroutine

import android.util.Log
import kotlinx.coroutines.*

/**
 *  Created by wangshun3 on 2021/3/5
 *  Des :
 *  https://www.kotlincn.net/docs/reference/coroutines/basics.html
 *  https://blog.csdn.net/u011133887/article/details/98617852
 */
object RunBlockingDemo {

    @JvmStatic
    fun main(args: Array<String>) {
//        main()
//        main2()
//        main3()
//        main4()

        println("main static fun .")
    }

    //runBlocking 方法会阻塞当前线程来等待
    private fun main() = runBlocking { // this: CoroutineScope
        launch {// 在 runBlocking 作用域中启动一个新协程
            delay(2000L)
            println("Task from runBlocking  ")
        }

        println("Coroutine scope is middle ")

        //coroutineScope 只是挂起，会释放底层线程用于其他用途
        //创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束。
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(5000L)
                println("Task from nested launch ")
            }

            delay(1000L)
            println("Task from coroutine scope ") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over ") // 这一行在内嵌 launch 执行完毕后才输出
    }

    private fun main2() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
    }

    //提取函数重构
    private fun main3() = runBlocking {
        launch {
            doWorld()
        }
        println("Hello,")
    }

    // 这是你的第一个挂起函数
    private suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    private fun main4() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
        }
        println("Hello,aa") // 主线程中的代码会立即执行
    }
}