//package com.china.soft.rest.controller.mq;
//
////import com.china.soft.commons.rocket.RocketMQProducer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.security.PermitAll;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//@PermitAll
//public class RocketController {
//
//
//    @Autowired
//    private RocketMQProducer rocketMQProducer;
//
//
//    @GetMapping("/concurrency")
//    public void concurrency() {
//        // 创建固定大小的线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(11);
//        executorService.submit(() -> {
//            for (int i = 0; i < 1000000; i++) {
//                rocketMQProducer.sendOneWayMessage("线程池中的线程: " + Thread.currentThread().getName() + " 打印: " + i);
//            }
//        });
//        // 关闭线程池
//        executorService.shutdown();
//        try {
//            // 等待所有任务完成
//            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executorService.shutdownNow();
//        }
//    }
//
//    @GetMapping("/sendSync")
//    public String sendSync(@RequestParam String message) {
//        rocketMQProducer.sendSyncMessage(message);
//        return "同步消息发送成功";
//    }
//
//    @GetMapping("/sendAsync")
//    public String sendAsync(@RequestParam String message) {
//        rocketMQProducer.sendAsyncMessage(message);
//        return "异步消息发送中";
//    }
//
//    @GetMapping("/sendOneWay")
//    public String sendOneWay(@RequestParam String message) {
//        rocketMQProducer.sendOneWayMessage(message);
//        return "单向消息发送成功";
//    }
//}