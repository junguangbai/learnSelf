package com.thread.self;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author baijunguang
 * @date 2020/6/5-15:58
 * 创建线程的第三种方法
 */
public class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {

        return null;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //未来的任务，异步操作，并发下避免阻塞，在完成任务后用get()方法获得结果
        FutureTask<Integer> task = new FutureTask<Integer>(new MyThread());

           //注意同一个task任务对象开启线程任务后call方法只会执行一次
            new Thread(task).start();
            new Thread(task).start();


        //该方法是阻塞的方法，放在最后使用，获得异步完成任务的结果
        Integer integer = task.get();
    }
}
