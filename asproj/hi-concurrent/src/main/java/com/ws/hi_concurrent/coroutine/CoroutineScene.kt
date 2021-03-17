package com.ws.hi_concurrent.coroutine

import android.util.Log
import com.ws.hi_concurrent.concurrent.ConcurrentTest.TAG
import kotlinx.coroutines.*


//https://doc.devio.org/as/book/docs/Part2/线程与线程池开发核心技术/5.kotlin_coroutine.html
//https://www.kotlincn.net/docs/reference/coroutines/basics.html
object CoroutineScene {

    //---------------------------并发流程控制---------------------------

    /**
     * 一次启动三个子线程，采用同步的方式拿到他们的返回值，并且更新UI
     */
    fun startScene1() {
        GlobalScope.launch(Dispatchers.Main) {
            Log.e(TAG, "coroutine is running")

            val request1 = request1()
            val request2 = request2(request1)
            val request3 = request3(request2)

            updateUI(request3)
        }

        Log.e(TAG, "coroutine has launched")

    }

    private fun updateUI(result3: String) {
        Log.e(TAG, "update ui work on ${Thread.currentThread().name}")
        Log.e(TAG, "param: $result3")
    }

    //suspend关键字的作用
    private suspend fun request1(): String {
        delay(2 * 1000)//不会暂停线程，但是会暂停当前的协程
        Log.e(TAG, "request1 work on ${Thread.currentThread().name}")
        return "return from request1"
    }

    private suspend fun request2(result: String): String {
        delay(2 * 1000)
        Log.e(TAG, "request2 work on ${Thread.currentThread().name}")
        return "return from request2"
    }

    private suspend fun request3(result: String): String {
        delay(2 * 1000)
        Log.e(TAG, "request3 work on ${Thread.currentThread().name}")
        return "return from request3"
    }


    //---------------------------并发流程控制---------------------------

    //启动一个协程，先运行request1,然后同时运行request2和reques3
    fun startScene2() {
        GlobalScope.launch(Dispatchers.Main) {
            val result1 = request1()

            val deferred2 = GlobalScope.async {
                request2(result1)
            }

            val deferred3 = GlobalScope.async {
                request3(result1)
            }

            //await必须同时调用，不能依次调用
            //deferred2.await()
            //deferred3.await()

            updateUI(deferred2.await(), deferred3.await())
        }
    }

    private fun updateUI(result2: String, result3: String) {
        Log.e(TAG, "update ui work on ${Thread.currentThread().name}")
        Log.e(TAG, "param: $result2 + $result3")
    }

    fun funWithContext() {
        // 主线程内启动一个协程
        GlobalScope.launch(Dispatchers.Main) {
            // 切换到IO线程，withContext串行调用
            withContext(Dispatchers.IO) {
                delay(1000)
                Log.d("RunBlockingDemo", "processIO in ${Thread.currentThread().name}")
            }
            // 自动切回主线程
            Log.d("RunBlockingDemo", "processUI in ${Thread.currentThread().name}")
        }
    }

}