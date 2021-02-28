package org.devio.`as`.proj.common.rn.view

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

}