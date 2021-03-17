package com.ws.hi_concurrent.concurrent

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

/**
 * Created by wangshun3 on 2021/3/5
 * Des :
 *
 * 等待所有人准备好了发车
 */
object CountDownLatchDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        countDownLatchFun()
    }

    private fun countDownLatchFun() {
        val latch = CountDownLatch(5)
        val service =
            Executors.newFixedThreadPool(5)
        for (i in 0..4) {
            val no = i + 1
            val runnable = Runnable {
                try {
                    Thread.sleep((Math.random() * 10000).toLong())
                    println("No." + no + "准备好了。")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    latch.countDown()
                }
            }
            service.submit(runnable)
        }
        println("等待所有人准备完毕.....")
        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        println("所有人都准备好了，可以发车了")
    }
}