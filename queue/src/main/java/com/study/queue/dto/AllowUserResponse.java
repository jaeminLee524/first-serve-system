package com.study.queue.dto;

public record AllowUserResponse(
        Long requestCount,
        Long allowedcount
) {
    public static AllowUserResponse of(Long count, Long longMono) {
        return new AllowUserResponse(count, longMono);
    }
}
