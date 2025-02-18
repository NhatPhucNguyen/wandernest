package com.wn.wandernest.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    @Value("${google.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping()
    public ResponseEntity<ByteArrayResource> getMapPhoto(@RequestParam String photoReference) {
        try {
            String googlePhotoUrl = "https://3aa03863-2530-4d5a-8bc0-3391189c2bb9.mock.pstmn.io" +
                    "?maxwidth=400&photo_reference=" + photoReference +
                    "&key=" + apiKey;
            byte[] imageBytes = restTemplate.getForObject(URI.create(googlePhotoUrl), byte[].class);
            if (imageBytes == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(new ByteArrayResource(imageBytes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
