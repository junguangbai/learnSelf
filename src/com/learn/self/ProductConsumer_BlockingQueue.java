package com.learn.self;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author baijunguang
 * @date 2020/6/5-13:35
 * volatile/CAS/AtomicInteger/BlockingQueue/线程交互/原子引用
 */
class ShareData{
    //标志位@1.可见性@2.不保证原子性@3.禁止指令重排序
    private volatile Boolean FLAG = true;
    //设置队列存放数据，中的offer()和poll()方法用的是ReentrantLock实现了线程间的互相通信
    private BlockingQueue<String> blockingQueue = null; //泛型没有加，如果不是存放整形数据可以使用原子引用类型
    //创建原子整形，底层实现CAS思想
    private AtomicInteger atomicInteger = new AtomicInteger();  //也就是这里用AtomicReference
    //构造函数确定具体类型
    public ShareData(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        //输出具体的类型
        System.out.println("BlockingQueue具体的类型："+blockingQueue.getClass());
    }

    //生产方法,无需判断干活通知，全部在队列中实现
    public void prod() throws InterruptedException {
        boolean retValue;
        while(FLAG){
            retValue = blockingQueue.offer(atomicInteger.incrementAndGet() + "", 2, TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName()+"\t 插入队列成功  O(∩_∩)O哈哈~");
            }else{
                System.out.println(Thread.currentThread().getName()+"\t 插入队列失败 o(╥﹏╥)o");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t 大boss叫停。。。。");
    }

    //消费方法
    public void consumer() throws InterruptedException {
        String poll = null;
        while(FLAG){
            poll = blockingQueue.poll(2, TimeUnit.SECONDS);
            if(poll==null||"".equalsIgnoreCase(poll)){
               System.out.println(Thread.currentThread().getName()+"\t 超过两秒钟没有取出数据，我要退出了");System.out.println();
               System.out.println();
               System.out.println();
               return;
            }
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println(Thread.currentThread().getName()+"\t 消费蛋糕成功result= "+poll);
        }
    }
    //总控开关
    public void stop(){
        FLAG=false;
    }
}

public class ProductConsumer_BlockingQueue {


    public static void main(String[] args) {
    ShareData shareData = new ShareData(new ArrayBlockingQueue<String>(10));

            new Thread(()->{
                try {
                    shareData.prod();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Prod").start();

            new Thread(()->{
                try {
                    shareData.consumer();
                    System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Consumer").start();

            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e){ e.printStackTrace(); }
               System.out.println();
               System.out.println();
               System.out.println();

            shareData.stop();
    }
}
