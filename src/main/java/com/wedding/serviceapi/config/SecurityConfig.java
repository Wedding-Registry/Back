package com.wedding.serviceapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.filter.CustomAuthorizationFilter;
import com.wedding.serviceapi.auth.securitycustom.CustomServiceAuthenticationProvider;
import com.wedding.serviceapi.auth.securitycustom.CustomLoginAuthenticationManager;
import com.wedding.serviceapi.auth.securitycustom.CustomSocialAuthenticationProvider;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.service.CustomServiceLoginUserDetails;
import com.wedding.serviceapi.auth.service.CustomSocialLoginUserDetails;
import com.wedding.serviceapi.filter.CustomAuthenticationFilter;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;

    private final String SERVICE_LOGIN_URI = "/login/service";
    private final String SOCIAL_LOGIN_KAKAO_URI = "/login/oauth/kakao";
    private final String SOCIAL_LOGIN_GOOGLE_URI = "/login/oauth/google";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login/**", "/auth/signup", "/auth/social/info").permitAll()
                .anyRequest().hasAuthority("USER")
                .and()
                .cors()
                .and()
                .addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
                .addFilter(serviceLoginFilter())
                .addFilterBefore(customJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CustomAuthorizationFilter customJwtAuthenticationFilter() {
        return new CustomAuthorizationFilter(jwtUtil, objectMapper);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public CustomAuthenticationFilter serviceLoginFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(jwtUtil, objectMapper);
        customAuthenticationFilter.setFilterProcessesUrl("/login/**");
        customAuthenticationFilter.setAuthenticationManager(customLoginAuthenticationManager());
        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager customLoginAuthenticationManager() {
        Map<String, AuthenticationProvider> authenticationProviderMap = Map.of(SERVICE_LOGIN_URI, customServiceAuthenticationProvider(),
                SOCIAL_LOGIN_KAKAO_URI, customSocialAuthenticationProvider(),
                SOCIAL_LOGIN_GOOGLE_URI, customSocialAuthenticationProvider()
        );
        return new CustomLoginAuthenticationManager(authenticationProviderMap);
    }

    @Bean
    public AuthenticationProvider customServiceAuthenticationProvider() {
        return new CustomServiceAuthenticationProvider(customServiceLoginUserDetails(), passwordEncoder());
    }

    @Bean
    public CustomServiceLoginUserDetails customServiceLoginUserDetails() {
        return new CustomServiceLoginUserDetails(usersRepository, boardsRepository);
    }

    @Bean
    public AuthenticationProvider customSocialAuthenticationProvider() {
        return new CustomSocialAuthenticationProvider(customSocialLoginUserDetails());
    }

    @Bean
    public CustomSocialLoginUserDetails customSocialLoginUserDetails() {
        return new CustomSocialLoginUserDetails(usersRepository, boardsRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
