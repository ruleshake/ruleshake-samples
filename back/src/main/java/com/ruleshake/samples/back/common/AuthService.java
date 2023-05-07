package com.ruleshake.samples.back.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.ruleshake.samples.back.configuration.SamplesConfiguration;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

public class AuthService {

    private final WebClient webClient;
    private final AsyncCache<String, String> tokenCache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.of(43200, ChronoUnit.SECONDS))
        .buildAsync();

    public AuthService(SamplesConfiguration.AuthProperties authProperties) {
        this.webClient = WebClient.builder()
            .baseUrl(authProperties.getUrl())
            .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest ->
                Mono.just(new M2MRequest(authProperties.getClientId(), authProperties.getClientSecret(), authProperties.getAudience(), "client_credentials"))
                    .map(body -> ClientRequest.from(clientRequest).body(Mono.just(body), M2MRequest.class).build())
            ))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Mono<String> getToken() {
        CompletableFuture<String> token = tokenCache.getIfPresent("token");
        if (token != null) {
            return Mono.fromFuture(token);
        }
        return webClient.post()
            .retrieve()
            .bodyToMono(M2MResponse.class)
            .map(M2MResponse::accessToken)
            .doOnNext(newToken -> tokenCache.put("token", CompletableFuture.completedFuture(newToken)));
    }

    public Mono<Context> getSecurityContext() {
        return getToken()
            .map(token -> ReactiveSecurityContextHolder.withAuthentication(new JwtAuthenticationToken(parse(token))));
    }

    @SneakyThrows
    private Jwt parse(String accessToken) {
        JWT jwt = JWTParser.parse(accessToken);
        return new Jwt(
            accessToken,
            jwt.getJWTClaimsSet().getIssueTime().toInstant(),
            jwt.getJWTClaimsSet().getExpirationTime().toInstant(),
            jwt.getHeader().toJSONObject(),
            jwt.getJWTClaimsSet().toJSONObject()
        );
    }

    public record M2MRequest(@JsonProperty("client_id") String clientId,
                             @JsonProperty("client_secret") String clientSecret,
                             @JsonProperty("audience") String audience,
                             @JsonProperty("grant_type") String grantType) {

    }

    public record M2MResponse(@JsonProperty("access_token") String accessToken) {

    }

}
