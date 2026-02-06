package com.china.soft.rest.controller.demo;

import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.security.PermitAll;

import java.time.Duration;

@RestController
@RequestMapping("/flux")
public class Demo5Controller {

    @GetMapping("/stream")
    @PermitAll
    public Flux<CommonResponse<String>> streamWords() {
        Flux<String> map = Flux.just("apple", "banana", "cherry", "date")
                .delayElements(Duration.ofSeconds(1)) // 模拟异步延迟
                .map(word -> word.toUpperCase());
        return map.map(word -> CommonResponse.success(word));
    }
}
