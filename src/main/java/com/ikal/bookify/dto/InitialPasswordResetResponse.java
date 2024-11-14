package com.ikal.bookify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitialPasswordResetResponse {
    private String token;
}
