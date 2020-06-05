package com.learn.self;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {

    //原子引用线程，参数为空 Creates a new AtomicReference with null initial value.
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName()+"\t come in myLock O(∩_∩)O哈哈~");
        //如果是null就把当前线程设置成内部属性的value值
        while (!atomicReference.compareAndSet(null, thread)) {
            System.out.println(Thread.currentThread().getName()+"\t 等待"+atomicReference.get().getName()+"释放锁");
        }
    }


    public void myUnLock(){
        Thread thread = Thread.currentThread();
        //如果是当前线程就把null设置成内部属性的value值
        atomicReference.compareAndSet(thread,null);
        System.out.println(Thread.currentThread().getName()+"\t 释放了锁");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();


        new Thread(()->{
            spinLockDemo.myLock();
            try { TimeUnit.MILLISECONDS.sleep(1001); } catch (InterruptedException e){ e.printStackTrace(); }
            spinLockDemo.myUnLock();
        },"AA").start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()->{
            spinLockDemo.myLock();
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }
            spinLockDemo.myUnLock();
        },"BB").start();
    }
}