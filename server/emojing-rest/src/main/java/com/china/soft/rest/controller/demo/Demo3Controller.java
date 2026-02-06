package com.china.soft.rest.controller.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo3Controller {

    public static void main(String[] args) {
        // 在静态方法中不允许直接调用类中非静态类方法，静态方法属于类本身，不属于某一个特定的实例，说白了就是不需要创建类的实例就可以调用静态方法，否则只能通过new实例的形式进行调用
        Demo3Controller demo = new Demo3Controller();
        demo.demo01();
        demo.demo02();
    }

    public void demo01(){
        // 创建一个数组，通过迭代器的形式进行输出
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        Iterator<Integer> list = integers.iterator();
        while (list.hasNext()) {
            System.out.println("这是数组中的参数："+list.next());
        }
    }

    public void demo02(){
        // 有学生表，和成绩表，查询学生平均成绩在80分以上的学生
        String sql = "SELECT * FROM student LEFT JOIN course on student.id = course.id GROUP BY student.id having avg(course.corse) > 80";
        System.out.println(sql);
    }

    public void demo3(){
        // 运用分段锁，用于高并发的场景中，可已在高并发的情况下同时在不同分段中进行读取和写入，互不影响,只有在同时访问同意分段下的时候再回用锁
        ConcurrentHashMap<String, String> currentMap = new ConcurrentHashMap<>();
        System.out.println(currentMap);
        String abc = "";
    }


     public void demo4(){
         ExecutorService threadPool = Executors.newFixedThreadPool(3);
         threadPool.execute(()->{
             System.out.println("任务被执行,线程:" + Thread.currentThread().getName());
         });
     }

}
