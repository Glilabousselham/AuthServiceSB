package org.glila.auth.config.security;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf((CsrfConfigurer<HttpSecurity> t) -> t.disable())
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(HttpMethod.POST, "/users").permitAll();
                    requests.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    requests.requestMatchers(HttpMethod.POST, "/auth/check").permitAll();

                    requests.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthenticatorFilter(userDetailsService()), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement((SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

        return httpSecurity.build();
    }

    /**
     * the password encoder used to encode user passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    // disable the session
    @Bean
    public ServletContextInitializer preventSessionCreation() {
        return servletContext -> servletContext.setSessionTrackingModes(Collections.emptySet());
    }
}
