package com.jbhunt.authvalidation.dto;

import lombok.Data;

@Data
public class CustomerAuthDTO {
    private String clientId;
    private String clientSecret;
    private String token;
}
