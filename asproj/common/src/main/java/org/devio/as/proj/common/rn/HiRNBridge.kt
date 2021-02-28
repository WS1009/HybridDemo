package org.devio.`as`.proj.common.rn

import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.react.bridge.*
import org.devio.`as`.proj.common.core.IHiBridge

//本质是一个NativeModule,用户JS侧向Native侧传递数据
class HiRNBridge(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext),
    IHiBridge<ReadableMap, Promise> {
    override fun getName(): String {
        return "HiRNBridge"
    }

    @ReactMethod
    override fun onBack(p: ReadableMap?) {
        currentActivity?.run {
            runOnUiThread {
                onBackPressed()
            }
        }
    }

    //RN跳转到Native的相关页面
    @ReactMethod
    override fun goToNative(p: ReadableMap) {
        ARouter.getInstance().build("/detail/main").withString(
            "goodsId",
            p.getString("goodsId")
        ).navigation()
    }

    //有返回值
    @ReactMethod
    override fun getHeaderParams(callback: Promise) {
        val params: WritableMap = Arguments.createMap()
        params.putString("boarding-pass","boarding-pass")
        params.putString("auth-token","auth-token")
        callback.resolve(params)
    }

}