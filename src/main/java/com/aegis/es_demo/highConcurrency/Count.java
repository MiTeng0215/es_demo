package com.aegis.es_demo.highConcurrency;

/**
 * 现在有一个临界资源count=100,
 * 100个线程去争抢,保证每一个count不能被重复抢夺
 * 不枷锁的情况下,100个线程几乎获得的是同一个临界资源
 * 如果每次都在Thread中放入新的对象,意味着对每一次锁的是不同的对象
 * 如果是同一个对象,当一个线程获得该锁时,其他线程阻塞
 */
public class Count implements Runnable{
    int count =100;
    public static void main(String[] args) {
        Count count = new Count();
        for (int i=0;i<100;i++){
            new Thread(count).start();
        }
    }

    @Override
    public synchronized void run() {
        count--;
        System.out.println(Thread.currentThread().getName()+"---------"+(count));
    }
}
