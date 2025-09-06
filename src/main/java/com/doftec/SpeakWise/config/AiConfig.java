package com.doftec.SpeakWise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {
    public final String apikey;

    public AiConfig(@Value("${OPEN_ROUTER_API}") String apikey) {
        this.apikey = apikey;
    }
    @Bean
    RestClient restClientAi(){
        return RestClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader("Authorization", "Bearer " + apikey)
                .build();

    }

}
