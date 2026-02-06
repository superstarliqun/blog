package com.china.soft.rest.controller.mq;

public class NotifyExample {

    public static void main(String[] args) {

        SharedResource resource = new SharedResource();

        Thread producer = new Thread(new Producer(resource), "Producer");
        Thread consumer = new Thread(new Consumer(resource), "Consumer");

        producer.start();
        consumer.start();
    }

    static class SharedResource {
        private int data = 0;
        private boolean dataAvailable = false;

        public synchronized void setData(int data) {
            while (dataAvailable) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.data = data;
            dataAvailable = true;
            notify(); // 或者使用 notifyAll() 通知所有等待的线程
        }

        public synchronized int getData() {
            while (!dataAvailable) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int result = data;
            dataAvailable = false;
            notify(); // 或者使用 notifyAll() 通知所有等待的线程
            return result;
        }
    }

    static class Producer implements Runnable {
        private final SharedResource resource;

        public Producer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                resource.setData(i);
                System.out.println("Producer set data: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final SharedResource resource;

        public Consumer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int data = resource.getData();
                System.out.println("Consumer got data: " + data);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}