package org.devio.`as`.proj.debug

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_playground.*
import org.devio.`as`.proj.common.rn.HiRNActivity

@Route(path="/debug/playground")
class PlaygroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

        btn_rn.setOnClickListener{
            ARouter.getInstance().build("/rn/main").withString(HiRNActivity.HI_RN_BUNDLE,"/bridgeDemo").navigation()
        }
    }
}