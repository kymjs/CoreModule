package com.kymjs.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 线程切换工具类
 * Created by ZhangTao on 9/1/16.
 */
public class ThreadSwitch extends Thread {

    private final BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingQueue<>(8);
    private Handler handler = new Handler(Looper.getMainLooper());

    private static class Holder {
        private static final ThreadSwitch INSTANCE = new ThreadSwitch();
    }

    public static ThreadSwitch get() {
        return Holder.INSTANCE;
    }

////////////////////////////////////////////////////////////////////////////////////

    public interface Function extends Runnable {
    }

    public interface IO extends Runnable {
    }

    private ThreadSwitch() {
        this.start();
    }

    public ThreadSwitch io(final IO func) {
        mPoolWorkQueue.add(func);
        return get();
    }

    public ThreadSwitch ui(final Function func) {
        mPoolWorkQueue.add(func);
        return get();
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                final Runnable task = mPoolWorkQueue.take();
                if (task != null) {
                    if (task instanceof IO) {
                        task.run();
                    } else {
                        handler.post(task);
                    }
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
