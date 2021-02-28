package org.devio.`as`.proj.common.rn

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.DeviceEventManagerModule
import org.devio.hi.library.util.HiStatusBar


//ReactNative混合开发指导文档：https://www.devio.org/2020/04/19/React-Native-Hybrid-Android/

//打本地 rn bundle包
// react-native bundle --platform android --dev false --entry-file index.js --bundle-output ../asproj/app/src/main/assets/index.android.bundle --assets-dest ../asproj/app/src/main/res/

@Route(path = "/rn/main")
class HiRNActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //预加载后，RN对StatusBar调用失效
        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, translucent = true)
        super.onCreate(savedInstanceState)

        initRN()
        fireEvent()
        setContentView(mReactRootView)
    }

    private fun fireEvent() {
        val event: WritableMap = WritableNativeMap()
        event.putString("method", "onStart")
//        mReactInstanceManager!!.currentReactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
//            ?.emit("HI_RN_EVENT",event)
        mReactRootView?.fireEvent("HI_RN_EVENT", event)
    }

    private fun initRN() {
        val routeTo = intent.getStringExtra(HI_RN_BUNDLE)
        val params = Bundle()
        params.putString("routeTo", routeTo)
        mReactRootView =
            HiRNCacheManager.instance?.getCachedReactRootView(this, routeTo, params)
        mReactInstanceManager = mReactRootView!!.reactInstanceManager
    }

    override fun onPause() {
        super.onPause()
        mReactInstanceManager?.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mReactInstanceManager?.onHostDestroy(this)

        //为了提高下次打开的体验，不去detachRootView,防止第二次获取RN缓存时不出现白页
        //mReactRootView?.unmountReactApplication()

//        HiRNCacheManager.instance?.destroy(HiRNCacheManager.MODULE_NAME)
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
        const val HI_RN_BUNDLE = "rn_module"
    }

}