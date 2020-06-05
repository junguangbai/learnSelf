package com.thread.self;

import java.util.concurrent.*;

/**
 * @author baijunguang
 * @date 2020/6/5-17:33
 */
public class MyThreadPool {

    private static int availableProcessors;
    private static int corePoolSize;
    private static int maxPoolSize;

    /**
     * 正常生产中根据CPU密集型还是IO密集型进行设置最大线程数
     * 一般是读写操作比较多的话（CRUD）就采用IO密集型
     * 下面根据IO密集型配置的核心线程数，分两种情况
     * @1.并不是一直处于IO密集操作就设置成CPU核心数*2
     * @2.如果大量线程处于阻塞状态就根据公式：CPU核心数/(1-i),0.8<=i<=0.9,自己适配
     */
    static {
        availableProcessors = Runtime.getRuntime().availableProcessors();
        double tempMax = availableProcessors / (1 - 0.9);
        maxPoolSize = new Double(tempMax).intValue();
        int intCore = new Double(tempMax / 4).intValue();
        corePoolSize = intCore < 2 ? 2 : intCore;
        System.out.println("corePoolSize="+corePoolSize+"  maxPoolSize="+maxPoolSize);
    }

    public static void main(String[] args) {

        //创建线程池必须重写不可以用Executors工具创建一个线程池，因为默认的队列是无界的，会导致触发OOM异常
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                corePoolSize,       //核心线程数
                maxPoolSize,         //最大工作线程数
                1,     //结合时间单位，非核心线程保持活跃时间，
                                     // 如果核心线程处理任务都绰绰有余，满足了这个时间要求后，非核心线程就会退出
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),  //等待任务的阻塞队列
                Executors.defaultThreadFactory(),              //默认的线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy());//CallerRunsPolicy：谁调用的任务就会退给谁，这里默认是main线程


        try {

            for (int i = 0; i < 100; i++) {
                threadPool.execute(()->{System.out.println(Thread.currentThread().getName()+"  \t 执行任务"); });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
