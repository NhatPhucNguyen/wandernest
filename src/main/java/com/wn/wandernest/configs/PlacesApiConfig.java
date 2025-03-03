package com.wn.wandernest.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class PlacesApiConfig {
    @Value("https://3aa03863-2530-4d5a-8bc0-3391189c2bb9.mock.pstmn.io")
    private String baseUrl;
    @Value("${GOOGLE_KEY}")
    private String apiKey;
    //TODO: Implement google photo
    // @Value("${GOOGLE_ACCESS_KEY}")
    // private String accessKey;
    private int maxRetries = 1;
}
