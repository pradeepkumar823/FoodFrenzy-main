package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

        @Bean
        public SecurityContextRepository securityContextRepository() {
                return new HttpSessionSecurityContextRepository();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityContext(context -> context
                                                .securityContextRepository(securityContextRepository()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/",
                                                                "/home",
                                                                "/product",
                                                                "/location",
                                                                "/about",
                                                                "/login",
                                                                "/register",
                                                                "/products",
                                                                "/profile",
                                                                "/logout",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**",
                                                                "/verify",
                                                                "/product/search")
                                                .permitAll() // Allow public access
                                                .requestMatchers("/userLogin", "/adminLogin", "/product/order")
                                                .permitAll()// allow manual
                                                .requestMatchers("/admin/services", "/admin/**").permitAll()
                                                .anyRequest().authenticated())

                                .csrf(csrf -> csrf.disable())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .successHandler(oauth2LoginSuccessHandler))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/home")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
