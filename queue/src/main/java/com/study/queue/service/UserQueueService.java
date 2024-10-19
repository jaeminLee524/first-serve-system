package com.study.queue.service;

import com.study.queue.exception.ErrorCode;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    public static final String USER_WAIT_QUEUE = "user:wait-queue:%s";
    public static final String USER_PROCEED_QUEUE = "user:proceed-queue:%s";
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public Mono<Long> registerWaitQueue(final String queueName, final Long userId) {
        long unixTimestamp = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().add(USER_WAIT_QUEUE.formatted(queueName), userId.toString(), unixTimestamp)
            .filter(result -> result)
            .switchIfEmpty(Mono.error(ErrorCode.QUEUE_ALREADY_REGISTERD_USER.build()))
            .flatMap(i -> reactiveRedisTemplate.opsForZSet().rank(USER_WAIT_QUEUE.formatted(queueName), userId.toString()))
            .map(i -> i >= 0 ? i + 1 : i);
    }

    public Mono<Long> allowUser(final String queue, final Long count) {
        return reactiveRedisTemplate.opsForZSet().popMin(USER_WAIT_QUEUE.formatted(queue), count)
            .flatMap(member -> reactiveRedisTemplate.opsForZSet().add(USER_PROCEED_QUEUE.formatted(queue), member.getValue(), Instant.now().getEpochSecond()))
            .count();
    }

    public Mono<Boolean> isAllowed(final String queue, final Long userId) {
        return reactiveRedisTemplate.opsForZSet().rank(USER_PROCEED_QUEUE.formatted(queue), userId.toString())
                .defaultIfEmpty(-1L)
                .map(rank -> rank >= 0);
    }
}
