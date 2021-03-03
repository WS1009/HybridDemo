package com.ws.hi_concurrent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.Continuation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ConcurrentTest.main(this)

        CoroutineScene.startScene1()

        CoroutineScene.startScene2()

        val callback = Continuation<String>(Dispatchers.Main) { result ->
            Log.e("MainActivity", result.getOrNull())
        }
        CoroutineScene2_decompiled.request2(callback)

    }
}
