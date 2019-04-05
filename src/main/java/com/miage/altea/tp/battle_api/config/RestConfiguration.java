package com.miage.altea.tp.battle_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestConfiguration {

    @Value("${trainers.service.user.name}")
    private String username;

    @Value("${trainers.service.user.password}")
    private String password;

    @Bean
    RestTemplate trainerApiRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new BasicAuthenticationInterceptor(this.username, this.password));
        interceptors.add((request, body, execution) -> {
            request.getHeaders().add("Accept-Language", LocaleContextHolder.getLocale().getLanguage());
            return execution.execute(request, body);
        });

        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
