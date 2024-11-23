package com.example.oauth.config;

import com.example.oauth.repository.UserRepository;
import com.example.oauth.service.SocialAppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity h) throws Exception {
        h.authorizeHttpRequests(a -> a.requestMatchers("/", "/login", "/error", "/webjars/**").permitAll().requestMatchers("/h2-console/**").permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/oauth2/**").permitAll().anyRequest().authenticated()).oauth2Login(oauth2 -> oauth2.loginPage("/").defaultSuccessUrl("/user")).logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID")).csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")).headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        return h.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(UserRepository ur) {
        return new SocialAppService(ur);
    }
}
