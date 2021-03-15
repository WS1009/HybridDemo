package org.devio.hi.library.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

object HiDataBus {

    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()

    fun <T> with(eventName: String): StickyLiveData<T> {
        //基于事件名称 订阅 分发消息
        //由于一个livedata只能分发一种数据类型
        //所有不同的event事件，需要使用不同的livedata实例去分发

        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as StickyLiveData<T>

    }

    //通过一堆的反射获取livedata当中的mversion字段，来控制粘性数据的分发与否，但是我们认为反射不够优雅。
    class StickyLiveData<T>(private val eventName: String) : LiveData<T>() {
        var mStickData: T? = null
        var mVersion = 0

        fun setStickData(stickyData: T) {
            mStickData = stickyData
            setValue(stickyData)
            //就是在主线程去发送数据
        }

        fun postStickData(stickData: T) {
            mStickData = stickData
            postValue(stickData)
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
            //允许制定注册的观察者是否关心粘性事件
            //sticky=true ，如果之前存在已经发送的数据，那么这个observer会收到之前的粘性事件消息

            owner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventMap.remove(eventName)
                }
            })
            super.observe(owner, StickyObserver(this, sticky, observer))
        }
    }

    class StickyObserver<T>(
        val stickyLiveData: HiDataBus.StickyLiveData<T>,
        val sticky: Boolean,
        val observer: Observer<in T>
    ) : Observer<T> {
        //lastVersion和livedata的version对齐的原因，就是为了控制粘性事件的分发
        private var lastVersion = stickyLiveData.mVersion

        override fun onChanged(t: T) {
            if (lastVersion >= stickyLiveData.mVersion) {
                //说明stickylivedata没有要新的数据需要发送
                if (sticky && stickyLiveData.mStickData != null) {
                    observer.onChanged(stickyLiveData.mStickData)
                }
                return
            }
            lastVersion = stickyLiveData.mVersion
            observer.onChanged(t)
        }

    }

}


