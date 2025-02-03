package com.wn.wandernest.configs;

import org.springframework.context.annotation.Configuration;

import lombok.Data;
@Data
@Configuration
public class AccommodationApiConfig {
    private String baseUrl = "https://3aa03863-2530-4d5a-8bc0-3391189c2bb9.mock.pstmn.io";//postman mock server
    private String apiKey = "example";
    private int maxRetries = 1;
}
