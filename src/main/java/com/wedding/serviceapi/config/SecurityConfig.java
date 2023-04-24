package com.wedding.serviceapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.CustomJwtAuthenticationFilter;
import com.wedding.serviceapi.auth.CustomServiceAuthenticationProvider;
import com.wedding.serviceapi.auth.CustomLoginAuthenticationManager;
import com.wedding.serviceapi.auth.CustomSocialLAuthenticationProvider;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.service.CustomServiceLoginUserDetails;
import com.wedding.serviceapi.auth.service.CustomSocialLoginUserDetails;
import com.wedding.serviceapi.filter.CustomAuthenticationFilter;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors()
                .and()
                .addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
                .addFilter(serviceLoginFilter())
                .addFilterAfter(customJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CustomJwtAuthenticationFilter customJwtAuthenticationFilter() {
        return new CustomJwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
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
    public CustomLoginAuthenticationManager customLoginAuthenticationManager() {
        return new CustomLoginAuthenticationManager(new ArrayList<>(
                List.of(customServiceAuthenticationProvider(), customSocialAuthenticationProvider())
        ));
    }

    @Bean
    public AuthenticationProvider customServiceAuthenticationProvider() {
        return new CustomServiceAuthenticationProvider(customServiceLoginUserDetails(), passwordEncoder());
    }

    @Bean
    public CustomServiceLoginUserDetails customServiceLoginUserDetails() {
        return new CustomServiceLoginUserDetails(usersRepository);
    }

    @Bean
    public AuthenticationProvider customSocialAuthenticationProvider() {
        return new CustomSocialLAuthenticationProvider(customSocialLoginUserDetails());
    }

    @Bean
    public CustomSocialLoginUserDetails customSocialLoginUserDetails() {
        return new CustomSocialLoginUserDetails(usersRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
