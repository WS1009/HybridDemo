package org.devio.`as`.proj.common.rn

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import org.devio.`as`.proj.common.BuildConfig


//ReactNative混合开发指导文档：https://www.devio.org/2020/04/19/React-Native-Hybrid-Android/

//打本地 rn bundle包
// react-native bundle --platform android --dev false --entry-file index.js --bundle-output ../asproj/app/src/main/assets/index.android.bundle --assets-dest ../asproj/app/src/main/res/

@Route(path = "/rn/main")
class HiRNActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRN()
        setContentView(mReactRootView)
    }

    private fun initRN() {
        val routeTo = intent.getStringExtra(HI_RN_BUNDLE)
        val params = Bundle()
        params.putString("routeTo", routeTo)

        mReactRootView = ReactRootView(this)
        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackage(MainReactPackage())
            .addPackage(HiReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()
        // 这个"rn_module"名字一定要和我们在index.js中注册的名字保持一致AppRegistry.registerComponent()
        mReactRootView?.startReactApplication(mReactInstanceManager, "rn_module", params)
    }

    override fun onPause() {
        super.onPause()
        mReactInstanceManager?.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mReactInstanceManager?.onHostDestroy(this)
        mReactRootView?.unmountReactApplication()
    }

    override fun onBackPressed() {
        if (mReactRootView != null) {
            mReactInstanceManager?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        mReactInstanceManager?.onHostResume(this, this)
    }

    //如果RN消费了返回事件就不会回调此方法
    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager!!.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    companion object {
        const val HI_RN_BUNDLE = "HI_RN_BUNDLE"
    }

}