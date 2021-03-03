package com.ws.hi_concurrent

import android.util.Log
import com.ws.hi_concurrent.ConcurrentTest.TAG
import kotlinx.coroutines.*


//https://doc.devio.org/as/book/docs/Part2/%E7%BA%BF%E7%A8%8B%E4%B8%8E%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%BC%80%E5%8F%91%E6%A0%B8%E5%BF%83%E6%8A%80%E6%9C%AF/5.kotlin_coroutine.html
object CoroutineScene {

    //---------------------------并发流程控制---------------------------

    /**
     * 一次启动三个子线程，屏气同步的方式拿到他们的返回值，并且更新UI
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

    //启动一个协程，限制性request1,然后同时运行request2和reques3
    fun startScene2() {
        GlobalScope.launch(Dispatchers.Main) {
            val result1 = request1()
            val deferred2 = GlobalScope.async {
                request2(result1)
            }

            val deferred3 = GlobalScope.async {
                request3(result1)
            }

            updateUI(deferred2.await(), deferred3.await())
        }
    }

    private fun updateUI(result2: String, result3: String) {
        Log.e(TAG, "update ui work on ${Thread.currentThread().name}")
        Log.e(TAG, "param: $result2 + $result3")
    }
}