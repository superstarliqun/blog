package com.china.soft.rest.controller.demo;

public class ThreadLocalDemo {

    // 创建一个ThreadLocal变量，每个线程都会有自己独立的变量副本
    private static ThreadLocal<Integer> threadLocalValue = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;  // 初始值为0
        }
    };

    public static void main(String[] args) {
        // 创建并启动三个线程
        Thread thread1 = new Thread(new Task(), "Thread-1");
        Thread thread2 = new Thread(new Task(), "Thread-2");
        Thread thread3 = new Thread(new Task(), "Thread-3");

        thread1.start();
        thread2.start();
        thread3.start();
    }

    // 定义一个任务，每个线程会执行这个任务
    static class Task implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                int currentValue = threadLocalValue.get();
                threadLocalValue.set(currentValue + 1);
                System.out.println(Thread.currentThread().getName() + " - Value: " + threadLocalValue.get());
                try {
                    Thread.sleep(100); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}