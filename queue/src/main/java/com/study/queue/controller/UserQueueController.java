package com.study.queue.controller;

import com.study.queue.dto.RegisterUserResponse;
import com.study.queue.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
@RestController
public class UserQueueController {

    private final UserQueueService userQueueService;

    @PostMapping("")
    public Mono<RegisterUserResponse>  registerUser(
        @RequestParam("user_id") final Long userId,
        @RequestParam(value = "queue", defaultValue = "default") final String  queue
    ) {
        return userQueueService.registerWaitQueue(queue, userId)
            .map(RegisterUserResponse::new);
    }
}
