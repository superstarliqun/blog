package com.china.soft.rest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
/**
 * @Description: SpringApplication主启动类
 * @author MingYue
 * @Date 2023/9/15
 */
@SpringBootApplication()
@ComponentScan(value = {"com.china","com.china.soft.commons"})
@MapperScan(basePackages = {"com.china.soft.*.provider.dal.mapper"})
@EnableScheduling // 开启定时任务
@EnableAsync
public class EmojingRestApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(EmojingRestApplication.class, args);

		// 打印自定义提示语
		System.out.println("\n========================================");
		System.out.println("🚀 项目启动成功！端口：8585");
		System.out.println("📢 访问文档：http://localhost:8585/doc.html");
		System.out.println("========================================\n");
	}

	/**
	 * 解决 Elasticsearch 和 Redis 底层的 Netty 启动冲突问题
	 */
	@PostConstruct
	public void init() {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	/**
	 * doc.html是在jar包里的，需要使用资源处理器注册静态资源
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
