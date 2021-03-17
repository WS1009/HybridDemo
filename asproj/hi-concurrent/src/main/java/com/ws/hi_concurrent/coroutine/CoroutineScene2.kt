package com.ws.hi_concurrent.coroutine

import kotlinx.coroutines.delay

/**
 *  Created by wangshun3 on 2021/3/12
 *  Des :
 */

object CoroutineScene2 {
    suspend fun request2(): String {
        delay(2 * 1000)
        return "result from request2"
    }
}