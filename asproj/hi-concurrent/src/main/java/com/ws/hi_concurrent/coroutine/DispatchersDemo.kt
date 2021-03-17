package com.ws.hi_concurrent.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

/**
 *  Created by wangshun3 on 2021/3/5
 *  Des :
 */
object DispatchersDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        dispatchersFun()
    }

    private fun dispatchersFun() = runBlocking<Unit> {
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }

        println("dispatchersFun            : I'm working in thread ${Thread.currentThread().name}")

        launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // 将会获取默认调度器
            println("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }
}