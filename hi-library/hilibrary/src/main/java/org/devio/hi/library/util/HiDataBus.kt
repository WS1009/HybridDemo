package org.devio.hi.library.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

/**
 * LiveData消息总线
 */
object HiDataBus {

    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()

    fun <T> with(eventName: String): StickyLiveData<T> {
        //基于事件名称 订阅 分发消息
        //由于一个LiveData只能分发一种数据类型
        //所有不同的event事件，需要使用不同的LiveData实例去分发

        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as StickyLiveData<T>

    }

    //通过一堆的反射获取LiveData当中的mVersion字段，来控制粘性数据的分发与否，但是我们认为反射不够优雅。
    class StickyLiveData<T>(private val eventName: String) : LiveData<T>() {
        var mStickyData: T? = null
        var mVersion = 0

        fun setStickyData(stickyData: T) {
            mStickyData = stickyData
            setValue(stickyData)
            //就是在主线程去发送数据
        }

        fun postStickyData(stickyData: T) {
            mStickyData = stickyData
            postValue(stickyData)
            // 不受线程限制
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observerSticky(owner, false, observer)
        }

        fun observerSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            super.observe(owner, StickyObserver(this, sticky, observer))

            //允许制定注册的观察者是否关心粘性事件
            //sticky=true ，如果之前存在已经发送的数据，那么这个observer会收到之前的粘性事件消息
            owner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                //监听宿主发生销毁事件，主动把LiveData移除掉
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventMap.remove(eventName)
                }
            })
        }
    }

    class StickyObserver<T>(
        private val stickyLiveData: HiDataBus.StickyLiveData<T>,
        private val sticky: Boolean,
        val observer: Observer<in T>
    ) : Observer<T> {
        //lastVersion和LiveData的version对齐的原因，就是为了控制粘性事件的分发
        //sticky！=true 只能接受到注册之后发送的消息，如果要接受粘性事件，则sticky需要传递true
        private var lastVersion = stickyLiveData.mVersion

        override fun onChanged(t: T) {
            if (lastVersion >= stickyLiveData.mVersion) {
                //说明stickyLiveData没有更新的数据需要发送
                if (sticky && stickyLiveData.mStickyData != null) {
                    observer.onChanged(stickyLiveData.mStickyData)
                }
                return
            }
            lastVersion = stickyLiveData.mVersion
            observer.onChanged(t)
        }
    }
}