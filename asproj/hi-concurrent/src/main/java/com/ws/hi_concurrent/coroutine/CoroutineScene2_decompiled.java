package com.ws.hi_concurrent.coroutine;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

/**
 * 协程的逆向还原
 * <p>
 * suspend fun request2(): String {
 * delay(2 * 1000)
 * Log.e(TAG, "request2 completed")
 * return "result from request2"
 * }
 */

//https://mp.weixin.qq.com/s?__biz=MzUyMzk0NTk4OQ==&mid=2247484884&idx=1&sn=17e0033eee4e481ec0d3e4c9bd7d56ee&scene=21#wechat_redirect
public class CoroutineScene2_decompiled {

    private static final String TAG = CoroutineScene2_decompiled.class.getSimpleName();

    public static final Object request2(Continuation preCallback) {

        ContinuationImpl request2Callback;

        if (!(preCallback instanceof ContinuationImpl) || (((ContinuationImpl) preCallback).label & Integer.MIN_VALUE) == 0) {//1
            request2Callback = new ContinuationImpl(preCallback) {
                @Override
                Object invokeSuspend(@Nullable Object resumeResult) {//6
                    this.result = resumeResult;
                    this.label |= Integer.MIN_VALUE;
                    return request2(this);
                }
            };
        } else {
            request2Callback = (ContinuationImpl) preCallback;//7
        }

        switch (request2Callback.label) {
            case 0: {//2
                // delay 不会暂停线程，但是会暂停当前的协程,暂停协程时返回 CoroutineSingletons.COROUTINE_SUSPENDED
                Object delay = DelayKt.delay(2000, request2Callback);
                if (delay == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {//3
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
            }
        }
        Log.e(TAG, "request2 completed");
        return "result from request2";//8
    }

    static abstract class ContinuationImpl<T> implements Continuation<T> {

        private Continuation preCallback;

        int label;
        Object result;

        public ContinuationImpl(Continuation preCallback) {
            this.preCallback = preCallback;
        }

        @NotNull
        @Override
        public CoroutineContext getContext() {
            return preCallback.getContext();
        }

        @Override
        public void resumeWith(@NotNull Object resumeResult) {
            Object suspend = invokeSuspend(resumeResult);//5 9
            if (suspend == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            preCallback.resumeWith(suspend);//10
        }

        abstract Object invokeSuspend(@Nullable Object resumeResult);

    }


}
