package com.aegis.es_demo.highConcurrency;

public class DemoJoin extends Thread{
    @Override
    public void run() {
        System.out.println("hello");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(() ->{
            for (int i = 0;i<10;i++) {
                System.out.println(Thread.currentThread().getName()+"---启动了");
            }
        });
        Thread thread1 = new Thread(() ->{
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0; i<10; i++) {
                System.out.println(Thread.currentThread().getName()+"---启动了");
            }

        });
        thread.start();
        thread1.start();
    }
}
