package com.ruleshake.samples.back.configuration;

import com.ruleshake.referential.client.ReferentialClient;
import com.ruleshake.referential.client.config.EnableRuleShakeReferentialClient;
import com.ruleshake.runner.client.RunnerClient;
import com.ruleshake.runner.client.config.EnableRuleShakeRunnerClient;
import com.ruleshake.samples.back.service.AuthService;
import com.ruleshake.samples.back.service.DiscountService;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRuleShakeReferentialClient
@EnableRuleShakeRunnerClient
public class SamplesConfiguration {

    @Bean
    public AuthService authService(AuthProperties authProperties) {
        return new AuthService(authProperties);
    }

    @Bean
    public DiscountService discountService(AuthService authService, ReferentialClient referentialClient, RunnerClient runnerClient) {
        return new DiscountService(authService, referentialClient, runnerClient);
    }

    @Bean
    @ConfigurationProperties("ruleshake.auth")
    public AuthProperties authProperties() {
        return new AuthProperties();
    }

    @Data
    public static final class AuthProperties {

        private String url;
        private String audience;
        private String clientId;
        private String clientSecret;

    }

}
