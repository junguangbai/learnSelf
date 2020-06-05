package com.learn.self;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 资源缓存类工具
 * @author baijunguang
 * @date 2020/6/4-21:05
 */
class Cache{
    //缓存对象
    private volatile Map<String,Object> myCache = new HashMap<>();
    //读写锁
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    //添加缓存内容
    public void put(String key,Object value){
        readWriteLock.writeLock().lock();
        try {

            System.out.println(Thread.currentThread().getName()+"\t 正在写入  O(∩_∩)O哈哈~");
         //   try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e){ e.printStackTrace(); }
            myCache.put(key,value);
            System.out.println(Thread.currentThread().getName()+"\t 写入完成 ");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    //读取缓存
    public void get(String key){
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+"\t 读取缓存key:"+key);
            //try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e){ e.printStackTrace(); }
            Object obj = myCache.get(key);
            System.out.println(Thread.currentThread().getName()+"\t 读取完成"+obj);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    //清空缓存
    public void clearCache(){
        myCache.clear();
    }
}

public class MyCache {

    public static void main(String[] args) {

        Cache cache = new Cache();

        for (int i = 1; i <= 5; i++) {
            final int num = i;
            new Thread(()->{
                cache.put(num+"", UUID.randomUUID());
            },String.valueOf(i)).start();
        }

        //产生一个疑问线程优先获取共享锁是什么原因？？
       //try { TimeUnit.MILLISECONDS.sleep(1150); } catch (InterruptedException e){ e.printStackTrace(); }

        for (int i = 1; i <= 5; i++) {
            final int num = i;
            new Thread(()->{
                cache.get(num+"");
            },String.valueOf(i)).start();
        }

    }
}
