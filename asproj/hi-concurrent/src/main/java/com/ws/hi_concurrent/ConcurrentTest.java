package com.ws.hi_concurrent;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class ConcurrentTest {

    public static final String TAG = ConcurrentTest.class.getSimpleName();
    private static final int MSG_WHAT_1 = 1;

    public static void main(Context context) {
        class MyAsyncTask extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {
                for (int i = 0; i < 10; i++) {
                    publishProgress(i * 10);
                }
                return params[0];
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                Log.e(TAG, "onProgressUpdate: " + values[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e(TAG, "onPostExecute: " + result);
            }
        }

        //适用于指导任务执行进度并更新UI的场景
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("execute muasynctask");


        //以这种方式提交的任务，所有任务串行执行，既先来后到，但是如果其中有一条任务休眠了，或者执行时间过长，后面的任务都将被阻塞
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //....
            }
        });


        //适用于并发任务的执行
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                //....
            }
        });


        //适用于主线程需要和子线程通信的场景
        //应用于持续性任务，轮训
        HandlerThread handlerThread = new HandlerThread("hander-thread");
        handlerThread.start();

        MyHandler myHandler = new MyHandler(handlerThread.getLooper());
        myHandler.sendEmptyMessage(MSG_WHAT_1);

    }

    static class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handleMessage: " + msg.what);
            Log.e(TAG, "handleMessage: " + Thread.currentThread().getName());
        }
    }

}
