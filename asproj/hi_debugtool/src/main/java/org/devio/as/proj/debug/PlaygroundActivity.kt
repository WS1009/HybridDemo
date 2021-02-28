package org.devio.`as`.proj.debug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_playground.*
import org.devio.`as`.proj.common.rn.HiRNActivity
import org.devio.`as`.proj.common.rn.HiRNCacheManager

@Route(path = "/debug/playground")
class PlaygroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)


        //与浏览历史模块对比效果，MODULE_NAME_BRIDGE_DEMO模块未预加载
        btn_rn.setOnClickListener {
            ARouter.getInstance().build("/rn/main")
                .withString(HiRNActivity.HI_RN_BUNDLE, HiRNCacheManager.MODULE_NAME_BRIDGE_DEMO)
                .navigation()
        }
    }
}