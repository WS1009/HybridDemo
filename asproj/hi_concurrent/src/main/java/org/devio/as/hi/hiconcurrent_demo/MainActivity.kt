package org.devio.`as`.hi.hiconcurrent_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        com.ws.hi_concurrent.ConcurrentTest.main(this)
    }
}