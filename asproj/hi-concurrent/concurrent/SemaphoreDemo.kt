package com.ws.hi_concurrent.concurrent

import java.util.*
import java.util.concurrent.Semaphore

/**
 * Created by wangshun3 on 2021/3/5
 * Des :
 * 限流 Demo
 *
 * https://doc.devio.org/as/book/docs/Part2/线程与线程池开发核心技术/7.threadpool_optimization.html#countdownlatch
 */
object SemaphoreDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        semaphoreFun()
    }

    private fun semaphoreFun() {
        val semaphore = Semaphore(3)
        for (i in 0..9) {
            val finalIndex = i + 1
            Thread(
                Runnable {
                    //获取许可证
                    try {
                        semaphore.acquire()
                        Thread.sleep(Random().nextInt(5000).toLong())
                        //模拟随机执行时长
                        println("working thread No:$finalIndex")
                        //释放许可证
                        semaphore.release()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            ).start()
        }
    }
}