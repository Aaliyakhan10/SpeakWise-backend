package com.doftec.SpeakWise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class LangToolConfig {

    @Bean
    RestClient restClientLangTool(){
        return RestClient.builder()
                .baseUrl("https://api.languagetool.org")
                .build();
    }
}
