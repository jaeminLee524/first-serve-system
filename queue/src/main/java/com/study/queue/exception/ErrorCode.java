package com.study.queue.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {

     QUEUE_ALREADY_REGISTERD_USER(HttpStatus.CONFLICT, "uq-001", "alredy registered in queue"),
    ;

    private HttpStatus httpStatus;
    private String code;
    private String reason;

    public ApplicationException build() {
        return new ApplicationException(httpStatus, code, reason);
    }

    public ApplicationException build(Object... args) {
        return new ApplicationException(httpStatus, code, reason.formatted(args));
    }
}
