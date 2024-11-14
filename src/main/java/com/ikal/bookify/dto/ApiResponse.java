package com.ikal.bookify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String status;
    private String message;
    private Object data;
    private String errorCode;
    private String details;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String status, String message, String errorCode, String details) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
    }
}
