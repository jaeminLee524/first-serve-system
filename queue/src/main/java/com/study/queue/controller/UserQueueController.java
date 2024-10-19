package com.study.queue.controller;

import com.study.queue.dto.AllowUserResponse;
import com.study.queue.dto.AllowedUserResponse;
import com.study.queue.dto.RegisterUserResponse;
import com.study.queue.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
@RestController
public class UserQueueController {

    private final UserQueueService userQueueService;

    @PostMapping("")
    public Mono<RegisterUserResponse> registerUser(
            @RequestParam("user_id") final Long userId,
            @RequestParam(value = "queue", defaultValue = "default") final String queue
    ) {
        return userQueueService.registerWaitQueue(queue, userId)
                .map(RegisterUserResponse::new);
    }

    @PostMapping("/allow")
    public Mono<AllowUserResponse> allowUser(
            @RequestParam(value = "queue", defaultValue = "default") final String queue,
            @RequestParam(value = "count", defaultValue = "1") final Long count
    ) {
        return userQueueService.allowUser(queue, count)
                .map(allowCount -> AllowUserResponse.of(count, allowCount));
    }

     @GetMapping("/allowed")
    public Mono<?> isAllowedUser(
            @RequestParam(name = "queue", defaultValue = "default") final String queue,
            @RequestParam(name = "user_id") final Long userId
     ) {
        return userQueueService.isAllowed(queue, userId)
                .map(AllowedUserResponse::new);
     }
}
