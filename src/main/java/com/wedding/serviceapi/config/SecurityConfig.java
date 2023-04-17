package com.wedding.serviceapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.CustomServiceAuthenticationProvider;
import com.wedding.serviceapi.auth.CustomLoginAuthenticationManager;
import com.wedding.serviceapi.auth.CustomSocialLAuthenticationProvider;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.service.CustomServiceLoginUserDetails;
import com.wedding.serviceapi.auth.service.CustomSocialLoginUserDetails;
import com.wedding.serviceapi.filter.ServiceLoginFilter;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
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
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
                .addFilter(serviceLoginFilter());

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public ServiceLoginFilter serviceLoginFilter() {
        ServiceLoginFilter serviceLoginFilter = new ServiceLoginFilter(jwtUtil, objectMapper);
        serviceLoginFilter.setFilterProcessesUrl("/login/**");
        serviceLoginFilter.setAuthenticationManager(customLoginAuthenticationManager());
        return serviceLoginFilter;
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
