package com.learn.self;

import java.util.concurrent.TimeUnit;

/**
 * @author baijunguang
 * @date 2020/6/5-20:46
 * 线程A 拥有A的锁尝试获取B的锁
 * 线程B 拥有B的锁尝试获取A的锁
 * jps -l windows端常看当前有哪些进程在运行，找到进程号
 * jstack 进程号  java跟踪栈信息
 */
public class DeadLock implements Runnable {

    String lockA;
    String lockB;

    public DeadLock(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {

        synchronized (lockA){
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"\t come in,拥有"+lockA+"的锁,尝试获得锁："+lockB);
            synchronized (lockB){

            }
        }
    }

}

class MyDeadLock{

    public static void main(String[] args) {

        new Thread(new DeadLock("lockA","lockB"),"AAA").start();
        new Thread(new DeadLock("lockB","lockA"),"BBB").start();
    }
}