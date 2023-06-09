package com.interview.interviewservice.config;


import com.interview.interviewservice.controller.AuthenticationController;
import com.interview.interviewservice.controller.CandidateController;
import com.interview.interviewservice.controller.CompanyController;
import com.interview.interviewservice.controller.UserController;
import com.interview.interviewservice.jwt.JwtAuthenticationEntryPoint;
import com.interview.interviewservice.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomDetailService customDetailService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtRequestFilter authenticationJwtTokenFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // We don't need CSRF for this example
        http.cors().and().csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests()
                .requestMatchers(HttpMethod.POST, AuthenticationController.BASE_URL + "login").permitAll()
                .requestMatchers(HttpMethod.PATCH, AuthenticationController.BASE_URL + "verify").permitAll()
                .requestMatchers(HttpMethod.POST, AuthenticationController.BASE_URL + "reset-password").permitAll()
                .requestMatchers(HttpMethod.POST, AuthenticationController.BASE_URL + "forgot").permitAll()
                .requestMatchers(HttpMethod.POST, AuthenticationController.BASE_URL + "reset").permitAll()
                .requestMatchers(HttpMethod.POST, CompanyController.BASE_URL + "save").permitAll()
                .requestMatchers(HttpMethod.POST, CandidateController.BASE_URL + "save").permitAll()
                .requestMatchers(HttpMethod.POST, UserController.BASE_URL).permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public FilterRegistrationBean corsFilterRegistration(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200","https://courageous-piroshki-f9154b.netlify.app/"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**",configuration);

        FilterRegistrationBean filter = new FilterRegistrationBean(new CorsFilter(source));
        filter.setOrder(0);
        return filter;
    }


}
