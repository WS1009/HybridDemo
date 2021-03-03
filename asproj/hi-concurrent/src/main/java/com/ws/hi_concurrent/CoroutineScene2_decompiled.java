package com.ws.hi_concurrent;

import android.util.Log;

import androidx.core.graphics.TypefaceCompatApi26Impl;

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
public class CoroutineScene2_decompiled {

    private static final String TAG = CoroutineScene2_decompiled.class.getSimpleName();

    public static final Object request2(Continuation preCallback) {

        ContinuationImpl request2Callback;

        if (!(preCallback instanceof ContinuationImpl) || (((ContinuationImpl) preCallback).label & Integer.MIN_VALUE) == 0) {
            request2Callback = new ContinuationImpl(preCallback) {
                @Override
                Object invokeSuspend(@Nullable Object resumeResult) {
                    this.result = resumeResult;
                    this.label |= Integer.MIN_VALUE;
                    return request2(this);
                }
            };
        } else {
            request2Callback = (ContinuationImpl) preCallback;
        }

        switch (request2Callback.label) {
            case 0: {
                Object delay = DelayKt.delay(2000, request2Callback);
                if (delay == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
            }
        }
        Log.e(TAG, "request2 completed");
        return "result from request2";
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
            Object suspend = invokeSuspend(resumeResult);
            if (suspend == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            preCallback.resumeWith(suspend);
        }

        abstract Object invokeSuspend(@Nullable Object resumeResult);

    }


}
