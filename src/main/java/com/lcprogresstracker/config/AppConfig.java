package com.lcprogresstracker.config;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public HttpPost getHttpPost(){
        HttpPost post = new HttpPost("https://leetcode.com/graphql");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Referer", "https://leetcode.com");
        return post;
    }
    @Bean
    public CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }
}
