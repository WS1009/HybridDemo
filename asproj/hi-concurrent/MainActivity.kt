package com.ws.hi_concurrent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.lifecycle.whenResumed
import androidx.lifecycle.whenStarted
import com.ws.hi_concurrent.concurrent.ConcurrentTest
import com.ws.hi_concurrent.coroutine.CoroutineScene
import com.ws.hi_concurrent.coroutine.CoroutineScene2_decompiled
import com.ws.hi_concurrent.coroutine.CoroutineScene3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ConcurrentTest.main(this)

        CoroutineScene.startScene1()

        CoroutineScene.startScene2()

        CoroutineScene.funWithContext()

        val callback = Continuation<String>(Dispatchers.Main) { result ->
            Log.e("MainActivity", result.getOrNull())//11
        }
        CoroutineScene2_decompiled.request2(callback)

        lifecycleScope.launch {//4
            val content = CoroutineScene3.parseAssetsFile(assets, "config.json")
            Log.e("MainActivity", content)
        }

        //但我们的宿主的生命周期至少是onCreate的时候才会启动
        lifecycleScope.launchWhenCreated {
            whenCreated {
                //这里的代码只有宿主的生命周期为onCreate的时候才会执行，否则都是暂停的
            }

            whenResumed {

            }

            whenStarted {

            }
        }

    }
}