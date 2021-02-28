package org.devio.`as`.proj.common.rn.view

import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class HiRNImageViewManager : SimpleViewManager<HiRNImageView>() {
    override fun createViewInstance(reactContext: ThemedReactContext): HiRNImageView {
        return HiRNImageView(context = reactContext)
    }

    override fun getName(): String {
        return "HiRNImageView"
    }

    @ReactProp(name = "src")
    fun setUrl(view: HiRNImageView, source: String?) {
        source?.let {
            view.setUrl(url = it)
        }
    }

    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
        //将native的事件名 onNativeClick 映射给js端 onJSClick
        return MapBuilder.builder<String, Any>().put(
            "onNativeClick", MapBuilder.of(
                "phasedRegistrationNames",//固定值
                MapBuilder.of(
                    "bubbled",//固定值
                    "onJSClick"
                )
            )
        ).build()
    }

}