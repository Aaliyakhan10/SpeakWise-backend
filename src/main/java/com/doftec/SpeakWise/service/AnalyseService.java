package com.doftec.SpeakWise.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class AnalyseService {
    private final RestClient restClient;
    public AnalyseService(RestClient restClientLangTool){
        this.restClient=restClientLangTool;
    }
    public String analyseTest(String speech) {

        if (speech == null || speech.trim().isEmpty()) {
            throw new IllegalArgumentException("Speech input cannot be empty");
        }
        System.out.println("Speech: " + speech);


            String body = "text=" + URLEncoder.encode(speech, StandardCharsets.UTF_8)
                    + "&language=" + URLEncoder.encode("en-US", StandardCharsets.UTF_8);

            return restClient
                    .post()
                    .uri("/v2/check")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        }

}
