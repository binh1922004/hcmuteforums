package com.backend.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINT = {"/api/users", "/api/auth/login", "/api/otp/get", "/api/otp/validate",
    "/api/categories/**", "/api/subcategory/**", "/api/auth/introspect", "/ws", "/api/otp/getOTPResetPassword", "/api/otp/validatedOTP"
    ,"/api/users/updatePassword", "/api/chatbot/**"};
    private final String[] PUBLIC_ENDPOINT_GET = {"/api/topics/**", "/upload/**", "/api/reply/**", "/api/follow/**"
    ,"/api/users/personInfo","/api/profiles/personProfile","/api/follow/check", "/api/search/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, CustomJwtDecoder customJwtDecoder) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT_GET).permitAll()
                        .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        //cai nay tu bat nen phai ta
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    @Bean
    public CustomJwtDecoder jwtDecoder() {
        return new CustomJwtDecoder();
    }
}
