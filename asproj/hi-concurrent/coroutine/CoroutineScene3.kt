package com.ws.hi_concurrent.coroutine

import android.content.res.AssetManager
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 让普通方法适配协程，成为真正的挂起函数。
 * 让调用方以同步的方式拿到异步任务的返回结果
 */
object CoroutineScene3 {
    suspend fun parseAssetsFile(assetManager: AssetManager, fileName: String): String {
        return suspendCancellableCoroutine { continuation ->

            Thread(
                Runnable {
                    val inputStream = assetManager.open(fileName)
                    val br = BufferedReader(InputStreamReader(inputStream))
                    var line: String?
                    var stringBuilder = StringBuilder()

                    //kotlin不支持此写法
//                while ((line = br.readLine()) != null) {
//
//                }

                    do {
                        line = br.readLine()
                        if (line != null) stringBuilder.append(line) else break
                    } while (true)

                    inputStream.close()
                    br.close()

//                Thread.sleep(2000)

                    continuation.resumeWith(Result.success(stringBuilder.toString()))
                }
            ).start()
        }
    }
}