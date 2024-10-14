package com.study.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@RequiredArgsConstructor
@SpringBootApplication
public class QueueApplication implements ApplicationListener<ApplicationReadyEvent> {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        reactiveRedisTemplate.opsForValue().set("key", "test").subscribe();
    }
}
