package com.tcs.microservices.cuentas_movimientos.util.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private List<String> details;
    private String path;
    private String method;
    private ExceptionDetail exception;
    private String userMessage;

    @Getter
    @Builder
    public static class ExceptionDetail {
        private String type;
        private StackTraceElement[] trace;
    }
}