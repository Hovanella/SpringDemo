package com.example.demo.config;

import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig {

    private final JWTFilter jwtFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfig(JWTFilter jwtFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        return
                http.
                        cors().and().
                        csrf().disable().
                        authorizeRequests().
                        antMatchers("/authors/AddAuthor", "/genres/AddGenre", "/authors/GetAuthor/{name}",
                                "genres/GetGenre/{name}","authors/GetAllAuthors","genres/GetAllGenres","genres/GetGenre/{name}","/tracks/CopyTrackInAudio","/users/all").hasRole("ADMIN").
                        antMatchers("/tracks/TracksForUser/{login}", "/tracks/AddOrUpdateRating").hasAnyRole("USER", "ADMIN").
                        antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","/your-app-root/swagger-ui/**","/spring-security-rest/**",  "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**").permitAll().
                        antMatchers("/users/login", "/users/register").permitAll().
                        anyRequest().hasAnyRole("USER", "ADMIN").
                        and().
                        addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
                        authenticationManager(authenticationManager).
                        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                        and().
                        build();


    }



}

