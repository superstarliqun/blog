package com.china.soft.rest.controller.demo;

import com.china.soft.dto.a.Company;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 场景题：同一时间可以访问多个企业，但是每个企业同一时间只能访问一次，如果需要访问两次需要等待第一次访问完，在进行访问第二次（一家公司只能先查询完A再访问接口查询B）
 * @author MingYue
 * @Date 2024/6/3
 */
public class Demo1Controller {

    // 用于存储公司ID是否存在锁
    private final ConcurrentHashMap<Integer, ReentrantLock> locks = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // 执行demo1
        Demo1Controller controller = new Demo1Controller();
        LinkedList<Company> list = new LinkedList<>();
        list.add(new Company(1,"A公司","A测试资源1"));
        list.add(new Company(1,"A公司","A测试资源2"));
        list.add(new Company(2,"B公司","B测试资源1"));
        list.add(new Company(3,"C公司","C测试资源1"));
        list.add(new Company(3,"C公司","c测试资源2"));
        list.add(new Company(3,"C公司","C测试资源3"));

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(list.size());
        // 创建CompletionService用于管理多线程任务
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        // 提交每个公司的任务
        for (Company company : list) {
            completionService.submit(() -> controller.demo1(company));
        }
        // 关闭线程池
        executorService.shutdown();
    }

    // 场景题：需要实现一个接口，这个接口可以访问公司的API,但是访问这个API同一时间只能访问一个，可以同时访问多家公司，但是同一时刻，一家公司只能先查询完A再访问接口查询B，怎么实现
    public String demo1(Company company) throws InterruptedException {
        // computeIfAbsent判断有没有指定键值，如果没有则创建一个ReentrantLock对象
        ReentrantLock lock = locks.computeIfAbsent(company.getSid(), k -> new ReentrantLock());
        lock.lock();
        try {
            // 用于模仿接口访问定时为1秒,如果
            Thread.sleep(1000);
            System.out.println("数据输出："+company.toString());
        } finally {
            // 接口访问完成之后需要进行释放锁
            lock.unlock();
        }
        return "访问接口正常";
    }


}
