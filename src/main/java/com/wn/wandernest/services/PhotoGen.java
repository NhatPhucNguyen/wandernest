package com.wn.wandernest.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wn.wandernest.configs.PlacesApiConfig;
import com.wn.wandernest.dtos.PhotoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhotoGen {
    private final RestTemplate restTemplate;
    private final PlacesApiConfig config;
    public String generatePhotoUri(String photoName) {
        try {
            String url = String.format(
                    "https://places.googleapis.com/v1/%s/media?key=%s&maxHeightPx=800&maxWidthPx=800&skipHttpRedirect=true",
                    photoName,config.getApiKey());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "text/plain");

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<PhotoResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    PhotoResponse.class);
            if (response.getBody() != null) {
                PhotoResponse body = response.getBody();
                if (body != null) {
                    return body.getPhotoUri();
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
