package com.jbhunt.authvalidation.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class CustomerCredentials {

    @Id
    private String id;
    private String clientId;
    private String clientSecret;
}
