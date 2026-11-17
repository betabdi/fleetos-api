package com.fleetos.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {
 @Bean SecurityFilterChain security(HttpSecurity http,ApiKeyAuthenticationFilter apiKey) throws Exception {
  return http.csrf(csrf->csrf.disable()).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .exceptionHandling(errors->errors.authenticationEntryPoint((request,response,e)->response.sendError(401,"Valid FleetOS API key required"))
    .accessDeniedHandler((request,response,e)->response.sendError(403,"Role does not permit this operation")))
   .authorizeHttpRequests(auth->auth
    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.GET,"/api/**").hasAnyRole("ADMIN","DISPATCHER","VIEWER")
    .requestMatchers("/api/**").hasAnyRole("ADMIN","DISPATCHER")
    .anyRequest().denyAll())
   .addFilterBefore(apiKey,UsernamePasswordAuthenticationFilter.class).build();
 }
}
